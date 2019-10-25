package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class loanapplications extends AppCompatActivity {
    private ImageView mGroupPhoto;
    private TextView mGroupname;
    private DatabaseReference mDatabasegroup,mDatabase,mDatabasemembers;
    private RecyclerView mLoansapplicationslist;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanapplications);
        mGroupname=findViewById(R.id.groupname1);
        mGroupPhoto=findViewById(R.id.imageView111);
        user=getIntent().getExtras().getString("key");
        mLoansapplicationslist=findViewById(R.id.loansapplicants);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mLoansapplicationslist.setHasFixedSize(true);
        mLoansapplicationslist.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mDatabasegroup= FirebaseDatabase.getInstance().getReference().child("details");
        //mDatabasegroup.keepSynced(true);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("loanrequests");
        //mDatabase.keepSynced(true);
        mDatabasemembers=FirebaseDatabase.getInstance().getReference().child("members").child(user);

        mDatabasemembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String grpkey=(String)dataSnapshot.child("details").child("groupid").getValue();
                mDatabasegroup.child(grpkey).child("groupdetails").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String gimage= (String) dataSnapshot.child("profileImage").getValue();
                        String ggroup=(String)dataSnapshot.child("groupName").getValue();
                        mGroupname.setText(ggroup);
                        Picasso.with(loanapplications.this).load(gimage).into(mGroupPhoto);
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
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<loanapps,loansViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<loanapps, loansViewHolder>(
                loanapps.class,
                R.layout.loanapp_row,
                loansViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final loansViewHolder viewHolder, final loanapps model, int position) {
                final String u_key=getRef(position).getKey();

                if (model.getStatus().equals("pending")){
                    viewHolder.setPhoto(getApplicationContext(),model.getUserid());
                    viewHolder.setapplierName(model.getUserid());
                    viewHolder.setapplydate(model.getRequestdate());
                    viewHolder.setapplyamount(model.getAmountrequested());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent userwindow = new Intent(loanapplications.this,loanprocessing.class);
                            userwindow.putExtra("id",u_key);
                            startActivity(userwindow);
                        }
                    });
                }else{
                    viewHolder.Layout_hide();
                }


            }
        };
        mLoansapplicationslist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class loansViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        private final DatabaseReference mDatabaseMembers;

        View mView;

        public loansViewHolder(View itemView) {
            super(itemView);
            mDatabaseMembers=FirebaseDatabase.getInstance().getReference().child("members");
            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.loadapp_item);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }
        public void setapplierName(String appname){
            mDatabaseMembers.child(appname).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name= (String) dataSnapshot.child("name").getValue();
                    TextView mAppliername = (TextView)mView.findViewById(R.id.appliername);
                    mAppliername.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setapplydate(String date){
            TextView mDateapp = (TextView)mView.findViewById(R.id.dateapplied);
            mDateapp.setText(date);
        }
        public void setapplyamount(String amount){
            TextView mAmountapp = (TextView)mView.findViewById(R.id.appliedamount);
            mAmountapp.setText(amount);
        }
        public void setPhoto(final Context ctx, String uid) {
            mDatabaseMembers.child(uid).child("details").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image= (String) dataSnapshot.child("profileImage").getValue();
                    ImageView serphoto=(ImageView)mView.findViewById(R.id.applierimage);
                    Picasso.with(ctx).load(image).into(serphoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
