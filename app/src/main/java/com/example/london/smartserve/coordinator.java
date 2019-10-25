package com.example.london.smartserve;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class coordinator extends AppCompatActivity {
    private CardView card1,card2,card3;
    private TextView textView,cashgvn,grpass;
    private ImageView imageView;
    private Toolbar mBar;
    private FirebaseAuth mAuth;
    private String user=null,fieldid;
    private DatabaseReference mDatabase,mD,mD1,mD2,offline,mD5;
    private RecyclerView recyclerView;
    private ProgressDialog mProgress;
    private ProcessBuilder mpr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        mBar=findViewById(R.id.bar1);
        setSupportActionBar(mBar);
        getSupportActionBar().setTitle("Coordinator");
        mAuth=FirebaseAuth.getInstance();
        cashgvn=findViewById(R.id.fieldamountgiven);
        user=getIntent().getExtras().getString("user");
        textView=findViewById(R.id.cordname);
        //textView.addTextChangedListener(filterTextWatcher);
        grpass=findViewById(R.id.fieldgrpgiven);
        grpass=findViewById(R.id.fieldgrpgiven);
        imageView=findViewById(R.id.cordprofpic);

        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts").child(user);
        mD5.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        mD1= FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mD2= FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user);
        mD=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        offline=FirebaseDatabase.getInstance().getReference();
        card1=findViewById(R.id.chosegroup);
        card2=findViewById(R.id.managegroups);
        card3=findViewById(R.id.fieldcard);
        recyclerView=findViewById(R.id.grpsgiven);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgress=new ProgressDialog(this);
        mpr=new ProcessBuilder();


        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("fieldid").exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    fieldid = dataSnapshot.child("fieldid").getValue().toString();
                    textView.setText(name);
                    Picasso.with(getApplicationContext()).load(image).into(imageView);
                    mD.child(fieldid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("amountgiven").exists()) {
                                cashgvn.setText("Kshs. "+dataSnapshot.child("amountgiven").getValue().toString());
                            } else {
                                cashgvn.setVisibility(View.GONE);
                                grpass.setVisibility(View.GONE);
                            }

                            mD.child(fieldid).child("groupsgiven").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mProgress.setTitle("Preparing for offline use");
                                    mProgress.setIcon(R.drawable.ic_sync_black_24dp);
                                    mProgress.show();
                                    mProgress.setCanceledOnTouchOutside(true);
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        String st1=snapshot.getKey();
                                        offline.child("details").child(st1).keepSynced(true);
                                        offline.child("finances").child(st1).keepSynced(true);
                                        offline.child("Mpesa").child("Received").keepSynced(true);
                                        offline.child("Receipts").child(st1).keepSynced(true);
                                        offline.child("reports").child(st1).keepSynced(true);
                                        offline.child("masterfinance").keepSynced(true);
                                        offline.child("fees").child(st1).keepSynced(true);
                                        offline.child("loanapprovals").keepSynced(true);
                                        mD5.keepSynced(true);
                                        mD.child(fieldid).keepSynced(true);
                                        offline.child("members").child("allmembers").child(st1).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                                    if (snapshot1.child("uid").exists()){
                                                    String st2=snapshot1.child("uid").getValue().toString();
                                                    offline.child("members").child(st2).keepSynced(true);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mProgress.dismiss();
                                    }
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(coordinator.this);
                                    builders.setTitle("Mode Selection")
                                            .setCancelable(true)
                                            .setMessage("Would you like to continue online?")
                                            .setPositiveButton("Yes, Go online", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseDatabase.getInstance().goOnline();

                                                }
                                            })
                                            .setNegativeButton("No, Continue offline", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseDatabase.getInstance().goOffline();
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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseRecyclerAdapter<groupsgiven,GroupgivenViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupgivenViewholder>(
                            groupsgiven.class,
                            R.layout.groupgivenlist_row,
                            GroupgivenViewholder.class,
                            mD.child(fieldid).child("groupsgiven")
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
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    int siz=adapter.getItemCount();
                    if (siz==0){

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mD5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("Amountin").exists()){
                            Intent next =new Intent(coordinator.this,grouplist.class);
                            next.putExtra("user",user);
                            startActivity(next);
                        }else {
                            Toast.makeText(coordinator.this,"Please make sure you have been handed over by the secretary",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(coordinator.this,groupslist.class);
                next.putExtra("user",user);
                startActivity(next);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next =new Intent(coordinator.this,setupgroup.class);
                startActivity(next);
            }
        });
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent loggetout=new Intent(coordinator.this,Main.class);
            startActivity(loggetout);
            finish();
        }
        if(item.getItemId()==R.id.action_profile){
            Intent loggetout=new Intent(coordinator.this,editemployee.class);
            loggetout.putExtra("key",mAuth.getCurrentUser().getUid());
            startActivity(loggetout);
        }
        if(item.getItemId()==R.id.action_cashaccount){
            Intent loggetot=new Intent(coordinator.this,empcash.class);
            loggetot.putExtra("key",mAuth.getCurrentUser().getUid());
            startActivity(loggetot);
        }
        if(item.getItemId()==R.id.action_hep){
            Intent loggetot=new Intent(coordinator.this,support.class);
            //loggetot.putExtra("key",mAuth.getCurrentUser().getUid());
            startActivity(loggetot);
        }
        return true;
    }
}
