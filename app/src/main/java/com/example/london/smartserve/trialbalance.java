package com.example.london.smartserve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class trialbalance extends AppCompatActivity {
    private DatabaseReference databaseReference;

    private TextView mCashBank,mMpesa,mPettyCash,mUnhanded,mLoans,mAdvances,mSavings,mSchoolfees,mRiskFund
            ,mInterestInc,mFeesIncome,mSalaries,mRent,mTransport,mOfficeExp,mProffesionalFees,
    mOtherExpenses,mProfits,mDirector,mSuspense,mShareCap,mBankLoans,mBankLoans2,mTotalDr,mTotalCr;
    private double cb1,cb2,mp1,mp2,pt1,pt2,mu1,mu2,l1,l2,a1,a2,s1,s2,sf1,sf2,rf1,rf2
            ,in1,in2,f1,f2,sl1,sl2,r1,r2,tr1,tr2,of1,of2,pr1,pr2,pr3,dir1,dir2,
    oth1,oth2,p1,p2,ss1,ss2,sc1,sc2,bl1,bl2,totdr,totcr;
    private double cashdr=0,mpesadr=0,pettydr=0,unhadr=0,loandr=0,advandr=0,savingscr=0,savingsdr=0,schoolfeescr=0,riskdr
            ,intrcr=0,feescr=0,salodr=0,rentdr,transidr=0,officedr=0,profedr=0,
            otherexdr=0,profitdr=0,profitcr=0,directordr=0,directorcr=0,suspensedr=0,sharedr=0,sharecr=0,bankloansdr,bankloanscr;


    private String month=new SimpleDateFormat("MMM").format(new Date()),year=new SimpleDateFormat("yyyy").format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trialbalance);

        mCashBank=findViewById(R.id.cashdr);
        mMpesa=findViewById(R.id.mpesadr);
        mPettyCash=findViewById(R.id.pettydr);
        mUnhanded=findViewById(R.id.unhandeddr);

        mLoans=findViewById(R.id.loansgvdr);

        mAdvances=findViewById(R.id.advgvdr);

        mSavings=findViewById(R.id.depocr);
        mSchoolfees=findViewById(R.id.sccr);
        mRiskFund=findViewById(R.id.withdcr);

        mInterestInc=findViewById(R.id.intrcr);
        mFeesIncome=findViewById(R.id.feescr);

        mSalaries=findViewById(R.id.salodr);
        mRent=findViewById(R.id.rentdr);
        mTransport=findViewById(R.id.purchdr);
        mOfficeExp=findViewById(R.id.billsdr);
        mProffesionalFees=findViewById(R.id.insudr);
        mOtherExpenses=findViewById(R.id.addr);

        mProfits=findViewById(R.id.profitcr);
        mDirector=findViewById(R.id.dirdr);
        mSuspense=findViewById(R.id.suspensedr);
        mShareCap=findViewById(R.id.sharecapcr);
        mBankLoans=findViewById(R.id.bankloansdr);
        mBankLoans2=findViewById(R.id.bankloanscr);

        mTotalCr=findViewById(R.id.totcr);
        mTotalDr=findViewById(R.id.totdr);


        databaseReference= FirebaseDatabase.getInstance().getReference().child("Accounting");

        //cashbank

        databaseReference.child("Bank").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cb1=0;
                cb2=0;
                double v2=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        double cl=Double.parseDouble(snapshot.child("withdraw").getValue().toString());
                        cb1=cb1+cl;

                        double c2=Double.parseDouble(snapshot.child("deposit").getValue().toString());
                        cb2=cb2+c2;

                    }

                    if (cb1>cb2){
                        Log.e("path","1");
                        v2=-(cb1-cb2);
                        cashdr=v2;
                        Log.e("path",String.valueOf(v2));
                        mCashBank.setText("(Kshs. "+String.format("%1$,.2f",v2)+")");
                    }else {
                        Log.e("path","2");
                        v2=cb2-cb1;
                        cashdr=v2;
                        mCashBank.setText("Kshs. "+String.format("%1$,.2f",v2));
                    }
                }


                //mpesa

                databaseReference.child("Mpesa").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mp1=0;
                        mp2=0;
                        double v2=0;

                        if (dataSnapshot.hasChildren()){
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.child("debitac").exists()&&snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                                mp1=mp1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            if (snapshot.child("debitac").exists()&&!snapshot.child("debitac").getValue().toString().equals("Mpesa")){
                                mp2=mp2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                            }

                            v2=mp1-mp2;
                            mpesadr=v2;
                            mMpesa.setText("Kshs. "+String.format("%1$,.2f",v2));
                        }
                        }else {
                            mMpesa.setText("Kshs. 0");
                        }

                        //petty

                        databaseReference.child("Petty").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                pt1=0;
                                pt2=0;

                                double v2=0;
                                if (dataSnapshot.hasChildren()){
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        if (snapshot.child("debitac").exists()){
                                            if (snapshot.child("debitac").getValue().toString().equals("Petty")){
                                                pt1=pt1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                            }

                                            if (!snapshot.child("debitac").getValue().toString().equals("Petty")){
                                                pt2=pt2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                            }
                                        }

                                        v2=pt1-pt2;
                                        pettydr=v2;
                                        mPettyCash.setText("Kshs "+String.format("%1$,.2f",v2));

                                    }


                                }else {
                                    mPettyCash.setText("Kshs 0");
                                }

                                //unhanded

                                databaseReference.child("Unhanded").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mu1=0;
                                        mu2=0;

                                        double v2=0;
                                        if (dataSnapshot.hasChildren()){
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                if (snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                                    mu1=mu1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                }

                                                if (!snapshot.child("debitac").getValue().toString().equals("Unhanded")){
                                                    mu2=mu2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                }

                                                v2=mu1-mu2;
                                                unhadr=v2;
                                                mUnhanded.setText("Kshs "+String.format("%1$,.2f",v2));
                                            }

                                        }else {
                                            mUnhanded.setText("Kshs 0");
                                        }

                                        //loans

                                        databaseReference.child("Longtermloan").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                l1=0;
                                                l2=0;
                                                double v2=0;
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    if (snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                                        l1=l1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                    }
                                                    if (!snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                                                        l2=l2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                    }

                                                    v2=l1-l2;
                                                    loandr=v2;

                                                    mLoans.setText("Kshs. "+String.format("%1$,.2f",v2));
                                                }

                                                //advances

                                                databaseReference.child("Shorttermloans").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        a1=0;
                                                        a2=0;

                                                        double v2=0;
                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                            if (snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                                                a1=a1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                            }

                                                            if (!snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                                                                a2=a2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                            }

                                                            v2=a1-a2;
                                                            advandr=v2;

                                                            mAdvances.setText("Kshs. "+String.format("%1$,.2f",v2));
                                                        }

                                                        //lsf

                                                        databaseReference.child("Membersfund").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                s1=0;
                                                                s2=0;
                                                                double v2=0;
                                                                if (dataSnapshot.hasChildren()){
                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                                        if (snapshot.child("debitac").exists()){
                                                                            if (snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                                                                s1=s1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                            }

                                                                            if (!snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                                                                                s2=s2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                            }
                                                                        }

                                                                        v2=s2-s1;
                                                                        savingscr=v2;
                                                                        mSavings.setText("Kshs. "+String.format("%1$,.2f",v2));
                                                                    }

                                                                }else {
                                                                    mSavings.setText("Kshs. 0");
                                                                }

                                                                //schoolfees

                                                                databaseReference.child("Schoolfees").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        sf1=0;
                                                                        sf1=0;
                                                                        double v2=0;
                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                            if (snapshot.child("creditac").getValue().toString().equals("Schoolfees")){
                                                                                String am=snapshot.child("amount").getValue().toString();
                                                                                double amm=Double.parseDouble(am);
                                                                                sf1=sf1+amm;
                                                                                v2=sf1;

                                                                                schoolfeescr=v2;
                                                                                mSchoolfees.setText("Kshs. "+String.format("%1$,.2f",v2));
                                                                            }
                                                                        }

                                                                        //riskfund

                                                                        databaseReference.child("Riskfund").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                rf1=0;
                                                                                rf2=0;
                                                                                double v2=0;
                                                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                    if (snapshot.child("amount").exists()){
                                                                                        if (snapshot.child("creditac").getValue().toString().equals("Riskfund")){
                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                            double amm=Double.parseDouble(am);
                                                                                            rf1=rf1+amm;
                                                                                            v2=rf1;

                                                                                            riskdr=v2;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                mRiskFund.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                //interest income

                                                                                databaseReference.child("Income").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        in1=0;
                                                                                        in2=0;
                                                                                        double v2=0;
                                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                String am=snapshot.child("amount").getValue().toString();
                                                                                                double amm=Double.parseDouble(am);
                                                                                                in1=in1+amm;
                                                                                                v2=in1;
                                                                                                intrcr=v2;
                                                                                            }
                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                String am=snapshot.child("amount").getValue().toString();
                                                                                                double amm=Double.parseDouble(am);
                                                                                                profitdr=profitdr+amm;
                                                                                            }
                                                                                        }
                                                                                        mInterestInc.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                        //feesincome
                                                                                        databaseReference.child("Feesfines").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                f1=0;
                                                                                                f2=0;
                                                                                                double v2=0;
                                                                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                    Log.e("key",snapshot.getKey());
                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                        String am=snapshot.child("amount").getValue().toString();
                                                                                                        double amm=Double.parseDouble(am);
                                                                                                        f1=f1+amm;
                                                                                                        v2=f1;
                                                                                                        feescr=v2;
                                                                                                    }
                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                        String am=snapshot.child("amount").getValue().toString();
                                                                                                        double amm=Double.parseDouble(am);
                                                                                                        profitdr=profitdr+amm;
                                                                                                    }
                                                                                                }
                                                                                                mFeesIncome.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                //expenses

                                                                                                //salo

                                                                                                databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        sl1=0;
                                                                                                        double v2=0;
                                                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                if (snapshot.child("name").getValue().toString().equals("Salaries")){
                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                    sl1=sl1+amm;
                                                                                                                    v2=sl1;
                                                                                                                    salodr=v2;
                                                                                                                }
                                                                                                            }
                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                if (snapshot.child("name").getValue().toString().equals("Salaries")){
                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                    profitcr=profitcr+amm;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        mSalaries.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                        //rent

                                                                                                        databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                r1=0;
                                                                                                                double v2=0;
                                                                                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Rent")){
                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                            r1=r1+amm;
                                                                                                                            v2=r1;
                                                                                                                            rentdr=v2;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Rent")){
                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                            profitcr=profitcr+amm;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                mRent.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                                //transi

                                                                                                                databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        tr1=0;
                                                                                                                        double v2=0;
                                                                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                                if (snapshot.child("name").getValue().toString().contains("Transport and Subsistence")){
                                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                                    tr1=tr1+amm;
                                                                                                                                    v2=tr1;

                                                                                                                                    transidr=v2;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                                if (snapshot.child("name").getValue().toString().equals("Transport and Subsistence")){
                                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                                    profitcr=profitcr+amm;
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        mTransport.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                                        //officeexp

                                                                                                                        databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                of1=0;
                                                                                                                                double v2=0;
                                                                                                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Office Expenses")){
                                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                                            of1=of1+amm;
                                                                                                                                            v2=of1;
                                                                                                                                            officedr=v2;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Office Expenses")){
                                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                                            profitcr=profitcr+amm;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                mOfficeExp.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                                                //proff

                                                                                                                                databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                        pr1=0;
                                                                                                                                        double v2=0;
                                                                                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                                                if (snapshot.child("name").getValue().toString().equals("Professional Fees")){
                                                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                                                    pr1=pr1+amm;
                                                                                                                                                    v2=pr1;
                                                                                                                                                    profedr=v2;
                                                                                                                                                }
                                                                                                                                            }

                                                                                                                                            if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                                                if (snapshot.child("name").getValue().toString().equals("Professional Fees")){
                                                                                                                                                    String am=snapshot.child("amount").getValue().toString();
                                                                                                                                                    double amm=Double.parseDouble(am);
                                                                                                                                                    profitcr=profitcr+amm;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        mProffesionalFees.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                                                        //other exp

                                                                                                                                        databaseReference.child("Expenses").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                oth1=0;
                                                                                                                                                double v2=0;
                                                                                                                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&months(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(month)){
                                                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Other")){
                                                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                                                            oth1=oth1+amm;
                                                                                                                                                            v2=oth1;
                                                                                                                                                            otherexdr=v2;
                                                                                                                                                        }
                                                                                                                                                    }

                                                                                                                                                    if (years(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(year)&&thismonth(Long.parseLong(snapshot.child("timestamp").getValue().toString())).equals(getLastDate())){
                                                                                                                                                        if (snapshot.child("name").getValue().toString().equals("Other")){
                                                                                                                                                            String am=snapshot.child("amount").getValue().toString();
                                                                                                                                                            double amm=Double.parseDouble(am);
                                                                                                                                                            profitcr=profitcr+amm;
                                                                                                                                                        }
                                                                                                                                                    }


                                                                                                                                                }
                                                                                                                                                mOtherExpenses.setText("Kshs. "+String.format("%1$,.2f",v2));

                                                                                                                                                //director

                                                                                                                                                databaseReference.child("Director").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                        dir1=0;
                                                                                                                                                        dir2=0;

                                                                                                                                                        double v2=0;
                                                                                                                                                        if (dataSnapshot.hasChildren()){
                                                                                                                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                                                                                                                                if (snapshot.child("debitac").getValue().toString().equals("Director")){
                                                                                                                                                                    dir1=dir1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                }

                                                                                                                                                                if (!snapshot.child("debitac").getValue().toString().equals("Director")){
                                                                                                                                                                    dir2=dir2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                }

                                                                                                                                                                v2=dir1-dir2;
                                                                                                                                                                directordr=v2;
                                                                                                                                                                mDirector.setText("Kshs "+String.format("%1$,.2f",v2));
                                                                                                                                                            }

                                                                                                                                                        }else {
                                                                                                                                                            mDirector.setText("Kshs 0");
                                                                                                                                                        }

                                                                                                                                                        //suspense

                                                                                                                                                        databaseReference.child("Suspense").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                ss1=0;
                                                                                                                                                                ss2=0;

                                                                                                                                                                double v2=0;
                                                                                                                                                                if (dataSnapshot.hasChildren()){
                                                                                                                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                                                                                                                                        if (snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                                                                                                                                                            ss1=ss1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                        }

                                                                                                                                                                        if (!snapshot.child("debitac").getValue().toString().equals("Suspense")){
                                                                                                                                                                            ss2=ss2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                        }

                                                                                                                                                                        v2=ss1-ss2;
                                                                                                                                                                        suspensedr=v2;
                                                                                                                                                                        mSuspense.setText("Kshs "+String.format("%1$,.2f",v2));
                                                                                                                                                                    }

                                                                                                                                                                    //sharecap

                                                                                                                                                                }else {
                                                                                                                                                                    mSuspense.setText("Kshs 0");
                                                                                                                                                                }

                                                                                                                                                                databaseReference.child("ShareCapital").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                        sc1=0;
                                                                                                                                                                        sc2=0;

                                                                                                                                                                        double v2=0;
                                                                                                                                                                        if (dataSnapshot.hasChildren()){
                                                                                                                                                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                                                                                                                                                if (snapshot.child("debitac").getValue().toString().equals("ShareCapital")){
                                                                                                                                                                                    sc1=sc1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                                }

                                                                                                                                                                                if (!snapshot.child("debitac").getValue().toString().equals("ShareCapital")){
                                                                                                                                                                                    sc2=sc2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                                }

                                                                                                                                                                                v2=sc2-sc1;
                                                                                                                                                                                sharedr=v2;
                                                                                                                                                                                mShareCap.setText("Kshs "+String.format("%1$,.2f",v2));
                                                                                                                                                                            }

                                                                                                                                                                            //profitreserves


                                                                                                                                                                        }else {
                                                                                                                                                                            mShareCap.setText("Kshs 0");
                                                                                                                                                                        }

                                                                                                                                                                        databaseReference.child("ProfitReserves").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                bl1=0;
                                                                                                                                                                                bl2=0;
                                                                                                                                                                                pr3=0;

                                                                                                                                                                                double v2=0;
                                                                                                                                                                                if (dataSnapshot.exists()){
                                                                                                                                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                                                                                                                                                                        if (snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                                                                                                                                                                            bl1=bl1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                                            v2=bl1;
                                                                                                                                                                                            bankloansdr=v2;
                                                                                                                                                                                            //mBankLoans.setText("Kshs "+String.format("%1$,.2f",v2));
                                                                                                                                                                                        }

                                                                                                                                                                                        if (!snapshot.child("debitac").getValue().toString().equals("ProfitReserves")){
                                                                                                                                                                                            bl2=bl2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                                                                                                                                                                            v2=bl2;
                                                                                                                                                                                            bankloanscr=v2;
                                                                                                                                                                                            mBankLoans2.setText("Kshs "+String.format("%1$,.2f",v2));
                                                                                                                                                                                        }

                                                                                                                                                                                    }

                                                                                                                                                                                }else {
                                                                                                                                                                                    mBankLoans.setText("Kshs. 0");
                                                                                                                                                                                    mBankLoans2.setText("Kshs. 0");
                                                                                                                                                                                }

                                                                                                                                                                                p1=profitdr-profitcr;

                                                                                                                                                                                mProfits.setText("Kshs "+String.format("%1$,.2f",p1));

                                                                                                                                                                                Log.e("cash",String.valueOf(cashdr));
                                                                                                                                                                                Log.e("petty",String.valueOf(pettydr));


                                                                                                                                                                                mTotalDr.setText(String.format("%1$,.2f",cashdr+mpesadr+pettydr+unhadr+loandr+advandr+salodr+rentdr+transidr+officedr+profedr+otherexdr+suspensedr+directordr+bankloansdr));
                                                                                                                                                                                mTotalCr.setText(String.format("%1$,.2f",bankloanscr+feescr+intrcr+riskdr+savingscr+sharedr+p1));
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

    private String years(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        return sdfs.format(date);
    }

    private String months(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        return sdfs.format(date);
    }

    private String getLastDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.MONTH, -1); //Removing one

        return sdf.format(c.getTime());
    }

    private String thismonth(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.UK);
        return sdf.format(date);
    }
}
