package com.boson.dakotahmoore.mentalhealthmanager;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;


public class SliderFragment extends Fragment {
    // : Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView nameText;
    TextView valueText;
    SeekBar input;

    // : Rename and change types of parameters
    private String Name;
    private String Value;
    private int max;
    private  int min;
    private Button logActivity;
    private Activity c =this.getActivity();
    private OnFragmentInteractionListener mListener;

    public SliderFragment() {
        // Required empty public constructor
    }

    //TODO: add new Instance Method to instantiate values
    // : Rename and change types and number of parameters
    public static SliderFragment newInstance(String param1, String param2) {
        SliderFragment fragment = new SliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            Name = getArguments().getString(ARG_PARAM1);
//            Value = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_slider, null);
        logActivity=(Button) rootView.findViewById(R.id.button);
        logActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LogOptionsDialog dLog=new LogOptionsDialog(c);
                dLog.show();
            }
        });
        Name=getArguments().getString("name");
        Name=Name.substring(0,1).toUpperCase()+Name.substring(1);
        max=getArguments().getInt("max");
        min=getArguments().getInt("min");
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameText=(TextView)getView().findViewById(R.id.DataName);
        nameText.setText(Name);
         valueText=(TextView)getView().findViewById(R.id.SeekValue);
        valueText.setText("0");
        input=(SeekBar)getView().findViewById(R.id.seekBar);
        input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                int diff=max-min;
                int range=100/diff;
               valueText.setText(Integer.toString(progress/range));

            }
        });

    }
    // : Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // : Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
