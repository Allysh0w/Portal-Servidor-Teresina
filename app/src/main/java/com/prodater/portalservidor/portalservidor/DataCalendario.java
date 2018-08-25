package com.prodater.portalservidor.portalservidor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Javac on 13/01/2016.
 */
public class DataCalendario extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
        String Data_escolhida;



@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

// Usa a Data atual como padrão do DataPicker
final Calendar c = Calendar.getInstance();
       // int year = c.get(Calendar.YEAR); // Padrão para usar em outras aplicações, pega o ano atual do sistema.
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

// Cria uma nova instancia do DataPicker e retorna ela
        return new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog, this, 1980, month, day); // 1990 ano padrao do calendario para os servidores


        }


    // Seta a data
@Override
public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String DataFormatada = sdf.format(c.getTime());
        Data_escolhida = DataFormatada;
        Data_escolhida = Data_escolhida.replace("-","/");


        EsqueceuSenhaContracheque retornaData = new EsqueceuSenhaContracheque();
        retornaData.PegaData("Data de nascimento: " + Data_escolhida);

        //t.botao.setText(Data_escolhida);
        /*Button activityButton = (Button)getActivity().findViewById(R.id.botao_nascimento);
        activityButton.setText (DataFormatada);*/ // Usado para passar os parametros do fragment para a acitivity
        //Toast.makeText(getActivity(), "Data selecionada: " + DataFormatada, Toast.LENGTH_LONG).show();
        }


        }


