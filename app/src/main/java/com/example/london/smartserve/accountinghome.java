package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class accountinghome extends AppCompatActivity {
    private TextView asat,bank,petty,mpesa,memfund,income,loan,feesfines,unhanded,expenses,mschfees,mSuspense,mProfitR;
    private CardView c1,c2,c3,c4,c5,c6,c7,c8;
    private DatabaseReference mD,mD1,mD2,mDatabse,mDatabase;
    private long l1,l2,l3,l4;
    private double totalcl=0,totalcp=0,p1=0,p2=0,p3=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,v9=0,m1=0,m2=0,
            m3=0,totalcf=0,manbal,secbal,cc1,cc2,cc3,i1=0,i2=0,i3,f1=0,f2=0,f3=0,u1=0,u2=0,u3=0,sf1=0,sf2=0,sf3=0,ss1=0,ss2=0,ss3=0;

    private String month=new SimpleDateFormat("MMM").format(new Date()),year=new SimpleDateFormat("yyyy").format(new Date());

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountinghome);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("Smart Serve Accounts");

        asat=findViewById(R.id.asat);
        bank=findViewById(R.id.bnkacc);
        petty=findViewById(R.id.pettyacc);
        unhanded=findViewById(R.id.unhanded);
        mpesa=findViewById(R.id.mpsacc);
        memfund=findViewById(R.id.memfundacc);
        income=findViewById(R.id.incacc);
        feesfines=findViewById(R.id.feesnfines);
        loan=findViewById(R.id.loansacc);
        expenses=findViewById(R.id.expnses);
        mschfees=findViewById(R.id.scfees);
        mSuspense=findViewById(R.id.suspensee);
        mProfitR=findViewById(R.id.profitres);

        c1=findViewById(R.id.balancesheet);
        c2=findViewById(R.id.profitloss);
        c3=findViewById(R.id.trialbalance);
        c4=findViewById(R.id.bankaccounts);
        c5=findViewById(R.id.masterdailyreport);
        c6=findViewById(R.id.masteroverall);
        c7=findViewById(R.id.directorsacc);
        c8=findViewById(R.id.journals);

        mD= FirebaseDatabase.getInstance().getReference().child("Accounts");
        mDatabse= FirebaseDatabase.getInstance().getReference().child("Employees");
        mD1=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD2=FirebaseDatabase.getInstance().getReference().child("Mpesa").child("Records").child("general");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("masterfinance");

        asat.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,bankact.class);
                startActivity(next);
            }
        });

        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,dateselector5.class);
                startActivity(next);
            }
        });

        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,monthselector3.class);
                startActivity(next);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,balancesheet.class);
                startActivity(next);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,trialbalance.class);
                startActivity(next);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,profitloss.class);
                startActivity(next);
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,directorhome.class);
                startActivity(next);
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(accountinghome.this,jounalentries.class);
                startActivity(next);
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Bank");
                startActivity(home);
            }
        });

        memfund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Memberfund");
                startActivity(home);
            }
        });

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Loan");
                startActivity(home);
            }
        });

        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Advance");
                startActivity(home);
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Income");
                startActivity(home);
            }
        });

        petty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Petty");
                startActivity(home);
            }
        });

        unhanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Unhanded");
                startActivity(home);
            }
        });

        feesfines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "FeesFines");
                startActivity(home);
            }
        });

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Expenses");
                startActivity(home);
            }
        });

        mschfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Schoolfees");
                startActivity(home);
            }
        });

        mSuspense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "Suspense");
                startActivity(home);
            }
        });
        mProfitR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(accountinghome.this, individualaccount.class);
                home.putExtra("option", "ProfitRes");
                startActivity(home);
            }
        });

        mD1.child("Bank").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalcl=0;
                totalcp=0;
                l2=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("withdraw").exists()&&snapshot.child("deposit").exists()){
                            double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                            totalcl=totalcl+cl;

                            double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                            totalcp=totalcp+c2;
                        }
                    }
                    if (totalcl>totalcp){
                        double vb=totalcl-totalcp;
                        bank.setText("(Kshs. "+df.format(vb)+")");
                        bank.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }else {
                        double vb=totalcp-totalcl;
                        bank.setText("Kshs. "+df.format(vb));
                        bank.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Membersfund").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cc1=0;
                cc2=0;
                cc3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                cc1=cc1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                cc2=cc2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }

                        cc3=cc2-cc1;
                        memfund.setText("Kshs "+df.format(cc3));
                        memfund.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    memfund.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Longtermloan").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v8=0;
                v9=0;
                v7=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                v8=v8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                v9=v9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }


                        v7=v8-v9;
                        loan.setText("Kshs "+df.format(v7));
                        loan.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    loan.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Income").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i1=0;
                i2=0;
                i3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                if (snapshot.child("debitac").getValue().toString().equals("Income")){
                                    i1=i1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Income")){
                                    i2=i2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }
                            }
                        }




                        i3=i2-i1;
                        income.setText("Kshs "+df.format(i3));
                        income.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    income.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Feesfines").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                f1=0;
                f2=0;
                f3=0;

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                if (snapshot.child("debitac").getValue().toString().equals("Feesfines")){
                                    f1=f1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Feesfines")){
                                    f2=f2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }
                            }
                        }

                        f3=f2-f1;
                        feesfines.setText("Kshs "+df.format(f3));
                        feesfines.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    feesfines.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Shorttermloans").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    m1=0;
                    m2=0;
                    m3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                m1=m1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                m2=m2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }


                        m3=m1-m2;
                        mpesa.setText("Kshs "+df.format(m3));
                        mpesa.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mpesa.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Petty").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p1=0;
                p2=0;
                p3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Petty")){
                                p1=p1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Petty")){
                                p2=p2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }



                        p3=p1-p2;
                        petty.setText("Kshs "+df.format(p3));
                        petty.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    petty.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Unhanded").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u1=0;
                u2=0;
                u3=3;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                u1=u1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                u2=u2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }



                        u3=u1-u2;
                        unhanded.setText("Kshs "+df.format(u3));
                        unhanded.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    unhanded.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Expenses").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v3=0;
                v4=0;
                v5=5;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").exists()){
                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                if (snapshot.child("debitac").getValue().toString().equals("Expense")){
                                    v3=v3+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }

                                if (!snapshot.child("debitac").getValue().toString().equals("Expense")){
                                    v4=v4+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                }
                            }
                        }

                        v5=v3-v4;
                        expenses.setText("Kshs "+df.format(v5));
                        expenses.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    expenses.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Schoolfees").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    sf1=0;
                    sf2=0;
                    sf3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Schoolfees")){
                                sf1=sf1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Schoolfees")){
                                sf2=sf2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }


                        sf3=sf2-sf1;
                        mschfees.setText("Kshs "+df.format(sf3));
                        mschfees.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mschfees.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("Suspense").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ss1=0;
                ss2=0;
                ss3=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                ss1=ss1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                ss2=ss2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }


                        ss3=ss1-ss2;
                        mSuspense.setText("Kshs "+df.format(ss3));
                        mSuspense.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mSuspense.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD1.child("ProfitReserves").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                manbal=0;
                secbal=0;
                totalcf=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").exists()){
                            if (snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                manbal=manbal+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (!snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                secbal=secbal+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }
                        }


                        totalcf=secbal-manbal;
                        mProfitR.setText("Kshs "+df.format(totalcf));
                        mProfitR.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mProfitR.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String years(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        return sdfs.format(date);
    }

    private String months(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        return sdfs.format(date);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
