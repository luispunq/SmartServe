package com.example.london.smartserve;

import android.app.admin.DeviceAdminInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class report2 extends AppCompatActivity {
    private TextView lsf,mSftot,mMeettot,mTOtt,advpaid,advintr,loanrepa,regfee,fines,totcoll,regs,
            advbf,advgiv,intonadv,advtot,adpaid,advcf,nets,
            loansbf,loansgvn,loantot,lopaid,loancf,insus,mcscfo,chand,adgv,lngv,withs,mpesas;
    private ImageView mGrpImage;
    private TextView grpname,cashfrmoffice,cashtooffice,fac,meedate,ven,advcontri,loancontri,feecontri,totalcontri;
    private String grpkey,meeetid=null,cloan=null,fieldid,oldmisc;
    private DatabaseReference mDatabase,mDemployee,mD,mD1,mD2,mD3,mD4,mD5,mD6,mD7,mD8,accounting,mD0;
    private double totamount=0,amountsf=0,lsfcm=0,loaninstcm=0,advpaycm=0,intadvpaidamnt=0,intadvbf=0,riskcm=0,mpesapay=0,total=0,tots=0,total2=0,wcommcm=0,advncf=0,nettotal=0,advintrestcm=0,loancashtot=0,
            finescm=0,advinttot=0,cashtotal=0,cashgivend=0,totalloanfee=0,totaladvfee=0,memfee=0,insupay=0d,totaladvences=0;
    private String adamount=null,loantots=null,user=null,fkey=null,cashgiven=null,cashcollected=null,cashpaid=null,netcash=null,num;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab,fab2;
    private String st2="0",st4="0",st3="0",st1="0";
    private String to=null,rf;
    private String st=null,date=null,n1;
    private String thasf=null,oldcb=null;
    private String grname;
    private String lsfbf=null,nme=null;
    private double withds=0;
    private double lsfs,loans,advs;
    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        date = extras.getString("date");
        Log.e("date",date);
        cashfrmoffice=findViewById(R.id.moneygiven);
        mMeettot=findViewById(R.id.moneyto);
        mSftot=findViewById(R.id.moneysfto);
        cashtooffice=findViewById(R.id.moneytot);
        lsf=findViewById(R.id.dd1);
        mAuth=FirebaseAuth.getInstance();
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
        regs=findViewById(R.id.dd6erg);
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

        mDemployee=FirebaseDatabase.getInstance().getReference().child("Employees");
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Receipts").child(grpkey);
        mD= FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        mD1= FirebaseDatabase.getInstance().getReference().child("finances").child(grpkey);
        mD2= FirebaseDatabase.getInstance().getReference().child("reports").child(grpkey);
        mD3= FirebaseDatabase.getInstance().getReference().child("fees").child(grpkey);
        mD4= FirebaseDatabase.getInstance().getReference().child("fieldwork");
        mD5=FirebaseDatabase.getInstance().getReference().child("groupperfomancesummary");
        mD6=FirebaseDatabase.getInstance().getReference().child("finances").child("cashdetails");
        mD7=FirebaseDatabase.getInstance().getReference().child("masterfinance");
        mD8=FirebaseDatabase.getInstance().getReference().child("meetings");
        mD0=FirebaseDatabase.getInstance().getReference().child("Accounts");




        mD.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grname=(String)dataSnapshot.child("groupName").getValue();
                num=dataSnapshot.child("num").getValue().toString();
                n1=dataSnapshot.child("numbers").getValue().toString();
                final String grimage=(String)dataSnapshot.child("profileImage").getValue();
                grpname.setText(grname);
                Picasso.with(report2.this).load(grimage).into(mGrpImage);


                mD.child("temp").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        meeetid=dataSnapshot.child("tempid").getValue().toString();
                        fieldid=dataSnapshot.child("fieldid").getValue().toString();
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(report2.this);
                                builder.setTitle("Select Options")
                                        .setCancelable(true)
                                        .setItems(R.array.PerfomanceAnalysis, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case 0:
                                                        Intent backtosignin =new Intent(report2.this,perfomanceanalysis.class);
                                                            Bundle extras = new Bundle();
                                                            extras.putString("gkey", grpkey);
                                                            extras.putString("meet", meeetid);
                                                            extras.putString("date", date);
                                                            backtosignin.putExtras(extras);
                                                            startActivity(backtosignin);
                                                            dialog.dismiss();
                                                        break;
                                                    case 1:
                                                            Intent backtosigninf =new Intent(report2.this,schoolfeesperfomanceanalysis.class);
                                                            Bundle extrasd = new Bundle();
                                                            extrasd.putString("gkey", grpkey);
                                                            extrasd.putString("meet", meeetid);
                                                            extrasd.putString("date", date);
                                                            backtosigninf.putExtras(extrasd);
                                                            startActivity(backtosigninf);
                                                            dialog.dismiss();
                                                        break;
                                                    case 2:
                                                            Intent backtosigninfo =new Intent(report2.this,loandefaultsperfomanceanalysis.class);
                                                            Bundle extrasdo = new Bundle();
                                                            extrasdo.putString("gkey", grpkey);
                                                            extrasdo.putString("meet", meeetid);
                                                            extrasdo.putString("date", date);
                                                            backtosigninfo.putExtras(extrasdo);
                                                            startActivity(backtosigninfo);
                                                            dialog.dismiss();
                                                            break;

                                                }
                                            }
                                        });
                                AlertDialog alert11 = builder.create();
                                alert11.show();

                            }
                        });

                        mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("reportflags").exists()){
                                    fab2.setVisibility(View.GONE);
                                    String trs=dataSnapshot.child("reportid").getValue().toString();
                                    mD2.child("finalreport").child(trs).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("chand").exists()&&dataSnapshot.child("regs").exists()&&dataSnapshot.child("withdrawals").exists()&&dataSnapshot.child("mpesas").exists()&&dataSnapshot.child("advcontribution").exists()&&dataSnapshot.child("loancontribution").exists()&&dataSnapshot.child("feesnfines").exists()){
                                                fac.setText(dataSnapshot.child("facilitator").getValue().toString());
                                                ven.setText(dataSnapshot.child("venue").getValue().toString());
                                                meedate.setText(dataSnapshot.child("meetdate").getValue().toString());
                                                fines.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("fines").getValue().toString())));
                                                intonadv.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("intonadv").getValue().toString())));
                                                advintr.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("intonadv").getValue().toString())));
                                                insus.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("insurance").getValue().toString())));
                                                nets.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashtooffice").getValue().toString())));
                                                chand.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("chand").getValue().toString())));
                                                adgv.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesGiven").getValue().toString())));
                                                lngv.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansGivencash").getValue().toString())));
                                                withs.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("withdrawals").getValue().toString())));
                                                mpesas.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("mpesas").getValue().toString())));
                                                regs.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("regs").getValue().toString())));
                                                advcontri.setText("Kshs. "+dataSnapshot.child("advcontribution").getValue().toString());
                                                loancontri.setText("Kshs. "+dataSnapshot.child("loancontribution").getValue().toString());
                                                feecontri.setText("Kshs. "+dataSnapshot.child("feesnfines").getValue().toString());
                                                totalcontri.setText("Kshs. "+dataSnapshot.child("contributiontotal").getValue().toString());
                                                cashfrmoffice.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashfromoffice").getValue().toString())));
                                                cashtooffice.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashtooffice").getValue().toString())));
                                                if (dataSnapshot.child("sftot").exists()&&dataSnapshot.child("meettot").exists()){
                                                    mSftot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("sftot").getValue().toString())));
                                                    mMeettot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("meettot").getValue().toString())));
                                                }else {
                                                    mSftot.setText("Kshs. -");
                                                    mMeettot.setText("Kshs. -");
                                                }

                                                lsf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLSF").getValue().toString())));
                                                advpaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCollected").getValue().toString())));
                                                loanrepa.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansInstallmentsCollected").getValue().toString())));
                                                totcoll.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString())));
                                                advbf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesBroughtForward").getValue().toString())));
                                                advgiv.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesGiven").getValue().toString())));
                                                adpaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCollected").getValue().toString())));
                                                advcf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCarriedForward").getValue().toString())));
                                                loansbf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansBroughtForward").getValue().toString())));
                                                loansgvn.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansGivencash").getValue().toString())));
                                                lopaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansInstallmentsCollected").getValue().toString())));
                                                loancf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansCarriedForward").getValue().toString())));
                                                if (dataSnapshot.child("grandtotal1").exists()&&dataSnapshot.child("grandtotal2").exists()){
                                                    advtot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("grandtotal1").getValue().toString())));
                                                    loantot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("grandtotal2").getValue().toString())));}
                                                    else{
                                                    advtot.setText("Kshs. 0");
                                                    loantot.setText("Kshs. 0");
                                                }
                                            }else{
                                                String a="0";
                                                String b="0";
                                                fac.setText(dataSnapshot.child("facilitator").getValue().toString());
                                                ven.setText(dataSnapshot.child("venue").getValue().toString());
                                                meedate.setText(dataSnapshot.child("meetdate").getValue().toString());
                                                fines.setText("Kshs. "+df.format(Double.parseDouble(a)));
                                                intonadv.setText("Kshs. "+df.format(Double.parseDouble(b)));
                                                advcontri.setText("Kshs. 0");
                                                loancontri.setText("Kshs. 0");
                                                feecontri.setText("Kshs. 0");
                                                totalcontri.setText("Kshs. 0");
                                                regs.setText("Kshs. "+"0");
                                                cashfrmoffice.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashfromoffice").getValue().toString())));
                                                cashtooffice.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashtooffice").getValue().toString())));
                                                if (dataSnapshot.child("sftot").exists()&&dataSnapshot.child("meettot").exists()){
                                                    mSftot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("sftot").getValue().toString())));
                                                    mMeettot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("meettot").getValue().toString())));
                                                }else {
                                                    mSftot.setText("Kshs. -");
                                                    mMeettot.setText("Kshs. -");
                                                }
                                                lsf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLSF").getValue().toString())));
                                                advpaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCollected").getValue().toString())));
                                                loanrepa.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansInstallmentsCollected").getValue().toString())));
                                                totcoll.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString())));
                                                advbf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesBroughtForward").getValue().toString())));
                                                advgiv.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesGiven").getValue().toString())));
                                                adpaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCollected").getValue().toString())));
                                                advcf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalAdvancesCarriedForward").getValue().toString())));
                                                loansbf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansBroughtForward").getValue().toString())));
                                                loansgvn.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansGiven").getValue().toString())));
                                                lopaid.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansInstallmentsCollected").getValue().toString())));
                                                loancf.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("totalLoansCarriedForward").getValue().toString())));
                                                if (dataSnapshot.child("grandtotal1").exists()&&dataSnapshot.child("grandtotal2").exists()){
                                                    advtot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("grandtotal1").getValue().toString())));
                                                    loantot.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("grandtotal2").getValue().toString())));
                                                }else{
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
                                    mD.child("temp").child(date).addListenerForSingleValueEvent(new ValueEventListener()  {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            user=dataSnapshot.child("facid").getValue().toString();
                                            meeetid=dataSnapshot.child("tempid").getValue().toString();
                                            mDemployee.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    fkey=dataSnapshot.child("fieldid").getValue().toString();
                                                    nme=dataSnapshot.child("name").getValue().toString();
                                                    fac.setText(nme);
                                                    mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            cashfrmoffice.setText("Kshs. "+df.format(Double.parseDouble(dataSnapshot.child("cashfromoffice").getValue().toString())));
                                                            meedate.setText(dataSnapshot.child("date").getValue().toString());
                                                            ven.setText(dataSnapshot.child("venue").getValue().toString());
                                                            mD.child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                        String amount=(String)snapshot.child("totalamount").getValue();
                                                                        String lsfamount=(String)snapshot.child("lsf").getValue();
                                                                        String loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                                                                        String advpayamount=(String)snapshot.child("advancepayment").getValue();
                                                                        String advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                                                                        String mpesapays=(String)snapshot.child("Mpesa").getValue();
                                                                        String withdraws=(String)snapshot.child("wamount").getValue();;
                                                                        String insurance=(String)snapshot.child("insurance").getValue();
                                                                        String wcomm=(String)snapshot.child("wcomm").getValue();
                                                                        String fines=(String)snapshot.child("fines").getValue();
                                                                        String others=(String)snapshot.child("fines").getValue();

                                                                        double val1=Double.parseDouble(amount);
                                                                        double val2=Double.parseDouble(lsfamount);
                                                                        double val3=Double.parseDouble(loaninstamount);
                                                                        double val4=Double.parseDouble(advpayamount);
                                                                        double val5=Double.parseDouble(advpayintrest);
                                                                        double val6=Double.parseDouble(fines);
                                                                        double val8=Double.parseDouble(wcomm);
                                                                        double val9=Double.parseDouble(mpesapays);
                                                                        double val10=Double.parseDouble(insurance);
                                                                        double val11=Double.parseDouble(withdraws);

                                                                        mpesapay=mpesapay+val9;
                                                                        wcommcm=wcommcm+val8;
                                                                        advintrestcm=advintrestcm+val5;
                                                                        finescm=finescm+val6;
                                                                        totamount=totamount+val1;
                                                                        lsfcm=lsfcm+val2;
                                                                        loaninstcm=loaninstcm+val3;
                                                                        advpaycm=advpaycm+val4;
                                                                        insupay=insupay+val10;
                                                                        withds=withds+val11;


                                                                        st1=String.valueOf(totamount);
                                                                        st2=String.valueOf(lsfcm);
                                                                        st3=String.valueOf(loaninstcm);
                                                                        st4=String.valueOf(advpaycm);
                                                                        lsf.setText("Kshs. "+df.format(Double.parseDouble(st2)));
                                                                        advpaid.setText("Kshs. "+df.format(Double.parseDouble(st4)));
                                                                        advintr.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(advintrestcm))));
                                                                        loanrepa.setText("Kshs. "+df.format(Double.parseDouble(st3)));
                                                                        insus.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(insupay))));

                                                                    }

                                                                    mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.child("groupadvancesbf").exists()){
                                                                                lsfbf=dataSnapshot.child("groupsavingsbf").getValue().toString();
                                                                                adamount=(String)dataSnapshot.child("groupadvancesbf").getValue();
                                                                                intadvbf=Double.parseDouble(adamount);
                                                                                advbf.setText("Kshs. "+df.format(Double.parseDouble(adamount)));
                                                                            }else {
                                                                                adamount = "0";
                                                                                intadvbf = Double.parseDouble(adamount);
                                                                                advbf.setText("Kshs. "+df.format(Double.parseDouble(adamount)));
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    mD.child("meetings").child(meeetid).child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.hasChild("total")){
                                                                                totaladvences=Double.parseDouble(dataSnapshot.child("total").getValue().toString());
                                                                                mD.child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.hasChildren()) {
                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                String advpaidamnt = (String) snapshot.child("advancegiven").getValue();
                                                                                                double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                advcontri.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                                tots = intadvpaidamnt + intadvbf;
                                                                                                advinttot = advinttot + intrestonad;
                                                                                                to = String.valueOf(tots);
                                                                                                String advfee = snapshot.child("advancefee").getValue().toString();
                                                                                                double advf = Double.parseDouble(advfee);
                                                                                                totaladvfee = totaladvfee + advf;
                                                                                                advncf = tots - advpaycm;
                                                                                                cashgivend = cashgivend + (Double.parseDouble(String.format("%.2f",oldintadvpaidamnt / 1.1)));
                                                                                                intonadv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advinttot))));
                                                                                                advgiv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                                advtot.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", tots))));
                                                                                                adpaid.setText("Kshs. "+df.format(Double.parseDouble( st4)));
                                                                                                adgv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                                advcf.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advncf))));
                                                                                            }
                                                                                        }else {
                                                                                            String advpaidamnt = "0";
                                                                                            double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                            double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                            intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                            advcontri.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                            tots = intadvpaidamnt + intadvbf;
                                                                                            advinttot = advinttot + intrestonad;
                                                                                            to = String.valueOf(tots);
                                                                                            String advfee = "0";
                                                                                            double advf = Double.parseDouble(advfee);
                                                                                            totaladvfee = totaladvfee + advf;
                                                                                            advncf = tots - advpaycm;
                                                                                            cashgivend = cashgivend + (Double.parseDouble(String.format("%.2f",oldintadvpaidamnt / 1.1)));
                                                                                            intonadv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advinttot))));
                                                                                            advgiv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                            advtot.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", tots))));
                                                                                            adpaid.setText("Kshs. "+df.format(Double.parseDouble( st4)));
                                                                                            adgv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                            advcf.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advncf))));

                                                                                        }
                                                                                        mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                cloan=(String)dataSnapshot.child("grouploansbf").getValue();
                                                                                                loansbf.setText("Kshs. "+df.format(Double.parseDouble(cloan)));

                                                                                                mD1.child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        if (dataSnapshot.hasChildren()){
                                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                                String test = snapshot.child("status").getValue().toString();
                                                                                                                String datetest = snapshot.child("dateapproved").getValue().toString();
                                                                                                                if (datetest.equals(date)) {
                                                                                                                    if (test.equals("done")) {
                                                                                                                        loantots = (String) snapshot.child("amount").getValue();
                                                                                                                        String loancash = (String) snapshot.child("amountcash").getValue();
                                                                                                                        String loanfee = snapshot.child("fee").getValue().toString();
                                                                                                                        double loanf = Double.parseDouble(loanfee);
                                                                                                                        totalloanfee = totalloanfee + loanf;
                                                                                                                        loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                                        total = total + Double.parseDouble(loantots);
                                                                                                                        cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                                        loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                                        total2 = total + (Double.parseDouble(cloan));
                                                                                                                        st = String.valueOf(total2);
                                                                                                                        loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                                        lngv.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(loancashtot))));
                                                                                                                        loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                                        lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                                        nettotal = total2 - (Double.parseDouble(st3));
                                                                                                                        loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                                    }
                                                                                                                }else {
                                                                                                                    loantots = "0";
                                                                                                                    String loancash = "0";
                                                                                                                    String loanfee = "0";
                                                                                                                    double loanf = Double.parseDouble(loanfee);
                                                                                                                    totalloanfee = totalloanfee + loanf;
                                                                                                                    loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                                    total = total + Double.parseDouble(loantots);
                                                                                                                    cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                                    loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                                    total2 = total + (Double.parseDouble(cloan));
                                                                                                                    st = String.valueOf(total2);
                                                                                                                    loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                                    lngv.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(loancashtot))));
                                                                                                                    loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                                    lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                                    nettotal = total2 - (Double.parseDouble(st3));
                                                                                                                    loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                                }
                                                                                                            }
                                                                                                        }else {
                                                                                                            loantots = "0";
                                                                                                            String loancash = "0";
                                                                                                            String loanfee = "0";
                                                                                                            double loanf = Double.parseDouble(loanfee);
                                                                                                            totalloanfee = totalloanfee + loanf;
                                                                                                            loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                            total = total + Double.parseDouble(loantots);
                                                                                                            cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                            loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                            total2 = total + (Double.parseDouble(cloan));
                                                                                                            st = String.valueOf(total2);
                                                                                                            loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                            lngv.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(loancashtot))));
                                                                                                            loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                            lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                            nettotal = total2 - (Double.parseDouble(st3));
                                                                                                            loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                        }
                                                                                                        mD3.child(date).child("memberregistration").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                if (dataSnapshot.hasChildren()){
                                                                                                                for (DataSnapshot snap:dataSnapshot.getChildren()){
                                                                                                                    if (snap.child("groupid").getValue().toString().equals("mpesa")){
                                                                                                                        String advfe=snap.child("amount").getValue().toString();
                                                                                                                        double advf=Double.parseDouble(advfe);
                                                                                                                        mpesapay=mpesapay+advf;
                                                                                                                        memfee=memfee+advf;
                                                                                                                        regs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(memfee))));
                                                                                                                    }else {
                                                                                                                        String advfe=snap.child("amount").getValue().toString();
                                                                                                                        double advf=Double.parseDouble(advfe);
                                                                                                                        memfee=memfee+advf;
                                                                                                                        regs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(memfee))));
                                                                                                                    }

                                                                                                                }
                                                                                                                }else {
                                                                                                                    memfee=0;
                                                                                                                    regs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(memfee))));
                                                                                                                }

                                                                                                                mD.child("schoolfees").child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        if (dataSnapshot.child("reportid").exists()){
                                                                                                                            String sfid=dataSnapshot.child("reportid").getValue().toString();

                                                                                                                            mD2.child("schoolfees").child(sfid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                    amountsf=Double.parseDouble(dataSnapshot.child("cashtooffice").getValue().toString());
                                                                                                                                    mSftot.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(amountsf))));
                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                                                                }
                                                                                                                            });
                                                                                                                        }else {
                                                                                                                            amountsf=0;
                                                                                                                            mSftot.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(amountsf))));
                                                                                                                        }
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                                    }
                                                                                                                });

                                                                                                                mD4.child(user).child(date).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        if (dataSnapshot.hasChildren()) {
                                                                                                                            if (dataSnapshot.hasChild("cashback")){
                                                                                                                                oldcb=dataSnapshot.child("cashback").getValue().toString();
                                                                                                                            }else{
                                                                                                                                oldcb="0";
                                                                                                                            }
                                                                                                                            mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                    cashgiven = dataSnapshot.child("cashfromoffice").getValue().toString();
                                                                                                                                    final double fin = finescm + totalloanfee + totaladvfee + memfee;
                                                                                                                                    fines.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(fin))));
                                                                                                                                    final double feecon = finescm + totalloanfee + totaladvfee + memfee;
                                                                                                                                    cashcollected = String.valueOf(totamount + fin-finescm);
                                                                                                                                    feecontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(feecon))));
                                                                                                                                    totalcontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(Math.round(feecon + (total - loancashtot) + (((intadvpaidamnt / 1.1) / 10) + advintrestcm))))));
                                                                                                                                    cashpaid = String.valueOf(cashgivend);
                                                                                                                                    withs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(withds))));
                                                                                                                                    mpesas.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(mpesapay))));

                                                                                                                                    final double c1 = Double.parseDouble(cashgiven) + Double.parseDouble(cashcollected);
                                                                                                                                    double c2 = c1 - (cashgivend  + mpesapay + withds);
                                                                                                                                    netcash = String.valueOf(c2);
                                                                                                                                    nets.setText("Kshs. "+df.format(Double.parseDouble(netcash)));
                                                                                                                                    chand.setText("Kshs. "+df.format(Double.parseDouble(String.format("%.2f",c1))));
                                                                                                                                    totcoll.setText("Kshs. "+df.format(Double.parseDouble( cashcollected)));
                                                                                                                                    mMeettot.setText("Kshs. "+df.format(Double.parseDouble( netcash)));
                                                                                                                                    cashtooffice.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(Double.parseDouble(netcash)+amountsf))));

                                                                                                                                    final Double valof=Double.parseDouble(lsfbf)+lsfcm;



                                                                                                                                    fab2.setOnClickListener(new View.OnClickListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onClick(View v) {
                                                                                                                                            final EditText taskedit=new EditText(report2.this);
                                                                                                                                            taskedit.setHint("Enter School Fees Cash Collected.");
                                                                                                                                            taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                                                                                            taskedit.setText(String.valueOf(amountsf));
                                                                                                                                            final AlertDialog.Builder builders = new AlertDialog.Builder(report2.this);
                                                                                                                                            builders.setTitle("Save Report")
                                                                                                                                                    .setMessage("Enter Schoolfees Banking and save.")
                                                                                                                                                    .setView(taskedit)
                                                                                                                                                    .setCancelable(true)
                                                                                                                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                                                                                            thasf=taskedit.getText().toString();
                                                                                                                                                            amountsf=Double.parseDouble(thasf);
                                                                                                                                                            if (advncf==0&&tots>advpaycm){
                                                                                                                                                                Toast.makeText(report2.this,"Please Check that Carried Forward Values Compute.",Toast.LENGTH_LONG).show();
                                                                                                                                                            }else {
                                                                                                                                                                if (nettotal==0&&total2>Double.parseDouble(st3)){
                                                                                                                                                                    Toast.makeText(report2.this,"Please Check that Carried Forward Values Compute.",Toast.LENGTH_LONG).show();
                                                                                                                                                                }else {
                                                                                                                                                                    mD4.child(user).child(date).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            if (dataSnapshot.child("cashcollected").exists()){
                                                                                                                                                                                double cb=Double.parseDouble(dataSnapshot.child("cashback").getValue().toString());
                                                                                                                                                                                double cp=Double.parseDouble(dataSnapshot.child("cashpaid").getValue().toString());
                                                                                                                                                                                double cc=Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString());

                                                                                                                                                                                double ncb=cb+(Double.parseDouble(netcash)+amountsf);
                                                                                                                                                                                double ncp=cp+Double.parseDouble(cashpaid);
                                                                                                                                                                                double ncc=cc+Double.parseDouble(cashcollected);

                                                                                                                                                                                DatabaseReference cashstatus = mD4.child(user).child(date).child(fkey);
                                                                                                                                                                                cashstatus.child("cashcollected").setValue(String.valueOf(ncc));
                                                                                                                                                                                cashstatus.child("cashpaid").setValue(String.valueOf(ncp));
                                                                                                                                                                                cashstatus.child("cashback").setValue(String.valueOf(ncb));
                                                                                                                                                                                cashstatus.child("groupsgiven").child(grpkey).child("amountback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));

                                                                                                                                                                            }else {
                                                                                                                                                                                DatabaseReference cashstatus = mD4.child(user).child(date).child(fkey);
                                                                                                                                                                                cashstatus.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                                cashstatus.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                                cashstatus.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                                cashstatus.child("groupsgiven").child(grpkey).child("amountback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                            }
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });

                                                                                                                                                                    DatabaseReference cashupdates = mD.child("meetings").child(meeetid).child("cashdetails");
                                                                                                                                                                    cashupdates.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                    cashupdates.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                    cashupdates.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)));
                                                                                                                                                                    cashupdates.child("cashfromoffice").setValue(cashgiven);

                                                                                                                                                                    DatabaseReference session=mD8.child("all").child(date).push();
                                                                                                                                                                    session.child("groupName").setValue(grname);
                                                                                                                                                                    session.child("groupImage").setValue(grimage);
                                                                                                                                                                    session.child("groupid").setValue(grpkey);
                                                                                                                                                                    session.child("facilitator").setValue(nme);
                                                                                                                                                                    session.child("meetid").setValue(meeetid);


                                                                                                                                                                    DatabaseReference globalcashupdates = mD6.child(date).child(grpkey);
                                                                                                                                                                    globalcashupdates.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                    globalcashupdates.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                    globalcashupdates.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                    globalcashupdates.child("cashfromoffice").setValue(cashgiven);

                                                                                                                                                                    final DatabaseReference globalcashupdats = mD6.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);

                                                                                                                                                                    globalcashupdats.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            if (dataSnapshot.exists()){
                                                                                                                                                                                double pcc=Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString());
                                                                                                                                                                                double pcp=Double.parseDouble(dataSnapshot.child("cashpaid").getValue().toString());
                                                                                                                                                                                double pcb=Double.parseDouble(dataSnapshot.child("cashback").getValue().toString());
                                                                                                                                                                                double pcfo=Double.parseDouble(dataSnapshot.child("cashfromoffice").getValue().toString());

                                                                                                                                                                                String ncc=String.valueOf(pcc+Double.parseDouble(cashcollected));
                                                                                                                                                                                String ncp=String.valueOf(pcp+Double.parseDouble(cashpaid));
                                                                                                                                                                                String ncb=String.valueOf(pcb+Double.parseDouble(netcash)+amountsf);
                                                                                                                                                                                String ncfo=String.valueOf(pcfo+Double.parseDouble(cashgiven));

                                                                                                                                                                                globalcashupdats.child("cashcollected").setValue(ncc);
                                                                                                                                                                                globalcashupdats.child("cashpaid").setValue(ncp);
                                                                                                                                                                                globalcashupdats.child("cashback").setValue(ncb);
                                                                                                                                                                                globalcashupdats.child("cashfromoffice").setValue(ncfo);


                                                                                                                                                                            }else {
                                                                                                                                                                                globalcashupdats.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                                globalcashupdats.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                                globalcashupdats.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                                globalcashupdats.child("cashfromoffice").setValue(cashgiven);

                                                                                                                                                                            }

                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });



                                                                                                                                                                    mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            DatabaseReference createreport = mD2.child("finalreport").push();
                                                                                                                                                                            String str = createreport.getKey();
                                                                                                                                                                            createreport.child("id").setValue(str);
                                                                                                                                                                            createreport.child("facilitator").setValue(nme);
                                                                                                                                                                            createreport.child("advcontribution").setValue(advcontri.getText().toString());
                                                                                                                                                                            createreport.child("loancontribution").setValue(loancontri.getText().toString());
                                                                                                                                                                            createreport.child("feesnfines").setValue(feecontri.getText().toString());
                                                                                                                                                                            createreport.child("contributiontotal").setValue(totalcontri.getText().toString());
                                                                                                                                                                            createreport.child("fines").setValue(String.valueOf(finescm + totalloanfee + totaladvfee + memfee));
                                                                                                                                                                            createreport.child("intonadv").setValue(String.format("%.2f", advinttot));
                                                                                                                                                                            createreport.child("attendance").setValue(dataSnapshot.child("attendance").getValue().toString());
                                                                                                                                                                            createreport.child("meetdate").setValue(dataSnapshot.child("date").getValue().toString());
                                                                                                                                                                            createreport.child("venue").setValue(dataSnapshot.child("venue").getValue().toString());
                                                                                                                                                                            createreport.child("cashfromoffice").setValue(dataSnapshot.child("cashfromoffice").getValue().toString());
                                                                                                                                                                            createreport.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                            createreport.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                            createreport.child("cashtooffice").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                            createreport.child("sftot").setValue(String.valueOf(amountsf));
                                                                                                                                                                            createreport.child("meettot").setValue(netcash);
                                                                                                                                                                            createreport.child("regs").setValue(String.valueOf(memfee));
                                                                                                                                                                            createreport.child("mpesas").setValue(String.valueOf(mpesapay));
                                                                                                                                                                            createreport.child("totalLSF").setValue(st2);
                                                                                                                                                                            createreport.child("withdrawals").setValue(String.valueOf(withds));
                                                                                                                                                                            createreport.child("chand").setValue(String.format("%.2f",c1));
                                                                                                                                                                            createreport.child("insurance").setValue(String.valueOf(insupay));
                                                                                                                                                                            createreport.child("grandtotal1").setValue(String.format("%.2f", Double.parseDouble(to)));
                                                                                                                                                                            createreport.child("grandtotal2").setValue(st);
                                                                                                                                                                            createreport.child("totalAdvancesCollected").setValue(st4);
                                                                                                                                                                            createreport.child("totalLoansInstallmentsCollected").setValue(st3);
                                                                                                                                                                            createreport.child("totalAdvancesBroughtForward").setValue(String.format("%.2f", Double.parseDouble(adamount)));
                                                                                                                                                                            createreport.child("totalAdvancesGiven").setValue(String.format("%.2f", intadvpaidamnt));
                                                                                                                                                                            createreport.child("totalAdvancesCarriedForward").setValue(String.format("%.2f", advncf));
                                                                                                                                                                            createreport.child("totalLoansBroughtForward").setValue(cloan);
                                                                                                                                                                            createreport.child("totalLoansGiven").setValue(String.valueOf(total));
                                                                                                                                                                            createreport.child("totalLoansGivencash").setValue(String.valueOf(loancashtot));
                                                                                                                                                                            createreport.child("totalLoansCarriedForward").setValue(String.valueOf(nettotal));


                                                                                                                                                                            DatabaseReference reportdone = mD.child("meetings").child(meeetid);
                                                                                                                                                                            reportdone.child("reportflags").setValue("done");
                                                                                                                                                                            reportdone.child("reportid").setValue(str);

                                                                                                                                                                            DatabaseReference fieldreport=mD4.child(user).child(date).child(fkey);
                                                                                                                                                                            fieldreport.child("report").setValue(str);

                                                                                                                                                                            DatabaseReference reportdones = mD.child("groupdetails");
                                                                                                                                                                            reportdones.child("cashfromoffice").setValue("0");

                                                                                                                                                                            DatabaseReference session=mD8.child("master").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
                                                                                                                                                                            session.child("groupName").setValue(grname);
                                                                                                                                                                            session.child("groupImage").setValue(grimage);
                                                                                                                                                                            session.child("date").setValue(date);
                                                                                                                                                                            session.child("groupid").setValue(grpkey);
                                                                                                                                                                            session.child("facilitator").setValue(nme);
                                                                                                                                                                            session.child("meetid").setValue(meeetid);

                                                                                                                                                                            DatabaseReference allgroups = mD5.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
                                                                                                                                                                            allgroups.child("groupname").setValue(grname);
                                                                                                                                                                            allgroups.child("members").setValue(n1);
                                                                                                                                                                            allgroups.child("num").setValue(Integer.parseInt(num));
                                                                                                                                                                            allgroups.child("regs").setValue(String.valueOf(memfee));
                                                                                                                                                                            allgroups.child("officer").setValue(nme);
                                                                                                                                                                            allgroups.child("income").setValue(String.format("%.2f",(feecon + (total - loancashtot) + (((intadvpaidamnt / 1.1) / 10) + advintrestcm))));


                                                                                                                                                                            mD4.child(user).child(date).child(fieldid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                                                                                                                    if (dataSnapshot.child("groupsgiven").child(grpkey).child("fare").exists()){
                                                                                                                                                                                        String ffare=dataSnapshot.child("groupsgiven").child(grpkey).child("fare").getValue().toString();
                                                                                                                                                                                        final double faree=Double.parseDouble(ffare);

                                                                                                                                                                                        mD0.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                                if (dataSnapshot.child("Balance").exists()){
                                                                                                                                                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                                                                                                                                                    String balb42=dataSnapshot.child(date).child("Amountout").getValue().toString();
                                                                                                                                                                                                    double balb44=Double.parseDouble(balb4);
                                                                                                                                                                                                    double balb442=Double.parseDouble(balb42);
                                                                                                                                                                                                    double e=balb44+Double.parseDouble(netcash)+amountsf;
                                                                                                                                                                                                    double e2=balb442+Double.parseDouble(netcash)+amountsf;

                                                                                                                                                                                                    DatabaseReference newdepos=mD0.child(user);
                                                                                                                                                                                                    newdepos.child(date).child("Amountout").setValue(String.valueOf(e2));


                                                                                                                                                                                                    DatabaseReference newdepo=mD0.child(user);
                                                                                                                                                                                                    newdepo.child("Balance").setValue(String.valueOf(e-faree));
                                                                                                                                                                                                }
                                                                                                                                                                                            }

                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                                            }
                                                                                                                                                                                        });

                                                                                                                                                                                    }else {
                                                                                                                                                                                        final double faree=0.0;

                                                                                                                                                                                        mD0.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                                if (dataSnapshot.child("Balance").exists()){
                                                                                                                                                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                                                                                                                                                    String balb42=dataSnapshot.child(date).child("Amountout").getValue().toString();
                                                                                                                                                                                                    double balb44=Double.parseDouble(balb4);
                                                                                                                                                                                                    double balb442=Double.parseDouble(balb42);
                                                                                                                                                                                                    double e=balb44+Double.parseDouble(netcash)+amountsf;
                                                                                                                                                                                                    double e2=balb442+Double.parseDouble(netcash)+amountsf;

                                                                                                                                                                                                    DatabaseReference newdepos=mD0.child(user);
                                                                                                                                                                                                    newdepos.child(date).child("Amountout").setValue(String.valueOf(e2));


                                                                                                                                                                                                    DatabaseReference newdepo=mD0.child(user);
                                                                                                                                                                                                    newdepo.child("Balance").setValue(String.valueOf(e-faree));
                                                                                                                                                                                                    newdepo.child("Flag").setValue(user);
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


                                                                                                                                                                            genMeetRCT(amountsf,mpesapay,grname, lsfcm, memfee, advpaycm, loaninstcm, fin, totalloanfee, cashcollected, String.format("%.2f", intadvpaidamnt), (String.valueOf(total)), cashpaid, netcash, nme);

                                                                                                                                                                            doacoounts();
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            dialog.dismiss();
                                                                                                                                                            fab2.setVisibility(View.GONE);
                                                                                                                                                            Toast.makeText(report2.this, "Report Saved..", Toast.LENGTH_LONG).show();
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
                                                                            }else {
                                                                                totaladvences=0;
                                                                                mD.child("meetings").child(meeetid).child("advances").child("advancesgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.hasChildren()) {
                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                String advpaidamnt = (String) snapshot.child("advancegiven").getValue();
                                                                                                double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                                double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                                intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                                advcontri.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                                tots = intadvpaidamnt + intadvbf;
                                                                                                advinttot = advinttot + intrestonad;
                                                                                                to = String.valueOf(tots);
                                                                                                String advfee = snapshot.child("advancefee").getValue().toString();
                                                                                                double advf = Double.parseDouble(advfee);
                                                                                                totaladvfee = totaladvfee + advf;
                                                                                                advncf = tots - advpaycm;
                                                                                                cashgivend = cashgivend + (Double.parseDouble(String.format("%.2f",oldintadvpaidamnt / 1.1)));
                                                                                                intonadv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advinttot))));
                                                                                                advgiv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                                advtot.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", tots))));
                                                                                                adpaid.setText("Kshs. "+df.format(Double.parseDouble( st4)));
                                                                                                adgv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                                advcf.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advncf))));
                                                                                            }
                                                                                        }else {
                                                                                            String advpaidamnt = "0";
                                                                                            double oldintadvpaidamnt = Double.parseDouble(advpaidamnt);
                                                                                            double intrestonad = oldintadvpaidamnt - (oldintadvpaidamnt / 1.1);
                                                                                            intadvpaidamnt = intadvpaidamnt + oldintadvpaidamnt;
                                                                                            advcontri.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f",((intadvpaidamnt / 1.1) / 10) + advintrestcm))));
                                                                                            tots = intadvpaidamnt + intadvbf;
                                                                                            advinttot = advinttot + intrestonad;
                                                                                            to = String.valueOf(tots);
                                                                                            String advfee = "0";
                                                                                            double advf = Double.parseDouble(advfee);
                                                                                            totaladvfee = totaladvfee + advf;
                                                                                            advncf = tots - advpaycm;
                                                                                            cashgivend = cashgivend + (Double.parseDouble(String.format("%.2f",oldintadvpaidamnt / 1.1)));
                                                                                            intonadv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advinttot))));
                                                                                            advgiv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                            advtot.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", tots))));
                                                                                            adpaid.setText("Kshs. "+df.format(Double.parseDouble( st4)));
                                                                                            adgv.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", totaladvences))));
                                                                                            advcf.setText("Kshs. "+df.format(Double.parseDouble( String.format("%.2f", advncf))));

                                                                                        }
                                                                                        mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                cloan=(String)dataSnapshot.child("grouploansbf").getValue();
                                                                                                loansbf.setText("Kshs. "+df.format(Double.parseDouble(cloan)));

                                                                                                mD1.child("loans").child("approvals").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        if (dataSnapshot.hasChildren()){
                                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                                String test = snapshot.child("status").getValue().toString();
                                                                                                                String datetest = snapshot.child("dateapproved").getValue().toString();
                                                                                                                if (datetest.equals(date)) {
                                                                                                                    if (test.equals("done")) {
                                                                                                                        loantots = (String) snapshot.child("amount").getValue();
                                                                                                                        String loancash = (String) snapshot.child("amountcash").getValue();
                                                                                                                        String loanfee = snapshot.child("fee").getValue().toString();
                                                                                                                        double loanf = Double.parseDouble(loanfee);
                                                                                                                        totalloanfee = totalloanfee + loanf;
                                                                                                                        loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                                        total = total + Double.parseDouble(loantots);
                                                                                                                        cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                                        loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                                        total2 = total + (Double.parseDouble(cloan));
                                                                                                                        st = String.valueOf(total2);
                                                                                                                        loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                                        lngv.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(loancashtot))));
                                                                                                                        loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                                        lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                                        nettotal = total2 - (Double.parseDouble(st3));
                                                                                                                        loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                                    }
                                                                                                                }else {
                                                                                                                    loantots = "0";
                                                                                                                    String loancash = "0";
                                                                                                                    String loanfee = "0";
                                                                                                                    double loanf = Double.parseDouble(loanfee);
                                                                                                                    totalloanfee = totalloanfee + loanf;
                                                                                                                    loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                                    total = total + Double.parseDouble(loantots);
                                                                                                                    cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                                    loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                                    total2 = total + (Double.parseDouble(cloan));
                                                                                                                    st = String.valueOf(total2);
                                                                                                                    loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                                    lngv.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(loancashtot))));
                                                                                                                    loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                                    lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                                    nettotal = total2 - (Double.parseDouble(st3));
                                                                                                                    loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                                }
                                                                                                            }
                                                                                                        }else {
                                                                                                            loantots = "0";
                                                                                                            String loancash = "0";
                                                                                                            String loanfee = "0";
                                                                                                            double loanf = Double.parseDouble(loanfee);
                                                                                                            totalloanfee = totalloanfee + loanf;
                                                                                                            loancashtot = loancashtot + Double.parseDouble(loancash);
                                                                                                            total = total + Double.parseDouble(loantots);
                                                                                                            cashgivend = cashgivend + Double.parseDouble(loancash);
                                                                                                            loancontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total - loancashtot))));
                                                                                                            total2 = total + (Double.parseDouble(cloan));
                                                                                                            st = String.valueOf(total2);
                                                                                                            loansgvn.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total))));
                                                                                                            lngv.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(loancashtot))));
                                                                                                            loantot.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(total2))));
                                                                                                            lopaid.setText("Kshs. "+df.format(Double.parseDouble( st3)));
                                                                                                            nettotal = total2 - (Double.parseDouble(st3));
                                                                                                            loancf.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(nettotal))));
                                                                                                        }
                                                                                                        mD3.child(date).child("memberregistration").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                for (DataSnapshot snap:dataSnapshot.getChildren()){
                                                                                                                    String advfe=snap.child("amount").getValue().toString();
                                                                                                                    double advf=Double.parseDouble(advfe);
                                                                                                                    memfee=memfee+advf;
                                                                                                                    regs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(memfee))));
                                                                                                                }

                                                                                                                mD.child("schoolfees").child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        if (dataSnapshot.child("reportid").exists()){
                                                                                                                            String sfid=dataSnapshot.child("reportid").getValue().toString();

                                                                                                                            mD2.child("schoolfees").child(sfid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                    amountsf=Double.parseDouble(dataSnapshot.child("cashtooffice").getValue().toString());
                                                                                                                                    mSftot.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(amountsf))));
                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                                                                }
                                                                                                                            });
                                                                                                                        }else {
                                                                                                                            amountsf=0;
                                                                                                                            mSftot.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(amountsf))));
                                                                                                                        }
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                                    }
                                                                                                                });

                                                                                                                mD4.child(user).child(date).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        if (dataSnapshot.hasChildren()) {
                                                                                                                            if (dataSnapshot.hasChild("cashback")){
                                                                                                                                oldcb=dataSnapshot.child("cashback").getValue().toString();
                                                                                                                            }else{
                                                                                                                                oldcb="0";
                                                                                                                            }
                                                                                                                            mD.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                    cashgiven = dataSnapshot.child("cashfromoffice").getValue().toString();
                                                                                                                                    final double fin = finescm + totalloanfee + totaladvfee + memfee;
                                                                                                                                    fines.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(fin))));
                                                                                                                                    final double feecon = finescm + totalloanfee + totaladvfee + memfee;
                                                                                                                                    cashcollected = String.valueOf(totamount + fin-finescm);
                                                                                                                                    feecontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf(feecon))));
                                                                                                                                    totalcontri.setText("Kshs. "+df.format(Double.parseDouble( String.valueOf((feecon + (total - loancashtot) + (((intadvpaidamnt / 1.1) / 10) + advintrestcm))))));
                                                                                                                                    cashpaid = String.valueOf(cashgivend);
                                                                                                                                    withs.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(withds))));
                                                                                                                                    mpesas.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(mpesapay))));

                                                                                                                                    final double c1 = Double.parseDouble(cashgiven) + Double.parseDouble(cashcollected);
                                                                                                                                    double c2 = c1 - (cashgivend  + mpesapay + withds);
                                                                                                                                    netcash = String.valueOf(c2);
                                                                                                                                    nets.setText("Kshs. "+df.format(Double.parseDouble(netcash)));
                                                                                                                                    chand.setText("Kshs. "+df.format(Double.parseDouble(String.format("%.2f",c1))));
                                                                                                                                    totcoll.setText("Kshs. "+df.format(Double.parseDouble( cashcollected)));
                                                                                                                                    mMeettot.setText("Kshs. "+df.format(Double.parseDouble( netcash)));
                                                                                                                                    cashtooffice.setText("Kshs. "+df.format(Double.parseDouble(String.valueOf(Double.parseDouble(netcash)+amountsf))));



                                                                                                                                    fab2.setOnClickListener(new View.OnClickListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onClick(View v) {
                                                                                                                                            final EditText taskedit=new EditText(report2.this);
                                                                                                                                            taskedit.setHint("Enter School Fees Cash Collected.");
                                                                                                                                            taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                                                                                            taskedit.setText(String.valueOf(amountsf));
                                                                                                                                            final AlertDialog.Builder builders = new AlertDialog.Builder(report2.this);
                                                                                                                                            builders.setTitle("Save Report")
                                                                                                                                                    .setMessage("Enter Schoolfees Banking and save.")
                                                                                                                                                    .setView(taskedit)
                                                                                                                                                    .setCancelable(true)
                                                                                                                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                                                                                            thasf=taskedit.getText().toString();
                                                                                                                                                            amountsf=Double.parseDouble(thasf);
                                                                                                                                                            if (advncf==0&&tots>advpaycm){
                                                                                                                                                                Toast.makeText(report2.this,"Please Check that Carried Forward Values Compute.",Toast.LENGTH_LONG).show();
                                                                                                                                                            }else {
                                                                                                                                                                if (nettotal==0&&total2>Double.parseDouble(st3)){
                                                                                                                                                                    Toast.makeText(report2.this,"Please Check that Carried Forward Values Compute.",Toast.LENGTH_LONG).show();
                                                                                                                                                                }else {
                                                                                                                                                                    mD4.child(user).child(date).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            if (dataSnapshot.child("cashcollected").exists()){
                                                                                                                                                                                double cb=Double.parseDouble(dataSnapshot.child("cashback").getValue().toString());
                                                                                                                                                                                double cp=Double.parseDouble(dataSnapshot.child("cashpaid").getValue().toString());
                                                                                                                                                                                double cc=Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString());

                                                                                                                                                                                double ncb=cb+(Double.parseDouble(netcash)+amountsf);
                                                                                                                                                                                double ncp=cp+Double.parseDouble(cashpaid);
                                                                                                                                                                                double ncc=cc+Double.parseDouble(cashcollected);

                                                                                                                                                                                DatabaseReference cashstatus = mD4.child(user).child(date).child(fkey);
                                                                                                                                                                                cashstatus.child("cashcollected").setValue(String.valueOf(ncc));
                                                                                                                                                                                cashstatus.child("cashpaid").setValue(String.valueOf(ncp));
                                                                                                                                                                                cashstatus.child("cashback").setValue(String.valueOf(ncb));
                                                                                                                                                                                cashstatus.child("groupsgiven").child(grpkey).child("amountback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));

                                                                                                                                                                            }else {
                                                                                                                                                                                DatabaseReference cashstatus = mD4.child(user).child(date).child(fkey);
                                                                                                                                                                                cashstatus.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                                cashstatus.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                                cashstatus.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                                cashstatus.child("groupsgiven").child(grpkey).child("amountback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                            }
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });

                                                                                                                                                                    DatabaseReference cashupdates = mD.child("meetings").child(meeetid).child("cashdetails");
                                                                                                                                                                    cashupdates.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                    cashupdates.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                    cashupdates.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)));
                                                                                                                                                                    cashupdates.child("cashfromoffice").setValue(cashgiven);

                                                                                                                                                                    DatabaseReference session=mD8.child("all").child(date).push();
                                                                                                                                                                    session.child("groupName").setValue(grname);
                                                                                                                                                                    session.child("groupImage").setValue(grimage);
                                                                                                                                                                    session.child("groupid").setValue(grpkey);
                                                                                                                                                                    session.child("facilitator").setValue(nme);
                                                                                                                                                                    session.child("meetid").setValue(meeetid);


                                                                                                                                                                    DatabaseReference globalcashupdates = mD6.child(date).child(grpkey);
                                                                                                                                                                    globalcashupdates.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                    globalcashupdates.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                    globalcashupdates.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                    globalcashupdates.child("cashfromoffice").setValue(cashgiven);

                                                                                                                                                                    final DatabaseReference globalcashupdats = mD6.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);

                                                                                                                                                                    globalcashupdats.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            if (dataSnapshot.exists()){
                                                                                                                                                                                double pcc=Double.parseDouble(dataSnapshot.child("cashcollected").getValue().toString());
                                                                                                                                                                                double pcp=Double.parseDouble(dataSnapshot.child("cashpaid").getValue().toString());
                                                                                                                                                                                double pcb=Double.parseDouble(dataSnapshot.child("cashback").getValue().toString());
                                                                                                                                                                                double pcfo=Double.parseDouble(dataSnapshot.child("cashfromoffice").getValue().toString());

                                                                                                                                                                                String ncc=String.valueOf(pcc+Double.parseDouble(cashcollected));
                                                                                                                                                                                String ncp=String.valueOf(pcp+Double.parseDouble(cashpaid));
                                                                                                                                                                                String ncb=String.valueOf(pcb+Double.parseDouble(netcash)+amountsf);
                                                                                                                                                                                String ncfo=String.valueOf(pcfo+Double.parseDouble(cashgiven));

                                                                                                                                                                                globalcashupdats.child("cashcollected").setValue(ncc);
                                                                                                                                                                                globalcashupdats.child("cashpaid").setValue(ncp);
                                                                                                                                                                                globalcashupdats.child("cashback").setValue(ncb);
                                                                                                                                                                                globalcashupdats.child("cashfromoffice").setValue(ncfo);



                                                                                                                                                                            }else {
                                                                                                                                                                                globalcashupdats.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                                globalcashupdats.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                                globalcashupdats.child("cashback").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                                globalcashupdats.child("cashfromoffice").setValue(cashgiven);


                                                                                                                                                                            }

                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });



                                                                                                                                                                    mD.child("meetings").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                            DatabaseReference createreport = mD2.child("finalreport").push();
                                                                                                                                                                            String str = createreport.getKey();
                                                                                                                                                                            createreport.child("id").setValue(str);
                                                                                                                                                                            createreport.child("facilitator").setValue(nme);
                                                                                                                                                                            createreport.child("advcontribution").setValue(advcontri.getText().toString());
                                                                                                                                                                            createreport.child("loancontribution").setValue(loancontri.getText().toString());
                                                                                                                                                                            createreport.child("feesnfines").setValue(feecontri.getText().toString());
                                                                                                                                                                            createreport.child("contributiontotal").setValue(totalcontri.getText().toString());
                                                                                                                                                                            createreport.child("fines").setValue(String.valueOf(finescm + totalloanfee + totaladvfee + memfee));
                                                                                                                                                                            createreport.child("intonadv").setValue(String.format("%.2f", advinttot));
                                                                                                                                                                            createreport.child("attendance").setValue(dataSnapshot.child("attendance").getValue().toString());
                                                                                                                                                                            createreport.child("meetdate").setValue(dataSnapshot.child("date").getValue().toString());
                                                                                                                                                                            createreport.child("venue").setValue(dataSnapshot.child("venue").getValue().toString());
                                                                                                                                                                            createreport.child("cashfromoffice").setValue(dataSnapshot.child("cashfromoffice").getValue().toString());
                                                                                                                                                                            createreport.child("cashcollected").setValue(cashcollected);
                                                                                                                                                                            createreport.child("cashpaid").setValue(cashpaid);
                                                                                                                                                                            createreport.child("cashtooffice").setValue(String.valueOf(Double.parseDouble(netcash)+amountsf));
                                                                                                                                                                            createreport.child("sftot").setValue(String.valueOf(amountsf));
                                                                                                                                                                            createreport.child("meettot").setValue(netcash);
                                                                                                                                                                            createreport.child("regs").setValue(String.valueOf(memfee));
                                                                                                                                                                            createreport.child("mpesas").setValue(String.valueOf(mpesapay));
                                                                                                                                                                            createreport.child("totalLSF").setValue(st2);
                                                                                                                                                                            createreport.child("withdrawals").setValue(String.valueOf(withds));
                                                                                                                                                                            createreport.child("chand").setValue(String.format("%.2f",c1));
                                                                                                                                                                            createreport.child("insurance").setValue(String.valueOf(insupay));
                                                                                                                                                                            createreport.child("grandtotal1").setValue(String.format("%.2f", Double.parseDouble(to)));
                                                                                                                                                                            createreport.child("grandtotal2").setValue(st);
                                                                                                                                                                            createreport.child("totalAdvancesCollected").setValue(st4);
                                                                                                                                                                            createreport.child("totalLoansInstallmentsCollected").setValue(st3);
                                                                                                                                                                            createreport.child("totalAdvancesBroughtForward").setValue(String.format("%.2f", Double.parseDouble(adamount)));
                                                                                                                                                                            createreport.child("totalAdvancesGiven").setValue(String.format("%.2f", intadvpaidamnt));
                                                                                                                                                                            createreport.child("totalAdvancesCarriedForward").setValue(String.format("%.2f", advncf));
                                                                                                                                                                            createreport.child("totalLoansBroughtForward").setValue(cloan);
                                                                                                                                                                            createreport.child("totalLoansGiven").setValue(String.valueOf(total));
                                                                                                                                                                            createreport.child("totalLoansGivencash").setValue(String.valueOf(loancashtot));
                                                                                                                                                                            createreport.child("totalLoansCarriedForward").setValue(String.valueOf(nettotal));



                                                                                                                                                                            DatabaseReference reportdone = mD.child("meetings").child(meeetid);
                                                                                                                                                                            reportdone.child("reportflags").setValue("done");
                                                                                                                                                                            reportdone.child("reportid").setValue(str);

                                                                                                                                                                            DatabaseReference fieldreport=mD4.child(user).child(date).child(fkey);
                                                                                                                                                                            fieldreport.child("report").setValue(str);

                                                                                                                                                                            DatabaseReference reportdones = mD.child("groupdetails");
                                                                                                                                                                            reportdones.child("cashfromoffice").setValue("0");

                                                                                                                                                                            DatabaseReference allgroups = mD5.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
                                                                                                                                                                            allgroups.child("groupname").setValue(grname);
                                                                                                                                                                            allgroups.child("members").setValue(n1);
                                                                                                                                                                            allgroups.child("num").setValue(Integer.parseInt(num));
                                                                                                                                                                            allgroups.child("regs").setValue(String.valueOf(memfee));
                                                                                                                                                                            allgroups.child("officer").setValue(nme);
                                                                                                                                                                            allgroups.child("income").setValue(String.format("%.2f",(feecon + (total - loancashtot) + (((intadvpaidamnt / 1.1) / 10) + advintrestcm))));

                                                                                                                                                                            DatabaseReference session=mD8.child("master").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
                                                                                                                                                                            session.child("groupName").setValue(grname);
                                                                                                                                                                            session.child("groupImage").setValue(grimage);
                                                                                                                                                                            session.child("groupid").setValue(grpkey);
                                                                                                                                                                            session.child("facilitator").setValue(nme);
                                                                                                                                                                            session.child("meetid").setValue(meeetid);
                                                                                                                                                                            session.child("date").setValue(date);

                                                                                                                                                                            mD4.child(user).child(date).child(fieldid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                                                                                                                    if (dataSnapshot.child("groupsgiven").child(grpkey).child("fare").exists()){
                                                                                                                                                                                        String ffare=dataSnapshot.child("groupsgiven").child(grpkey).child("fare").getValue().toString();
                                                                                                                                                                                        final double faree=Double.parseDouble(ffare);

                                                                                                                                                                                        mD0.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                                if (dataSnapshot.child("Balance").exists()){
                                                                                                                                                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                                                                                                                                                    String balb42=dataSnapshot.child(date).child("Amountout").getValue().toString();
                                                                                                                                                                                                    double balb44=Double.parseDouble(balb4);
                                                                                                                                                                                                    double balb442=Double.parseDouble(balb42);
                                                                                                                                                                                                    double e=balb44+Double.parseDouble(netcash)+amountsf;
                                                                                                                                                                                                    double e2=balb442+Double.parseDouble(netcash)+amountsf;

                                                                                                                                                                                                    DatabaseReference newdepos=mD0.child(user);
                                                                                                                                                                                                    newdepos.child(date).child("Amountout").setValue(String.valueOf(e2));


                                                                                                                                                                                                    DatabaseReference newdepo=mD0.child(user);
                                                                                                                                                                                                    newdepo.child("Balance").setValue(String.valueOf(e-faree));
                                                                                                                                                                                                }
                                                                                                                                                                                            }

                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                                            }
                                                                                                                                                                                        });

                                                                                                                                                                                    }else {
                                                                                                                                                                                        final double faree=0.0;

                                                                                                                                                                                        mD0.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                                if (dataSnapshot.child("Balance").exists()){
                                                                                                                                                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                                                                                                                                                    String balb42=dataSnapshot.child(date).child("Amountout").getValue().toString();
                                                                                                                                                                                                    double balb44=Double.parseDouble(balb4);
                                                                                                                                                                                                    double balb442=Double.parseDouble(balb42);
                                                                                                                                                                                                    double e=balb44+Double.parseDouble(netcash)+amountsf;
                                                                                                                                                                                                    double e2=balb442+Double.parseDouble(netcash)+amountsf;

                                                                                                                                                                                                    DatabaseReference newdepos=mD0.child(user);
                                                                                                                                                                                                    newdepos.child(date).child("Amountout").setValue(String.valueOf(e2));


                                                                                                                                                                                                    DatabaseReference newdepo=mD0.child(user);
                                                                                                                                                                                                    newdepo.child("Balance").setValue(String.valueOf(e-faree));
                                                                                                                                                                                                    newdepo.child("Flag").setValue(user);
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

                                                                                                                                                                            genMeetRCT(amountsf,mpesapay,grname, lsfcm, memfee, advpaycm, loaninstcm, fin, totalloanfee, cashcollected, String.format("%.2f", intadvpaidamnt), (String.valueOf(total)), cashpaid, netcash, nme);

                                                                                                                                                                            doacoounts();
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                                }
                                                                                                                                                            }


                                                                                                                                                            dialog.dismiss();
                                                                                                                                                            fab2.setVisibility(View.GONE);
                                                                                                                                                            Toast.makeText(report2.this, "Report Saved..", Toast.LENGTH_LONG).show();
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

    private void doacoounts() {
        DatabaseReference membersfund=accounting.child("Membersfund").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        membersfund.child("name").setValue("LSF");
        membersfund.child("amount").setValue(String.valueOf(lsfcm));
        membersfund.child("type").setValue("red");
        membersfund.child("meet").setValue(meeetid);
        membersfund.child("group").setValue(grpkey);
        membersfund.child("debitac").setValue("BankCash");
        membersfund.child("creditac").setValue("Membersfund");
        membersfund.child("description").setValue("Deposit made in "+grname);
        membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bank=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        bank.child("name").setValue("LSF");
        bank.child("amount").setValue(String.valueOf(lsfcm));
        bank.child("type").setValue("blue");
        bank.child("meet").setValue(meeetid);
        bank.child("group").setValue(grpkey);
        bank.child("debitac").setValue("BankCash");
        bank.child("creditac").setValue("Membersfund");
        bank.child("description").setValue("Deposit made in "+grname);
        bank.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bankm=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        bankm.child("name").setValue("LSF");
        bankm.child("amount").setValue(String.valueOf(lsfcm));
        bankm.child("type").setValue("blue");
        bankm.child("meet").setValue(meeetid);
        bankm.child("group").setValue(grpkey);
        bankm.child("debitac").setValue("BankCash");
        bankm.child("creditac").setValue("Membersfund");
        bankm.child("description").setValue("Deposit made in "+grname);
        bankm.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference bankwm=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        bankwm.child("name").setValue("LSF");
        bankwm.child("amount").setValue(String.valueOf(withds));
        bankwm.child("type").setValue("red");
        bankwm.child("meet").setValue(meeetid);
        bankwm.child("group").setValue(grpkey);
        bankwm.child("debitac").setValue("Membersfund");
        bankwm.child("creditac").setValue("BankCash");
        bankwm.child("description").setValue("Withdrawin group "+grname);
        bankwm.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bankw2=accounting.child("Membersfund").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        bankw2.child("name").setValue("LSF");
        bankw2.child("amount").setValue(String.valueOf(withds));
        bankw2.child("type").setValue("blue");
        bankw2.child("meet").setValue(meeetid);
        bankw2.child("group").setValue(grpkey);
        bankw2.child("debitac").setValue("Membersfund");
        bankw2.child("creditac").setValue("BankCash");
        bankw2.child("description").setValue("Withdrawal in Group "+grname);
        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1=accounting.child("Feesfines").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees1.child("name").setValue("Fees and Fines");
        fees1.child("amount").setValue(String.valueOf(memfee));
        fees1.child("type").setValue("blue");
        fees1.child("meet").setValue(meeetid);
        fees1.child("group").setValue(grpkey);
        fees1.child("debitac").setValue("BankCash");
        fees1.child("creditac").setValue("Feesfines");
        fees1.child("description").setValue("Registration Fees paid in Group "+grname);
        fees1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees2.child("name").setValue("Fees and Fines");
        fees2.child("amount").setValue(String.valueOf(memfee));
        fees2.child("meet").setValue(meeetid);
        fees2.child("group").setValue(grpkey);
        fees2.child("type").setValue("blue");
        fees2.child("debitac").setValue("BankCash");
        fees2.child("creditac").setValue("Feesfines");
        fees2.child("description").setValue("Registration Fees paid in Group "+grname);
        fees2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2m=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        fees2m.child("name").setValue("Fees and Fines");
        fees2m.child("amount").setValue(String.valueOf(memfee));
        fees2m.child("meet").setValue(meeetid);
        fees2m.child("group").setValue(grpkey);
        fees2m.child("type").setValue("blue");
        fees2m.child("debitac").setValue("BankCash");
        fees2m.child("creditac").setValue("Feesfines");
        fees2m.child("description").setValue("Registration Fees paid in Group "+grname);
        fees2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1lo=accounting.child("Feesfines").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees1lo.child("name").setValue("Fees and Fines");
        fees1lo.child("amount").setValue(String.valueOf(totalloanfee));
        fees1lo.child("type").setValue("blue");
        fees1lo.child("meet").setValue(meeetid);
        fees1lo.child("group").setValue(grpkey);
        fees1lo.child("debitac").setValue("BankCash");
        fees1lo.child("creditac").setValue("Feesfines");
        fees1lo.child("description").setValue("Loan Processing Fees paid in Group "+grname);
        fees1lo.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2lo=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees2lo.child("name").setValue("Fees and Fines");
        fees2lo.child("amount").setValue(String.valueOf(totalloanfee));
        fees2lo.child("type").setValue("blue");
        fees2lo.child("meet").setValue(meeetid);
        fees2lo.child("group").setValue(grpkey);
        fees2lo.child("debitac").setValue("BankCash");
        fees2lo.child("creditac").setValue("Feesfines");
        fees2lo.child("description").setValue("Loan Processing Fee paid in Group "+grname);
        fees2lo.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2lom=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        fees2lom.child("name").setValue("Fees and Fines");
        fees2lom.child("amount").setValue(String.valueOf(totalloanfee));
        fees2lom.child("type").setValue("blue");
        fees2lom.child("meet").setValue(meeetid);
        fees2lom.child("group").setValue(grpkey);
        fees2lom.child("debitac").setValue("BankCash");
        fees2lom.child("creditac").setValue("Feesfines");
        fees2lom.child("description").setValue("Loan Processing Fee paid in Group "+grname);
        fees2lom.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1ad=accounting.child("Feesfines").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees1ad.child("name").setValue("Fees and Fines");
        fees1ad.child("amount").setValue(String.valueOf(totaladvfee));
        fees1ad.child("type").setValue("blue");
        fees1ad.child("meet").setValue(meeetid);
        fees1ad.child("group").setValue(grpkey);
        fees1ad.child("debitac").setValue("BankCash");
        fees1ad.child("creditac").setValue("Feesfines");
        fees1ad.child("description").setValue("Advance Processing Fees paid in Group "+grname);
        fees1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2ad=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees2ad.child("name").setValue("Fees and Fines");
        fees2ad.child("amount").setValue(String.valueOf(totaladvfee));
        fees2ad.child("type").setValue("blue");
        fees2ad.child("meet").setValue(meeetid);
        fees2ad.child("group").setValue(grpkey);
        fees2ad.child("debitac").setValue("BankCash");
        fees2ad.child("creditac").setValue("Feesfines");
        fees2ad.child("description").setValue("Advance Processing Fees paid in Group "+grname);
        fees2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2adm=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        fees2adm.child("name").setValue("Fees and Fines");
        fees2adm.child("amount").setValue(String.valueOf(totaladvfee));
        fees2adm.child("type").setValue("blue");
        fees2adm.child("meet").setValue(meeetid);
        fees2adm.child("group").setValue(grpkey);
        fees2adm.child("debitac").setValue("BankCash");
        fees2adm.child("creditac").setValue("Feesfines");
        fees2adm.child("description").setValue("Advance Processing Fees paid in Group "+grname);
        fees2adm.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1f=accounting.child("Feesfines").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees1f.child("name").setValue("Fees and Fines");
        fees1f.child("amount").setValue(String.valueOf(finescm));
        fees1f.child("type").setValue("blue");
        fees1f.child("meet").setValue(meeetid);
        fees1f.child("group").setValue(grpkey);
        fees1f.child("debitac").setValue("BankCash");
        fees1f.child("creditac").setValue("Feesfines");
        fees1f.child("description").setValue("Fines paid in Group "+grname);
        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2f=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        fees2f.child("name").setValue("Fees and Fines");
        fees2f.child("amount").setValue(String.valueOf(finescm));
        fees2f.child("type").setValue("blue");
        fees2f.child("meet").setValue(meeetid);
        fees2f.child("group").setValue(grpkey);
        fees2f.child("debitac").setValue("BankCash");
        fees2f.child("creditac").setValue("Feesfines");
        fees2f.child("description").setValue("Fines paid in Group "+grname);
        fees2f.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2fm=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        fees2fm.child("name").setValue("Fees and Fines");
        fees2fm.child("amount").setValue(String.valueOf(finescm));
        fees2fm.child("type").setValue("blue");
        fees2fm.child("meet").setValue(meeetid);
        fees2fm.child("group").setValue(grpkey);
        fees2fm.child("debitac").setValue("BankCash");
        fees2fm.child("creditac").setValue("Feesfines");
        fees2fm.child("description").setValue("Fines paid in Group "+grname);
        fees2fm.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl=accounting.child("Shorttermloans").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stl.child("name").setValue("Short Term Loan");
        stl.child("amount").setValue(String.valueOf(intadvpaidamnt/1.1));
        stl.child("type").setValue("blue");
        stl.child("meet").setValue(meeetid);
        stl.child("group").setValue(grpkey);
        stl.child("debitac").setValue("Shorttermloans");
        stl.child("creditac").setValue("BankCash");
        stl.child("description").setValue("Advance Given in Group "+grname);
        stl.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl1=accounting.child("Shorttermloans").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stl1.child("name").setValue("Short Term Loan");
        stl1.child("amount").setValue(String.valueOf((intadvpaidamnt/1.1)/10));
        stl1.child("type").setValue("blue");
        stl1.child("meet").setValue(meeetid);
        stl1.child("group").setValue(grpkey);
        stl1.child("debitac").setValue("Shorttermloans");
        stl1.child("creditac").setValue("BankCash");
        stl1.child("description").setValue("Interest on Advances Given "+grname);
        stl1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stl2.child("name").setValue("Short Term Loan");
        stl2.child("amount").setValue(String.valueOf(intadvpaidamnt/1.1));
        stl2.child("type").setValue("red");
        stl2.child("meet").setValue(meeetid);
        stl2.child("group").setValue(grpkey);
        stl2.child("debitac").setValue("Shorttermloans");
        stl2.child("creditac").setValue("BankCash");
        stl2.child("description").setValue("Advance Given in Group "+grname);
        stl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2m=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        stl2m.child("name").setValue("Short Term Loan");
        stl2m.child("amount").setValue(String.valueOf(intadvpaidamnt/1.1));
        stl2m.child("type").setValue("red");
        stl2m.child("meet").setValue(meeetid);
        stl2m.child("group").setValue(grpkey);
        stl2m.child("debitac").setValue("Shorttermloans");
        stl2m.child("creditac").setValue("BankCash");
        stl2m.child("description").setValue("Advance Given in Group "+grname);
        stl2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //adpaid

        DatabaseReference stlp=accounting.child("Shorttermloans").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stlp.child("name").setValue("Short Term Loan");
        stlp.child("amount").setValue(st4);
        stlp.child("type").setValue("red");
        stlp.child("meet").setValue(meeetid);
        stlp.child("group").setValue(grpkey);
        stlp.child("debitac").setValue("BankCash");
        stlp.child("creditac").setValue("Shorttermloans");
        stlp.child("description").setValue("Advance Paid in Group "+grname);
        stlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2p=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stl2p.child("name").setValue("Short Term Loan");
        stl2p.child("amount").setValue(st4);
        stl2p.child("type").setValue("blue");
        stl2p.child("meet").setValue(meeetid);
        stl2p.child("group").setValue(grpkey);
        stl2p.child("debitac").setValue("BankCash");
        stl2p.child("creditac").setValue("Shorttermloans");
        stl2p.child("description").setValue("Advance Paid in Group "+grname);
        stl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2mp=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        stl2mp.child("name").setValue("Short Term Loan");
        stl2mp.child("amount").setValue(st4);
        stl2mp.child("type").setValue("blue");
        stl2mp.child("meet").setValue(meeetid);
        stl2mp.child("group").setValue(grpkey);
        stl2mp.child("debitac").setValue("BankCash");
        stl2mp.child("creditac").setValue("Shorttermloans");
        stl2mp.child("description").setValue("Advance Paid in Group "+grname);
        stl2mp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2pia=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        stl2pia.child("name").setValue("Short Term Loan");
        stl2pia.child("amount").setValue(String.valueOf(advintrestcm));
        stl2pia.child("type").setValue("blue");
        stl2pia.child("meet").setValue(meeetid);
        stl2pia.child("group").setValue(grpkey);
        stl2pia.child("debitac").setValue("BankCash");
        stl2pia.child("creditac").setValue("Shorttermloans");
        stl2pia.child("description").setValue("Interest on Advance paid in Group "+grname);
        stl2pia.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2mpia2=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        stl2mpia2.child("name").setValue("Short Term Loan");
        stl2mpia2.child("amount").setValue(String.valueOf(advintrestcm));
        stl2mpia2.child("type").setValue("blue");
        stl2mpia2.child("meet").setValue(meeetid);
        stl2mpia2.child("group").setValue(grpkey);
        stl2mpia2.child("debitac").setValue("BankCash");
        stl2mpia2.child("creditac").setValue("Shorttermloans");
        stl2mpia2.child("description").setValue("Interest on Advance paid in Group "+grname);
        stl2mpia2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //Loan given

        DatabaseReference ltl=accounting.child("Longtermloan").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        ltl.child("name").setValue("Loan");
        ltl.child("amount").setValue(String.valueOf(loancashtot));
        ltl.child("type").setValue("blue");
        ltl.child("meet").setValue(meeetid);
        ltl.child("group").setValue(grpkey);
        ltl.child("debitac").setValue("Longtermloan");
        ltl.child("creditac").setValue("BankCash");
        ltl.child("description").setValue("Loan Given in Group "+grname);
        ltl.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        ltl2.child("name").setValue("Loan");
        ltl2.child("amount").setValue(String.valueOf(loancashtot));
        ltl2.child("type").setValue("red");
        ltl2.child("meet").setValue(meeetid);
        ltl2.child("group").setValue(grpkey);
        ltl2.child("debitac").setValue("Longtermloan");
        ltl2.child("creditac").setValue("BankCash");
        ltl2.child("description").setValue("Loan Given in Group "+grname);
        ltl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2m=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        ltl2m.child("name").setValue("Loan");
        ltl2m.child("amount").setValue(String.valueOf(loancashtot));
        ltl2m.child("type").setValue("red");
        ltl2m.child("meet").setValue(meeetid);
        ltl2m.child("group").setValue(grpkey);
        ltl2m.child("debitac").setValue("Longtermloan");
        ltl2m.child("creditac").setValue("BankCash");
        ltl2m.child("description").setValue("Loan Given in Group "+grname);
        ltl2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //loanpaid

        DatabaseReference ltlp=accounting.child("Longtermloan").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        ltlp.child("name").setValue("Loan");
        ltlp.child("amount").setValue(st3);
        ltlp.child("type").setValue("red");
        ltlp.child("meet").setValue(meeetid);
        ltlp.child("group").setValue(grpkey);
        ltlp.child("debitac").setValue("BankCash");
        ltlp.child("creditac").setValue("Longtermloan");
        ltlp.child("description").setValue("Loan Paid in Group "+grname);
        ltlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2p=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        ltl2p.child("name").setValue("Loan");
        ltl2p.child("amount").setValue(st3);
        ltl2p.child("type").setValue("blue");
        ltl2p.child("meet").setValue(meeetid);
        ltl2p.child("group").setValue(grpkey);
        ltl2p.child("debitac").setValue("BankCash");
        ltl2p.child("creditac").setValue("Longtermloan");
        ltl2p.child("description").setValue("Loan Paid in Group "+grname);
        ltl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2mp=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        ltl2mp.child("name").setValue("Loan");
        ltl2mp.child("amount").setValue(st3);
        ltl2mp.child("type").setValue("blue");
        ltl2mp.child("meet").setValue(meeetid);
        ltl2mp.child("group").setValue(grpkey);
        ltl2mp.child("debitac").setValue("BankCash");
        ltl2mp.child("creditac").setValue("Longtermloan");
        ltl2mp.child("description").setValue("Loan Paid in Group "+grname);
        ltl2mp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //

        DatabaseReference risk=accounting.child("Riskfund").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        risk.child("name").setValue("Risk Fund");
        risk.child("amount").setValue(String.valueOf(insupay));
        risk.child("type").setValue("blue");
        risk.child("meet").setValue(meeetid);
        risk.child("group").setValue(grpkey);
        risk.child("debitac").setValue("BankCash");
        risk.child("creditac").setValue("Riskfund");
        risk.child("description").setValue("Risk fund paid in Group "+grname);
        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference risk2=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        risk2.child("name").setValue("Risk Fund");
        risk2.child("amount").setValue(String.valueOf(insupay));
        risk2.child("type").setValue("blue");
        risk2.child("meet").setValue(meeetid);
        risk2.child("group").setValue(grpkey);
        risk2.child("debitac").setValue("BankCash");
        risk2.child("creditac").setValue("Riskfund");
        risk2.child("description").setValue("Risk fund paid in Group "+grname);
        risk2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference sf=accounting.child("Schoolfees").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        sf.child("name").setValue("School Fees");
        sf.child("amount").setValue(thasf);
        sf.child("type").setValue("red");
        sf.child("meet").setValue(meeetid);
        sf.child("group").setValue(grpkey);
        sf.child("debitac").setValue("BankCash");
        sf.child("creditac").setValue("Schoolfees");
        sf.child("description").setValue("School Fees Banked in Group "+grname);
        sf.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference sfb=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        sfb.child("name").setValue("School Fees");
        sfb.child("amount").setValue(thasf);
        sfb.child("type").setValue("blue");
        sfb.child("meet").setValue(meeetid);
        sfb.child("group").setValue(grpkey);
        sfb.child("debitac").setValue("BankCash");
        sfb.child("creditac").setValue("Schoolfees");
        sfb.child("description").setValue("School Fees Banked in Group "+grname);
        sfb.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference risk2m=accounting.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        risk2m.child("name").setValue("Risk Fund");
        risk2m.child("amount").setValue(String.valueOf(insupay));
        risk2m.child("type").setValue("blue");
        risk2m.child("meet").setValue(meeetid);
        risk2m.child("group").setValue(grpkey);
        risk2m.child("debitac").setValue("BankCash");
        risk2m.child("creditac").setValue("Riskfund");
        risk2m.child("description").setValue("Risk fund paid in Group "+grname);
        risk2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1=accounting.child("Income").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        inc1.child("name").setValue("Income");
        inc1.child("amount").setValue(String.valueOf(total - loancashtot));
        inc1.child("type").setValue("blue");
        inc1.child("meet").setValue(meeetid);
        inc1.child("group").setValue(grpkey);
        inc1.child("debitac").setValue("BankCash");
        inc1.child("creditac").setValue("Income");
        inc1.child("description").setValue("Loan Interest paid in Group "+grname);
        inc1.child("timestamp").setValue(ServerValue.TIMESTAMP);



        DatabaseReference inc1ad=accounting.child("Income").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(String.valueOf(advinttot + advintrestcm));
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("BankCash");
        inc1ad.child("creditac").setValue("Income");
        inc1ad.child("description").setValue("Advance Interest paid in Group "+grname);
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference mpesa=accounting.child("Mpesa").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).push();
        mpesa.child("name").setValue("Mpesa");
        mpesa.child("amount").setValue(String.valueOf(mpesapay));
        mpesa.child("type").setValue("blue");
        mpesa.child("meet").setValue(meeetid);
        mpesa.child("group").setValue(grpkey);
        mpesa.child("debitac").setValue("Mpesa");
        mpesa.child("creditac").setValue("BankCash");
        mpesa.child("description").setValue("Mpesa received in Group "+grname);
        mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference mpesam=accounting.child("Mpesa").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        mpesam.child("name").setValue("Mpesa");
        mpesam.child("amount").setValue(String.valueOf(mpesapay));
        mpesam.child("type").setValue("blue");
        mpesam.child("meet").setValue(meeetid);
        mpesam.child("group").setValue(grpkey);
        mpesam.child("debitac").setValue("Mpesa");
        mpesam.child("creditac").setValue("BankCash");
        mpesam.child("description").setValue("Mpesa received in Group "+grname);
        mpesam.child("timestamp").setValue(ServerValue.TIMESTAMP);

        doacoountsall();
    }

    private void doacoountsall() {
        DatabaseReference membersfund=accounting.child("Membersfund").child("Trans").child("all").push();
        membersfund.child("name").setValue("LSF");
        membersfund.child("amount").setValue(String.valueOf(lsfcm));
        membersfund.child("type").setValue("blue");
        membersfund.child("meet").setValue(meeetid);
        membersfund.child("group").setValue(grpkey);
        membersfund.child("debitac").setValue("BankCash");
        membersfund.child("creditac").setValue("Membersfund");
        membersfund.child("description").setValue("LSF Deposit in "+grname);
        membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bank=accounting.child("BankCash").child("Trans").child("all").push();
        bank.child("name").setValue("LSF");
        bank.child("amount").setValue(String.valueOf(lsfcm));
        bank.child("type").setValue("blue");
        bank.child("meet").setValue(meeetid);
        bank.child("group").setValue(grpkey);
        bank.child("debitac").setValue("BankCash");
        bank.child("creditac").setValue("Membersfund");
        bank.child("description").setValue("LSF Deposits in "+grname);
        bank.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bankw=accounting.child("BankCash").child("Trans").child("all").push();
        bankw.child("name").setValue("LSF");
        bankw.child("amount").setValue(String.valueOf(withds));
        bankw.child("type").setValue("red");
        bankw.child("meet").setValue(meeetid);
        bankw.child("group").setValue(grpkey);
        bankw.child("debitac").setValue("Membersfund");
        bankw.child("creditac").setValue("BankCash");
        bankw.child("description").setValue("Withdrawals in Group "+grname);
        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bankw2=accounting.child("Membersfund").child("Trans").child("all").push();
        bankw2.child("name").setValue("LSF");
        bankw2.child("amount").setValue(String.valueOf(withds));
        bankw2.child("type").setValue("red");
        bankw2.child("meet").setValue(meeetid);
        bankw2.child("group").setValue(grpkey);
        bankw2.child("debitac").setValue("Membersfund");
        bankw2.child("creditac").setValue("BankCash");
        bankw2.child("description").setValue("Withdrawals in Group "+grname);
        bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        mD.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {
                if (dataSnapshot1.child("withdrawal").exists()){

                    mD0.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Balance").exists()){
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(dataSnapshot1.child("withdrawal").getValue().toString());
                                DatabaseReference newdepo=mD0.child(user);
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }else {
                                String balb4="0";
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(dataSnapshot1.child("withdrawal").getValue().toString());
                                DatabaseReference newdepo=mD0.child(user);
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference rrr=mD.child("groupdetails");
                    rrr.child("withdrawal").removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference fees1=accounting.child("Feesfines").child("Trans").child("all").push();
        fees1.child("name").setValue("Fees and Fines");
        fees1.child("amount").setValue(String.valueOf(memfee));
        fees1.child("type").setValue("blue");
        fees1.child("meet").setValue(meeetid);
        fees1.child("group").setValue(grpkey);
        fees1.child("debitac").setValue("BankCash");
        fees1.child("creditac").setValue("Feesfines");
        fees1.child("description").setValue("Registration Fees for"+grname);
        fees1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2=accounting.child("BankCash").child("Trans").child("all").push();
        fees2.child("name").setValue("Fees and Fines");
        fees2.child("amount").setValue(String.valueOf(memfee));
        fees2.child("meet").setValue(meeetid);
        fees2.child("group").setValue(grpkey);
        fees2.child("type").setValue("blue");
        fees2.child("debitac").setValue("BankCash");
        fees2.child("creditac").setValue("Feesfines");
        fees2.child("description").setValue("Registration Fees for "+grname);
        fees2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1lo=accounting.child("Feesfines").child("Trans").child("all").push();
        fees1lo.child("name").setValue("Fees and Fines");
        fees1lo.child("amount").setValue(String.valueOf(totalloanfee));
        fees1lo.child("type").setValue("blue");
        fees1lo.child("meet").setValue(meeetid);
        fees1lo.child("group").setValue(grpkey);
        fees1lo.child("debitac").setValue("BankCash");
        fees1lo.child("creditac").setValue("Feesfines");
        fees1lo.child("description").setValue("Loan Processing Fees "+grname);
        fees1lo.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2lo=accounting.child("BankCash").child("Trans").child("all").push();
        fees2lo.child("name").setValue("Fees and Fines");
        fees2lo.child("amount").setValue(String.valueOf(totalloanfee));
        fees2lo.child("type").setValue("blue");
        fees2lo.child("meet").setValue(meeetid);
        fees2lo.child("group").setValue(grpkey);
        fees2lo.child("debitac").setValue("BankCash");
        fees2lo.child("creditac").setValue("Feesfines");
        fees2lo.child("description").setValue("Loan Processing Fee "+grname);
        fees2lo.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1ad=accounting.child("Feesfines").child("Trans").child("all").push();
        fees1ad.child("name").setValue("Fees and Fines");
        fees1ad.child("amount").setValue(String.valueOf(totaladvfee));
        fees1ad.child("type").setValue("blue");
        fees1ad.child("meet").setValue(meeetid);
        fees1ad.child("group").setValue(grpkey);
        fees1ad.child("debitac").setValue("BankCash");
        fees1ad.child("creditac").setValue("Feesfines");
        fees1ad.child("description").setValue("Advance Processing Fees "+grname);
        fees1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2ad=accounting.child("BankCash").child("Trans").child("all").push();
        fees2ad.child("name").setValue("Fees and Fines");
        fees2ad.child("amount").setValue(String.valueOf(totaladvfee));
        fees2ad.child("type").setValue("blue");
        fees2ad.child("meet").setValue(meeetid);
        fees2ad.child("group").setValue(grpkey);
        fees2ad.child("debitac").setValue("BankCash");
        fees2ad.child("creditac").setValue("Feesfines");
        fees2ad.child("description").setValue("Advance Processing Fees "+grname);
        fees2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1f=accounting.child("Feesfines").child("Trans").child("all").push();
        fees1f.child("name").setValue("Fees and Fines");
        fees1f.child("amount").setValue(String.valueOf(finescm));
        fees1f.child("type").setValue("blue");
        fees1f.child("meet").setValue(meeetid);
        fees1f.child("group").setValue(grpkey);
        fees1f.child("debitac").setValue("BankCash");
        fees1f.child("creditac").setValue("Feesfines");
        fees1f.child("description").setValue("Fines paid in Group "+grname);
        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2f=accounting.child("BankCash").child("Trans").child("all").push();
        fees2f.child("name").setValue("Fees and Fines");
        fees2f.child("amount").setValue(String.valueOf(finescm));
        fees2f.child("type").setValue("blue");
        fees2f.child("meet").setValue(meeetid);
        fees2f.child("group").setValue(grpkey);
        fees2f.child("debitac").setValue("BankCash");
        fees2f.child("creditac").setValue("Feesfines");
        fees2f.child("description").setValue("Fines paid in Group "+grname);
        fees2f.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl=accounting.child("Shorttermloans").child("Trans").child("all").push();
        stl.child("name").setValue("Short Term Loan");
        stl.child("amount").setValue(String.format("%.2f",(intadvpaidamnt/1.1)));
        stl.child("type").setValue("blue");
        stl.child("meet").setValue(meeetid);
        stl.child("group").setValue(grpkey);
        stl.child("debitac").setValue("Shorttermloans");
        stl.child("creditac").setValue("BankCash");
        stl.child("description").setValue("Advance Given in Group "+grname);
        stl.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl1=accounting.child("Shorttermloans").child("Trans").child("all").push();
        stl1.child("name").setValue("Short Term Loan");
        stl1.child("amount").setValue(String.format("%.2f",(intadvpaidamnt/1.1)/10));
        stl1.child("type").setValue("blue");
        stl1.child("meet").setValue(meeetid);
        stl1.child("group").setValue(grpkey);
        stl1.child("debitac").setValue("Shorttermloans");
        stl1.child("creditac").setValue("BankCash");
        stl1.child("description").setValue("Interest on Advances given "+grname);
        stl1.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2=accounting.child("BankCash").child("Trans").child("all").push();
        stl2.child("name").setValue("Short Term Loan");
        stl2.child("amount").setValue(String.format("%.2f",(intadvpaidamnt/1.1)/10));
        stl2.child("type").setValue("red");
        stl2.child("meet").setValue(meeetid);
        stl2.child("group").setValue(grpkey);
        stl2.child("debitac").setValue("Shorttermloans");
        stl2.child("creditac").setValue("BankCash");
        stl2.child("description").setValue("Advance Given in Group "+grname);
        stl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //advancepaid

        DatabaseReference stlp=accounting.child("Shorttermloans").child("Trans").child("all").push();
        stlp.child("name").setValue("Short Term Loan");
        stlp.child("amount").setValue(st4);
        stlp.child("type").setValue("red");
        stlp.child("meet").setValue(meeetid);
        stlp.child("group").setValue(grpkey);
        stlp.child("debitac").setValue("BankCash");
        stlp.child("creditac").setValue("Shorttermloans");
        stlp.child("description").setValue("Advance Paid in Group "+grname);
        stlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2p=accounting.child("BankCash").child("Trans").child("all").push();
        stl2p.child("name").setValue("Short Term Loan");
        stl2p.child("amount").setValue(st4);
        stl2p.child("type").setValue("blue");
        stl2p.child("meet").setValue(meeetid);
        stl2p.child("group").setValue(grpkey);
        stl2p.child("debitac").setValue("BankCash");
        stl2p.child("creditac").setValue("Shorttermloans");
        stl2p.child("description").setValue("Advance Paid in Group "+grname);
        stl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2pc=accounting.child("BankCash").child("Trans").child("all").push();
        stl2pc.child("name").setValue("Short Term Loan");
        stl2pc.child("amount").setValue(String.valueOf(advintrestcm));
        stl2pc.child("type").setValue("blue");
        stl2pc.child("meet").setValue(meeetid);
        stl2pc.child("group").setValue(grpkey);
        stl2pc.child("debitac").setValue("BankCash");
        stl2pc.child("creditac").setValue("Shorttermloans");
        stl2pc.child("description").setValue("Interest on Advance paid in Group "+grname);
        stl2pc.child("timestamp").setValue(ServerValue.TIMESTAMP);

        //Loans givn

        DatabaseReference ltl=accounting.child("Longtermloan").child("Trans").child("all").push();
        ltl.child("name").setValue("Loan");
        ltl.child("amount").setValue(String.valueOf(loancashtot));
        ltl.child("type").setValue("blue");
        ltl.child("meet").setValue(meeetid);
        ltl.child("group").setValue(grpkey);
        ltl.child("debitac").setValue("Longtermloan");
        ltl.child("creditac").setValue("BankCash");
        ltl.child("description").setValue("Loan Given in Group "+grname);
        ltl.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc2m=accounting.child("Longtermloan").child("Trans").child("all").push();
        inc2m.child("name").setValue("Income");
        inc2m.child("amount").setValue(String.valueOf(total-loancashtot));
        inc2m.child("type").setValue("blue");
        inc2m.child("meet").setValue(meeetid);
        inc2m.child("group").setValue(grpkey);
        inc2m.child("debitac").setValue("Longtermloan");
        inc2m.child("creditac").setValue("Income");
        inc2m.child("description").setValue("Interest for Loans given in "+grname);
        inc2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2=accounting.child("BankCash").child("Trans").child("all").push();
        ltl2.child("name").setValue("Loan");
        ltl2.child("amount").setValue(String.valueOf(loancashtot));
        ltl2.child("type").setValue("red");
        ltl2.child("meet").setValue(meeetid);
        ltl2.child("group").setValue(grpkey);
        ltl2.child("debitac").setValue("Longtermloan");
        ltl2.child("creditac").setValue("BankCash");
        ltl2.child("description").setValue("Loan Given in Group "+grname);
        ltl2.child("timestamp").setValue(ServerValue.TIMESTAMP);


        //loan paid

        DatabaseReference ltlp=accounting.child("Longtermloan").child("Trans").child("all").push();
        ltlp.child("name").setValue("Loan");
        ltlp.child("amount").setValue(st3);
        ltlp.child("type").setValue("red");
        ltlp.child("meet").setValue(meeetid);
        ltlp.child("group").setValue(grpkey);
        ltlp.child("debitac").setValue("BankCash");
        ltlp.child("creditac").setValue("Longtermloan");
        ltlp.child("description").setValue("Loan Paid in Group "+grname);
        ltlp.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference ltl2p=accounting.child("BankCash").child("Trans").child("all").push();
        ltl2p.child("name").setValue("Loan");
        ltl2p.child("amount").setValue(st3);
        ltl2p.child("type").setValue("blue");
        ltl2p.child("meet").setValue(meeetid);
        ltl2p.child("group").setValue(grpkey);
        ltl2p.child("debitac").setValue("BankCash");
        ltl2p.child("creditac").setValue("Longtermloan");
        ltl2p.child("description").setValue("Loan Paid in Group "+grname);
        ltl2p.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference risk=accounting.child("Riskfund").child("Trans").child("all").push();
        risk.child("name").setValue("Risk Fund");
        risk.child("amount").setValue(String.valueOf(insupay));
        risk.child("type").setValue("blue");
        risk.child("meet").setValue(meeetid);
        risk.child("group").setValue(grpkey);
        risk.child("debitac").setValue("BankCash");
        risk.child("creditac").setValue("Riskfund");
        risk.child("description").setValue("Risk fund paid in Group "+grname);
        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference risk2=accounting.child("BankCash").child("Trans").child("all").push();
        risk2.child("name").setValue("Risk Fund");
        risk2.child("amount").setValue(String.valueOf(insupay));
        risk2.child("type").setValue("blue");
        risk2.child("meet").setValue(meeetid);
        risk2.child("group").setValue(grpkey);
        risk2.child("debitac").setValue("BankCash");
        risk2.child("creditac").setValue("Riskfund");
        risk2.child("description").setValue("Risk fund paid in Group "+grname);
        risk2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1=accounting.child("Income").child("Trans").child("all").push();
        inc1.child("name").setValue("Income");
        inc1.child("amount").setValue(String.valueOf(total - loancashtot));
        inc1.child("type").setValue("blue");
        inc1.child("meet").setValue(meeetid);
        inc1.child("group").setValue(grpkey);
        inc1.child("debitac").setValue("Longtermloan");
        inc1.child("creditac").setValue("Income");
        inc1.child("description").setValue("Loan Interest paid in Group "+grname);
        inc1.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference inc1ad=accounting.child("Income").child("Trans").child("all").push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(String.valueOf(advinttot + advintrestcm));
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("Shorttermloans");
        inc1ad.child("creditac").setValue("Income");
        inc1ad.child("description").setValue("Advance Interest paid in Group "+grname);
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference mpesa=accounting.child("Mpesa").child("Trans").child("all").push();
        mpesa.child("name").setValue("Mpesa");
        mpesa.child("amount").setValue(String.valueOf(mpesapay));
        mpesa.child("type").setValue("blue");
        mpesa.child("meet").setValue(meeetid);
        mpesa.child("group").setValue(grpkey);
        mpesa.child("debitac").setValue("Mpesa");
        mpesa.child("creditac").setValue("BankCash");
        mpesa.child("description").setValue("Mpesa received in Group "+grname);
        mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference mpesab=accounting.child("BankCash").child("Trans").child("all").push();
        mpesab.child("name").setValue("Mpesa");
        mpesab.child("amount").setValue(String.valueOf(mpesapay));
        mpesab.child("type").setValue("blue");
        mpesab.child("meet").setValue(meeetid);
        mpesab.child("group").setValue(grpkey);
        mpesab.child("debitac").setValue("Mpesa");
        mpesab.child("creditac").setValue("BankCash");
        mpesab.child("description").setValue("Mpesa received in Group "+grname);
        mpesab.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference unhandedb=accounting.child("Unhanded").child("Trans").child("all").push();
        unhandedb.child("name").setValue("Unhanded");
        unhandedb.child("amount").setValue(netcash);
        unhandedb.child("type").setValue("blue");
        unhandedb.child("meet").setValue(meeetid);
        unhandedb.child("group").setValue(grpkey);
        unhandedb.child("debitac").setValue("Unhanded");
        unhandedb.child("creditac").setValue(nme);
        unhandedb.child("description").setValue(grname+" banking."+" by "+nme);
        unhandedb.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference unhandedbsf=accounting.child("Unhanded").child("Trans").child("all").push();
        unhandedbsf.child("name").setValue("Unhanded");
        unhandedbsf.child("amount").setValue(String.valueOf(amountsf));
        unhandedbsf.child("type").setValue("blue");
        unhandedbsf.child("meet").setValue(meeetid);
        unhandedbsf.child("group").setValue(grpkey);
        unhandedbsf.child("debitac").setValue("Unhanded");
        unhandedbsf.child("creditac").setValue("Schoolfees");
        unhandedbsf.child("description").setValue(grname+" Schoolfees banking."+" by "+nme);
        unhandedbsf.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference sf=accounting.child("Schoolfees").child("Trans").child("all").push();
        sf.child("name").setValue("School Fees");
        sf.child("amount").setValue(thasf);
        sf.child("type").setValue("red");
        sf.child("meet").setValue(meeetid);
        sf.child("group").setValue(grpkey);
        sf.child("debitac").setValue("BankCash");
        sf.child("creditac").setValue("Schoolfees");
        sf.child("description").setValue("School Fees Banked in Group "+grname+" by "+nme);
        sf.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference sfb=accounting.child("BankCash").child("Trans").child("all").push();
        sfb.child("name").setValue("School Fees");
        sfb.child("amount").setValue(thasf);
        sfb.child("type").setValue("blue");
        sfb.child("meet").setValue(meeetid);
        sfb.child("group").setValue(grpkey);
        sfb.child("debitac").setValue("BankCash");
        sfb.child("creditac").setValue("Riskfund");
        sfb.child("description").setValue("School Fees Banked in Group "+grname+" by "+nme);
        sfb.child("timestamp").setValue(ServerValue.TIMESTAMP);


        savememberstat();

        employeeimprest();
    }

    private void employeeimprest() {
        DatabaseReference membersfund=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        membersfund.child("name").setValue("LSF");
        membersfund.child("amount").setValue(String.valueOf(lsfcm));
        membersfund.child("type").setValue("blue");
        membersfund.child("meet").setValue(meeetid);
        membersfund.child("group").setValue(grpkey);
        membersfund.child("debitac").setValue(nme);
        membersfund.child("creditac").setValue("-");
        membersfund.child("description").setValue("LSF Deposit in "+grname);
        membersfund.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference bankw=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        bankw.child("name").setValue("LSF");
        bankw.child("amount").setValue(String.valueOf(withds));
        bankw.child("type").setValue("red");
        bankw.child("meet").setValue(meeetid);
        bankw.child("group").setValue(grpkey);
        bankw.child("debitac").setValue("-");
        bankw.child("creditac").setValue(nme);
        bankw.child("description").setValue("Withdrawals in Group "+grname);
        bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference fees1=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        fees1.child("name").setValue("Fees and Fines");
        fees1.child("amount").setValue(String.valueOf(memfee));
        fees1.child("type").setValue("blue");
        fees1.child("meet").setValue(meeetid);
        fees1.child("group").setValue(grpkey);
        fees1.child("debitac").setValue(nme);
        fees1.child("creditac").setValue("Feesfines");
        fees1.child("description").setValue("Registration Fees for"+grname);
        fees1.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference fees1lo=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        fees1lo.child("name").setValue("Fees and Fines");
        fees1lo.child("amount").setValue(String.valueOf(totalloanfee));
        fees1lo.child("type").setValue("blue");
        fees1lo.child("meet").setValue(meeetid);
        fees1lo.child("group").setValue(grpkey);
        fees1lo.child("debitac").setValue(nme);
        fees1lo.child("creditac").setValue("Feesfines");
        fees1lo.child("description").setValue("Loan Processing Fees "+grname);
        fees1lo.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference fees1ad=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        fees1ad.child("name").setValue("Fees and Fines");
        fees1ad.child("amount").setValue(String.valueOf(totaladvfee));
        fees1ad.child("type").setValue("blue");
        fees1ad.child("meet").setValue(meeetid);
        fees1ad.child("group").setValue(grpkey);
        fees1ad.child("debitac").setValue(nme);
        fees1ad.child("creditac").setValue("Feesfines");
        fees1ad.child("description").setValue("Advance Processing Fees "+grname);
        fees1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference fees1f=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        fees1f.child("name").setValue("Fees and Fines");
        fees1f.child("amount").setValue(String.valueOf(finescm));
        fees1f.child("type").setValue("blue");
        fees1f.child("meet").setValue(meeetid);
        fees1f.child("group").setValue(grpkey);
        fees1f.child("debitac").setValue(nme);
        fees1f.child("creditac").setValue("Feesfines");
        fees1f.child("description").setValue("Fines paid in Group "+grname);
        fees1f.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference stl=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        stl.child("name").setValue("Short Term Loan");
        stl.child("amount").setValue(String.format("%.2f",(intadvpaidamnt/1.1)));
        stl.child("type").setValue("blue");
        stl.child("meet").setValue(meeetid);
        stl.child("group").setValue(grpkey);
        stl.child("debitac").setValue("-");
        stl.child("creditac").setValue(nme);
        stl.child("description").setValue("Advance Given in Group "+grname);
        stl.child("timestamp").setValue(ServerValue.TIMESTAMP);


        //advancepaid

        DatabaseReference stlp=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        stlp.child("name").setValue("Short Term Loan");
        stlp.child("amount").setValue(st4);
        stlp.child("type").setValue("red");
        stlp.child("meet").setValue(meeetid);
        stlp.child("group").setValue(grpkey);
        stlp.child("debitac").setValue(nme);
        stlp.child("creditac").setValue("-");
        stlp.child("description").setValue("Advance Paid in Group "+grname);
        stlp.child("timestamp").setValue(ServerValue.TIMESTAMP);



        DatabaseReference stl2pc=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        stl2pc.child("name").setValue("Short Term Loan");
        stl2pc.child("amount").setValue(String.valueOf(advintrestcm));
        stl2pc.child("type").setValue("blue");
        stl2pc.child("meet").setValue(meeetid);
        stl2pc.child("group").setValue(grpkey);
        stl2pc.child("debitac").setValue(nme);
        stl2pc.child("creditac").setValue("Shorttermloans");
        stl2pc.child("description").setValue("Interest on Advance paid in Group "+grname);
        stl2pc.child("timestamp").setValue(ServerValue.TIMESTAMP);


        //loan paid

        DatabaseReference ltlp=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        ltlp.child("name").setValue("Loan");
        ltlp.child("amount").setValue(st3);
        ltlp.child("type").setValue("red");
        ltlp.child("meet").setValue(meeetid);
        ltlp.child("group").setValue(grpkey);
        ltlp.child("debitac").setValue(nme);
        ltlp.child("creditac").setValue("-");
        ltlp.child("description").setValue("Loan Paid in Group "+grname);
        ltlp.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference risk=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        risk.child("name").setValue("Risk Fund");
        risk.child("amount").setValue(String.valueOf(insupay));
        risk.child("type").setValue("blue");
        risk.child("meet").setValue(meeetid);
        risk.child("group").setValue(grpkey);
        risk.child("debitac").setValue(nme);
        risk.child("creditac").setValue("Riskfund");
        risk.child("description").setValue("Risk fund paid in Group "+grname);
        risk.child("timestamp").setValue(ServerValue.TIMESTAMP);


        DatabaseReference mpesa=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        mpesa.child("name").setValue("Mpesa");
        mpesa.child("amount").setValue(String.valueOf(mpesapay));
        mpesa.child("type").setValue("blue");
        mpesa.child("meet").setValue(meeetid);
        mpesa.child("group").setValue(grpkey);
        mpesa.child("debitac").setValue("-");
        mpesa.child("creditac").setValue(nme);
        mpesa.child("description").setValue("Mpesa received in Group "+grname);
        mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference sf=accounting.child("Imprests").child(nme).child(date).child("Trans").child("all").push();
        sf.child("name").setValue("School Fees");
        sf.child("amount").setValue(thasf);
        sf.child("type").setValue("red");
        sf.child("meet").setValue(meeetid);
        sf.child("group").setValue(grpkey);
        sf.child("debitac").setValue(nme);
        sf.child("creditac").setValue("-");
        sf.child("description").setValue("School Fees Banked in Group "+grname);
        sf.child("timestamp").setValue(ServerValue.TIMESTAMP);

    }

    private void savememberstat() {
        final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
        final DatabaseReference mem=FirebaseDatabase.getInstance().getReference().child("members");
        mD.child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final DatabaseReference abf = mD1.child("advances");
                final DatabaseReference bf = mD1.child("loans");
                final DatabaseReference allgroups = mD5.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpkey);
                final DatabaseReference updatefinances=mD1;
                final DatabaseReference master = mD7.child(grpkey);
                for (final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    mem.child(dataSnapshot1.child("id").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name=dataSnapshot1.child("memberid").getValue().toString();
                            String saving=dataSnapshot1.child("lsfcf").getValue().toString();
                            String advance=dataSnapshot1.child("advcf").getValue().toString();
                            String loan=dataSnapshot1.child("loancf").getValue().toString();

                            lsfs=lsfs+Double.parseDouble(saving);
                            loans=loans+Double.parseDouble(loan);
                            advs=advs+Double.parseDouble(advance);

                            memstat.child(dataSnapshot1.child("id").getValue().toString()).child("name").setValue(name);
                            memstat.child(dataSnapshot1.child("id").getValue().toString()).child("savings").setValue(saving);
                            memstat.child(dataSnapshot1.child("id").getValue().toString()).child("advance").setValue(advance);
                            memstat.child(dataSnapshot1.child("id").getValue().toString()).child("loans").setValue(loan);

                            mem.child(dataSnapshot1.child("id").getValue().toString()).child("accounttemptrs").removeValue();


                            abf.child("currentadvancebf").setValue(String.format("%.2f",advs));
                            bf.child("currentloanbf").setValue(String.valueOf(loans));


                            updatefinances.child("advances").child("currentadvance").setValue(String.format("%.2f", advs));
                            updatefinances.child("loans").child("currentloan").setValue(String.valueOf(loans));
                            updatefinances.child("savings").child("totalsavings").setValue(String.valueOf(lsfs));


                            allgroups.child("advance").setValue(String.format("%.2f", advs));
                            allgroups.child("loans").setValue(String.valueOf(loans));
                            allgroups.child("lsf").setValue(String.valueOf(lsfs));


                            master.child("totaladvances").setValue(String.format("%.2f", advs));
                            master.child("totalloans").setValue(String.valueOf(loans));
                            master.child("totalsavings").setValue(String.valueOf(lsfs));

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

    private void genMeetRCT(double amountsf,double mpesa,String grpname,double lsfcm,double memfee,double advpaycm,double loaninstcm,double fin,double totalloanfee,String cashcollected,String advpayd,String loanrepaid, String cashpaid, String netcash,String nme) {
        final DatabaseReference nr=mDatabase.child(meeetid).child("MeetingTransferReciept");
        nr.child("date").setValue(date);
        nr.child("groupname").setValue(grpname);
        nr.child("lsfcm").setValue(String.valueOf(lsfcm));
        nr.child("regfees").setValue(String.valueOf(memfee));
        nr.child("other").setValue("0");
        nr.child("loanandadv").setValue(String.valueOf(advpaycm+loaninstcm));
        nr.child("finesandpenalties").setValue(String.valueOf(fin));
        nr.child("loanprocfee").setValue(String.valueOf(totalloanfee));
        nr.child("collcash").setValue(cashcollected);
        nr.child("advgive").setValue(advpayd);
        nr.child("loangvn").setValue(loanrepaid);
        nr.child("totapaymnt").setValue(cashpaid);
        nr.child("netcash").setValue(netcash);
        nr.child("officer").setValue(nme);
        nr.child("mpesa").setValue(String.valueOf(mpesa));
        nr.child("sfees").setValue(String.valueOf(amountsf));
    }
}
