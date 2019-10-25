package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class dailybudgetting extends AppCompatActivity {
    private EditText mchqamnt,mitmamt,mItemName,mChqnu,mDesc;
    private Button mProcess,mAdditems,mDone;
    private RecyclerView budgetitems;
    private DatabaseReference mD,mDatabase,mDx,mE;
    private FirebaseAuth mAuth;
    private Spinner mSitem,mBAnk;
    private String curritem,db,cr,eid,bselected,key;
    private TextView mChamt,mBudgtamt,mbal;
    private CheckBox ch4;
    private double v1=0,v2=0,todaycount=0,todaycount2=0,todaycount3,tod4,luuam=0;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailybudgetting);
        mchqamnt=findViewById(R.id.chqamnt);
        mChqnu=findViewById(R.id.chqnuo);
        mItemName=findViewById(R.id.itmname);
        mProcess=findViewById(R.id.addchq);
        mAdditems=findViewById(R.id.addiem);
        mDesc=findViewById(R.id.expdesc);
        mDone=findViewById(R.id.button3);
        mAuth=FirebaseAuth.getInstance();
        eid=mAuth.getCurrentUser().getUid();
        mSitem=findViewById(R.id.spnitems);
        mBAnk=findViewById(R.id.banknm);
        budgetitems=findViewById(R.id.itemsadded);
        mitmamt=findViewById(R.id.itmamount);
        mChamt=findViewById(R.id.bankamnt);
        mBudgtamt=findViewById(R.id.budamount);
        mbal=findViewById(R.id.netba);
        ch4=findViewById(R.id.checkBox4);
        budgetitems.setHasFixedSize(true);
        budgetitems.setLayoutManager(new LinearLayoutManager(this));

        mD= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDx= FirebaseDatabase.getInstance().getReference().child("Notifications").child("FieldStart");
        mE=FirebaseDatabase.getInstance().getReference().child("Employees");

        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");

        mE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("position").getValue().toString().equals("secretary")&&snapshot.child("Branch").getValue().toString().equals("Thika")){
                        key=snapshot.getKey();
                        Log.e("found","man");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mD.child("Bank").child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> banks = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.getKey();
                    banks.add(type);
                }
                banks.add("default");

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(dailybudgetting.this, android.R.layout.simple_spinner_item, banks);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBAnk.setAdapter(areasAdapter);

                mBAnk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bselected=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.child("name").getValue();
                    if (dsp.child("period").getValue().toString().equals("Daily")){
                        items.add(type);
                    }
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(dailybudgetting.this, android.R.layout.simple_spinner_item, items);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSitem.setAdapter(areasAdapter);

                mSitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        curritem=parent.getItemAtPosition(position).toString();
                        mItemName.setText(curritem);
                        mD.child("Expenses").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnap:dataSnapshot.getChildren()){
                                    String targetdoc= (String) dataSnap.child("name").getValue();
                                    if (targetdoc.equals(curritem)){
                                        db=(String)dataSnap.child("debitac").getValue().toString();
                                        cr=(String)dataSnap.child("creditac").getValue().toString();

                                    }
                                }

                                if (curritem.equals("Loans")){
                                    todaycount=0;
                                    mDatabase.child("meetings").child("schedule").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    mDatabase.child("details").child(snapshot.child("groupid").getValue().toString()).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child("togivefac").exists()&&!dataSnapshot.child("togivefac").equals("")){
                                                                todaycount=todaycount+Double.parseDouble(dataSnapshot.child("togivefac").getValue().toString());
                                                                mitmamt.setText(String.valueOf(todaycount));
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    //todaycount=0;
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else if (curritem.equals("Transport and Subsistence")){
                                    todaycount2=0;
                                    mDatabase.child("meetings").child("schedule").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    mDatabase.child("details").child(snapshot.child("groupid").getValue().toString()).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (!dataSnapshot.child("transport").getValue().toString().equals("")){
                                                                todaycount2=todaycount2+Double.parseDouble(dataSnapshot.child("transport").getValue().toString());
                                                                mitmamt.setText(String.valueOf(todaycount2));
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }


                                                    });

                                                    //todaycount2=0;
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else {
                                    mitmamt.setText("0");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        curritem=parent.getItemAtPosition(0).toString();
                        mItemName.setText(curritem);
                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recievecheque();
            }
        });

        mAdditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });


        FirebaseRecyclerAdapter<expenseitemadded,expenseitemsaddedViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<expenseitemadded,expenseitemsaddedViewHolder>(

                expenseitemadded.class,
                R.layout.expenseitemadded_row,
                expenseitemsaddedViewHolder.class,
                mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily")
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

                        final AlertDialog.Builder builders = new AlertDialog.Builder(dailybudgetting.this);
                        builders.setTitle("Confirm Item Removal")
                                .setMessage("Remove "+model.getDescription()+" ?")
                                .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference ref=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily");
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
        budgetitems.setAdapter(firebaseRecyclerAdapter);


        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v1=0;
                if (dataSnapshot.child("chequeamount").exists()) {
                    String camts = dataSnapshot.child("chequeamount").getValue().toString();
                    final double camt=Double.parseDouble(camts);
                    mchqamnt.setText(camts);
                    mChqnu.setText(dataSnapshot.child("chequenumber").getValue().toString());

                    mChamt.setText("Kshs. "+String.valueOf(camt));
                    mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            v1=0;
                            if (dataSnapshot.exists()){
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    if (snapshot.child("amount").exists()){
                                        double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                                        v1=v1+iamt;
                                    }
                                    mBudgtamt.setText(String.valueOf(v1));
                                    mbal.setText(String.valueOf(camt-v1));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }else {
                    String camts = "0";
                    final double camt=Double.parseDouble(camts);
                    mchqamnt.setText(camts);
                    mChqnu.setText("-");

                    mChamt.setText("Kshs. "+String.valueOf(camt));
                    mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            v1=0;
                            if (dataSnapshot1.exists()){
                                for (DataSnapshot snapshot:dataSnapshot1.getChildren()){
                                    if (snapshot.child("amount").exists()){
                                        double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                                        v1=v1+iamt;
                                    }
                                    mBudgtamt.setText(String.valueOf(v1));
                                    mbal.setText(String.valueOf(camt-v1));
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



        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggregate();
            }
        });


    }

    private void aggregate() {
        mProgress.setMessage("Aggregating day's Budget");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (!snapshot.child("name").getValue().toString().equals("Loans")&&!snapshot.child("name").getValue().toString().equals("Transport and Subsistence")){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            v2=v2+iamt;
                        }else {
                            String luua=snapshot.child("amount").getValue().toString();
                            luuam=luuam+Double.parseDouble(luua);
                        }
                    }
                }
                mDatabase.child("Accounts").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String balb4=dataSnapshot.child("Balance").getValue().toString();
                        double balb44=Double.parseDouble(balb4);
                        final double e=balb44-v2;

                        final AlertDialog.Builder builders = new AlertDialog.Builder(dailybudgetting.this);
                        builders.setTitle("Confirm Budget.")
                                .setMessage("Expense :"+String.valueOf(v2))
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference newdepo=mDatabase.child("Accounts").child(eid);
                                        newdepo.child("Balance").setValue(String.valueOf(e));
                                        dialog.dismiss();

                                        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    String cuu=snapshot.child("description").getValue().toString();
                                                    String cuua=snapshot.child("amount").getValue().toString();
                                                    String cuuname=snapshot.child("name").getValue().toString();

                                                    if (!snapshot.child("name").getValue().toString().equals("Loans")){
                                                        DatabaseReference reference3=mD.child("Expenses").child("Trans").child("all").push();
                                                        reference3.child("name").setValue(cuuname);
                                                        reference3.child("amount").setValue(cuua);
                                                        reference3.child("type").setValue("blue");
                                                        reference3.child("group").setValue("-");
                                                        reference3.child("meet").setValue("-");
                                                        reference3.child("debitac").setValue("Expense");
                                                        reference3.child("creditac").setValue("Petty");
                                                        reference3.child("description").setValue(cuu);
                                                        reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference reference3b=mD.child("Petty").child("Trans").child("all").push();
                                                        reference3b.child("name").setValue(cuuname);
                                                        reference3b.child("amount").setValue(cuua);
                                                        reference3b.child("type").setValue("red");
                                                        reference3b.child("group").setValue("-");
                                                        reference3b.child("meet").setValue("-");
                                                        reference3b.child("debitac").setValue("Expense");
                                                        reference3b.child("creditac").setValue("Petty");
                                                        reference3b.child("description").setValue(cuu);
                                                        reference3b.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                    }

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
                                        reference.child("description").setValue("Daily Expense");
                                        reference.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference mDd=mDx;
                                        mDd.removeValue();

                                        final EditText taskedit=new EditText(dailybudgetting.this);
                                        taskedit.setText(String.valueOf(luuam));
                                        final AlertDialog.Builder builders1 = new AlertDialog.Builder(dailybudgetting.this);
                                        builders1.setTitle("Enter Cash to Secretary")
                                                .setCancelable(true)
                                                .setView(taskedit)
                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        DatabaseReference newdepo=mD.child("Accounts").child(key).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                        newdepo.child("Amountin").setValue(taskedit.getText().toString());
                                                        newdepo.child("Amountout").setValue("0");


                                                        mD.child("Accounts").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("Balance").exists()){
                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                    double balb44=Double.parseDouble(balb4);
                                                                    double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                                                    DatabaseReference newdepo=mD.child("Accounts").child(eid);
                                                                    newdepo.child("Balance").setValue(String.valueOf(e));
                                                                }else {
                                                                    String balb4="0";
                                                                    double balb44=Double.parseDouble(balb4);
                                                                    double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                                                    DatabaseReference newdepo=mD.child("Accounts").child(eid);
                                                                    newdepo.child("Balance").setValue(String.valueOf(e));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        mD.child("Accounts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.child("Balance").exists()){
                                                                    String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                    double balb44=Double.parseDouble(balb4);
                                                                    double e=balb44+Double.parseDouble(taskedit.getText().toString());
                                                                    DatabaseReference newdepo=mD.child("Accounts").child(key);
                                                                    newdepo.child("Balance").setValue(String.valueOf(e));
                                                                }else {
                                                                    String balb4="0";
                                                                    double balb44=Double.parseDouble(balb4);
                                                                    double e=balb44+Double.parseDouble(taskedit.getText().toString());
                                                                    DatabaseReference newdepo=mD.child("Accounts").child(key);
                                                                    newdepo.child("Balance").setValue(String.valueOf(e));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        Intent bac = new Intent(dailybudgetting.this,managerhome.class);
                                                        startActivity(bac);
                                                        finish();

                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert1212 = builders1.create();
                                        alert1212.show();
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgress.dismiss();
    }

    private void additem() {
        String itemdesc=mDesc.getText().toString().trim();
        String itemamount=mitmamt.getText().toString().trim();

        DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").push();
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
        //mItemName.setText("");
    }

    private void recievecheque() {
        final String chequeno=mChqnu.getText().toString().trim();
        final String cheqeuamt=mchqamnt.getText().toString().trim();
        final EditText editText=new EditText(this);
        editText.setHint("Enter Officer..");
        final AlertDialog.Builder builders = new AlertDialog.Builder(dailybudgetting.this);
        builders.setTitle("Cheque Details")
                .setMessage("Please add details..")
                .setCancelable(true)
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    String oldchqnm=dataSnapshot.child("chequenumber").getValue().toString();
                                    double oldchqamt=Double.parseDouble(dataSnapshot.child("chequeamount").getValue().toString());
                                    final double newchqamt=oldchqamt+Double.parseDouble(cheqeuamt);
                                    String newchqnum=":"+oldchqnm+"and\n:"+chequeno;

                                    reference.child("chequenumber").setValue(newchqnum);
                                    reference.child("chequeamount").setValue(newchqamt);
                                    reference.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                    final DatabaseReference reference1=mDatabase.child("Accounts").child(eid).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            double ol=Double.parseDouble(dataSnapshot.child("Amountin").getValue().toString());
                                            double newoll=ol+Double.parseDouble(cheqeuamt);
                                            reference1.child("Amountin").setValue(newoll);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    final DatabaseReference reference2=mDatabase.child("Accounts").child(eid);
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            double ol=Double.parseDouble(dataSnapshot.child("Balance").getValue().toString());
                                            double newoll=ol+Double.parseDouble(cheqeuamt);
                                            reference2.child("Balance").setValue(newoll);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    DatabaseReference reference3=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    reference3.child("chequenumber").setValue(chequeno);
                                    reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference3.child("status").setValue("completed");
                                    reference3.child("deposit").setValue("0");
                                    reference3.child("withdraw").setValue(cheqeuamt);
                                    reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference3e=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    reference3e.child("chequenumber").setValue(chequeno);
                                    reference3e.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference3e.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference3e.child("status").setValue("completed");
                                    reference3e.child("deposit").setValue("0");
                                    reference3e.child("withdraw").setValue(cheqeuamt);
                                    reference3e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference referene3=mD.child("Bank").child("Trans").child("all").push();
                                    referene3.child("chequenumber").setValue(chequeno);
                                    referene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    referene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    referene3.child("status").setValue("completed");
                                    referene3.child("deposit").setValue("0");
                                    referene3.child("withdraw").setValue(cheqeuamt);
                                    referene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r1eference3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    r1eference3.child("chequenumber").setValue(chequeno);
                                    r1eference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r1eference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    r1eference3.child("status").setValue("completed");
                                    r1eference3.child("deposit").setValue("0");
                                    r1eference3.child("withdraw").setValue(cheqeuamt);
                                    r1eference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r1eference3m=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    r1eference3m.child("chequenumber").setValue(chequeno);
                                    r1eference3m.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r1eference3m.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    r1eference3m.child("status").setValue("completed");
                                    r1eference3m.child("deposit").setValue("0");
                                    r1eference3m.child("withdraw").setValue(cheqeuamt);
                                    r1eference3m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r2eferene3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child("all").push();
                                    r2eferene3.child("chequenumber").setValue(chequeno);
                                    r2eferene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r2eferene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    r2eferene3.child("status").setValue("completed");
                                    r2eferene3.child("deposit").setValue("0");
                                    r2eferene3.child("withdraw").setValue(cheqeuamt);
                                    r2eferene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    //-----


                                    DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("all").push();
                                    reference2petty.child("name").setValue("Petty");
                                    reference2petty.child("amount").setValue(cheqeuamt);
                                    reference2petty.child("type").setValue("blue");
                                    reference2petty.child("meet").setValue("-");
                                    reference2petty.child("group").setValue("-");
                                    reference2petty.child("debitac").setValue("Petty");
                                    reference2petty.child("creditac").setValue("BankCash");
                                    reference2petty.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    reference2pettym.child("name").setValue("Petty");
                                    reference2pettym.child("amount").setValue(cheqeuamt);
                                    reference2pettym.child("type").setValue("blue");
                                    reference2pettym.child("meet").setValue("-");
                                    reference2pettym.child("group").setValue("-");
                                    reference2pettym.child("debitac").setValue("Petty");
                                    reference2pettym.child("creditac").setValue("BankCash");
                                    reference2pettym.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                    reference2pettya.child("name").setValue("Petty");
                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                    reference2pettya.child("type").setValue("blue");
                                    reference2pettya.child("meet").setValue("-");
                                    reference2pettya.child("group").setValue("-");
                                    reference2pettya.child("debitac").setValue("Petty");
                                    reference2pettya.child("creditac").setValue("BankCash");
                                    reference2pettya.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    

                                    DatabaseReference r2eferene3d=mD.child("Bank").child("Accounts").child(bselected).child("recents").child("withdrawstransaction");
                                    r2eferene3d.child("amount").setValue(cheqeuamt);
                                    r2eferene3d.child("madeby").setValue(eid);
                                    r2eferene3d.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                    Toast.makeText(dailybudgetting.this,"Cheque Added",Toast.LENGTH_LONG).show();

                                    dialog.dismiss();

                                }else {
                                    reference.child("chequenumber").setValue(chequeno);
                                    reference.child("chequeamount").setValue(cheqeuamt);
                                    reference.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                    DatabaseReference reference1=mDatabase.child("Accounts").child(eid).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference1.child("Amountin").setValue(cheqeuamt);
                                    reference1.child("Amountout").setValue("0");

                                    DatabaseReference reference2=mDatabase.child("Accounts").child(eid);
                                    reference2.child("Balance").setValue(cheqeuamt);

                                    DatabaseReference reference3=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    reference3.child("chequenumber").setValue(chequeno);
                                    reference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    reference3.child("status").setValue("completed");
                                    reference3.child("deposit").setValue("0");
                                    reference3.child("withdraw").setValue(cheqeuamt);
                                    reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference3e=mD.child("Bank").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    reference3e.child("chequenumber").setValue(chequeno);
                                    reference3e.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    reference3e.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    reference3e.child("status").setValue("completed");
                                    reference3e.child("deposit").setValue("0");
                                    reference3e.child("withdraw").setValue(cheqeuamt);
                                    reference3e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference referene3=mD.child("Bank").child("Trans").child("all").push();
                                    referene3.child("chequenumber").setValue(chequeno);
                                    referene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    referene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    referene3.child("status").setValue("completed");
                                    referene3.child("deposit").setValue("0");
                                    referene3.child("withdraw").setValue(cheqeuamt);
                                    referene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r1eference3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                                    r1eference3.child("chequenumber").setValue(chequeno);
                                    r1eference3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r1eference3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    r1eference3.child("status").setValue("completed");
                                    r1eference3.child("deposit").setValue("0");
                                    r1eference3.child("withdraw").setValue(cheqeuamt);
                                    r1eference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r1eference3m=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    r1eference3m.child("chequenumber").setValue(chequeno);
                                    r1eference3m.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r1eference3m.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    r1eference3m.child("status").setValue("completed");
                                    r1eference3m.child("deposit").setValue("0");
                                    r1eference3m.child("withdraw").setValue(cheqeuamt);
                                    r1eference3m.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r2eferene3=mD.child("Bank").child("Accounts").child(bselected).child("Trans").child("all").push();
                                    r2eferene3.child("chequenumber").setValue(chequeno);
                                    r2eferene3.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                    r2eferene3.child("details").setValue("Cheque number: "+chequeno+" withdrawn by"+editText.getText().toString());
                                    r2eferene3.child("status").setValue("completed");
                                    r2eferene3.child("deposit").setValue("0");
                                    r2eferene3.child("withdraw").setValue(cheqeuamt);
                                    r2eferene3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    //-----

                                    DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("all").push();
                                    reference2petty.child("name").setValue("Petty");
                                    reference2petty.child("amount").setValue(cheqeuamt);
                                    reference2petty.child("type").setValue("blue");
                                    reference2petty.child("meet").setValue("-");
                                    reference2petty.child("group").setValue("-");
                                    reference2petty.child("debitac").setValue("Petty");
                                    reference2petty.child("creditac").setValue("BankCash");
                                    reference2petty.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    reference2pettym.child("name").setValue("Petty");
                                    reference2pettym.child("amount").setValue(cheqeuamt);
                                    reference2pettym.child("type").setValue("blue");
                                    reference2pettym.child("meet").setValue("-");
                                    reference2pettym.child("group").setValue("-");
                                    reference2pettym.child("debitac").setValue("Petty");
                                    reference2pettym.child("creditac").setValue("BankCash");
                                    reference2pettym.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                    reference2pettya.child("name").setValue("Petty");
                                    reference2pettya.child("amount").setValue(cheqeuamt);
                                    reference2pettya.child("type").setValue("blue");
                                    reference2pettya.child("meet").setValue("-");
                                    reference2pettya.child("group").setValue("-");
                                    reference2pettya.child("debitac").setValue("Petty");
                                    reference2pettya.child("creditac").setValue("BankCash");
                                    reference2pettya.child("description").setValue("Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());
                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference r2eferene3d=mD.child("Bank").child("Accounts").child(bselected).child("recents").child("withdrawstransaction");
                                    r2eferene3d.child("amount").setValue(cheqeuamt);
                                    r2eferene3d.child("madeby").setValue(eid);
                                    r2eferene3d.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                    DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("Chequeswith").push();
                                    endmeetnot.child("amount").setValue(cheqeuamt);
                                    endmeetnot.child("number").setValue(chequeno);
                                    endmeetnot.child("detail").setValue("Kshs. "+cheqeuamt+" Cheque number: "+chequeno+" withdrawn by "+editText.getText().toString());

                                    Toast.makeText(dailybudgetting.this,"Cheque Recieved",Toast.LENGTH_LONG).show();

                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
}
