package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaCas;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class meeting extends AppCompatActivity {
    private RelativeLayout lay1,lay2,lay3,lay4,lay5,lay6,lay7;
    private LinearLayout layoutL,layoutK,layoutJ;
    private TextView mName,mDesc;
    private TextInputEditText mAttndance;
    private TextInputLayout mlayt1;
    private ImageView gPhoto;
    private CheckBox ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8;
    private Button conclude,save1;
    private CardView c1,c2,c3,c4,c5,c6,c7,c8,c9;
    private DatabaseReference mDatabasegroups,mData,mD1,mD2,mD3,mD4,mD5,mD6,mDr;
    private String user,gkey,meetid,facilit=null,fieldid=null,cashgiven=null,sessionid=null,date=null,oldmisc;
    private FirebaseAuth mAuth;
    private String loc=null,flag,ploan,image,ggname;
    private DatabaseReference mD7;
    private List<String> members = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        gkey=getIntent().getExtras().getString("key");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        layoutL=findViewById(R.id.pay);
        layoutK=findViewById(R.id.companypay);
        layoutJ=findViewById(R.id.apps);
        mDesc=findViewById(R.id.desc);
        mName=findViewById(R.id.textView20);
        mAttndance=findViewById(R.id.attendance);
        mlayt1=findViewById(R.id.txtinput1);
        lay1=findViewById(R.id.step1);
        lay2=findViewById(R.id.step2);
        lay3=findViewById(R.id.step3);
        lay4=findViewById(R.id.step4);
        lay5=findViewById(R.id.step5);
        lay6=findViewById(R.id.step6);
        lay7=findViewById(R.id.step7);

        gPhoto=findViewById(R.id.imageView7);

        ch1=findViewById(R.id.checkBox);
        ch2=findViewById(R.id.checkBox2);
        ch3=findViewById(R.id.checkBox3);
        ch4=findViewById(R.id.checkBox4);
        ch5=findViewById(R.id.checkBox5);
        ch6=findViewById(R.id.checkBox6);
        ch7=findViewById(R.id.checkBox7);
        ch8=findViewById(R.id.checkBox8);

        conclude=findViewById(R.id.button3);
        save1=findViewById(R.id.button4);
        c1=findViewById(R.id.acceptpay);
        c2=findViewById(R.id.viewreport);
        c3=findViewById(R.id.makepay);
        c4=findViewById(R.id.viewreports);
        c5=findViewById(R.id.loans);
        c6=findViewById(R.id.advances);
        c7=findViewById(R.id.viewreceipts);
        c8=findViewById(R.id.viewaddefa);



        mDatabasegroups= FirebaseDatabase.getInstance().getReference().child("details").child(gkey);
        mData=FirebaseDatabase.getInstance().getReference().child("meetings").child("insession");
        mD1=FirebaseDatabase.getInstance().getReference().child("finances").child(gkey);
        mD2=FirebaseDatabase.getInstance().getReference().child("Notifications");
        mD3=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user);
        mD4=FirebaseDatabase.getInstance().getReference().child("details").child(gkey);
        mDr=FirebaseDatabase.getInstance().getReference().child("Accounts").child(user);
        mD5=FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(gkey);
        mD6=FirebaseDatabase.getInstance().getReference().child("members");
        mD7=FirebaseDatabase.getInstance().getReference().child("masterfinance");




        mDatabasegroups.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image=(String)dataSnapshot.child("profileImage").getValue();
                ggname=(String)dataSnapshot.child("groupName").getValue();
                cashgiven=dataSnapshot.child("cashfromoffice").getValue().toString();
                sessionid=dataSnapshot.child("sessionid").getValue().toString();
                loc=(String)dataSnapshot.child("nextvenue").getValue();
                mName.setText(ggname+" Meeting");
                Picasso.with(meeting.this).load(image).into(gPhoto);
                meetid=(String)dataSnapshot.child("meetid").getValue();
                if (dataSnapshot.child("date").exists()){
                    date=dataSnapshot.child("date").getValue().toString();
                }else {
                    date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                }
                Log.e("meetid",meetid);
                mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("date").exists())
                        {
                            Log.e("path","1");
                            date=dataSnapshot.child("date").getValue().toString();
                        }else {
                            Log.e("path","2");
                            date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                        }

                        Log.e("date",date);

                        mDatabasegroups.child("meetings").child(meetid).child("flag").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()){
                                    lay3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mlayt1.setVisibility(View.VISIBLE);
                                            save1.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    lay5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            layoutL.setVisibility(View.VISIBLE);
                                            mDesc.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    lay6.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            layoutK.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    lay7.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            layoutJ.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    save1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String detail=mAttndance.getText().toString();


                                            DatabaseReference startmeet=mDatabasegroups.child("meetings").child(meetid);
                                            startmeet.keepSynced(true);
                                            startmeet.child("cashfromoffice").setValue(cashgiven);
                                            startmeet.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                            startmeet.child("status").setValue("in session");
                                            startmeet.child("attendance").setValue(detail);
                                            startmeet.child("flag").setValue("yes");

                                            DatabaseReference startmeetnot=mD2.child("MeetingStart").push();
                                            startmeetnot.child("name").setValue(ggname);

                                            mD1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String oldsavings=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                    if (dataSnapshot.child("loans").child("currentloanbf").exists()){
                                                        String oldloans=dataSnapshot.child("loans").child("currentloanbf").getValue().toString();
                                                        String oldadvance=dataSnapshot.child("advances").child("currentadvancebf").getValue().toString();
                                                        DatabaseReference startmeet=mDatabasegroups.child("meetings").child(meetid);
                                                        startmeet.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        startmeet.child("status").setValue("in session");
                                                        startmeet.child("attendance").setValue(detail);
                                                        startmeet.child("flag").setValue("yes");
                                                        startmeet.child("groupsavingsbf").setValue(oldsavings);
                                                        startmeet.child("grouploansbf").setValue(oldloans);
                                                        startmeet.child("groupadvancesbf").setValue(oldadvance);


                                                        FirebaseDatabase.getInstance().getReference().child("finances").child(gkey).child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("currentadvancebf").exists()){
                                                                    DatabaseReference startmeet=mDatabasegroups.child("schoolfees").child("meetings").child(meetid);

                                                                    startmeet.child("groupadvancesbf").setValue(dataSnapshot.child("currentadvancebf").getValue().toString());
                                                                }else {
                                                                    DatabaseReference startmeet=mDatabasegroups.child("schoolfees").child("meetings").child(meetid);

                                                                    startmeet.child("groupadvancesbf").setValue("0");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                    else {
                                                        String oldloans="0";
                                                        String oldadvance="0";
                                                        DatabaseReference startmeet=mDatabasegroups.child("meetings").child(meetid);
                                                        startmeet.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        startmeet.child("status").setValue("in session");
                                                        startmeet.child("attendance").setValue(detail);
                                                        startmeet.child("flag").setValue("yes");
                                                        startmeet.child("groupsavingsbf").setValue(oldsavings);
                                                        startmeet.child("grouploansbf").setValue(oldloans);
                                                        startmeet.child("groupadvancesbf").setValue(oldadvance);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        if (!members.contains(snapshot.child("uid").getValue().toString())){
                                                            final String nname=snapshot.child("name").getValue().toString();
                                                            final String uid=snapshot.child("uid").getValue().toString();
                                                            //final String id=snapshot.child("id").getValue().toString();

                                                            mD6.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String lsfcf=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                                    String loancf=dataSnapshot.child("loans").child("totalloan").getValue().toString();
                                                                    String advancecf=dataSnapshot.child("advances").child("currentadvance").getValue().toString();

                                                                    DatabaseReference ptmps=mD4.child("transactions").child(meetid).push();
                                                                    ptmps.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                                    ptmps.child("totalamount").setValue("0");
                                                                    ptmps.child("lsf").setValue("0");
                                                                    ptmps.child("wcomm").setValue("0");
                                                                    ptmps.child("loaninstallments").setValue("0");
                                                                    ptmps.child("advancepayment").setValue("0");
                                                                    ptmps.child("fines").setValue("0");
                                                                    ptmps.child("lsfcf").setValue(lsfcf);
                                                                    ptmps.child("advanceinterest").setValue("0");
                                                                    ptmps.child("wamount").setValue("0");
                                                                    ptmps.child("paymentmode").setValue("0");
                                                                    ptmps.child("insurance").setValue("0");
                                                                    ptmps.child("memberid").setValue(nname);
                                                                    ptmps.child("Mpesa").setValue("0");
                                                                    ptmps.child("id").setValue(uid);
                                                                    ptmps.child("loangvn").setValue("0");
                                                                    ptmps.child("loancf").setValue(loancf);
                                                                    ptmps.child("advgvn").setValue("0");
                                                                    ptmps.child("advcf").setValue(advancecf);

                                                                    DatabaseReference f = mD6.child(uid).child("ptemptrs");
                                                                    f.child("pid").setValue(ptmps.getKey());
                                                                    f.child("status").setValue("false");

                                                                    members.add(uid);

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            ch1.setChecked(true);
                                            ch2.setChecked(true);
                                            ch3.setChecked(true);
                                            Toast.makeText(meeting.this, "Meeting Started.", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                    c1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child("attendance").exists()){
                                                        Intent pay = new Intent(meeting.this,memberlist.class);
                                                        pay.putExtra("key",gkey);
                                                        startActivity(pay);
                                                    }else {
                                                        Toast.makeText(meeting.this, "Please enter meeting attendance.", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                    c2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,report1.class);
                                            loan.putExtra("key",gkey);
                                            startActivity(loan);
                                        }
                                    });
                                    c3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,memberlistb.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("key", gkey);
                                            extras.putString("meet", meetid);
                                            loan.putExtras(extras);
                                            startActivity(loan);
                                        }
                                    });
                                    c4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            final AlertDialog.Builder builders = new AlertDialog.Builder(meeting.this);
                                            builders.setTitle("Select Options")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Meeting Report", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent loan = new Intent(meeting.this,report2.class);
                                                            //loan.putExtra("key",gkey);
                                                            Bundle extras = new Bundle();
                                                            extras.putString("key", gkey);
                                                            extras.putString("date",date);
                                                            loan.putExtras(extras);
                                                            dialog.dismiss();
                                                            startActivity(loan);

                                                        }
                                                    })
                                                    .setNegativeButton("School Fees Report", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent loan = new Intent(meeting.this,schoolfeesreport.class);
                                                            //loan.putExtra("key",gkey);
                                                            Bundle extras = new Bundle();
                                                            extras.putString("key", gkey);
                                                            extras.putString("date",date);
                                                            loan.putExtras(extras);
                                                            dialog.dismiss();
                                                            startActivity(loan);
                                                         }
                                                    });
                                            AlertDialog alert121 = builders.create();
                                            alert121.show();
                                        }
                                    });
                                    c5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,memberlistc.class);
                                            loan.putExtra("key",gkey);
                                            startActivity(loan);
                                        }
                                    });
                                    c6.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,memberlistcc.class);
                                            loan.putExtra("key",gkey);
                                            startActivity(loan);
                                        }
                                    });
                                    c7.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,receiptselection.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("key", gkey);
                                            extras.putString("meet", meetid);
                                            loan.putExtras(extras);
                                            startActivity(loan);
                                        }
                                    });


                                    c8.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loan = new Intent(meeting.this,memberlistj.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("key", gkey);
                                            extras.putString("meet", meetid);
                                            extras.putString("date",date);
                                            loan.putExtras(extras);
                                            startActivity(loan);
                                        }
                                    });

                                    conclude.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ch1.isChecked()&&ch2.isChecked()&&ch3.isChecked()&&ch4.isChecked()&&ch5.isChecked()&&ch6.isChecked()&&ch7.isChecked()&&ch8.isChecked()){

                                                mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.child("reportid").exists()){
                                                            Toast.makeText(meeting.this, "Proceed.", Toast.LENGTH_LONG).show();

                                                            mlayt1.setVisibility(View.GONE);
                                                            save1.setVisibility(View.GONE);
                                                            layoutL.setVisibility(View.GONE);
                                                            mDesc.setVisibility(View.GONE);
                                                            layoutK.setVisibility(View.GONE);
                                                            layoutJ.setVisibility(View.GONE);

                                                            mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String fieldii=dataSnapshot.child("fieldid").getValue().toString();
                                                                    DatabaseReference rfid=mD3.child(date).child(fieldii);
                                                                    rfid.child("groupsgiven").child(gkey).child("status").setValue("done");

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            DatabaseReference removesession=mDatabasegroups.child("groupdetails");
                                                            removesession.child("flag").removeValue();
                                                            removesession.child("facid").removeValue();

                                                            DatabaseReference endmeet=mDatabasegroups.child("meetings").child(meetid);
                                                            endmeet.child("status").setValue("ended");

                                                            DatabaseReference endsession=mData;
                                                            endsession.child(sessionid).removeValue();

                                                            DatabaseReference endmeetnot=mD2.child("MeetingEnd").push();
                                                            endmeetnot.child("name").setValue(ggname);

                                                            DatabaseReference ref=mDatabasegroups.child("groupdetails");
                                                            ref.child("togivefac").setValue("0");

                                                            mD1.child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                        DatabaseReference fr=mD1.child("loans").child("approvals").child(snapshot.getKey());
                                                                        fr.child("status").setValue("ended");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            Intent loan = new Intent(meeting.this,groupinfo.class);
                                                            loan.putExtra("key",gkey);
                                                            startActivity(loan);
                                                            finish();

                                                        }else {
                                                            Toast.makeText(meeting.this, "Please check that report is saved.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                            }else{
                                                Toast.makeText(meeting.this, "Please Check all boxes in the meeting steps.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    mDatabasegroups.child("meetings").child(meetid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String attnd=dataSnapshot.child("attendance").getValue().toString();
                                            ch1.setChecked(true);
                                            ch2.setChecked(true);
                                            ch3.setChecked(true);

                                            mlayt1.setVisibility(View.VISIBLE);
                                            mAttndance.setText(attnd);
                                            save1.setVisibility(View.GONE);

                                            layoutL.setVisibility(View.VISIBLE);
                                            mDesc.setVisibility(View.VISIBLE);

                                            lay6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layoutK.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            lay7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layoutJ.setVisibility(View.VISIBLE);
                                                }
                                            });


                                            c1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child("attendance").exists()){
                                                                Intent pay = new Intent(meeting.this,memberlist.class);
                                                                pay.putExtra("key",gkey);
                                                                startActivity(pay);
                                                            }else {
                                                                Toast.makeText(meeting.this, "Please enter meeting attendance.", Toast.LENGTH_LONG).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });
                                            c2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,report1.class);
                                                    loan.putExtra("key",gkey);
                                                    startActivity(loan);
                                                }
                                            });
                                            c3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,memberlistb.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("key", gkey);
                                                    extras.putString("meet", meetid);
                                                    loan.putExtras(extras);
                                                    startActivity(loan);
                                                }
                                            });
                                            c4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final AlertDialog.Builder builders = new AlertDialog.Builder(meeting.this);
                                                    builders.setTitle("Select Options")
                                                            .setCancelable(true)
                                                            .setPositiveButton("Meeting Report", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent loan = new Intent(meeting.this,report2.class);
                                                                    //loan.putExtra("key",gkey);
                                                                    Bundle extras = new Bundle();
                                                                    extras.putString("key", gkey);
                                                                    extras.putString("date",date);
                                                                    loan.putExtras(extras);
                                                                    dialog.dismiss();
                                                                    startActivity(loan);
                                                                }
                                                            })
                                                            .setNegativeButton("School Fees Report", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent loan = new Intent(meeting.this,schoolfeesreport.class);
                                                                    //loan.putExtra("key",gkey);
                                                                    Bundle extras = new Bundle();
                                                                    extras.putString("key", gkey);
                                                                    extras.putString("date",date);
                                                                    loan.putExtras(extras);
                                                                    dialog.dismiss();
                                                                    startActivity(loan);
                                                                }
                                                            });
                                                    AlertDialog alert121 = builders.create();
                                                    alert121.show();
                                                }
                                            });
                                            c5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,memberlistc.class);
                                                    loan.putExtra("key",gkey);
                                                    startActivity(loan);
                                                }
                                            });
                                            c6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,memberlistcc.class);
                                                    loan.putExtra("key",gkey);
                                                    startActivity(loan);
                                                }
                                            });
                                            c7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,receiptselection.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("key", gkey);
                                                    extras.putString("meet", meetid);
                                                    loan.putExtras(extras);
                                                    startActivity(loan);
                                                    startActivity(loan);
                                                }
                                            });


                                            c8.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent loan = new Intent(meeting.this,memberlistj.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("key", gkey);
                                                    extras.putString("meet", meetid);
                                                    extras.putString("date",date);
                                                    loan.putExtras(extras);
                                                    startActivity(loan);
                                                }
                                            });
                                            conclude.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (ch1.isChecked()&&ch2.isChecked()&&ch3.isChecked()&&ch4.isChecked()&&ch5.isChecked()&&ch6.isChecked()&&ch7.isChecked()&&ch8.isChecked()){

                                                        mD4.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("reportid").exists()){
                                                                    Toast.makeText(meeting.this, "Proceed.", Toast.LENGTH_LONG).show();

                                                                    mlayt1.setVisibility(View.GONE);
                                                                    save1.setVisibility(View.GONE);
                                                                    layoutL.setVisibility(View.GONE);
                                                                    mDesc.setVisibility(View.GONE);
                                                                    layoutK.setVisibility(View.GONE);
                                                                    layoutJ.setVisibility(View.GONE);

                                                                    mDatabasegroups.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String fieldii=dataSnapshot.child("fieldid").getValue().toString();
                                                                            DatabaseReference rfid=mD3.child(date).child(fieldii);
                                                                            rfid.child("groupsgiven").child(gkey).child("status").setValue("done");

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    DatabaseReference removesession=mDatabasegroups.child("groupdetails");
                                                                    removesession.child("flag").removeValue();
                                                                    removesession.child("facid").removeValue();

                                                                    DatabaseReference endmeet=mDatabasegroups.child("meetings").child(meetid);
                                                                    endmeet.child("status").setValue("ended");

                                                                    DatabaseReference endsession=mData;
                                                                    endsession.child(sessionid).removeValue();

                                                                    DatabaseReference endmeetnot=mD2.child("MeetingEnd").push();
                                                                    endmeetnot.child("name").setValue(ggname);

                                                                    DatabaseReference ref=mDatabasegroups.child("groupdetails");
                                                                    ref.child("togivefac").setValue("0");

                                                                    mData.child("finances").child(gkey).child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                DatabaseReference fr=mD1.child("loans").child("approvals").child(snapshot.getKey());
                                                                                fr.child("status").setValue("ended");
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    Intent loan = new Intent(meeting.this,groupinfo.class);
                                                                    loan.putExtra("key",gkey);
                                                                    startActivity(loan);
                                                                    finish();

                                                                }else {
                                                                    Toast.makeText(meeting.this, "Please check that report is saved.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }else{
                                                        Toast.makeText(meeting.this, "Please Check all boxes in the meeting steps.", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
