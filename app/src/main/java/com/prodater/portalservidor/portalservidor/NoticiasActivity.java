package com.prodater.portalservidor.portalservidor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

public class NoticiasActivity extends AppCompatActivity {

    WebView web;
    ProgressDialog esperaNoticia = null;
    ImageButton botaoCompartilha;
    String urlControleCompartilhamento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        getSupportActionBar().hide(); // Esconde a ActionBar

        ListenerBotoes();
        //Iniciando o webview
        web = (WebView)findViewById(R.id.navegador_noticias);
        web.getSettings().setJavaScriptEnabled(true);
        web.setBackgroundColor(0);
        web.loadUrl("http://portalpmt.teresina.pi.gov.br/mobile/blog_lista.php");
        web.setWebViewClient(new WebViewClient(){

            //Abre a requisição na mesma janela
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
                //Quando a página começar a carregar
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("c","pagestart");
                EsperaCarregarNoticia();
            }

            // Quando a página terminar de carregar
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("t","pagestart");
                esperaNoticia.dismiss();
                urlControleCompartilhamento = url;
                botaoCompartilha.setVisibility(View.VISIBLE);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Toast.makeText(NoticiasActivity.this,"Não foi possível carregar, verifique sua conexão.", Toast.LENGTH_LONG).show();
            }

        });

        //setContentView(navega);


    }
        // Testar com API's antiga.
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("TESTE", "back button pressed");
        }
        return super.onKeyDown(keyCode, event);

    }
*/

        @Override
        public void onBackPressed()
        {
            if(web.canGoBack()){
                web.goBack();
            }else{
                new AlertDialog.Builder(NoticiasActivity.this)
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setTitle("Notícias PMT")
                        .setMessage("Você deseja retornar a tela principal?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        }



    public void EsperaCarregarNoticia (){

        esperaNoticia = new ProgressDialog(NoticiasActivity.this);
        esperaNoticia.setCancelable(true);
        esperaNoticia.setMessage("Aguarde um momento...");
        esperaNoticia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        esperaNoticia.show();


    }


    //Ouve o botão de compartilhamento de notícias
    public void ListenerBotoes (){
        botaoCompartilha = (ImageButton)findViewById(R.id.compartilhar_botao);
        botaoCompartilha.setVisibility(View.INVISIBLE);

        botaoCompartilha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (urlControleCompartilhamento.equals("http://portalpmt.teresina.pi.gov.br/mobile/blog_lista.php")){
                    Toast.makeText(NoticiasActivity.this, "Você precisa carregar uma notícia antes de compartilhar.",Toast.LENGTH_LONG).show();
                }else{
                    CompartilhaUrl();
                }

            }

        });
    }

    // Método que compartilha link ou texto
    private void CompartilhaUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Adiciona os dados para a intent
        share.putExtra(Intent.EXTRA_SUBJECT, "Compartilhar");
        share.putExtra(Intent.EXTRA_TEXT, urlControleCompartilhamento);

        startActivity(Intent.createChooser(share, "Compartilhe essa notícia!"));
    }


}


