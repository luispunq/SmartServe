package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class inbox extends AppCompatActivity {
    private DatabaseReference mDatabasehelp;
    private RecyclerView mInboxlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mInboxlist=findViewById(R.id.inboxlist);
        mInboxlist.setHasFixedSize(true);
        mInboxlist.setLayoutManager(layoutManager);

        mDatabasehelp= FirebaseDatabase.getInstance().getReference().child("edits");


        FirebaseRecyclerAdapter<inboxitem,inboxViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<inboxitem, inboxViewHolder>(
                inboxitem.class,
                R.layout.inbox_row,
                inboxViewHolder.class,
                mDatabasehelp
        )
        {
            @Override
            protected void populateViewHolder(inboxViewHolder viewHolder, final inboxitem model, int position) {
                final String u_key=getRef(position).getKey();

                if (model.getStatus().equals("pending")){
                    viewHolder.setPhoto(getApplicationContext(),model.getUser());
                    viewHolder.setmessage(model.getMessage());
                    viewHolder.setrdate(model.getDate());
                    viewHolder.setqName(model.getUser());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builders = new AlertDialog.Builder(inbox.this);
                            builders.setTitle("Message")
                                    .setMessage(model.getMessage())
                                    .setCancelable(true)
                                    .setPositiveButton("Confirm read", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseReference reference=mDatabasehelp.child(u_key);
                                            reference.child("status").setValue("read");

                                            Toast.makeText(inbox.this,"Marked Read",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                            Intent loggetot=new Intent(inbox.this,memberpage.class);
                                            loggetot.putExtra("key",u_key);
                                            startActivity(loggetot);
                                        }
                                    })
                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert121 = builders.create();
                            alert121.show();
                        }
                    });

                }else {
                    viewHolder.Layout_hide();
                }



            }
        };
        mInboxlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class inboxViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        private final DatabaseReference mDatabase;

        View mView;

        public inboxViewHolder(View itemView) {
            super(itemView);
            mDatabase=FirebaseDatabase.getInstance().getReference().child("Employees");
            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.inbox_item);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }
        public void setqName(String uid){
            mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name= (String) dataSnapshot.child("name").getValue();
                    TextView mrname = (TextView)mView.findViewById(R.id.inboxsendername);
                    mrname.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setrdate(String date){
            TextView mDateapp = (TextView)mView.findViewById(R.id.inboxdatesent);
            mDateapp.setText(date);
        }
        public void setmessage(String message){
            TextView mAmountapp = (TextView)mView.findViewById(R.id.inboxre);
            mAmountapp.setText(message);
        }
        public void setPhoto(final Context ctx, String uid) {
            mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image= (String) dataSnapshot.child("image").getValue();
                    ImageView serphoto=(ImageView)mView.findViewById(R.id.inboxprofimage);
                    Picasso.with(ctx).load(image).into(serphoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
