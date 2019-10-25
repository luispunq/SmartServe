package com.example.london.smartserve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.LruCache;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class perfomanceanalysis extends AppCompatActivity {
    private RecyclerView mTrans;
    private DatabaseReference mDatabase,mD,mD2,mD3,mD4,mAcc,mD6,accounting;
    private String grpkey,meetid,date,gname;
    private double totamount=0,lsfcm=0,loaninstcm=0,advpaycm=0,intadvpaidamnt=0,intadvbf=0,riskcm=0,total=0,wcommcm=0,advncf=0,nettotal=0,advintrestcm=0,loancashtot=0,
            finescm=0,advinttot=0,cashtotal=0,cashgivend=0,totalloanfee=0
            ,totaladvfee=0,memfee=0,ad=0,lo=0,sa=0,lg=0,ag=0,sacff=0,locff=0,mpesa=0,adcff=0;
    private String st2=null,st4=null,st3=null,st1=null,st5=null,st6=null,dirpath,filename="image";
    private TextView tamnt,tlsf,tadpaid,tadgn,tlpaid,tlgvn,tadvint,lsfcf,lcf,adcf,pGname,pFac,pDate,mMpesa;
    private LinearLayout mtopdetails;
    private FloatingActionButton fab,fabe;
    private int height;
    private ByteArrayOutputStream bytes,bytesw;
    private Bitmap b1,b2;
    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    private String mdate,memuid=null,defaultstring="Default";
    private String fname,oldgrpsaving,oldgrploan,oldgrpadv;
    private AlertDialog alert11;
    private ArrayList<Integer> selectedItems;
    StringBuilder stringBuilder = new StringBuilder();


    DecimalFormat df = new DecimalFormat("##,###,###.#");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfomanceanalysis);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("gkey");
        meetid = extras.getString("meet");
        date = extras.getString("date");
        tamnt = findViewById(R.id.totrepaid);
        mtopdetails=findViewById(R.id.topdetails);
        pGname=findViewById(R.id.pgname);
        pFac=findViewById(R.id.pfac);
        pDate=findViewById(R.id.pdate);
        tlsf = findViewById(R.id.totlsf);
        lsfcf=findViewById(R.id.totlsfcf);
        lcf=findViewById(R.id.totloancf);
        adcf=findViewById(R.id.totadvcf);
        tadpaid = findViewById(R.id.totadvinst);
        tadgn = findViewById(R.id.totadvgvn);
        tlpaid = findViewById(R.id.totloaninst);
        tlgvn = findViewById(R.id.totloangvn);
        tadvint = findViewById(R.id.totinterest);
        mMpesa=findViewById(R.id.totmpesa);
        mTrans = findViewById(R.id.transactionlist);
        fab=findViewById(R.id.floatingActionButton3);
        mTrans.setHasFixedSize(true);
        mTrans.setItemViewCacheSize(50);
        mTrans.setDrawingCacheEnabled(true);
        mTrans.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mTrans.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("transactions").child(meetid);

        mD = FirebaseDatabase.getInstance().getReference().child("Employees");
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD2 = FirebaseDatabase.getInstance().getReference().child("defaults");

        mD3 = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);

        mD4=FirebaseDatabase.getInstance().getReference().child("members");

        mAcc=FirebaseDatabase.getInstance().getReference().child("Accounting");






        mTrans.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mTrans.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen(mTrans);
            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String amount=(String)snapshot.child("totalamount").getValue();
                    String lsfamount=(String)snapshot.child("lsf").getValue();
                    String loaninstamount=(String)snapshot.child("loaninstallments").getValue();
                    String advpayamount=(String)snapshot.child("advancepayment").getValue();
                    String advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                    String loagiv=(String)snapshot.child("loangvn").getValue();
                    String advgi=(String)snapshot.child("advgvn").getValue();
                    String sacf=(String)snapshot.child("lsfcf").getValue();
                    String locf=(String)snapshot.child("loancf").getValue();
                    String adcfs=(String)snapshot.child("advcf").getValue();
                    String mpesas=(String)snapshot.child("Mpesa").getValue();

                    double val1=Double.parseDouble(amount);
                    double val2=Double.parseDouble(lsfamount);
                    double val3=Double.parseDouble(loaninstamount);
                    double val4=Double.parseDouble(advpayamount);
                    double val5=Double.parseDouble(advpayintrest);
                    double val6=Double.parseDouble(loagiv);
                    double val7=Double.parseDouble(advgi);
                    double val8=Double.parseDouble(sacf);
                    double val9=Double.parseDouble(locf);
                    double val10=Double.parseDouble(adcfs);
                    double val11=Double.parseDouble(mpesas);


                    advintrestcm=advintrestcm+val5;
                    totamount=totamount+val1;
                    lsfcm=lsfcm+val2;
                    loaninstcm=loaninstcm+val3;
                    advpaycm=advpaycm+val4;
                    lg=lg+val6;
                    ag=ag+val7;
                    sacff=sacff+val8;
                    locff=locff+val9;
                    adcff=adcff+val10;
                    mpesa=mpesa+val11;

                    st1=String.valueOf(totamount);
                    st2=String.valueOf(lsfcm);
                    st3=String.valueOf(loaninstcm);
                    st4=String.valueOf(advpaycm);
                    st5=String.valueOf(totamount);
                    st6=String.valueOf(advintrestcm);

                    tamnt.setText("Kshs. "+st5);
                    tamnt.setTextSize(20);
                    tlsf.setText("Kshs. "+st2);
                    tlsf.setTextSize(20);
                    tadpaid.setText("Kshs. "+st4);
                    tadpaid.setTextSize(20);
                    tlpaid.setText("Kshs. "+st3);
                    tlpaid.setTextSize(20);
                    tadvint.setText("Kshs. "+st6);
                    tadvint.setTextSize(20);
                    tadgn.setText("Kshs. "+String.valueOf(ag));
                    tadgn.setTextSize(20);
                    tlgvn.setText("Kshs. "+String.valueOf(lg));
                    tlgvn.setTextSize(20);
                    lsfcf.setText("Kshs. "+String.valueOf(sacff));
                    lsfcf.setTextSize(20);
                    lcf.setText("Kshs. "+String.valueOf(locff));
                    lcf.setTextSize(20);
                    adcf.setText("Kshs. "+String.valueOf(adcff));
                    adcf.setTextSize(20);
                    mMpesa.setText("Kshs. "+String.valueOf(mpesa));
                    mMpesa.setTextSize(20);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD3.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdate=dataSnapshot.child("date").getValue().toString();
                pDate.setText("Date: "+mdate);
                mD.child(dataSnapshot.child("facid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fname=dataSnapshot.child("name").getValue().toString();
                        pFac.setText("Facilitator: "+fname);
                        pGname.setText("Group Name: "+gname);
                        pGname.setTextSize(20);
                        pDate.setTextSize(20);
                        pFac.setTextSize(20);
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


        mD3.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   gname=dataSnapshot.child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<reporttrans,transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<reporttrans, transViewHolder>(

                reporttrans.class,
                R.layout.reporttrans_row,
                transViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final transViewHolder viewHolder, final reporttrans model, int position) {

                final String rk=getRef(position).getKey();

                viewHolder.setLoangivn(model.getLoangvn());
                viewHolder.setLoancf(model.getLoancf());
                viewHolder.setAdvccf(model.getAdvcf());
                viewHolder.setLsfcf(model.getLsfcf());
                viewHolder.setAdvcgvn(model.getAdvgvn());
                viewHolder.setName(model.getMemberid());
                viewHolder.setTotalRepaid(model.getTotalamount());
                viewHolder.setLSF(model.getLsf());
                viewHolder.setAdvance(model.getAdvancepayment());
                viewHolder.setLoanInst(model.getLoaninstallments());
                viewHolder.setPaymode(model.getPaymentmode());
                viewHolder.setInsu1(model.getAdvanceinterest());

                mD3.child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child("reportid").exists()){

                            viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {

                                    selectedItems = new ArrayList();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(perfomanceanalysis.this);
                                    // Set the dialog title
                                    builder.setTitle("Book Default: "+model.getMemberid())
                                            .setMultiChoiceItems(R.array.defaulttypes, null,
                                                    new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which,
                                                                            boolean isChecked) {
                                                            if (isChecked) {
                                                                // If the user checked the item, add it to the selected items
                                                                selectedItems.add(which);
                                                            } else if (selectedItems.contains(which)) {
                                                                // Else, if the item is already in the array, remove it
                                                                selectedItems.remove(Integer.valueOf(which));
                                                            }
                                                        }
                                                    })
                                            // Set the action buttons
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User clicked OK, so save the selectedItems results somewhere
                                                    // or return them to the component that opened the dialog
                                                    stringBuilder.append(defaultstring);
                                                    if (!selectedItems.isEmpty()){
                                                        if (selectedItems.contains(0)){
                                                            stringBuilder.append("-savings");
                                                        }
                                                        if (selectedItems.contains(1)){
                                                            stringBuilder.append("-advance");

                                                        }
                                                        if (selectedItems.contains(2)){
                                                            stringBuilder.append("-loan");
                                                        }

                                                        bookdefault(model.getId(),grpkey,meetid,fname,stringBuilder.toString());
                                                    }

                                                }
                                            })
                                            .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                   //...
                                                }
                                            });

                                    AlertDialog alert11 = builder.create();
                                    alert11.show();


                                    return false;
                                }
                            });

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(perfomanceanalysis.this);
                                    builders.setTitle("Reverse Transaction")
                                            .setCancelable(true)
                                            .setPositiveButton("Revert", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Double.parseDouble(model.getWamount()) > 1) {
                                                        rollback(model.getId(),grpkey);
                                                        memuid=model.getId();
                                                        reverseaccountwit(memuid);
                                                        reverseallaccountsdep(memuid);
                                                    }else {
                                                        rollback2(model.getId(),grpkey);
                                                        memuid=model.getId();
                                                        reverseaccountdep(memuid);
                                                        reverseallaccountsdep(memuid);
                                                    }

                                                    DatabaseReference reference=mDatabase.child(rk);
                                                    reference.removeValue();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        return false;
                    }
                });

            }
        };

        mTrans.setAdapter(firebaseRecyclerAdapter);


    }

    private void bookdefault(String id, String grpkey, String meetid, String fname, String string) {

        DatabaseReference dapp=mD2.child("requests").push();

        dapp.child("memberid").setValue(id);
        dapp.child("groupid").setValue(grpkey);
        dapp.child("meetid").setValue(meetid);
        dapp.child("fac").setValue(fname);
        dapp.child("status").setValue("pending");
        dapp.child("particulars").setValue(string);
        dapp.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

        Toast.makeText(this,"Default Booked for member.", Toast.LENGTH_SHORT).show();

    }

    private void reverseallaccountsdep(final String memuid) {
        mD4.child(memuid).child("accounttemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference removal=mAcc;
                if (dataSnapshot.hasChild("riskkey")){
                    removal.child("Riskfund").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("riskkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("risk2key").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memberfundcont")){
                    removal.child("Membersfund").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("memberfundcont").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankkey").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memberfundwith")){
                    removal.child("Membersfund").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankwkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("memberfundwith").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("loanikey")){
                    removal.child("Longtermloan").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("loanikey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankloanikey").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("advpkey")){
                    removal.child("Shorttermloans").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("advpkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankadvpkey").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("inc1adkey")){
                    removal.child("Income").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("inc1adkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankinc2adkey").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("fees1fkey")){
                    removal.child("Feesfines").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("fees1fkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankfees2fkey").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("advpikey")){
                    removal.child("Shorttermloans").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("advpikey").getValue().toString()).removeValue();
                    removal.child("Income").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("advpintkey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankadvpikey").getValue().toString()).removeValue();
                    removal.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(date).child(dataSnapshot.child("bankadvpintkey").getValue().toString()).removeValue();
                }
                if (dataSnapshot.hasChild("unhanded")){
                    removal.child("Unhanded").child("Trans").child("all").child(dataSnapshot.child("unhanded").getValue().toString()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reverseaccountdep(final String memuid) {
        mD4.child(memuid).child("accounttemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference removal=mD4.child(memuid).child("account");
                if (dataSnapshot.hasChild("memaccdepo")){
                    removal.child(dataSnapshot.child("memaccdepo").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccloan")){
                    removal.child(dataSnapshot.child("memaccloan").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccadv")){
                    removal.child(dataSnapshot.child("memaccadv").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccadvint")){
                    removal.child(dataSnapshot.child("memaccadvint").getValue().toString()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reverseaccountwit(final String memuid) {
        mD4.child(memuid).child("accounttemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference removal=mD4.child(memuid).child("account");
                if (dataSnapshot.hasChild("memaccwit")){
                    removal.child(dataSnapshot.child("memaccwit").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccloan")){
                    removal.child(dataSnapshot.child("memaccloan").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccadv")){
                    removal.child(dataSnapshot.child("memaccadv").getValue().toString()).removeValue();
                }

                if (dataSnapshot.hasChild("memaccadvint")){
                    removal.child(dataSnapshot.child("memaccadvint").getValue().toString()).removeValue();
                }
                if (dataSnapshot.hasChild("unhanded")){
                    removal.child(dataSnapshot.child("unhanded").getValue().toString()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void rollback(final String memberid, final String grpkey) {
        try{
            mD4.child(memberid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    final String toups=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                    final String toupl=dataSnapshot.child("loans").child("totalloan").getValue().toString();
                    final String toupa=dataSnapshot.child("advances").child("currentadvance").getValue().toString();

                    final String tmps=dataSnapshot.child("savings").child("temp").child("tempsavings").getValue().toString();
                    final String tmpl=dataSnapshot.child("loans").child("temp").child("temploan").getValue().toString();
                    final String tmpa=dataSnapshot.child("advances").child("temp").child("tempadv").getValue().toString();

                    final double tous=Double.parseDouble(toups)+200;
                    final double toul=Double.parseDouble(toupl);
                    final double toua=Double.parseDouble(toupa);

                    double us=tous+Double.parseDouble(tmps);
                    double ul=toul+Double.parseDouble(tmpl);
                    double ua=toua+Double.parseDouble(tmpa);

                    double newadvance=(toua+Double.parseDouble(tmpa));

                    double uint=((newadvance/1.1)*0.1)+10;

                    final DatabaseReference databaseReference=mD4.child(memberid);

                    if (dataSnapshot.child("savings").child("temp").child("tempssid").exists()){
                        databaseReference.child("savings").child("savings").child(dataSnapshot.child("savings").child("temp").child("tempssid").getValue().toString()).removeValue();
                    }
                    if (dataSnapshot.child("loans").child("temp").child("templl").exists()){
                        databaseReference.child("loans").child("loanss").child(dataSnapshot.child("loans").child("temp").child("templl").getValue().toString()).removeValue();
                    }
                    databaseReference.child("savings").child("totalsavings").setValue(String.valueOf(us));
                    databaseReference.child("loans").child("totalloan").setValue(String.valueOf(ul));
                    databaseReference.child("advances").child("currentadvance").setValue(String.valueOf(newadvance));
                    databaseReference.child("advances").child("currpenalty").setValue(String.valueOf(uint));

                    databaseReference.child("temptrs").removeValue();
                    databaseReference.child("ptemptrs").removeValue();

                    databaseReference.child("advances").child("nextadvpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    databaseReference.child("loans").child("nextloanpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                    databaseReference.child("attendance").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).removeValue();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Transaction has not been reversed.", Toast.LENGTH_SHORT).show();
        }

        /*try {
            mD4.child(memberid).child("risktemptrs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String riskkey=dataSnapshot.child("riskkey").getValue().toString();
                    String risk2key=dataSnapshot.child("risk2key").getValue().toString();
                    if (dataSnapshot.child("memberfundcont").exists()){
                        String memberfundcont=dataSnapshot.child("memberfundcont").getValue().toString();
                        String bankkey=dataSnapshot.child("bankkey").getValue().toString();
                    }else {
                        String bankwkey=dataSnapshot.child("bankwkey").getValue().toString();
                        String bankw2key=dataSnapshot.child("bankw2key").getValue().toString();
                    }
                    if (dataSnapshot.child("inc1adkey").exists()){
                        String inc1adkey=dataSnapshot.child("inc1adkey").getValue().toString();
                        String inc2adkey=dataSnapshot.child("inc2adkey").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }*/


    }

    private void rollback2(final String memberid, final String grpkey) {
        try{
            mD4.child(memberid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String toups=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                    final String toupl=dataSnapshot.child("loans").child("totalloan").getValue().toString();
                    final String toupa=dataSnapshot.child("advances").child("currentadvance").getValue().toString();

                    final String tmps=dataSnapshot.child("savings").child("temp").child("tempsavings").getValue().toString();
                    final String tmpl=dataSnapshot.child("loans").child("temp").child("temploan").getValue().toString();
                    final String tmpa=dataSnapshot.child("advances").child("temp").child("tempadv").getValue().toString();

                    final double tous=Double.parseDouble(toups);
                    final double toul=Double.parseDouble(toupl);
                    final double toua=Double.parseDouble(toupa);

                    double us=tous-Double.parseDouble(tmps);
                    double ul=toul+Double.parseDouble(tmpl);
                    double ua=toua+Double.parseDouble(tmpa);

                    double newadvance=(toua+Double.parseDouble(tmpa));

                    double uint=((newadvance/1.1)*0.1)+10;

                    final DatabaseReference databaseReference=mD4.child(memberid);


                    if (dataSnapshot.child("savings").child("temp").child("tempssid").exists()){
                        databaseReference.child("savings").child("savings").child(dataSnapshot.child("savings").child("temp").child("tempssid").getValue().toString()).removeValue();
                    }
                    if (dataSnapshot.child("loans").child("temp").child("templl").exists()){
                        databaseReference.child("loans").child("loanss").child(dataSnapshot.child("loans").child("temp").child("templl").getValue().toString()).removeValue();
                    }
                    databaseReference.child("savings").child("totalsavings").setValue(String.valueOf(us));
                    databaseReference.child("loans").child("totalloan").setValue(String.valueOf(ul));
                    databaseReference.child("advances").child("currentadvance").setValue(String.valueOf(newadvance));
                    databaseReference.child("advances").child("currpenalty").setValue(String.valueOf(uint));

                    DatabaseReference databaseReference2=mD4.child(memberid);
                    databaseReference2.child("savings").child("temp").child("tempsavings").removeValue();
                    databaseReference2.child("loans").child("temp").child("temploan").removeValue();
                    databaseReference2.child("advances").child("temp").child("tempadv").removeValue();

                    databaseReference.child("temptrs").removeValue();
                    databaseReference.child("ptemptrs").removeValue();
                    databaseReference.child("attendance").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).removeValue();

                    databaseReference.child("advances").child("nextadvpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    databaseReference.child("loans").child("nextloanpaydate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Transaction has not been reversed.", Toast.LENGTH_SHORT).show();
        }


    }

    private void screen(RecyclerView view) {

        View u = findViewById(R.id.tebo);
        View w = findViewById(R.id.tebototals);
        View y = mtopdetails;

        b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());
        b2 = getBitmapFromView(w,w.getHeight(),w.getWidth());
        Bitmap b3=getBitmapFromView(y,y.getHeight(),y.getWidth());

        RelativeLayout z = (RelativeLayout) findViewById(R.id.top);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        RecyclerView.Adapter adapter = view.getAdapter();

        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = getBitmapFromView(holder.itemView,holder.itemView.getHeight(),holder.itemView.getWidth());
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height+(height/2), Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            bigCanvas.drawBitmap(b3,0f,0f,paint);
            bigCanvas.drawBitmap(b1,0f,b3.getHeight(),paint);
            iHeight=iHeight+b1.getHeight()+b3.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

            bigCanvas.drawBitmap(b2,0f,iHeight,paint);

            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/PerfomanceAnalysis/"+gname);
                File fil = new File(f,date+".jpg");
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                fo.write(bytes.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/PerfomanceAnalysis/"+gname);
            File fil = new File(f,date+".jpg");
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static class transViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public transViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.transaccard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String name){
            TextView mGroupname = mView.findViewById(R.id.gridmembername);
            mGroupname.setText(name);
            mGroupname.setTextSize(20);
        }
        public void setTotalRepaid(String totalrepaid){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdtotalrepaid);
            mGroupname.setText("Kshs. "+totalrepaid);
            mGroupname.setTextSize(20);
        }
        public void setLSF(String lsf){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdlsf);
            mGroupname.setText("Kshs. "+lsf);
            mGroupname.setTextSize(20);
        }
        public void setLoanInst(String loaninst){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdloaninst);
            mGroupname.setText("Kshs. "+loaninst);
            mGroupname.setTextSize(20);
        }
        public void setAdvance(String advance){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvinst);
            mGroupname.setText("Kshs. "+advance);
            mGroupname.setTextSize(20);
        }
        public void setPaymode(String paymode){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdpaymode);
            mGroupname.setText(paymode);
            mGroupname.setTextSize(20);
        }
        public void setInsu1(String insu1){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvanceint);
            mGroupname.setText("Kshs. "+insu1);
            mGroupname.setTextSize(20);
        }
        public void setLoangivn(final String loangiv){
            TextView mGroupame = mView.findViewById(R.id.grdloangivn);
            mGroupame.setText("Kshs. "+loangiv);
            mGroupame.setTextSize(20);
        }
        public void setAdvcgvn(final String advcgvn){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdadvancegiven);
            mGrouname.setText("Kshs. "+advcgvn);
            mGrouname.setTextSize(20);
        }
        public void setLoancf(final String loancf){
            TextView mGroupame = mView.findViewById(R.id.grdloancf);
            mGroupame.setText("Kshs. "+loancf);
            mGroupame.setTextSize(20);
        }
        public void setAdvccf(final String advccf){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdadvancecf);
            mGrouname.setText("Kshs. "+advccf);
            mGrouname.setTextSize(20);
        }
        public void setLsfcf(final String lsfcf){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdlsfcf);
            mGrouname.setText("Kshs. "+lsfcf);
            mGrouname.setTextSize(20);
        }
    }

}
