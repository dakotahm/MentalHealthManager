package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DataCollectActivity extends AppCompatActivity implements SliderFragment.OnFragmentInteractionListener,BooleanFragment.OnFragmentInteractionListener {
    static private boolean firstrun = true;
    String tag;
    private int userId=1;
    //Get id of Listview for fragments and initialize the manager
    LinearLayout fragmentList;
    FrameLayout theHack;
    FragmentManager fragManager = getSupportFragmentManager();
    DatabaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collect);
        userId=getIntent().getIntExtra("user",1);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mydb= new DatabaseHelper(this);
       // setSupportActionBar(toolbar);
        fragmentList = (LinearLayout) findViewById(R.id.MasterLinear);
        mydb=new DatabaseHelper(this);

        String requestURL = "http://mhm.bri.land/getMeasurables.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            String test=response.toString();
                           // System.out.println(response);
                            int jsonResponse = new JSONObject(response).getInt("success");
                            Log.d(tag,"The resposne was "+jsonResponse+"\n\n\n");
                            JSONObject mainObject = null;
                            try {
                                mainObject = new JSONObject(response);
                                JSONArray Measurables =mainObject.getJSONArray("measurables");
                                //System.out.println(Measurables.toString());
                                FragmentTransaction fragmentTransaction;


                                LinearLayout master=(LinearLayout)findViewById(R.id.MasterLinear) ;
                                LinearLayout frags = new LinearLayout(getApplicationContext());
                                frags.setOrientation(LinearLayout.VERTICAL);
                                frags.setId(View.generateViewId());
                                fragmentTransaction = fragManager.beginTransaction();
                                for(int i=0;i<Measurables.length();i++){
                                    JSONObject measurable=Measurables.getJSONObject(i);
                                    //Get Arguments from JSON
                                    Bundle args=new Bundle();
                                    //insert into local database if its not in there

                                    if(mydb.checkMeasurables(measurable.getInt("id")).getCount()==0){
                                        if(measurable.getString("type").equals("boolean")){
                                          boolean result= mydb.insertMeasurable(measurable.getInt("id"),measurable.getString("name"),measurable.getString("type"),-1,-1,userId);
                                            if(result)
                                                System.out.println("BOOLEAN INSERT FAILED");
                                        }else{
                                            boolean result= mydb.insertMeasurable(measurable.getInt("id"),measurable.getString("name"),measurable.getString("type"),measurable.getInt("max"),measurable.getInt("min"),userId);
                                            if(result)
                                                System.out.println("BOOLEAN INSERT FAILED");
                                        }
                                    }

                                    args.putInt("id",measurable.getInt("id"));
                                    args.putString("name",measurable.getString("name"));
                                    args.putString("type",measurable.getString("type"));
                                    args.putInt("user",userId);
                                    if(measurable.getString("type").equals("value")){
                                       args.putInt("max",measurable.getInt("max"));
                                       args.putInt("min",measurable.getInt("min"));

                                        SliderFragment newSlider = new SliderFragment();
                                        newSlider.setArguments(args);


                                        fragmentTransaction.add(fragmentList.getId(), newSlider,"newFragment"+i);
                                        //System.out.println(measurable.getString("name"));

                                    }else{
                                        //System.out.println("BOOLEAN");
                                       BooleanFragment newBool = new BooleanFragment();
                                       newBool.setArguments(args);
                                      fragmentTransaction.add(fragmentList.getId(), newBool,"newFragment"+i);
                                        //System.out.println(measurable.getString("name"));
                                   }


                                }
                                fragmentTransaction.commit();

                            } catch (JSONException e) {
                                Log.d(tag, "INVALID JSON");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("user_id", String.valueOf( userId));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(postRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_collect, menu);
        return true;
    }
    @Override
    public void onBackPressed(){

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
        }else if(id==R.id.AddMeasurableActivity){
            Intent intent=new Intent(this,addMeasurable.class);
            intent.putExtra("user",userId);
            startActivity(intent);
        }else if(id==R.id.LogOut){
            Intent intent=new Intent(this,LoginActivity.class);
            mydb.UpdateStatus(userId,0);
            startActivity(intent);
        }else if(id==R.id.DataDisplayAction){
            Intent displayIntent = new Intent(DataCollectActivity.this, DisplayDataActivity.class);
            displayIntent.putExtra("user",userId);
            startActivity(displayIntent);
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
