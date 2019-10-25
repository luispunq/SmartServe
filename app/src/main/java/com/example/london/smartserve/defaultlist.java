package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class defaultlist extends AppCompatActivity {
    private DatabaseReference mDatabasehelp,mD2;
    private RecyclerView mInboxlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaultlist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mInboxlist=findViewById(R.id.defaultlist);
        mInboxlist.setHasFixedSize(true);
        mInboxlist.setLayoutManager(layoutManager);
        mDatabasehelp= FirebaseDatabase.getInstance().getReference().child("defaults").child("requests");
        mD2= FirebaseDatabase.getInstance().getReference().child("members");

        FirebaseRecyclerAdapter<defaultquerry,defaultHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<defaultquerry, defaultHolder>(
                defaultquerry.class,
                R.layout.default_row,
                defaultHolder.class,
                mDatabasehelp
        )
        {
            @Override
            protected void populateViewHolder(defaultHolder viewHolder, final defaultquerry model, int position) {
                final String u_key=getRef(position).getKey();

                if (model.getStatus().equals("pending")){
                    viewHolder.setPhoto(getApplicationContext(),model.getMemberid());
                    viewHolder.setmessage(model.getParticulars());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setName(model.getMemberid());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mD2.child(model.getMemberid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name=dataSnapshot.child("details").child("name").getValue().toString();
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(defaultlist.this);
                                    builders.setTitle("Default Approval")
                                            .setMessage("Default for "+name)
                                            .setCancelable(true)
                                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent userwind = new Intent(defaultlist.this,defaultprocessing.class);
                                                    userwind.putExtra("key",u_key);
                                                    startActivity(userwind);
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert121 = builders.create();
                                    alert121.show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                }else {
                    viewHolder.Layout_hide();
                }



            }
        };
        mInboxlist.setAdapter(firebaseRecyclerAdapter);


    }

    public static class defaultHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        private final DatabaseReference mDatabase;

        View mView;

        public defaultHolder(View itemView) {
            super(itemView);
            mDatabase=FirebaseDatabase.getInstance().getReference().child("members");
            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.default_item);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }
        public void setName(String uid){
            mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name= (String) dataSnapshot.child("details").child("name").getValue();
                    TextView mrname = (TextView)mView.findViewById(R.id.defaulername);
                    mrname.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setDate(String date){
            TextView mDateapp = (TextView)mView.findViewById(R.id.inboxdatesent);
            mDateapp.setText(date);
        }
        public void setmessage(String message){
            TextView mAmountapp = (TextView)mView.findViewById(R.id.deafultparts);
            mAmountapp.setText(message);
        }
        public void setPhoto(final Context ctx, String uid) {
            mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image= (String) dataSnapshot.child("details").child("profileImage").getValue();
                    ImageView serphoto=(ImageView)mView.findViewById(R.id.defaultpic);
                    Picasso.with(ctx).load(image).into(serphoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
