package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class memberlistg extends AppCompatActivity {

    private RecyclerView mMemberlist;
    private EditText mSearch;
    private DatabaseReference mDatabase,mDatatabasekey,data;
    private String meetid=null;
    private String grpkey,unow;
    private Toolbar bar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist);
        grpkey= getIntent().getExtras().getString("key");
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        unow = extras.getString("user");
        mMemberlist = (RecyclerView) findViewById(R.id.memberslist);
        mSearch=findViewById(R.id.editText2);
        mSearch.addTextChangedListener(filterTextWatcher);
        mMemberlist.setHasFixedSize(true);
        mMemberlist.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(grpkey);

    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            String uname=mSearch.getText().toString().trim();
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

    private void firebaseuserserach(final String uname) {
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
                        Intent next = new Intent(memberlistg.this,editmember.class);
                        Bundle extras = new Bundle();
                        extras.putString("key", grpkey);
                        extras.putString("user", u_key);
                        next.putExtras(extras);
                        startActivity(next);
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
