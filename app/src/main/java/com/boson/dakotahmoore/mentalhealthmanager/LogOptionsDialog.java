package com.boson.dakotahmoore.mentalhealthmanager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

//TODO:Wire data time buttons to dateTimePickerDialog and TimePickerDialog and set text to selected value


public class LogOptionsDialog extends Dialog implements
        android.view.View.OnClickListener {
    // Fragment vars
    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static double CordX = 0.0;
    private static double CordY = 0.0;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private java.util.Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String time;

    private int userId;

    // : Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText logText;
    public Button yes, no;
    public Activity c;
    public Dialog d;
    private DatabaseHelper myDb;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                c.finish();
                break;
            case R.id.button3:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    public LogOptionsDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    //TODO: add handlers for date and time buttons call timepickers and retrieve values
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar= java.util.Calendar.getInstance();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_log_options_dialog);
        yes = (Button) findViewById(R.id.button2);
        no = (Button) findViewById(R.id.button3);
        logText=(EditText) findViewById(R.id.LogEditText);
        myDb=new DatabaseHelper(getContext());
        userId=myDb.checkUsers();

        //TODO:Override Yes button to update database
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        //Initialize request location service
        locationManager = (LocationManager) c.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Coordinates for database
                CordX = location.getLatitude();
                CordY = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                c.startActivity(i);
            }
        };

        configure_permission();

        //Push Yes, log to database
        yes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //Setup time and date
                year = calendar.get(calendar.YEAR);
                month = calendar.get(calendar.MONTH) + 1;
                day = calendar.get(calendar.DAY_OF_MONTH);
                hour = calendar.get(calendar.HOUR_OF_DAY);
                minute = calendar.get(calendar.MINUTE);
                second = calendar.get(calendar.SECOND);
                time = String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

                //Send a request to database and add time log
                String requestURL = "http://mhm.bri.land/addLog.php";
                StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //This is the Json object for response
                                    //parse for success
                                    JSONObject jsonResponse = new JSONObject(response).getJSONArray("data").getJSONObject(0);

                                } catch (JSONException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<>();
                                params.put("user_id", String.valueOf(userId));
                                //put your params here
                                params.put("timestamp", time);
                                return params;
                            }
                        };
               Volley.newRequestQueue(getContext()).add(postRequest);
                dismiss();
            }
        });

        //Push no, close this dialog
        no.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dismiss();
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_permission();
                break;
            default:
                break;
        }
    }

    void configure_permission(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 1, locationListener);
            }
        });
    }
}
