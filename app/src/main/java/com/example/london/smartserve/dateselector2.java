package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class dateselector2 extends AppCompatActivity {
    private static final String TAG="CalenderActivity";
    private CalendarView mCalenderView;
    private Button mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateselector);
        final String key=getIntent().getExtras().getString("key");
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.my_toolbarcal);
        final TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        mDone=(Button)findViewById(R.id.dateconfirm);
        mCalenderView=(CalendarView) findViewById(R.id.calendarView);
        mCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar fdate=new GregorianCalendar(year,month,dayOfMonth);
                SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM d, ''yy");
                fmt.setCalendar(fdate);
                final String dateFormated=fmt.format(fdate.getTime());
                mTitle.setText(dateFormated);
                mDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dateFormated!=null){
                            confirmDate(dateFormated,key);
                        }
                        else{
                            Toast.makeText(dateselector2.this, "Please select date..", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void confirmDate(String date,String key) {
        Intent back=new Intent(dateselector2.this,employeecashtrack.class);
        Bundle extras = new Bundle();
        extras.putString("key", key);
        extras.putString("date", date);
        back.putExtras(extras);
        startActivity(back);
        finish();
    }
}
