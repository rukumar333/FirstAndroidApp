    package com.example.rushil.omgandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


    public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

        Button mainButton;
        Button nameButton;
        TextView mainTextView;
        EditText mainEditText;
        ListView mainListView;
        ArrayAdapter mArrayAdapter;
        ArrayList mNameList = new ArrayList();
        ShareActionProvider mShareActionProdiver;
        DialogFragment ChangeNameDialogFragment;

        protected static final String PREFS = "prefs";
        protected static final String PREF_NAME = "name";
        SharedPreferences mSharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mainTextView = (TextView) findViewById(R.id.main_textview);
            mainTextView.setText("Set in java!");
            mainButton = (Button) findViewById(R.id.main_button);
            mainButton.setOnClickListener(this);
            nameButton = (Button) findViewById(R.id.name_button);
            nameButton.setOnClickListener(this);

            mainEditText = (EditText) findViewById(R.id.main_edittext);
            //set onkeylistener
            mainListView = (ListView) findViewById(R.id.main_listview);
            mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mNameList);
            mainListView.setAdapter(mArrayAdapter);
            mainListView.setOnItemClickListener(this);
            displayWelcome();
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem shareItem = menu.findItem(R.id.menu_item_share);
            if (shareItem != null) {
                mShareActionProdiver = (ShareActionProvider) shareItem.getActionProvider();
            }
            setShareIntent();
            return true;
        }

        private void setShareIntent() {
            if (mShareActionProdiver != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());
                mShareActionProdiver.setShareIntent(shareIntent);
            }
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.main_button:
                    String name = mainEditText.getText().toString();
                    mainTextView.setText(name + " is learning Android development!");
                    mainEditText.setText("");
                    mNameList.add(name);
                    mArrayAdapter.notifyDataSetChanged();
                    setShareIntent();
                    break;
                case R.id.name_button:
                    changeWelcomeName();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d("omg android", position + ": " + mNameList.get(position));
        }


        public void displayWelcome() {
            mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
            String name = mSharedPreferences.getString(PREF_NAME, "");
            if (name.length() > 0) {
                Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
            } else {
                changeWelcomeName();
            }
        }

        public void changeWelcomeName(){
            /*
            mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputName = input.getText().toString();

                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    Toast.makeText(getApplicationContext(), "Welcome back, " + inputName + "!", Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
            */
            ChangeNameDialogFragment dialog = new ChangeNameDialogFragment();
            dialog.show(getFragmentManager(),"fragment_edit_name");
        }
    }
