package com.example.london.smartserve;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class emloyeelist extends AppCompatActivity {
    private TextView amto,ambk,mFac;
    private ImageView imageView;
    private Button mRc,mHo;
    private DatabaseReference mD2,mD3,mD4,mD5,mD,mD8,mDa,mDw,mD2x;
    private String user,test=null,unow=null,name=null,oldamt,oldmisc,b,amm,a=null,be=null,c=null,d=null,unowtype,usertype,ttype;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    byte FONT_TYPE;
    private int flag=0;

    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emloyeelist);
        Bundle extras = getIntent().getExtras();
        test = extras.getString("test");
        user=getIntent().getExtras().getString("user");
        amto=findViewById(R.id.fieldamountgiven);
        mAuth=FirebaseAuth.getInstance();
        unow=mAuth.getCurrentUser().getUid();
        mFac=findViewById(R.id.cordname);
        imageView=findViewById(R.id.cordprofpic);
        mRc=findViewById(R.id.button12);
        mHo=findViewById(R.id.button13);
        recyclerView=findViewById(R.id.grpsgiven);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mD2=FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD2x=FirebaseDatabase.getInstance().getReference().child("Employees").child(unow);
        mD3=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user);
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(unow);
        mD4= FirebaseDatabase.getInstance().getReference().child("Accounts").child(user);
        mD=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD8=FirebaseDatabase.getInstance().getReference().child("meetings").child("master");
        mDa=FirebaseDatabase.getInstance().getReference().child("reports");
        mDw=FirebaseDatabase.getInstance().getReference().child("details");

        myCalendar=Calendar.getInstance();

        mD2x.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               unowtype=dataSnapshot.child("Branch").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usertype=dataSnapshot.child("Branch").getValue().toString();
                ttype=dataSnapshot.child("position").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Balance").exists()){
                    b=dataSnapshot.child("Balance").getValue().toString();
                }else {
                    b="0";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                final String fieldid=dataSnapshot.child("fieldid").getValue().toString();
                mFac.setText(name);
                Picasso.with(getApplicationContext()).load(image).into(imageView);


                mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (test.equals("to")){
                            TextView textView=findViewById(R.id.title1);
                            textView.setText("Amount Given");
                            if (dataSnapshot.child("amountgiven").exists()){
                            amto.setText("Kshs. "+dataSnapshot.child("amountgiven").getValue().toString());
                            }else {
                                amto.setText("Kshs. 0");
                            }


                            mRc.setVisibility(View.GONE);
                            mHo.setVisibility(View.VISIBLE);
                        }else{
                            TextView textView=findViewById(R.id.title1);
                            textView.setText("Amount Back");
                            mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Balance").exists()){

                                        amto.setText("Kshs. "+dataSnapshot.child("Balance").getValue().toString());
                                    }else {
                                        amto.setText("Kshs. 0");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mRc.setVisibility(View.VISIBLE);
                            mHo.setVisibility(View.GONE);
                        }

                        mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChildren()){
                                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupgivenViewholder>(
                                            groupsgiven.class,
                                            R.layout.groupgivenlist_row,
                                            GroupgivenViewholder.class,
                                            mD3.child(lastyear()).child(fieldid).child("groupsgiven")
                                    )
                                    {
                                        @Override
                                        protected void populateViewHolder(final GroupgivenViewholder viewHolder, final groupsgiven model, int position) {
                                            viewHolder.setGroupName(model.getName());
                                            viewHolder.setGroupAmount(model.getAmount());

                                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent loan = new Intent(emloyeelist.this,report2.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("key", model.getGroupid());
                                                    extras.putString("date",lastyear());
                                                    loan.putExtras(extras);
                                                    startActivity(loan);
                                                }
                                            });
                                        }
                                    };

                                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                                }else {
                                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupgivenViewholder>(
                                            groupsgiven.class,
                                            R.layout.groupgivenlist_row,
                                            GroupgivenViewholder.class,
                                            mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).child("groupsgiven")
                                    )
                                    {
                                        @Override
                                        protected void populateViewHolder(final GroupgivenViewholder viewHolder, final groupsgiven model, int position) {
                                            viewHolder.setGroupName(model.getName());
                                            viewHolder.setGroupAmount(model.getAmount());

                                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent loan = new Intent(emloyeelist.this,report2.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("key", model.getGroupid());
                                                    extras.putString("date",new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                                    loan.putExtras(extras);
                                                    startActivity(loan);
                                                }
                                            });
                                        }
                                    };

                                    recyclerView.setAdapter(firebaseRecyclerAdapter);
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
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Log.e("date",new SimpleDateFormat("EEE, MMM d, ''yy").format((myCalendar.getTime())));

                        DatabaseReference db=mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format((myCalendar.getTime()))).child(fieldid);
                        db.child("status").setValue("Recieved");

                        receiveing(fieldid);
                    }

                };

                mRc.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(emloyeelist.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();






                    }
                });



                mHo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference db=mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid);
                        db.child("status").setValue("Handed Over");

                        giving(fieldid);


                        /*if (ttype.equals("secretary")){
                            givesec();
                        }else {
                            if (unowtype.equals(usertype)){
                                giving(fieldid);
                            }else {
                                Toast.makeText(emloyeelist.this,"Employee is in another branch.",Toast.LENGTH_LONG).show();
                            }
                        }*/

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void givesec() {
        final TextView editText=new TextView(emloyeelist.this);
        editText.setHint("Enter Amount Given");
        final AlertDialog.Builder builders = new AlertDialog.Builder(emloyeelist.this);
        builders.setTitle("Branch Handover")
                .setMessage("Transfer to "+usertype+" branch.")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(editText.getText().toString());
                                DatabaseReference newdepo=mD5;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44+Double.parseDouble(editText.getText().toString());
                                DatabaseReference newdepo=mD4;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(emloyeelist.this,"Given from "+usertype+" branch.",Toast.LENGTH_LONG).show();

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

    private void receivesec() {
        final TextView editText=new TextView(emloyeelist.this);
        editText.setHint("Enter Amount Recieved");
        final AlertDialog.Builder builders = new AlertDialog.Builder(emloyeelist.this);
        builders.setTitle("Branch Handover")
                .setMessage("Receive from "+usertype+" branch.")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44+Double.parseDouble(editText.getText().toString());
                                DatabaseReference newdepo=mD5;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(editText.getText().toString());
                                DatabaseReference newdepo=mD4;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(emloyeelist.this,"Received from "+usertype+" branch.",Toast.LENGTH_LONG).show();

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

    private void giving(final String fieldid) {
        mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("amountgiven").getValue().toString().isEmpty()&&!dataSnapshot.child("misccash").getValue().toString().isEmpty())
                {
                oldamt=dataSnapshot.child("amountgiven").getValue().toString();
                oldmisc=dataSnapshot.child("misccash").getValue().toString();
                }else {
                    oldamt="0";
                    oldmisc="0";
                }

                double tot=Double.parseDouble(oldamt)+Double.parseDouble(oldmisc);

                final EditText taskedit=new EditText(emloyeelist.this);
                taskedit.setHint("Enter Cash Given");
                taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                final AlertDialog.Builder buildes = new AlertDialog.Builder(emloyeelist.this);
                buildes.setTitle("Confirm Handover")
                        .setMessage("Give Kshs. "+String.valueOf(tot)+" To\n"+name)
                        .setView(taskedit)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference newfield=mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid);
                                newfield.child("actualamountgiven").setValue(taskedit.getText().toString());
                                DatabaseReference newfields=mD3.child("all").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                newfields.child("amountgiven").setValue(taskedit.getText().toString());
                                newfields.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                DatabaseReference newdepo=mD4.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                newdepo.child("Amountin").setValue(taskedit.getText().toString());
                                newdepo.child("Amountout").setValue("0");

                                amm="Kshs. "+taskedit.getText().toString();

                                mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("Balance").exists()){
                                            String balb4=dataSnapshot.child("Balance").getValue().toString();
                                            double balb44=Double.parseDouble(balb4);
                                            double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                            DatabaseReference newdepo=mD5;
                                            newdepo.child("Balance").setValue(String.valueOf(e));
                                        }else {
                                            String balb4="0";
                                            double balb44=Double.parseDouble(balb4);
                                            double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                            DatabaseReference newdepo=mD5;
                                            newdepo.child("Balance").setValue(String.valueOf(e));
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("Balance").exists()){
                                            String balb4=dataSnapshot.child("Balance").getValue().toString();
                                            double balb44=Double.parseDouble(balb4);
                                            double e=balb44+Double.parseDouble(taskedit.getText().toString());
                                            DatabaseReference newdepo=mD4;
                                            newdepo.child("Balance").setValue(String.valueOf(e));
                                            newdepo.child("Flag").setValue("up");
                                        }else {
                                            String balb4="0";
                                            double balb44=Double.parseDouble(balb4);
                                            double e=balb44+Double.parseDouble(taskedit.getText().toString());
                                            DatabaseReference newdepo=mD4;
                                            newdepo.child("Balance").setValue(String.valueOf(e));
                                            newdepo.child("Flag").setValue("up");
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            ref.child(snapshot.getKey()).child("groupname").setValue(snapshot.child("name").getValue().toString());
                                            ref.child(snapshot.getKey()).child("amountout").setValue(snapshot.child("amount").getValue().toString());
                                            ref.child(snapshot.getKey()).child("amountbanked").setValue("0");
                                            ref.child(snapshot.getKey()).child("user").setValue(user);
                                            ref.child(snapshot.getKey()).child("status").setValue("given");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mD3.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            final String grpk=snapshot.getKey();

                                            mD8.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpk).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    mDw.child(grpk).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final String cfof=dataSnapshot.child("cashfromoffice").getValue().toString();
                                                            final String groupname=dataSnapshot.child("groupName").getValue().toString();

                                                            DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("all").push();
                                                            reference2petty.child("name").setValue("Petty");
                                                            reference2petty.child("amount").setValue(cfof);
                                                            reference2petty.child("type").setValue("red");
                                                            reference2petty.child("meet").setValue("-");
                                                            reference2petty.child("group").setValue("-");
                                                            reference2petty.child("debitac").setValue(name);
                                                            reference2petty.child("creditac").setValue("Petty");
                                                            reference2petty.child("description").setValue("Group: "+groupname+ " Loan, by "+name);
                                                            reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                            DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                            reference2pettym.child("name").setValue("Petty");
                                                            reference2pettym.child("amount").setValue(cfof);
                                                            reference2pettym.child("type").setValue("red");
                                                            reference2pettym.child("meet").setValue("-");
                                                            reference2pettym.child("group").setValue("-");
                                                            reference2pettym.child("debitac").setValue(name);
                                                            reference2pettym.child("creditac").setValue("Petty");
                                                            reference2pettym.child("description").setValue("Group: "+groupname+ " Loan, by "+name);
                                                            reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                            DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                                            reference2pettya.child("name").setValue("Loans");
                                                            reference2pettya.child("amount").setValue(cfof);
                                                            reference2pettya.child("type").setValue("blue");
                                                            reference2pettya.child("meet").setValue("-");
                                                            reference2pettya.child("group").setValue("-");
                                                            reference2pettya.child("debitac").setValue(name);
                                                            reference2pettya.child("creditac").setValue("Petty");
                                                            reference2pettya.child("description").setValue("Group: "+groupname+ " Loan, by "+name);
                                                            reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                            DatabaseReference unhanded=mD.child("Unhanded").child("Trans").child("all").push();
                                                            unhanded.child("name").setValue("Loans");
                                                            unhanded.child("amount").setValue(cfof);
                                                            unhanded.child("type").setValue("blue");
                                                            unhanded.child("meet").setValue("-");
                                                            unhanded.child("group").setValue("-");
                                                            unhanded.child("debitac").setValue("Unhanded");
                                                            unhanded.child("creditac").setValue("Petty");
                                                            unhanded.child("description").setValue(name+"'s Imprest for Loan to "+groupname);
                                                            unhanded.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                            DatabaseReference officerim=mD.child("Imprests").child(name).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Trans").child("all").push();
                                                            officerim.child("name").setValue("Loans");
                                                            officerim.child("amount").setValue(cfof);
                                                            officerim.child("type").setValue("blue");
                                                            officerim.child("meet").setValue("-");
                                                            officerim.child("group").setValue("-");
                                                            officerim.child("creditac").setValue("Petty");
                                                            officerim.child("debitac").setValue(name);
                                                            officerim.child("description").setValue("Loan to "+groupname);
                                                            officerim.child("timestamp").setValue(ServerValue.TIMESTAMP);


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

                                Toast.makeText(emloyeelist.this,"Given!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                Intent bac = new Intent(emloyeelist.this,secretary.class);
                                bac.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(bac);
                                finish();
                            }
                        })
                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent bac = new Intent(emloyeelist.this,secretary.class);
                                bac.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(bac);
                                finish();
                            }
                        });
                AlertDialog alert11 = buildes.create();
                alert11.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void receiveing(final String fieldid) {

        final String oldamt=b;
        final EditText taskedit=new EditText(emloyeelist.this);
        taskedit.setHint("Enter Cash Received");
        taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builders = new AlertDialog.Builder(emloyeelist.this);
        builders.setTitle("Confirm Reception")
                .setMessage("Receive Kshs. "+oldamt+" From "+name)
                .setView(taskedit)
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        amm=taskedit.getText().toString();
                        DatabaseReference newfield=mD3.child(updateLabel()).child(fieldid);
                        newfield.child("actualcashback").setValue(taskedit.getText().toString());
                        DatabaseReference newfields=mD3.child("all").child(updateLabel());
                        newfields.child("cashback").setValue(taskedit.getText().toString());

                        DatabaseReference newdepo=mD4.child(updateLabel());
                        newdepo.child("Amountout").setValue(oldamt);

                        mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44+Double.parseDouble(taskedit.getText().toString());
                                DatabaseReference newdepo=mD5;
                                newdepo.child("Balance").setValue(String.valueOf(e));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String balb4=dataSnapshot.child("Balance").getValue().toString();
                                double balb44=Double.parseDouble(balb4);
                                double e=balb44-Double.parseDouble(taskedit.getText().toString());
                                DatabaseReference newdepo=mD4;
                                newdepo.child("Balance").setValue(String.valueOf(e));
                                newdepo.child("Flag").setValue("down");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mD3.child(updateLabel()).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(updateLabel());
                                    mD3.child(updateLabel()).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                final String grpk=snapshot.getKey();

                                                mD8.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpk).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            final String meetid=dataSnapshot.child("meetid").getValue().toString();
                                                            final String groupname=dataSnapshot.child("groupName").getValue().toString();
                                                            mDw.child(grpk).child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    final String reportid=dataSnapshot.child("reportid").getValue().toString();

                                                                    mDa.child(grpk).child("finalreport").child(reportid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String banking=dataSnapshot.child("cashtooffice").getValue().toString();
                                                                            String meetingba=dataSnapshot.child("sftot").getValue().toString();

                                                                            DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(updateLabel()).child("all").push();
                                                                            reference2petty.child("name").setValue("Petty");
                                                                            reference2petty.child("amount").setValue(banking);
                                                                            reference2petty.child("type").setValue("red");
                                                                            reference2petty.child("meet").setValue("-");
                                                                            reference2petty.child("group").setValue("-");
                                                                            reference2petty.child("creditac").setValue(name);
                                                                            reference2petty.child("debitac").setValue("Petty");
                                                                            reference2petty.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                            reference2pettym.child("name").setValue("Petty");
                                                                            reference2pettym.child("amount").setValue(banking);
                                                                            reference2pettym.child("type").setValue("blue");
                                                                            reference2pettym.child("meet").setValue("-");
                                                                            reference2pettym.child("group").setValue("-");
                                                                            reference2pettym.child("creditac").setValue(name);
                                                                            reference2pettym.child("debitac").setValue("Petty");
                                                                            reference2pettym.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                                                            reference2pettya.child("name").setValue("Petty");
                                                                            reference2pettya.child("amount").setValue(banking);
                                                                            reference2pettya.child("type").setValue("blue");
                                                                            reference2pettya.child("meet").setValue("-");
                                                                            reference2pettya.child("group").setValue("-");
                                                                            reference2pettya.child("creditac").setValue(name);
                                                                            reference2pettya.child("debitac").setValue("Petty");
                                                                            reference2pettya.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference unhandedb=mD.child("Unhanded").child("Trans").child("all").push();
                                                                            unhandedb.child("name").setValue("Unhanded");
                                                                            unhandedb.child("amount").setValue(banking);
                                                                            unhandedb.child("type").setValue("red");
                                                                            unhandedb.child("meet").setValue("-");
                                                                            unhandedb.child("group").setValue("-");
                                                                            unhandedb.child("debitac").setValue("Petty");
                                                                            unhandedb.child("creditac").setValue("Unhanded");
                                                                            unhandedb.child("description").setValue(groupname+" handovers."+" by "+name);
                                                                            unhandedb.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            if (Double.parseDouble(amm)>Double.parseDouble(oldamt)){
                                                                                DatabaseReference unhandedb1=mD.child("Unhanded").child("Trans").child("all").push();
                                                                                unhandedb1.child("name").setValue("Unhanded");
                                                                                unhandedb1.child("amount").setValue(Double.parseDouble(amm)-Double.parseDouble(oldamt));
                                                                                unhandedb1.child("type").setValue("blue");
                                                                                unhandedb1.child("meet").setValue("-");
                                                                                unhandedb1.child("group").setValue("-");
                                                                                unhandedb1.child("debitac").setValue("Unhanded");
                                                                                unhandedb1.child("creditac").setValue(name);
                                                                                unhandedb1.child("description").setValue(groupname+" excess handover."+" by "+name);
                                                                                unhandedb1.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                            }else if (Double.parseDouble(amm)<Double.parseDouble(oldamt)){
                                                                                DatabaseReference unhandedb1=mD.child("Unhanded").child("Trans").child("all").push();
                                                                                unhandedb1.child("name").setValue("Unhanded");
                                                                                unhandedb1.child("amount").setValue(Double.parseDouble(oldamt)-Double.parseDouble(amm));
                                                                                unhandedb1.child("type").setValue("red");
                                                                                unhandedb1.child("meet").setValue("-");
                                                                                unhandedb1.child("group").setValue("-");
                                                                                unhandedb1.child("debitac").setValue(name);
                                                                                unhandedb1.child("creditac").setValue("Unhanded");
                                                                                unhandedb1.child("description").setValue(groupname+" less handover."+" by "+name);
                                                                                unhandedb1.child("timestamp").setValue(ServerValue.TIMESTAMP);
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
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mD3.child(updateLabel()).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                if (snapshot.child("amountback").exists()){
                                                    ref.child(snapshot.getKey()).child("groupname").setValue(snapshot.child("name").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("amountbanked").setValue(snapshot.child("amountback").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("user").setValue(user);
                                                    ref.child(snapshot.getKey()).child("status").setValue("received");
                                                }else {
                                                    ref.child(snapshot.getKey()).child("groupname").setValue(snapshot.child("name").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("amountbanked").setValue("0");
                                                    ref.child(snapshot.getKey()).child("user").setValue(user);
                                                    ref.child(snapshot.getKey()).child("status").setValue("received");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers").child(getLastDate());
                                    mD3.child(getLastDate()).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                final String grpk=snapshot.getKey();

                                                mD8.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(grpk).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            final String meetid=dataSnapshot.child("meetid").getValue().toString();
                                                            final String groupname=dataSnapshot.child("groupName").getValue().toString();
                                                            mDw.child(grpk).child("meetings").child(meetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    final String reportid=dataSnapshot.child("reportid").getValue().toString();

                                                                    mDa.child(grpk).child("finalreport").child(reportid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String banking=dataSnapshot.child("cashtooffice").getValue().toString();
                                                                            String meetingba=dataSnapshot.child("sftot").getValue().toString();

                                                                            DatabaseReference reference2petty=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(updateLabel()).child("all").push();
                                                                            reference2petty.child("name").setValue("Petty");
                                                                            reference2petty.child("amount").setValue(banking);
                                                                            reference2petty.child("type").setValue("red");
                                                                            reference2petty.child("meet").setValue("-");
                                                                            reference2petty.child("group").setValue("-");
                                                                            reference2petty.child("creditac").setValue(name);
                                                                            reference2petty.child("debitac").setValue("Petty");
                                                                            reference2petty.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2petty.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference reference2pettym=mD.child("Petty").child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                                                                            reference2pettym.child("name").setValue("Petty");
                                                                            reference2pettym.child("amount").setValue(banking);
                                                                            reference2pettym.child("type").setValue("blue");
                                                                            reference2pettym.child("meet").setValue("-");
                                                                            reference2pettym.child("group").setValue("-");
                                                                            reference2pettym.child("creditac").setValue(name);
                                                                            reference2pettym.child("debitac").setValue("Petty");
                                                                            reference2pettym.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2pettym.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference reference2pettya=mD.child("Petty").child("Trans").child("all").push();
                                                                            reference2pettya.child("name").setValue("Petty");
                                                                            reference2pettya.child("amount").setValue(banking);
                                                                            reference2pettya.child("type").setValue("blue");
                                                                            reference2pettya.child("meet").setValue("-");
                                                                            reference2pettya.child("group").setValue("-");
                                                                            reference2pettya.child("creditac").setValue(name);
                                                                            reference2pettya.child("debitac").setValue("Petty");
                                                                            reference2pettya.child("description").setValue("Group: "+groupname+ " Handover from "+name);
                                                                            reference2pettya.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            DatabaseReference unhandedb=mD.child("Unhanded").child("Trans").child("all").push();
                                                                            unhandedb.child("name").setValue("Unhanded");
                                                                            unhandedb.child("amount").setValue(banking);
                                                                            unhandedb.child("type").setValue("red");
                                                                            unhandedb.child("meet").setValue("-");
                                                                            unhandedb.child("group").setValue("-");
                                                                            unhandedb.child("debitac").setValue("Petty");
                                                                            unhandedb.child("creditac").setValue("Unhanded");
                                                                            unhandedb.child("description").setValue(groupname+" handovers."+" by "+name);
                                                                            unhandedb.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                                                            if (Double.parseDouble(amm)>Double.parseDouble(oldamt)){
                                                                                DatabaseReference unhandedb1=mD.child("Unhanded").child("Trans").child("all").push();
                                                                                unhandedb1.child("name").setValue("Unhanded");
                                                                                unhandedb1.child("amount").setValue(Double.parseDouble(amm)-Double.parseDouble(oldamt));
                                                                                unhandedb1.child("type").setValue("blue");
                                                                                unhandedb1.child("meet").setValue("-");
                                                                                unhandedb1.child("group").setValue("-");
                                                                                unhandedb1.child("debitac").setValue("Unhanded");
                                                                                unhandedb1.child("creditac").setValue(name);
                                                                                unhandedb1.child("description").setValue(groupname+" excess handover."+" by "+name);
                                                                                unhandedb1.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                            }else if (Double.parseDouble(amm)<Double.parseDouble(oldamt)){
                                                                                DatabaseReference unhandedb1=mD.child("Unhanded").child("Trans").child("all").push();
                                                                                unhandedb1.child("name").setValue("Unhanded");
                                                                                unhandedb1.child("amount").setValue(Double.parseDouble(oldamt)-Double.parseDouble(amm));
                                                                                unhandedb1.child("type").setValue("red");
                                                                                unhandedb1.child("meet").setValue("-");
                                                                                unhandedb1.child("group").setValue("-");
                                                                                unhandedb1.child("debitac").setValue(name);
                                                                                unhandedb1.child("creditac").setValue("Unhanded");
                                                                                unhandedb1.child("description").setValue(groupname+" less handover."+" by "+name);
                                                                                unhandedb1.child("timestamp").setValue(ServerValue.TIMESTAMP);
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
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mD3.child(getLastDate()).child(fieldid).child("groupsgiven").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                if (snapshot.child("amountback").exists()){
                                                    ref.child(snapshot.getKey()).child("groupname").setValue(snapshot.child("name").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("amountbanked").setValue(snapshot.child("amountback").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("user").setValue(user);
                                                    ref.child(snapshot.getKey()).child("status").setValue("received");
                                                }else {
                                                    ref.child(snapshot.getKey()).child("groupname").setValue(snapshot.child("name").getValue().toString());
                                                    ref.child(snapshot.getKey()).child("amountbanked").setValue("0");
                                                    ref.child(snapshot.getKey()).child("user").setValue(user);
                                                    ref.child(snapshot.getKey()).child("status").setValue("received");
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

                        Toast.makeText(emloyeelist.this,"Received!", Toast.LENGTH_LONG).show();
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

    public static class GroupgivenViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupgivenViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupgivencard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww);
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww2);
            mGroupname.setText("Kshs. "+groupname);
        }
    }

    private String lastyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(java.util.Calendar.DATE, -1); //Remove 1 year

        return sdf.format(c.getTime());
    }



    private String getLastDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        //SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, -1); //Removing one

        return sdf.format(c.getTime());
    }

    private String updateLabel() {
        String myFormat = "EEE, MMM d, ''yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        Log.e("date",sdf.format(myCalendar.getTime()));
        return  sdf.format(myCalendar.getTime());
    }
}
