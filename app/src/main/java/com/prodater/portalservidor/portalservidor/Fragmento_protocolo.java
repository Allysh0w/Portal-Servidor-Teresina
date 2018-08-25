package com.prodater.portalservidor.portalservidor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Javac on 19/01/2016.
 */
public class Fragmento_protocolo  extends DialogFragment {

    TextView visualizaProtocolo;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Infla o layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.visualiza_protocolo, null);
        String textoProtocolo = this.getArguments().getString("message");
        visualizaProtocolo = (TextView)customView.findViewById(R.id.textview_protocolo_resposta);
        visualizaProtocolo.setText(textoProtocolo);
        visualizaProtocolo.setMovementMethod(new ScrollingMovementMethod());
        // infla e seta o layout para o dialog
        builder.setView(customView)

                // Adiciona as ações do botoes;
                .setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // se for ok, nao faz nada

                    }
                });

        return builder.create();

    }

}
