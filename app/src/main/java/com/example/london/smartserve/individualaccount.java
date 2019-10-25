package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class individualaccount extends AppCompatActivity {
    private TextView mAccname,mBalfigdr,mBalfigcr,mTotalDr,mTotalCr,mBalDr,mBalCr;
    private RecyclerView mList;
    private DatabaseReference mD1,mD2,mD3;
    private String option,month,year,modyea;
    private FloatingActionButton fab,fab2;
    private long l1,l2,l3,l4;
    private double totalcl=0,totalcp=0,v1=0,v2=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,
            i1=0,i2=0,i3=0,f1=0,f2=0,f3=0,v9=0,m1=0,m2=0,m3=0,totalcf=0,manbal,secbal,cc1,cc2,cc3,r1,r2,x1=0,x2=0,x3=0,bc8,bc9;

    DecimalFormat df = new DecimalFormat("##,###,###.#");
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individualaccount);
        option=getIntent().getExtras().getString("option");
        mAccname=findViewById(R.id.accountname);
        mBalfigdr=findViewById(R.id.balancingpartdr);
        mBalfigcr=findViewById(R.id.balancingpartcr);
        mTotalDr=findViewById(R.id.totalsdr);
        mTotalCr=findViewById(R.id.totalscr);
        mBalDr=findViewById(R.id.balancedowndr);
        mBalCr=findViewById(R.id.balancedowncr);
        fab=findViewById(R.id.accselectFaB);
        fab2=findViewById(R.id.accselectFaB2);
        fab2.setVisibility(View.INVISIBLE);



        mList=findViewById(R.id.entrieslist);
        mList.setLayoutManager(mLayoutManager);
        mList.setHasFixedSize(true);
        mList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mList.setDrawingCacheEnabled(true);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        mD1= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD2=FirebaseDatabase.getInstance().getReference().child("members").child(option).child("account");
        mD3=FirebaseDatabase.getInstance().getReference().child("members").child(option);


        switch (option) {

            case "BankCash":


                break;
            case "Bank":

                mAccname.setText("BANK/CASH ACCOUNT");

                mD1.child("Bank").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v3=0;
                        v4=0;
                        l2=0;
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                                totalcl=totalcl+cl;

                                mTotalDr.setText("Kshs. "+df.format(totalcl));

                                double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                                totalcp=totalcp+c2;

                                mTotalCr.setText("Kshs. "+df.format(totalcp));

                            }

                            if (totalcl>totalcp){
                                double vb=totalcl-totalcp;
                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                mBalDr.setText("Kshs. "+df.format(vb));
                                mTotalCr.setText("Kshs. "+df.format(totalcl));
                            }else {
                                double vb=totalcp-totalcl;
                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                mBalCr.setText("Kshs. "+df.format(vb));
                                mTotalDr.setText("Kshs. "+df.format(totalcp));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<bankacc, accountholder> firebaseRecyclerAdapte = new FirebaseRecyclerAdapter<bankacc, accountholder>(

                        bankacc.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Bank").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final bankacc model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDetails());
                        if (model.getWithdraw().equals("0")){
                            viewHolder.setDebit(model.getDeposit());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getWithdraw());
                            viewHolder.setDebit("0");
                        }
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String yearx=modyear(years(model.getTimestamp()));
                                final String monthx=months(model.getTimestamp());
                                final String dayx=new SimpleDateFormat("EEE, MMM d, ''yy").format(model.getTimestamp());
                                mD1.child("BankCash").child("Trans").child(years(model.getTimestamp())).child(monthx).child(dayx).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        bc8=0;
                                        bc9=0;
                                        mBalfigcr.setText("");
                                        mBalfigdr.setText("");
                                        mTotalCr.setText("");
                                        mTotalDr.setText("");
                                        mBalCr.setText("");
                                        mBalDr.setText("");

                                        bc8=0;
                                        bc9=0;
                                        if (dataSnapshot.hasChildren()){
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                /*if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(years(model.getTimestamp()))&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(String.valueOf(model.getTimestamp()))&&snapshot.child("debitac").getValue().toString().equals("BankCash")){
                                                    bc8=bc8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                }

                                                if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(years(model.getTimestamp()))&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(String.valueOf(model.getTimestamp()))&&!snapshot.child("debitac").getValue().toString().equals("BankCash")){
                                                    bc9=bc9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                }*/
                                                if (snapshot.child("amount").getValue().toString().equals("0")){
                                                    DatabaseReference rm=mD1.child("BankCash").child("Trans").child(years(model.getTimestamp())).child(monthx).child(dayx).child(snapshot.getKey());
                                                    rm.removeValue();
                                                }

                                                if (snapshot.child("debitac").getValue().toString().equals("BankCash")){
                                                    bc8=bc8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                    mTotalDr.setText("Kshs. "+df.format(bc8));
                                                }

                                                if (!snapshot.child("debitac").getValue().toString().equals("BankCash")){
                                                    bc9=bc9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                    mTotalCr.setText("Kshs. "+df.format(bc9));
                                                }
                                            }
                                            if (bc8>bc9){
                                                double vb=bc8-bc9;
                                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                                mBalDr.setText("Kshs. "+df.format(vb));
                                                mTotalCr.setText("Kshs. "+df.format(bc8));
                                            }else {
                                                double vb=bc9-bc8;
                                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                                mBalCr.setText("Kshs. "+df.format(vb));
                                                mTotalDr.setText("Kshs. "+df.format(bc9));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdaptre = new FirebaseRecyclerAdapter<account, accountholder>(

                                        account.class,
                                        R.layout.accountsentries_row,
                                        accountholder.class,
                                        mD1.child("BankCash").child("Trans").child(years(model.getTimestamp())).child(monthx).child(dayx).limitToLast(50)
                                ) {
                                    @Override
                                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                        viewHolder.setDate(model.getTimestamp());
                                        viewHolder.setDesc(model.getDescription());
                                        if (model.getDebitac().equals("BankCash")){
                                            viewHolder.setDebit(model.getAmount());
                                            viewHolder.setCredit("0");
                                        }else {
                                            viewHolder.setCredit(model.getAmount());
                                            viewHolder.setDebit("0");
                                        }
                                    }
                                };

                                mList.setAdapter(firebaseRecyclerAdaptre);
                            }
                        });
                    }
                };

                mList.setAdapter(firebaseRecyclerAdapte);


                break;
            case "Loan":

                mAccname.setText("LONG TERM LOAN ACCOUNT");

                mD1.child("Longtermloan").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("Longtermloan").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                    m1=m1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(m1));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                    m2=m2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(m2));
                                }



                            }
                            if (m1>m2){
                                double vb=m1-m2;
                                l1=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(l1));
                                mBalDr.setText("Kshs. "+df.format(l1));
                                mTotalCr.setText("Kshs. "+df.format(m1));
                            }else {
                                double vb=m2-m1;
                                l1=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(l1));
                                mBalCr.setText("Kshs. "+df.format(l1));
                                mTotalDr.setText("Kshs. "+df.format(m2));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterloans = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Longtermloan").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());

                        if (model.getDebitac().equals("Longtermloan")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdapterloans);

                break;
            case "Advance":

                mAccname.setText("SHORT TERM LOAN ACCOUNT");

                mD1.child("Shorttermloans").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v6=0;
                        v7=0;
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("Shorttermloans").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                    v6=v6+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(v6));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                    v7=v7+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(v7));
                                }

                            }
                            if (v6>v7){
                                double vb=v6-v7;
                                l3=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(l3));
                                mBalDr.setText("Kshs. "+df.format(l3));
                                mTotalCr.setText("Kshs. "+df.format(v6));
                            }else {
                                double vb=v7-v6;
                                l3=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(l3));
                                mBalCr.setText("Kshs. "+df.format(l3));
                                mTotalDr.setText("Kshs. "+df.format(v7));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapteradvance = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Shorttermloans").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());

                        if (model.getDebitac().equals("Shorttermloans")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdapteradvance);

                break;
            case "Suspense":

                mAccname.setText("SUSPENSE ACCOUNT");

                mD1.child("Suspense").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v6=0;
                        v7=0;
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("Suspense").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                    v6=v6+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(v6));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                    v7=v7+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(v7));
                                }

                            }
                            if (v6>v7){
                                double vb=v6-v7;
                                l3=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(l3));
                                mBalDr.setText("Kshs. "+df.format(l3));
                                mTotalCr.setText("Kshs. "+df.format(v6));
                            }else {
                                double vb=v7-v6;
                                l3=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(l3));
                                mBalCr.setText("Kshs. "+df.format(l3));
                                mTotalDr.setText("Kshs. "+df.format(v7));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdaptersus = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Suspense").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());

                        if (model.getDebitac().equals("Suspense")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdaptersus);

                break;


            case "Income":
                mAccname.setText("INCOME ACCOUNT");

                fab2.setVisibility(View.VISIBLE);

                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layout=new LinearLayout(individualaccount.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final Spinner taskedit=new Spinner(individualaccount.this);
                        final Spinner taskedit2=new Spinner(individualaccount.this);
                        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(individualaccount.this,R.array.Months,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit2.setAdapter(adapter);

                        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(individualaccount.this,R.array.Years,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit.setAdapter(adapter1);

                        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                month=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        taskedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        layout.addView(taskedit);
                        layout.addView(taskedit2);
                        final AlertDialog.Builder builders = new AlertDialog.Builder(individualaccount.this);
                        builders.setTitle("Select Accounting Period")
                                .setView(layout)
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mD1.child("Income").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()){
                                                    i1=0;
                                                    i2=0;
                                                    mBalfigcr.setText("");
                                                    mBalfigdr.setText("");
                                                    mTotalCr.setText("");
                                                    mTotalDr.setText("");
                                                    mBalCr.setText("");
                                                    mBalDr.setText("");
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                        if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(month)){
                                                        if (snapshot.child("debitac").getValue().toString().equals("Income")){
                                                            i1=i1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                            mTotalDr.setText("Kshs. "+df.format(i1));
                                                        }

                                                        if (!snapshot.child("debitac").getValue().toString().equals("Income")){
                                                            i2=i2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                            mTotalCr.setText("Kshs. "+df.format(i2));
                                                        }
                                                        }

                                                    if (i1>i2){
                                                        double vb=i1-i2;
                                                        //l4=(long)vb;
                                                        mBalfigcr.setText("Kshs. "+df.format(vb));
                                                        mBalDr.setText("Kshs. "+df.format(vb));
                                                        mTotalCr.setText("Kshs. "+df.format(i1));
                                                    }else {
                                                        double vb=i2-i1;
                                                        //l4=(long)vb;
                                                        mBalfigdr.setText("Kshs. "+df.format(vb));
                                                        mBalCr.setText("Kshs. "+df.format(vb));
                                                        mTotalDr.setText("Kshs. "+df.format(i2));
                                                    }
                                                }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterinc = new FirebaseRecyclerAdapter<account, accountholder>(

                                                account.class,
                                                R.layout.accountsentries_row,
                                                accountholder.class,
                                                mD1.child("Income").child("Trans").child("all").limitToLast(50)
                                        ) {
                                            @Override
                                            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                if (years(model.getTimestamp()).contains(year)&&months(model.getTimestamp()).contains(month)){
                                                    viewHolder.setDate(model.getTimestamp());
                                                    viewHolder.setDesc(model.getDescription());

                                                    if (model.getDebitac().equals("Income")){
                                                        viewHolder.setDebit(model.getAmount());
                                                        viewHolder.setCredit("0");
                                                    }else {
                                                        viewHolder.setCredit(model.getAmount());
                                                        viewHolder.setDebit("0");
                                                    }

                                                    if (Double.parseDouble(model.getAmount())<1.0){
                                                        viewHolder.Layout_hide();
                                                    }
                                                }else {
                                                    viewHolder.Layout_hide();
                                                }


                                            }
                                        };

                                        mList.setAdapter(firebaseRecyclerAdapterinc);


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
                    }
                });

                break;
            case "Schoolfees":
                mAccname.setText("SCHOOL FEES ACCOUNT");

                fab2.setVisibility(View.VISIBLE);

                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layout=new LinearLayout(individualaccount.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final Spinner taskedit=new Spinner(individualaccount.this);
                        final Spinner taskedit2=new Spinner(individualaccount.this);
                        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(individualaccount.this,R.array.Months,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit2.setAdapter(adapter);

                        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(individualaccount.this,R.array.Years,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit.setAdapter(adapter1);

                        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                month=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        taskedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        layout.addView(taskedit);
                        layout.addView(taskedit2);
                        final AlertDialog.Builder builders = new AlertDialog.Builder(individualaccount.this);
                        builders.setTitle("Select Accounting Period")
                                .setView(layout)
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mD1.child("Schoolfees").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()){
                                                    i1=0;
                                                    i2=0;
                                                    mBalfigcr.setText("");
                                                    mBalfigdr.setText("");
                                                    mTotalCr.setText("");
                                                    mTotalDr.setText("");
                                                    mBalCr.setText("");
                                                    mBalDr.setText("");
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                        if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(month)){
                                                            if (snapshot.child("debitac").getValue().toString().equals("Income")){
                                                                i1=i1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                mTotalDr.setText("Kshs. "+df.format(i1));
                                                            }

                                                            if (!snapshot.child("debitac").getValue().toString().equals("Income")){
                                                                i2=i2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                mTotalCr.setText("Kshs. "+df.format(i2));
                                                            }
                                                        }

                                                        if (i1>i2){
                                                            double vb=i1-i2;
                                                            //l4=(long)vb;
                                                            mBalfigcr.setText("Kshs. "+df.format(vb));
                                                            mBalDr.setText("Kshs. "+df.format(vb));
                                                            mTotalCr.setText("Kshs. "+df.format(i1));
                                                        }else {
                                                            double vb=i2-i1;
                                                            //l4=(long)vb;
                                                            mBalfigdr.setText("Kshs. "+df.format(vb));
                                                            mBalCr.setText("Kshs. "+df.format(vb));
                                                            mTotalDr.setText("Kshs. "+df.format(i2));
                                                        }
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterscf = new FirebaseRecyclerAdapter<account, accountholder>(

                                                account.class,
                                                R.layout.accountsentries_row,
                                                accountholder.class,
                                                mD1.child("Schoolfees").child("Trans").child("all").limitToLast(50)
                                        ) {
                                            @Override
                                            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                if (years(model.getTimestamp()).contains(year)&&months(model.getTimestamp()).contains(month)){
                                                    viewHolder.setDate(model.getTimestamp());
                                                    viewHolder.setDesc(model.getDescription());

                                                    if (model.getDebitac().equals("Schoolfees")){
                                                        viewHolder.setDebit(model.getAmount());
                                                        viewHolder.setCredit("0");
                                                    }else {
                                                        viewHolder.setCredit(model.getAmount());
                                                        viewHolder.setDebit("0");
                                                    }

                                                    if (Double.parseDouble(model.getAmount())<1.0){
                                                        viewHolder.Layout_hide();
                                                    }
                                                }else {
                                                    viewHolder.Layout_hide();
                                                }


                                            }
                                        };

                                        mList.setAdapter(firebaseRecyclerAdapterscf);


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
                    }
                });

                break;

            case "Expenses":
                mAccname.setText("EXPENSES ACCOUNT");

                fab2.setVisibility(View.VISIBLE);

                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layout=new LinearLayout(individualaccount.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final Spinner taskedit=new Spinner(individualaccount.this);
                        final Spinner taskedit2=new Spinner(individualaccount.this);
                        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(individualaccount.this,R.array.Months,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit2.setAdapter(adapter);

                        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(individualaccount.this,R.array.Years,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit.setAdapter(adapter1);

                        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                month=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        taskedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        layout.addView(taskedit);
                        layout.addView(taskedit2);
                        final AlertDialog.Builder builders = new AlertDialog.Builder(individualaccount.this);
                        builders.setTitle("Select Accounting Period")
                                .setView(layout)
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mD1.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()){
                                                    x1=0;
                                                    x2=0;
                                                    mBalfigcr.setText("");
                                                    mBalfigdr.setText("");
                                                    mTotalCr.setText("");
                                                    mTotalDr.setText("");
                                                    mBalCr.setText("");
                                                    mBalDr.setText("");
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                        if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(month)){
                                                            if (snapshot.child("debitac").getValue().toString().equals("Expense")){
                                                                x1=x1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                mTotalDr.setText("Kshs. "+df.format(x1));
                                                            }

                                                            if (!snapshot.child("debitac").getValue().toString().equals("Expense")){
                                                                x2=x2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                mTotalCr.setText("Kshs. "+df.format(x2));
                                                            }
                                                        }

                                                        if (x1>x2){
                                                            double vb=x1-x2;
                                                            //l4=(long)vb;
                                                            mBalfigcr.setText("Kshs. "+df.format(vb));
                                                            mBalDr.setText("Kshs. "+df.format(vb));
                                                            mTotalCr.setText("Kshs. "+df.format(x1));
                                                        }else {
                                                            double vb=x2-x1;
                                                            //l4=(long)vb;
                                                            mBalfigdr.setText("Kshs. "+df.format(vb));
                                                            mBalCr.setText("Kshs. "+df.format(vb));
                                                            mTotalDr.setText("Kshs. "+df.format(x2));
                                                        }
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterexp = new FirebaseRecyclerAdapter<account, accountholder>(

                                                account.class,
                                                R.layout.accountsentries_row,
                                                accountholder.class,
                                                mD1.child("Expenses").child("Trans").child("all").limitToLast(50)
                                        ) {
                                            @Override
                                            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                if (years(model.getTimestamp()).contains(year)&&months(model.getTimestamp()).contains(month)){
                                                    viewHolder.setDate(model.getTimestamp());
                                                    viewHolder.setDesc(model.getDescription());

                                                    if (model.getDebitac().equals("Expense")){
                                                        viewHolder.setDebit(model.getAmount());
                                                        viewHolder.setCredit("0");
                                                    }else {
                                                        viewHolder.setCredit(model.getAmount());
                                                        viewHolder.setDebit("0");
                                                    }

                                                    if (Double.parseDouble(model.getAmount())<1.0){
                                                        viewHolder.Layout_hide();
                                                    }
                                                }else {
                                                    viewHolder.Layout_hide();
                                                }


                                            }
                                        };

                                        mList.setAdapter(firebaseRecyclerAdapterexp);


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
                    }
                });

                break;
            case "FeesFines":

                mAccname.setText("FEES AND FINES ACCOUNT");

                fab2.setVisibility(View.VISIBLE);

                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layout=new LinearLayout(individualaccount.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final Spinner taskedit=new Spinner(individualaccount.this);
                        final Spinner taskedit2=new Spinner(individualaccount.this);
                        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(individualaccount.this,R.array.Months,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit2.setAdapter(adapter);

                        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(individualaccount.this,R.array.Years,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit.setAdapter(adapter1);

                        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                month=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        taskedit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String yearss=parent.getItemAtPosition(position).toString();
                                if (yearss.equals("2017")){
                                    year="17";
                                }else if (yearss.equals("2018")){
                                    year="18";
                                }else if (yearss.equals("2019")){
                                    year="19";
                                }else if (yearss.equals("2020")){
                                    year="20";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        layout.addView(taskedit);
                        layout.addView(taskedit2);
                        final AlertDialog.Builder builderss = new AlertDialog.Builder(individualaccount.this);
                        builderss.setTitle("Select Accounting Period")
                                .setView(layout)
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mD1.child("Feesfines").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()){
                                                    mBalfigcr.setText("");
                                                    mBalfigdr.setText("");
                                                    mTotalCr.setText("");
                                                    mTotalDr.setText("");
                                                    mBalCr.setText("");
                                                    mBalDr.setText("");
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).contains(month)){
                                                            if (snapshot.child("debitac").getValue().toString().equals("Feesfines")){
                                                            f1=f1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                            mTotalDr.setText("Kshs. "+df.format(f1));
                                                        }

                                                        if (!snapshot.child("debitac").getValue().toString().equals("Feesfines")){
                                                            f2=f2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                            mTotalCr.setText("Kshs. "+df.format(f2));
                                                        }
                                                        }

                                                        if (f1>f2){
                                                            double vb=f1-f2;
                                                            //l4=(long)vb;
                                                            mBalfigcr.setText("Kshs. "+df.format(vb));
                                                            mBalDr.setText("Kshs. "+df.format(vb));
                                                            mTotalCr.setText("Kshs. "+df.format(f1));
                                                        }else {
                                                            double vb=f2-f1;
                                                            //l4=(long)vb;
                                                            mBalfigdr.setText("Kshs. "+df.format(vb));
                                                            mBalCr.setText("Kshs. "+df.format(vb));
                                                            mTotalDr.setText("Kshs. "+df.format(f2));
                                                        }
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterfines = new FirebaseRecyclerAdapter<account, accountholder>(

                                                account.class,
                                                R.layout.accountsentries_row,
                                                accountholder.class,
                                                mD1.child("Feesfines").child("Trans").child("all").limitToLast(50)
                                        ) {
                                            @Override
                                            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                if (years(model.getTimestamp()).contains(year)&&months(model.getTimestamp()).contains(month)){
                                                    viewHolder.setDate(model.getTimestamp());
                                                    viewHolder.setDesc(model.getDescription());

                                                    if (model.getDebitac().equals("Feesfines")){
                                                        viewHolder.setDebit(model.getAmount());
                                                        viewHolder.setCredit("0");
                                                    }else {
                                                        viewHolder.setCredit(model.getAmount());
                                                        viewHolder.setDebit("0");
                                                    }

                                                    if (Double.parseDouble(model.getAmount())<1.0){
                                                        viewHolder.Layout_hide();
                                                    }
                                                }else {
                                                    viewHolder.Layout_hide();
                                                }


                                            }
                                        };

                                        mList.setAdapter(firebaseRecyclerAdapterfines);

                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert121 = builderss.create();
                        alert121.show();
                    }
                });

                break;

            case "Petty":

                mAccname.setText("PETTY CASH ACCOUNT");

                mD1.child("Petty").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v3=0;
                        v4=0;
                        l2=0;
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("Petty").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("Petty")){
                                    v3=v3+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(v3));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Petty")){
                                    v4=v4+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(v4));
                                }

                            }
                            if (v3>v4){
                                double vb=v3-v4;
                                l2=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(l2));
                                mBalDr.setText("Kshs. "+df.format(l2));
                                mTotalCr.setText("Kshs. "+df.format(v3));
                            }else {
                                double vb=v4-v3;
                                l2=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(l2));
                                mBalCr.setText("Kshs. "+df.format(l2));
                                mTotalDr.setText("Kshs. "+df.format(v4));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdaptepetty = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Petty").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());
                        if (model.getDebitac().equals("Petty")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }
                        if (!model.getDebitac().equals("Petty")){
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdaptepetty);


                break;

            case "ProfitRes":

                mAccname.setText("PROFIT RESERVES ACCOUNT");

                mD1.child("ProfitReserves").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v3=0;
                        v4=0;
                        l2=0;
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("ProfitReserves").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                    v3=v3+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(v3));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                    v4=v4+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(v4));
                                }

                            }
                            if (v3>v4){
                                double vb=v3-v4;
                                l2=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(l2));
                                mBalDr.setText("Kshs. "+df.format(l2));
                                mTotalCr.setText("Kshs. "+df.format(v3));
                            }else {
                                double vb=v4-v3;
                                l2=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(l2));
                                mBalCr.setText("Kshs. "+df.format(l2));
                                mTotalDr.setText("Kshs. "+df.format(v4));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapteprof = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("ProfitReserves").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());
                        if (model.getDebitac().equals("ProfitReserves")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }
                        if (!model.getDebitac().equals("ProfitReserves")){
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdapteprof);


                break;

            case "Unhanded":

                mAccname.setText("Officer's Imprest Account");

                mD1.child("Unhanded").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBalfigcr.setText("");
                        mBalfigdr.setText("");
                        mTotalCr.setText("");
                        mTotalDr.setText("");
                        mBalCr.setText("");
                        mBalDr.setText("");
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                if (Double.parseDouble(snapshot.child("amount").getValue().toString())<1.0){
                                    DatabaseReference rem=mD1.child("Unhanded").child("Trans").child("all").child(snapshot.getKey());
                                    rem.removeValue();
                                }

                                if (snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                    cc1=cc1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(cc1));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                    cc2=cc2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(cc2));
                                }



                            }
                            if (cc1>cc2){
                                double vb=cc1-cc2;
                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                mBalDr.setText("Kshs. "+df.format(vb));
                                mTotalCr.setText("Kshs. "+df.format(cc1));
                            }else {
                                double vb=cc2-cc1;
                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                mBalCr.setText("Kshs. "+df.format(vb));
                                mTotalDr.setText("Kshs. "+df.format(cc2));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterunha = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Unhanded").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());

                        if (model.getDebitac().equals("Unhanded")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                        if (model.getDescription().contains("banking")&&!model.getCreditac().equals("Schoolfees")){

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String officer=model.getCreditac();
                                    final String dayx=new SimpleDateFormat("EEE, MMM d, ''yy").format(model.getTimestamp());
                                    mD1.child("Imprests").child(model.getCreditac()).child(formateddate(model.getTimestamp())).child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            bc8=0;
                                            bc9=0;
                                            mBalfigcr.setText("");
                                            mBalfigdr.setText("");
                                            mTotalCr.setText("");
                                            mTotalDr.setText("");
                                            mBalCr.setText("");
                                            mBalDr.setText("");

                                            if (dataSnapshot.hasChildren()){
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    if (snapshot.child("amount").getValue().toString().equals("0")){
                                                        DatabaseReference rm=mD1.child("Imprests").child(model.getCreditac()).child(formateddate(model.getTimestamp())).child("Trans").child("all").child(snapshot.getKey());
                                                        rm.removeValue();
                                                    }

                                                    if (snapshot.child("debitac").getValue().toString().equals(officer)){
                                                        bc8=bc8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                        mTotalDr.setText("Kshs. "+df.format(bc8));
                                                    }

                                                    if (!snapshot.child("debitac").getValue().toString().equals(officer)){
                                                        bc9=bc9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                        mTotalCr.setText("Kshs. "+df.format(bc9));
                                                    }
                                                }
                                                if (bc8>bc9){
                                                    double vb=bc8-bc9;
                                                    mBalfigcr.setText("Kshs. "+df.format(vb));
                                                    mBalDr.setText("Kshs. "+df.format(vb));
                                                    mTotalCr.setText("Kshs. "+df.format(bc8));
                                                }else {
                                                    double vb=bc9-bc8;
                                                    mBalfigdr.setText("Kshs. "+df.format(vb));
                                                    mBalCr.setText("Kshs. "+df.format(vb));
                                                    mTotalDr.setText("Kshs. "+df.format(bc9));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdaptrest = new FirebaseRecyclerAdapter<account, accountholder>(

                                            account.class,
                                            R.layout.accountsentries_row,
                                            accountholder.class,
                                            mD1.child("Imprests").child(model.getCreditac()).child(formateddate(model.getTimestamp())).child("Trans").child("all").limitToLast(50)
                                    ) {
                                        @Override
                                        protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                            viewHolder.setDate(model.getTimestamp());
                                            viewHolder.setDesc(model.getDescription());
                                            if (model.getDebitac().equals(officer)){
                                                viewHolder.setDebit(model.getAmount());
                                                viewHolder.setCredit("0");
                                            }else {
                                                viewHolder.setCredit(model.getAmount());
                                                viewHolder.setDebit("0");
                                            }
                                        }
                                    };

                                    mList.setAdapter(firebaseRecyclerAdaptrest);
                                }
                            });

                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdapterunha);

                break;

            case "Memberfund":

                mAccname.setText("MEMBER SAVINGS ACCOUNT");

                mD1.child("Membersfund").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            r1=0;
                            r2=0;
                            mBalfigcr.setText("");
                            mBalfigdr.setText("");
                            mTotalCr.setText("");
                            mTotalDr.setText("");
                            mBalCr.setText("");
                            mBalDr.setText("");
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                if (snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                    r1=r1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalDr.setText("Kshs. "+df.format(r1));
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                    r2=r2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                    mTotalCr.setText("Kshs. "+df.format(r2));
                                }
                            }
                            if (r1>r2){
                                double vb=r1-r2;
                                //l4=(long)vb;
                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                mBalDr.setText("Kshs. "+df.format(vb));
                                mTotalCr.setText("Kshs. "+df.format(r1));
                            }else {
                                double vb=r2-r1;
                                //l4=(long)vb;
                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                mBalCr.setText("Kshs. "+df.format(vb));
                                mTotalDr.setText("Kshs. "+df.format(r2));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapterlsf = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.accountsentries_row,
                        accountholder.class,
                        mD1.child("Membersfund").child("Trans").child("all").limitToLast(50)
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        viewHolder.setDate(model.getTimestamp());
                        viewHolder.setDesc(model.getDescription());

                        if (model.getDebitac().equals("Membersfund")){
                            viewHolder.setDebit(model.getAmount());
                            viewHolder.setCredit("0");
                        }else {
                            viewHolder.setCredit(model.getAmount());
                            viewHolder.setDebit("0");
                        }

                        if (Double.parseDouble(model.getAmount())<1.0){
                            viewHolder.Layout_hide();
                        }

                    }
                };

                mList.setAdapter(firebaseRecyclerAdapterlsf);



                break;
            default:

                fab.setVisibility(View.VISIBLE);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(individualaccount.this);
                        builder.setTitle("Select Account")
                                .setCancelable(true)
                                .setItems(R.array.memberaccount, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        mAccname.setText("LSF Account for "+dataSnapshot.child("details").child("name").getValue().toString());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        v1=0;
                                                        v2=0;
                                                        mBalfigcr.setText("");
                                                        mBalfigdr.setText("");
                                                        mTotalCr.setText("");
                                                        mTotalDr.setText("");
                                                        mBalCr.setText("");
                                                        mBalDr.setText("");
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                if (snapshot.child("name").getValue().toString().equals("LSF")){

                                                                    if (snapshot.child("debitac").getValue().toString().equals("member")){
                                                                        v1=v1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalDr.setText("Kshs. "+df.format(v1));
                                                                    }

                                                                    if (snapshot.child("creditac").getValue().toString().equals("member")){
                                                                        v2=v2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalCr.setText("Kshs. "+df.format(v2));
                                                                    }
                                                                }
                                                            }
                                                            if (v1>v2){
                                                                double vb=v1-v2;
                                                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                                                mBalDr.setText("Kshs. "+df.format(vb));
                                                                mTotalCr.setText("Kshs. "+df.format(v1));
                                                            }else {
                                                                double vb=v2-v1;
                                                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                                                mBalCr.setText("Kshs. "+df.format(vb));
                                                                mTotalDr.setText("Kshs. "+df.format(v2));
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<account, accountholder>(

                                                        account.class,
                                                        R.layout.accountsentries_row,
                                                        accountholder.class,
                                                        mD2
                                                ) {
                                                    @Override
                                                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                        if (model.getName().equals("LSF")){
                                                            viewHolder.setDate(model.getTimestamp());
                                                            viewHolder.setDesc(model.getDescription());

                                                            if (model.getDebitac().equals("member")){
                                                                viewHolder.setDebit(model.getAmount());
                                                                viewHolder.setCredit("0");
                                                            }else {
                                                                viewHolder.setCredit(model.getAmount());
                                                                viewHolder.setDebit("0");
                                                            }

                                                            if (Double.parseDouble(model.getAmount())==0.0){
                                                                viewHolder.Layout_hide();
                                                            }
                                                        }else {
                                                            viewHolder.Layout_hide();
                                                        }

                                                    }
                                                };

                                                mList.setAdapter(firebaseRecyclerAdapter);
                                                break;
                                            case 1:
                                                mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        mAccname.setText("Loan Account for "+dataSnapshot.child("details").child("name").getValue().toString());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        v1=0;
                                                        v2=0;
                                                        mBalfigcr.setText("");
                                                        mBalfigdr.setText("");
                                                        mTotalCr.setText("");
                                                        mTotalDr.setText("");
                                                        mBalCr.setText("");
                                                        mBalDr.setText("");
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                if (snapshot.child("name").getValue().toString().equals("Loan")){

                                                                    if (snapshot.child("debitac").getValue().toString().equals("member")){
                                                                        v1=v1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalDr.setText("Kshs. "+df.format(v1));
                                                                    }

                                                                    if (snapshot.child("creditac").getValue().toString().equals("member")){
                                                                        v2=v2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalCr.setText("Kshs. "+df.format(v2));
                                                                    }
                                                                }
                                                            }
                                                            if (v1>v2){
                                                                double vb=v1-v2;
                                                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                                                mBalDr.setText("Kshs. "+df.format(vb));
                                                                mTotalCr.setText("Kshs. "+df.format(v1));
                                                            }else {
                                                                double vb=v2-v1;
                                                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                                                mBalCr.setText("Kshs. "+df.format(vb));
                                                                mTotalDr.setText("Kshs. "+df.format(v2));
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<account, accountholder>(

                                                        account.class,
                                                        R.layout.accountsentries_row,
                                                        accountholder.class,
                                                        mD2
                                                ) {
                                                    @Override
                                                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                        if (model.getName().equals("Loan")){
                                                            viewHolder.setDate(model.getTimestamp());
                                                            viewHolder.setDesc(model.getDescription());

                                                            if (model.getDebitac().equals("member")){
                                                                viewHolder.setDebit(model.getAmount());
                                                                viewHolder.setCredit("0");
                                                            }else {
                                                                viewHolder.setCredit(model.getAmount());
                                                                viewHolder.setDebit("0");
                                                            }

                                                            if (Double.parseDouble(model.getAmount())==0.0){
                                                                viewHolder.Layout_hide();
                                                            }
                                                        }else {
                                                            viewHolder.Layout_hide();
                                                        }

                                                    }
                                                };

                                                mList.setAdapter(firebaseRecyclerAdapter2);
                                                break;
                                            case 2:

                                                mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        mAccname.setText("Advance Account for "+dataSnapshot.child("details").child("name").getValue().toString());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        v1=0;
                                                        v2=0;
                                                        mBalfigcr.setText("");
                                                        mBalfigdr.setText("");
                                                        mTotalCr.setText("");
                                                        mTotalDr.setText("");
                                                        mBalCr.setText("");
                                                        mBalDr.setText("");
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                                                if (snapshot.child("name").getValue().toString().equals("Short Term Loan")) {

                                                                    if (snapshot.child("debitac").getValue().toString().equals("member")) {
                                                                        v1 = v1 + Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalDr.setText("Kshs. " + df.format(v1));
                                                                    }

                                                                    if (snapshot.child("creditac").getValue().toString().equals("member")) {
                                                                        v2 = v2 + Double.parseDouble(snapshot.child("amount").getValue().toString());

                                                                        mTotalCr.setText("Kshs. " + df.format(v2));
                                                                    }
                                                                }
                                                            }
                                                            if (v1>v2){
                                                                double vb=v1-v2;
                                                                mBalfigcr.setText("Kshs. "+df.format(vb));
                                                                mBalDr.setText("Kshs. "+df.format(vb));
                                                                mTotalCr.setText("Kshs. "+df.format(v1));
                                                            }else {
                                                                double vb=v2-v1;
                                                                mBalfigdr.setText("Kshs. "+df.format(vb));
                                                                mBalCr.setText("Kshs. "+df.format(vb));
                                                                mTotalDr.setText("Kshs. "+df.format(v2));
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter3 = new FirebaseRecyclerAdapter<account, accountholder>(

                                                        account.class,
                                                        R.layout.accountsentries_row,
                                                        accountholder.class,
                                                        mD2
                                                ) {
                                                    @Override
                                                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                                        if (model.getName().equals("Short Term Loan")){
                                                            viewHolder.setDate(model.getTimestamp());
                                                            viewHolder.setDesc(model.getDescription());

                                                            if (model.getDebitac().equals("member")){
                                                                viewHolder.setDebit(model.getAmount());
                                                                viewHolder.setCredit("0");
                                                            }else {
                                                                viewHolder.setCredit(model.getAmount());
                                                                viewHolder.setDebit("0");
                                                            }

                                                            if (Double.parseDouble(model.getAmount())==0.0){
                                                                viewHolder.Layout_hide();
                                                            }
                                                        }else {
                                                            viewHolder.Layout_hide();
                                                        }

                                                    }
                                                };

                                                mList.setAdapter(firebaseRecyclerAdapter3);

                                                break;
                                        }
                                    }
                                });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                });
                break;
        }



    }

    public static class banktrsholder extends RecyclerView.ViewHolder {
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        private TextView zoom;
        DecimalFormat df = new DecimalFormat("##,###,###.#");

        public banktrsholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout =  itemView.findViewById(R.id.bankaccrow_row);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            zoom=itemView.findViewById(R.id.badate);
        }
        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.badate);
            mGroupname.setText(groupname);
        }
        public void setChequenum(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.bachequenum);
            mGrouploc.setText(venue);
        }
        public void setDetail(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.badetails);
            mGrouploc.setText(date);
        }
        public void setDepo(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.badepo);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
        public void setWith(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.bawith);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
    }

    public static class accountholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        //private TextView link;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        DecimalFormat df = new DecimalFormat("##,###,###.#");


        public accountholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.accountingentries);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //link=mView.findViewById(R.id.btrname);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(long date){
            TextView loandate = (TextView)mView.findViewById(R.id.entrydate);
            loandate.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(date));
        }
        public void setDesc(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydetail);
            amountgiven.setText(amount);
        }
        public void setDebit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setCredit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrycr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
    }

    private String years(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        return sdfs.format(date);
    }
    private String months(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        return sdfs.format(date);
    }
    private String formateddate(long date){
        SimpleDateFormat sdfs = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        return sdfs.format(date);
    }
    private String modyear(String date){
        switch (date) {
            case "2018": {
                modyea = "'18";
                //return modyea;
                break;
            }
            case "2019": {
                modyea = "'19";
                //return modyea;
                break;
            }
            case "2020": {
                modyea = "'20";
                //return modyea;
                break;
            }
        }
        return modyea;
    }
}
