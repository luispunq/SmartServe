package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class empcash extends AppCompatActivity {
    private String userkey=null;
    private DatabaseReference mDatabaseUsers,mD,mD5;
    private String user_name=null,cash=null,fid;
    private View mview;

    private TextView outball,amoutin,amoutout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empcash);
        userkey =getIntent().getExtras().getString("key");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mview=findViewById(R.id.popup);

        outball=findViewById(R.id.outbal);
        amoutin=findViewById(R.id.amountgvn);
        amoutout=findViewById(R.id.amountrtn);




        int width=dm.widthPixels;
        int height=dm.heightPixels;


        getWindow().setLayout((int)(width),(int)(height*0.7));
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(userkey);



        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountin").exists()){
                    String bal=dataSnapshot.child("Balance").getValue().toString();
                    String amin=dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountin").getValue().toString();
                    String amout=dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountout").getValue().toString();

                    outball.setText("Kshs. "+bal);
                    amoutin.setText("Kshs. "+amin);
                    amoutout.setText("Kshs. "+amout);
                }else {
                    String bal=dataSnapshot.child("Balance").getValue().toString();
                    String amin="0";
                    String amout="0";

                    outball.setText("Kshs. "+bal);
                    amoutin.setText("Kshs. "+amin);
                    amoutout.setText("Kshs. "+amout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
