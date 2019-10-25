package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class mpesapayment extends AppCompatActivity {
    private EditText mPhoneNumber,mGname,mReason;
    private Button mSubmit,mProcess;
    private EditText mAmount,mSender,mTrannumber;
    private RecyclerView mGlist;
    private DatabaseReference mDatabase,mD,mD1,mD2,mD3,mD4,mD5,mD6;
    private ProgressDialog mProgress;
    private String ngame,user,grid,amt,rsn,txn,date;
    private String name,ops;
    private FirebaseAuth mAuth;
    private AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesapaymentcomp);
        mGlist=findViewById(R.id.glist);
        mGlist.setHasFixedSize(true);
        mGlist.setLayoutManager(new LinearLayoutManager(this));
        mTrannumber=findViewById(R.id.txnnum);
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);
        mAuth=FirebaseAuth.getInstance();
        ops=mAuth.getCurrentUser().getUid();
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
        mD6=FirebaseDatabase.getInstance().getReference().child("Employees").child(ops);
        //mD6.keepSynced(true);
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
        Query firebaseSearchQuery = mDatabase.orderByChild("name").startAt(uname.toUpperCase()).endAt(uname.toUpperCase()+ "\uf8ff");

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
                ngame=model.getName();

                viewHolder.setGroupName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RecyclerView recyclerView=new RecyclerView(mpesapayment.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mpesapayment.this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mpesapayment.this);
                        builder.setTitle("Select Member")
                                .setCancelable(true)
                                .setView(recyclerView);
                        recycleradapt(recyclerView,b_key,model.getName());
                        alert11 = builder.create();
                        alert11.show();
                    }
                });

            }
        };
        mGlist.setAdapter(firebaseRecyclerAdapter);
    }
    private void recycleradapt(RecyclerView r, final String b_key, final String ngame) {
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
                        alert11.dismiss();
                        amt=mAmount.getText().toString();
                        name=model.getName();
                        date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
                        txn=mTrannumber.getText().toString();
                        rsn=mReason.getText().toString();
                        String sts="received";


                        DatabaseReference newMpesa=mD2.child(ngame).push();
                        newMpesa.child("id").setValue(newMpesa.getKey());
                        newMpesa.child("Amount").setValue(amt);
                        newMpesa.child("Member").setValue(model.getUid());
                        newMpesa.child("Name").setValue(name);
                        newMpesa.child("Date").setValue(date);
                        newMpesa.child("TransactionNumber").setValue(txn);
                        newMpesa.child("Reason").setValue(rsn);
                        newMpesa.child("GroupID").setValue(b_key);
                        newMpesa.child("Status").setValue(sts);


                        user=model.getUid();
                        grid=b_key;

                        DatabaseReference pm=mD3.child(user).child("Mpesa");
                        pm.child("amount").setValue(amt);
                        confirm();

                        //FirebaseDatabase.getInstance().getReference().child("members").child(u_key).child("details").child("clearance").setValue("groupAC");
                        //alert11.dismiss();

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


    public void confirm() {
        mD2.child(ngame).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference reference=mD4.child(date).push();
                reference.child("date").setValue(date);
                reference.child("txn").setValue(txn);
                reference.child("name").setValue(name);
                reference.child("contact").setValue(mPhoneNumber.getText().toString());
                reference.child("amount").setValue(amt);
                reference.child("group").setValue(ngame);
                reference.child("rsn").setValue(rsn);

                DatabaseReference reference2=mD4.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all").push();
                reference2.child("date").setValue(date);
                reference2.child("txn").setValue(txn);
                reference2.child("name").setValue(name);
                reference2.child("contact").setValue(mPhoneNumber.getText().toString());
                reference2.child("amount").setValue(amt);
                reference2.child("group").setValue(ngame);

                DatabaseReference referece=mD4.child("all").push();
                referece.child("date").setValue(date);
                referece.child("txn").setValue(txn);
                referece.child("name").setValue(name);
                referece.child("contact").setValue(mPhoneNumber.getText().toString());
                referece.child("amount").setValue(amt);
                referece.child("group").setValue(ngame);

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



                Toast.makeText(mpesapayment.this, "Processed!", Toast.LENGTH_LONG).show();
                genRCPT(amt, user, date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void genRCPT(String amount, final String memid, String date) {
        final DatabaseReference nr = mD5.child(grid).child("secretary").child("Mpesa").child(memid);
        DatabaseReference nnr = nr.child("details").push();
        nnr.child("amount").setValue(amount);
        nr.child("date").setValue(date);
        nr.child("name").setValue(name);
        nr.child("rsn").setValue(rsn);
        mD6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String facname = dataSnapshot.child("name").getValue().toString();
                nr.child("officer").setValue(facname);
                //alert11.dismiss();

                Toast.makeText(mpesapayment.this, "Receipt Generated!", Toast.LENGTH_LONG).show();

                Intent bac = new Intent(mpesapayment.this,secmpesarcpts.class);
                Bundle extras = new Bundle();
                extras.putString("key", grid);
                extras.putString("user", memid);
                bac.putExtras(extras);
                startActivity(bac);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
