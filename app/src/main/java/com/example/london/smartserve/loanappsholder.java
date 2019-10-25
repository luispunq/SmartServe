package com.example.london.smartserve;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class loanappsholder extends RecyclerView.ViewHolder {
    private final RelativeLayout layout;
    final RelativeLayout.LayoutParams layoutParams;
    private final DatabaseReference mDatabaseMembers;


    View mView;

    public loanappsholder(View itemView) {
        super(itemView);
        mDatabaseMembers= FirebaseDatabase.getInstance().getReference().child("members");
        mView = itemView;
        layout = (RelativeLayout) itemView.findViewById(R.id.loadapp_item);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void Layout_hide(){
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
