package com.prodater.portalservidor.portalservidor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrincipalActivity extends AppCompatActivity {

    ImageButton contracheque;
    ImageButton protocolo;
    ImageButton noticias;
    ImageButton informacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getSupportActionBar().hide();

        //Ouve o clique das ImageButton da tela inicial
        addListenerOnButton();
    }

    // Cria um fragmento de DatePicker
    public void setDate(View view) {
        DialogFragment picker = new DataCalendario();
        picker.show(getFragmentManager(), "datePicker");

    }


    //Clique dos botões da tela principal.
    public void addListenerOnButton() {

        //Inicialização das variáveis dos image button
        contracheque = (ImageButton) findViewById(R.id.img_contracheque);
        protocolo =    (ImageButton) findViewById(R.id.img_protocolo);
        noticias =     (ImageButton) findViewById(R.id.img_noticias);
        informacoes =  (ImageButton) findViewById(R.id.img_informacoes);

        //Escuta o click do botão contracheque
        contracheque.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(PrincipalActivity.this, ContrachequeActivity.class);
                startActivity(intent);
                //Toast.makeText(PrincipalActivity.this,
                //      "Contracheque clicado!", Toast.LENGTH_SHORT).show();

            }

        });

        //Escuta o click do botão protocolo
        protocolo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(PrincipalActivity.this, ProtocoloActivity.class);
                startActivity(intent);

            }

        });

        //Escuta o click do botão noticias
        noticias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(PrincipalActivity.this, NoticiasActivity.class);
                startActivity(intent);

            }

        });

        //Escuta o click do botão informações
        informacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(PrincipalActivity.this, InformacoesActivity.class);
                startActivity(intent);

            }

        });


    }


    // Implementação do botão voltar
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);
        alert.setTitle("Você deseja sair?");
        // alert.setMessage("Message");

        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Sua ação aqui
                moveTaskToBack(true);
                finish();

            }
        });

        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();

    }

}
