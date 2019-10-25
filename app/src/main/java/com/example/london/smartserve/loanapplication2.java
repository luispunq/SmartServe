package com.example.london.smartserve;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class loanapplication2 extends AppCompatActivity {

    private ImageView mMemberphoto,smember;
    private TextView mMembername,mGroupName,mTotalSavings,mLoanlimit,mIntrest,mInstallments,mAttach,selectedmember;
    private EditText mAmountreq,mInstallmentperiod,reco;
    private Button mCommit,sav1,sav2,sav3,sav4,sav5,sav6;
    private DatabaseReference mDatabasemembers,mDatabasegroup,mDatabaseguarantors,loanrequest,extras,mD;
    private StorageReference store;
    public static final int GALLERY_PICK = 1889;
    private FirebaseAuth mAuth;
    private String user,grid=null;
    private LinearLayout lay1,lay2,lay3,lay4,lay5,lay6;
    private ProgressDialog mProgressdialog;
    private Uri mImageuri=null;
    private int monnthlypay=0;
    private RelativeLayout rl1,rl2,rl3,rl4,rl5,rl6;
    private String totalsavings=null,name=null,image=null,group=null,loankey=null,b_key,userselected=null;
    private double finalamount;
    private ProgressDialog mProgress;
    private Toolbar mBar;
    private AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanapplication2);
        mAuth=FirebaseAuth.getInstance();
        mBar=findViewById(R.id.bard2);
        setSupportActionBar(mBar);
        Bundle extra = getIntent().getExtras();
        user = extra.getString("key");
        b_key = extra.getString("gkey");
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);
        mMemberphoto=findViewById(R.id.loanappmemberprofpic);
        selectedmember=findViewById(R.id.title9name);
        smember=findViewById(R.id.itemname1);
        mMembername=findViewById(R.id.loanappmembername);
        mGroupName=findViewById(R.id.loanappmembergroup);
        mTotalSavings=findViewById(R.id.savingstat);
        mLoanlimit=findViewById(R.id.loanstat);
        mIntrest=findViewById(R.id.loanintrest);
        reco=findViewById(R.id.recomendation);
        mInstallments=findViewById(R.id.title14);
        mAmountreq=findViewById(R.id.reqamount);
        mInstallmentperiod=findViewById(R.id.title10);
        mAttach=findViewById(R.id.title15);
        mInstallmentperiod.addTextChangedListener(filterTextWatcher);
        mCommit=findViewById(R.id.loanappbutton);
        sav4=findViewById(R.id.save4);
        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mDatabasegroup=FirebaseDatabase.getInstance().getReference().child("loanrequests");
        mDatabaseguarantors=FirebaseDatabase.getInstance().getReference().child("loanextras");
        store= FirebaseStorage.getInstance().getReference().child("loanapplications");
        mD=FirebaseDatabase.getInstance().getReference().child("members").child("allmembers");


        loanrequest=mDatabasegroup.push();
        loankey=loanrequest.getKey();


        mProgressdialog=new ProgressDialog(this);

        sav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecyclerView recyclerView=new RecyclerView(loanapplication2.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(loanapplication2.this));
                final AlertDialog.Builder builder = new AlertDialog.Builder(loanapplication2.this);
                builder.setTitle("Select Member")
                        .setView(recyclerView);
                recycleradapt(recyclerView,b_key);
                alert11 = builder.create();
                alert11.show();
            }
        });


        mDatabasemembers.child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name= (String) dataSnapshot.child("name").getValue();
                image= (String) dataSnapshot.child("profileImage").getValue();
                group=(String)dataSnapshot.child("group").getValue();
                grid=dataSnapshot.child("groupid").getValue().toString();
                mMembername.setText(name);
                mGroupName.setText(group);
                Picasso.with(loanapplication2.this).load(image).into(mMemberphoto);
                Picasso.with(loanapplication2.this).load(image).into(smember);
                selectedmember.setText(name);
                userselected=user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemembers.child("project").child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalsavings= (String) dataSnapshot.child("totalsavings").getValue();

                if (totalsavings==null){
                    totalsavings="1";
                }

                mTotalSavings.setText("Kshs."+totalsavings);
                double savings=Double.parseDouble(totalsavings);
                double limit=savings*3;
                String loanlimit=String.valueOf(limit);
                mLoanlimit.setText("Kshs. "+loanlimit);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyloan();
            }
        });

    }

    private void recycleradapt(RecyclerView r, final String b_key) {
        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                mD.child(b_key)
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
                        selectedmember.setText(model.getName());
                        userselected=model.getUid();
                        alert11.dismiss();

                        Toast.makeText(loanapplication2.this, "Member Added", Toast.LENGTH_LONG).show();

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


    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            double principal=Integer.parseInt(mAmountreq.getText().toString());
            String timet=mInstallmentperiod.getText().toString();

            if (timet.equals("")){
                timet="1";

            }

            double time=Double.parseDouble(timet);

            finalamount=(principal*0.01*time)+principal;

            monnthlypay=(int)(finalamount/time);

            mInstallments.setText("Kshs. "+String.valueOf(monnthlypay));

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void applyloan() {
        mProgressdialog.setMessage("Please wait..");
        mProgressdialog.setMessage("Applying for Loan..");
        mProgressdialog.setCanceledOnTouchOutside(false);
        mProgressdialog.show();
        final String amountrequested=String.valueOf(finalamount);
        final String amountcash=mAmountreq.getText().toString();
        final String installmentperiod=mInstallmentperiod.getText().toString();
        final String rate=mIntrest.getText().toString();
        final String monthlyins=String.valueOf(monnthlypay);
        String notes=reco.getText().toString();

        if (!TextUtils.isEmpty(notes)){

        loanrequest.child("requestid").setValue(loankey);
        loanrequest.child("requestdate").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        loanrequest.child("amountcash").setValue(amountcash);
        loanrequest.child("userid").setValue(user);
        loanrequest.child("amountrequested").setValue(amountrequested);
        loanrequest.child("rate").setValue(rate);
        loanrequest.child("period").setValue(installmentperiod);
        loanrequest.child("monthlypay").setValue(monthlyins);
        loanrequest.child("status").setValue("pending");
        loanrequest.child("recommendation").setValue(userselected);
        loanrequest.child("image").setValue(notes);


            mProgressdialog.dismiss();
            Toast.makeText(loanapplication2.this, "Loan Applied!", Toast.LENGTH_LONG).show();

            Intent back=new Intent(loanapplication2.this,memberlistc.class);
            back.putExtra("key",grid);
            startActivity(back);
            finish();

        }else {
            Toast.makeText(loanapplication2.this, "Please Enter Loan Item Details!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_PICK)
                //onSelectFromGalleryResult(data);
                mImageuri=data.getData();
        }
    }
}
