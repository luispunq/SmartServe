package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

public class memberlistc extends AppCompatActivity {

    private RecyclerView mMemberlist;
    private EditText mSearch;
    private DatabaseReference mDatabase,mDatatabasekey,data;
    private String userkey=null;
    private String grpkey,gname=null;
    private Toolbar bar;
    private ProgressDialog mProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist);
        grpkey=getIntent().getExtras().getString("key");
        mMemberlist = (RecyclerView) findViewById(R.id.memberslist);
        mSearch=findViewById(R.id.editText2);
        mProgress=new ProgressDialog(this);
        mSearch.addTextChangedListener(filterTextWatcher);
        mMemberlist.setHasFixedSize(true);
        mMemberlist.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(grpkey);

        //mDatabase.keepSynced(true);


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


                        final AlertDialog.Builder builder = new AlertDialog.Builder(memberlistc.this);
                        builder.setTitle("Select Options")
                                .setCancelable(true)
                                .setItems(R.array.Options4, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                Intent loans = new Intent(memberlistc.this,loanapplication.class);
                                                Bundle extras = new Bundle();
                                                extras.putString("key", u_key);
                                                extras.putString("gkey", grpkey);
                                                extras.putString("type", "fill");
                                                loans.putExtras(extras);
                                                startActivity(loans);
                                                finish();
                                                break;
                                            case 1:
                                                Intent usewinded = new Intent(memberlistc.this,loanapplication2.class);
                                                Bundle exras = new Bundle();
                                                exras.putString("key", u_key);
                                                exras.putString("gkey", grpkey);
                                                usewinded.putExtras(exras);
                                                startActivity(usewinded);
                                                finish();
                                                break;
                                            case 2:
                                                Intent userwinde = new Intent(memberlistc.this,memberpage.class);
                                                userwinde.putExtra("key",u_key);
                                                startActivity(userwinde);
                                                finish();
                                                break;
                                            case 3:
                                                Intent loansf = new Intent(memberlistc.this,loanapplication.class);
                                                Bundle extrasf = new Bundle();
                                                extrasf.putString("key", u_key);
                                                extrasf.putString("gkey", grpkey);
                                                extrasf.putString("type", "form");
                                                loansf.putExtras(extrasf);
                                                startActivity(loansf);
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
