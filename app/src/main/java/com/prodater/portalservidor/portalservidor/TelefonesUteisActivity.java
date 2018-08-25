package com.prodater.portalservidor.portalservidor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TelefonesUteisActivity extends AppCompatActivity {


    String[] orgaos ={
            "AEROPORTO",
            "BOMBEIROS",
            "CENTRAL DE ARTESANATO",
            "CORREIOS",
            "DELEGACIA DA MULHER",
            "FALA TERESINA",
            "POLÍCIA CIVIL",
            "POLÍCIA MILITAR",
            "PROCON",
            "PRONTO – SOCORRO-HUT",
            "RODOVIÁRIA",
            "RONDA CIDADÃO",
            "RONE",
            "SAMU"
    };

    String[] telefones ={
            "(86) 3133-6270",
            "193",
            "(86) 3222-5772",
            "(86) 3301-3500",
            "(86) 3222-2323",
            "(86) 3216-3132",
            "(86) 3216-5212",
            "190",
            "(86) 3216-4550",
            "(86) 3229-4872",
            "(86) 3229-9047",
            "(86) 3131-4781",
            "(86) 3213-1614",
            "192"
    };


    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefones_uteis);
        getSupportActionBar().hide();
        InicializaLista();


    }

    //Inicia Lista que preenche os paises (Imagem, nome e descrição)
    public void InicializaLista(){
        CustomListAdapter adapter=new CustomListAdapter(this, orgaos,telefones);
        list=(ListView)findViewById(R.id.listaTelefones);
        list.setAdapter(adapter);
        //list.setSelection(list.getCount() -1);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String numero_telefone = telefones[+position];

                //Chama o telefone e disca o número
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(numero_telefone)));
                try {

                    startActivity(in);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Não foi encontrado aplicação para efetuar chamadas nesse dispositivo.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
