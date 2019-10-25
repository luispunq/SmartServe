package com.example.london.smartserve;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class groupstats extends AppCompatActivity {
    private ImageView mGroupPhoto,mClick;
    private TextView mGroupname,mGroupsavings,mGrouploans,mGrouploantakers,mGroupmembernumbers,mNextdate,mVenue,mGrpadv;
    private DatabaseReference mDatabasegroup,mDatabasegroupfinance,mDatabase,mD;
    private FirebaseAuth mAuth;
    private String user,grpkey=null;

    private String ggroup=null;
    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupstats);
        mAuth=FirebaseAuth.getInstance();
        mClick=findViewById(R.id.imageView2);
        user=mAuth.getCurrentUser().getUid();
        grpkey=getIntent().getExtras().getString("key");
        mDatabasegroup= FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        mDatabasegroupfinance=FirebaseDatabase.getInstance().getReference().child("finances").child(grpkey);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("members").child(user);
        mGroupPhoto=findViewById(R.id.imageView);
        mGroupname=findViewById(R.id.groupname);

        mGrouploantakers=findViewById(R.id.loanstaken);
        mGrouploans=findViewById(R.id.grouploans);
        mGrpadv=findViewById(R.id.groupadvances);
        mGroupsavings=findViewById(R.id.grouptotalsavings);
        mGroupmembernumbers=findViewById(R.id.membernumbers);
        mNextdate=findViewById(R.id.meetdate);
        mVenue=findViewById(R.id.meetuploc);

        CardView textView=findViewById(R.id.meetings);
        CardView mms=findViewById(R.id.membs);
        CardView edits=findViewById(R.id.groupedit);
        CardView memreg=findViewById(R.id.memberdetails);

        edits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(groupstats.this,groupsetting.class);
                next.putExtra("key",grpkey);
                startActivity(next);
            }
        });

        /*mGroupmembernumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewstats = new Intent(groupstats.this,newmemberemail.class);
                Bundle extras = new Bundle();
                extras.putString("key", grpkey);
                extras.putString("user", user);
                viewstats.putExtras(extras);
                startActivity(viewstats);
            }
        });*/

        memreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtosigninf =new Intent(groupstats.this,memberstats.class);
                Bundle extrasd = new Bundle();
                extrasd.putString("key", grpkey);
                extrasd.putString("year", new SimpleDateFormat("yyyy").format(new Date()));
                extrasd.putString("month", new SimpleDateFormat("MMM").format(new Date()));
                backtosigninf.putExtras(extrasd);
                startActivity(backtosigninf);
            }
        });

        /*mGroupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(groupstats.this,editmemberfinance.class);
                Bundle extras = new Bundle();
                extras.putString("gkey", grpkey);
                extras.putString("user", user);
                next.putExtras(extras);
                startActivity(next);
            }
        });*/

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(groupstats.this,reportsview.class);
                next.putExtra("key",grpkey);
                startActivity(next);
            }
        });

        mms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(groupstats.this,memberlistg.class);
                Bundle extras = new Bundle();
                extras.putString("key", grpkey);
                extras.putString("user", user);
                next.putExtras(extras);
                startActivity(next);
            }
        });

        mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(groupstats.this,memberlisti.class);
                next.putExtra("key",grpkey);
                startActivity(next);
            }
        });

        mDatabasegroup.child("groupdetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String gimage= (String) dataSnapshot.child("profileImage").getValue();
                ggroup=(String)dataSnapshot.child("groupName").getValue();
                String gnumbers=(String)dataSnapshot.child("numbers").getValue();
                String gnextdate=(String)dataSnapshot.child("nextdate").getValue();
                String gvenue=(String)dataSnapshot.child("nextvenue").getValue();
                mGroupname.setText(ggroup);
                Picasso.with(groupstats.this).load(gimage).into(mGroupPhoto);
                mGroupmembernumbers.setText(gnumbers);
                mNextdate.setText(gnextdate);
                mVenue.setText(gvenue);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasegroupfinance.child("savings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalsavings= (String) dataSnapshot.child("totalsavings").getValue();
                mGroupsavings.setText("Kshs. "+df.format(Double.parseDouble(totalsavings)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabasegroupfinance.child("advances").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalsavings= (String) dataSnapshot.child("currentadvance").getValue();
                mGrpadv.setText("Kshs. "+df.format(Double.parseDouble(totalsavings)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabasegroupfinance.child("loans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalloans= (String) dataSnapshot.child("currentloan").getValue();
                mGrouploans.setText("Kshs. "+df.format(Double.parseDouble(totalloans)));
                mDatabasegroupfinance.child("loans").child("loanon").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long count=dataSnapshot.getChildrenCount();
                        String countvalue=String.valueOf(count);
                        mGrouploantakers.setText(countvalue);
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




}
