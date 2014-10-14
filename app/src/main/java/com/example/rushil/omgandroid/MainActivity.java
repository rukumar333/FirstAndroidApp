    package com.example.rushil.omgandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;


    public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, ChangeNameDialogFragment.EnterKeyListener {

        Button mainButton;
        //Button nameButton;
        TextView mainTextView;
        EditText mainEditText;
        ListView mainListView;
        JSONAdapter mJSONAdapter;
        ArrayList mNameList = new ArrayList();
        ShareActionProvider mShareActionProdiver;
        DialogFragment ChangeNameDialogFragment;
        ChangeNameDialogFragment dialog;

        protected static final String PREFS = "prefs";
        protected static final String PREF_NAME = "name";
        private static final String NAMES = "com.example.omgandroid.MainActivity.mNameList";
        private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
        SharedPreferences mSharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setProgressBarIndeterminateVisibility(false);
            setContentView(R.layout.activity_main);
            mainTextView = (TextView) findViewById(R.id.main_textview);
            mainButton = (Button) findViewById(R.id.main_button);
            mainButton.setOnClickListener(this);
            //nameButton = (Button) findViewById(R.id.name_button);
            //nameButton.setOnClickListener(this);

            mainEditText = (EditText) findViewById(R.id.main_edittext);
            mainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        queryBooks(mainEditText.getText().toString());
                        mainEditText.setText("");
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainEditText.getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
            });
            mainListView = (ListView) findViewById(R.id.main_listview);
            if(savedInstanceState != null){
                Log.d("debug", "saved instance state was not null");
                mNameList = savedInstanceState.getStringArrayList(NAMES);
            }else{
                displayWelcome();
            }
            mJSONAdapter = new JSONAdapter(this,getLayoutInflater());
            mainListView.setAdapter(mJSONAdapter);
            mainListView.setOnItemClickListener(this);
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
        /*
        @Override
        protected void onSaveInstanceState(Bundle outState){
            ArrayList<String> names = new ArrayList<String>();
            for(int i = 0; i < mArrayAdapter.getCount(); i ++){
                names.add((String) mArrayAdapter.getItem(i));
            }
            outState.putStringArrayList(NAMES,names);
        }
        */
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
                    queryBooks(mainEditText.getText().toString());
                    mainEditText.setText("");
                    break;
                    /*
                    String name = mainEditText.getText().toString();
                    mainTextView.setText(name + " is learning Android development!");
                    mainEditText.setText("");
                    mNameList.add(name);
                    mArrayAdapter.notifyDataSetChanged();
                    setShareIntent();
                    break;
                    */
                /*
                case R.id.name_button:
                    changeWelcomeName();
                    */
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

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
            dialog = new ChangeNameDialogFragment();
            dialog.show(getFragmentManager(),"fragment_edit_name");
        }

        public void closeDialog(){
            dialog.dismiss();
        }

        private void queryBooks(String searchString){
            String urlString = "";
            try{
                urlString = URLEncoder.encode(searchString,"UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            setProgressBarIndeterminateVisibility(true);
            client.get(QUERY_URL + urlString,
                    new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(JSONObject jsonObject){
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                            mJSONAdapter.updateData(jsonObject.optJSONArray("docs"));
                            setProgressBarIndeterminateVisibility(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable, JSONObject error){
                            Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            setProgressBarIndeterminateVisibility(false);
                            Log.e("omg android", statusCode + " " + throwable.getMessage());
                        }
            });
        }
    }
