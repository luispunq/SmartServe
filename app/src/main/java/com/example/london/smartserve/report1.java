package com.example.london.smartserve;

import android.app.admin.DeviceAdminInfo;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class report1 extends AppCompatActivity {
    private TextView lsf,advpaid,advintr,loanrepa,regfee,fines,totcoll,
                        advbf,advgiv,intonadv,advtot,adpaid,advcf,
                            loansbf,loansgvn,loantot,lopaid,loancf,advintrest,intronloans;
    private ImageView mGrpImage;
    private TextView grpname,cashfrmoffice;
    private String grpkey=null,meeetid=null,cloan=null;
    private DatabaseReference mDatabase,mD,mD1,mD2,mD3,mD4;
    private double totamount=0,lsfcm=0,loaninstcm=0,advpaycm=0,mpesapay=0,intadvpaidamnt=0,intadvbf=0,total=0,advintrestcm=0,finescm=0,advinttot=0,cashtotal=0;
    private String adamount=null,mpesapays=null,loantots=null,amount=null,lsfamount=null,loaninstamount=null,advpayamount=null,advpayintrest=null,fine=null;
    private String st4=null,st3=null;
    private FloatingActionButton fab;
    private String to;
    private double advncf;
    private String casht=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report1);
        grpkey=getIntent().getExtras().getString("key");
        cashfrmoffice=findViewById(R.id.moneygiven);
        lsf=findViewById(R.id.dd1);
        advpaid=findViewById(R.id.dd2);
        advintr=findViewById(R.id.dd3);
        loanrepa=findViewById(R.id.dd4);
        regfee=findViewById(R.id.dd5);
        fines=findViewById(R.id.dd6);
        totcoll=findViewById(R.id.dd7);
        advbf=findViewById(R.id.dd21);
        advgiv=findViewById(R.id.dd22);
        advintrest=findViewById(R.id.dd222);
        intonadv=findViewById(R.id.dd23);
        advtot=findViewById(R.id.dd24);
        adpaid=findViewById(R.id.dd25);
        advcf=findViewById(R.id.dd26);
        loansbf=findViewById(R.id.dd31);
        loansgvn=findViewById(R.id.dd32);
        intronloans=findViewById(R.id.dd321);
        loantot=findViewById(R.id.dd33);
        lopaid=findViewById(R.id.dd34);
        loancf=findViewById(R.id.dd35);
        grpname=findViewById(R.id.sum1grpname);
        mGrpImage=findViewById(R.id.sum1grpprofpic);
        fab=findViewById(R.id.floatingActionButton4);

        mD= FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        mD1= FirebaseDatabase.getInstance().getReference().child("finances").child(grpkey);



        mD.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String grname=(String)dataSnapshot.child("groupName").getValue();
                String grimage=(String)dataSnapshot.child("profileImage").getValue();
                grpname.setText(grname);
                Picasso.with(report1.this).load(grimage).into(mGrpImage);

                mD.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("meetid").exists()){
                            meeetid=(String)dataSnapshot.child("meetid").getValue();
                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent backtosignin =new Intent(report1.this,perfomanceanalysis.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("gkey", grpkey);
                                    extras.putString("meet", meeetid);
                                    extras.putString("date", new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    backtosignin.putExtras(extras);
                                    startActivity(backtosignin);
                                }
                            });
                            mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("cashfromoffice")){
                                    cashfrmoffice.setText("Kshs. "+dataSnapshot.child("cashfromoffice").getValue().toString());}
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mD.child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        if (dataSnapshot.hasChild("advanceinterest")&&dataSnapshot.hasChild("fine")){
                                            amount=(String)snapshot.child("totalamount").getValue();
                                            lsfamount=(String)snapshot.child("lsf").getValue();
                                            loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                                            advpayamount=(String)snapshot.child("advancepayment").getValue();
                                            advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                                            mpesapays=(String)snapshot.child("Mpesa").getValue();
                                            fine=(String)snapshot.child("fine").getValue();
                                        }else {
                                            amount=(String)snapshot.child("totalamount").getValue();
                                            lsfamount=(String)snapshot.child("lsf").getValue();
                                            loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                                            advpayamount=(String)snapshot.child("advancepayment").getValue();
                                            mpesapays=(String)snapshot.child("Mpesa").getValue();
                                            advpayintrest="0";
                                            fine="0";
                                        }

                                        double val1=Double.parseDouble(amount);
                                        double val2=Double.parseDouble(lsfamount);
                                        double val3=Double.parseDouble(loaninstamount);
                                        double val4=Double.parseDouble(advpayamount);
                                        double val5=Double.parseDouble(advpayintrest);
                                        double val6=Double.parseDouble(fine);
                                        double val7=Double.parseDouble(mpesapays);

                                        mpesapay=mpesapay+val7;
                                        advintrestcm=advintrestcm+val5;
                                        finescm=finescm+val6;
                                        totamount=totamount+val1;
                                        if (val2>0){
                                            lsfcm=lsfcm+val2;
                                        }

                                        loaninstcm=loaninstcm+val3;
                                        advpaycm=advpaycm+val4;
                                        String st1=String.valueOf(totamount);
                                        String st2=String.valueOf(lsfcm);
                                        st3=String.valueOf(loaninstcm);
                                        st4=String.valueOf(advpaycm);
                                        lsf.setText("Kshs. "+st2);

                                        advpaid.setText("Kshs. "+st4);
                                        loanrepa.setText("Kshs. "+st3);
                                        totcoll.setText("Kshs. "+(Double.parseDouble(st1)-mpesapay));


                                        mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                adamount=(String)dataSnapshot.child("groupadvancesbf").getValue();
                                                intadvbf=Double.parseDouble(adamount);
                                                advbf.setText("Kshs. "+adamount);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mD.child("meetings").child(meeetid).child("advances").child("c2b").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    if (snapshot.child("advancegiven").exists()){
                                                        String advpaidamnt=(String)snapshot.child("advancegiven").getValue();
                                                        double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                        intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                        double tots=intadvpaidamnt+intadvbf;
                                                        double advncf=tots-advpaycm;
                                                        advtot.setText("Kshs. "+String.valueOf(tots));
                                                        adpaid.setText("Kshs. "+st4);
                                                        advcf.setText("Kshs. "+String.valueOf(advncf));
                                                    }else{
                                                        String advpaidamnt="0";
                                                        double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                        intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                        double tots=intadvpaidamnt+intadvbf;
                                                        double advncf=tots-advpaycm;
                                                        advtot.setText("Kshs. "+String.valueOf(tots));
                                                        adpaid.setText("Kshs. "+st4);
                                                        advcf.setText("Kshs. "+String.valueOf(advncf));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                cloan=(String)dataSnapshot.child("grouploansbf").getValue();
                                                loansbf.setText("Kshs. "+cloan);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mD.child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    String advpaidamnt=(String)snapshot.child("advancegiven").getValue();
                                                    double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                    double intrestonad=oldintadvpaidamnt-(oldintadvpaidamnt/1.1);
                                                    intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                    double tots=intadvpaidamnt+intadvbf;
                                                    advinttot=advinttot+intrestonad;
                                                    to=String.valueOf(tots);
                                                    advncf=tots-advpaycm;
                                                    advintrest.setText(String.valueOf(advinttot));
                                                    advgiv.setText("Kshs. "+String.format("%.2f",oldintadvpaidamnt));
                                                    advtot.setText("Kshs. "+String.valueOf(tots));
                                                    adpaid.setText("Kshs. "+st4);
                                                    advcf.setText("Kshs. "+String.valueOf(advncf));
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mD1.child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot.hasChildren()) {
                                                        String test = snapshot.child("status").getValue().toString();
                                                        if (test.equals("accepted")) {
                                                            loantots = (String) snapshot.child("amount").getValue();
                                                            casht = snapshot.child("amountcash").getValue().toString();
                                                            cashtotal = cashtotal + (Double.parseDouble(casht));
                                                            total = total + (Double.parseDouble(loantots));
                                                            intronloans.setText(String.valueOf(total-cashtotal));
                                                            double total2 = total + (Double.parseDouble(cloan));
                                                            String upsatekey = snapshot.getKey();
                                                            DatabaseReference updates = mD1.child("loans").child("approvals").child(upsatekey);
                                                            //updates.child("status").setValue("done");
                                                            loansgvn.setText("Kshs. "+String.valueOf(cashtotal));
                                                            loantot.setText("Kshs. "+String.valueOf(total2));
                                                            lopaid.setText("Kshs. " + st3);
                                                            double nettotal = total2 - (Double.parseDouble(st3));
                                                            loancf.setText("Kshs. " + String.valueOf(nettotal));
                                                        } else {
                                                            loantots = "0";
                                                            total = total + (Double.parseDouble(loantots));
                                                            double total2 = total + (Double.parseDouble(cloan));
                                                            String upsatekey = snapshot.getKey();
                                                            DatabaseReference updates = mD1.child("loans").child("approvals").child(upsatekey);
                                                            //updates.child("status").setValue("done");
                                                            loansgvn.setText("Kshs. " + String.valueOf(total));
                                                            loantot.setText("Kshs. " + String.valueOf(total2));
                                                            lopaid.setText("Kshs. " + st3);
                                                            double nettotal = total2 - (Double.parseDouble(st3));
                                                            loancf.setText("Kshs. " + String.valueOf(nettotal));
                                                        }
                                                    }else {
                                                        loantots = "0";
                                                        total = total + (Double.parseDouble(loantots));
                                                        double total2 = total + (Double.parseDouble(cloan));
                                                        loansgvn.setText("Kshs. " + String.valueOf(total));
                                                        loantot.setText("Kshs. " + String.valueOf(total2));
                                                        lopaid.setText("Kshs. " + st3);
                                                        double nettotal = total2 - (Double.parseDouble(st3));
                                                        loancf.setText("Kshs. " + String.valueOf(nettotal));
                                                    }
                                                }

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
                            }else{
                            mD.child("temp").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    meeetid=dataSnapshot.child("tempid").getValue().toString();
                                    mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            cashfrmoffice.setText("Kshs. "+dataSnapshot.child("cashfromoffice").getValue().toString());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mD.child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                if (dataSnapshot.hasChild("advanceinterest")&&dataSnapshot.hasChild("fine")){
                                                    amount=(String)snapshot.child("totalamount").getValue();
                                                    lsfamount=(String)snapshot.child("lsf").getValue();
                                                    loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                                                    advpayamount=(String)snapshot.child("advancepayment").getValue();
                                                    advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                                                    fine=(String)snapshot.child("fine").getValue();
                                                }else {
                                                    amount=(String)snapshot.child("totalamount").getValue();
                                                    lsfamount=(String)snapshot.child("lsf").getValue();
                                                    loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                                                    advpayamount=(String)snapshot.child("advancepayment").getValue();
                                                    advpayintrest="0";
                                                    fine="0";
                                                }

                                                double val1=Double.parseDouble(amount);
                                                double val2=Double.parseDouble(lsfamount);
                                                double val3=Double.parseDouble(loaninstamount);
                                                double val4=Double.parseDouble(advpayamount);
                                                double val5=Double.parseDouble(advpayintrest);
                                                double val6=Double.parseDouble(fine);

                                                advintrestcm=advintrestcm+val5;
                                                finescm=finescm+val6;
                                                totamount=totamount+val1;
                                                lsfcm=lsfcm+val2;
                                                loaninstcm=loaninstcm+val3;
                                                advpaycm=advpaycm+val4;
                                                String st1=String.valueOf(totamount);
                                                String st2=String.valueOf(lsfcm);
                                                st3=String.valueOf(loaninstcm);
                                                st4=String.valueOf(advpaycm);
                                                lsf.setText("Kshs. "+st2);
                                                advpaid.setText("Kshs. "+st4);
                                                loanrepa.setText("Kshs. "+st3);
                                                totcoll.setText("Kshs. "+st1);

                                                mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        adamount=(String)dataSnapshot.child("groupadvancesbf").getValue();
                                                        intadvbf=Double.parseDouble(adamount);
                                                        advbf.setText("Kshs. "+adamount);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                mD.child("meetings").child(meeetid).child("advances").child("c2b").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                            if (snapshot.child("advancegiven").exists()){
                                                                String advpaidamnt=(String)snapshot.child("advancegiven").getValue();
                                                                double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                                intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                                double tots=intadvpaidamnt+intadvbf;
                                                                double advncf=tots-advpaycm;
                                                                advgiv.setText("Kshs. "+String.valueOf(intadvpaidamnt));
                                                                advtot.setText("Kshs. "+String.valueOf(tots));
                                                                adpaid.setText("Kshs. "+st4);
                                                                advcf.setText("Kshs. "+String.valueOf(advncf));
                                                            }else{
                                                                String advpaidamnt="0";
                                                                double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                                intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                                double tots=intadvpaidamnt+intadvbf;
                                                                double advncf=tots-advpaycm;
                                                                advgiv.setText("Kshs. "+String.valueOf(intadvpaidamnt));
                                                                advtot.setText("Kshs. "+String.valueOf(tots));
                                                                adpaid.setText("Kshs. "+st4);
                                                                advcf.setText("Kshs. "+String.valueOf(advncf));
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        cloan=(String)dataSnapshot.child("grouploansbf").getValue();
                                                        loansbf.setText("Kshs. "+cloan);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                mD.child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                            if (snapshot.child("advancegiven").exists()){
                                                                String advpaidamnt=(String)snapshot.child("advancegiven").getValue();
                                                                double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                                double intrestonad=oldintadvpaidamnt-(oldintadvpaidamnt/1.1);

                                                                intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                                double tots=intadvpaidamnt+intadvbf;
                                                                advinttot=advinttot+intrestonad;
                                                                to=String.valueOf(tots);
                                                                advncf=tots-advpaycm;
                                                                advintrest.setText(String.valueOf(advinttot));
                                                                advgiv.setText("Kshs. "+String.valueOf(intadvpaidamnt/1.1));
                                                                advtot.setText("Kshs. "+String.valueOf(tots));
                                                                adpaid.setText("Kshs. "+st4);
                                                                advcf.setText("Kshs. "+String.valueOf(advncf));
                                                            }else{
                                                                String advpaidamnt="0";
                                                                double oldintadvpaidamnt=Double.parseDouble(advpaidamnt);
                                                                intadvpaidamnt=intadvpaidamnt+oldintadvpaidamnt;
                                                                double tots=intadvpaidamnt+intadvbf;
                                                                to=String.valueOf(tots);
                                                                advncf=tots-advpaycm;
                                                                advgiv.setText("Kshs. "+String.valueOf(intadvpaidamnt/1.1));
                                                                advtot.setText("Kshs. "+String.valueOf(tots));
                                                                adpaid.setText("Kshs. "+st4);
                                                                advcf.setText("Kshs. "+String.valueOf(advncf));
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                mD1.child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String test = snapshot.child("status").getValue().toString();
                                                            if (test.equals("done")) {

                                                                loantots = (String) snapshot.child("amount").getValue();
                                                                casht=snapshot.child("amountcash").getValue().toString();

                                                                cashtotal=cashtotal+(Double.parseDouble(casht));
                                                                total = total + (Double.parseDouble(loantots));
                                                                intronloans.setVisibility(View.GONE);//.setText(String.valueOf(total-cashtotal));
                                                                double total2 = total + (Double.parseDouble(cloan));
                                                                String upsatekey = snapshot.getKey();
                                                                DatabaseReference updates = mD1.child("loans").child("approvals").child(upsatekey);
                                                                updates.child("status").setValue("done");
                                                                loansgvn.setText("Kshs. "+String.valueOf(cashtotal));
                                                                loantot.setText("Kshs. "+String.valueOf(total2));
                                                                lopaid.setText("Kshs. "+st3);
                                                                double nettotal = total2 - (Double.parseDouble(st3));
                                                                loancf.setText("Kshs. "+String.valueOf(nettotal));
                                                            } else {
                                                                loantots = "0";
                                                                total = total + (Double.parseDouble(loantots));
                                                                double total2 = total + (Double.parseDouble(cloan));
                                                                String upsatekey = snapshot.getKey();
                                                                DatabaseReference updates = mD1.child("loans").child("approvals").child(upsatekey);
                                                                updates.child("status").setValue("done");
                                                                loansgvn.setText("Kshs. "+String.valueOf(total));
                                                                loantot.setText("Kshs. "+String.valueOf(total2));
                                                                lopaid.setText("Kshs. "+st3);
                                                                double nettotal = total2 - (Double.parseDouble(st3));
                                                                loancf.setText("Kshs. "+String.valueOf(nettotal));
                                                            }
                                                        }

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
