package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;



public class LineChartFragment extends Fragment  {
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
    private LineData data;
    private  LineDataSet datas;
    private ArrayList<Entry> entries= new ArrayList<Entry>();
    private ArrayList<measurableWrapper> measurables=new ArrayList<measurableWrapper>();
    private  ArrayList<MoodLog> Logs=new ArrayList<MoodLog>();
    private String[] timeValues={"Day","Week","Month","Year"};
    private Spinner timeSpinner;
    private Spinner measurableSpinner;
    private OnFragmentInteractionListener mListener;
    private DatabaseHelper dbHelper;
    private ListView LogList;

    public LineChartFragment() {
        // Required empty public constructor
    }


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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbHelper=new DatabaseHelper(getContext());
        userId=getArguments().getInt("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_line_chart, container, false);
        return view;
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
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)  {
        lineChart=(LineChart)getView().findViewById(R.id.LineDisplay);
        timeSpinner=(Spinner)getView().findViewById(R.id.Timeframe);
        measurableSpinner=(Spinner)getView().findViewById(R.id.trackedActivity);
        LogList=(ListView)getView().findViewById(R.id.LogList);


        Cursor cursor=dbHelper.getMeasurables(userId);
        if (cursor.moveToFirst()){
            do{
                //String data = cursor.getString(cursor.getColumnIndex("data"));
                measurableWrapper measurable = new measurableWrapper(cursor.getString(1),cursor.getInt(0));
                measurables.add(measurable);
            }while(cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> timeAdapter=new ArrayAdapter<String> (getContext(),android.R.layout.simple_spinner_item,timeValues);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<measurableWrapper> measurableAdapter=new ArrayAdapter<measurableWrapper> (getContext(),android.R.layout.simple_spinner_item, measurables);
        measurableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurableSpinner.setAdapter(measurableAdapter);
       timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           public void onItemSelected(AdapterView<?> parent, View view,
                                      final int position, long id) {
               System.out.println("ITEM SELECTED");
               timeSpinnerValue=timeSpinner.getSelectedItem().toString();
               final Date timeFilter =getDate();
               final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
               final SimpleDateFormat stringFormatter = new SimpleDateFormat("yyyy-m-dd HH:MM:SS");
               Logs=new ArrayList<MoodLog>();
               entries=new ArrayList<Entry>();

               String requestURL = "http://mhm.bri.land/getLogs.php";


               StringRequest postRequest = new StringRequest(Request.Method.POST, requestURL,
                       new Response.Listener<String>() {

                           @Override
                           public void onResponse(String response) {
                               try {
                                   String test=response.toString();
                                    System.out.println(response);
                                   JSONObject jsonResponse = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                                   JSONArray values=jsonResponse.getJSONArray("values");
                                   System.out.println(values);
                                   JSONObject mainObject = null;
                                   try {
                                      for(int i=0;i<values.length();i++){
                                          JSONObject value=values.getJSONObject(i);
                                            System.out.println("Created Value");

                                          JSONObject data=new JSONObject(value.getString("data"));
                                          System.out.println("Created Data");


                                          int logValue=0;
                                          try {
                                              if(jsonResponse.getString("type").equals("boolean")){
                                                  logValue=1;
                                              }else{
                                                  logValue=data.getInt("value");
                                                  System.out.println("Accessed Value");

                                              }
                                          }catch (JSONException e){
                                              continue;
                                          }


                                         String Large[]=value.getString("timestamp").split("\\s|:|-");
                                          System.out.println("Accessed Time");

                                          Date logTime =new Date(Integer.parseInt(Large[0])-1900,Integer.parseInt(Large[1])-1,Integer.parseInt(Large[2]),Integer.parseInt(Large[3]),Integer.parseInt(Large[4]),Integer.parseInt(Large[5]));
                                          float milli=(float)logTime.getTime();
                                          System.out.println(milli);
                                          entries.add(new Entry(milli,logValue));
                                          Logs.add(new MoodLog(data.getString("log"),value.getInt("id"),logTime));
                                          System.out.println("Accessed log");

                                      }

                                       Collections.sort(entries, new Comparator<Entry>() {
                                           @Override
                                           public int compare(Entry o1, Entry o2) {
                                               return (int)(o1.getX()-o2.getX());
                                           }
                                       });
                                      ArrayAdapter<MoodLog> listAdapter= new ArrayAdapter<MoodLog>(getContext(),android.R.layout.simple_list_item_1,Logs);
                                       LogList.setAdapter(listAdapter);
                                       datas=new LineDataSet(entries,measurables.get(measurableSpinner.getSelectedItemPosition()).name);
                                       data=new LineData(datas);
                                       datas.setColors(ColorTemplate.LIBERTY_COLORS);
                                       datas.setDrawFilled(true);
                                       XAxis x =lineChart.getXAxis();
                                       x.setValueFormatter(new valueFormatter());
                                     lineChart.getAxisRight().setEnabled(false);
                                       x.setPosition(XAxis.XAxisPosition.BOTTOM);

                                       lineChart.setData(data);
                                       lineChart.animateXY(800,800);


                                   } catch (JSONException e) {
                                       System.out.println(e.getMessage());

                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   Toast.makeText(getActivity(),"There is no data for these parameters",Toast.LENGTH_SHORT).show();
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
                       params.put("filter_date",formatter.format(timeFilter));
//                       System.out.println(formatter.format(timeFilter));
                       params.put("measurable_id",Integer.toString(measurables.get(measurableSpinner.getSelectedItemPosition()).Id));
                       return params;
                   }
               };
               Volley.newRequestQueue(getContext()).add(postRequest);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       });
        measurableSpinner.setOnItemSelectedListener(timeSpinner.getOnItemSelectedListener());
    }




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
    public Date Time;
    public int Value;
    public int Lat;
    public int Lng;
        public MoodLog(String _log, int _Id,Date _Time){
            Log=_log;
            Id=_Id;
            Time=_Time;
        }
        @Override
        public String toString(){
            final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            final SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:MM");
            String buffer="Time: "+TimeFormat.format(Time)+"   Date: "+ dateFormat.format(Time)+"\n";
            buffer+=Log;
            return buffer;
        }
}

class measurableWrapper{
    public int Id;
    public int value;
    public int max;
    public int min;
    public String name;
    public measurableWrapper(String _name, int _Id){
        name=_name;
        Id=_Id;
    }
    @Override
    public String toString(){
        return name;
    }
}

