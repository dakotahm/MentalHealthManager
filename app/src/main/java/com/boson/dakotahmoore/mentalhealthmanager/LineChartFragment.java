package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Context;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;



public class LineChartFragment extends Fragment {
    // : Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int userId=1;
    private String timeSpinnerValue;
    private int MeasurableId;
    // : Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart lineChart;
    private SensorManager sensorManager;
    private LineData data=new LineData();
    private  LineDataSet datas;
    private ArrayList<Entry> entries= new ArrayList<Entry>();

    private OnFragmentInteractionListener mListener;

    public LineChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineChartFragment.
     */
    // : Rename and change types and number of parameters
    public static LineChartFragment newInstance(String param1, String param2) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeSpinnerValue="Month";
        lineChart=(LineChart)getView().findViewById(R.id.LineDisplay);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // : Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Date getDate(){
        Calendar cal =Calendar.getInstance();
        switch (timeSpinnerValue){
            case "Day":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                return cal.getTime();
            case "Week":
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                return cal.getTime();
            case "Month":
                cal.add(Calendar.MONTH, -1);
                return cal.getTime();
            case  "Year":
                cal.add(Calendar.YEAR, -1);
                return cal.getTime();
            default:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                return cal.getTime();
        }
    }

    class GetLogs extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params){
            String requestURL = "http://mhm.bri.land/getLogs.php";
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", String.valueOf( userId));

            Date timeframe = getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
            postDataParams.put("filter_date",formatter.format(timeframe));
            postDataParams.put("measurable_id",String.valueOf(MeasurableId));
            String result= Glue.performPostCall(requestURL,postDataParams);
            try {
                JSONObject mainObject = new JSONObject(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            entries=new ArrayList<Entry>();
            return result;
        }

        @Override
        protected void onPostExecute(String Result) {
//            JSONObject mainObject = null;
//            try {
//                mainObject = new JSONObject(Result);
//                JSONArray Measurables =mainObject.getJSONArray("measurables");
//                FragmentTransaction fragmentTransaction;
//                fragmentTransaction = fragManager.beginTransaction();
//                for(int i=0;i<Measurables.length();i++){
//                    JSONObject measurable=Measurables.getJSONObject(i);
//
//                    //Get Arguments from JSON
//                    Bundle args=new Bundle();
//                    args.putInt("max",measurable.getInt("max"));
//                    args.putInt("min",measurable.getInt("min"));
//                    args.putInt("id",measurable.getInt("id"));
//                    args.putString("name",measurable.getString("name"));
//                    args.putString("type",measurable.getString("type"));
//
//                    if(measurable.getString("type")=="value"){
//                        SliderFragment newSlider = new SliderFragment();
//                        newSlider.setArguments(args);
//                        fragmentTransaction.add(fragmentList.getId(), newSlider);
//                    }else{
//                        BooleanFragment newBool = new BooleanFragment();
//                        newBool.setArguments(args);
//                        fragmentTransaction.add(fragmentList.getId(), newBool);
//                    }
//
//                }
//                fragmentTransaction.commit();
//            } catch (JSONException e) {
//                Log.d(tag, "INVALID JSON");
//            }
        }
    }
}

class MoodLog{
       public String Log;
    public int Id;
    public String Time;
    public int Value;
    public int Lat;
    public int Lng;
        public MoodLog(String _log, int _Id,String _Time){
            Log=_log;
            Id=_Id;
            Time=_Time;
        }


}