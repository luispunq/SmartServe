package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class schoolfeesreport extends AppCompatActivity {
    private TextView lsf,advpaid,advintr,loanrepa,regfee,fines,totcoll,
            advbf,advgiv,intonadv,advtot,adpaid,advcf,nets,
            loansbf,loansgvn,loantot,lopaid,loancf,insus,mcscfo,chand,adgv,lngv,withs,mpesas;
    private ImageView mGrpImage;
    private TextView grpname,cashfrmoffice,cashtooffice,fac,meedate,ven,advcontri,loancontri,feecontri,totalcontri;
    private String grpkey=null,meeetid=null,cloan=null;
    private DatabaseReference mDatabase,mDemployee;
    private double totamount=0,lsfcm=0,totaladvences=0,advpaycm=0,intadvpaidamnt=0,intadvbf=0,riskcm=0,mpesapay=0,total=0,wcommcm=0,advncf=0,nettotal=0,advintrestcm=0,loancashtot=0,
            finescm=0,advinttot=0,cashtotal=0,cashgivend=0,totalloanfee=0,totaladvfee=0,memfee=0,insupay=0;
    private String adamount=null,loantots=null,user=null,fkey=null,cashgiven=null,cashcollected=null,cashpaid=null,netcash=null;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab,fab2;
    private String st2=null,st4=null,st3=null,st1=null;
    private String to=null;
    private String st=null,date;
    private String oldcb=null;
    private String grname;
    private String lsfbf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolfeesreport);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        date = extras.getString("date");
        //date="Tue, Jan 15, '19";
        cashfrmoffice=findViewById(R.id.moneygiven);
        cashtooffice=findViewById(R.id.moneyto);
        lsf=findViewById(R.id.dd1);
        mAuth=FirebaseAuth.getInstance();
        //user=mAuth.getCurrentUser().getUid();
        fac=findViewById(R.id.facilitator);
        meedate=findViewById(R.id.dateheld);
        ven=findViewById(R.id.meetvenue);
        advpaid=findViewById(R.id.dd2);
        advintr=findViewById(R.id.dd6d);
        loanrepa=findViewById(R.id.dd4);
        regfee=findViewById(R.id.dd5);
        fines=findViewById(R.id.dd6);
        totcoll=findViewById(R.id.dd7);
        advbf=findViewById(R.id.dd21);
        advgiv=findViewById(R.id.dd22);
        intonadv=findViewById(R.id.dd23);
        advtot=findViewById(R.id.dd24);
        adpaid=findViewById(R.id.dd25);
        advcf=findViewById(R.id.dd26);
        loansbf=findViewById(R.id.dd31);
        loansgvn=findViewById(R.id.dd32);
        loantot=findViewById(R.id.dd33);
        lopaid=findViewById(R.id.dd34);
        loancf=findViewById(R.id.dd35);
        insus=findViewById(R.id.dd6e);
        mcscfo=findViewById(R.id.dd6f);
        chand=findViewById(R.id.dd7e);
        adgv=findViewById(R.id.dd7r);
        lngv=findViewById(R.id.dd7v);
        withs=findViewById(R.id.dd7h);
        mpesas=findViewById(R.id.dd7i);
        nets=findViewById(R.id.dd7j);
        totalcontri=findViewById(R.id.totalcontr);
        advcontri=findViewById(R.id.advcontr);
        loancontri=findViewById(R.id.loancontr);
        feecontri=findViewById(R.id.feeandfines);
        grpname=findViewById(R.id.sum1grpname);
        mGrpImage=findViewById(R.id.sum1grpprofpic);
        fab=findViewById(R.id.floatingActionButton2);
        fab2=findViewById(R.id.floatingActionButton5);
        mDemployee=FirebaseDatabase.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        mDatabase.child("details").child(grpkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grname=(String)dataSnapshot.child("groupName").getValue();
                String grimage=(String)dataSnapshot.child("profileImage").getValue();
                grpname.setText(grname);
                Picasso.with(schoolfeesreport.this).load(grimage).into(mGrpImage);

                mDatabase.child("details").child(grpkey).child("temp").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        meeetid=dataSnapshot.child("tempid").getValue().toString();
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent backtosignin =new Intent(schoolfeesreport.this,schoolfeesperfomanceanalysis.class);
                                Bundle extras = new Bundle();
                                extras.putString("gkey", grpkey);
                                extras.putString("meet", meeetid);
                                extras.putString("date", new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                backtosignin.putExtras(extras);
                                startActivity(backtosignin);
                            }
                        });
                        mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("reportflags").exists()){
                                    fab2.setVisibility(View.GONE);
                                    String trs=dataSnapshot.child("reportid").getValue().toString();
                                    mDatabase.child("reports").child(grpkey).child("schoolfees").child(trs).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("fines").exists()&&dataSnapshot.child("intonadv").exists()&&dataSnapshot.child("contributiontotal").exists()&&dataSnapshot.child("advcontribution").exists()&&dataSnapshot.child("loancontribution").exists()&&dataSnapshot.child("feesnfines").exists()){
                                                fac.setText(dataSnapshot.child("facilitator").getValue().toString());
                                                ven.setText(dataSnapshot.child("venue").getValue().toString());
                                                meedate.setText(dataSnapshot.child("meetdate").getValue().toString());
                                                fines.setText(dataSnapshot.child("fines").getValue().toString());
                                                intonadv.setText(dataSnapshot.child("intonadv").getValue().toString());
                                                advcontri.setText(dataSnapshot.child("advcontribution").getValue().toString());
                                                feecontri.setText(dataSnapshot.child("feesnfines").getValue().toString());
                                                totalcontri.setText(dataSnapshot.child("contributiontotal").getValue().toString());
                                                cashfrmoffice.setText("Kshs. "+dataSnapshot.child("cashfromoffice").getValue().toString());
                                                cashtooffice.setText("Kshs. "+dataSnapshot.child("cashtooffice").getValue().toString());
                                                lsf.setText("Kshs. "+dataSnapshot.child("totalLSF").getValue().toString());
                                                advpaid.setText("Kshs. "+dataSnapshot.child("totalAdvancesCollected").getValue().toString());
                                                totcoll.setText("Kshs. "+dataSnapshot.child("cashcollected").getValue().toString());
                                                advbf.setText("Kshs. "+dataSnapshot.child("totalAdvancesBroughtForward").getValue().toString());
                                                advgiv.setText("Kshs. "+dataSnapshot.child("totalAdvancesGiven").getValue().toString());
                                                adpaid.setText("Kshs. "+dataSnapshot.child("totalAdvancesCollected").getValue().toString());
                                                advcf.setText("Kshs. "+dataSnapshot.child("totalAdvancesCarriedForward").getValue().toString());
                                                if (dataSnapshot.child("grandtotal1").exists()&&dataSnapshot.child("grandtotal2").exists()){
                                                    advtot.setText("Kshs. "+dataSnapshot.child("grandtotal1").getValue().toString());
                                                    loantot.setText("Kshs. "+dataSnapshot.child("grandtotal2").getValue().toString());}else{
                                                    advtot.setText("Kshs. 0");
                                                    loantot.setText("Kshs. 0");
                                                }
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{
                                    mDatabase.child("details").child(grpkey).child("temp").child(date).addListenerForSingleValueEvent(new ValueEventListener()  {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("fieldid").exists()) {

                                                user=dataSnapshot.child("facid").getValue().toString();
                                                meeetid=dataSnapshot.child("tempid").getValue().toString();
                                                mDemployee.child("Employees").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        fkey=dataSnapshot.child("fieldid").getValue().toString();
                                                        final String nme=dataSnapshot.child("name").getValue().toString();
                                                        fac.setText(nme);
                                                        mDatabase.child("details").child(grpkey).child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                cashfrmoffice.setText("Kshs. 0");
                                                                mcscfo.setText("Kshs. 0");
                                                                meedate.setText(dataSnapshot.child("date").getValue().toString());
                                                                ven.setText(dataSnapshot.child("venue").getValue().toString());
                                                                mDatabase.child("details").child(grpkey).child("schoolfees").child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                            String amount=(String)snapshot.child("totalamount").getValue();
                                                                            String lsfamount=(String)snapshot.child("lsf").getValue();
                                                                            String advpayamount=(String)snapshot.child("advancepayment").getValue();
                                                                            String advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                                                                            String mpesapays=(String)snapshot.child("Mpesa").getValue();
                                                                            String wcomm=(String)snapshot.child("wcomm").getValue();

                                                                            double val1=Double.parseDouble(amount);
                                                                            double val2=Double.parseDouble(lsfamount);
                                                                            double val4=Double.parseDouble(advpayamount);
                                                                            double val5=Double.parseDouble(advpayintrest);
                                                                            double val8=Double.parseDouble(wcomm);
                                                                            double val9=Double.parseDouble(mpesapays);

                                                                            mpesapay=mpesapay+val9;
                                                                            wcommcm=wcommcm+val8;
                                                                            advintrestcm=advintrestcm+val5;
                                                                            totamount=totamount+val1;
                                                                            lsfcm=lsfcm+val2;
                                                                            advpaycm=advpaycm+val4;


                                                                            st1=String.valueOf(totamount);
                                                                            st2=String.valueOf(lsfcm);
                                                                            st4=String.valueOf(advpaycm);
                                                                            lsf.setText("Kshs. "+st2);
                                                                            advpaid.setText("Kshs. "+st4);
                                                                            advintr.setText("Kshs "+String.valueOf(advintrestcm));

                                                                        }

                                                                        mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.child("groupadvancesbf").exists()){
                                                                                    adamount=(String)dataSnapshot.child("groupadvancesbf").getValue();
                                                                                    intadvbf=Double.parseDouble(adamount);
                                                                                    advbf.setText("Kshs. "+adamount);
                                                                                }else {
                                                                                    adamount = "0";
                                                                                    intadvbf = Double.parseDouble(adamount);
                                                                                    advbf.setText("Kshs. "+adamount);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.hasChild("total")){
                                                                                    totaladvences=Double.parseDouble(dataSnapshot.child("total").getValue().toString());
                                                                                    mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.hasChildren()) {
                                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                                                                    String advpaidamnt = (String) snapshot.child("advancegiven").getValue();
                                                                                                    double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                    double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                    intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                    advcontri.setText("Kshs. " + String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm));
                                                                                                    double tots = intadvpaidamnt + intadvbf;
                                                                                                    advinttot = advinttot + intrestonad;
                                                                                                    to = String.valueOf(tots);
                                                                                                    String advfee = snapshot.child("advancefee").getValue().toString();
                                                                                                    double advf = Double.parseDouble(advfee);
                                                                                                    totaladvfee = totaladvfee + advf;
                                                                                                    advncf = tots - advpaycm;
                                                                                                    cashgivend = cashgivend + (oldintadvpaidamnt / 1.1);
                                                                                                    intonadv.setText("Kshs. " + String.format("%.2f", advinttot));
                                                                                                    advgiv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                    advtot.setText("Kshs. " + String.format("%.2f", tots));
                                                                                                    adpaid.setText("Kshs. " + st4);
                                                                                                    adgv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                    advcf.setText("Kshs. " + String.format("%.2f", advncf));

                                                                                                }
                                                                                            }else{
                                                                                                String advpaidamnt = "0";
                                                                                                double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                advcontri.setText("Kshs. " + String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm));
                                                                                                double tots = intadvpaidamnt + intadvbf;
                                                                                                advinttot = advinttot + intrestonad;
                                                                                                advncf = tots - advpaycm;
                                                                                                to = String.valueOf(tots);
                                                                                                intonadv.setText("Kshs. " + String.format("%.2f", advinttot));
                                                                                                advgiv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                advtot.setText("Kshs. " + String.format("%.2f", tots));
                                                                                                adpaid.setText("Kshs. " + st4);
                                                                                                adgv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                advcf.setText("Kshs. " + String.format("%.2f", advncf));
                                                                                            }
                                                                                            mDatabase.child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    if (dataSnapshot.hasChildren()) {
                                                                                                        if (dataSnapshot.hasChild("cashback")){
                                                                                                            oldcb=dataSnapshot.child("cashback").getValue().toString();
                                                                                                        }else{
                                                                                                            oldcb="0";
                                                                                                        }
                                                                                                        mDatabase.child("details").child(grpkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                cashgiven = "0";
                                                                                                                final double fin = totaladvfee;
                                                                                                                fines.setText("Kshs. " + String.valueOf(fin));
                                                                                                                final double feecon = totaladvfee ;
                                                                                                                cashcollected = String.valueOf(totamount + fin);
                                                                                                                feecontri.setText("Kshs. " + String.valueOf(feecon));
                                                                                                                totalcontri.setText("Kshs. " + String.valueOf((feecon +(((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                                                cashpaid = String.valueOf(cashgivend);
                                                                                                                withs.setText("Kshs. "+String.valueOf(wcommcm));
                                                                                                                mpesas.setText(String.valueOf(mpesapay));

                                                                                                                double c1 = Double.parseDouble(cashgiven) + Double.parseDouble(cashcollected);
                                                                                                                double c2 = c1 - (cashgivend + wcommcm + mpesapay);
                                                                                                                netcash = String.valueOf(c2);
                                                                                                                nets.setText(netcash);
                                                                                                                totcoll.setText("Kshs. " + cashcollected);
                                                                                                                cashtooffice.setText("Kshs. " + netcash);


                                                                                                                fab2.setOnClickListener(new View.OnClickListener() {
                                                                                                                    @Override
                                                                                                                    public void onClick(View v) {
                                                                                                                        final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeesreport.this);
                                                                                                                        builders.setTitle("Save Report")
                                                                                                                                .setMessage("Do you want to save the report ?")
                                                                                                                                .setCancelable(true)
                                                                                                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                                                        DatabaseReference cashstatus = mDatabase.child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                                                                        cashstatus.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash) + Double.parseDouble(oldcb)));



                                                                                                                                        mDatabase.child("details").child(grpkey).child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                DatabaseReference createreport = mDatabase.child("reports").child(grpkey).child("schoolfees").push();
                                                                                                                                                String str = createreport.getKey();
                                                                                                                                                createreport.child("id").setValue(str);
                                                                                                                                                createreport.child("groupname").setValue(str);
                                                                                                                                                createreport.child("facilitator").setValue(nme);
                                                                                                                                                createreport.child("advcontribution").setValue(advcontri.getText().toString());
                                                                                                                                                createreport.child("feesnfines").setValue(feecontri.getText().toString());
                                                                                                                                                createreport.child("contributiontotal").setValue(totalcontri.getText().toString());
                                                                                                                                                createreport.child("fines").setValue("Kshs. " + String.valueOf(finescm + totalloanfee + totaladvfee + memfee));
                                                                                                                                                createreport.child("intonadv").setValue("Kshs. " + String.format("%.2f", advinttot));
                                                                                                                                                createreport.child("attendance").setValue(dataSnapshot.child("attendance").getValue().toString());
                                                                                                                                                createreport.child("meetdate").setValue(dataSnapshot.child("date").getValue().toString());
                                                                                                                                                createreport.child("venue").setValue(ven.getText().toString());
                                                                                                                                                createreport.child("cashfromoffice").setValue(dataSnapshot.child("cashfromoffice").getValue().toString());
                                                                                                                                                createreport.child("cashcollected").setValue(cashcollected);
                                                                                                                                                createreport.child("cashpaid").setValue(cashpaid);
                                                                                                                                                createreport.child("cashtooffice").setValue(netcash);
                                                                                                                                                createreport.child("totalLSF").setValue(st2);
                                                                                                                                                createreport.child("grandtotal1").setValue(String.format("%.2f", Double.parseDouble(to)));
                                                                                                                                                createreport.child("grandtotal2").setValue(st);
                                                                                                                                                createreport.child("totalAdvancesCollected").setValue(st4);
                                                                                                                                                createreport.child("totalAdvancesBroughtForward").setValue(String.format("%.2f", Double.parseDouble(adamount)));
                                                                                                                                                createreport.child("totalAdvancesGiven").setValue(String.format("%.2f", intadvpaidamnt));
                                                                                                                                                createreport.child("totalAdvancesCarriedForward").setValue(String.format("%.2f", advncf));


                                                                                                                                                DatabaseReference abf = mDatabase.child("finances").child(grpkey).child("advances").child("schoolfees");
                                                                                                                                                abf.child("currentadvancebf").setValue(String.valueOf(advncf));



                                                                                                                                                DatabaseReference reportdone = mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid);
                                                                                                                                                reportdone.child("reportflags").setValue("done");
                                                                                                                                                reportdone.child("reportid").setValue(str);

                                                                                                                                                fab2.setVisibility(View.GONE);

                                                                                                                                                Toast.makeText(schoolfeesreport.this,"Saved!",Toast.LENGTH_LONG).show();


                                                                                                                                                genMeetRCT(grname, lsfcm, advpaycm, fin, cashcollected, String.format("%.2f", intadvpaidamnt) , cashpaid, netcash, nme);

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
                                                                                }else {
                                                                                    totaladvences=0;

                                                                                    mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.hasChildren()) {
                                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                                                                    String advpaidamnt = (String) snapshot.child("advancegiven").getValue();
                                                                                                    double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                    double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                    intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                    advcontri.setText("Kshs. " + String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm));
                                                                                                    double tots = intadvpaidamnt + intadvbf;
                                                                                                    advinttot = advinttot + intrestonad;
                                                                                                    to = String.valueOf(tots);
                                                                                                    String advfee = snapshot.child("advancefee").getValue().toString();
                                                                                                    double advf = Double.parseDouble(advfee);
                                                                                                    totaladvfee = totaladvfee + advf;
                                                                                                    advncf = tots - advpaycm;
                                                                                                    cashgivend = cashgivend + (oldintadvpaidamnt / 1.1);
                                                                                                    intonadv.setText("Kshs. " + String.format("%.2f", advinttot));
                                                                                                    advgiv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                    advtot.setText("Kshs. " + String.format("%.2f", tots));
                                                                                                    adpaid.setText("Kshs. " + st4);
                                                                                                    adgv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                    advcf.setText("Kshs. " + String.format("%.2f", advncf));

                                                                                                }
                                                                                            }else{
                                                                                                String advpaidamnt = "0";
                                                                                                double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                advcontri.setText("Kshs. " + String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm));
                                                                                                double tots = intadvpaidamnt + intadvbf;
                                                                                                advinttot = advinttot + intrestonad;
                                                                                                advncf = tots - advpaycm;
                                                                                                to = String.valueOf(tots);
                                                                                                intonadv.setText("Kshs. " + String.format("%.2f", advinttot));
                                                                                                advgiv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                advtot.setText("Kshs. " + String.format("%.2f", tots));
                                                                                                adpaid.setText("Kshs. " + st4);
                                                                                                adgv.setText("Kshs. " + String.format("%.2f", totaladvences));
                                                                                                advcf.setText("Kshs. " + String.format("%.2f", advncf));
                                                                                            }
                                                                                            mDatabase.child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    if (dataSnapshot.hasChildren()) {
                                                                                                        if (dataSnapshot.hasChild("cashback")){
                                                                                                            oldcb=dataSnapshot.child("cashback").getValue().toString();
                                                                                                        }else{
                                                                                                            oldcb="0";
                                                                                                        }
                                                                                                        mDatabase.child("details").child(grpkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                cashgiven = "0";
                                                                                                                final double fin = totaladvfee;
                                                                                                                fines.setText("Kshs. " + String.valueOf(fin));
                                                                                                                final double feecon = totaladvfee ;
                                                                                                                cashcollected = String.valueOf(totamount + fin);
                                                                                                                feecontri.setText("Kshs. " + String.valueOf(feecon));
                                                                                                                totalcontri.setText("Kshs. " + String.valueOf((feecon +(((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                                                cashpaid = String.valueOf(cashgivend);
                                                                                                                withs.setText("Kshs. "+String.valueOf(wcommcm));
                                                                                                                mpesas.setText(String.valueOf(mpesapay));

                                                                                                                double c1 = Double.parseDouble(cashgiven) + Double.parseDouble(cashcollected);
                                                                                                                double c2 = c1 - (cashgivend + wcommcm + mpesapay);
                                                                                                                netcash = String.valueOf(c2);
                                                                                                                nets.setText(netcash);
                                                                                                                totcoll.setText("Kshs. " + cashcollected);
                                                                                                                cashtooffice.setText("Kshs. " + netcash);


                                                                                                                fab2.setOnClickListener(new View.OnClickListener() {
                                                                                                                    @Override
                                                                                                                    public void onClick(View v) {
                                                                                                                        final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeesreport.this);
                                                                                                                        builders.setTitle("Save Report")
                                                                                                                                .setMessage("Do you want to save the report ?")
                                                                                                                                .setCancelable(true)
                                                                                                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                                                        DatabaseReference cashstatus = mDatabase.child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                                                                                                        cashstatus.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash) + Double.parseDouble(oldcb)));



                                                                                                                                        mDatabase.child("details").child(grpkey).child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                DatabaseReference createreport = mDatabase.child("reports").child(grpkey).child("schoolfees").push();
                                                                                                                                                String str = createreport.getKey();
                                                                                                                                                createreport.child("id").setValue(str);
                                                                                                                                                createreport.child("groupname").setValue(str);
                                                                                                                                                createreport.child("facilitator").setValue(nme);
                                                                                                                                                createreport.child("advcontribution").setValue(advcontri.getText().toString());
                                                                                                                                                createreport.child("feesnfines").setValue(feecontri.getText().toString());
                                                                                                                                                createreport.child("contributiontotal").setValue(totalcontri.getText().toString());
                                                                                                                                                createreport.child("fines").setValue("Kshs. " + String.valueOf(finescm + totalloanfee + totaladvfee + memfee));
                                                                                                                                                createreport.child("intonadv").setValue("Kshs. " + String.format("%.2f", advinttot));
                                                                                                                                                createreport.child("attendance").setValue(dataSnapshot.child("attendance").getValue().toString());
                                                                                                                                                createreport.child("meetdate").setValue(dataSnapshot.child("date").getValue().toString());
                                                                                                                                                createreport.child("venue").setValue(ven.getText().toString());
                                                                                                                                                createreport.child("cashfromoffice").setValue(dataSnapshot.child("cashfromoffice").getValue().toString());
                                                                                                                                                createreport.child("cashcollected").setValue(cashcollected);
                                                                                                                                                createreport.child("cashpaid").setValue(cashpaid);
                                                                                                                                                createreport.child("cashtooffice").setValue(netcash);
                                                                                                                                                createreport.child("totalLSF").setValue(st2);
                                                                                                                                                createreport.child("grandtotal1").setValue(String.format("%.2f", Double.parseDouble(to)));
                                                                                                                                                createreport.child("grandtotal2").setValue(st);
                                                                                                                                                createreport.child("totalAdvancesCollected").setValue(st4);
                                                                                                                                                createreport.child("totalAdvancesBroughtForward").setValue(String.format("%.2f", Double.parseDouble(adamount)));
                                                                                                                                                createreport.child("totalAdvancesGiven").setValue(String.format("%.2f", intadvpaidamnt));
                                                                                                                                                createreport.child("totalAdvancesCarriedForward").setValue(String.format("%.2f", advncf));


                                                                                                                                                DatabaseReference abf = mDatabase.child("finances").child(grpkey).child("advances").child("schoolfees");
                                                                                                                                                abf.child("currentadvancebf").setValue(String.valueOf(advncf));



                                                                                                                                                DatabaseReference reportdone = mDatabase.child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid);
                                                                                                                                                reportdone.child("reportflags").setValue("done");
                                                                                                                                                reportdone.child("reportid").setValue(str);

                                                                                                                                                fab2.setVisibility(View.GONE);

                                                                                                                                                Toast.makeText(schoolfeesreport.this,"Saved!",Toast.LENGTH_LONG).show();


                                                                                                                                                genMeetRCT(grname, lsfcm, advpaycm, fin, cashcollected, String.format("%.2f", intadvpaidamnt) , cashpaid, netcash, nme);

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void genMeetRCT(String grpname,double lsfcm,double advpaycm,double fin,String cashcollected,String advpayd, String cashpaid, String netcash,String nme) {
        final DatabaseReference nr=mDatabase.child("Receipts").child(grpkey).child(meeetid).child("SchoolfeesReciept");
        nr.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        nr.child("groupname").setValue(grpname);
        nr.child("lsfcm").setValue(String.valueOf(lsfcm));
        nr.child("other").setValue("0");
        nr.child("loanandadv").setValue(String.valueOf(advpaycm));
        nr.child("finesandpenalties").setValue(String.valueOf(fin));
        nr.child("collcash").setValue(cashcollected);
        nr.child("advgive").setValue(advpayd);
        nr.child("totapaymnt").setValue(cashpaid);
        nr.child("netcash").setValue(netcash);
        nr.child("officer").setValue(nme);
    }
}
