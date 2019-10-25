package com.example.london.smartserve;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class fieldstart extends AppCompatActivity {
    private EditText mCahgiven,mMisc;
    private DatabaseReference mCash,mEmployees,mDatabase,mData,mD,mAl,reference,mD5,mD4;
    private ImageView empImage;
    private TextView empname,mtitle;
    private FirebaseAuth mAuth;
    private String user,empid=null,gkey=null,fkey=null,g1=null,g2=null,g3=null,g4=null,name=null;
    private Button done,start,swit;
    private RecyclerView upcoming,mAll;
    private ProgressDialog mProgress;
    private double tt=0,fffa=0;
    private TimerTask timerTask;
    //make sure activity finishes when closed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldstart);
        Bundle extras = getIntent().getExtras();
        empid = extras.getString("id");
        fkey = extras.getString("fid");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        empname=findViewById(R.id.employeename);
        mtitle=findViewById(R.id.textView22);
        empImage=findViewById(R.id.employeeimage);
        mtitle=findViewById(R.id.textView22);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mCash= FirebaseDatabase.getInstance().getReference().child("fieldwork").child(empid);
        mEmployees=FirebaseDatabase.getInstance().getReference().child("Employees").child(empid);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule");
        mData=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(empid);
        mD=FirebaseDatabase.getInstance().getReference().child("details");
        mAl=FirebaseDatabase.getInstance().getReference().child("Groups");
        reference=FirebaseDatabase.getInstance().getReference().child("Notifications").child("FieldStart");
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(empid);
        mD4=FirebaseDatabase.getInstance().getReference().child("members");
        mCahgiven=findViewById(R.id.moneygiven);
        mMisc=findViewById(R.id.moneygiven2);
        done=findViewById(R.id.button6);
        start=findViewById(R.id.button7);
        upcoming=findViewById(R.id.upcomingmeetings);
        mAll=findViewById(R.id.allmeetings);
        swit=findViewById(R.id.button8);
        mAll.setHasFixedSize(true);
        mAll.setLayoutManager(new LinearLayoutManager(this));
        upcoming.setHasFixedSize(true);
        upcoming.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        mProgress=new ProgressDialog(this);



        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("amountgiven")){
                            mCahgiven.setVisibility(View.GONE);
                            mMisc.setVisibility(View.GONE);
                            done.setVisibility(View.GONE);
                            mtitle.setText("Kshs. "+dataSnapshot.child("amountgiven").getValue().toString());
                        }else{
                            mCahgiven.setVisibility(View.VISIBLE);
                            mMisc.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        thread.start();


        swit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upcoming.getVisibility()==View.VISIBLE){
                    upcoming.setVisibility(View.INVISIBLE);
                    mAll.setVisibility(View.VISIBLE);
                }else {
                    upcoming.setVisibility(View.VISIBLE);
                    mAll.setVisibility(View.INVISIBLE);
                }
            }
        });

        mEmployees.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue().toString();
                String imge=dataSnapshot.child("image").getValue().toString();
                empname.setText(name);
                Picasso.with(fieldstart.this).load(imge).into(empImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //done.setVisibility(View.VISIBLE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bac = new Intent(fieldstart.this,masterhome.class);
                startActivity(bac);
                finish();
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<upcomingmeeting,upcomingMeetingsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<upcomingmeeting, upcomingMeetingsViewHolder>(
                upcomingmeeting.class,
                R.layout.upcomingmeeting_row,
                upcomingMeetingsViewHolder.class,
                mDatabase.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()))
        )
        {
            @Override
            protected void populateViewHolder(final upcomingMeetingsViewHolder viewHolder, final upcomingmeeting model, final int position) {
                final String b_key=model.getGroupid();
                final String kk=getRef(position).getKey();
                gkey=model.getGroupid();

                mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(model.getGroupid())){
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        }else {
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                if (model.getAssign().equals("unassigned"))
                {
                    viewHolder.setGroupName(model.getGroupName());
                    viewHolder.setVenue(model.getVenue());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setAmount(model.getGroupid());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.child("Balance").exists()){
                                        if (Double.parseDouble(dataSnapshot.child("Balance").getValue().toString())<1.0){
                                            mCash.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").hasChild(b_key)){

                                                        mD.child(b_key).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("togivefac").exists()){
                                                                    tt=tt+Double.parseDouble(dataSnapshot.child("togivefac").getValue().toString());
                                                                    mCahgiven.setText(String.valueOf(tt));
                                                                }
                                                                else{
                                                                    tt=0;
                                                                    mCahgiven.setText(String.valueOf("0"));
                                                                }
                                                                LinearLayout layout=new LinearLayout(fieldstart.this);
                                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                                LinearLayout layout2=new LinearLayout(fieldstart.this);
                                                                layout2.setOrientation(LinearLayout.HORIZONTAL);
                                                                final EditText taskedit=new EditText(fieldstart.this);
                                                                taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                final EditText taskeditfare=new EditText(fieldstart.this);
                                                                taskeditfare.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                final EditText taskedit2=new EditText(fieldstart.this);
                                                                taskedit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                final TextView taskedit3=new TextView(fieldstart.this);
                                                                taskedit3.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                final CheckBox checkBox=new CheckBox(fieldstart.this);
                                                                taskedit.setGravity(Gravity.START);
                                                                checkBox.setGravity(Gravity.END);


                                                                layout2.addView(taskeditfare);
                                                                layout2.addView(taskedit3);
                                                                layout2.addView(checkBox);
                                                                taskedit.setHint("Enter Cash Assigned");
                                                                taskeditfare.setHint("Enter Cash For Withdrawal");
                                                                taskedit3.setText("Withdrawal: ");
                                                                taskedit2.setHint("Enter Misc. Cash");
                                                                layout.addView(taskedit);
                                                                layout.addView(layout2);
                                                                layout.addView(taskedit2);
                                                                final AlertDialog.Builder builders = new AlertDialog.Builder(fieldstart.this);
                                                                builders.setTitle("Confirm Assignment")
                                                                        .setMessage("Assign "+name+" Kshs. "+mCahgiven.getText().toString())
                                                                        .setView(layout)
                                                                        .setCancelable(true)
                                                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                if (checkBox.isChecked()){
                                                                                    final double ffa=Double.parseDouble(taskeditfare.getText().toString());
                                                                                    DatabaseReference rr=mD.child(gkey).child("groupdetails");
                                                                                    rr.child("withdrawal").setValue(taskeditfare.getText().toString());
                                                                                    final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                    mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.child("amountgiven").exists()){
                                                                                                String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                                                String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                                                double amb4d=Double.parseDouble(amb4);
                                                                                                double newamnt=Double.parseDouble(taskedit.getText().toString());
                                                                                                double miscb4d=Double.parseDouble(miscb4);
                                                                                                double newmisc=Double.parseDouble(taskedit2.getText().toString());

                                                                                                double updated=newamnt+amb4d+ffa;
                                                                                                double updatedmisc=newmisc+miscb4d;

                                                                                                newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                                newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                                                newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                                DatabaseReference endmeetnot=reference.child(empid);
                                                                                                endmeetnot.child("groupid").setValue(b_key);
                                                                                                endmeetnot.child("empid").setValue(empid);
                                                                                            }else {
                                                                                                String amb4="0";
                                                                                                double amb4d=Double.parseDouble(amb4);

                                                                                                double newamnt=Double.parseDouble(taskedit.getText().toString());

                                                                                                double updated=newamnt+amb4d+ffa;
                                                                                                newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                                newfield.child("misccash").setValue(taskedit2.getText().toString());
                                                                                                newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                                                DatabaseReference endmeetnot=reference.child(empid);
                                                                                                endmeetnot.child("groupid").setValue(b_key);
                                                                                                endmeetnot.child("empid").setValue(empid);
                                                                                            }

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                    DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                                                    w.child("cashfromoffice").setValue(taskedit.getText().toString());
                                                                                    w.child("transport").setValue(taskedit2.getText().toString());

                                                                                    DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").child(b_key);
                                                                                    putfielddata.child("name").setValue(model.getGroupName());
                                                                                    putfielddata.child("groupid").setValue(b_key);
                                                                                    putfielddata.child("status").setValue("pending");
                                                                                    putfielddata.child("fare").setValue(taskeditfare.getText().toString());
                                                                                    putfielddata.child("meetid").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                    putfielddata.child("amount").setValue(taskedit.getText().toString());
                                                                                    DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                    flagss.child("gaflags").setValue("flags");


                                                                                    mD4.child("allmembers").child(gkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            final String date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                                                                                            String key=gkey;
                                                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                final String uid=snapshot.child("uid").getValue().toString();
                                                                                                mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot3) {
                                                                                                        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                                                                                        if (dataSnapshot3.child(uid).child("loans").child("totalloan").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("totalloan").getValue().toString())>0){
                                                                                                            memstat.child("loans").child("nextloanpaydate").setValue(date);
                                                                                                        }
                                                                                                        if (dataSnapshot3.child(uid).child("advances").child("currentadvance").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currentadvance").getValue().toString())>0){
                                                                                                            memstat.child("advances").child("nextadvpaydate").setValue(date);
                                                                                                        }
                                                                                                        memstat.child("sftemptrs").removeValue();
                                                                                                        memstat.child("temptrs").removeValue();
                                                                                                        memstat.child("ptemptrs").removeValue();
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                    DatabaseReference updatefkey=mEmployees.child("fieldid");
                                                                                    updatefkey.setValue(fkey);
                                                                                    Toast.makeText(fieldstart.this,"Assigned!", Toast.LENGTH_LONG).show();
                                                                                    dialog.dismiss();
                                                                                }else {
                                                                                    final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                    mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.child("amountgiven").exists()){
                                                                                                String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                                                String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                                                double amb4d=Double.parseDouble(amb4);
                                                                                                double newamnt=Double.parseDouble(taskedit.getText().toString());
                                                                                                double miscb4d=Double.parseDouble(miscb4);
                                                                                                double newmisc=Double.parseDouble(taskedit2.getText().toString());

                                                                                                double updated=newamnt+amb4d;
                                                                                                double updatedmisc=newmisc+miscb4d;

                                                                                                newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                                newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                                                newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                                DatabaseReference endmeetnot=reference.child(empid);
                                                                                                endmeetnot.child("groupid").setValue(b_key);
                                                                                                endmeetnot.child("empid").setValue(empid);
                                                                                            }else {
                                                                                                String amb4="0";
                                                                                                double amb4d=Double.parseDouble(amb4);

                                                                                                double newamnt=Double.parseDouble(taskedit.getText().toString());

                                                                                                double updated=newamnt+amb4d;
                                                                                                newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                                newfield.child("misccash").setValue(taskedit2.getText().toString());
                                                                                                newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                                                DatabaseReference endmeetnot=reference.child(empid);
                                                                                                endmeetnot.child("groupid").setValue(b_key);
                                                                                                endmeetnot.child("empid").setValue(empid);
                                                                                            }

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                    DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                                                    w.child("cashfromoffice").setValue(taskedit.getText().toString());
                                                                                    w.child("transport").setValue(taskedit2.getText().toString());

                                                                                    DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").child(b_key);
                                                                                    putfielddata.child("name").setValue(model.getGroupName());
                                                                                    putfielddata.child("groupid").setValue(b_key);
                                                                                    putfielddata.child("status").setValue("pending");
                                                                                    putfielddata.child("fare").setValue(taskedit2.getText().toString());
                                                                                    putfielddata.child("meetid").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                    putfielddata.child("amount").setValue(taskedit.getText().toString());
                                                                                    DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                    flagss.child("gaflags").setValue("flags");


                                                                                    mD4.child("allmembers").child(gkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            final String date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                                                                                            String key=gkey;
                                                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                final String uid=snapshot.child("uid").getValue().toString();
                                                                                                mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot3) {
                                                                                                        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                                                                                        if (dataSnapshot3.child(uid).child("loans").child("totalloan").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("totalloan").getValue().toString())>0){
                                                                                                            memstat.child("loans").child("nextloanpaydate").setValue(date);
                                                                                                        }
                                                                                                        if (dataSnapshot3.child(uid).child("advances").child("currentadvance").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currentadvance").getValue().toString())>0){
                                                                                                            memstat.child("advances").child("nextadvpaydate").setValue(date);
                                                                                                        }
                                                                                                        memstat.child("sftemptrs").removeValue();
                                                                                                        memstat.child("temptrs").removeValue();
                                                                                                        memstat.child("ptemptrs").removeValue();
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                    DatabaseReference updatefkey=mEmployees.child("fieldid");
                                                                                    updatefkey.setValue(fkey);
                                                                                    Toast.makeText(fieldstart.this,"Assigned!", Toast.LENGTH_LONG).show();
                                                                                    dialog.dismiss();
                                                                                }

                                                                            }
                                                                        })
                                                                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                AlertDialog alert121 = builders.create();
                                                                alert121.show();

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                        Toast.makeText(fieldstart.this,"Group Added for "+name,Toast.LENGTH_LONG).show();
                                                    }else{
                                                        DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven");
                                                        putfielddata.child(b_key).removeValue();
                                                        DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                        flagss.child("gaflags").setValue("flag");

                                                        mD.child(b_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                final double transi=Double.parseDouble(dataSnapshot.child("groupdetails").child("transport").getValue().toString());
                                                                final double amb=Double.parseDouble(dataSnapshot.child("groupdetails").child("cashfromoffice").getValue().toString());
                                                                final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.child("amountgiven").exists()){
                                                                            if (dataSnapshot.child("groupsgiven").child(b_key).hasChild("fare")){
                                                                                fffa=Double.parseDouble(dataSnapshot.child("groupsgiven").child(b_key).child("fare").getValue().toString());
                                                                            }else {
                                                                                fffa=0;
                                                                            }
                                                                            String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                            double miscb4d=Double.parseDouble(miscb4);
                                                                            String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                            double amb4d=Double.parseDouble(amb4);


                                                                            double updatedmisc=miscb4d-transi;
                                                                            double updated=amb4d-amb-fffa;

                                                                            newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                            newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                            newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                        }else {
                                                                            if (dataSnapshot.child("groupsgiven").child(b_key).hasChild("fare")){
                                                                                fffa=Double.parseDouble(dataSnapshot.child("groupsgiven").child(b_key).child("fare").getValue().toString());
                                                                            }else {
                                                                                fffa=0;
                                                                            }
                                                                            String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                            double miscb4d=Double.parseDouble(miscb4);
                                                                            String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                            double amb4d=Double.parseDouble(amb4);


                                                                            double updatedmisc=miscb4d-transi;
                                                                            double updated=amb4d-amb-fffa;

                                                                            newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                            newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                            newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

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

                                                        Toast.makeText(fieldstart.this,"Group Removed",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }else {
                                            Toast.makeText(fieldstart.this,"Please make sure you handed over previous banking",Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        DatabaseReference reference=mD5;
                                        reference.child("Balance").setValue("0");
                                        Toast.makeText(fieldstart.this,"Please try again",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        upcoming.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<groups,GroupViewholder> firebaseRecyclerAdapte = new FirebaseRecyclerAdapter<groups, GroupViewholder>(

                groups.class,
                R.layout.groupslist_row,
                GroupViewholder.class,
                mAl.orderByChild("name")
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final groups model, final int position) {
                final String b_key=getRef(position).getKey();
                gkey=b_key;
                mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(getRef(position).getKey())){
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        }else {
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.setGroupName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Balance").exists()){
                                if (Double.parseDouble(dataSnapshot.child("Balance").getValue().toString())<1.0){
                                    mCash.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").hasChild(b_key)){

                                                mD.child(b_key).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.child("togivefac").exists()){
                                                            tt=tt+Double.parseDouble(dataSnapshot.child("togivefac").getValue().toString());
                                                            mCahgiven.setText(String.valueOf(tt));
                                                        }
                                                        else{
                                                            tt=0;
                                                            mCahgiven.setText(String.valueOf("0"));
                                                        }
                                                        LinearLayout layout=new LinearLayout(fieldstart.this);
                                                        layout.setOrientation(LinearLayout.VERTICAL);
                                                        LinearLayout layout2=new LinearLayout(fieldstart.this);
                                                        layout2.setOrientation(LinearLayout.HORIZONTAL);
                                                        final EditText taskedit=new EditText(fieldstart.this);
                                                        taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        final EditText taskeditfare=new EditText(fieldstart.this);
                                                        taskeditfare.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        final EditText taskedit2=new EditText(fieldstart.this);
                                                        taskedit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        final TextView taskedit3=new TextView(fieldstart.this);
                                                        taskedit3.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        final CheckBox checkBox=new CheckBox(fieldstart.this);
                                                        taskedit.setGravity(Gravity.START);
                                                        checkBox.setGravity(Gravity.END);


                                                        layout2.addView(taskeditfare);
                                                        layout2.addView(taskedit3);
                                                        layout2.addView(checkBox);
                                                        taskedit.setHint("Enter Cash Assigned");
                                                        taskeditfare.setHint("Enter Cash For Withdrawal");
                                                        taskedit3.setText("Withdrawal: ");
                                                        taskedit2.setHint("Enter Misc. Cash");
                                                        layout.addView(taskedit);
                                                        layout.addView(layout2);
                                                        layout.addView(taskedit2);
                                                        final AlertDialog.Builder builders = new AlertDialog.Builder(fieldstart.this);
                                                        builders.setTitle("Confirm Assignment")
                                                                .setMessage("Assign "+name+" Kshs. "+mCahgiven.getText().toString())
                                                                .setView(layout)
                                                                .setCancelable(true)
                                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        if (checkBox.isChecked()){
                                                                            final double ffa=Double.parseDouble(taskeditfare.getText().toString());
                                                                            DatabaseReference rr=mD.child(gkey).child("groupdetails");
                                                                            rr.child("withdrawal").setValue(taskeditfare.getText().toString());
                                                                            final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                            mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (dataSnapshot.child("amountgiven").exists()){
                                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                                        String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                                        double amb4d=Double.parseDouble(amb4);
                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());
                                                                                        double miscb4d=Double.parseDouble(miscb4);
                                                                                        double newmisc=Double.parseDouble(taskedit2.getText().toString());

                                                                                        double updated=newamnt+amb4d+ffa;
                                                                                        double updatedmisc=newmisc+miscb4d;

                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                        DatabaseReference endmeetnot=reference.child(empid);
                                                                                        endmeetnot.child("groupid").setValue(b_key);
                                                                                        endmeetnot.child("empid").setValue(empid);
                                                                                    }else {
                                                                                        String amb4="0";
                                                                                        double amb4d=Double.parseDouble(amb4);

                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());

                                                                                        double updated=newamnt+amb4d+ffa;
                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(taskedit2.getText().toString());
                                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                                        DatabaseReference endmeetnot=reference.child(empid);
                                                                                        endmeetnot.child("groupid").setValue(b_key);
                                                                                        endmeetnot.child("empid").setValue(empid);
                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                                            w.child("cashfromoffice").setValue(taskedit.getText().toString());
                                                                            w.child("transport").setValue(taskedit2.getText().toString());

                                                                            DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").child(b_key);
                                                                            putfielddata.child("name").setValue(model.getName());
                                                                            putfielddata.child("groupid").setValue(b_key);
                                                                            putfielddata.child("status").setValue("pending");
                                                                            putfielddata.child("fare").setValue(taskeditfare.getText().toString());
                                                                            putfielddata.child("meetid").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                            putfielddata.child("amount").setValue(taskedit.getText().toString());
                                                                            DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                            flagss.child("gaflags").setValue("flags");


                                                                            mD4.child("allmembers").child(gkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    final String date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                                                                                    String key=gkey;
                                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                        final String uid=snapshot.child("uid").getValue().toString();
                                                                                        mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                                                                final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                                                                                if (dataSnapshot3.child(uid).child("loans").child("totalloan").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("totalloan").getValue().toString())>0){
                                                                                                    memstat.child("loans").child("nextloanpaydate").setValue(date);
                                                                                                }
                                                                                                if (dataSnapshot3.child(uid).child("advances").child("currentadvance").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currentadvance").getValue().toString())>0){
                                                                                                    memstat.child("advances").child("nextadvpaydate").setValue(date);
                                                                                                }
                                                                                                memstat.child("sftemptrs").removeValue();
                                                                                                memstat.child("temptrs").removeValue();
                                                                                                memstat.child("ptemptrs").removeValue();
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });

                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            DatabaseReference updatefkey=mEmployees.child("fieldid");
                                                                            updatefkey.setValue(fkey);
                                                                            Toast.makeText(fieldstart.this,"Assigned!", Toast.LENGTH_LONG).show();
                                                                            dialog.dismiss();
                                                                        }else {
                                                                            final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                            mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (dataSnapshot.child("amountgiven").exists()){
                                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                                        String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                                        double amb4d=Double.parseDouble(amb4);
                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());
                                                                                        double miscb4d=Double.parseDouble(miscb4);
                                                                                        double newmisc=Double.parseDouble(taskedit2.getText().toString());

                                                                                        double updated=newamnt+amb4d;
                                                                                        double updatedmisc=newmisc+miscb4d;

                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                                        DatabaseReference endmeetnot=reference.child(empid);
                                                                                        endmeetnot.child("groupid").setValue(b_key);
                                                                                        endmeetnot.child("empid").setValue(empid);
                                                                                    }else {
                                                                                        String amb4="0";
                                                                                        double amb4d=Double.parseDouble(amb4);

                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());

                                                                                        double updated=newamnt+amb4d;
                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(taskedit2.getText().toString());
                                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                                        DatabaseReference endmeetnot=reference.child(empid);
                                                                                        endmeetnot.child("groupid").setValue(b_key);
                                                                                        endmeetnot.child("empid").setValue(empid);
                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                                            w.child("cashfromoffice").setValue(taskedit.getText().toString());
                                                                            w.child("transport").setValue(taskedit2.getText().toString());

                                                                            DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").child(b_key);
                                                                            putfielddata.child("name").setValue(model.getName());
                                                                            putfielddata.child("groupid").setValue(b_key);
                                                                            putfielddata.child("status").setValue("pending");
                                                                            putfielddata.child("fare").setValue(taskedit2.getText().toString());
                                                                            putfielddata.child("meetid").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                            putfielddata.child("amount").setValue(taskedit.getText().toString());
                                                                            DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                            flagss.child("gaflags").setValue("flags");


                                                                            mD4.child("allmembers").child(gkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    final String date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                                                                                    String key=gkey;
                                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                        final String uid=snapshot.child("uid").getValue().toString();
                                                                                        mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot3) {
                                                                                                final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                                                                                if (dataSnapshot3.child(uid).child("loans").child("totalloan").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("totalloan").getValue().toString())>0){
                                                                                                    memstat.child("loans").child("nextloanpaydate").setValue(date);
                                                                                                }
                                                                                                if (dataSnapshot3.child(uid).child("advances").child("currentadvance").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currentadvance").getValue().toString())>0){
                                                                                                    memstat.child("advances").child("nextadvpaydate").setValue(date);
                                                                                                }
                                                                                                memstat.child("sftemptrs").removeValue();
                                                                                                memstat.child("temptrs").removeValue();
                                                                                                memstat.child("ptemptrs").removeValue();
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });

                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            DatabaseReference updatefkey=mEmployees.child("fieldid");
                                                                            updatefkey.setValue(fkey);
                                                                            Toast.makeText(fieldstart.this,"Assigned!", Toast.LENGTH_LONG).show();
                                                                            dialog.dismiss();
                                                                        }

                                                                    }
                                                                })
                                                                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                        AlertDialog alert121 = builders.create();
                                                        alert121.show();

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                Toast.makeText(fieldstart.this,"Group Added for "+name,Toast.LENGTH_LONG).show();
                                            }else{
                                                DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven");
                                                putfielddata.child(b_key).removeValue();
                                                DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                flagss.child("gaflags").setValue("flag");

                                                mD.child(b_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        final double transi=Double.parseDouble(dataSnapshot.child("groupdetails").child("transport").getValue().toString());
                                                        final double amb=Double.parseDouble(dataSnapshot.child("groupdetails").child("cashfromoffice").getValue().toString());
                                                        final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                        mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("amountgiven").exists()){
                                                                    if (dataSnapshot.child("groupsgiven").child(b_key).hasChild("fare")){
                                                                        fffa=Double.parseDouble(dataSnapshot.child("groupsgiven").child(b_key).child("fare").getValue().toString());
                                                                    }else {
                                                                        fffa=0;
                                                                    }
                                                                    String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                    double miscb4d=Double.parseDouble(miscb4);
                                                                    String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                    double amb4d=Double.parseDouble(amb4);


                                                                    double updatedmisc=miscb4d-transi;
                                                                    double updated=amb4d-amb-fffa;

                                                                    newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                    newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                    newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                }else {
                                                                    if (dataSnapshot.child("groupsgiven").child(b_key).hasChild("fare")){
                                                                        fffa=Double.parseDouble(dataSnapshot.child("groupsgiven").child(b_key).child("fare").getValue().toString());
                                                                    }else {
                                                                        fffa=0;
                                                                    }
                                                                    String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                    double miscb4d=Double.parseDouble(miscb4);
                                                                    String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                    double amb4d=Double.parseDouble(amb4);


                                                                    double updatedmisc=miscb4d-transi;
                                                                    double updated=amb4d-amb-fffa;

                                                                    newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                    newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                    newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

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

                                                Toast.makeText(fieldstart.this,"Group Removed",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else {
                                    Toast.makeText(fieldstart.this,"Please make sure you handed over previous banking",Toast.LENGTH_LONG).show();
                                }
                                }else {
                                DatabaseReference reference=mD5;
                                reference.child("Balance").setValue("0");
                                Toast.makeText(fieldstart.this,"Please try again",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


            }
        };

        mAll.setAdapter(firebaseRecyclerAdapte);
    }

    private void runInBackground(final String date,final String key) {
        mProgress.setTitle("Please Wait");
        mProgress.setMessage("Updating Member details");
        mProgress.setCanceledOnTouchOutside(false);

        new Thread() {


            public void run() {
                //Implementing your long running operations
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot3) {

                        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    final String uid=snapshot.child("uid").getValue().toString();
                                    final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                    if (dataSnapshot3.child(uid).child("loans").child("installment").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("installment").getValue().toString())>0){
                                        memstat.child("loans").child("nextloanpaydate").setValue(date);
                                    }
                                    if (dataSnapshot3.child(uid).child("advances").child("currpenalty").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currpenalty").getValue().toString())>0){
                                        memstat.child("advances").child("nextadvpaydate").setValue(date);
                                    }
                                    memstat.child("sftemptrs").removeValue();
                                    memstat.child("ptemptrs").removeValue();
                                    mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("temptrs").exists()){
                                                DatabaseReference rrr=mD4.child(uid);
                                                rrr.child("temptrs").removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
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
            }
        }.start();
    }

    public static class upcomingMeetingsViewHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;
        private DatabaseReference m;
        private EditText mCag;


        public upcomingMeetingsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            m=FirebaseDatabase.getInstance().getReference().child("details");
            layout =  itemView.findViewById(R.id.upcmngcard);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.upgrpname);
            mGroupname.setText(groupname);
        }
        public void setVenue(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpven);
            mGrouploc.setText(venue);
        }
        public void setDate(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpdate);
            mGrouploc.setText(date);
        }
        public void setAmount(String amount){
            m.child(amount).child("groupdetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("togivefac").exists()){
                    String n=dataSnapshot.child("togivefac").getValue().toString();
                    TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                    mGrouploc.setText("Kshs. "+n);
                    }else {
                        String n="0";
                        TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                        mGrouploc.setText("Kshs. "+n);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public static class GroupViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupslistcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textView9);
            mGroupname.setText(groupname);
        }
    }




    private void setupmemberstat(final String date, final String key) {

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot3) {

                mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String uid=snapshot.child("uid").getValue().toString();
                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                            if (dataSnapshot3.child(uid).child("loans").child("installment").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("installment").getValue().toString())>0){
                                memstat.child("loans").child("nextloanpaydate").setValue(date);
                            }
                            if (dataSnapshot3.child(uid).child("advances").child("currpenalty").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currpenalty").getValue().toString())>0){
                                memstat.child("advances").child("nextadvpaydate").setValue(date);
                            }
                            memstat.child("sftemptrs").removeValue();
                            memstat.child("temptrs").removeValue();
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

    }

    public static class MyAsyncTask extends AsyncTask<String, String, Void> {

        DatabaseReference mD4=FirebaseDatabase.getInstance().getReference().child("members");

        @Override
        protected Void doInBackground(String... strings) {
            final String date=strings[0];
            final String key=strings[1];

            mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot3) {

                    mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String uid=snapshot.child("uid").getValue().toString();
                                final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                if (dataSnapshot3.child(uid).child("loans").child("installment").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("loans").child("installment").getValue().toString())>0){
                                    memstat.child("loans").child("nextloanpaydate").setValue(date);
                                }
                                if (dataSnapshot3.child(uid).child("advances").child("currpenalty").exists()&&Double.parseDouble(dataSnapshot3.child(uid).child("advances").child("currpenalty").getValue().toString())>0){
                                    memstat.child("advances").child("nextadvpaydate").setValue(date);
                                }
                                memstat.child("sftemptrs").removeValue();
                                memstat.child("temptrs").removeValue();
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

            return null;
        }
    }

}
