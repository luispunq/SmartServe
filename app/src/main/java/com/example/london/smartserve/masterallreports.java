package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

public class masterallreports extends AppCompatActivity {
    private TextView mCashfrmOffice,mCashcoll,mCashgiven,mCashback,mNumberofmeets;
    private DatabaseReference mDatabase,mD2;
    private double totalcl=0,totalcp=0,totalcb=0,totalcf=0;
    private RecyclerView recyclerView;
    private int count=0;
    private String date=null,month,year,yearT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterallreports);
        mCashfrmOffice=findViewById(R.id.cashfromoffice);
        date=getIntent().getExtras().getString("date");
        String string = date;
        String[] parts = string.split("-");
        month = parts[0];
        year = parts[1];

        if (year.equals("18")){
            yearT="2018";
        }else if (year.equals("19")){
            yearT="2019";
        }


        mCashcoll=findViewById(R.id.cashcollected);
        mCashgiven=findViewById(R.id.cashgiven);
        mCashback=findViewById(R.id.cashback);
        mNumberofmeets=findViewById(R.id.meetings);
        recyclerView=findViewById(R.id.daymeets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("finances").child("cashdetails");
        //mDatabase.keepSynced(true);
        mD2= FirebaseDatabase.getInstance().getReference().child("meetings").child("master");
        //mD2.keepSynced(true);

        mD2.child(yearT).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNumberofmeets.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child(yearT).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    double cl=Double.parseDouble(snapshot.child("cashcollected").getValue().toString());
                    double cp=Double.parseDouble(snapshot.child("cashpaid").getValue().toString());
                    double cb=Double.parseDouble(snapshot.child("cashback").getValue().toString());
                    double cf=Double.parseDouble(snapshot.child("cashfromoffice").getValue().toString());

                    totalcl=totalcl+cl;
                    totalcp=totalcp+cp;
                    totalcb=totalcb+cb;
                    totalcf=totalcf+cf;

                    mCashcoll.setText("Kshs. "+String.valueOf(totalcl));
                    mCashgiven.setText("Kshs. "+String.valueOf(totalcp));
                    mCashback.setText("Kshs. "+String.valueOf(totalcb));
                    mCashfrmOffice.setText("Kshs. "+String.valueOf(totalcf));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<meetings2,masterallreports.meetsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<meetings2, masterallreports.meetsViewHolder>(
                meetings2.class,
                R.layout.meeting2_row,
                masterallreports.meetsViewHolder.class,
                mD2.child(yearT).child(month).orderByChild("groupName")
        )
        {
            @Override
            protected void populateViewHolder(final masterallreports.meetsViewHolder viewHolder, final meetings2 model, int position) {
                final String b_key=model.getGroupid();

                viewHolder.setGroupName(model.getGroupName());
                viewHolder.setGroupdate(model.getDate());
                viewHolder.setMemberPhoto(getApplicationContext(),model.getGroupImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(masterallreports.this,report2.class);
                        //next.putExtra("key",b_key);
                        Bundle extras = new Bundle();
                        extras.putString("key", b_key);
                        extras.putString("date", model.getDate());
                        next.putExtras(extras);
                        startActivity(next);
                        finish();
                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class meetsViewHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;



        public meetsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (CardView) itemView.findViewById(R.id.meetingcarrd2);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.meetcardname);
            membername.setText(name);
        }
        public void setGroupdate(String name){
            TextView membername = (TextView)mView.findViewById(R.id.meetcardname2);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.meetgrpphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }
}
