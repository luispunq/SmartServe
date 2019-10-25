package com.example.london.smartserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class employeebasic extends AppCompatActivity {
    private TextView t1,t2,t3,t4,basicpay,comms,others,mNHIF,mNSSF,mPAYE,mLOAN,mAdv;
    private ImageView im1,imsett,editded,editpays;
    private CardView cc1,c2,mGiveloan,mGiveadv,remove,transfer;
    private DatabaseReference mDatabase,mD1;
    private FirebaseAuth mAuth;
    private String s1,s2,bp,cp,op,nssfp,nhifp,payep,ustype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeebasic);
        s1=getIntent().getExtras().getString("User_Key");
        mAuth=FirebaseAuth.getInstance();
        s2=mAuth.getCurrentUser().getUid();

        t1=findViewById(R.id.allgroups);
        t2=findViewById(R.id.type1grp);
        t3=findViewById(R.id.type2grp);
        t4=findViewById(R.id.type3grp);
        im1=findViewById(R.id.cordprofpic);
        cc1=findViewById(R.id.chosegroup);
        c2=findViewById(R.id.managegroups);
        imsett=findViewById(R.id.imageView13);

        editpays=findViewById(R.id.edit3);
        editded=findViewById(R.id.edit2);

        basicpay=findViewById(R.id.basicpay);
        comms=findViewById(R.id.commbon);
        others=findViewById(R.id.otherpay);
        mNHIF=findViewById(R.id.nhifpay);
        mNSSF=findViewById(R.id.nssfpay);
        mPAYE=findViewById(R.id.paye);
        mLOAN=findViewById(R.id.loanded);
        mAdv=findViewById(R.id.advded);

        mGiveadv=findViewById(R.id.empadv);
        mGiveloan=findViewById(R.id.emploan);

        remove=findViewById(R.id.empremove);
        transfer=findViewById(R.id.emptransfar);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Employees").child(s1);
        mD1= FirebaseDatabase.getInstance().getReference().child("Employees").child(s2);

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ustype=dataSnapshot.child("position").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference old=FirebaseDatabase.getInstance().getReference().child("OldEmployees").child(s1);
                old.child("uid").setValue(s1);

                DatabaseReference remove=mDatabase;
                remove.removeValue();

                Toast.makeText(employeebasic.this,"Employee Removed",Toast.LENGTH_LONG).show();

                if (ustype.equals("manager")){
                    Intent forw=new Intent(employeebasic.this,managerhome.class);
                    startActivity(forw);
                }else {
                    Intent forw=new Intent(employeebasic.this,masterhome.class);
                    startActivity(forw);
                }
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText oths=new EditText(employeebasic.this);
                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                builders.setTitle("Branch Change")
                        .setMessage("Change Operating Branch for "+t1.getText().toString())
                        .setView(oths)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference remove=mDatabase;
                                remove.child("Branch").setValue(oths.getText().toString());

                                Toast.makeText(employeebasic.this,"Branch Changed to "+oths.getText().toString(),Toast.LENGTH_LONG).show();
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

        mDatabase.child("basicpays").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                basicpay.setText("Kshs. "+dataSnapshot.child("basicpay").getValue().toString());
                comms.setText("Kshs. "+dataSnapshot.child("comms").getValue().toString());
                others.setText("Kshs. "+dataSnapshot.child("other").getValue().toString());

                bp=dataSnapshot.child("basicpay").getValue().toString();
                cp=dataSnapshot.child("comms").getValue().toString();
                op=dataSnapshot.child("other").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("deductions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNHIF.setText("Kshs. "+dataSnapshot.child("nhif").getValue().toString());
                mNSSF.setText("Kshs. "+dataSnapshot.child("nssf").getValue().toString());
                mPAYE.setText("Kshs. "+dataSnapshot.child("paye").getValue().toString());

                nhifp=dataSnapshot.child("nhif").getValue().toString();
                nssfp=dataSnapshot.child("nssf").getValue().toString();
                payep=dataSnapshot.child("paye").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLOAN.setText("Kshs. "+dataSnapshot.child("totalloan").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAdv.setText("Kshs. "+dataSnapshot.child("currentadvance").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editpays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=new LinearLayout(employeebasic.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText basi=new EditText(employeebasic.this);
                final EditText coms=new EditText(employeebasic.this);
                final EditText oths=new EditText(employeebasic.this);
                final TextView basix=new TextView(employeebasic.this);
                final TextView comsx=new TextView(employeebasic.this);
                final TextView othsx=new TextView(employeebasic.this);
                basix.setText("Basic Pay:");
                comsx.setText("Commision:");
                othsx.setText("Other Pay:");
                basi.setText(bp);
                coms.setText(cp);
                oths.setText(op);
                basi.setHint("Enter Basic Pay");
                coms.setHint("Enter Commision");
                oths.setHint("Enter Other Pay");
                layout.addView(basix);
                layout.addView(basi);
                layout.addView(comsx);
                layout.addView(coms);
                layout.addView(othsx);
                layout.addView(oths);
                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                builders.setTitle("Earning Adjustment")
                        .setMessage("Smart Serve Earnings for "+t1.getText().toString())
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference newloan=mDatabase.child("basicpays");
                                newloan.child("basicpay").setValue(basi.getText().toString());
                                newloan.child("comms").setValue(coms.getText().toString());
                                newloan.child("other").setValue(oths.getText().toString());

                                Toast.makeText(employeebasic.this,"Earnings Adjusted!", Toast.LENGTH_LONG).show();
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

        editded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=new LinearLayout(employeebasic.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText nhifs=new EditText(employeebasic.this);
                final EditText nssfs=new EditText(employeebasic.this);
                final EditText payes=new EditText(employeebasic.this);
                final TextView basix=new TextView(employeebasic.this);
                final TextView comsx=new TextView(employeebasic.this);
                final TextView othsx=new TextView(employeebasic.this);
                nhifs.setText(nhifp);
                nssfs.setText(nssfp);
                payes.setText(payep);
                basix.setText("NHIF Deduction:");
                comsx.setText("NSSF Deduction:");
                othsx.setText("PAYE Deduction:");
                nhifs.setHint("Enter NHIF");
                nssfs.setHint("Enter NSSF");
                payes.setHint("Enter PAYE");
                layout.addView(basix);
                layout.addView(nhifs);
                layout.addView(comsx);
                layout.addView(nssfs);
                layout.addView(othsx);
                layout.addView(payes);
                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                builders.setTitle("Deduction Adjustment")
                        .setMessage("Smart Serve Deductions for "+t1.getText().toString())
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference newloan=mDatabase.child("deductions");
                                newloan.child("nssf").setValue(nssfs.getText().toString());
                                newloan.child("nhif").setValue(nhifs.getText().toString());
                                newloan.child("paye").setValue(payes.getText().toString());

                                Toast.makeText(employeebasic.this,"Deductions Adjusted!", Toast.LENGTH_LONG).show();
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


        mGiveadv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builders1 = new AlertDialog.Builder(employeebasic.this);
                builders1.setTitle("Smart Serve Advances")
                        .setCancelable(true)
                        .setPositiveButton("Give Advance", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                final EditText misccash=new EditText(employeebasic.this);
                                misccash.setHint("Enter Advance Amount");
                                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                                builders.setTitle("Employee Advance")
                                        .setMessage("Smart Serve Employee advance for "+t1.getText().toString())
                                        .setCancelable(true)
                                        .setView(misccash)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DatabaseReference newloan=mDatabase.child("advances");
                                                newloan.child("currentadvance").setValue(misccash.getText().toString());

                                                DatabaseReference newloans=mDatabase.child("advances").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                                                newloans.child("currentadvance").setValue(misccash.getText().toString());
                                                newloans.child("date").setValue((new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())));


                                                Toast.makeText(employeebasic.this,"Advance Given ",Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert121g = builders.create();
                                alert121g.show();

                            }
                        })
                        .setNegativeButton("Receive Payments", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                //dialog1.cancel();

                                final EditText misccasha=new EditText(employeebasic.this);
                                misccasha.setHint("Enter Advance Repayment");
                                final AlertDialog.Builder buildersr = new AlertDialog.Builder(employeebasic.this);
                                buildersr.setTitle("Employee Advance")
                                        .setMessage("Smart Serve Employee advance for "+t1.getText().toString())
                                        .setCancelable(true)
                                        .setView(misccasha)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog2, int which) {

                                                final double advancepaid=Double.parseDouble(misccasha.getText().toString());

                                                mDatabase.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        double current=Double.parseDouble(dataSnapshot.child("currentadvance").getValue().toString());

                                                        DatabaseReference newloan=mDatabase.child("advances");
                                                        newloan.child("currentadvance").setValue(String.valueOf(current-advancepaid));
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                                Toast.makeText(employeebasic.this,"Advance Paid",Toast.LENGTH_LONG).show();
                                                dialog2.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog2, int which) {
                                                dialog2.dismiss();
                                            }
                                        });
                                AlertDialog alert121r = buildersr.create();
                                alert121r.show();
                            }
                        });
                AlertDialog alert121 = builders1.create();
                alert121.show();



            }
        });

        mGiveloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builders2 = new AlertDialog.Builder(employeebasic.this);
                builders2.setTitle("Smart Serve Loans")
                        .setCancelable(true)
                        .setPositiveButton("Give Loans", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                final EditText misccash=new EditText(employeebasic.this);
                                misccash.setHint("Enter Loans Amount");
                                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                                builders.setTitle("Employee Loans")
                                        .setMessage("Smart Serve Employee loans for "+t1.getText().toString())
                                        .setCancelable(true)
                                        .setView(misccash)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DatabaseReference newloan=mDatabase.child("advances");
                                                newloan.child("currentadvance").setValue(misccash.getText().toString());

                                                DatabaseReference newloans=mDatabase.child("advances").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                                                newloans.child("currentadvance").setValue(misccash.getText().toString());
                                                newloans.child("date").setValue((new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())));


                                                Toast.makeText(employeebasic.this,"Advance Given ",Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert121g = builders.create();
                                alert121g.show();

                            }
                        })
                        .setNegativeButton("Receive Payments", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                //dialog1.cancel();

                                final EditText misccashal=new EditText(employeebasic.this);
                                misccashal.setHint("Enter Loan Repayment");
                                final AlertDialog.Builder buildersrl = new AlertDialog.Builder(employeebasic.this);
                                buildersrl.setTitle("Employee Loan")
                                        .setMessage("SmartServe Employee loan for "+t1.getText().toString())
                                        .setCancelable(true)
                                        .setView(misccashal)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog3, int which) {

                                                final double advancepaid=Double.parseDouble(misccashal.getText().toString());

                                                mDatabase.child("loans").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        double current=Double.parseDouble(dataSnapshot.child("totalloan").getValue().toString());

                                                        DatabaseReference newloan=mDatabase.child("loans");
                                                        newloan.child("totalloan").setValue(String.valueOf(current-advancepaid));
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                                Toast.makeText(employeebasic.this,"Loan Paid",Toast.LENGTH_LONG).show();
                                                dialog3.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog3, int which) {
                                                dialog3.dismiss();
                                            }
                                        });
                                AlertDialog alert121rl = buildersrl.create();
                                alert121rl.show();
                            }
                        });
                AlertDialog alert121l = builders2.create();
                alert121l.show();


            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                t1.setText(dataSnapshot.child("name").getValue().toString());
                if (dataSnapshot.child("phone").exists()) {
                    t2.setText(dataSnapshot.child("phone").getValue().toString());
                }else {
                    t2.setText("07..");
                }
                t3.setText(dataSnapshot.child("startdate").getValue().toString());
                t4.setText(dataSnapshot.child("salary").getValue().toString());
                Picasso.with(employeebasic.this).load(dataSnapshot.child("image").getValue().toString()).into(im1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(employeebasic.this);
                builders.setTitle("Call "+t1.getText().toString())
                        .setMessage("Phone number "+t2.getText().toString())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_call_black_24dp)
                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String uri = "tel:" + t2.getText().toString() ;
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);
                            }
                        });
                AlertDialog alert121 = builders.create();
                alert121.show();
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forw=new Intent(employeebasic.this,emppopup.class);
                forw.putExtra("User_Key",s1);
                startActivity(forw);
            }
        });
        cc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usermodify = new Intent(employeebasic.this,dateselector2.class);
                usermodify.putExtra("key",s1);
                startActivity(usermodify);
                finish();
            }
        });
    }
}
