package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;

public class emppopup extends AppCompatActivity {
    private String userkey=null;
    private ImageView mPopImage;
    private TextView mPopname,mPopcah,mpOp;
    private Button mPopedituser,mPopremoveuser;
    private DatabaseReference mDatabaseUsers,mD,mD5;
    private String user_name=null,cash=null,fid;
    private ProgressDialog mDialog;
    private RecyclerView recyclerView;
    private View mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emppopup);
        mDialog=new ProgressDialog(this);
        userkey =getIntent().getExtras().getString("User_Key");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mview=findViewById(R.id.popup);
        recyclerView=findViewById(R.id.grpgvn);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        int width=dm.widthPixels;
        int height=dm.heightPixels;


        getWindow().setLayout((int)(width),(int)(height*0.7));
        mPopImage=(ImageView)findViewById(R.id.imageView9);
        mPopname=(TextView)findViewById(R.id.popupname);
        mpOp=findViewById(R.id.popupengaged);
        mPopcah=(TextView)findViewById(R.id.popupcash);
        mPopedituser=(Button)findViewById(R.id.modifyuser);
        mPopremoveuser=(Button)findViewById(R.id.removeuser);
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(userkey);
        mD5.keepSynced(true);
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Employees").child(userkey);
        mD=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(userkey);

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("fieldid")){
                    fid=dataSnapshot.child("fieldid").getValue().toString();

                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupgivenViewholder>(
                            groupsgiven.class,
                            R.layout.groupgivenlist_row,
                            GroupgivenViewholder.class,
                            mD.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fid).child("groupsgiven")
                    )
                    {
                        @Override
                        protected void populateViewHolder(final GroupgivenViewholder viewHolder, final groupsgiven model, int position) {
                            if (model.getStatus().equals("pending")){
                                viewHolder.setGroupName(model.getName());
                                viewHolder.setGroupAmount(model.getAmount());
                            }else {
                                viewHolder.Layout_hide();
                            }

                        }

                    };
                    recyclerView.setAdapter(firebaseRecyclerAdapter);

                }else {
                    DatabaseReference newfield=mD.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).push();
                    DatabaseReference nf=mDatabaseUsers.child("fieldid");
                    nf.setValue(newfield.getKey());
                    fid=newfield.getKey();
                    Intent usermodify = new Intent(emppopup.this,masterhome.class);
                    startActivity(usermodify);
                    finish();
                    Toast.makeText(emppopup.this,"Employee Set Up, proceed to assignment.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPopremoveuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usermodify = new Intent(emppopup.this,employeebasic.class);
                usermodify.putExtra("User_Key",userkey);
                startActivity(usermodify);
                finish();
                //Toast.makeText(emppopup.this,"Employee Set Up, proceed to assignment.",Toast.LENGTH_LONG).show();
            }
        });
        /*
        mDialog.setMessage("Removing User..");
                mDialog.show();
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDatabaseUsers.removeValue();
                        mDialog.dismiss();
                        Toast.makeText(emppopup.this,"User: "+user_name+" has been removed!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        */

        mPopedituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usermodify = new Intent(emppopup.this,fieldstart.class);
                Bundle extras = new Bundle();
                extras.putString("id", userkey);
                extras.putString("fid", fid);
                usermodify.putExtras(extras);
                startActivity(usermodify);
                finish();
            }
        });

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_name= (String) dataSnapshot.child("name").getValue();
                final String user_Image= (String) dataSnapshot.child("image").getValue();
                final String user_type=(String) dataSnapshot.child("status").getValue();

                /*mD.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(dataSnapshot.child("fieldid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("amountgiven").exists()){
                            cash=dataSnapshot.child("amountgiven").getValue().toString();
                        }else {
                            cash="0";
                        }
                        mPopname.setText(user_name);
                        mPopcah.setText("Kshs. "+cash);
                        mpOp.setText(user_type);
                        Picasso.with(emppopup.this).load(user_Image).into(mPopImage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Balance").exists()){
                            String bal=dataSnapshot.child("Balance").getValue().toString();

                            mPopname.setText(user_name);
                            mPopcah.setText("Kshs. "+bal);
                            mpOp.setText(user_type);
                            Picasso.with(emppopup.this).load(user_Image).into(mPopImage);

                        }else {
                            String bal="0";

                            mPopname.setText(user_name);
                            mPopcah.setText("Kshs. "+bal);
                            mpOp.setText(user_type);
                            Picasso.with(emppopup.this).load(user_Image).into(mPopImage);

                        }
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

    }

    public static class GroupgivenViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupgivenViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupgivencard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww);
            mGroupname.setText(groupname);
        }
        public void setGroupAmount(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textVieww2);
            mGroupname.setText("Kshs. "+groupname);
        }
    }
}
