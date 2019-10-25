package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.anychart.AnyChart;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class mastergrouplistadapter extends FirebaseRecyclerAdapter<mastergroup,mastergrouplistholder>{

    private Context context;
    private DatabaseReference mDatabase,mDatabasegroup;
    private String s1;
    private String s2;
    private String s3;


    public mastergrouplistadapter(Class<mastergroup> modelClass, int modelLayout, Class<mastergrouplistholder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        mDatabasegroup=FirebaseDatabase.getInstance().getReference().child("details");
    }

    @Override
    protected void populateViewHolder(final mastergrouplistholder viewHolder, final mastergroup model, int position) {
        final String key=getRef(position).getKey();
        mDatabasegroup.child(key).child("groupdetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("numbers").exists()){
                s1=dataSnapshot.child("numbers").getValue().toString();
                }
                else {
                    s1="0";
                }
                if (dataSnapshot.child("location").exists()){
                    s2=dataSnapshot.child("location").getValue().toString();
                }else {
                    s2="Location not set";
                }
                if (dataSnapshot.child("nextdate").exists()) {
                    s3 = dataSnapshot.child("nextdate").getValue().toString();
                }else {
                    s3="Date not set";
                }
                //String s4=dataSnapshot.child("profileImage").getValue().toString();

                viewHolder.grname.setText(model.getName());
                viewHolder.grmeetdate.setText(s3);
                viewHolder.grlocation.setText(s2);
                viewHolder.grnumbers.setText(s1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewstats = new Intent(context,groupstats.class);
                viewstats.putExtra("key",key);
                context.startActivity(viewstats);
            }
        });

    }
}
