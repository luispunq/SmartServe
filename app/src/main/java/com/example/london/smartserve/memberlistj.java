package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class memberlistj extends AppCompatActivity {

    private RecyclerView mMemberlist;
    private DatabaseReference mDatabase,mDatatabasekey,data,mD3,mD1;
    private EditText mSearch;
    private String date,ggname;
    private String grpkey,ploanl,ploana,flagl,flaga,fl,fa,meet;
    private Toolbar bar;
    private ProgressDialog mProgress;
    private double pla,pll,arrflag;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist);
        //grpkey=getIntent().getExtras().getString("key");
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        meet = extras.getString("meet");
        date=extras.getString("date");
        mMemberlist = (RecyclerView) findViewById(R.id.memberslist);
        mMemberlist.setHasFixedSize(true);
        mMemberlist.setLayoutManager(new LinearLayoutManager(this));
        mD1=FirebaseDatabase.getInstance().getReference().child("finances").child(grpkey);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(grpkey);
        data = FirebaseDatabase.getInstance().getReference().child("members");
        mD3=FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        //mDatabase.keepSynced(true);
        mProgress=new ProgressDialog(this);
        mSearch=findViewById(R.id.editText2);

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ggname=dataSnapshot.child("groupdetails").child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                mDatabase.orderByChild("name")
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {
                final String u_key=model.getUid();

                data.child(u_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.child("attendance").exists()){
                        if (dataSnapshot1.child("attendance").child(date).exists()){
                            if (Double.parseDouble(dataSnapshot1.child("loans").child("totalloan").getValue().toString())>0&&dataSnapshot1.child("loans").child("nextloanpaydate").exists()&&!dataSnapshot1.child("loans").child("nextloanpaydate").getValue().toString().equals(date)){
                                viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                                viewHolder.setMemberName(model.getName());
                                viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.present));
                                if (Double.parseDouble(dataSnapshot1.child("advances").child("currentadvance").getValue().toString())>0&&dataSnapshot1.child("advances").child("nextadvpaydate").exists()&&!dataSnapshot1.child("advances").child("nextadvpaydate").getValue().toString().equals(date)){
                                    viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                                    viewHolder.setMemberName(model.getName());
                                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.present));
                                }else {
                                    viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                                    viewHolder.setMemberName(model.getName());
                                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.colorComplement));
                                }
                            }else {
                                viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                                viewHolder.setMemberName(model.getName());
                                viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.colorComplement));
                            }


                        }else {
                            viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                            viewHolder.setMemberName(model.getName());
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.absent));
                        }
                        }else {
                            viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                            viewHolder.setMemberName(model.getName());
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.absent));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("loanflag").exists()&&dataSnapshot.child("loans").child("nextloanpaydate").getValue().toString().equals(date)){
                                    flagl=dataSnapshot.child("loans").child("nextloanpaydate").getValue().toString();
                                    ploanl=dataSnapshot.child("loans").child("totalloan").getValue().toString();
                                    pll=Double.parseDouble(ploanl);
                                    if (dataSnapshot.child("loans").child("compflag").exists()){
                                        arrflag=Double.parseDouble(dataSnapshot.child("loans").child("compflag").getValue().toString());
                                    }else {
                                        arrflag=0;
                                    }
                                    fl="yes";
                                }else {
                                    flagl="0";
                                    ploanl="0";
                                    fl="no";
                                    arrflag=0;
                                }
                                if (dataSnapshot.child("advances").child("nextadvpaydate").exists()&&dataSnapshot.child("advances").child("nextadvpaydate").getValue().toString().equals(date)){
                                    flaga=dataSnapshot.child("advances").child("nextadvpaydate").getValue().toString();
                                    ploana=dataSnapshot.child("advances").child("currentadvance").getValue().toString();
                                    pla=Double.parseDouble(ploana);
                                    fa="yes";
                                }else {
                                    flaga="0";
                                    ploana="0";
                                    fa="no";
                                }

                                if (fl.equals("no")&&fa.equals("no")){
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(memberlistj.this);
                                    builders.setTitle("Confirm Defaults.")
                                            .setMessage("Member is absent.")
                                            .setCancelable(true)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            DatabaseReference newgroupmeetpayment = mD3.child("transactions").child(meet).push();
                                                            final String temptr = newgroupmeetpayment.getKey();
                                                            newgroupmeetpayment.child("date").setValue(date);
                                                            newgroupmeetpayment.child("totalamount").setValue("0");
                                                            newgroupmeetpayment.child("lsf").setValue("0");
                                                            newgroupmeetpayment.child("wcomm").setValue("0");
                                                            newgroupmeetpayment.child("loaninstallments").setValue("0");
                                                            newgroupmeetpayment.child("advancepayment").setValue("0");
                                                            newgroupmeetpayment.child("fines").setValue("0");
                                                            newgroupmeetpayment.child("loaninterest").setValue("0");
                                                            newgroupmeetpayment.child("advanceinterest").setValue("0");
                                                            newgroupmeetpayment.child("wamount").setValue("0");
                                                            newgroupmeetpayment.child("paymentmode").setValue("Absent");
                                                            newgroupmeetpayment.child("insurance").setValue("0");
                                                            newgroupmeetpayment.child("memberid").setValue(dataSnapshot.child("details").child("name").getValue().toString());
                                                            newgroupmeetpayment.child("Mpesa").setValue("0");
                                                            newgroupmeetpayment.child("id").setValue(u_key);
                                                            newgroupmeetpayment.child("loangvn").setValue("0");
                                                            newgroupmeetpayment.child("loancf").setValue(dataSnapshot.child("loans").child("totalloan").getValue().toString());
                                                            newgroupmeetpayment.child("advgvn").setValue("0");
                                                            newgroupmeetpayment.child("advcf").setValue(dataSnapshot.child("advances").child("currentadvance").getValue().toString());


                                                            double val=Double.parseDouble(dataSnapshot.child("savings").child("totalsavings").getValue().toString());

                                                            DatabaseReference refs=data.child(u_key);
                                                            refs.child("savings").child("totalsavings").setValue(String.valueOf(val-100));



                                                            DatabaseReference savingsupdatde = data.child(u_key).child("attendance");
                                                            savingsupdatde.child(date).setValue("marked");

                                                            newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(val-100));

                                                            DatabaseReference reference=mD1.child("defaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                            reference.child("groupid").setValue(grpkey);
                                                            reference.child("groupname").setValue(ggname);
                                                            reference.child("memberid").setValue(u_key);
                                                            reference.child("date").setValue(date);
                                                            reference.child("cf").setValue(String.valueOf(val-100));

                                                            ViewGroup.LayoutParams layoutParams=viewHolder.mView.getLayoutParams();
                                                            layoutParams.height=0;
                                                            viewHolder.mView.setLayoutParams(layoutParams);
                                                            Toast.makeText(memberlistj.this,model.getName()+" Has been noted absent",Toast.LENGTH_LONG).show();

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    dialog.dismiss();
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
                                }else if (fl.equals("yes")&&fa.equals("no")){
                                    final AlertDialog.Builder builders2 = new AlertDialog.Builder(memberlistj.this);
                                    builders2.setTitle("Confirm Defaults.")
                                            .setMessage("Member has loan default\nTotal Loan "+ploanl)
                                            .setCancelable(true)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    DatabaseReference loansupdats=data.child(u_key).child("loans");
                                                    loansupdats.child("totalloan").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));
                                                    loansupdats.child("loanflag").setValue(setdate());
                                                    loansupdats.child("compflag").setValue(String.valueOf(1+arrflag));

                                                    data.child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String savingsb4=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                            //final String loansb4=String.valueOf(pll*1.01);
                                                            final String advb4=dataSnapshot.child("advances").child("currentadvance").getValue().toString();


                                                            final DatabaseReference newgroupmeetpayment = mD3.child("loandefaulters").child(meet).push();
                                                            newgroupmeetpayment.child("date").setValue(date);
                                                            newgroupmeetpayment.child("totalamount").setValue("0");
                                                            newgroupmeetpayment.child("lsf").setValue("0");
                                                            newgroupmeetpayment.child("wcomm").setValue("0");
                                                            newgroupmeetpayment.child("loaninstallments").setValue("0");
                                                            newgroupmeetpayment.child("advancepayment").setValue("0");
                                                            newgroupmeetpayment.child("fines").setValue("0");
                                                            newgroupmeetpayment.child("loaninterest").setValue("0");
                                                            newgroupmeetpayment.child("advanceinterest").setValue("0");
                                                            newgroupmeetpayment.child("wamount").setValue("0");
                                                            newgroupmeetpayment.child("paymentmode").setValue("0");
                                                            newgroupmeetpayment.child("insurance").setValue("0");
                                                            newgroupmeetpayment.child("memberid").setValue(model.getName());
                                                            newgroupmeetpayment.child("Mpesa").setValue("0");
                                                            newgroupmeetpayment.child("id").setValue(model.getUid());
                                                            newgroupmeetpayment.child("loangvn").setValue("0");
                                                            newgroupmeetpayment.child("loancf").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));
                                                            newgroupmeetpayment.child("advgvn").setValue("0");
                                                            newgroupmeetpayment.child("advcf").setValue(advb4);

                                                            data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.child("attendance").hasChild(date)){
                                                                        String meetid=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();

                                                                        DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).child(meetid);
                                                                        newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                        newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                        newgroupmeetpayments.child("id").setValue(u_key);
                                                                        newgroupmeetpayments.child("loancf").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));
                                                                        newgroupmeetpayments.child("advcf").setValue(advb4);


                                                                    }else {
                                                                        DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).push();
                                                                        newgroupmeetpayments.child("date").setValue(date);
                                                                        newgroupmeetpayments.child("totalamount").setValue("0");
                                                                        newgroupmeetpayments.child("lsf").setValue("0");
                                                                        newgroupmeetpayments.child("wcomm").setValue("0");
                                                                        newgroupmeetpayments.child("loaninstallments").setValue("0");
                                                                        newgroupmeetpayments.child("advancepayment").setValue("0");
                                                                        newgroupmeetpayments.child("fines").setValue("0");
                                                                        newgroupmeetpayments.child("loaninterest").setValue("0");
                                                                        newgroupmeetpayments.child("advanceinterest").setValue("0");
                                                                        newgroupmeetpayments.child("wamount").setValue("0");
                                                                        newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                        newgroupmeetpayments.child("insurance").setValue("0");
                                                                        newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                        newgroupmeetpayments.child("Mpesa").setValue("0");
                                                                        newgroupmeetpayments.child("id").setValue(u_key);
                                                                        newgroupmeetpayments.child("loangvn").setValue("0");
                                                                        newgroupmeetpayments.child("loancf").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));
                                                                        newgroupmeetpayments.child("advgvn").setValue("0");
                                                                        newgroupmeetpayments.child("advcf").setValue(advb4);


                                                                        double val=Double.parseDouble(dataSnapshot.child("savings").child("totalsavings").getValue().toString());

                                                                        DatabaseReference refs=data.child(u_key);
                                                                        refs.child("savings").child("totalsavings").setValue(String.valueOf(val-100));


                                                                        DatabaseReference referencee=mD1.child("defaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();                                                           referencee.child("groupid").setValue(grpkey);
                                                                        referencee.child("groupname").setValue(ggname);
                                                                        referencee.child("memberid").setValue(u_key);
                                                                        referencee.child("date").setValue(date);
                                                                        referencee.child("cf").setValue(String.valueOf(val-100));


                                                                        DatabaseReference savingsupdatde = data.child(u_key).child("attendance");
                                                                        savingsupdatde.child(date).setValue("marked");

                                                                        newgroupmeetpayments.child("lsfcf").setValue(String.valueOf(val-100));
                                                                        newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(val-100));
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                                            DatabaseReference reference=mD1.child("loandefaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                            reference.child("groupid").setValue(grpkey);
                                                            reference.child("groupname").setValue(ggname);
                                                            reference.child("memberid").setValue(u_key);
                                                            reference.child("date").setValue(date);
                                                            reference.child("cf").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));



                                                            ViewGroup.LayoutParams layoutParams=viewHolder.mView.getLayoutParams();
                                                            layoutParams.height=0;
                                                            viewHolder.mView.setLayoutParams(layoutParams);
                                                            Toast.makeText(memberlistj.this,model.getName()+" Has been noted absent",Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert1212 = builders2.create();
                                    alert1212.show();
                                }else if (fa.equals("yes")&&fl.equals("no")){
                                    final AlertDialog.Builder builders22 = new AlertDialog.Builder(memberlistj.this);
                                    builders22.setTitle("Confirm Defaults.")
                                            .setMessage("Member has advance default\nTotal Advance "+ploana)
                                            .setCancelable(true)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            DatabaseReference loansupdates=data.child(u_key).child("advances");
                                                            loansupdates.child("currentadvance").setValue(String.valueOf(Math.round(((pla*1.1)+10)*100)/100));
                                                            loansupdates.child("currpenalty").setValue(String.valueOf(Math.round((((pla*1.1)+10)/10))/100));
                                                            loansupdates.child("nextadvpaydate").setValue(setdate());

                                                            data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String savingsb4=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                                    final String loansb4=dataSnapshot.child("loans").child("currentloan").getValue().toString();
                                                                    final String advb4=String.valueOf(Math.round(((pla*1.1)+10)*100)/100);


                                                                    final DatabaseReference newgroupmeetpayment = mD3.child("advancedefaulters").child(meet).push();
                                                                    newgroupmeetpayment.child("date").setValue(date);
                                                                    newgroupmeetpayment.child("totalamount").setValue("0");
                                                                    newgroupmeetpayment.child("lsf").setValue("0");
                                                                    newgroupmeetpayment.child("wcomm").setValue("0");
                                                                    newgroupmeetpayment.child("loaninstallments").setValue("0");
                                                                    newgroupmeetpayment.child("advancepayment").setValue("0");
                                                                    newgroupmeetpayment.child("fines").setValue("0");
                                                                    newgroupmeetpayment.child("loaninterest").setValue("0");
                                                                    newgroupmeetpayment.child("advanceinterest").setValue("0");
                                                                    newgroupmeetpayment.child("wamount").setValue("0");
                                                                    newgroupmeetpayment.child("paymentmode").setValue("0");
                                                                    newgroupmeetpayment.child("insurance").setValue("0");
                                                                    newgroupmeetpayment.child("memberid").setValue(model.getName());
                                                                    newgroupmeetpayment.child("Mpesa").setValue("0");
                                                                    newgroupmeetpayment.child("id").setValue(u_key);
                                                                    newgroupmeetpayment.child("loangvn").setValue("0");
                                                                    newgroupmeetpayment.child("loancf").setValue(loansb4);
                                                                    newgroupmeetpayment.child("advgvn").setValue("0");
                                                                    newgroupmeetpayment.child("advcf").setValue(advb4);


                                                                    data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.child("attendance").getValue().toString().equals(date)){
                                                                                String meetid=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();

                                                                                DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).child(meetid);
                                                                                newgroupmeetpayments.child("date").setValue(date);
                                                                                newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                                newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                                newgroupmeetpayments.child("id").setValue(u_key);
                                                                                newgroupmeetpayments.child("loancf").setValue(loansb4);
                                                                                newgroupmeetpayments.child("advcf").setValue(advb4);

                                                                            }else {

                                                                                DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).push();
                                                                                newgroupmeetpayments.child("date").setValue(date);
                                                                                newgroupmeetpayments.child("totalamount").setValue("0");
                                                                                newgroupmeetpayments.child("lsf").setValue("0");
                                                                                newgroupmeetpayments.child("wcomm").setValue("0");
                                                                                newgroupmeetpayments.child("loaninstallments").setValue("0");
                                                                                newgroupmeetpayments.child("advancepayment").setValue("0");
                                                                                newgroupmeetpayments.child("fines").setValue("0");
                                                                                newgroupmeetpayments.child("loaninterest").setValue("0");
                                                                                newgroupmeetpayments.child("advanceinterest").setValue("0");
                                                                                newgroupmeetpayments.child("wamount").setValue("0");
                                                                                newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                                newgroupmeetpayments.child("insurance").setValue("0");
                                                                                newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                                newgroupmeetpayments.child("Mpesa").setValue("0");
                                                                                newgroupmeetpayments.child("id").setValue(u_key);
                                                                                newgroupmeetpayments.child("loangvn").setValue("0");
                                                                                newgroupmeetpayments.child("loancf").setValue(loansb4);
                                                                                newgroupmeetpayments.child("advgvn").setValue("0");
                                                                                newgroupmeetpayments.child("advcf").setValue(advb4);

                                                                                double val=Double.parseDouble(dataSnapshot.child("savings").child("totalsavings").getValue().toString());

                                                                                DatabaseReference refs=data.child(u_key);
                                                                                refs.child("savings").child("totalsavings").setValue(String.valueOf(val-100));


                                                                                DatabaseReference savingsupdatde = data.child(u_key).child("attendance");
                                                                                savingsupdatde.child(date).setValue("marked");

                                                                                newgroupmeetpayments.child("lsfcf").setValue(String.valueOf(val-100));
                                                                                newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(val-100));

                                                                                DatabaseReference referencee=mD1.child("defaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                                referencee.child("groupid").setValue(grpkey);
                                                                                referencee.child("groupname").setValue(ggname);
                                                                                referencee.child("memberid").setValue(u_key);
                                                                                referencee.child("date").setValue(date);
                                                                                referencee.child("cf").setValue(String.valueOf(val-100));

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });



                                                                    DatabaseReference reference=mD1.child("advancedefaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                    reference.child("groupid").setValue(grpkey);
                                                                    reference.child("groupname").setValue(ggname);
                                                                    reference.child("memberid").setValue(u_key);
                                                                    reference.child("date").setValue(date);
                                                                    reference.child("cf").setValue(advb4);



                                                                    ViewGroup.LayoutParams layoutParams=viewHolder.mView.getLayoutParams();
                                                                    layoutParams.height=0;
                                                                    viewHolder.mView.setLayoutParams(layoutParams);
                                                                    Toast.makeText(memberlistj.this,model.getName()+" Has been noted absent",Toast.LENGTH_LONG).show();
                                                                    dialog.dismiss();
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
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert1212r = builders22.create();
                                    alert1212r.show();
                                }else if (fa.equals("yes")&&fl.equals("yes")){
                                    final AlertDialog.Builder builders22b = new AlertDialog.Builder(memberlistj.this);
                                    builders22b.setTitle("Confirm Defaults.")
                                            .setMessage("Member has advance and loan default\nTotal Loan "+ploanl+"\nTotal Advance "+ploana)
                                            .setCancelable(true)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            DatabaseReference loansupdates=data.child(u_key).child("advances");
                                                            loansupdates.child("currentadvance").setValue(String.valueOf(Math.round(((pla*1.1)+10)*100)/100));
                                                            loansupdates.child("currpenalty").setValue(String.valueOf(Math.round((((pla*1.1)+10)/10))/100));
                                                            loansupdates.child("nextadvpaydate").setValue(setdate());

                                                            DatabaseReference loansupdats=data.child(u_key).child("loans");
                                                            loansupdats.child("totalloan").setValue(String.valueOf(Math.round((pll*1.06)*100)/100));
                                                            loansupdats.child("nextloanpaydate").setValue(setdate());
                                                            loansupdats.child("compflag").setValue(String.valueOf(1+arrflag));

                                                            data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    //String savingsb4=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                                    String loansb4ad=String.valueOf(pll*1.01);
                                                                    final String advb4ad=String.valueOf(Math.round(((pla*1.1)+10)*100)/100);


                                                                    //String savingsb4=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                                                                    final String loansb4=String.valueOf(Math.round((pll*1.06)*100)/100);
                                                                    final String advb4=String.valueOf(Math.round(((pla*1.1)+10)*100)/100);


                                                                    final DatabaseReference newgroupmeetpayment = mD3.child("advancedefaulters").child(meet).push();
                                                                    newgroupmeetpayment.child("date").setValue(date);
                                                                    newgroupmeetpayment.child("totalamount").setValue("0");
                                                                    newgroupmeetpayment.child("lsf").setValue("0");
                                                                    newgroupmeetpayment.child("wcomm").setValue("0");
                                                                    newgroupmeetpayment.child("loaninstallments").setValue("0");
                                                                    newgroupmeetpayment.child("advancepayment").setValue("0");
                                                                    newgroupmeetpayment.child("fines").setValue("0");
                                                                    newgroupmeetpayment.child("loaninterest").setValue("0");
                                                                    newgroupmeetpayment.child("advanceinterest").setValue("0");
                                                                    newgroupmeetpayment.child("wamount").setValue("0");
                                                                    newgroupmeetpayment.child("paymentmode").setValue("0");
                                                                    newgroupmeetpayment.child("insurance").setValue("0");
                                                                    newgroupmeetpayment.child("memberid").setValue(model.getName());
                                                                    newgroupmeetpayment.child("Mpesa").setValue("0");
                                                                    newgroupmeetpayment.child("id").setValue(u_key);
                                                                    newgroupmeetpayment.child("loangvn").setValue("0");
                                                                    newgroupmeetpayment.child("loancf").setValue(loansb4);
                                                                    newgroupmeetpayment.child("advgvn").setValue("0");
                                                                    newgroupmeetpayment.child("advcf").setValue(advb4ad);

                                                                    final DatabaseReference newgroupmeetpayment2 = mD3.child("loandefaulters").child(meet).push();
                                                                    newgroupmeetpayment2.child("date").setValue(date);
                                                                    newgroupmeetpayment2.child("totalamount").setValue("0");
                                                                    newgroupmeetpayment2.child("lsf").setValue("0");
                                                                    newgroupmeetpayment2.child("wcomm").setValue("0");
                                                                    newgroupmeetpayment2.child("loaninstallments").setValue("0");
                                                                    newgroupmeetpayment2.child("advancepayment").setValue("0");
                                                                    newgroupmeetpayment2.child("fines").setValue("0");
                                                                    newgroupmeetpayment2.child("loaninterest").setValue("0");
                                                                    newgroupmeetpayment2.child("advanceinterest").setValue("0");
                                                                    newgroupmeetpayment2.child("wamount").setValue("0");
                                                                    newgroupmeetpayment2.child("paymentmode").setValue("0");
                                                                    newgroupmeetpayment2.child("insurance").setValue("0");
                                                                    newgroupmeetpayment2.child("memberid").setValue(model.getName());
                                                                    newgroupmeetpayment2.child("Mpesa").setValue("0");
                                                                    newgroupmeetpayment2.child("id").setValue(model.getUid());
                                                                    newgroupmeetpayment2.child("loangvn").setValue("0");
                                                                    newgroupmeetpayment2.child("loancf").setValue(loansb4);
                                                                    newgroupmeetpayment2.child("advgvn").setValue("0");
                                                                    newgroupmeetpayment2.child("advcf").setValue(advb4ad);

                                                                    data.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.child("attendance").getValue().toString().equals(date)){
                                                                                String meetid=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();

                                                                                DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).child(meetid);
                                                                                newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                                newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                                newgroupmeetpayments.child("id").setValue(u_key);
                                                                                newgroupmeetpayments.child("loancf").setValue(loansb4);
                                                                                newgroupmeetpayments.child("advcf").setValue(advb4ad);

                                                                            }else {

                                                                                DatabaseReference newgroupmeetpayments = mD3.child("transactions").child(meet).push();
                                                                                newgroupmeetpayments.child("date").setValue(date);
                                                                                newgroupmeetpayments.child("totalamount").setValue("0");
                                                                                newgroupmeetpayments.child("lsf").setValue("0");
                                                                                newgroupmeetpayments.child("wcomm").setValue("0");
                                                                                newgroupmeetpayments.child("loaninstallments").setValue("0");
                                                                                newgroupmeetpayments.child("advancepayment").setValue("0");
                                                                                newgroupmeetpayments.child("fines").setValue("0");
                                                                                newgroupmeetpayments.child("loaninterest").setValue("0");
                                                                                newgroupmeetpayments.child("advanceinterest").setValue("0");
                                                                                newgroupmeetpayments.child("wamount").setValue("0");
                                                                                newgroupmeetpayments.child("paymentmode").setValue("Defauter");
                                                                                newgroupmeetpayments.child("insurance").setValue("0");
                                                                                newgroupmeetpayments.child("memberid").setValue(model.getName());
                                                                                newgroupmeetpayments.child("Mpesa").setValue("0");
                                                                                newgroupmeetpayments.child("id").setValue(u_key);
                                                                                newgroupmeetpayments.child("loangvn").setValue("0");
                                                                                newgroupmeetpayments.child("loancf").setValue(loansb4);
                                                                                newgroupmeetpayments.child("advgvn").setValue("0");
                                                                                newgroupmeetpayments.child("advcf").setValue(advb4ad);

                                                                                DatabaseReference reference=mD1.child("advancedefaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                                reference.child("groupid").setValue(grpkey);
                                                                                reference.child("groupname").setValue(ggname);
                                                                                reference.child("memberid").setValue(u_key);
                                                                                reference.child("date").setValue(date);
                                                                                reference.child("cf").setValue(advb4);

                                                                                DatabaseReference reference5=mD1.child("loandefaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                                reference5.child("groupid").setValue(grpkey);
                                                                                reference5.child("groupname").setValue(ggname);
                                                                                reference5.child("memberid").setValue(u_key);
                                                                                reference5.child("date").setValue(date);
                                                                                reference5.child("cf").setValue(loansb4);


                                                                                double val=Double.parseDouble(dataSnapshot.child("savings").child("totalsavings").getValue().toString());

                                                                                DatabaseReference refs=data.child(u_key);
                                                                                refs.child("savings").child("totalsavings").setValue(String.valueOf(val-100));

                                                                                DatabaseReference savingsupdatde = data.child(u_key).child("attendance");
                                                                                savingsupdatde.child(date).setValue("marked");

                                                                                newgroupmeetpayments.child("lsfcf").setValue(String.valueOf(val-100));
                                                                                newgroupmeetpayment.child("lsfcf").setValue(String.valueOf(val-100));
                                                                                newgroupmeetpayment2.child("lsfcf").setValue(String.valueOf(val-100));


                                                                                DatabaseReference referencee=mD1.child("defaulters").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                                referencee.child("groupid").setValue(grpkey);
                                                                                referencee.child("groupname").setValue(ggname);
                                                                                referencee.child("memberid").setValue(u_key);
                                                                                referencee.child("date").setValue(date);
                                                                                referencee.child("cf").setValue(String.valueOf(val-100));


                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });




                                                                    ViewGroup.LayoutParams layoutParams=viewHolder.mView.getLayoutParams();
                                                                    layoutParams.height=0;
                                                                    viewHolder.mView.setLayoutParams(layoutParams);
                                                                    Toast.makeText(memberlistj.this,model.getName()+" Has been noted absent",Toast.LENGTH_LONG).show();
                                                                    dialog.dismiss();
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
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert1212rb = builders22b.create();
                                    alert1212rb.show();
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
        mMemberlist.setAdapter(firebaseRecyclerAdapter);

    }



    public static class membersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.memberscard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.membercardname);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.membercardphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }
    private String setdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 30); // Adding 30 days

        return sdf.format(c.getTime());
    }
}
