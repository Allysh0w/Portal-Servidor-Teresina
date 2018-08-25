package com.prodater.portalservidor.portalservidor;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Javac on 11/02/2016.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] orgao_nome;
    private final String[] telefone;

    public CustomListAdapter(Activity context, String[] orgao,String[] telefone) {
        super(context, R.layout.lista_telefones, orgao);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.orgao_nome=orgao;
        this.telefone=telefone;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lista_telefones, parent,false);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nome_orgao);
        TextView extratxt = (TextView) rowView.findViewById(R.id.telefone_orgao);

        txtTitle.setText(orgao_nome[position]);
        txtTitle.setTypeface(null, Typeface.BOLD);
        extratxt.setText(telefone[position]);

        return rowView;

    };
}

