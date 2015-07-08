package br.ufu.weightThrowing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogoAlerta extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
 
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
 
        builder.setMessage("Regresar dato de control")
               .setTitle("Alerta!")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });
 
        return builder.create();
    }
}