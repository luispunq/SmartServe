package com.example.london.smartserve;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public  class upcomingMeetingsViewHolder extends RecyclerView.ViewHolder{
    private final CardView layout;
    final CardView.LayoutParams layoutParams;
    View mView;
    private DatabaseReference m;


    public upcomingMeetingsViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        m= FirebaseDatabase.getInstance().getReference().child("details");
        layout =  itemView.findViewById(R.id.upcmngcard);
        layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    private void Layout_hide(){
        layoutParams.height=0;
        layout.setLayoutParams(layoutParams);
    }

    public void setGroupName(String groupname){
        TextView mGroupname = (TextView)mView.findViewById(R.id.upgrpname);
        mGroupname.setText(groupname);
    }
    public void setVenue(String venue){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpven);
        mGrouploc.setText(venue);
    }
    public void setDate(String date){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpdate);
        mGrouploc.setText(date);
    }
    public void setAmount(String amount){
        m.child(amount).child("groupdetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("togivefac").exists()){
                    String n=dataSnapshot.child("togivefac").getValue().toString();
                    TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                    mGrouploc.setText(n);
                }else {
                    String n="0";
                    TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                    mGrouploc.setText(n);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
