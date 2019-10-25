package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class directorhome extends AppCompatActivity {
    private TextView mOutBal,mBalfigdr,mBalfigcr,mTotalDr,mTotalCr,mBalDr,mBalCr;
    private RecyclerView mList,mList2;
    private DatabaseReference mD,mD1,mD2,mD3;
    private CardView c1,c2,c3,c5,c6,c7,cc2,cc3;
    private EditText mItemName,mDesc;

    private FirebaseAuth mAuth;

    private EditText taskedit,task2,chq,mitmamt;
    private LinearLayout layout,lin2;
    private Spinner taskedit2,taskedit3,taskedit4;

    private String bselected=null,bselected2=null,user,drcr=null;
    private double v7=0,v8=0,v9=0,v2=0;

    private Spinner mSitem,mSitem1;
    private String curritem,db,cr,eid,speriod;

    private Button mProcess,mAdditems,mDone;

    private ProgressDialog mProgress;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directorhome1);

        mOutBal=findViewById(R.id.outbal);

        mList=findViewById(R.id.entrieslist);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setHasFixedSize(true);
        mList.setItemViewCacheSize(50);
        mList.setDrawingCacheEnabled(true);

        mList2=findViewById(R.id.itemsadded);
        mList2.setLayoutManager(new LinearLayoutManager(this));
        mList2.setHasFixedSize(true);
        mList2.setItemViewCacheSize(50);
        mList2.setDrawingCacheEnabled(true);

        mD=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD1= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Director");
        mD2= FirebaseDatabase.getInstance().getReference().child("Accounts");
        mD3=FirebaseDatabase.getInstance().getReference().child("Employees");



        c1=findViewById(R.id.monthlyre);
        c2=findViewById(R.id.dailyre);
        c3=findViewById(R.id.repot);
        cc2=findViewById(R.id.dirxpns);
        c5=findViewById(R.id.shcapital);
        c6=findViewById(R.id.profitres);
        c7=findViewById(R.id.suspensea);
        cc3=findViewById(R.id.dirxpenses);


        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");

        lin2=findViewById(R.id.options21);

        layout=new LinearLayout(directorhome.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        taskedit=new EditText(directorhome.this);
        taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
        task2=new EditText(directorhome.this);
        taskedit2=new Spinner(directorhome.this);
        chq=new EditText(directorhome.this);
        chq.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskedit3=new Spinner(directorhome.this);
        taskedit4=new Spinner(directorhome.this);

        if(task2.getParent() != null) {
            ((ViewGroup)task2.getParent()).removeView(task2); // <- fix
        }
        if(taskedit.getParent() != null) {
            ((ViewGroup)taskedit.getParent()).removeView(taskedit); // <- fix
        }
        if(chq.getParent() != null) {
            ((ViewGroup)chq.getParent()).removeView(chq); // <- fix
        }

        mBalfigdr=findViewById(R.id.balancingpartdr);
        mBalfigcr=findViewById(R.id.balancingpartcr);
        mTotalDr=findViewById(R.id.totalsdr);
        mTotalCr=findViewById(R.id.totalscr);
        mBalDr=findViewById(R.id.balancedowndr);
        mBalCr=findViewById(R.id.balancedowncr);

        mItemName=findViewById(R.id.itmname);
        mProcess=findViewById(R.id.addchq);
        mAdditems=findViewById(R.id.addiem);
        mDesc=findViewById(R.id.expdesc);
        mDone=findViewById(R.id.button3);
        mitmamt=findViewById(R.id.itmamount);
        mSitem=findViewById(R.id.spnitems);
        mSitem1=findViewById(R.id.spnitems1);


        cc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin2.setVisibility(View.GONE);
            }
        });

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("position").getValue().toString().equals("secretary")&&snapshot.child("Branch").getValue().toString().equals("Thika")){
                        user=snapshot.getKey();

                        mD.child("Bank").child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> banks = new ArrayList<>();
                                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                                    String type=(String)dsp.getKey();
                                    banks.add(type);
                                }

                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(directorhome.this, android.R.layout.simple_spinner_item, banks);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                taskedit3.setAdapter(areasAdapter);

                                taskedit3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        bselected2=parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                        bselected2=banks.get(0);

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(directorhome.this,R.array.directortrans,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit2.setAdapter(adapter2);

                        taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                bselected=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                                bselected=parent.getItemAtPosition(0).toString();

                            }
                        });

                        ArrayAdapter<CharSequence> adapter22=ArrayAdapter.createFromResource(directorhome.this,R.array.drcrs,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        taskedit4.setAdapter(adapter22);

                        taskedit4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                drcr=parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                                drcr=parent.getItemAtPosition(0).toString();

                            }
                        });




                        
                        c5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout layout=new LinearLayout(directorhome.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                final EditText basi=new EditText(directorhome.this);
                                basi.setInputType(InputType.TYPE_CLASS_NUMBER);
                                final TextView basic=new EditText(directorhome.this);
                                //layout.addView(basic);
                                layout.addView(basi);
                                basic.setText("Current Share Capital Balance: ");
                                basi.setHint("Enter Share Capital");
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                builders.setTitle("Share Capital")
                                        .setMessage("Share Capital Update")
                                        .setView(layout)
                                        .setCancelable(true)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (!TextUtils.isEmpty(basi.getText().toString())){

                                                    DatabaseReference bankw=mD.child("ShareCapital").child("Trans").child("all").push();
                                                    bankw.child("name").setValue("Share Capital");
                                                    bankw.child("amount").setValue(basi.getText().toString());
                                                    bankw.child("type").setValue("blue");
                                                    bankw.child("meet").setValue("-");
                                                    bankw.child("group").setValue("-");
                                                    bankw.child("debitac").setValue("Bank");
                                                    bankw.child("creditac").setValue("ShareCapital");
                                                    bankw.child("description").setValue("Share Capital Update");
                                                    bankw.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    Toast.makeText(directorhome.this,"Share Capital Updated!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }else {
                                                    Toast.makeText(directorhome.this,"Enter New Values!", Toast.LENGTH_SHORT).show();
                                                }

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

                        cc2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("View","Setup");
                                lin2.setVisibility(View.VISIBLE);
                            }
                        });

                        c6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                layout.addView(taskedit4);
                                layout.addView(taskedit);
                                taskedit.setHint("Enter Amount..");
                                builders.setTitle("Profit Reserve Account")
                                        .setMessage("Update Profit Reserves.")
                                        .setCancelable(true)
                                        .setView(layout)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {

                                                final String cheqeuamt=taskedit.getText().toString();

                                                if (drcr.equals("Dr")){

                                                    mD.child("ProfitReserves").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()){

                                                                DatabaseReference mpesa=mD.child("ProfitReserves").child("Trans").child("all").push();
                                                                mpesa.child("name").setValue("Profit Reserves");
                                                                mpesa.child("amount").setValue(cheqeuamt);
                                                                mpesa.child("type").setValue("blue");
                                                                mpesa.child("meet").setValue("-");
                                                                mpesa.child("group").setValue("-");
                                                                mpesa.child("debitac").setValue("ProfitReserves");
                                                                mpesa.child("creditac").setValue("Director");
                                                                mpesa.child("description").setValue("Profit Reserves Debited");
                                                                mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                                                Toast.makeText(directorhome.this,"Amount Debited!", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();

                                                            }else {
                                                                DatabaseReference mpesa=mD.child("ProfitReserves").child("Trans").child("all").push();
                                                                mpesa.child("name").setValue("Profit Reserves");
                                                                mpesa.child("amount").setValue(cheqeuamt);
                                                                mpesa.child("type").setValue("red");
                                                                mpesa.child("meet").setValue("-");
                                                                mpesa.child("group").setValue("-");
                                                                mpesa.child("debitac").setValue("ProfitReserves");
                                                                mpesa.child("creditac").setValue("Director");
                                                                mpesa.child("description").setValue("Profit Reserves Debited");
                                                                mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                                                Toast.makeText(directorhome.this,"Amount Debited!", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }else {
                                                    mD.child("ProfitReserves").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()){

                                                                DatabaseReference mpesa=mD.child("ProfitReserves").child("Trans").child("all").push();
                                                                mpesa.child("name").setValue("Profit Reserves");
                                                                mpesa.child("amount").setValue(cheqeuamt);
                                                                mpesa.child("type").setValue("red");
                                                                mpesa.child("meet").setValue("-");
                                                                mpesa.child("group").setValue("-");
                                                                mpesa.child("debitac").setValue("Director");
                                                                mpesa.child("creditac").setValue("ProfitReserves");
                                                                mpesa.child("description").setValue("Profit Reserves Credited");
                                                                mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                Toast.makeText(directorhome.this,"Amount Credited!", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();

                                                            }else {
                                                                DatabaseReference mpesa=mD.child("ProfitReserves").child("Trans").child("all").push();
                                                                mpesa.child("name").setValue("Profit Reserves");
                                                                mpesa.child("amount").setValue(cheqeuamt);
                                                                mpesa.child("type").setValue("red");
                                                                mpesa.child("meet").setValue("-");
                                                                mpesa.child("group").setValue("-");
                                                                mpesa.child("debitac").setValue("Director");
                                                                mpesa.child("creditac").setValue("ProfitReserves");
                                                                mpesa.child("description").setValue("Profit Reserves Credited");
                                                                mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                Toast.makeText(directorhome.this,"Amount Credited!", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

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

                        c1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                layout.addView(taskedit2);
                                layout.addView(taskedit3);
                                layout.addView(taskedit);
                                layout.addView(chq);
                                layout.addView(task2);
                                taskedit.setHint("Enter Amount..");
                                chq.setHint("Enter Cheque Number..");
                                task2.setHint("Transaction Details..");
                                builders.setTitle("Director Bank Transaction")
                                        .setMessage("Make Deposits or Withrawals from Bank Accounts.")
                                        .setCancelable(true)
                                        .setView(layout)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String chequeno=chq.getText().toString();
                                                String cheqeuamt=taskedit.getText().toString();
                                                String detail=task2.getText().toString();

                                                if (bselected.equals("DEPOSIT")){

                                                    DatabaseReference reference3=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    reference3.child("chequenumber").setValue(chequeno);
                                                    reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    reference3.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    reference3.child("status").setValue("completed");
                                                    reference3.child("deposit").setValue(cheqeuamt);
                                                    reference3.child("withdraw").setValue("0");
                                                    reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference3e=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                    reference3e.child("chequenumber").setValue(chequeno);
                                                    reference3e.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    reference3e.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    reference3e.child("status").setValue("completed");
                                                    reference3e.child("deposit").setValue(cheqeuamt);
                                                    reference3e.child("withdraw").setValue("0");
                                                    reference3e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference referene3=mD.child("Bank").child("Trans").child("all").push();
                                                    referene3.child("chequenumber").setValue(chequeno);
                                                    referene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    referene3.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    referene3.child("status").setValue("completed");
                                                    referene3.child("deposit").setValue(cheqeuamt);
                                                    referene3.child("withdraw").setValue("0");
                                                    referene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r1eference3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    r1eference3.child("chequenumber").setValue(chequeno);
                                                    r1eference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r1eference3.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    r1eference3.child("status").setValue("completed");
                                                    r1eference3.child("deposit").setValue(cheqeuamt);
                                                    r1eference3.child("withdraw").setValue("0");
                                                    r1eference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r1eference3m=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                    r1eference3m.child("chequenumber").setValue(chequeno);
                                                    r1eference3m.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r1eference3m.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    r1eference3m.child("status").setValue("completed");
                                                    r1eference3m.child("deposit").setValue(cheqeuamt);
                                                    r1eference3m.child("withdraw").setValue("0");
                                                    r1eference3m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r2eferene3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child("all").push();
                                                    r2eferene3.child("chequenumber").setValue(chequeno);
                                                    r2eferene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r2eferene3.child("details").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    r2eferene3.child("status").setValue("completed");
                                                    r2eferene3.child("deposit").setValue(cheqeuamt);
                                                    r2eferene3.child("withdraw").setValue("0");
                                                    r2eferene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Bank");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("blue");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("creditac").setValue("Director");
                                                    reference2pettya.child("debitac").setValue("BankCash");
                                                    reference2pettya.child("description").setValue("Cheque number: "+chequeno+" deposited by the Director for: "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);



                                                    Toast.makeText(directorhome.this,"Deposit Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }else {

                                                    DatabaseReference reference3=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    reference3.child("chequenumber").setValue(chequeno);
                                                    reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    reference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    reference3.child("status").setValue("completed");
                                                    reference3.child("deposit").setValue("0");
                                                    reference3.child("withdraw").setValue(cheqeuamt);
                                                    reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference3e=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                    reference3e.child("chequenumber").setValue(chequeno);
                                                    reference3e.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    reference3e.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    reference3e.child("status").setValue("completed");
                                                    reference3e.child("deposit").setValue("0");
                                                    reference3e.child("withdraw").setValue(cheqeuamt);
                                                    reference3e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference referene3=mD.child("Bank").child("Trans").child("all").push();
                                                    referene3.child("chequenumber").setValue(chequeno);
                                                    referene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    referene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    referene3.child("status").setValue("completed");
                                                    referene3.child("deposit").setValue("0");
                                                    referene3.child("withdraw").setValue(cheqeuamt);
                                                    referene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r1eference3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                                    r1eference3.child("chequenumber").setValue(chequeno);
                                                    r1eference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r1eference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    r1eference3.child("status").setValue("completed");
                                                    r1eference3.child("deposit").setValue("0");
                                                    r1eference3.child("withdraw").setValue(cheqeuamt);
                                                    r1eference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r1eference3m=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                    r1eference3m.child("chequenumber").setValue(chequeno);
                                                    r1eference3m.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r1eference3m.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    r1eference3m.child("status").setValue("completed");
                                                    r1eference3m.child("deposit").setValue("0");
                                                    r1eference3m.child("withdraw").setValue(cheqeuamt);
                                                    r1eference3m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference r2eferene3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child("all").push();
                                                    r2eferene3.child("chequenumber").setValue(chequeno);
                                                    r2eferene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    r2eferene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    r2eferene3.child("status").setValue("completed");
                                                    r2eferene3.child("deposit").setValue("0");
                                                    r2eferene3.child("withdraw").setValue(cheqeuamt);
                                                    r2eferene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Bank");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("red");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("creditac").setValue("BankCash");
                                                    reference2pettya.child("debitac").setValue("Director");
                                                    reference2pettya.child("description").setValue("Cheque number: "+chequeno+" withdrawn by the Director for: "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                                    Toast.makeText(directorhome.this,"Withdraw Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }

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

                        c2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                layout.addView(taskedit2);
                                //layout.addView(taskedit3);
                                layout.addView(taskedit);
                                layout.addView(chq);
                                layout.addView(task2);
                                taskedit.setHint("Enter Amount..");
                                chq.setHint("Enter Transaction Number..");
                                task2.setHint("Transaction Details..");
                                builders.setTitle("Director Mpesa Transaction")
                                        .setMessage("Make Deposits or Withrawals from Mpesa Account.")
                                        .setCancelable(true)
                                        .setView(layout)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String chequeno=chq.getText().toString();
                                                String cheqeuamt=taskedit.getText().toString();
                                                String detail=task2.getText().toString();

                                                if (bselected.equals("DEPOSIT")){

                                                    DatabaseReference mpesa=mD.child("Mpesa").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Mpesa");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("blue");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("debitac").setValue("Mpesa");
                                                    mpesa.child("creditac").setValue("Director");
                                                    mpesa.child("description").setValue("Mpesa deposited by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Mpesa");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("red");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("creditac").setValue("Director");
                                                    reference2pettya.child("debitac").setValue("Mpesa");
                                                    reference2pettya.child("description").setValue("Mpesa deposited by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    Toast.makeText(directorhome.this,"Deposit Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }else {

                                                    DatabaseReference mpesa=mD.child("Mpesa").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Mpesa");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("blue");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("creditac").setValue("Mpesa");
                                                    mpesa.child("debitac").setValue("Director");
                                                    mpesa.child("description").setValue("Mpesa withdrawal by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Mpesa");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("blue");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("debitac").setValue("Director");
                                                    reference2pettya.child("creditac").setValue("Mpesa");
                                                    reference2pettya.child("description").setValue("Mpesa withdrawal by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    Toast.makeText(directorhome.this,"Withdraw Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }

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

                        c7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                layout.addView(taskedit2);
                                //layout.addView(taskedit3);
                                layout.addView(taskedit);
                                //layout.addView(chq);
                                layout.addView(task2);
                                taskedit.setHint("Enter Amount..");
                                chq.setHint("Enter Details..");
                                task2.setHint("Transaction Details..");
                                builders.setTitle("Suspense Account")
                                        .setMessage("Make Deposits or Withrawals from Suspense Account.")
                                        .setCancelable(true)
                                        .setView(layout)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String chequeno=chq.getText().toString();
                                                String cheqeuamt=taskedit.getText().toString();
                                                String detail=task2.getText().toString();

                                                if (bselected.equals("DEPOSIT")){

                                                    DatabaseReference mpesa=mD.child("Suspense").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Mpesa");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("blue");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("debitac").setValue("Suspense");
                                                    mpesa.child("creditac").setValue("Director");
                                                    mpesa.child("description").setValue(cheqeuamt+" deposited by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Mpesa");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("red");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("creditac").setValue("Director");
                                                    reference2pettya.child("debitac").setValue("Suspense");
                                                    reference2pettya.child("description").setValue(cheqeuamt+" deposited by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    Toast.makeText(directorhome.this,"Deposit Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }else {

                                                    DatabaseReference mpesa=mD.child("Suspense").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Mpesa");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("blue");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("creditac").setValue("Suspense");
                                                    mpesa.child("debitac").setValue("Director");
                                                    mpesa.child("description").setValue(cheqeuamt+"  withdrawal by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Mpesa");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("blue");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("debitac").setValue("Director");
                                                    reference2pettya.child("creditac").setValue("Suspense");
                                                    reference2pettya.child("description").setValue(cheqeuamt+"  withdrawal by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    Toast.makeText(directorhome.this,"Withdraw Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }

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

                        c3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                                layout.addView(taskedit2);
                                //layout.addView(taskedit3);
                                layout.addView(taskedit);
                                //layout.addView(chq);
                                layout.addView(task2);
                                taskedit.setHint("Enter Amount..");
                                //chq.setHint("Enter Transaction Number..");
                                task2.setHint("Transaction Details..");
                                builders.setTitle("Director Petty Cash Transaction")
                                        .setMessage("Make Deposits or Withrawals from Petty Cash Account.")
                                        .setCancelable(true)
                                        .setView(layout)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                //String chequeno=chq.getText().toString();
                                                final String cheqeuamt=taskedit.getText().toString();
                                                String detail=task2.getText().toString();

                                                if (bselected.equals("DEPOSIT")){

                                                    DatabaseReference mpesa=mD.child("Petty").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Director");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("blue");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("debitac").setValue("Petty");
                                                    mpesa.child("creditac").setValue("Director");
                                                    mpesa.child("description").setValue("Cash deposited by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Petty");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("red");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("creditac").setValue("Director");
                                                    reference2pettya.child("debitac").setValue("Petty");
                                                    reference2pettya.child("description").setValue("Cash deposited by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    mD2.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                            double balb44=Double.parseDouble(balb4);
                                                            double e=balb44+Double.parseDouble(cheqeuamt);
                                                            DatabaseReference newdepo=mD2.child(user);
                                                            newdepo.child("Balance").setValue(String.valueOf(e));

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    Toast.makeText(directorhome.this,"Deposit Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }else {

                                                    DatabaseReference mpesa=mD.child("Petty").child("Trans").child("all").push();
                                                    mpesa.child("name").setValue("Petty");
                                                    mpesa.child("amount").setValue(cheqeuamt);
                                                    mpesa.child("type").setValue("red");
                                                    mpesa.child("meet").setValue("-");
                                                    mpesa.child("group").setValue("-");
                                                    mpesa.child("creditac").setValue("Petty");
                                                    mpesa.child("debitac").setValue("Director");
                                                    mpesa.child("description").setValue("Cash withdrawal by the Director for "+detail);
                                                    mpesa.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    DatabaseReference reference2pettya=mD.child("Director").child("Trans").child("all").push();
                                                    reference2pettya.child("name").setValue("Mpesa");
                                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                                    reference2pettya.child("type").setValue("blue");
                                                    reference2pettya.child("meet").setValue("-");
                                                    reference2pettya.child("group").setValue("-");
                                                    reference2pettya.child("debitac").setValue("Director");
                                                    reference2pettya.child("creditac").setValue("Petty");
                                                    reference2pettya.child("description").setValue("Cash withdrawal by the Director for "+detail);
                                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    mD2.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                            double balb44=Double.parseDouble(balb4);
                                                            double e=balb44-Double.parseDouble(cheqeuamt);
                                                            DatabaseReference newdepo=mD2.child(user);
                                                            newdepo.child("Balance").setValue(String.valueOf(e));

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    Toast.makeText(directorhome.this,"Withdraw Done!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }

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

                        mD.child("Director").child("Trans").child("all").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                v8=0;
                                v9=0;

                                mBalfigcr.setText("");
                                mBalfigdr.setText("");
                                mTotalCr.setText("");
                                mTotalDr.setText("");
                                mBalCr.setText("");
                                mBalDr.setText("");

                                if (dataSnapshot.hasChildren()){
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        if (snapshot.child("debitac").exists()&&snapshot.child("debitac").getValue().toString().equals("Director")){
                                            v8=v8+Double.parseDouble(snapshot.child("amount").getValue().toString());

                                            mTotalDr.setText("Kshs. "+df.format(v8));
                                        }

                                        if (snapshot.child("debitac").exists()&&!snapshot.child("debitac").getValue().toString().equals("Director")){
                                            v9=v9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                                            mTotalCr.setText("Kshs. "+df.format(v9));
                                        }

                                        v7=v8-v9;
                                        mOutBal.setText("Kshs "+df.format(v7));


                                    }

                                    if (v8>v9){
                                        double vb=v8-v9;
                                        mBalfigcr.setText("Kshs. "+df.format(vb));
                                        mBalDr.setText("Kshs. "+df.format(vb));
                                        mTotalCr.setText("Kshs. "+df.format(v8));
                                    }else {
                                        double vb=v9-v8;
                                        mBalfigdr.setText("Kshs. "+df.format(vb));
                                        mBalCr.setText("Kshs. "+df.format(vb));
                                        mTotalDr.setText("Kshs. "+df.format(v9));
                                    }

                                }else {
                                    mOutBal.setText("Kshs 0");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdaptre = new FirebaseRecyclerAdapter<account, accountholder>(

                                account.class,
                                R.layout.accountsentries_row,
                                accountholder.class,
                                mD.child("Director").child("Trans").child("all").limitToLast(50)
                        ) {
                            @Override
                            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                                viewHolder.setDate(model.getTimestamp());
                                viewHolder.setDesc(model.getDescription());
                                if (model.getDebitac().equals("Director")){
                                    viewHolder.setDebit(model.getAmount());
                                    viewHolder.setCredit("0");
                                }else {
                                    viewHolder.setCredit(model.getAmount());
                                    viewHolder.setDebit("0");
                                }
                            }
                        };

                        mList.setAdapter(firebaseRecyclerAdaptre);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        ArrayAdapter<CharSequence> adapters=ArrayAdapter.createFromResource(this,R.array.periods,android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSitem.setAdapter(adapters);


        mSitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speriod=parent.getItemAtPosition(position).toString();

                switch (speriod) {
                    case "Daily":
                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> items = new ArrayList<>();
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    String type = (String) dsp.child("name").getValue();
                                    if (dsp.child("period").getValue().toString().equals("Daily")) {
                                        items.add(type);
                                    }
                                }

                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(directorhome.this, android.R.layout.simple_spinner_item, items);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSitem1.setAdapter(areasAdapter);

                                mSitem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        curritem = parent.getItemAtPosition(position).toString();
                                        mItemName.setText(curritem);
                                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                                    String targetdoc = (String) dataSnap.child("name").getValue();
                                                    if (targetdoc.equals(curritem)) {
                                                        db = (String) dataSnap.child("debitac").getValue().toString();
                                                        cr = (String) dataSnap.child("creditac").getValue().toString();

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        curritem = parent.getItemAtPosition(0).toString();
                                        mItemName.setText(curritem);
                                    }

                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    case "Monthly":
                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> items = new ArrayList<>();
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    String type = (String) dsp.child("name").getValue();
                                    if (dsp.child("period").getValue().toString().equals("Monthly")) {
                                        items.add(type);
                                    }
                                }

                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(directorhome.this, android.R.layout.simple_spinner_item, items);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSitem1.setAdapter(areasAdapter);

                                mSitem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        curritem = parent.getItemAtPosition(position).toString();
                                        mItemName.setText(curritem);
                                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                                    String targetdoc = (String) dataSnap.child("name").getValue();
                                                    if (targetdoc.equals(curritem)) {
                                                        db = (String) dataSnap.child("debitac").getValue().toString();
                                                        cr = (String) dataSnap.child("creditac").getValue().toString();

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        curritem = parent.getItemAtPosition(0).toString();
                                        mItemName.setText(curritem);
                                    }

                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    default:
                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> items = new ArrayList<>();
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    String type = (String) dsp.child("name").getValue();
                                    if (dsp.child("period").getValue().toString().equals("Annual")) {
                                        items.add(type);
                                    }
                                }

                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(directorhome.this, android.R.layout.simple_spinner_item, items);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSitem1.setAdapter(areasAdapter);

                                mSitem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        curritem = parent.getItemAtPosition(position).toString();
                                        mItemName.setText(curritem);
                                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                                    String targetdoc = (String) dataSnap.child("name").getValue();
                                                    if (targetdoc.equals(curritem)) {
                                                        db = (String) dataSnap.child("debitac").getValue().toString();
                                                        cr = (String) dataSnap.child("creditac").getValue().toString();

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        curritem = parent.getItemAtPosition(0).toString();
                                        mItemName.setText(curritem);
                                    }

                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });

        FirebaseRecyclerAdapter<expenseitemadded, expenseitemsaddedViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<expenseitemadded, expenseitemsaddedViewHolder>(

                expenseitemadded.class,
                R.layout.expenseitemadded_row,
                expenseitemsaddedViewHolder.class,
                mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("directoritems").child("daily")
        )
        {
            @Override
            protected void populateViewHolder(final expenseitemsaddedViewHolder viewHolder, final expenseitemadded model, final int position) {

                viewHolder.setName(model.getName());
                viewHolder.setCreditac(model.getCreditac());
                viewHolder.setDebitac(model.getDebitac());
                viewHolder.setPeriod(model.getDescription());
                viewHolder.setAmount(model.getAmount());

                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                        builders.setTitle("Confirm Item Removal")
                                .setMessage("Remove "+model.getDescription()+" ?")
                                .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference ref=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("directoritems").child("daily");
                                        ref.child(getRef(position).getKey()).removeValue();
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),model.getName()+" removed from list",Toast.LENGTH_LONG).show();
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


        };
        mList2.setAdapter(firebaseRecyclerAdapter);

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggregate();
            }
        });

    }

    private void additem() {
        String itemdesc=mDesc.getText().toString().trim();
        String itemamount=mitmamt.getText().toString().trim();

        DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("directoritems").child("daily").push();
        reference.child("name").setValue(curritem);
        reference.child("amount").setValue(itemamount);
        reference.child("type").setValue("blue");
        reference.child("meet").setValue("-");
        reference.child("group").setValue("-");
        reference.child("debitac").setValue(db);
        reference.child("creditac").setValue(cr);
        reference.child("description").setValue(itemdesc);

        Toast.makeText(this,curritem+" added to list",Toast.LENGTH_LONG).show();

        mitmamt.setText("");
        mDesc.setText("");
    }

    public static class accountholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        //private TextView link;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        DecimalFormat df = new DecimalFormat("##,###,###.#");


        public accountholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.accountingentries);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //link=mView.findViewById(R.id.btrname);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(long date){
            TextView loandate = (TextView)mView.findViewById(R.id.entrydate);
            loandate.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(date));
        }
        public void setDesc(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydetail);
            amountgiven.setText(amount);
        }
        public void setDebit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setCredit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrycr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
    }

    public static class expenseitemsaddedViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        private TextView remove;
        View mView;


        public expenseitemsaddedViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            remove=itemView.findViewById(R.id.btn);
            layout =  itemView.findViewById(R.id.expitemslist);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.expitmname);
            mGroupname.setText(groupname);
        }
        public void setDebitac(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmdebitac);
            mGrouploc.setText(venue);
        }
        public void setCreditac(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmcreditac);
            mGrouploc.setText(date);
        }
        public void setPeriod(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmdesc);
            mGrouploc.setText(amount);
        }
        public void setAmount(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmamt);
            mGrouploc.setText(amount);
        }
    }

    private void aggregate() {
        mProgress.setMessage("Aggregating day's Budget");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("directoritems").child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                        v2=v2+iamt;
                    }
                }

                mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("directoritems").child("daily").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                            final String cuu=snapshot.child("description").getValue().toString();
                            final String cuua=snapshot.child("amount").getValue().toString();
                            final String cuuname=snapshot.child("name").getValue().toString();

                            final AlertDialog.Builder builders = new AlertDialog.Builder(directorhome.this);
                            builders.setTitle("Confirm Budget.")
                                    .setMessage("Expense :"+String.valueOf(v2))
                                    .setCancelable(true)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!snapshot.child("name").getValue().toString().equals("Loans")){
                                                DatabaseReference reference3=mD.child("Expenses").child("Trans").child("all").push();
                                                reference3.child("name").setValue(cuuname);
                                                reference3.child("amount").setValue(cuua);
                                                reference3.child("type").setValue("blue");
                                                reference3.child("group").setValue("-");
                                                reference3.child("meet").setValue("-");
                                                reference3.child("debitac").setValue("Expense");
                                                reference3.child("creditac").setValue("Director");
                                                reference3.child("description").setValue(cuu);
                                                reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                            }
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference reference=mD.child("Expenses").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                reference.child("name").setValue("Expenses");
                reference.child("amount").setValue(String.valueOf(v2));
                reference.child("type").setValue("blue");
                reference.child("group").setValue("-");
                reference.child("meet").setValue("-");
                reference.child("debitac").setValue("Expense");
                reference.child("creditac").setValue("BankCash");
                reference.child("description").setValue("Director's Expenses");
                reference.child("timestamp").setValue(ServerValue.TIMESTAMP);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgress.dismiss();
        lin2.setVisibility(View.GONE);
    }
}
