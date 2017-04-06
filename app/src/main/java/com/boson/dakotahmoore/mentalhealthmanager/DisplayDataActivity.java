package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.StringTokenizer;

public class DisplayDataActivity extends AppCompatActivity implements LineChartFragment.OnFragmentInteractionListener {

    private int userId=1;
    private String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        FragmentManager fragManager = getSupportFragmentManager();
        final Intent intent=getIntent();
        userId= intent.getIntExtra("user",1);



        Bundle bundle=new Bundle();
        bundle.putInt("user",userId);
        FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
        Fragment chart=new LineChartFragment();
        chart.setArguments(bundle);
        fragmentTransaction.add(R.id.ChartPlaceholder,chart);
        //TODO:This is an example to help you out but it currently fails on commit
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.LogOut){
            DatabaseHelper myDB=new DatabaseHelper(this);
            myDB.UpdateStatus(userId,0);
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }else if(id==R.id.DataCollectAction){
            Intent addActivityIntent = new Intent(DisplayDataActivity.this, DataCollectActivity.class);
            addActivityIntent.putExtra("user",userId);
            startActivity(addActivityIntent);
        }else if(id==R.id.GetMeasurableAction){
            Intent intent=new Intent(this,addMeasurable.class);
            intent.putExtra("user",userId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
