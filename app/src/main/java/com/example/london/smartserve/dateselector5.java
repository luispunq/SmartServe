package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class dateselector5 extends AppCompatActivity {
    private static final String TAG="CalenderActivity";
    private CalendarView mCalenderView;
    private Button mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateselector);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.my_toolbarcal);
        final TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        mDone=(Button)findViewById(R.id.dateconfirm);
        mCalenderView=(CalendarView) findViewById(R.id.calendarView);
        mCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, final int month, int dayOfMonth) {
                GregorianCalendar fdate=new GregorianCalendar(year,month,dayOfMonth);
                SimpleDateFormat fmt = new SimpleDateFormat("EEE");
                fmt.setCalendar(fdate);
                final String yearT=new SimpleDateFormat("yyyy").format(fdate.getTime());
                final String monthT=new SimpleDateFormat("MMM").format(fdate.getTime());
                final String dayt=new SimpleDateFormat("EEE").format(fdate.getTime());
                final String datt=new SimpleDateFormat("EEE, MMM d, ''yy").format(fdate.getTime());
                final long dattee=fdate.getTimeInMillis();
                final String dateFormated=fmt.format(fdate.getTime());
                mTitle.setText(dateFormated);
                mDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dateFormated!=null){
                            confirmDate(yearT,monthT,dayt,datt,dattee);
                        }
                        else{
                            Toast.makeText(dateselector5.this, "Please select date..", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void confirmDate(String year,String month,String day,String dat,long datta) {
        Intent backtosignin =new Intent(dateselector5.this,dailycashstatement.class);
        Bundle extras = new Bundle();
        extras.putString("year", year);
        extras.putString("month", month);
        extras.putString("day", day);
        extras.putString("date", dat);
        extras.putString("datee", String.valueOf(datta));
        backtosignin.putExtras(extras);
        startActivity(backtosignin);
        finish();
    }
}
