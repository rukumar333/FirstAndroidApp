package com.example.rushil.omgandroid;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeNameDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EnterKeyListener mListener;

    public interface EnterKeyListener{
        public void closeDialog();
    }

    public ChangeNameDialogFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (EnterKeyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWordSelectedListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(MainActivity.PREFS, MainActivity.MODE_PRIVATE);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout,null);
        alert.setView(view);
        alert.setTitle("Hello!");
        alert.setMessage("What is your name?");

        final EditText input = (EditText)view.findViewById(R.id.name_edittext);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("","We are in the keylistner");
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    Log.d("","We are in the keylistner if statement");
                    String inputName = input.getText().toString();
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(MainActivity.PREF_NAME, inputName);
                    e.commit();
                    mListener.closeDialog();
                    Toast.makeText(getActivity().getApplicationContext(), "Welcome back, " + inputName + "!", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                String inputName = input.getText().toString();
                Log.d("input debug","this is the input" + inputName);
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(MainActivity.PREF_NAME, inputName);
                e.commit();

                Toast.makeText(getActivity().getApplicationContext(), "Welcome back, " + inputName + "!", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return alert.create();
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
    */

}
