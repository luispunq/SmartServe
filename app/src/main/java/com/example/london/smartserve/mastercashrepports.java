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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mastercashrepports extends AppCompatActivity {
    private TextView mCashfrmOffice,mCashcoll,mCashgiven,mCashback,mNumberofmeets;
    private DatabaseReference mDatabase,mD1;
    private double totalcl=0,totalcp=0,totalcb=0,totalcf=0;
    private RecyclerView recyclerView;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastercashrepports);
        date=getIntent().getExtras().getString("date");
        mCashfrmOffice=findViewById(R.id.cashfromoffice);
        mCashcoll=findViewById(R.id.cashcollected);
        mCashgiven=findViewById(R.id.cashgiven);
        mCashback=findViewById(R.id.cashback);
        mNumberofmeets=findViewById(R.id.meetings);
        recyclerView=findViewById(R.id.daymeets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("meetings").child("all").child(date);
        mD1= FirebaseDatabase.getInstance().getReference().child("finances").child("cashdetails").child(date);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                mD1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String c=String.valueOf(dataSnapshot.getChildrenCount());
                        mNumberofmeets.setText(c+" Meetings");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<meetings2,meetsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<meetings2, meetsViewHolder>(

                meetings2.class,
                R.layout.meeting_row,
                meetsViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(meetsViewHolder viewHolder, final meetings2 model, int position) {
                final String b_key=model.getGroupid();

                viewHolder.setGroupName(model.getGroupName());
                viewHolder.setMemberPhoto(getApplicationContext(),model.getGroupImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next = new Intent(mastercashrepports.this,report2.class);
                        Bundle extrasd = new Bundle();
                        extrasd.putString("key", b_key);
                        extrasd.putString("date", date);
                        next.putExtras(extrasd);
                        startActivity(next);
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
            layout = (CardView) itemView.findViewById(R.id.meetingcarrd);
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
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.meetgrpphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }
}
