package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class DataCollectActivity extends AppCompatActivity implements SliderFragment.OnFragmentInteractionListener {
    static private boolean firstrun = true;

    //Get id of Listview for fragments and initialize the manager
    LinearLayout fragmentList;
    FragmentManager fragManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentList = (LinearLayout) findViewById(R.id.CollectDataList);
        //Austin this is how you add fragments to the view
        //TODO: make this process dynamic from a query
        SliderFragment newSlider = new SliderFragment();
        FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
        fragmentTransaction.add(fragmentList.getId(), newSlider);
        fragmentTransaction.commit();

        FloatingActionButton addActivity = (FloatingActionButton) findViewById(R.id.AddActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addActivityIntent = new Intent(DataCollectActivity.this, DataCollectActivity.class);
                startActivity(addActivityIntent);
            }
        });

        FloatingActionButton displayActivity = (FloatingActionButton) findViewById(R.id.DisplayActivity);
        displayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent displayIntent = new Intent(DataCollectActivity.this, DisplayDataActivity.class);
                startActivity(displayIntent);
            }
        });

        FloatingActionButton treatmentActivity = (FloatingActionButton) findViewById(R.id.TreatmentActivity);
        treatmentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent treatmentIntent = new Intent(DataCollectActivity.this, TreatmentAidActivity.class);
                startActivity(treatmentIntent);

            }
        });

        //Jump to login when opening app
//        if(firstrun)
//        {
//            firstrun = false;
//            Log.d("debugging","loading login activity");
//            Intent myIntent = new Intent(this, LoginActivity.class);
//            startActivity(myIntent);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_collect, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
