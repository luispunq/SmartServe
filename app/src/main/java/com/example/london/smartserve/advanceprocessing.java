package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class advanceprocessing extends AppCompatActivity {
    private TextView advMemberName,advId,advDate,mLimit;
    private EditText mAmount,advgrname,advgrid;
    private String userid=null,user;
    private Button mApply;
    private DatabaseReference mDatabaseadvances,mD,mD2,mD3,mD4,mD5,accounting,memacco;
    private FirebaseAuth mAuth;
    private String grpkey=null;
    private String meeetid=null;
    private ProgressDialog mProgress;
    private String name=null,fac;
    private String id=null;
    private String date=null;
    private double nu1=0;
    private AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanceprocessing);
        advMemberName=findViewById(R.id.membername);
        advId=findViewById(R.id.idnumber);
        mProgress=new ProgressDialog(this);
        mLimit=findViewById(R.id.advalimit);
        advDate=findViewById(R.id.advancedate);
        advgrname=findViewById(R.id.grname);
        advgrid=findViewById(R.id.gridnumber);
        mAmount=findViewById(R.id.advamount);
        mApply=findViewById(R.id.button5);
        userid=getIntent().getExtras().getString("key");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        accounting=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabaseadvances= FirebaseDatabase.getInstance().getReference().child("members").child("allmembers");
        //mDatabaseadvances.keepSynced(true);
        mD5= FirebaseDatabase.getInstance().getReference();
        //mD5.keepSynced(true);
        mD= FirebaseDatabase.getInstance().getReference().child("members").child(userid);
        //mD.keepSynced(true);
        mD2= FirebaseDatabase.getInstance().getReference();
        //mD2.keepSynced(true);
        mD3= FirebaseDatabase.getInstance().getReference();
        //mD3.keepSynced(true);
        memacco=FirebaseDatabase.getInstance().getReference().child("members").child(userid).child("account");


        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=(String)dataSnapshot.child("details").child("name").getValue();
                id=(String)dataSnapshot.child("details").child("id").getValue();
                date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                grpkey=(String)dataSnapshot.child("details").child("groupid").getValue();
                String membersavings=dataSnapshot.child("savings").child("totalsavings").getValue().toString();
                mLimit.setText(String.valueOf(Double.parseDouble(membersavings)*2));
                advMemberName.setText(name);
                advId.setText(id);
                advDate.setText(date);

                advgrname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RecyclerView recyclerView=new RecyclerView(advanceprocessing.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(advanceprocessing.this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(advanceprocessing.this);
                        builder.setTitle("Select Guarantor")
                                .setView(recyclerView);
                        recycleradapt(recyclerView,grpkey);

                        alert11 = builder.create();
                        alert11.show();
                    }
                });

                mD2.child("details").child(grpkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        meeetid=(String)dataSnapshot.child("meetid").getValue();
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

        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(advanceprocessing.this);
                builders.setTitle("Confirm Advance")
                        .setMessage("Advance "+name+" Kshs. "+mAmount.getText().toString())
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mD.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("ptemptrs").child("pid").exists()){
                                        final String id=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();

                                            mD2.child("details").child(grpkey).child("transactions").child(meeetid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child(id).exists()){
                                                        processadvance();
                                                    }else {
                                                        Toast.makeText(advanceprocessing.this,"Please make sure you have made member payments.", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }else {
                                            Toast.makeText(advanceprocessing.this,"Please make.", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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

    private void processadvance() {
        try{
            mProgress.setTitle("Please wait");
            mProgress.setMessage("Approving Advance Details..");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            final String advamont=mAmount.getText().toString();
            double advc= (1.1*Double.parseDouble(advamont));
            final String advamount=String.format("%.2f",advc);
            final String advgrnname=advgrname.getText().toString();
            String advggrid=advgrid.getText().toString();
            double penalty= (0.1*Double.parseDouble(advamont))+10;
            final String penamnt=String.format("%.2f",penalty);



            mD3.child("finances").child(grpkey).child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String curradv=(String)dataSnapshot.child("currentadvance").getValue();
                    double curradvint=Double.parseDouble(curradv);
                    final double advapp=Double.parseDouble(advamount);
                    double newadvbal=advapp+curradvint;
                    final String newadvamnt=String.valueOf(newadvbal);

                    DatabaseReference newgrpadv=mD3.child("finances").child(grpkey).child("advances");
                    newgrpadv.child("currentadvance").setValue(newadvamnt);

                    DatabaseReference newadvance=mD3.child("finances").child(grpkey).child("advances").child("advancees").push();
                    newadvance.child("dateapproved").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newadvance.child("memberid").setValue(userid);
                    newadvance.child("amount").setValue(advamount);
                    newadvance.child("guarantor").setValue(advgrnname);

                    DatabaseReference newadvances=mD3.child("finances").child(grpkey).child("advances").child("alladvancees").push();
                    newadvances.child("dateapproved").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newadvances.child("memberid").setValue(userid);
                    newadvances.child("amount").setValue(advamount);
                    newadvances.child("guarantor").setValue(advgrnname);


                    mD.child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String curradv=(String)dataSnapshot.child("currentadvance").getValue();
                            double curradvint=Double.parseDouble(curradv);
                            final double advapp=Double.parseDouble(advamount);
                            double newpadvbal=advapp+curradvint;
                            final String newpcurradv=String.valueOf(newpadvbal);

                            DatabaseReference newpersonaladv=mD.child("advances");
                            newpersonaladv.child("currentadvance").setValue(newpcurradv);
                            newpersonaladv.child("currpenalty").setValue(penamnt);
                            newpersonaladv.child("nextadvpaydate").setValue(setdate());

                            mD.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String id=dataSnapshot.child("ptemptrs").child("pid").getValue().toString();
                                    final DatabaseReference tt=mD2.child("details").child(grpkey).child("transactions").child(meeetid).child(id);
                                    tt.child("advgvn").setValue(advamont);
                                    tt.child("advcf").setValue(newpcurradv);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference newpersonaladvs=mD.child("advances").child(meeetid);
                            newpersonaladvs.child("advancegiven").setValue(advamont);
                            newpersonaladvs.child("currpenalty").setValue(penamnt);
                            newpersonaladvs.child("nextadvpaydate").setValue(setdate());

                            DatabaseReference advdetail=mD.child("advances").child("advances").push();
                            advdetail.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            advdetail.child("amount").setValue(advamount);

                            DatabaseReference meettrans=mD2.child("details").child(grpkey).child("meetings").child(meeetid).child("advances").child("advancesgiven").push();
                            meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            meettrans.child("membername").setValue(name);
                            meettrans.child("memberid").setValue(userid);
                            meettrans.child("advancegiven").setValue(advamount);
                            meettrans.child("advancefee").setValue("10");

                            mD2.child("details").child(grpkey).child("meetings").child(meeetid).child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild("total")){
                                        double nu2=Double.parseDouble(dataSnapshot.child("total").getValue().toString())+advapp/1.1;
                                        DatabaseReference meettrs=mD2.child("details").child(grpkey).child("meetings").child(meeetid).child("advances");
                                        meettrs.child("total").setValue(String.valueOf(nu2));
                                    }else {
                                        DatabaseReference meettrns=mD2.child("details").child(grpkey).child("meetings").child(meeetid).child("advances");
                                        meettrns.child("total").setValue(String.valueOf(advapp/1.1));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference adfee=mD5.child("fees").child(grpkey).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("advancefees").push();
                            adfee.child("amount").setValue("10");
                            adfee.child("memberid").setValue(userid);

                            doaccount(penamnt,newadvamnt,advamont);
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
            Toast.makeText(advanceprocessing.this,"Processed!", Toast.LENGTH_LONG).show();
            Intent loans = new Intent(advanceprocessing.this,meeting.class);
            loans.putExtra("key",grpkey);
            startActivity(loans);
            finish();
        }catch (Exception e){
        }

    }

    private void doaccount(String penamnt,String advamount,String advmont) {
        DatabaseReference stl=accounting.child("Shorttermloans").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        stl.child("name").setValue("Short Term Loan");
        stl.child("amount").setValue(advamount);
        stl.child("type").setValue("blue");
        stl.child("meet").setValue(meeetid);
        stl.child("group").setValue(grpkey);
        stl.child("debitac").setValue("Shorttermloans");
        stl.child("creditac").setValue("BankCash");
        stl.child("description").setValue("Advance Given to "+name);
        stl.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        stl2.child("name").setValue("Short Term Loan");
        stl2.child("amount").setValue(advmont);
        stl2.child("type").setValue("red");
        stl2.child("meet").setValue(meeetid);
        stl2.child("group").setValue(grpkey);
        stl2.child("debitac").setValue("Shorttermloans");
        stl2.child("creditac").setValue("BankCash");
        stl2.child("description").setValue("Advance Given to "+name);
        stl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference stl2m=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        stl2m.child("name").setValue("Short Term Loan");
        stl2m.child("amount").setValue(advmont);
        stl2m.child("type").setValue("red");
        stl2m.child("meet").setValue(meeetid);
        stl2m.child("group").setValue(grpkey);
        stl2m.child("debitac").setValue("Shorttermloans");
        stl2m.child("creditac").setValue("Bank/Cash");
        stl2m.child("description").setValue("Advance Given to "+name);
        stl2m.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc1ad=accounting.child("Income").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        inc1ad.child("name").setValue("Income");
        inc1ad.child("amount").setValue(penamnt);
        inc1ad.child("type").setValue("blue");
        inc1ad.child("meet").setValue(meeetid);
        inc1ad.child("group").setValue(grpkey);
        inc1ad.child("debitac").setValue("Income");
        inc1ad.child("creditac").setValue("-");
        inc1ad.child("description").setValue("Advance Interest paid by "+name);
        inc1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc2ad=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        inc2ad.child("name").setValue("Income");
        inc2ad.child("amount").setValue(penamnt);
        inc2ad.child("type").setValue("blue");
        inc2ad.child("meet").setValue(meeetid);
        inc2ad.child("group").setValue(grpkey);
        inc2ad.child("debitac").setValue("Income");
        inc2ad.child("creditac").setValue("-");
        inc2ad.child("description").setValue("Advance Interest paid by "+name);
        inc2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference inc2adm=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        inc2adm.child("name").setValue("Income");
        inc2adm.child("amount").setValue(penamnt);
        inc2adm.child("type").setValue("blue");
        inc2adm.child("meet").setValue(meeetid);
        inc2adm.child("group").setValue(grpkey);
        inc2adm.child("debitac").setValue("Income");
        inc2adm.child("creditac").setValue("-");
        inc2adm.child("description").setValue("Advance Interest paid by "+name);
        inc2adm.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees1ad=accounting.child("Feesfines").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        fees1ad.child("name").setValue("Fees and Fines");
        fees1ad.child("amount").setValue("10");
        fees1ad.child("type").setValue("blue");
        fees1ad.child("meet").setValue(meeetid);
        fees1ad.child("group").setValue(grpkey);
        fees1ad.child("debitac").setValue("Bank/Cash");
        fees1ad.child("creditac").setValue("-");
        fees1ad.child("description").setValue("Advance Processing Fees for "+name);
        fees1ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2ad=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
        fees2ad.child("name").setValue("Fees and Fines");
        fees2ad.child("amount").setValue("10");
        fees2ad.child("type").setValue("blue");
        fees2ad.child("meet").setValue(meeetid);
        fees2ad.child("group").setValue(grpkey);
        fees2ad.child("debitac").setValue("Feesfines");
        fees2ad.child("creditac").setValue("-");
        fees2ad.child("description").setValue("Advance Processing Fees for "+name);
        fees2ad.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference fees2adm=accounting.child("BankCash").child(grpkey).child("Trans").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
        fees2adm.child("name").setValue("Fees and Fines");
        fees2adm.child("amount").setValue("10");
        fees2adm.child("type").setValue("blue");
        fees2adm.child("meet").setValue(meeetid);
        fees2adm.child("group").setValue(grpkey);
        fees2adm.child("debitac").setValue("Feesfines");
        fees2adm.child("creditac").setValue("-");
        fees2adm.child("description").setValue("Advance Processing Fees for "+name);
        fees2adm.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference peracc=memacco.push();
        peracc.child("name").setValue("Short Term Loan");
        peracc.child("amount").setValue(advamount);
        peracc.child("type").setValue("blue");
        peracc.child("meet").setValue(meeetid);
        peracc.child("group").setValue(grpkey);
        peracc.child("debitac").setValue("member");
        peracc.child("creditac").setValue("BankCash");
        peracc.child("description").setValue("Advance Given");
        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

        DatabaseReference peraccfee=memacco.push();
        peraccfee.child("name").setValue("Income");
        peraccfee.child("amount").setValue(penamnt);
        peraccfee.child("type").setValue("blue");
        peraccfee.child("meet").setValue(meeetid);
        peraccfee.child("group").setValue(grpkey);
        peraccfee.child("debitac").setValue("member");
        peraccfee.child("creditac").setValue("Income");
        peraccfee.child("description").setValue("Advance Interest");
        peraccfee.child("timestamp").setValue(ServerValue.TIMESTAMP);

    }

    private void recycleradapt(RecyclerView r, final String b_key) {
        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member,membersViewHolder>(
                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                mDatabaseadvances.child(b_key)
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final member model, int position) {
                final String u_key=model.getUid();

                viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        advgrname.setText(model.getName());
                        advgrid.setText(model.getId());
                        alert11.dismiss();
                    }
                });


            }
        };
        r.setAdapter(firebaseRecyclerAdapter);

    }

    public static class membersViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }




        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.membercardname);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.membercardphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }

    private String setdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 30); // Adding 30 days
        String output = sdf.format(c.getTime());

        return output;
    }
}
