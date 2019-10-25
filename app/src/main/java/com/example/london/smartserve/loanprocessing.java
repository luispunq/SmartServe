package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.GregorianCalendar;
import java.util.Locale;

public class loanprocessing extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername,mGroupName,mLoanAmount,mSavings,mOfficer,mloanintr,mloanins;
    private Button mAccept,mDecline;
    private DatabaseReference mDatabaseloanextras,mDatabaseloansapp,mDatabasemembers,mDatabse,mDatabase,mData;
    private FirebaseAuth mAuth;
    private String user,id;
    private RecyclerView mGlist,mSlist;
    private String loanamount=null,loancash="0",loanamountcsh=null,name;
    private String uid=null;
    private String installments,recoms,notes="defaults";
    private String grpid;
    private ProgressDialog mProgress;
    double newtogive=0;
    private DatabaseReference mDatabases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanprocessing);
        id=getIntent().getExtras().getString("id");
        mAuth=FirebaseAuth.getInstance();
        //user=mAuth.getCurrentUser().getUid();
        mProgress=new ProgressDialog(this);
        mMemberphoto=findViewById(R.id.memberlpprofpic);
        mMembername=findViewById(R.id.memberlpname);
        mGroupName=findViewById(R.id.memberlpgroup);
        mLoanAmount=findViewById(R.id.reqloan);
        mSavings=findViewById(R.id.lsfnow);
        mOfficer=findViewById(R.id.officer);
        mloanintr=findViewById(R.id.reqloancash);
        mloanins=findViewById(R.id.reqloaninstall);
        mAccept=findViewById(R.id.registerbtn2);
        mDecline=findViewById(R.id.registerbtn3);
        mGlist = findViewById(R.id.gurlist);
        mGlist.setHasFixedSize(true);
        mGlist.setLayoutManager(new LinearLayoutManager(this));
        mSlist = findViewById(R.id.seclist);
        mSlist.setHasFixedSize(true);
        mSlist.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseloanextras=FirebaseDatabase.getInstance().getReference().child("loanextras").child(id);
        mDatabse=FirebaseDatabase.getInstance().getReference().child("finances");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("details");
        mDatabases=FirebaseDatabase.getInstance().getReference().child("loanapprovals");
        mDatabaseloansapp= FirebaseDatabase.getInstance().getReference().child("loanrequests").child(id);
        mDatabasemembers=FirebaseDatabase.getInstance().getReference().child("members");

        mDatabaseloansapp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=(String)dataSnapshot.child("userid").getValue();
                loanamount=(String)dataSnapshot.child("amountrequested").getValue();
                loanamountcsh=(String)dataSnapshot.child("amountcash").getValue();
                installments=(String)dataSnapshot.child("monthlypay").getValue();
                loancash=dataSnapshot.child("amountcash").getValue().toString();
                mloanintr.setText(String.valueOf(Double.parseDouble(loanamount)-Double.parseDouble(loancash)));
                mloanins.setText(dataSnapshot.child("monthlypay").getValue().toString());
                recoms=dataSnapshot.child("recommendation").getValue().toString();
                notes=dataSnapshot.child("image").getValue().toString();



                mDatabasemembers.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String savingsamount=(String)dataSnapshot.child("savings").child("totalsavings").getValue();
                        name= (String) dataSnapshot.child("details").child("name").getValue();
                        String image= (String) dataSnapshot.child("details").child("profileImage").getValue();
                        String group=(String)dataSnapshot.child("details").child("group").getValue();
                        grpid=(String)dataSnapshot.child("details").child("groupid").getValue();
                        mMembername.setText(name);
                        mGroupName.setText(group);
                        mSavings.setText(savingsamount);
                        Picasso.with(loanprocessing.this).load(image).into(mMemberphoto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mLoanAmount.setText("Kshs. "+loanamount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recoms.equals(uid)){
                    approvegrouploan();
                }else {
                    approveloan();
                }
            }
        });
        mDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineloan();
            }
        });

    }


    private void approvegrouploan() {
        mProgress.setTitle("Please wait");
        mProgress.setMessage("Approving Loan Details..");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        DatabaseReference loanrequest=mDatabases.child(uid);



        loanrequest.child("amountcash").setValue(loanamountcsh);
        loanrequest.child("userid").setValue(uid);
        loanrequest.child("amountrequested").setValue(loanamount);
        loanrequest.child("monthlypay").setValue(mloanins.getText().toString());
        loanrequest.child("status").setValue("pending");
        loanrequest.child("type").setValue("group");
        loanrequest.child("gmid").setValue(recoms);
        loanrequest.child("desc").setValue(notes);

        DatabaseReference ref=mDatabase.child(grpid).child("groupdetails");
        ref.child("togivefac").setValue(loanamountcsh);

        /*DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("LoanApproval").push();
        endmeetnot.child("amount").setValue(loanamount);
        endmeetnot.child("user").setValue(name);*/


        mProgress.dismiss();
        Toast.makeText(loanprocessing.this,"Group Loan Approved!", Toast.LENGTH_SHORT).show();
        Intent loans = new Intent(loanprocessing.this,masterhome.class);
        startActivity(loans);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<guarantors,guarantorsViewHolder> fireecyclerAdapter = new FirebaseRecyclerAdapter<guarantors, guarantorsViewHolder>(

                guarantors.class,
                R.layout.guarantors_row,
                guarantorsViewHolder.class,
                mDatabaseloanextras.child("guarantors")
        ) {
            @Override
            protected void populateViewHolder(guarantorsViewHolder viewHolder, guarantors model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setID(model.getId());
                viewHolder.setPhone(model.getContact());
            }
        };
        mGlist.setAdapter(fireecyclerAdapter);

    FirebaseRecyclerAdapter<securities,securitiesViewHolder> adapter2 = new FirebaseRecyclerAdapter<securities, securitiesViewHolder>(

            securities.class,
            R.layout.securities_row,
            securitiesViewHolder.class,
            mDatabaseloanextras.child("security")
    ) {
        @Override
        protected void populateViewHolder(securitiesViewHolder viewHolder, securities model, int position) {
            viewHolder.setName(model.getItemname());
            viewHolder.setVal1(model.getMarketvalue());
            viewHolder.setVal2(model.getCompvalue());
        }
    };
        mSlist.setAdapter(adapter2);
}

    private void declineloan() {
        Toast.makeText(loanprocessing.this, "Loan Declined", Toast.LENGTH_LONG).show();
        DatabaseReference setstatus=mDatabaseloansapp;
        setstatus.child("status").setValue("declined");

        DatabaseReference loanrequest=mDatabases.child(uid);
        loanrequest.child("amountcash").setValue(loanamountcsh);
        loanrequest.child("userid").setValue(uid);
        loanrequest.child("amountrequested").setValue(loanamount);
        loanrequest.child("monthlypay").setValue(mloanins.getText().toString());
        loanrequest.child("status").setValue("declined");
        loanrequest.child("type").setValue("indv");
        loanrequest.child("gmid").setValue(uid);
        loanrequest.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        loanrequest.child("gname").setValue(mGroupName.getText().toString());
        loanrequest.child("username").setValue(mMembername.getText().toString());

        Intent loans = new Intent(loanprocessing.this,masterhome.class);
        startActivity(loans);
        finish();
    }

    private void approveloan() {
        mProgress.setTitle("Please wait");
        mProgress.setMessage("Approving Loan Details..");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        DatabaseReference refs=mDatabaseloansapp;
        refs.child("status").setValue("done");


        DatabaseReference loanrequest=mDatabases.child(uid);
        loanrequest.child("amountcash").setValue(loanamountcsh);
        loanrequest.child("userid").setValue(uid);
        loanrequest.child("amountrequested").setValue(loanamount);
        loanrequest.child("monthlypay").setValue(mloanins.getText().toString());
        loanrequest.child("status").setValue("pending");
        loanrequest.child("type").setValue("indv");
        loanrequest.child("gmid").setValue(uid);
        loanrequest.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        loanrequest.child("gname").setValue(mGroupName.getText().toString());
        loanrequest.child("username").setValue(mMembername.getText().toString());

        DatabaseReference ref=mDatabase.child(grpid).child("groupdetails");
        ref.child("togivefac").setValue(loanamountcsh);

        /*DatabaseReference endmeetnot=FirebaseDatabase.getInstance().getReference().child("Notifications").child("LoanApproval").push();
        endmeetnot.child("amount").setValue(loanamount);
        endmeetnot.child("user").setValue(name);*/

        loanrequest.child(uid).removeValue();

        mProgress.dismiss();
        Toast.makeText(loanprocessing.this,"Individual Loan Approved!", Toast.LENGTH_SHORT).show();
        Intent loans = new Intent(loanprocessing.this,masterhome.class);
        startActivity(loans);
        finish();
    }

    public static class guarantorsViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public guarantorsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public void setName(String name){
            TextView name1 = (TextView)mView.findViewById(R.id.gcardname);
            name1.setText(name);
        }
        public void setID(String id){
            TextView id1 = (TextView)mView.findViewById(R.id.gcardid);
            id1.setText(id);
        }
        public void setPhone(String phone){
            TextView contact = (TextView)mView.findViewById(R.id.gcardphone);
            contact.setText(phone);
        }

    }

    public static class securitiesViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public securitiesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public void setName(String name){
            TextView itemname1 = (TextView)mView.findViewById(R.id.scardname1);
            itemname1.setText(name);
        }
        public void setVal1(String val1){
            TextView value1 = (TextView)mView.findViewById(R.id.scardval1);
            value1.setText(val1);
        }
        public void setVal2(String val2){
            TextView value2 = (TextView)mView.findViewById(R.id.scardval2);
            value2.setText(val2);
        }

    }

}
