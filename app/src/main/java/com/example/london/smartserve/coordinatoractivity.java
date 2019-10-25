package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class
coordinatoractivity extends AppCompatActivity {
    private TextView mDate,mLocation,gname,mChange;
    private ImageView gImage;
    private String user,gkey=null,image=null,name=null,date=null,loc=null,facilit=null,fieldid=null,cashgiven=null,misc=null;
    private CardView card1,card2,card3,card4;
    private DatabaseReference mDatabase3,mData1,mD,mD1,mD2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatoractivity);
        Bundle extras = getIntent().getExtras();
        gkey = extras.getString("key");
        user = extras.getString("user");
        mAuth=FirebaseAuth.getInstance();
        mDate=findViewById(R.id.nowdate);
        mChange=findViewById(R.id.change);
        mLocation=findViewById(R.id.nowvenue);
        gname=findViewById(R.id.cordopgroupname);
        gImage=findViewById(R.id.cordopimageView);
        card1=findViewById(R.id.meetingcard);
        card2=findViewById(R.id.groupprof);
        card3=findViewById(R.id.updatess);
        card4=findViewById(R.id.addnewmem);
        mDatabase3= FirebaseDatabase.getInstance().getReference().child("details").child(gkey);
        //mDatabase3.keepSynced(true);
        mD=FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD1=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user);
        mD2=FirebaseDatabase.getInstance().getReference().child("meetings").child("insession");


        mDatabase3.child("groupdetails").child("meetid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mChange.setText("Continue Meeting");
                }else{
                    mChange.setText("Start Meeting");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facilit=dataSnapshot.child("name").getValue().toString();
                fieldid=dataSnapshot.child("fieldid").getValue().toString();

                mD1.child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("amountgiven").exists()){
                            //cashgiven=dataSnapshot.child("amountgiven").getValue().toString();
                            misc=dataSnapshot.child("misccash").getValue().toString();
                        }else {
                            cashgiven="0";
                            misc="0";
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase3.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("cashfromoffice").exists()) {
                    cashgiven = dataSnapshot.child("cashfromoffice").getValue().toString();
                    image = (String) dataSnapshot.child("profileImage").getValue();
                    name = (String) dataSnapshot.child("groupName").getValue();
                    date = dataSnapshot.child("nextdate").getValue().toString();
                    loc = (String) dataSnapshot.child("nextvenue").getValue();
                    gname.setText(name);
                    mDate.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    mLocation.setText(loc);
                    Picasso.with(coordinatoractivity.this).load(image).into(gImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent requestservice = new Intent(coordinatoractivity.this,meeting.class);

                DatabaseReference engageuser=mD;
                engageuser.child("status").setValue("Engaged");

                DatabaseReference checking=mDatabase3.child("groupdetails").child("meetid");
                checking.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            DatabaseReference newmeet=mDatabase3.child("meetings").push();
                            String newmeetid=newmeet.getKey();
                            newmeet.child("cashfromoffice").setValue(cashgiven);
                            newmeet.child("miscmoney").setValue(misc);
                            newmeet.child("fieldid").setValue(fieldid);
                            newmeet.child("facid").setValue(user);
                            newmeet.child("venue").setValue(loc);

                            DatabaseReference tempid=mDatabase3.child("temp").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            tempid.child("tempid").setValue(newmeetid);
                            tempid.child("fieldid").setValue(fieldid);
                            tempid.child("facid").setValue(user);


                            DatabaseReference session=mD2.push();
                            String skey=session.getKey();
                            session.child("groupName").setValue(name);
                            session.child("groupImage").setValue(image);
                            session.child("groupid").setValue(gkey);
                            session.child("cashfromoffice").setValue(cashgiven);
                            session.child("facilitator").setValue(facilit);


                            DatabaseReference id=mDatabase3.child("groupdetails");
                            id.child("meetid").setValue(newmeetid);
                            id.child("sessionid").setValue(skey);

                            requestservice.putExtra("key",gkey);
                            startActivity(requestservice);
                            finish();
                        }else{
                            requestservice.putExtra("key",gkey);
                            startActivity(requestservice);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewstats = new Intent(coordinatoractivity.this,groupstats.class);
                viewstats.putExtra("key",gkey);
                startActivity(viewstats);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewstats = new Intent(coordinatoractivity.this,groupinfo.class);
                viewstats.putExtra("key",gkey);
                startActivity(viewstats);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewstats = new Intent(coordinatoractivity.this,newmemberemail.class);
                //viewstats.putExtra("key",gkey);
                Bundle extras = new Bundle();
                extras.putString("key", gkey);
                viewstats.putExtras(extras);
                startActivity(viewstats);
            }
        });

    }
}
