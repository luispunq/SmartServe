package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class defaultprocessing extends AppCompatActivity {
    private TextView mMembername,mGroupName,mDate,mOfficer,mCurrentSaving,mUSaving
            ,mCLoans,mULoans,mDloan,mCAdv,mUAdv,mDAdv,mSapp,mLapp,mAapp;
    private CardView card1,card2,card3;
    private Button mDone;
    private DatabaseReference mD,mD1,mD2,accounting;
    private int s=0,l=0,a=0;
    private String key,uid,grpkey,particulars,cusavings,culoan,cuadv,meeetid;
    private double csaving=0,cloan=0,cadv=0,dsav=0,dloan=0,dadv=0,usav=0,uloan=0,uadv=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaultprocessing);
        key=getIntent().getExtras().getString("key");
        mMembername=findViewById(R.id.membername);
        mGroupName=findViewById(R.id.membergroup);
        mDate=findViewById(R.id.defaultdate);
        mOfficer=findViewById(R.id.defaultofficer);
        mCurrentSaving=findViewById(R.id.membertotalsavings);
        mUSaving=findViewById(R.id.membertotalsavings2);
        mCLoans=findViewById(R.id.membertotalloan);
        mULoans=findViewById(R.id.memberloanupdate);
        mDloan=findViewById(R.id.loandefaultapplied);
        mLapp=findViewById(R.id.title88);
        mSapp=findViewById(R.id.title4);
        mAapp=findViewById(R.id.loansde);
        mCAdv=findViewById(R.id.membertotalloanadv);
        mUAdv=findViewById(R.id.memberloanamountadv);
        mDAdv=findViewById(R.id.advdefaultapplied);
        card1=findViewById(R.id.cardsaves);
        card2=findViewById(R.id.cashsummary);
        card3=findViewById(R.id.advancesumm);
        mDone=findViewById(R.id.savedefaults);

        mD= FirebaseDatabase.getInstance().getReference().child("defaults").child("requests").child(key);
        mD1=FirebaseDatabase.getInstance().getReference().child("defaults");
        mD2=FirebaseDatabase.getInstance().getReference().child("members");
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");

        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=dataSnapshot.child("memberid").getValue().toString();
                mDate.setText(dataSnapshot.child("date").getValue().toString());
                grpkey=dataSnapshot.child("groupid").getValue().toString();
                mOfficer.setText(dataSnapshot.child("fac").getValue().toString());
                meeetid=dataSnapshot.child("meetid").getValue().toString();

                particulars=dataSnapshot.child("particulars").getValue().toString();
                if (particulars.contains("savings")){
                    s=1;
                    card1.setVisibility(View.VISIBLE);
                    mSapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            applysavings();
                        }
                    });
                }
                if (particulars.contains("advance")){
                    a=1;
                    card3.setVisibility(View.VISIBLE);
                    mAapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            applyadvance();
                        }
                    });
                }
                if (particulars.contains("loan")){
                    l=1;
                    card2.setVisibility(View.VISIBLE);
                    mLapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            applyloan();
                        }
                    });
                }

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMembername.setText(dataSnapshot.child("details").child("name").getValue().toString());
                        mGroupName.setText(dataSnapshot.child("details").child("group").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cusavings=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                        csaving=Double.parseDouble(cusavings);
                        mCurrentSaving.setText("Kshs. "+cusavings);
                        usav=csaving-100;
                        dsav=100;
                        mUSaving.setText("Kshs. "+String.valueOf(usav));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        culoan=dataSnapshot.child("loans").child("totalloan").getValue().toString();
                        cloan=Double.parseDouble(culoan);
                        mCLoans.setText("Kshs. "+culoan);
                        uloan=cloan+(cloan*0.06);
                        dloan=cloan*0.06;
                        mULoans.setText("Kshs. "+String.valueOf(uloan));
                        mDloan.setText("Kshs. "+String.valueOf(dloan));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mD2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cuadv=dataSnapshot.child("advances").child("currentadvance").getValue().toString();
                        cadv=Double.parseDouble(cuadv);
                        mCAdv.setText("Kshs. "+cuadv);
                        uadv=cadv+(cadv*0.1)+10;
                        dadv=(cadv*0.1)+10;
                        mUAdv.setText("Kshs. "+String.valueOf(uadv));
                        mDAdv.setText("Kshs. "+String.valueOf(dadv));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ree=mD;
                        ree.child("status").setValue("applied");

                        Intent userwind = new Intent(defaultprocessing.this,defaultlist.class);
                        //userwind.putExtra("key",grpkey);
                        startActivity(userwind);
                        finish();
                        Toast.makeText(defaultprocessing.this,"Done!", Toast.LENGTH_LONG).show();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void applyadvance() {
        DatabaseReference updateadv=mD2.child(uid).child("advances");
        updateadv.child("currentadvance").setValue(String.valueOf(uadv));

        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey).child(uid);

        //memstat.child("savings").setValue(sav.getText().toString());
        memstat.child("advance").setValue(String.valueOf(uadv)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAapp.setBackground(getResources().getDrawable(R.color.present));
                mAapp.setText("DONE");
            }
        });
        DatabaseReference inc1=accounting.child("Income").child("Trans").child("all").push();
        inc1.child("name").setValue("Income");
        inc1.child("amount").setValue(String.valueOf(dadv));
        inc1.child("type").setValue("red");
        inc1.child("meet").setValue(meeetid);
        inc1.child("group").setValue(grpkey);
        inc1.child("debitac").setValue("BankCash");
        inc1.child("creditac").setValue("Income");
        inc1.child("description").setValue("Default on Advance given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1a=accounting.child("Shorttermloans").child("Trans").child("all").push();
        inc1a.child("name").setValue("Short Term Loan");
        inc1a.child("amount").setValue(String.valueOf(dadv));
        inc1a.child("type").setValue("red");
        inc1a.child("meet").setValue(meeetid);
        inc1a.child("group").setValue(grpkey);
        inc1a.child("debitac").setValue("Shorttermloans");
        inc1a.child("creditac").setValue("Income");
        inc1a.child("description").setValue("Default on Advance given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1a.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference inc1ad=accounting.child("BankCash").child("Trans").child("all").push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(String.valueOf(dadv));
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("BankCash");
        inc1ad.child("creditac").setValue("Income");
        inc1ad.child("description").setValue("Default on Advance given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        final DatabaseReference savingssupdate = mD2.child(uid).child("advances").child("advancesss").push();
        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        savingssupdate.child("advbf").setValue(cuadv);
        savingssupdate.child("paid").setValue("-"+String.valueOf(dadv));
        savingssupdate.child("bal").setValue(String.valueOf(uadv));
        savingssupdate.child("fac").setValue("Manager-Defaults");
    }

    private void applyloan() {
        DatabaseReference updateadv=mD2.child(uid).child("loans");
        updateadv.child("totalloan").setValue(String.valueOf(uloan));

        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey).child(uid);

        //memstat.child("savings").setValue(sav.getText().toString());
        //memstat.child("advance").setValue(String.valueOf(uadv));
        memstat.child("loans").setValue(String.valueOf(uloan)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mLapp.setBackground(getResources().getDrawable(R.color.present));
                mLapp.setText("DONE");
            }
        });
        DatabaseReference inc1=accounting.child("Income").child("Trans").child("all").push();
        inc1.child("name").setValue("Income");
        inc1.child("amount").setValue(String.valueOf(dloan));
        inc1.child("type").setValue("red");
        inc1.child("meet").setValue(meeetid);
        inc1.child("group").setValue(grpkey);
        inc1.child("debitac").setValue("BankCash");
        inc1.child("creditac").setValue("Income");
        inc1.child("description").setValue("Default on Loan given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1l=accounting.child("Longtermloan").child("Trans").child("all").push();
        inc1l.child("name").setValue("Loan");
        inc1l.child("amount").setValue(String.valueOf(dloan));
        inc1l.child("type").setValue("red");
        inc1l.child("meet").setValue(meeetid);
        inc1l.child("group").setValue(grpkey);
        inc1l.child("debitac").setValue("Longtermloan");
        inc1l.child("creditac").setValue("Income");
        inc1l.child("description").setValue("Default on Loan given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1l.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference inc1ad=accounting.child("BankCash").child("Trans").child("all").push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(String.valueOf(dloan));
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("BankCash");
        inc1ad.child("creditac").setValue("Income");
        inc1ad.child("description").setValue("Default on Loan given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        final DatabaseReference savingssupdate = mD2.child(uid).child("loans").child("loanss").push();
        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        savingssupdate.child("loanbf").setValue(culoan);
        savingssupdate.child("inst").setValue("-"+String.valueOf(dloan));
        savingssupdate.child("bal").setValue(String.valueOf(uloan));
        savingssupdate.child("fac").setValue("Manager-Defaults");
    }

    private void applysavings() {
        DatabaseReference updateadv=mD2.child(uid).child("savings");
        updateadv.child("totalsavings").setValue(String.valueOf(usav));

        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey).child(uid);

        memstat.child("savings").setValue(String.valueOf(usav)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mSapp.setBackground(getResources().getDrawable(R.color.present));
                mSapp.setText("DONE");
            }
        });
        DatabaseReference inc1=accounting.child("Income").child("Trans").child("all").push();
        inc1.child("name").setValue("Income");
        inc1.child("amount").setValue(String.valueOf(dsav));
        inc1.child("type").setValue("red");
        inc1.child("meet").setValue(meeetid);
        inc1.child("group").setValue(grpkey);
        inc1.child("debitac").setValue("BankCash");
        inc1.child("creditac").setValue("Income");
        inc1.child("description").setValue("Default on Savings given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1s=accounting.child("Membersfund").child("Trans").child("all").push();
        inc1s.child("name").setValue("LSF");
        inc1s.child("amount").setValue(String.valueOf(dsav));
        inc1s.child("type").setValue("red");
        inc1s.child("meet").setValue(meeetid);
        inc1s.child("group").setValue(grpkey);
        inc1s.child("debitac").setValue("Membersfund");
        inc1s.child("creditac").setValue("Income");
        inc1s.child("description").setValue("Default on Savings given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1s.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference inc1ad=accounting.child("BankCash").child("Trans").child("all").push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(String.valueOf(dsav));
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("BankCash");
        inc1ad.child("creditac").setValue("Income");
        inc1ad.child("description").setValue("Default on Savings given to "+mMembername.getText().toString()+" in group "+mGroupName.getText().toString());
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        final DatabaseReference savingssupdate = mD2.child(uid).child("savings").child("savings").push();
        savingssupdate.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        savingssupdate.child("details").setValue("Default Penalty");
        savingssupdate.child("save").setValue("0");
        savingssupdate.child("with").setValue(String.valueOf(dsav));
        savingssupdate.child("bal").setValue(String.valueOf(usav));
        savingssupdate.child("fac").setValue("Manager");
    }
}
