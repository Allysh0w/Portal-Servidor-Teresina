package com.prodater.portalservidor.portalservidor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Javac on 13/01/2016.
 */
public class EsqueceuSenhaContracheque extends DialogFragment {

    public static String respostaDaRequisicao;
    //Precisa ser static para que possa ser acessado de outro fragment
    public static Button botaoNascimento;
    public static EditText esqueceuMatricula;
    public static EditText esqueceuCPf;
    public static String senhaRecuperada;
    String imeiRecuperar;
    ProgressDialog esperaCarregar = null;
    Context ContextoDaActivity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Toast.makeText(getActivity(),"testeeeeeee",Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Infla o layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.esqueceu_senha, null);
        botaoNascimento = (Button) customView.findViewById(R.id.botao_nascimento);
        esqueceuMatricula = (EditText) customView.findViewById(R.id.esqueceu_matricula);
        esqueceuCPf = (EditText) customView.findViewById(R.id.esqueceu_cpf);
        InicializaEscutaBotoes();

        // infla e seta o layout para o dialog
        builder.setView(customView)

                // Adiciona as ações do botoes;
                .setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            ContextoDaActivity = getActivity(); // Grava o contexto atual da atividade
                        // se for ok
                        if (botaoNascimento.getText().toString().equals("Data de Nascimento") || esqueceuMatricula.getText().toString().equals("") || esqueceuCPf.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Você precisa informar sua matrícula, CPF e data de nascimento.", Toast.LENGTH_LONG).show();
                        } else {
                            ContrachequeActivity pegaimei = new ContrachequeActivity();
                            imeiRecuperar = pegaimei.passaImeiParaRecupera;
                            Log.d("IMEI ", imeiRecuperar);
                            String somenteData = botaoNascimento.getText().toString().substring(20, botaoNascimento.getText().length());
                            EsperaRequisicao();
                            if (somenteData.equals("")) {
                                Toast.makeText(getActivity(), "Você precisa informar a data de nascimento", Toast.LENGTH_LONG).show();
                            } else {
                                PegaRespostaHtml(esqueceuMatricula.getText().toString(), esqueceuCPf.getText().toString(), somenteData);
                            }
                        }

                    }
                })
                .setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EsqueceuSenhaContracheque.this.getDialog().cancel();
                        // Se fechar
                    }
                });
        return builder.create();



    }


    public void PegaData(String texto) {
        botaoNascimento.setText(texto);
    }

    public void InicializaEscutaBotoes() {

        // Escuta o campo matricula
        esqueceuMatricula.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            // OnTyping do campo texto esqueceu matricula
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (esqueceuMatricula.length() > 6) {
                    esqueceuMatricula.setText(esqueceuMatricula.getText().toString().substring(0, esqueceuMatricula.length() - 1));
                    esqueceuMatricula.setSelection(esqueceuMatricula.length());
                }

            }
        });


        // Escuta o Campo Esqueceu CPF
        esqueceuCPf.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            // OnTyping do campo texto CPF
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (esqueceuCPf.length() > 11) {
                    esqueceuCPf.setText(esqueceuCPf.getText().toString().substring(0, esqueceuCPf.length() - 1));
                    esqueceuCPf.setSelection(esqueceuCPf.length());
                }

            }
        });


    }


    public void PegaRespostaHtml(String requisicaoMatricula, String requisicaoCpf, String requisicaoDataNascimento) {
        class OneShotTask implements Runnable {
            String requisicaoMatricula, requisicaoCpf, requisicaoDataNascimento;

            OneShotTask(String s, String cpf, String datanasc) {
                requisicaoMatricula = s;
                requisicaoCpf = cpf;
                requisicaoDataNascimento = datanasc;
            }

            ;

            public void run() {
                //##
                try {

                    // Constroi e seta o tempo limite de resposta da requisicao
                    String url_contrachequeRecuperar = "http://contracheque.teresina.pi.gov.br/contracheque/recuperar_movel.php?i=" + imeiRecuperar + "&m=" + requisicaoMatricula + "&c=" + requisicaoCpf + "&d=" + requisicaoDataNascimento;
                    URLConnection connection = (new URL(url_contrachequeRecuperar)).openConnection();
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
                    respostaDaRequisicao = html.toString();
                    if (respostaDaRequisicao.substring(0, 1).toString().equals("#")) {
                        senhaRecuperada = respostaDaRequisicao.substring(25, respostaDaRequisicao.length());

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ContrachequeActivity atividade = new ContrachequeActivity();
                                atividade.senha.setText(senhaRecuperada);
                                atividade.matricula.setText(requisicaoMatricula);

                            }
                        }, 0000);

                        esperaCarregar.dismiss();

                    } else {  // Caso algum dado errado

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ContrachequeActivity atividade = new ContrachequeActivity();
                                atividade.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(ContextoDaActivity, respostaDaRequisicao, Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }, 0000);


                        esperaCarregar.dismiss();

                    }
                    //Log.d("DEU CERTO", respostaDaRequisicao);

                } catch (Exception ex) { // Caso esteja sem conexão
                   String naoConectou = "Verifique sua conexão com a internet";
                    //doToast();

                    esperaCarregar.dismiss();
                    Log.d("DEU ERRADO", ex.getMessage());
                }

                //#########
            }
        }
        Thread h = new Thread(new OneShotTask(requisicaoMatricula, requisicaoCpf, requisicaoDataNascimento)); // Cria o objeto da thread com parametro
        h.start();


        //Log.d("CONTEM2", senhaRecuperada);
    }


    public void EsperaRequisicao() {


        esperaCarregar = new ProgressDialog(getActivity());
        esperaCarregar.setCancelable(true);
        esperaCarregar.setMessage("Aguarde um momento...");
        esperaCarregar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        esperaCarregar.show();


    }


    public void doToast() {
     Handler ErroHandler = new Handler(Looper.getMainLooper());
                    ErroHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ContrachequeActivity as = new ContrachequeActivity();
                            Toast.makeText(as.getApplicationContext(),"ewrger",Toast.LENGTH_LONG).show();



                        }
                    }, 0000);

    }

}