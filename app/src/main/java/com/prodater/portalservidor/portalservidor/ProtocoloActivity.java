package com.prodater.portalservidor.portalservidor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ProtocoloActivity extends AppCompatActivity {


    Button btnPesquisarProtocolo;
    EditText protocolo;
    EditText senhaProtocolo;
    String resultado;
    ProgressDialog esperaCarregarProtocolo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocolo);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide(); // Esconde a ActionBar
        InicializaBotoes();

    }


    private void InicializaBotoes(){

        btnPesquisarProtocolo = (Button)findViewById(R.id.botaopesquisarprotocolo);
        protocolo             = (EditText)findViewById(R.id.protocolo_protocolo);
        senhaProtocolo        = (EditText)findViewById(R.id.senhaprotocolo);


        btnPesquisarProtocolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(protocolo.getText().toString().equals("") || senhaProtocolo.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Você precisa preencher todos os campos", Toast.LENGTH_LONG).show();
                }else {

                    try {
                        EsperaRequisicao();
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {

                                    makePostRequest(protocolo.getText().toString(), senhaProtocolo.getText().toString());
                                    // makePostRequest("043802122015", "dde73cf7"); // Teste para debugar
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            Toast.makeText(getApplicationContext(), "Não foi possível conectar. Tente novamente", Toast.LENGTH_LONG).show();
                                            esperaCarregarProtocolo.dismiss();
                                        }
                                    });
                                }
                            }
                        };

                        thread.start();

                    } catch (Exception e) {
                        Log.d("DEU ERRO", e.getMessage());
                        esperaCarregarProtocolo.dismiss();
                        Toast.makeText(getApplicationContext(), "Erro ao conectar, verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
                    }
                }

            }

        });


    }


    private void makePostRequest(String protocolo, String senha) {


        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setIntParameter("http.socket.timeout", 10000); // limita caso a conexão esteja lenta
            client.getParams().setIntParameter("http.connection.timeout", 4000); // limita caso a conexão esteja lenta
            String postURL ="http://protocolo.teresina.pi.gov.br/consultar_mobile.php"; //"http://www.teresina.pi.gov.br/sistemas/protocolo/consultar_mobile.php";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("processo", protocolo));  // parametros passado pelo post
            params.add(new BasicNameValuePair("senha", senha));                 // mesmo acima
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            resultado = EntityUtils.toString(resEntity);
            resultado = resultado.trim(); // limpa espaços no começo e final da string

            Bundle bundle = new Bundle();
            String enviarTexto = resultado;
            bundle.putString("message", enviarTexto);
            Fragmento_protocolo dialogo = new Fragmento_protocolo();
            dialogo.setArguments(bundle);
            dialogo.show(getFragmentManager(), "VisualizaProtocolo");

            runOnUiThread(new Runnable() {
                public void run() {

                    esperaCarregarProtocolo.dismiss();
                }
            });


            Log.d("Resposta", resultado);
        }catch(Exception e){
            runOnUiThread(new Runnable() {
                public void run() {

                    esperaCarregarProtocolo.dismiss();
                    Toast.makeText(getApplicationContext(), "Erro ao conectar, verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
                }
            });
            Log.d("DEU ERRO", e.getMessage());
        }

    }



    public void EsperaRequisicao (){


        esperaCarregarProtocolo = new ProgressDialog(ProtocoloActivity.this);
        esperaCarregarProtocolo.setCancelable(true);
        esperaCarregarProtocolo.setMessage("Aguarde um momento...");
        esperaCarregarProtocolo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        esperaCarregarProtocolo.show();


    }


}
