package com.example.london.smartserve;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class groupinfo extends AppCompatActivity {
    private ImageView mGroupPhoto;
    private TextView mDate,mGroupname;
    private EditText mLocation;
    private Button mBtn;
    private DatabaseReference mDatabase,mDatabasemembers,mD1,mD2;
    private FirebaseAuth mAuth;
    private String user,groupid=null,grpkey=null;
    private ProgressDialog mProgress;
    private Calendar myCalendar;
    //private DatabaseReference m,mD3,mD4,mD5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        grpkey=getIntent().getExtras().getString("key");
        mProgress=new ProgressDialog(this);
        mGroupPhoto=findViewById(R.id.giimageView);
        mGroupname=findViewById(R.id.gigroupname);
        mDate=findViewById(R.id.nextdate);
        mLocation=findViewById(R.id.nextloc);
        mBtn=findViewById(R.id.button2);

        mD1=FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD2=FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        mDatabasemembers=FirebaseDatabase.getInstance().getReference().child("members").child(user);
        myCalendar=Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };




        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(groupinfo.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mDatabasemembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupid=(String)dataSnapshot.child("details").child("groupid").getValue();
                mDatabase.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String image=(String)dataSnapshot.child("profileImage").getValue();
                        String name=(String)dataSnapshot.child("groupName").getValue();
                        Picasso.with(groupinfo.this).load(image).into(mGroupPhoto);
                        mGroupname.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String date=mDate.getText().toString().trim();
                        String location=mLocation.getText().toString().trim();
                        mProgress.setTitle("Please Wait");
                        mProgress.setMessage("Updating Content");
                        mProgress.setCanceledOnTouchOutside(false);
                        mProgress.show();

                        DatabaseReference databaseReference=mDatabase.child("groupdetails");
                        databaseReference.child("nextdate").setValue(date);
                        databaseReference.child("nextvenue").setValue(location);
                        databaseReference.child("fieldid").removeValue();
                        databaseReference.child("meetid").removeValue();


                        DatabaseReference upcomings=mD2.child(date).push();
                        upcomings.child("groupid").setValue(grpkey);
                        upcomings.child("groupname").setValue(mGroupname.getText().toString());
                        upcomings.child("date").setValue(date);
                        upcomings.child("venue").setValue(location);
                        upcomings.child("assign").setValue("unassigned");

                        DatabaseReference removefiiel=mDatabase.child("groupdetails");
                        removefiiel.child("meetid").removeValue();

                        DatabaseReference ref=mDatabase.child("groupdetails");
                        ref.child("togivefac").setValue("0");

                        DatabaseReference removefiield=mD1;
                        removefiield.child("status").setValue("Not Engaged");



                        mProgress.dismiss();
                        Toast.makeText(groupinfo.this, "Updated!", Toast.LENGTH_LONG).show();

                        Intent loan = new Intent(groupinfo.this,coordinator.class);
                        loan.putExtra("user",user);
                        startActivity(loan);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void updateLabel() {
        String myFormat = "EEE, MMM d, ''yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mDate.setText(sdf.format(myCalendar.getTime()));
    }


}
