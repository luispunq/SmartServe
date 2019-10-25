package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class monthselector4 extends AppCompatActivity {
    private Button mDone;
    private Spinner mMonthselect;
    private Spinner mYearSelect;
    private String month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthselector);
        mDone=(Button)findViewById(R.id.monthdone);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.my_toolbarmonsel);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);

        mMonthselect=(Spinner)findViewById(R.id.monthselect);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Months,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthselect.setAdapter(adapter);

        mYearSelect=(Spinner)findViewById(R.id.yearselect);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.Years,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearSelect.setAdapter(adapter1);

        mMonthselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mYearSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year=parent.getItemAtPosition(position).toString();
                if (year.equals("2017")){
                    year="17";
                }else if (year.equals("2018")){
                    year="18";
                }else if (year.equals("2019")){
                    year="19";
                }else if (year.equals("2020")){
                    year="20";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String final_date=month+"-"+year;
                Intent back=new Intent(monthselector4.this,individualaccount.class);
                back.putExtra("options",final_date+"-"+"income");
                startActivity(back);
            }
        });
    }
}
