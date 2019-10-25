package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Man1 extends Fragment {
    private RecyclerView mactivemeetings,mSchedlist;
    private TextView mAllgroups,mGroupalloc,massign,mbuget,mTra,unread,unseen,defaultunseen;
    private DatabaseReference mDatabase,mDatabases,mD,mDatabasehelp,Database,mD2;
    private FirebaseAuth mAuth;
    private CardView card1,card2,card3,mLoanlink,mQuerrieslink;
    private String user;
    private upmeetingsadapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private double todaycount=0,todaycount2=0,todaycount3=0,todaycount4=0,todaycount5=0,d1=0;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_man_1, container, false);

        mactivemeetings=view.findViewById(R.id.managergrpslist);
        //mSchedlist=view.findViewById(R.id.managergrpslist);
        mAllgroups=view.findViewById(R.id.allgroups);
        card1=view.findViewById(R.id.masterdailyreport);
        card2=view.findViewById(R.id.masteroverall);
        card3=view.findViewById(R.id.defaultstask);

        massign=view.findViewById(R.id.gtitle2);
        mbuget=view.findViewById(R.id.bgtitle23);
        unread=view.findViewById(R.id.bgtitle4unread);
        unseen=view.findViewById(R.id.bgtitle4unseen);
        defaultunseen=view.findViewById(R.id.defnotfs);

        mAllgroups=view.findViewById(R.id.allgroups);
        mLoanlink=view.findViewById(R.id.loantask);
        mQuerrieslink=view.findViewById(R.id.querriestask);
        mGroupalloc=view.findViewById(R.id.ballgroups3);
        mTra=view.findViewById(R.id.ballgroups3t);

        mDatabasehelp= FirebaseDatabase.getInstance().getReference().child("edits");
        mD2= FirebaseDatabase.getInstance().getReference().child("defaults").child("requests");

        Database=FirebaseDatabase.getInstance().getReference().child("loanrequests");

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mactivemeetings.setHasFixedSize(true);
        mD=FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

        mDatabases= FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAdapter=new upmeetingsadapter(upcomingmeeting.class, R.layout.upcomingmeeting_row, upcomingMeetingsViewHolder.class, mDatabases, getContext(),new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));
        mactivemeetings.setLayoutManager(linearLayoutManager);
        mactivemeetings.setAdapter(mAdapter);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card = new Intent(getContext(),dateselector.class);
                startActivity(card);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card2 = new Intent(getContext(),dateselector4.class);
                startActivity(card2);
            }
        });


        mDatabase.child("meetings").child("schedule").child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAllgroups.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                if (dataSnapshot.exists()){
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mDatabase.child("details").child(snapshot.child("groupid").getValue().toString()).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.child("togivefac").exists()){
                            todaycount=todaycount+Double.parseDouble(dataSnapshot1.child("togivefac").getValue().toString());
                            if (dataSnapshot1.child("transport").exists()&&!dataSnapshot1.child("transport").getValue().toString().equals("")){
                                todaycount2=todaycount2+Double.parseDouble(dataSnapshot1.child("transport").getValue().toString());
                            }
                            }

                            todaycount3=todaycount2+todaycount;
                            mGroupalloc.setText(String.valueOf(todaycount3));
                            mTra.setText(String.valueOf(todaycount2));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasehelp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //todaycount4=0;
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        todaycount4=todaycount4+1;
                    }

                    unread.setText(String.valueOf((int)todaycount4)+" not reviewed.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d1=0;
                if (dataSnapshot.hasChildren()){
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        d1=d1+1;
                    }

                    defaultunseen.setText(String.valueOf((int)d1)+" not reviewed");
                }
                }else {
                    defaultunseen.setText("No unread");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        todaycount5=todaycount5+1;
                    }

                    unseen.setText(String.valueOf((int) todaycount5)+" loans to review");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        massign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_employeesa,view);
            }
        });

        mbuget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_budgets,view);
            }
        });

        mLoanlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_loans,view);
            }
        });

        mQuerrieslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loggetot=new Intent(getContext(),inbox.class);
                //loggetot.putExtra("key",mAuth.getCurrentUser().getUid());
                startActivity(loggetot);
            }
        });

        defaultunseen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loggetot=new Intent(getContext(),defaultlist.class);
                //loggetot.putExtra("key",mAuth.getCurrentUser().getUid());
                startActivity(loggetot);
            }
        });





        return  view;
        }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private void displaySelectedScreen(int itemId,View view) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Man1();
                break;
            case R.id.nav_calendar:
                fragment = new Man2();
                break;
            case R.id.nav_employeesa:
                fragment = new Man3();
                break;
            case R.id.nav_budgets:
                fragment = new Man4();
                break;
            case R.id.nav_loans:
                fragment = new Menu4();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }


}
