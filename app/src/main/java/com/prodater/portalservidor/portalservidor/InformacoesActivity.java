package com.prodater.portalservidor.portalservidor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InformacoesActivity extends AppCompatActivity {

    ImageButton maisInformacoes;
    ImageButton telefoneUteis;
    ImageButton btnEsic;
    ImageButton btnColab;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        getSupportActionBar().hide();

        InicializaClickInformacoes();
    }

    // Inicia a atividade que manda as informações para o browser
    public void CarregaUrlProdater(){

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.prodater.teresina.pi.gov.br")));

    }

    private void CarregaUrlEsic(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://esic.teresina.pi.gov.br")));
    }

    private void CarregaUrlColab(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.colab.re")));
    }

    // Escuta o click no textView
    private void InicializaClickInformacoes(){

        maisInformacoes = (ImageButton)findViewById(R.id.btn_maisinformacoes); // Captura o textview pelo id
        telefoneUteis   = (ImageButton)findViewById(R.id.btn_telefoneuteis);
        btnColab        = (ImageButton)findViewById(R.id.btn_colab);
        btnEsic         = (ImageButton)findViewById(R.id.btn_esic);


        maisInformacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Chama o browser
                CarregaUrlProdater();

            }

        });


        telefoneUteis.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(InformacoesActivity.this, TelefonesUteisActivity.class);
                startActivity(intent);

                // starta a activity aqui;
            }
        });


        btnEsic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarregaUrlEsic();
            }
        });


        btnColab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             CarregaUrlColab();
            }
        });

    }

}
