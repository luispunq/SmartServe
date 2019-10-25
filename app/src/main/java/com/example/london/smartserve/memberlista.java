package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class    memberlista extends AppCompatActivity {

    private RecyclerView mMemberlist;
    private DatabaseReference mDatabase,mDatatabasekey,data;
    private EditText mSearch;
    private String displaykey;
    private String grpkey;
    private Toolbar bar;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist);
        grpkey=getIntent().getExtras().getString("key");
        mMemberlist = (RecyclerView) findViewById(R.id.memberslist);
        bar=findViewById(R.id.bars);
        setSupportActionBar(bar);
        ActionBar ab = getSupportActionBar();
        //ab.setDisplayShowHomeEnabled(true);
        //ab.setDisplayHomeAsUpEnabled(true);
        mMemberlist.setHasFixedSize(true);
        mMemberlist.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(grpkey);
        data = FirebaseDatabase.getInstance().getReference().child("members");
        //mDatabase.keepSynced(true);
        mProgress=new ProgressDialog(this);
        mSearch=findViewById(R.id.editText2);
        mSearch.addTextChangedListener(filterTextWatcher);

    }



    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            String uname=mSearch.getText().toString().trim();
            try {
                firebaseuserserach(uname);
            }catch (Exception e){
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
        Query firebaseSearchQuery = mDatabase.orderByChild("name").startAt(uname).endAt(uname + "\uf8ff");

        FirebaseRecyclerAdapter<member,membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, membersViewHolder>(
                member.class,
                R.layout.member_row,
                membersViewHolder.class,
                firebaseSearchQuery
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

                        final AlertDialog.Builder builder = new AlertDialog.Builder(memberlista.this);
                        builder.setTitle("Select Payment Option")
                                .setCancelable(true)
                                .setItems(R.array.Options, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                Intent payments = new Intent(memberlista.this,paymentsec.class);
                                                payments.putExtra("key",u_key);
                                                startActivity(payments);
                                                finish();
                                                break;
                                            case 1:
                                                Intent userwindef = new Intent(memberlista.this,schoolfeespayment.class);
                                                userwindef.putExtra("key",u_key);
                                                startActivity(userwindef);
                                                finish();
                                                break;
                                            case 2:
                                                Intent userwinde = new Intent(memberlista.this,memberpage.class);
                                                userwinde.putExtra("key",u_key);
                                                startActivity(userwinde);
                                                finish();
                                                break;
                                            case 3:
                                                Intent userwinde3 = new Intent(memberlista.this,mpesapayment2.class);
                                                userwinde3.putExtra("key",u_key);
                                                startActivity(userwinde3);
                                                finish();
                                                break;
                                            case 4:
                                                Intent userwinder3 = new Intent(memberlista.this,grouppayment.class);
                                                userwinder3.putExtra("key",u_key);
                                                startActivity(userwinder3);
                                                finish();
                                                break;
                                        }
                                    }
                                });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                });


            }
        };
        mMemberlist.setAdapter(firebaseRecyclerAdapter);
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
