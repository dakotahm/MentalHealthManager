package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addMeasurable extends AppCompatActivity {
    Button submit;
    Spinner types;
    EditText Max;
    EditText Min;
    EditText Name;
    int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurable);
        submit=(Button) findViewById(R.id.addMeasurableBTN);
        types=(Spinner) findViewById(R.id.spinner);
        Max=(EditText) findViewById(R.id.MaxEditText);
        Min=(EditText) findViewById(R.id.MinimumEditText);
        Name=(EditText) findViewById(R.id.NameEditText);
        String typeList[]={"slider","boolean"};
        Intent intent=getIntent();
        UserId=intent.getIntExtra("user",1);

        ArrayAdapter<String> typesAdapter=new ArrayAdapter<String> (this,android.R.layout.simple_spinner_item, typeList);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(typesAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min,max;
                if(Name.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(),"Invalid Name Value",Toast.LENGTH_SHORT).show();
                }else{
                    if(Max.getText().toString().trim().length()==0){
                        min=0;
                    }else{
                        min=Integer.parseInt(Min.getText().toString());
                    }

                    if(Min.getText().toString().trim().length()==0){
                        max=100;
                    }else{
                        max=Integer.parseInt(Max.getText().toString());
                    }

                    String requestURL = "http://mhm.bri.land/addMeasurable.php";


                    StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject json =new JSONObject(response);
                                        if(json.getInt("success")==0){
                                            Toast.makeText(getApplicationContext(),"Failed to Add Measurable",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Measurable Added",Toast.LENGTH_SHORT).show();
                                        }
                                        Bundle bundle=new Bundle();
                                        bundle.putInt("user",UserId);

                                        Intent intent=new Intent(getApplicationContext(),DataCollectActivity.class);
                                        intent.putExtra("user",UserId);
                                        startActivity(intent);


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
                            params.put("user_id", String.valueOf( UserId));
                            params.put("name",Name.getText().toString());
                            params.put("max",Max.getText().toString());
                            params.put("min",Min.getText().toString());
                            params.put("type",types.getSelectedItem().toString());
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(postRequest);
                }
            }
        });

    }
}
