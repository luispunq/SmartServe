package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mpesapayment2 extends AppCompatActivity {
    private EditText mPhoneNumber,mGname,mReason;
    private Button mSubmit,mProcess;
    private EditText mAmount,mSender,mTrannumber;
    private RecyclerView mGlist;
    private DatabaseReference mDatabase,mD,mD1,mD2,mD3,mD4,mD5,mD6;
    private ProgressDialog mProgress;
    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesapayment);
        mGlist=findViewById(R.id.glist);
        mGlist.setHasFixedSize(true);
        mGlist.setLayoutManager(new LinearLayoutManager(this));
        mTrannumber=findViewById(R.id.txnnum);
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);
        mSender=findViewById(R.id.mpesasender);
        mAmount=findViewById(R.id.mpesaamount);
        mReason=findViewById(R.id.reason);
        mReason.setText("Contribution");
        mPhoneNumber=findViewById(R.id.editText);
        mGname=findViewById(R.id.editText2);
        mGname.addTextChangedListener(filterTextWatcher);
        mSubmit=findViewById(R.id.button10);
        mProcess=findViewById(R.id.button11);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Groups");
        //mDatabase.keepSynced(true);
        mD5=FirebaseDatabase.getInstance().getReference().child("Receipts");
        //mD5.keepSynced(true);
        mD1=FirebaseDatabase.getInstance().getReference().child("members").child("allmembers");
        //mD1.keepSynced(true);
        mD2=FirebaseDatabase.getInstance().getReference().child("Mpesa").child("Received");
        //mD2.keepSynced(true);
        mD3=FirebaseDatabase.getInstance().getReference().child("members");
        //mD3.keepSynced(true);
        mD4=FirebaseDatabase.getInstance().getReference().child("Mpesa").child("Records");
        //mD4.keepSynced(true);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPesaTrn();
            }
        });



    }

    public static class GroupViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupslistcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textView9);
            mGroupname.setText(groupname);
        }
    }

    private void mPesaTrn() {
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            String uname=mGname.getText().toString().trim();

            try {
                firebaseuserserach(uname);
            }catch (Exception e){
                Log.e("exception","Error!");
            }

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void firebaseuserserach(String uname) {
        //super.onStart();
        Query firebaseSearchQuery = mDatabase.orderByChild("name").startAt(uname.toUpperCase()).endAt(uname + "\uf8ff");

        FirebaseRecyclerAdapter<group,GroupViewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<group,GroupViewholder>(
                group.class,
                R.layout.grouplist_row,
                GroupViewholder.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final group model, int position) {

                final String b_key=getRef(position).getKey();
                final String bname=model.getName();


                viewHolder.setGroupName(model.getName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RecyclerView recyclerView=new RecyclerView(mpesapayment2.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mpesapayment2.this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mpesapayment2.this);
                        builder.setTitle("Select Member")
                                .setCancelable(true)
                                .setView(recyclerView);
                        recycleradapt(recyclerView,b_key,bname);
                        alert11 = builder.create();
                        alert11.show();
                    }
                });

            }
        };
        mGlist.setAdapter(firebaseRecyclerAdapter);
    }
    private void recycleradapt(RecyclerView r, final String b_key, final String bname) {
        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(

                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                mD1.child(b_key)
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
                        //mProgress.show();
                        //alert11.dismiss();
                        final String amt=mAmount.getText().toString();
                        String name=model.getName();
                        String date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                        String txn=mTrannumber.getText().toString();
                        String rsn=mReason.getText().toString();
                        String sts="received";

                        DatabaseReference newMpesa=mD2.child(bname).push();
                        newMpesa.child("id").setValue(newMpesa.getKey());
                        newMpesa.child("Amount").setValue(amt);
                        newMpesa.child("Member").setValue(model.getUid());
                        newMpesa.child("Name").setValue(name);
                        newMpesa.child("Date").setValue(date);
                        newMpesa.child("TransactionNumber").setValue(txn);
                        newMpesa.child("Reason").setValue(rsn);
                        newMpesa.child("GroupID").setValue(b_key);
                        newMpesa.child("Status").setValue(sts);

                        DatabaseReference reference=mD4.child(date).push();
                        reference.child("date").setValue(date);
                        reference.child("txn").setValue(txn);
                        reference.child("name").setValue(name);
                        reference.child("contact").setValue(mPhoneNumber.getText().toString());
                        reference.child("amount").setValue(amt);
                        reference.child("group").setValue(bname);

                        DatabaseReference reference2=mD4.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).push();
                        reference2.child("date").setValue(date);
                        reference2.child("txn").setValue(txn);
                        reference2.child("name").setValue(name);
                        reference2.child("contact").setValue(mPhoneNumber.getText().toString());
                        reference2.child("amount").setValue(amt);
                        reference2.child("group").setValue(bname);

                        DatabaseReference referece=mD4.child("all").push();
                        referece.child("date").setValue(date);
                        referece.child("txn").setValue(txn);
                        referece.child("name").setValue(name);
                        referece.child("contact").setValue(mPhoneNumber.getText().toString());
                        referece.child("amount").setValue(amt);
                        referece.child("group").setValue(bname);


                        mD4.child("general").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("currentfloat").exists()){
                                    String tot=dataSnapshot.child("currentfloat").getValue().toString();
                                    double tots=Double.parseDouble(tot);
                                    double newtots=tots+Double.parseDouble(amt);
                                    DatabaseReference refs=mD4.child("general");
                                    refs.child("currentfloat").setValue(String.valueOf(newtots));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference pm=mD3.child(model.getUid()).child("Mpesa");
                        pm.child("amount").setValue(amt);
                        //  mProgress.dismiss();
                        Toast.makeText(mpesapayment2.this, "Processed!", Toast.LENGTH_LONG).show();
                        Intent bac = new Intent(mpesapayment2.this,memberlist.class);
                        bac.putExtra("key", bname);
                        startActivity(bac);
                        finish();
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
}
