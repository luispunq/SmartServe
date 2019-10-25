package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class bankact extends AppCompatActivity {
    private TextView mbankname,balanceasat,accnumb,signatories,rebalance,bagen;
    private FloatingActionButton floatingActionButton;
    private RecyclerView banktrss;
    private DatabaseReference mDatabase,mD,mD2;
    private double totalcl=0,totalcp=0,totalcb=0,totalcf=0;
    private String banknam="default",choice="default";
    private HorizontalScrollView da;
    private FloatingActionButton fab;
    private Spinner taskedit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankact);
        mbankname=findViewById(R.id.bankname);
        balanceasat=findViewById(R.id.asat);
        accnumb=findViewById(R.id.pettyacc);
        signatories=findViewById(R.id.mpsacc);
        rebalance=findViewById(R.id.daiybudgettot);
        bagen=findViewById(R.id.bnkacc);
        floatingActionButton=findViewById(R.id.expanddaily);
        banktrss=findViewById(R.id.dailyitemsin);
        da=findViewById(R.id.dailyitems);
        fab=findViewById(R.id.bankselector);
        taskedit2=new Spinner(bankact.this);
        banktrss.setHasFixedSize(true);
        banktrss.setLayoutManager(new LinearLayoutManager(this));

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("Smart Serve Banks");

        balanceasat.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

        mD2= FirebaseDatabase.getInstance().getReference().child("Accounting");

        mD2.child("Bank").child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> banks = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.getKey();
                    banks.add(type);
                }
                banks.add("default");

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(bankact.this, android.R.layout.simple_spinner_item, banks);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                taskedit2.setAdapter(areasAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                banknam=parent.getItemAtPosition(position).toString();
                choice=banknam;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(bankact.this);
                if(taskedit2.getParent() != null) {
                    ((ViewGroup)taskedit2.getParent()).removeView(taskedit2); // <- fix
                }
                builders.setTitle("Bank Accounts")
                        .setMessage("Select Bank Account")
                        .setCancelable(true)
                        .setView(taskedit2)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choice=banknam;
                                dialog.dismiss();

                                if (choice.equals("default")){
                                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Bank").child("Trans").child("all");
                                    mD=FirebaseDatabase.getInstance().getReference().child("Accounting").child("Bank").child("Accounts");

                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            totalcl=0;
                                            totalcp=0;
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                DecimalFormat df = new DecimalFormat("##,###,###.#");
                                                double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                                                totalcl=totalcl+cl;

                                                double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                                                totalcp=totalcp+c2;

                                                totalcf=totalcp-totalcl;

                                                bagen.setText("Kshs. "+String.valueOf(df.format(totalcf)));
                                                rebalance.setText("Kshs. "+String.valueOf(df.format(totalcf)));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    mbankname.setText("All Accounts");

                                    mbankname.setText("All Accounts");
                                    accnumb.setText("-");
                                    signatories.setText("-");

                                    FirebaseRecyclerAdapter<bankacc,banktrsholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<bankacc,banktrsholder>(

                                            bankacc.class,
                                            R.layout.bankacc_row,
                                            banktrsholder.class,
                                            mDatabase
                                    )
                                    {
                                        @Override
                                        protected void populateViewHolder(final banktrsholder viewHolder, final bankacc model, final int position) {

                                            viewHolder.setDate(model.getDate());
                                            viewHolder.setChequenum(model.getChequenumber());
                                            viewHolder.setDetail(model.getDetails());
                                            viewHolder.setDepo(model.getDeposit());
                                            viewHolder.setWith(model.getWithdraw());

                                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent home = new Intent(bankact.this, individualaccount.class);
                                                    home.putExtra("option", "Bank");
                                                    startActivity(home);
                                                }
                                            });

                                        }


                                    };
                                    banktrss.setAdapter(firebaseRecyclerAdapter);



                                }else {
                                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Bank").child("Accounts").child(banknam).child("Trans").child("all");
                                    mD=FirebaseDatabase.getInstance().getReference().child("Accounting").child("Bank").child("Accounts");

                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            totalcl=0;
                                            totalcp=0;
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                DecimalFormat df = new DecimalFormat("##,###,###.#");
                                                double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                                                totalcl=totalcl+cl;

                                                double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                                                totalcp=totalcp+c2;

                                                totalcf=totalcp-totalcl;

                                                bagen.setText("Kshs. "+String.valueOf(df.format(totalcf)));
                                                rebalance.setText("Kshs. "+String.valueOf(df.format(totalcf)));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    mD.child(banknam).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mbankname.setText(dataSnapshot.child("Bankname").getValue().toString());
                                            accnumb.setText(dataSnapshot.child("Accountnum").getValue().toString());
                                            signatories.setText(dataSnapshot.child("Accountsig").getValue().toString());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    FirebaseRecyclerAdapter<bankacc,banktrsholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<bankacc,banktrsholder>(

                                            bankacc.class,
                                            R.layout.bankacc_row,
                                            banktrsholder.class,
                                            mDatabase
                                    )
                                    {
                                        @Override
                                        protected void populateViewHolder(final banktrsholder viewHolder, final bankacc model, final int position) {

                                            viewHolder.setDate(model.getDate());
                                            viewHolder.setChequenum(model.getChequenumber());
                                            viewHolder.setDetail(model.getDetails());
                                            viewHolder.setDepo(model.getDeposit());
                                            viewHolder.setWith(model.getWithdraw());

                                        }


                                    };
                                    banktrss.setAdapter(firebaseRecyclerAdapter);
                                }
                            }
                        })
                        .setNegativeButton("Add Bank Account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent next =new Intent(bankact.this,banksactivity.class);
                                startActivity(next);
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (da.getVisibility()==View.VISIBLE){
                    da.setVisibility(View.GONE);
                    floatingActionButton.setBackground(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
                }else {
                    da.setVisibility(View.VISIBLE);
                    floatingActionButton.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }
            }
        });






    }

    public static class banktrsholder extends RecyclerView.ViewHolder {

        View mView;

        public banktrsholder(View itemView) {
            super(itemView);

            mView = itemView;
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
            TextView mGrouploc = (TextView)mView.findViewById(R.id.badepo);
            mGrouploc.setText(amount);
        }
        public void setWith(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.bawith);
            mGrouploc.setText(amount);
        }
    }
}
