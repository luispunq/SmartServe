package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class budgetreport extends AppCompatActivity {


    private RecyclerView budgetitems;
    private DatabaseReference mD,mD4,mD3,mD5,mDx;
    private String uid,user,cr,date,bselected=null,type,yearx,monthx,dayx,datex,mbalb4;
    private TextView mChamt,mBudgtamt,mbal,day,secc;
    private double v1=0,seccc=0,tosave;
    private FirebaseAuth mAuth;
    private Button mDone;
    LinearLayout layout;
    private EditText taskedit,task2;
    private Spinner taskedit2;
    private String amout=null,choise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetreport);
        budgetitems=findViewById(R.id.itemsadded);
        Bundle extras = getIntent().getExtras();
        final String datew = extras.getString("date");
        type = extras.getString("type");
        choise = extras.getString("choise");

        if (type.equals("super")){
            long dat=Long.parseLong(datew);
            yearx=new SimpleDateFormat("yyyy").format(dat);
            monthx=new SimpleDateFormat("MMM").format(dat);
            dayx=new SimpleDateFormat("EEE").format(dat);
            datex=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
            date=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
        }else {
            long dat=Long.parseLong(datew);
            yearx=new SimpleDateFormat("yyyy").format(dat);
            monthx=new SimpleDateFormat("MMM").format(dat);
            dayx=new SimpleDateFormat("EEE").format(dat);
            datex=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
            date=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
        }
        mChamt=findViewById(R.id.bankamnt);
        day=findViewById(R.id.budgetday);
        mBudgtamt=findViewById(R.id.budamount);
        mbal=findViewById(R.id.netba);
        secc=findViewById(R.id.secrecie);
        mDone=findViewById(R.id.button3);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        budgetitems.setHasFixedSize(true);
        budgetitems.setLayoutManager(new LinearLayoutManager(this));
        mD= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD4=FirebaseDatabase.getInstance().getReference().child("Accounts");
        mD3=FirebaseDatabase.getInstance().getReference().child("Employees");
        mD5=FirebaseDatabase.getInstance().getReference().child("Accounts").child(user);

        day.setText(date);

        layout=new LinearLayout(budgetreport.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        taskedit=new EditText(budgetreport.this);
        task2=new EditText(budgetreport.this);
        taskedit2=new Spinner(budgetreport.this);

        secc.setBackground(getResources().getDrawable(R.drawable.inputoutline));


        if (!date.equals(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()))){
            //mDone.setVisibility(View.GONE);
        }

        mD.child("Bank").child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> banks = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    String type=(String)dsp.getKey();
                    banks.add(type);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(budgetreport.this, android.R.layout.simple_spinner_item, banks);
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

        if (choise.equals("one")){

            mD.child("Expenses").child(yearx).child(monthx).child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final double camt = Double.parseDouble(dataSnapshot.child("chequeamount").getValue().toString());

                        mChamt.setText("Kshs. "+String.valueOf(camt));



                        mD.child("Expenses").child(yearx).child(monthx).child(date).child("items").child("daily").addValueEventListener(new ValueEventListener() {
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
                                mBudgtamt.setText("Kshs. "+String.valueOf(v1));
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

            mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("position").getValue().toString().equals("secretary")&&snapshot.child("Branch").getValue().toString().equals("Thika")){
                            uid=snapshot.getKey();

                            mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Balance").exists()){
                                        mbalb4=dataSnapshot.child(date).child("Amountout").getValue().toString();
                                        secc.setText("Kshs. "+mbalb4);
                                        mbal.setText("Kshs. "+mbalb4);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }else {

            //mDone.setVisibility(View.GONE);

            mD3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("position").getValue().toString().equals("secretary")&&snapshot.child("Branch").getValue().toString().equals("Thika")){
                            uid=snapshot.getKey();

                            mD4.child(uid).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String secamount=dataSnapshot.child("Amountout").getValue().toString();
                                    amout=secamount;

                                    secc.setText("Kshs. "+secamount);

                                    mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("Balance").exists()){
                                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                tosave=Double.parseDouble(amout)+Double.parseDouble(balb4);
                                            }else {
                                                String balb4="0";
                                                tosave=Double.parseDouble(amout)+Double.parseDouble(balb4);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    mD.child("Expenses").child(yearx).child(monthx).child(date).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                final double camt = Double.parseDouble(dataSnapshot.child("chequeamount").getValue().toString());

                                                mChamt.setText("Kshs. "+String.valueOf(camt));


                                                if (secc.getText().toString().isEmpty()){
                                                    seccc=0;
                                                }else {
                                                    seccc=Double.parseDouble(amout);
                                                }

                                                mD.child("Expenses").child(yearx).child(monthx).child(date).child("items").child("daily").addValueEventListener(new ValueEventListener() {
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
                                                        mbal.setText("Kshs. "+String.valueOf(tosave));
                                                        mBudgtamt.setText("Kshs. "+String.valueOf(v1));
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

                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }




        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concudeday();
            }
        });



        FirebaseRecyclerAdapter<expenseitemadded,expenseitemsaddedViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<expenseitemadded,expenseitemsaddedViewHolder>(

                expenseitemadded.class,
                R.layout.expenseitemadded_row,
                expenseitemsaddedViewHolder.class,
                mD.child("Expenses").child(yearx).child(monthx).child(date).child("items").child("daily")
        )
        {
            @Override
            protected void populateViewHolder(final expenseitemsaddedViewHolder viewHolder, final expenseitemadded model, final int position) {

                viewHolder.setName(model.getName());
                viewHolder.setCreditac(model.getCreditac());
                viewHolder.setDebitac(model.getDebitac());
                viewHolder.setPeriod(model.getDescription());
                viewHolder.setAmount(model.getAmount());

                viewHolder.viewHide();
            }
        };

        budgetitems.setAdapter(firebaseRecyclerAdapter);

    }

    private void










    concudeday() {

        final AlertDialog.Builder builders = new AlertDialog.Builder(budgetreport.this);
        layout.addView(taskedit);
        layout.addView(task2);
        layout.addView(taskedit2);
        taskedit.setHint("Enter Bank Receipt Number..");
        task2.setHint("Depositing Officer..");
        builders.setTitle("Confirm Cash Details")
                .setMessage("Amount from bank:  "+mChamt.getText().toString()+" \nBudget amount: "+mBudgtamt.getText().toString()+" \nAmount from Secretary: "+secc.getText().toString()+"\n Balance: Kshs. "+mbal.getText().toString())
                .setCancelable(true)
                .setView(layout)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mD.child("Unconfirmed").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        Object object=snapshot.getValue();
                                        DatabaseReference conclude=mD;
                                        //conclude.setValue(object);
                                    }

                                    /*DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("all").push();
                                    reference2petty.child("name").setValue("Petty");
                                    reference2petty.child("amount").setValue(mbalb4);
                                    reference2petty.child("type").setValue("blue");
                                    reference2petty.child("meet").setValue("-");
                                    reference2petty.child("group").setValue("-");
                                    reference2petty.child("creditac").setValue("Petty");
                                    reference2petty.child("debitac").setValue("BankCash");
                                    reference2petty.child("description").setValue("Cash to Bank");
                                    reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                    reference2pettym.child("name").setValue("Petty");
                                    reference2pettym.child("amount").setValue(mbalb4);
                                    reference2pettym.child("type").setValue("blue");
                                    reference2pettym.child("meet").setValue("-");
                                    reference2pettym.child("group").setValue("-");
                                    reference2pettym.child("creditac").setValue("Petty");
                                    reference2pettym.child("debitac").setValue("BankCash");
                                    reference2pettym.child("description").setValue("Cash to Bank");
                                    reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                    DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                    reference2pettya.child("name").setValue("Petty");
                                    reference2pettya.child("amount").setValue(mbalb4);
                                    reference2pettya.child("type").setValue("blue");
                                    reference2pettya.child("meet").setValue("-");
                                    reference2pettya.child("group").setValue("-");
                                    reference2pettya.child("creditac").setValue("Petty");
                                    reference2pettya.child("debitac").setValue("BankCash");
                                    reference2pettya.child("description").setValue("Cash to Bank");
                                    reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);*/
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(budgetreport.this,"Banked!", Toast.LENGTH_LONG).show();
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



    public static class expenseitemsaddedViewHolder extends RecyclerView.ViewHolder{
        private TextView remove;
        View mView;


        public expenseitemsaddedViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            remove=itemView.findViewById(R.id.btn);
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
        public void viewHide(){
            remove.setVisibility(View.INVISIBLE);
        }
    }
}
