package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class employeecashtrack extends AppCompatActivity {
    private CardView card1;
    private TextView textView,cashgvn,grpass,gMisc;
    private ImageView imageView;
    private Toolbar mBar;
    private FirebaseAuth mAuth;
    private String user=null,fieldid,date=null;
    private DatabaseReference databaseReference,mD;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeecashtrack);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("key");
        date = extras.getString("date");
        mBar=findViewById(R.id.bar1);
        setSupportActionBar(mBar);
        getSupportActionBar().setTitle("Coordinator");
        mAuth=FirebaseAuth.getInstance();
        cashgvn=findViewById(R.id.fieldamountgiven);
        textView=findViewById(R.id.cordname);
        grpass=findViewById(R.id.fieldgrpgiven);
        //grpass=findViewById(R.id.fieldgrpgiven);
        imageView=findViewById(R.id.cordprofpic);
        gMisc=findViewById(R.id.aMisc);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mD=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user).child(date);

        card1=findViewById(R.id.chosegroup);
        recyclerView=findViewById(R.id.grpsgiven);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        databaseReference.child("Employees").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("fieldid").exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    fieldid = dataSnapshot.child("fieldid").getValue().toString();
                    textView.setText(name);
                    Picasso.with(getApplicationContext()).load(image).into(imageView);

                    mD.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(fieldid).child("misccash").exists()){
                            gMisc.setText("Kshs. "+dataSnapshot.child(fieldid).child("misccash").getValue().toString());
                            }
                            else {
                                gMisc.setText("Kshs. 0");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    databaseReference.child("Accounts").child(user).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                            cashgvn.setText(dataSnapshot.child("Amountin").getValue().toString());
                            grpass.setText("Kshs. "+dataSnapshot.child("Amountout").getValue().toString());
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder>(
                            groupsgiven.class,
                            R.layout.groupgivenlist_row,
                            GroupgivenViewholder.class,
                            mD.child(fieldid).child("groupsgiven")
                    )
                    {
                        @Override
                        protected void populateViewHolder(final GroupgivenViewholder viewHolder, final groupsgiven model, int position) {
                            viewHolder.setGroupName(model.getName());
                            viewHolder.setGroupAmount(model.getAmount());

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent next = new Intent(employeecashtrack.this,report2.class);
                                    Bundle extrasd = new Bundle();
                                    extrasd.putString("key", model.getGroupid());
                                    extrasd.putString("date", date);
                                    next.putExtras(extrasd);
                                    startActivity(next);
                                }
                            });
                        }
                    };

                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                }else {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    fieldid = "";
                    textView.setText(name);
                    Picasso.with(getApplicationContext()).load(image).into(imageView);

                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupgivenViewholder>(
                            groupsgiven.class,
                            R.layout.groupgivenlist_row,
                            GroupgivenViewholder.class,
                            mD.child(fieldid).child("groupsgiven")
                    )
                    {
                        @Override
                        protected void populateViewHolder(final GroupgivenViewholder viewHolder, final groupsgiven model, int position) {
                            viewHolder.setGroupName(model.getName());
                            viewHolder.setGroupAmount(model.getAmount());

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent next = new Intent(employeecashtrack.this,report2.class);
                                    Bundle extrasd = new Bundle();
                                    extrasd.putString("key", model.getGroupid());
                                    extrasd.putString("date", date);
                                    next.putExtras(extrasd);
                                    startActivity(next);
                                }
                            });

                        }
                    };

                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class GroupgivenViewholder extends RecyclerView.ViewHolder{
        View mView;


        public GroupgivenViewholder(View itemView) {
            super(itemView);

            mView = itemView;

        }



        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww);
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww2);
            mGroupname.setText("Kshs. "+groupname);
        }
    }
}
