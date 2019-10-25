package com.example.london.smartserve;

import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class secrecords extends AppCompatActivity {
    private TextView lBook,lTotal,lTotal1;
    private RecyclerView mLedgerlist;
    private FloatingActionButton fab;
    private DatabaseReference mD,mD1,mD2,mDw,mD8,mDa,mD5,mDx,mD5x;
    private double d1,d2,d3,d4;
    private int counter;
    private String user,date,key,datew,a,name=null,be,c,d,yearx,monthx,dayx,datex,bselected,uid,e,dater=null,as=null,bs=null,manamount;
    private FirebaseAuth mAuth;

    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;

    LinearLayout layout;
    private EditText taskedit,task2;
    private Spinner taskedit2;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secrecords);
        Bundle extras = getIntent().getExtras();
        datew = extras.getString("date");
        final long dat=Long.parseLong(datew);
        yearx=new SimpleDateFormat("yyyy").format(dat);
        monthx=new SimpleDateFormat("MMM").format(dat);
        dayx=new SimpleDateFormat("EEE").format(dat);
        datex=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
        date=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        lBook=findViewById(R.id.ledgerdate);
        lTotal=findViewById(R.id.ledtotal);
        lTotal1=findViewById(R.id.ledtotal1);

        mLedgerlist=findViewById(R.id.ledgerlistt);
        mLedgerlist.setHasFixedSize(true);
        mLedgerlist.setItemViewCacheSize(50);
        mLedgerlist.setDrawingCacheEnabled(true);
        mLedgerlist.setLayoutManager(new LinearLayoutManager(this));

        fab=findViewById(R.id.saveledger);

        layout=new LinearLayout(secrecords.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        taskedit=new EditText(secrecords.this);
        task2=new EditText(secrecords.this);
        taskedit2=new Spinner(secrecords.this);


        mD= FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(date);
        mD1=FirebaseDatabase.getInstance().getReference().child("fieldwork");
        mD2=FirebaseDatabase.getInstance().getReference().child("Employees");
        mDw=FirebaseDatabase.getInstance().getReference().child("details");
        mD8=FirebaseDatabase.getInstance().getReference().child("meetings").child("master");
        mDa=FirebaseDatabase.getInstance().getReference().child("reports");
        mD5=FirebaseDatabase.getInstance().getReference().child("Accounts").child(user);
        mD5x=FirebaseDatabase.getInstance().getReference().child("Accounts");
        mDx= FirebaseDatabase.getInstance().getReference().child("Accounting");

        lBook.setText("For the Date: "+date);

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("position").getValue().toString().equals("manager")){
                        key=snapshot.getKey();
                        break;
                    }
                }

                mD5x.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String amountre=dataSnapshot.child("Balance").getValue().toString();
                        double damountre=Double.parseDouble(amountre);
                        if (damountre>0){
                            manamount=amountre;
                        }else {
                            manamount="0";
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

        mDx.child("Bank").child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> banks = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.getKey();
                    banks.add(type);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(secrecords.this, android.R.layout.simple_spinner_item, banks);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                taskedit2.setAdapter(areasAdapter);

                taskedit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bselected=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                        bselected=banks.get(0);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    d1=0;
                    d2=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("status").getValue().toString().equals("received")){
                            String am=snapshot.child("amountbanked").getValue().toString();
                            d1=Double.parseDouble(am);
                            d2=d2+d1;

                            lTotal.setText("Kshs. "+df.format(d2));
                        }
                        String am2=snapshot.child("amountout").getValue().toString();
                        d3=Double.parseDouble(am2);
                        d4=d4+d3;

                        lTotal1.setText("Kshs. "+df.format(d4));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concudeday();
            }
        });



        FirebaseRecyclerAdapter<ledgerbook,ledgerbookholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ledgerbook,ledgerbookholder>(

                ledgerbook.class,
                R.layout.ledgerbook_row,
                ledgerbookholder.class,
                mD
        )
        {
            @Override
            protected void populateViewHolder(final ledgerbookholder viewHolder, final ledgerbook model, int position) {
                final String b_key=getRef(position).getKey();
                viewHolder.setGroup(model.getGroupname());
                viewHolder.setAmount(model.getAmountbanked());
                viewHolder.setAmountgiven(model.getAmountout());
                viewHolder.setOfficer(model.getUser());

                if (!model.getAmountbanked().equals("0")){
                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.present));
                }


                viewHolder.link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mD8.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(dat)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        if (snapshot.getKey().equals(b_key)){
                                            mD8.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(dat)).child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final String meetid=dataSnapshot.child("meetid").getValue().toString();
                                                    mDw.child(b_key).child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final String reportid=dataSnapshot.child("reportid").getValue().toString();
                                                            mDa.child(b_key).child("finalreport").child(reportid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    final String banking=dataSnapshot.child("cashtooffice").getValue().toString();
                                                                    final String meetingba=dataSnapshot.child("sftot").getValue().toString();
                                                                    name=dataSnapshot.child("facilitator").getValue().toString();
                                                                    dater=dataSnapshot.child("meetdate").getValue().toString();
                                                                    final String netcash=dataSnapshot.child("meettot").getValue().toString();
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(secrecords.this);
                                                                    builder.setTitle(model.getGroupname())
                                                                            .setMessage(model.getGroupname()+" Handover Details\n" +
                                                                                    "Meeting Banking: Kshs. "+banking+"\nSchool Fees Banking: Kshs. "+meetingba)
                                                                            .setPositiveButton("Print", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.dismiss();
                                                                                    if (Double.parseDouble(meetingba)>1){
                                                                                        counter=1;
                                                                                        printBillsf(netcash,banking,meetingba,model.getGroupname(),name,datex);
                                                                                    }else {
                                                                                        counter=0;
                                                                                        printBill(netcash,banking,meetingba,model.getGroupname(),name,datex);
                                                                                    }
                                                                                }
                                                                            })
                                                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });

                                                                    AlertDialog alert11 = builder.create();
                                                                    alert11.show();
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
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };

        mLedgerlist.setAdapter(firebaseRecyclerAdapter);

    }

    private void printBillsf(String amount, String all, String sf, String groupname, String name, String dater) {
        be=all;
        a=amount;
        c=sf;
        d=groupname;
        e=name;


        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+d,1,1);
                printText(leftRightAlign("Amount From Field\n\tKshs." , be));
                printNewLine();
                printText(leftRightAlign("Meeting Banking\n\tKshs." , a));
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , c));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                printBill3sf(a,be,c,d,e);

                DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("Handover").push();
                endmeetnot.child("facname").setValue(name);
                endmeetnot.child("detail").setValue(d+" Handover Details\n"+" Meeting Banking: Kshs. "+a+"\nSchool Fees Banking: Kshs. "+c);


                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printBill3sf(String amount, String all,String sf,String groupname,String name) {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+groupname,1,1);
                printText(leftRightAlign("Amount From Field\n\tKshs." , all));
                printNewLine();
                printText(leftRightAlign("Meeting Banking\n\tKshs." , amount));
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , sf));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                printBill2(sf,groupname);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printBill2(String sf,String groupname) {
        as=sf;
        bs=groupname;
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+groupname,1,1);
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , sf));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                printBill22(sf,groupname);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printBill22(String sf, String groupname) {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+groupname,1,1);
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , sf));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void concudeday() {

        final AlertDialog.Builder builders = new AlertDialog.Builder(secrecords.this);
        if(taskedit2.getParent() != null) {
            ((ViewGroup)taskedit2.getParent()).removeView(taskedit2); // <- fix
        }
        layout.addView(taskedit);
        layout.addView(task2);
        layout.addView(taskedit2);
        taskedit.setHint("Enter Bank Receipt Number..");
        task2.setHint("Depositing Officer..");
        builders.setTitle("Confirm Cash Details")
                .setMessage("Amount to Bank Secretary: Kshs. "+String.valueOf(d2)+"\nRebank Amount: Kshs. "+manamount)
                .setCancelable(true)
                .setView(layout)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double fmanaount=Double.parseDouble(manamount);
                        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(String.valueOf(d2));
                                DatabaseReference newdepo=mD5;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                                newdepo.child(date).child("Amountout").setValue(String.valueOf(d2));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        DatabaseReference reference3=mDx.child("Bank").child("Trans").child(yearx).child(monthx).child(date).push();
                        reference3.child("chequenumber").setValue(taskedit.getText().toString());
                        reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        reference3.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference3.child("status").setValue("completed");
                        reference3.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference3.child("withdraw").setValue("0");
                        reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference32=mDx.child("Bank").child("Trans").child(yearx).child(monthx).child("all").push();
                        reference32.child("chequenumber").setValue(taskedit.getText().toString());
                        reference32.child("date").setValue(date);
                        reference32.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference32.child("status").setValue("completed");
                        reference32.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference32.child("withdraw").setValue("0");
                        reference32.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference4=mDx.child("Bank").child("Trans").child("all").push();
                        reference4.child("chequenumber").setValue(taskedit.getText().toString());
                        reference4.child("date").setValue(date);
                        reference4.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference4.child("status").setValue("completed");
                        reference4.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference4.child("withdraw").setValue("0");
                        reference4.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference5=mDx.child("Bank").child("Accounts").child(bselected).child("Trans").child(yearx).child(monthx).child(date).push();
                        reference5.child("chequenumber").setValue(taskedit.getText().toString());
                        reference5.child("date").setValue(date);
                        reference5.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference5.child("status").setValue("completed");
                        reference5.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference5.child("withdraw").setValue("0");
                        reference5.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference5m=mDx.child("Bank").child("Accounts").child(bselected).child("Trans").child(yearx).child(monthx).child("all").push();
                        reference5m.child("chequenumber").setValue(taskedit.getText().toString());
                        reference5m.child("date").setValue(date);
                        reference5m.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference5m.child("status").setValue("completed");
                        reference5m.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference5m.child("withdraw").setValue("0");
                        reference5m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference6=mDx.child("Bank").child("Accounts").child(bselected).child("Trans").child("all").push();
                        reference6.child("chequenumber").setValue(taskedit.getText().toString());
                        reference6.child("date").setValue(date);
                        reference6.child("details").setValue("Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());
                        reference6.child("status").setValue("completed");
                        reference6.child("deposit").setValue(String.valueOf(d2+fmanaount));
                        reference6.child("withdraw").setValue("0");
                        reference6.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference r2eferene3d=mDx.child("Bank").child("Accounts").child(bselected).child("recents").child("depositstransaction");
                        r2eferene3d.child("amount").setValue(String.valueOf(d2+fmanaount));
                        r2eferene3d.child("madeby").setValue(user);
                        r2eferene3d.child("date").setValue(date);

                        DatabaseReference reference2petty=mDx.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("all").push();
                        reference2petty.child("name").setValue("Petty");
                        reference2petty.child("amount").setValue(String.valueOf(d2+fmanaount));
                        reference2petty.child("type").setValue("blue");
                        reference2petty.child("meet").setValue("-");
                        reference2petty.child("group").setValue("-");
                        reference2petty.child("creditac").setValue("Petty");
                        reference2petty.child("debitac").setValue("BankCash");
                        reference2petty.child("description").setValue("Cash to Bank");
                        reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference2pettym=mDx.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                        reference2pettym.child("name").setValue("Petty");
                        reference2pettym.child("amount").setValue(String.valueOf(d2+fmanaount));
                        reference2pettym.child("type").setValue("blue");
                        reference2pettym.child("meet").setValue("-");
                        reference2pettym.child("group").setValue("-");
                        reference2pettym.child("creditac").setValue("Petty");
                        reference2pettym.child("debitac").setValue("BankCash");
                        reference2pettym.child("description").setValue("Cash to Bank");
                        reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        DatabaseReference reference2pettya=mDx.child("Petty").child("Trans").child("all").push();
                        reference2pettya.child("name").setValue("Petty");
                        reference2pettya.child("amount").setValue(String.valueOf(d2+fmanaount));
                        reference2pettya.child("type").setValue("blue");
                        reference2pettya.child("meet").setValue("-");
                        reference2pettya.child("group").setValue("-");
                        reference2pettya.child("creditac").setValue("Petty");
                        reference2pettya.child("debitac").setValue("BankCash");
                        reference2pettya.child("description").setValue("Cash to Bank");
                        reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        ref.child(key).child("amountout").setValue("0");
                        ref.child(key).child("groupname").setValue("REBANK");
                        ref.child(key).child("amountbanked").setValue(manamount);
                        ref.child(key).child("user").setValue(key);
                        ref.child(key).child("status").setValue("received");

                        DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("Chequesdep").push();
                        endmeetnot.child("detail").setValue("Kshs. "+String.valueOf(d2)+" Cheque number: "+taskedit.getText().toString()+" Deposited by "+task2.getText().toString());


                        Toast.makeText(secrecords.this,"Banked!", Toast.LENGTH_LONG).show();
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


    //

    public static class ledgerbookholder extends RecyclerView.ViewHolder{
        private TextView link;
        private DatabaseReference mD2;
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        DecimalFormat df = new DecimalFormat("##,###,###.#");
        View mView;


        public ledgerbookholder(View itemView) {
            super(itemView);

            mView = itemView;
            link=itemView.findViewById(R.id.ledgerlink);
            mD2=FirebaseDatabase.getInstance().getReference().child("Employees");
            layout = (RelativeLayout) itemView.findViewById(R.id.ledgerbookcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroup(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.ledgergroup);
            loandate.setText(date);
        }
        public void setAmount(String detail){
            TextView amountgiven = (TextView)mView.findViewById(R.id.ledgeramount);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(detail)));
        }
        public void setAmountgiven(String detail){
            TextView amountgiven = (TextView)mView.findViewById(R.id.ledgeramountgiven);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(detail)));
        }
        public void setOfficer(String detail){
            mD2.child(detail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView amountgiven = (TextView)mView.findViewById(R.id.ledgerofficer);
                    amountgiven.setText(dataSnapshot.child("name").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    protected void printBill(String amount, String all, String sf, String groupname, String name,String dater) {

        a=all;
        be=amount;
        c=sf;
        d=groupname;
        e=name;


        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+d,1,1);
                printText(leftRightAlign("Amount From Field\n\tKshs." , be));
                printNewLine();
                printText(leftRightAlign("Meeting Banking\n\tKshs." , a));
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , c));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                printBill3(a,be,c,d,e);

                DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("Handover").push();
                endmeetnot.child("facname").setValue(name);
                endmeetnot.child("detail").setValue(d+" Handover Details\n"+" Meeting Banking: Kshs. "+a+"\nSchool Fees Banking: Kshs. "+c);


                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void printBill3(String amount, String all,String sf,String groupname,String name) {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime = dater;
                printCustom(dateTime,0,1);
                printNewLine();
                printCustom("Handover Receipt".toUpperCase(),1,1);
                printNewLine();
                printText(leftRightAlign("Officer: " , name));
                printNewLine();
                printNewLine();
                printCustom("Group Name: ".toUpperCase()+groupname,1,1);
                printText(leftRightAlign("Amount From Field\n\tKshs." , all));
                printNewLine();
                printText(leftRightAlign("Meeting Banking\n\tKshs." , amount));
                printNewLine();
                printText(leftRightAlign("School Fees Banking\n\tKshs." , sf));
                printNewLine();
                printNewLine();
                printCustom("--------",0,1);
                //printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();
                printNewLine();

                //printCustom("Officer "+mOfficer.getText().toString(),0,2);

                //printBill2(amount);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(str1.length() <20){
            int n = (20 - str1.length());
            ans = str1 + "      " + str2;
        }else {
            ans = str1 + "  " + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = DeviceList.getSocket();
            if(btsocket != null){
                if (counter==0){
                    printBill(a,be,c,d, name,dater);
                }else {
                    printBillsf(a,be,c,d, name,dater);
                }
                //printBill(a,be,c,d, name,dater);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLastDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.UK);
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf1.parse(datex)); //Nowusetodaydate.
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        c.add(Calendar.MONTH, -1); //Removing one

        return sdf.format(c.getTime());
    }

}
