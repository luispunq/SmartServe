package com.example.london.smartserve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profitloss extends AppCompatActivity {
    private TextView mIncom,mFees,mIncTot,mSal,mRent,mOffice,mTra,mExpenTot,mGrand;
    private DatabaseReference databaseReference;
    private double v1=0,v2,v3,v4,v5,v6,v7,v8,v9=0,cashdr=0,loandr=0,advandr=0,purchdr=0,advdr=0,rentdr=0,salodr=0,insurdr=0,intrcr=0,lsfcr=0,withcr=0,feescr=0,mpesadr=0;
    private String yearx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitloss);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Accounting");

        mIncom=findViewById(R.id.cashcr);
        mFees=findViewById(R.id.loansgvcr);
        mIncTot=findViewById(R.id.totcr);
        mSal=findViewById(R.id.liasaldr);
        mRent=findViewById(R.id.liarentdr);
        mOffice=findViewById(R.id.liaoedr);
        mTra=findViewById(R.id.liatrdr);
        mExpenTot=findViewById(R.id.liatotdr);

        mGrand=findViewById(R.id.grandtot);

        databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v1=0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("description").getValue().toString().equals("Daily Expense")){
                        String am=snapshot.child("amount").getValue().toString();
                        double amm=Double.parseDouble(am);
                        v1=v1+amm;
                        purchdr=v1;
                    }
                }
                mOffice.setText("Kshs. "+String.format("%1$,.2f",v1));

                databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        v2=0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.child("description").getValue().toString().equals("Rent")){
                                String am=snapshot.child("amount").getValue().toString();
                                double amm=Double.parseDouble(am);
                                v2=v2+amm;
                                rentdr=v2;
                            }
                        }
                        mRent.setText("Kshs. "+String.format("%1$,.2f",v2));

                        databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                v3=0;
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    if (snapshot.child("description").getValue().toString().equals("Salaries")){
                                        String am=snapshot.child("amount").getValue().toString();
                                        double amm=Double.parseDouble(am);
                                        v3=v3+amm;
                                        salodr=v3;
                                    }
                                }
                                mSal.setText("Kshs. "+String.format("%1$,.2f",v3));

                                databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        v4=0;
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            if (snapshot.child("description").getValue().toString().equals("Transport")){
                                                String am=snapshot.child("amount").getValue().toString();
                                                double amm=Double.parseDouble(am);
                                                v4=v4+amm;
                                                insurdr=v4;
                                            }
                                        }
                                        mTra.setText("Kshs. "+String.format("%1$,.2f",v4));

                                        databaseReference.child("Feesfines").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                v5=0;
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    String am=snapshot.child("amount").getValue().toString();
                                                    double amm=Double.parseDouble(am);
                                                    v5=v5+amm;
                                                    feescr=v5;
                                                }
                                                mFees.setText("Kshs. "+String.format("%1$,.2f",v5));

                                                databaseReference.child("Income").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        v6=0;
                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                            String am=snapshot.child("amount").getValue().toString();
                                                            double amm=Double.parseDouble(am);
                                                            v6=v6+amm;
                                                            intrcr=v6;
                                                        }
                                                        mIncom.setText("Kshs. "+String.format("%1$,.2f",v6));

                                                        mIncTot.setText("Kshs. "+String.format("%1$,.2f",intrcr+feescr));
                                                        mExpenTot.setText("Kshs. "+String.format("%1$,.2f",purchdr+rentdr+salodr+insurdr));

                                                        mGrand.setText("Kshs. "+String.format("%1$,.2f",(intrcr+feescr)-(purchdr+rentdr+salodr+insurdr)));
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
