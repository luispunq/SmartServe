package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

public class receiptselection extends AppCompatActivity {
    private CardView mDaily,mMonthly;
    private String grpkey=null,meetid=null,date=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiptselection);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        meetid = extras.getString("meet");
        mDaily=(CardView) findViewById(R.id.dailyre);
        mMonthly=(CardView) findViewById(R.id.monthlyre);

        mDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyre=new Intent(receiptselection.this,memberliste.class);
                Bundle extras = new Bundle();
                extras.putString("key", grpkey);
                extras.putString("meet", meetid);
                dailyre.putExtras(extras);
                startActivity(dailyre);
                finish();
            }
        });
        mMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monthlyre=new Intent(receiptselection.this,meetingreceipt.class);
                Bundle extras = new Bundle();
                extras.putString("key", grpkey);
                extras.putString("meet", meetid);
                monthlyre.putExtras(extras);
                startActivity(monthlyre);
                finish();
            }
        });
    }
}
