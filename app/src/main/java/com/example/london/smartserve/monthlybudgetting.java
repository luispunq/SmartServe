package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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

public class monthlybudgetting extends AppCompatActivity {
    private EditText mchqamnt,mitmamt,mItemName,mChqnu,mDesc;
    private Button mProcess,mAdditems,mDone;
    private RecyclerView budgetitems;
    private DatabaseReference mD,mDatabase,mD1;
    private FirebaseAuth mAuth;
    private Spinner mSitem;
    private String curritem,db,cr,salaname,salo="0",eid;
    private TextView mChamt,mBudgtamt,mbal;
    private double v1=0,v2=0,advz,loanz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlybudgetting);
        mchqamnt=findViewById(R.id.chqamnt);
        mAuth=FirebaseAuth.getInstance();
        eid=mAuth.getCurrentUser().getUid();
        mChqnu=findViewById(R.id.chqnuo);
        mItemName=findViewById(R.id.itmname);
        mProcess=findViewById(R.id.addchq);
        mAdditems=findViewById(R.id.addiem);
        mDesc=findViewById(R.id.expdesc);
        mDone=findViewById(R.id.button3);
        mSitem=findViewById(R.id.spnitems);
        budgetitems=findViewById(R.id.itemsadded);
        mitmamt=findViewById(R.id.itmamount);
        mChamt=findViewById(R.id.bankamnt);
        mBudgtamt=findViewById(R.id.budamount);
        mbal=findViewById(R.id.netba);
        budgetitems.setHasFixedSize(true);
        budgetitems.setLayoutManager(new LinearLayoutManager(this));

        mD= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD1= FirebaseDatabase.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Employees");

        mD.child("Expenses").child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.child("name").getValue();
                    if (dsp.child("period").getValue().toString().equals("Monthly")){
                        items.add(type);
                    }
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(monthlybudgetting.this, android.R.layout.simple_spinner_item, items);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSitem.setAdapter(areasAdapter);

                mSitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        curritem=parent.getItemAtPosition(position).toString();
                        mItemName.setText(curritem);
                        mD.child("Expenses").child("items").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnap:dataSnapshot.getChildren()){
                                    String targetdoc= (String) dataSnap.child("name").getValue();
                                    if (targetdoc.equals(curritem)){
                                        db=(String)dataSnap.child("debitac").getValue().toString();
                                        cr=(String)dataSnap.child("creditac").getValue().toString();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if (curritem.equals("Salaries")){

                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    LinearLayout layout=new LinearLayout(monthlybudgetting.this);
                                    layout.setOrientation(LinearLayout.VERTICAL);

                                    final Spinner emps=new Spinner(monthlybudgetting.this);

                                    layout.addView(emps);


                                    final List<String> emplist = new ArrayList<>();
                                    for(DataSnapshot dspd : dataSnapshot.getChildren()){
                                        String type=(String)dspd.child("name").getValue();
                                            emplist.add(type);
                                    }
                                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(monthlybudgetting.this, android.R.layout.simple_spinner_item, emplist);
                                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    emps.setAdapter(areasAdapter);

                                    emps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            salaname=parent.getItemAtPosition(position).toString();
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        if (snapshot.child("name").getValue().toString().equals(salaname)){

                                                            double basicpay=Double.parseDouble(snapshot.child("basicpays").child("basicpay").getValue().toString());
                                                            double commz=Double.parseDouble(snapshot.child("basicpays").child("comms").getValue().toString());
                                                            double otherz=Double.parseDouble(snapshot.child("basicpays").child("other").getValue().toString());
                                                            double payez=Double.parseDouble(snapshot.child("deductions").child("paye").getValue().toString());
                                                            double nhifz=Double.parseDouble(snapshot.child("deductions").child("nhif").getValue().toString());
                                                            double nssfz=Double.parseDouble(snapshot.child("deductions").child("nssf").getValue().toString());
                                                            loanz=Double.parseDouble(snapshot.child("loans").child("totalloan").getValue().toString());
                                                            advz=Double.parseDouble(snapshot.child("advances").child("currentadvance").getValue().toString());

                                                            salo=String.valueOf(basicpay+commz+otherz-(payez+nhifz+nssfz+loanz+advz));

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

                                        }
                                    });

                                    final AlertDialog.Builder builders = new AlertDialog.Builder(monthlybudgetting.this);
                                    builders.setTitle("Select Employee")
                                            .setView(layout)
                                            .setCancelable(true)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mitmamt.setText(salo);
                                                    mDesc.setText("Salary for "+salaname);
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

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
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
                mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly")
        )
        {
            @Override
            protected void populateViewHolder(final expenseitemsaddedViewHolder viewHolder, final expenseitemadded model, final int position) {

                viewHolder.setName(model.getName());
                viewHolder.setCreditac(model.getCreditac());
                viewHolder.setDebitac(model.getDebitac());
                viewHolder.setPeriod(model.getDescription());
                viewHolder.setAmount(model.getAmount());

                if (model.getStatus().equals("pending")) {
                    viewHolder.remove.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.remove.setVisibility(View.GONE);
                }

                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builders = new AlertDialog.Builder(monthlybudgetting.this);
                        builders.setTitle("Confirm Item Removal")
                                .setMessage("Remove " + model.getDescription() + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference ref = mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly");
                                        ref.child(getRef(position).getKey()).removeValue();
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
        };

        budgetitems.setAdapter(firebaseRecyclerAdapter);

        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v1=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("amount").exists()){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            v1=v1+iamt;
                        }
                    }
                }

                mBudgtamt.setText(String.valueOf(v1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final AlertDialog.Builder builders = new AlertDialog.Builder(monthlybudgetting.this);
                builders.setTitle("Commit Expenses ?")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
                alert121.show();*/
                aggregate();

            }
        });


    }

    private void aggregate() {
        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    DatabaseReference rm=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly");
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (!snapshot.child("status").getValue().toString().equals("done")){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            final String nnn=snapshot.child("name").getValue().toString();
                            v2=v2+iamt;

                            final AlertDialog.Builder builders = new AlertDialog.Builder(monthlybudgetting.this);
                            builders.setTitle("Confirm budget item: "+nnn)
                                    .setMessage("Amount :"+String.valueOf(iamt))
                                    .setCancelable(true)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                        String cuu=snapshot.child("description").getValue().toString();
                                                        String cuua=snapshot.child("amount").getValue().toString();
                                                        String cuuna=snapshot.child("name").getValue().toString();

                                                        DatabaseReference reference3=mD.child("Expenses").child("Trans").child("all").push();
                                                        reference3.child("name").setValue(cuuna);
                                                        reference3.child("amount").setValue(cuua);
                                                        reference3.child("type").setValue("blue");
                                                        reference3.child("group").setValue("-");
                                                        reference3.child("meet").setValue("-");
                                                        reference3.child("debitac").setValue("Expense");
                                                        reference3.child("creditac").setValue("BankCash");
                                                        reference3.child("description").setValue(cuu);
                                                        reference3.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                        DatabaseReference reference3b=mD.child("BankCash").child("Trans").child("all").push();
                                                        reference3b.child("name").setValue(cuuna);
                                                        reference3b.child("amount").setValue(cuua);
                                                        reference3b.child("type").setValue("red");
                                                        reference3b.child("group").setValue("-");
                                                        reference3b.child("meet").setValue("-");
                                                        reference3b.child("debitac").setValue("Expense");
                                                        reference3b.child("creditac").setValue("BankCash");
                                                        reference3b.child("description").setValue(cuu);
                                                        reference3b.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            DatabaseReference reference=mD.child("Expenses").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                            reference.child("name").setValue("Monthly Expenses");
                                            reference.child("amount").setValue(String.valueOf(v2));
                                            reference.child("type").setValue("blue");
                                            reference.child("group").setValue("-");
                                            reference.child("meet").setValue("-");
                                            reference.child("debitac").setValue("Expense");
                                            reference.child("creditac").setValue("BankCash");
                                            reference.child("description").setValue(nnn);
                                            reference.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                            DatabaseReference reference2e=mD.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                            reference2e.child("name").setValue("Monthly Expenses");
                                            reference2e.child("amount").setValue(String.valueOf(v2));
                                            reference2e.child("type").setValue("red");
                                            reference2e.child("group").setValue("-");
                                            reference2e.child("meet").setValue("-");
                                            reference2e.child("debitac").setValue("Expense");
                                            reference2e.child("creditac").setValue("BankCash");
                                            reference2e.child("description").setValue(nnn);
                                            reference2e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                            dialog.dismiss();
                                            aggregate();

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
                            rm.child(snapshot.getKey()).child("status").setValue("done");
                            break;
                        }
                    }
                }
                mD1.child("Accounts").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String balb4=dataSnapshot.child("Balance").getValue().toString();
                        double balb44=Double.parseDouble(balb4);
                        final double e=balb44-v2;

                        DatabaseReference newdepo=mD1.child("Accounts").child(eid);
                        newdepo.child("Balance").setValue(String.valueOf(e));


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

    private void additem() {
        String itemdesc=mDesc.getText().toString().trim();
        String itemamount=mitmamt.getText().toString().trim();

        DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly").push();
        reference.child("status").setValue("pending");
        reference.child("name").setValue(curritem);
        reference.child("amount").setValue(itemamount);
        reference.child("debitac").setValue(db);
        reference.child("type").setValue("blue");
        reference.child("creditac").setValue(cr);
        reference.child("description").setValue(itemdesc);

        mDesc.setText("");
        mitmamt.setText("");
    }

    private void recievecheque() {
        String chequeno=mChqnu.getText().toString().trim();
        String cheqeuamt=mchqamnt.getText().toString().trim();

        DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("cheque");
        reference.child("chequenumber").setValue(chequeno);
        reference.child("chequeamount").setValue(cheqeuamt);
        reference.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
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
