package com.boson.dakotahmoore.mentalhealthmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

//TODO:Wire data time buttons to dateTimePickerDialog and TimePickerDialog and set text to selected value


public class LogOptionsDialog extends Dialog implements
        android.view.View.OnClickListener {
    // : Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // : Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button yes, no;
    public Activity c;
    public Dialog d;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_log_options_dialog);
        yes = (Button) findViewById(R.id.button2);
        no = (Button) findViewById(R.id.button3);
        //TODO:Override Yes button to update database
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }




}
