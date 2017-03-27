package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TreatmentAidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_aid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addActivity = (FloatingActionButton) findViewById(R.id.AddActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addActivityIntent = new Intent(TreatmentAidActivity.this, DataCollectActivity.class);
                startActivity(addActivityIntent);
            }
        });

        FloatingActionButton displayActivity = (FloatingActionButton) findViewById(R.id.DisplayActivity);
        displayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent displayIntent = new Intent(TreatmentAidActivity.this, DisplayDataActivity.class);
                startActivity(displayIntent);
            }
        });

        FloatingActionButton treatmentActivity = (FloatingActionButton) findViewById(R.id.TreatmentActivity);
        treatmentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent treatmentIntent = new Intent(TreatmentAidActivity.this, TreatmentAidActivity.class);
                startActivity(treatmentIntent);

            }
        });
    }
}
