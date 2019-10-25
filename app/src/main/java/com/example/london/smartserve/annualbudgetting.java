package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class annualbudgetting extends AppCompatActivity {
    private EditText mchqamnt,mitmamt,mItemName,mChqnu,mDesc;
    private Button mProcess,mAdditems,mDone;
    private RecyclerView budgetitems;
    private DatabaseReference mD,mDatabase;
    private Spinner mSitem;
    private String curritem,db,cr,eid;
    private TextView mChamt,mBudgtamt,mbal;
    private double v1=0,v2=0;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annualbudgetting);
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
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");

        mD= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabase= FirebaseDatabase.getInstance().getReference();

        mD.child("Expenses").child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.child("name").getValue();
                    if (dsp.child("period").getValue().toString().equals("Annual")){
                        items.add(type);
                    }
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(annualbudgetting.this, android.R.layout.simple_spinner_item, items);
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
                mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual")
        )
        {
            @Override
            protected void populateViewHolder(final expenseitemsaddedViewHolder viewHolder, final expenseitemadded model, final int position) {

                if (model.getStatus().equals("pending")){

                    viewHolder.setName(model.getName());
                    viewHolder.setCreditac(model.getCreditac());
                    viewHolder.setDebitac(model.getDebitac());
                    viewHolder.setPeriod(model.getDescription());
                    viewHolder.setAmount(model.getAmount());

                    viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final AlertDialog.Builder builders = new AlertDialog.Builder(annualbudgetting.this);
                            builders.setTitle("Confirm Item Removal")
                                    .setMessage("Remove "+model.getDescription()+" ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseReference ref=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual");
                                            ref.child(getRef(position).getKey()).removeValue();
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(),model.getName()+" removed from list",Toast.LENGTH_LONG).show();
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
                }else {
                    viewHolder.remove.setVisibility(View.GONE);
                }
            }
        };

        budgetitems.setAdapter(firebaseRecyclerAdapter);

        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual").addValueEventListener(new ValueEventListener() {
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
                final AlertDialog.Builder builders = new AlertDialog.Builder(annualbudgetting.this);
                builders.setTitle("Commit Expenses ?")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                aggregate();
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

    private void aggregate() {
        mProgress.setMessage("Aggregating day's Budget");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    DatabaseReference rm=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual");
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (!snapshot.child("status").getValue().toString().equals("done")){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            rm.child(snapshot.getKey()).child("status").setValue("done");
                            v2=v2+iamt;
                        }
                    }
                }
                mDatabase.child("Accounts").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String balb4=dataSnapshot.child("Balance").getValue().toString();
                        double balb44=Double.parseDouble(balb4);
                        final double e=balb44-v2;

                        final AlertDialog.Builder builders = new AlertDialog.Builder(annualbudgetting.this);
                        builders.setTitle("Confirm Budget.")
                                .setMessage("Expense :"+String.valueOf(v2))
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference newdepo=mDatabase.child("Accounts").child(eid);
                                        newdepo.child("Balance").setValue(String.valueOf(e));
                                        dialog.dismiss();

                                        DatabaseReference reference=mD.child("Expenses").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                        reference.child("name").setValue("Expenses");
                                        reference.child("amount").setValue(String.valueOf(v2));
                                        reference.child("type").setValue("blue");
                                        reference.child("group").setValue("-");
                                        reference.child("meet").setValue("-");
                                        reference.child("debitac").setValue("Expense");
                                        reference.child("creditac").setValue("Bank/Cash");
                                        reference.child("description").setValue("Daily Expense");
                                        reference.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference referencer=mD.child("Expenses").child("Trans").child("all").push();
                                        referencer.child("name").setValue("Expenses");
                                        referencer.child("amount").setValue(String.valueOf(v2));
                                        referencer.child("type").setValue("blue");
                                        referencer.child("group").setValue("-");
                                        referencer.child("meet").setValue("-");
                                        referencer.child("debitac").setValue("Expense");
                                        referencer.child("creditac").setValue("Bank/Cash");
                                        referencer.child("description").setValue("Daily Expense");
                                        referencer.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference reference2=mD.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE").format(new Date())).push();
                                        reference2.child("name").setValue(curritem);
                                        reference2.child("amount").setValue(String.valueOf(v2));
                                        reference2.child("type").setValue("red");
                                        reference2.child("group").setValue("-");
                                        reference2.child("meet").setValue("-");
                                        reference2.child("debitac").setValue("Expense");
                                        reference2.child("creditac").setValue("Bank/Cash");
                                        reference2.child("description").setValue("Daily Expense");
                                        reference2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference reference2e=mD.child("BankCash").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                        reference2e.child("name").setValue(curritem);
                                        reference2e.child("amount").setValue(String.valueOf(v2));
                                        reference2e.child("type").setValue("red");
                                        reference2e.child("group").setValue("-");
                                        reference2e.child("meet").setValue("-");
                                        reference2e.child("debitac").setValue("Expense");
                                        reference2e.child("creditac").setValue("Bank/Cash");
                                        reference2e.child("description").setValue("Daily Expense");
                                        reference2e.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        DatabaseReference reference2r=mD.child("BankCash").child("Trans").child("all").push();
                                        reference2r.child("name").setValue(curritem);
                                        reference2r.child("amount").setValue(String.valueOf(v2));
                                        reference2r.child("type").setValue("red");
                                        reference2r.child("group").setValue("-");
                                        reference2r.child("meet").setValue("-");
                                        reference2r.child("debitac").setValue("Expense");
                                        reference2r.child("creditac").setValue("Bank/Cash");
                                        reference2r.child("description").setValue("Daily Expense");
                                        reference2r.child("timestamp").setValue(ServerValue.TIMESTAMP);


                                        Intent bac = new Intent(annualbudgetting.this,managerhome.class);
                                        startActivity(bac);
                                        finish();
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

        DatabaseReference reference=mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual").push();
        reference.child("name").setValue(curritem);
        reference.child("amount").setValue(itemamount);
        reference.child("debitac").setValue(db);
        reference.child("creditac").setValue(cr);
        reference.child("description").setValue(itemdesc);
        reference.child("status").setValue("pending");

        Toast.makeText(this,curritem+" added to list",Toast.LENGTH_LONG).show();
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
