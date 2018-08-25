package com.prodater.portalservidor.portalservidor;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;

public class ContrachequeActivity extends AppCompatActivity {
    String mesSeparado[];
    int mesSelecionado;
    ImageButton pesquisar;
    ImageButton limpar;
    ImageButton esqueceu_senha;
   public static EditText matricula;
   public static EditText senha;
    Spinner  ano;
    Spinner  mes;
    String ano_contracheque_arquivo;
    String mes_contracheque_arquivo;
    String nomeGeralDoArquivo;
    public static String passaImeiParaRecupera; // Enviado para o fragment, pois o mesmo não suporta a inicialização de um serviço
    public static String resposta1;
    ProgressDialog carregaMes = null;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracheque);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide(); // Esconde a ActionBar
        passaImeiParaRecupera = PegaIMEI();

        PegaAnoAtual();
        mes       =  (Spinner)findViewById(R.id.SpinnerMes);


        // Seta as variáveis de Spinner
        //Spinner AnoSpinner = (Spinner) findViewById(R.id.SpinnerAno);
        //Spinner MesSpinner = (Spinner) findViewById(R.id.SpinnerMes);

        // ################## Cria o adaptador que preenche o Spinner do Mes
        //ArrayAdapter adaptadorMes = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, Mes);
        //mes.setAdapter(adaptadorMes);
        // ################# Fim Spinner Ano

        CarregaButoes();

    }


    public void CarregaButoes() {
        // Inicia variáveis dos elementos do layout
        pesquisar = (ImageButton) findViewById(R.id.btn_pesquisar);
        limpar =    (ImageButton) findViewById(R.id.btn_limpar);
        matricula =  (EditText)findViewById(R.id.matricula_contracheque);
        senha     =  (EditText)findViewById(R.id.senha_contracheque);
        ano       =  (Spinner)findViewById(R.id.SpinnerAno);
       // mes       =  (Spinner)findViewById(R.id.SpinnerMes);

            // Escuta o botao pesquisar
        pesquisar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    /// Validação dos campos
                if (matricula.getText().toString().equals("") || senha.getText().toString().equals("") || ano.getSelectedItem().toString().equals("")|| mes.getSelectedItemPosition() < 0){
                   Toast.makeText(getApplicationContext(), "Você precisa preencher todos os campos",Toast.LENGTH_LONG).show();

                }else{
                        //mostra os campos que foram digitados
                    //Toast.makeText(getApplicationContext(),matricula.getText().toString()+ " "+senha.getText().toString()+" "+ano.getSelectedItem().toString()+" "+mes.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
                    startDownload();

                }

            /////////////########################### FIM RECUPERAR CLICK #############################
            }

        });

        limpar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EsqueceuSenhaContracheque dialog = new EsqueceuSenhaContracheque();
                dialog.show(getFragmentManager(),"EsqueceuSenha");

            }

        });

        // Pega o ano selecionado no Spinner
        ano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList limpaSpinner = new ArrayList();
                limpaSpinner.clear();
                mes.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, limpaSpinner));
                //parent.getSelectedItem().toString();
                //if (position > 0) {
                    //Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    EsperaCarregarMes();
                    PegaHtmlMes(parent.getSelectedItem().toString());

                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                mesSelecionado = mes.getSelectedItemPosition() + 1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }
        });


        // Campo matricula
        matricula.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            // OnTyping do campo texto matricula
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(matricula.length() > 6){
                   matricula.setText(matricula.getText().toString().substring(0, matricula.length() - 1));
                   matricula.setSelection(matricula.length());
               }

            }
        });




        // Campo senha
        senha.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            // OnTyping do campo texto matricula
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (senha.length() > 8) {
                    senha.setText(senha.getText().toString().substring(0, senha.length() - 1));
                    senha.setSelection(senha.length());
                }

            }
        });



    }

    // Carrega o Calendário. Chamado quando clica no botão de Data de Nascimento
    // Função setada no xml "esqueceu_senha"
public void BotaoNascimento(View view){
    DialogFragment picker = new DataCalendario();
    picker.show(getFragmentManager(), "datePicker");

}
        //Função de iniciar download
    private void startDownload() {
        String imei = PegaIMEI(); // Captura o imei do celular
            ano_contracheque_arquivo = ano.getSelectedItem().toString();
            mes_contracheque_arquivo =  mes.getSelectedItem().toString();
        String url = "http://contracheque.teresina.pi.gov.br/contracheque/emitir_movel.php?i="+imei+"&m="+matricula.getText().toString()+"&s="+senha.getText().toString()+"&a="+ano.getSelectedItem().toString()+"&r="+ mesSelecionado; // url de requisicao
        //Log.d("aaaaaaaa",url);
        new DownloadFileAsync().execute(url);  // Cria a instancia que vai baixar o arquivo (Contracheque)
    }

    // Cria a caixa de dialogo que conta a porcentagem do arquivo baixado
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Baixando Contracheque..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    // Passa o processo para um thread secundária e libera a thread principal (UI)
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Tamanho do arquivo: " + lenghtOfFile); // Tamanho total do arquivo

                if (lenghtOfFile > 100) {


                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream("/sdcard/contracheque_" + mes_contracheque_arquivo + "_" + ano_contracheque_arquivo + ".pdf"); // nome do arquivo de saída
                    nomeGeralDoArquivo = "contracheque_" +mes_contracheque_arquivo + "_" + ano_contracheque_arquivo + ".pdf";

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    AbrirLeitorPDF(nomeGeralDoArquivo);
                }else{
                    // Roda o Toast na Thread principal UI
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Dados incorretos ou o seu contracheque ainda não foi emitido pelo orgão.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                // Roda o Toast na Thread principal UI
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(), "Não foi possível efetuar o download do seu contracheque. Por favor, check sua conexão com a internet.", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;


        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

    }

    // Pega o IMEI do celular
    public String PegaIMEI(){
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
        return device_id;
    }

    //Abre o PDF através de uma intent
    public void AbrirLeitorPDF (String nomePDF){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+nomePDF);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }



   public void PegaHtmlMes(String ano_c) {
        class OneShotTask implements Runnable {
            String ano_c;
            OneShotTask(String s) { ano_c = s; }
            public void run() {
                //#########
                try {

                    // Constroi e seta o tempo limite de resposta da requisicao
                    String url_contracheque = "http://contracheque.teresina.pi.gov.br/contracheque/mes_movel.php?ano=";
                    URLConnection connection = (new URL(url_contracheque + ano_c)).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();

                    // Ler de guarda o resultado do html em uma string
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder html = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null; ) {
                        html.append(line);
                    }
                    in.close();
                    resposta1 = html.toString();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            mesSeparado = resposta1.split("\\;");
                            // Testar se a resposta ta vindo em branco, pra jogar o toast, caso o 2016 nao estiver contracheque ainda
                            CarregaMesesDinamico();
                            //Toast.makeText(getApplicationContext(),mesSeparado[13], Toast.LENGTH_SHORT).show();
                            carregaMes.dismiss();
                        }
                    });

                } catch (Exception ex) { // Caso esteja sem conexão

                    resposta1 = "Verifique sua conexão com a internet";

                    runOnUiThread(new Runnable() { // Roda o toast na thread principal
                        public void run() {
                            Toast.makeText(getApplicationContext(), resposta1, Toast.LENGTH_SHORT).show();
                            carregaMes.dismiss();
                            resposta1 = "";
                        }
                    });
                }

                //#########
            }
        }
        Thread t = new Thread(new OneShotTask(ano_c)); // Cria o objeto da thread com parametro
        t.start();
    }


        // Pega o ano atual do calendário. Ano mínimo para o contracheque é 2011
    public void PegaAnoAtual (){
        Calendar calendario = Calendar.getInstance();
        int anoAtual = calendario.get(Calendar.YEAR);
        Spinner AnoSpinner = (Spinner) findViewById(R.id.SpinnerAno);
        ArrayList<String> valor = new ArrayList();
        for (int i = anoAtual; i >= 2011; i-- ){
            valor.add(Integer.toString(i));

        }

        ArrayAdapter adaptadorAno = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,valor);
        AnoSpinner.setAdapter(adaptadorAno);

    }

    public void EsperaCarregarMes (){


                carregaMes = new ProgressDialog(ContrachequeActivity.this);
                carregaMes.setCancelable(true);
                carregaMes.setMessage("Aguarde um momento...");
                carregaMes.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                carregaMes.show();


    }
        // Carrega os meses Dinâmico para o Spinner
    public void CarregaMesesDinamico() {

        ArrayList<String> valorMes = new ArrayList();
        for (int j = 0; j <= mesSeparado.length - 1; j++ ){

            valorMes.add(RetornaMesesAno(j));

        }

        ArrayAdapter adaptadorMes = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, valorMes);
        mes.setAdapter(adaptadorMes);
    }

    // Retorna os Meses para ser colocado dentro do Spinner
    public String RetornaMesesAno (int indexMes ){
        String Mes[] = new String[14];
        Mes[0]  ="Janeiro";
        Mes[1]  ="Fevereiro";
        Mes[2]  ="Março";
        Mes[3]  ="Abril";
        Mes[4]  ="Maio";
        Mes[5]  ="Junho";
        Mes[6]  ="Julho";
        Mes[7]  ="Agosto";
        Mes[8]  ="Setembro";
        Mes[9] = "Outubro";
        Mes[10] = "Novembro";
        Mes[11] = "Dezembro";
        Mes[12] = "Décimo Terceiro";

        for(int k = 0; k<=Mes.length-1; k++){
            if(indexMes == k){
                return Mes[k];
            }

        }
        return null;
    }





}
