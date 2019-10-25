package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class monthlycashstatement extends AppCompatActivity {
    private TextView dat,mCashout,mCashin,mPesaR,mPesaB;
    private RecyclerView trsdetails;
    private DatabaseReference mD,mD2,mD3,mD4;
    private double totalcl=0,totalcp=0,totalcb=0,totalcf=0;
    private double m1,m2,m3,mm1,mm2,mm3;
    private String date=null,month,year,yearT;
    private long dte,l2,ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlycashstatement);
        date=getIntent().getExtras().getString("date");
        String string = date;
        String[] parts = string.split("-");
        month = parts[0];
        year = parts[1];

        if (year.equals("18")){
            yearT="2018";
        }else if (year.equals("19")){
            yearT="2019";
        }
        dat=findViewById(R.id.asat);
        mCashin=findViewById(R.id.pettyacc);
        mCashout=findViewById(R.id.bnkacc);
        mPesaR=findViewById(R.id.mpsacc);
        mPesaB=findViewById(R.id.memfundacc);
        trsdetails=findViewById(R.id.dailyitemsin);
        trsdetails.setHasFixedSize(true);
        trsdetails.setLayoutManager(new LinearLayoutManager(this));

        mD= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Bank");
        mD2=FirebaseDatabase.getInstance().getReference().child("Accounting");
        final DecimalFormat df = new DecimalFormat("##,###,###.#");

        dat.setText(month);

        mD.child("Trans").child(yearT).child(month).child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                        totalcl=totalcl+cl;

                        double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                        totalcp=totalcp+c2;

                        mCashin.setText("Kshs. "+String.valueOf(totalcp));

                        mCashout.setText("Kshs. "+String.valueOf(totalcl));
                    }
                }else {
                    mCashin.setText("Kshs. 0");

                    mCashout.setText("Kshs. 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.child("Mpesa").child("Trans").child(yearT).child(month).child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                            m1=m1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                            m2=m2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        m3=m1-m2;
                        l2=(long)m3;
                        mPesaR.setText("Kshs "+df.format(l2));
                        //mPesaR.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mPesaR.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.child("Mpesa").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                            mm1=mm1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                            mm2=mm2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        mm3=mm1-mm2;
                        ll2=(long)mm3;
                        mPesaB.setText("Kshs "+df.format(ll2));
                        //mPesaR.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mPesaB.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<bankacc,banktrsholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<bankacc,banktrsholder>(

                bankacc.class,
                R.layout.bankacc_row,
                banktrsholder.class,
                mD.child("Trans").child(yearT).child(month).child("all").orderByChild("timestamp")
        )
        {
            @Override
            protected void populateViewHolder(final banktrsholder viewHolder, final bankacc model, final int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setChequenum(model.getChequenumber());
                viewHolder.setDetail(model.getDetails());
                viewHolder.setDepo(model.getDeposit());
                viewHolder.setWith(model.getWithdraw());

                //viewHolder.Layout_hide();

                viewHolder.zoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String final_date=month+"-"+year;
                        Intent next = new Intent(monthlycashstatement.this,banktransactions.class);
                        Bundle extras = new Bundle();
                        extras.putString("type", "month");
                        extras.putString("date", final_date);
                        next.putExtras(extras);
                        startActivity(next);
                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getWithdraw().equals("0")){
                            Intent userwindo = new Intent(monthlycashstatement.this,masterallreports.class);
                            userwindo.putExtra("date",date);
                            startActivity(userwindo);
                        }
                    }
                });

            }


        };
        trsdetails.setAdapter(firebaseRecyclerAdapter);

    }

    public static class banktrsholder extends RecyclerView.ViewHolder {
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        private TextView zoom;

        public banktrsholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout =  itemView.findViewById(R.id.bankaccrow_row);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            zoom=itemView.findViewById(R.id.badate);
        }
        private void Layout_hide(){
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
            TextView mGrouploc = (TextView)mView.findViewById(R.id.badepo);
            mGrouploc.setText(amount);
        }
        public void setWith(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.bawith);
            mGrouploc.setText(amount);
        }
    }
}
