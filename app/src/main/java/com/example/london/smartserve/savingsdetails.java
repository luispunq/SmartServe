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

public class savingsdetails extends AppCompatActivity {
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
        setContentView(R.layout.activity_savingsdetails);
        mAuth=FirebaseAuth.getInstance();
        mUid=getIntent().getExtras().getString("key");
        mMemberphoto=findViewById(R.id.savingsmemberprofpic);
        mMembername=findViewById(R.id.savingsmembername);
        mGroupName=findViewById(R.id.savingsmembergroup);
        mCurrentloan=findViewById(R.id.savingstat);
        mloanhist=findViewById(R.id.savingslist);
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
                Picasso.with(savingsdetails.this).load(nsimage).into(mMemberphoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabasemembers.child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currsavings=(String)dataSnapshot.child("totalsavings").getValue();
                mCurrentloan.setText("Kshs. "+df.format(Double.parseDouble(currsavings)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<savings,savingsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<savings, savingsViewHolder>(

                savings.class,
                R.layout.savingshistory_row,
                savingsViewHolder.class,
                mDatabasemembers.child("savings").child("savings")
        )
        {
            @Override
            protected void populateViewHolder(final savingsViewHolder viewHolder, final savings model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setFacil(model.getFac());
                viewHolder.setDetail(model.getDetails());
                viewHolder.setDepo(model.getSave());
                viewHolder.setWithd(model.getWith());
                viewHolder.setBal(model.getBal());
            }
        };

        mloanhist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class savingsViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        DecimalFormat df = new DecimalFormat("##,###,###.#");
        View mView;


        public savingsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.savingshistory_row);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.histdate);
            loandate.setText(date);
        }
        public void setDetail(String detail){
            TextView amountgiven = (TextView)mView.findViewById(R.id.histdetail);
            amountgiven.setText(detail);
        }
        public void setDepo(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.histsaving);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(amount)));
        }
        public void setWithd(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.histwithdraw);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(amount)));
        }
        public void setBal(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.histbalance);
            amountgiven.setText("Kshs. "+df.format(Double.parseDouble(amount)));
        }
        public void setFacil(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.histfac);
            amountgiven.setText(amount);
        }
    }
}
