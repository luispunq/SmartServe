package com.example.london.smartserve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.DecimalFormat;

public class loandetails extends AppCompatActivity {
    private ImageView mMemberphoto;
    private TextView mMembername,mGroupName,mCurrentloan;
    private DatabaseReference mDatabasemembers;
    private FirebaseAuth mAuth;
    private String mUid;
    private RecyclerView mloanhist;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loandetails);
        mAuth=FirebaseAuth.getInstance();
        mUid=getIntent().getExtras().getString("key");
        mMemberphoto=findViewById(R.id.loansmemberprofpic);
        mMembername=findViewById(R.id.loansmembername);
        mGroupName=findViewById(R.id.loansmembergroup);
        mCurrentloan=findViewById(R.id.loansstat);
        mloanhist=findViewById(R.id.loanslist);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));
        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("members").child(mUid);
        mDatabasemembers.keepSynced(true);

        mDatabasemembers.child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nsname= (String) dataSnapshot.child("name").getValue();
                String nsimage= (String) dataSnapshot.child("profileImage").getValue();
                String nsgroup=(String)dataSnapshot.child("group").getValue();
                mMembername.setText(nsname);
                mGroupName.setText(nsgroup);
                Picasso.with(loandetails.this).load(nsimage).into(mMemberphoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemembers.child("loans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currloan=(String)dataSnapshot.child("totalloan").getValue();
                mCurrentloan.setText("Kshs. "+df.format(Double.parseDouble(currloan)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<loans,loansViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<loans, loansViewHolder>(

                loans.class,
                R.layout.loanshist_row,
                loansViewHolder.class,
                mDatabasemembers.child("loans").child("loanss")
        )
        {
            @Override
            protected void populateViewHolder(final loansViewHolder viewHolder, final loans model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setLoanbf(model.getLoanbf());
                viewHolder.setInst(model.getInst());
                viewHolder.setLoancf(model.getBal());
                viewHolder.setFacl(model.getFac());
            }
        };

        mloanhist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class loansViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        DecimalFormat df = new DecimalFormat("##,###,###.#");
        View mView;


        public loansViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.loanscard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.lhistdate);
            loandate.setText(date);
        }
        public void setLoanbf(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.lhistloanbf);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(amount)));
        }
        public void setInst(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.lhistinst);
            loandate.setText("Kshs. "+df.format(Double.parseDouble(date)));
        }
        public void setLoancf(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.lhistbalance);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(amount)));
        }
        public void setFacl(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.lhistfac);
            amountgiven.setText(amount);
        }

    }
}
