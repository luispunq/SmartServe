package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class employeelistadapter extends FirebaseRecyclerAdapter<employee,employeesholder>{

    private Context context;
    private DatabaseReference mD,mD4;
    private String user;
    private FirebaseAuth mAuth;
    private double val1=0;


    public employeelistadapter(Class<employee> modelClass, int modelLayout, Class<employeesholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        mD= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();

        mD4= FirebaseDatabase.getInstance().getReference().child("Accounts");

    }

    @Override
    protected void populateViewHolder(employeesholder viewHolder, final employee model, int position) {
        final String key=getRef(position).getKey();


        
        viewHolder.empname.setText(model.getName());
        Glide.with(context).load(model.getImage()).into(viewHolder.empimage);
        viewHolder.empstatus.setText(model.getStatus());

        if (model.getStatus().equals("Not Engaged")){
            viewHolder.empstatus.setTextColor(BLUE);
        }else{
            viewHolder.empstatus.setTextColor(RED);
        }

        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("kke",key);


                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Options")
                        .setCancelable(true)
                        .setItems(R.array.Options32, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent usewinde = new Intent(context,emloyeelist.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("test", "back");
                                        extras.putString("user", key);
                                        usewinde.putExtras(extras);
                                        context.startActivity(usewinde);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        Intent userwinde2 = new Intent(context,emloyeelist.class);
                                        Bundle extras1 = new Bundle();
                                        extras1.putString("test", "to");
                                        extras1.putString("user", key);
                                        userwinde2.putExtras(extras1);
                                        context.startActivity(userwinde2);
                                        dialog.dismiss();
                                        break;
                                }
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

                mD.child("Accounting").child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            val1=0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.child("name").getValue().toString().equals("Loans")||snapshot.child("name").getValue().toString().equals("Transport and Subsistence")){

                                String amnt = snapshot.child("amount").getValue().toString();

                                val1 = val1 + Double.parseDouble(amnt);
                            }

                        }

                        }

                        mD.child("Employees").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String test=dataSnapshot.child("position").getValue().toString();
                                String test2=dataSnapshot.child("Branch").getValue().toString();
                                if (test.equals("secretary")&&test2.equals("Thika")){
                                    final EditText taskedit=new EditText(context);
                                    taskedit.setText(String.valueOf(val1));
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(context);
                                    builders.setTitle("Enter Cash to Secretary")
                                            .setCancelable(true)
                                            .setView(taskedit)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DatabaseReference newdepo=mD.child("Accounts").child(key).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    newdepo.child("Amountin").setValue(taskedit.getText().toString());
                                                    newdepo.child("Amountout").setValue("0");


                                                    mD.child("Accounts").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child("Balance").exists()){
                                                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                                                double balb44=Double.parseDouble(balb4);
                                                                double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                                                DatabaseReference newdepo=mD.child("Accounts").child(user);
                                                                newdepo.child("Balance").setValue(String.valueOf(e));
                                                            }else {
                                                                String balb4="0";
                                                                double balb44=Double.parseDouble(balb4);
                                                                double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                                                DatabaseReference newdepo=mD.child("Accounts").child(user);
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
                                }else {
                                    Intent moresavings= new Intent(context,emppopup.class);
                                    moresavings.putExtra("User_Key",key);
                                    context.startActivity(moresavings);
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
        });

    }
}
