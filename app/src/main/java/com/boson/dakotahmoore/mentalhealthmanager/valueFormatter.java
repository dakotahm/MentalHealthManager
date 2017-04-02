package com.boson.dakotahmoore.mentalhealthmanager;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.Date;


public class valueFormatter implements IAxisValueFormatter {
    SimpleDateFormat dateFormat;

    public valueFormatter(){
        dateFormat= new SimpleDateFormat("M-dd");
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return dateFormat.format(new Date((long)value));
    }
}
