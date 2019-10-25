package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class schoolfeesadvanceprocessing extends AppCompatActivity {
    private TextView advMemberName,advId,advDate,mLimit;
    private EditText mAmount,advgrname,advgrid;
    private String userid=null,user;
    private Button mApply;
    private DatabaseReference mD,mD1,mD2,mD3,mD4,mDatabaseadvances;
    private FirebaseAuth mAuth;
    private String grpkey=null;
    private String meeetid=null;
    private ProgressDialog mProgress;
    private String name=null,fac;
    private String id=null,newpcurradv;
    private String date=null;
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

        mD4= FirebaseDatabase.getInstance().getReference().child("fees");
        mD3= FirebaseDatabase.getInstance().getReference().child("masterfinance");
        mD= FirebaseDatabase.getInstance().getReference().child("members").child(userid);
        mDatabaseadvances= FirebaseDatabase.getInstance().getReference().child("members").child("allmembers");
        mD1= FirebaseDatabase.getInstance().getReference().child("details");
        mD2= FirebaseDatabase.getInstance().getReference().child("finances");

        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=(String)dataSnapshot.child("details").child("name").getValue();
                id=(String)dataSnapshot.child("details").child("id").getValue();
                date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                grpkey=(String)dataSnapshot.child("details").child("groupid").getValue();
                String membersavings=dataSnapshot.child("savings").child("schoolfees").child("totalsavings").getValue().toString();
                mLimit.setText(String.valueOf(Double.parseDouble(membersavings)*2));
                advMemberName.setText(name);
                advId.setText(id);
                advDate.setText(date);

                advgrname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RecyclerView recyclerView=new RecyclerView(schoolfeesadvanceprocessing.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(schoolfeesadvanceprocessing.this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(schoolfeesadvanceprocessing.this);
                        builder.setTitle("Select Guarantor")
                                .setView(recyclerView);
                        recycleradapt(recyclerView,grpkey);

                        alert11 = builder.create();
                        alert11.show();
                    }
                });

                mD1.child(grpkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
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
                final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeesadvanceprocessing.this);
                builders.setTitle("Confirm Advance")
                        .setMessage("Advence "+name+" Kshs. "+mAmount.getText().toString())
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                processadvance();
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
            final String advamont=mAmount.getText().toString();
            double advc= (1.1*Double.parseDouble(advamont));
            final String advamount=String.format("%.2f",advc);
            final String advgrnname=advgrname.getText().toString();
            String advggrid=advgrid.getText().toString();
            double penalty= (0.1*Double.parseDouble(advamont))+10;
            final String penamnt=String.format("%.2f",penalty);
            mProgress.setTitle("Please wait");
            mProgress.setMessage("Approving Advance Details..");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            mD2.child(grpkey).child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String curradv;
                    if (dataSnapshot.child("currentadvance").exists()){
                    curradv=(String)dataSnapshot.child("currentadvance").getValue();
                    }else {
                        curradv="0";
                    }
                    double curradvint=Double.parseDouble(curradv);
                    final double advapp=Double.parseDouble(advamount);
                    double newadvbal=advapp+curradvint;
                    String newadvamnt=String.valueOf(newadvbal);

                    DatabaseReference newgrpadv=mD2.child(grpkey).child("advances").child("schoolfees");
                    newgrpadv.child("currentadvance").setValue(newadvamnt);

                    DatabaseReference newadvance=mD2.child(grpkey).child("advances").child("schoolfees").child("advancees").push();
                    newadvance.child("dateapproved").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newadvance.child("memberid").setValue(userid);
                    newadvance.child("amount").setValue(advamount);
                    newadvance.child("guarantor").setValue(advgrnname);

                    DatabaseReference newadvances=mD2.child(grpkey).child("advances").child("schoolfees").child("alladvancees").push();
                    newadvances.child("dateapproved").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                    newadvances.child("memberid").setValue(userid);
                    newadvances.child("amount").setValue(advamount);
                    newadvances.child("guarantor").setValue(advgrnname);


                    mD.child("advances").child("schoolfees").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("currentadvance").exists())
                            {
                            String curradv=(String)dataSnapshot.child("currentadvance").getValue();
                            double curradvint=Double.parseDouble(curradv);
                            double advapp=Double.parseDouble(advamount);
                            double newpadvbal=advapp+curradvint;
                            newpcurradv=String.valueOf(newpadvbal);

                            DatabaseReference newpersonaladv=mD.child("advances").child("schoolfees");
                            newpersonaladv.child("currentadvance").setValue(newpcurradv);
                            newpersonaladv.child("currpenalty").setValue(penamnt);
                            newpersonaladv.child("nextadvpaydate").setValue(setdate());
                            }else {
                                String curradv="0";
                                double curradvint=Double.parseDouble(curradv);
                                double advapp=Double.parseDouble(advamount);
                                double newpadvbal=advapp+curradvint;
                                newpcurradv=String.valueOf(newpadvbal);

                                DatabaseReference newpersonaladv=mD.child("advances").child("schoolfees");
                                newpersonaladv.child("currentadvance").setValue(newpcurradv);
                                newpersonaladv.child("currpenalty").setValue(penamnt);
                                newpersonaladv.child("nextadvpaydate").setValue(setdate());
                            }

                            mD.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("sftemptrs").child("trid").exists()){
                                        String iid=dataSnapshot.child("sftemptrs").child("trid").getValue().toString();
                                        final DatabaseReference tt=mD1.child(grpkey).child("schoolfees").child("transactions").child(meeetid).child(iid);
                                        tt.child("advgvn").setValue(advamont);
                                        tt.child("advcf").setValue(newpcurradv);
                                    }else {
                                    final DatabaseReference tt=mD1.child(grpkey).child("schoolfees").child("transactions").child(meeetid).push();
                                        tt.child("advgvn").setValue(advamont);
                                        tt.child("advcf").setValue(newpcurradv);
                                        tt.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                                        tt.child("totalamount").setValue("0");
                                        tt.child("lsf").setValue("0");
                                        tt.child("wcomm").setValue("0");
                                        tt.child("advancepayment").setValue("0");
                                        tt.child("lsfcf").setValue("0");
                                        tt.child("advanceinterest").setValue("0");
                                        tt.child("paymentmode").setValue("0");
                                        tt.child("memberid").setValue(name);
                                        tt.child("Mpesa").setValue("0");
                                        tt.child("id").setValue(userid);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference newpersonaladvs=mD.child("advances").child("schoolfees").child(meeetid);
                            newpersonaladvs.child("advancegiven").setValue(advamont);
                            newpersonaladvs.child("currpenalty").setValue(penamnt);
                            newpersonaladvs.child("nextadvpaydate").setValue(setdate());

                            DatabaseReference advdetail=mD.child("advances").child("schoolfees").child("advances").push();
                            advdetail.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            advdetail.child("amount").setValue(advamount);

                            DatabaseReference meettrans=mD1.child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances").child("advancesgiven").push();
                            meettrans.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
                            meettrans.child("membername").setValue(name);
                            meettrans.child("memberid").setValue(userid);
                            meettrans.child("advancegiven").setValue(advamount);
                            meettrans.child("advancefee").setValue("10");

                            DatabaseReference adfee=mD4.child(grpkey).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("schoolfees").child("advancefees").push();
                            adfee.child("amount").setValue("10");
                            adfee.child("memberid").setValue(userid);



                            FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild("total")){
                                        double nu2=Double.parseDouble(dataSnapshot.child("total").getValue().toString())+advapp/1.1;
                                        DatabaseReference meettrs=FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances");
                                        meettrs.child("total").setValue(String.valueOf(nu2));
                                    }else {
                                        DatabaseReference meettrns=FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("schoolfees").child("meetings").child(meeetid).child("advances");
                                        meettrns.child("total").setValue(String.valueOf(advapp/1.1));
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mProgress.dismiss();
            Toast.makeText(schoolfeesadvanceprocessing.this,"Processed!", Toast.LENGTH_LONG).show();
            Intent loans = new Intent(schoolfeesadvanceprocessing.this,meeting.class);
            loans.putExtra("key",grpkey);
            startActivity(loans);
            finish();
        }catch (Exception e){
            Log.e("Exception",String.valueOf(e));
        }

    }

    private void recycleradapt(RecyclerView r, final String b_key) {
        FirebaseRecyclerAdapter<member,advanceprocessing.membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member,advanceprocessing.membersViewHolder>(
                member.class,
                R.layout.member_row,
                advanceprocessing.membersViewHolder.class,
                mDatabaseadvances.child(b_key)
        )
        {
            @Override
            protected void populateViewHolder(final advanceprocessing.membersViewHolder viewHolder, final member model, int position) {
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
