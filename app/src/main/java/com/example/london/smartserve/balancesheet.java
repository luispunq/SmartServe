package com.example.london.smartserve;

import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class balancesheet extends AppCompatActivity {
    private TextView acashthis,acashlast,aloansthis,aloanlast,aadvthis,aadvlast,ldepothis,ldepolast,mYealB,mLastlB,mYeaaB,mLastaB,totassest;
    private DatabaseReference databaseReference;
    private double withcr,depocr,withcrthis,depocrthis;
    private long l1,l2,l3,l4;
    private String yearx;
    private double totalcl=0,totalcp=0,v1=0,v2=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,v9=0,m1=0,m2=0,m3=0,totalcf=0,manbal,secbal,cc1,cc2,cc3;

    DecimalFormat df = new DecimalFormat("##,###,###.#");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancesheet);

        acashthis=findViewById(R.id.cashdr);
        acashlast=findViewById(R.id.cashcr);
        aloansthis=findViewById(R.id.loansgvdr);
        aloanlast=findViewById(R.id.loansgvcr);
        aadvthis=findViewById(R.id.advgvdr);
        aadvlast=findViewById(R.id.advgvcr);
        ldepothis=findViewById(R.id.liacashdr);
        ldepolast=findViewById(R.id.liacashcr);

        mYealB=findViewById(R.id.yearlebol);
        mLastaB=findViewById(R.id.lastyear);
        mYeaaB=findViewById(R.id.yearlebo);
        mLastlB=findViewById(R.id.lastyearl);

        mYeaaB.setText(thisyear());
        mYealB.setText(thisyear());
        mLastaB.setText(lastyear());
        mLastlB.setText(lastyear());

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Accounting");

        databaseReference.child("Bank").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double v1=0;
                double v2=0;
                double v3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        long daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());

                        yearx=new SimpleDateFormat("yyyy").format(daate);

                        if (yearx.equals(thisyear())){
                            double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                            v1=v1+cl;

                            double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                            v2=v2+c2;

                            v3=v2-v1;

                        }
                        double advpy = Math.round(v3*100)/100;

                        acashthis.setText("Kshs. "+advpy);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Bank").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double v1=0;
                double v2=0;
                double v3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        long daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());

                        yearx=new SimpleDateFormat("yyyy").format(daate);

                        if (yearx.equals(lastyear())){
                            double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                            v1=v1+cl;

                            double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                            v2=v2+c2;

                            v3=v2-v1;

                        }
                        double advpy = Math.round(v3*100)/100;

                        acashlast.setText("Kshs. "+advpy);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Longtermloan").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                l1=0;
                l2=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                            v8=v8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                            v9=v9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        v7=v8-v9;
                        l1=(long)v7;
                        aloansthis.setText("Kshs "+df.format(l1));
                    }
                }else {
                    aloansthis.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Shorttermloans").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                            m1=m1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                            m2=m2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        m3=m1-m2;
                        l2=(long)m3;
                        aadvthis.setText("Kshs "+df.format(l2));
                    }
                }else {
                    aadvthis.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Membersfund").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double v1=0;
                double v2=0;
                double v3=0;
                double v4=0,v5=0;
                long daate;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("description").exists()&&snapshot.child("description").getValue().toString().equals("Withdraw")){
                            if (snapshot.child("timestamp").exists()){
                                daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());
                            }
                            else {
                                daate=Long.parseLong("1562260309753");
                            }

                            yearx=new SimpleDateFormat("yyyy").format(daate);

                            if (yearx.equals(lastyear())){
                                String am=snapshot.child("amount").getValue().toString();
                                double amm=Double.parseDouble(am);
                                v1=v1+amm;
                                withcr=v1;
                            }
                        }

                        if (snapshot.child("description").exists()&&snapshot.child("description").getValue().toString().equals("Deposit")){
                            if (snapshot.child("timestamp").exists()){
                                daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());
                            }
                            else {
                                daate=Long.parseLong("1562260309753");
                            }

                            yearx=new SimpleDateFormat("yyyy").format(daate);

                            if (yearx.equals(lastyear())){
                                String am=snapshot.child("amount").getValue().toString();
                                double amm=Double.parseDouble(am);
                                v2=v2+amm;
                                depocr=v2;
                            }
                        }

                        double lastacc=depocr-withcr;
                        long f1=(long)lastacc;

                        if (snapshot.child("description").exists()&&snapshot.child("description").getValue().toString().equals("Withdraw")){
                            if (snapshot.child("timestamp").exists()){
                                daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());
                            }
                            else {
                                daate=Long.parseLong("1562260309753");
                            }

                            yearx=new SimpleDateFormat("yyyy").format(daate);

                            if (yearx.equals(thisyear())){
                                String am=snapshot.child("amount").getValue().toString();
                                double amm=Double.parseDouble(am);
                                v4=v4+amm;
                                withcrthis=v4;
                            }
                        }

                        if (snapshot.child("description").exists()&&snapshot.child("description").getValue().toString().equals("Deposit")){
                            if (snapshot.child("timestamp").exists()){
                                daate=Long.parseLong(snapshot.child("timestamp").getValue().toString());
                            }
                            else {
                                daate=Long.parseLong("1562260309753");
                            }

                            yearx=new SimpleDateFormat("yyyy").format(daate);

                            if (yearx.equals(thisyear())){
                                String am=snapshot.child("amount").getValue().toString();
                                double amm=Double.parseDouble(am);
                                v5=v5+amm;
                                depocrthis=v5;
                            }
                        }

                        double thisacc=depocrthis-withcrthis;
                        long f2=(long)thisacc;

                        ldepolast.setText("Kshs. "+df.format(f1));
                        ldepothis.setText("Kshs. "+df.format(f2));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private String lastyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.UK);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(java.util.Calendar.YEAR, -1); //Remove 1 year

        return sdf.format(c.getTime());
    }

    private String thisyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.UK);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.

        return sdf.format(c.getTime());
    }

}
