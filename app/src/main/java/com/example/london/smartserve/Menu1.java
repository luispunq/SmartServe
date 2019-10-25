package com.example.london.smartserve;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.ACTIVITY_SERVICE;


public class Menu1 extends Fragment {
    private RecyclerView mactivemeetings;
    private TextView mAllgroups,mBusinessgroups,mGroupInvestmentgroups,mIndividualloanacoounts
            ,mIndmemberInvAccn,mTotalsaings,mTotalLoans,mMasteradv,mInsurr;
    private DatabaseReference mDatabase,mDatabases,mD1,mD2,mD3;
    private FirebaseAuth mAuth;
    private CardView card1,card2,card3;
    private LinearLayoutManager linearLayoutManager;
    private String user;
    private meetingsadapter mAdapter;
    private int todaycount=0,todaycount2=0,todaycount3=0,todaycount4=0;
    private double c1=0,c2=0,c3=0;
    private long l1,l2,l3,l4;
    private double totalcl=0,totalcp=0,p1=0,p2=0,p3=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,v9=0,m1=0,m2=0,
            m3=0,totalcf=0,manbal,secbal,cc1,cc2,cc3,i1=0,i2=0,i3,f1=0,f2=0,f3=0;
    private AnyChart mChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_menu_1, container, false);

        mactivemeetings=view.findViewById(R.id.activemeetings);
        mAllgroups=view.findViewById(R.id.allgroups);
        mBusinessgroups=view.findViewById(R.id.type1grp);
        mGroupInvestmentgroups=view.findViewById(R.id.type2grp);
        mIndividualloanacoounts=view.findViewById(R.id.type3grp);
        mIndmemberInvAccn=view.findViewById(R.id.type4grp);
        mTotalsaings=view.findViewById(R.id.totalsavings);
        mTotalLoans=view.findViewById(R.id.totalloans);
        mMasteradv=view.findViewById(R.id.masadvn);
        mInsurr=view.findViewById(R.id.masadvninsu);
        card1=view.findViewById(R.id.masterdailyreport);
        card2=view.findViewById(R.id.masteroverall);
        card3=view.findViewById(R.id.financesummaries);
        final DecimalFormat df = new DecimalFormat("##,###,###.#");

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mactivemeetings.setHasFixedSize(true);
        mDatabases= FirebaseDatabase.getInstance().getReference().child("meetings").child("insession");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mD1= FirebaseDatabase.getInstance().getReference().child("Groups");
        mD2= FirebaseDatabase.getInstance().getReference().child("details");
        mD3= FirebaseDatabase.getInstance().getReference().child("members");


        mAdapter=new meetingsadapter(activemeetings.class, R.layout.activemeetings_row, activemeetingsholder.class, mDatabases, getContext());
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
                Intent card2 = new Intent(getContext(),monthselector.class);
                startActivity(card2);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card2 = new Intent(getContext(),monthselector2.class);
                startActivity(card2);



            }
        });

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String number= String.valueOf(dataSnapshot.getChildrenCount());
                if (Integer.parseInt(number)>1){
                    mAllgroups.setText(number+" Groups");
                }else {
                    mAllgroups.setText(number+" Group");
                }

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String id=snapshot.getKey();
                    mD2.child(id).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("type").exists()){
                                String string=dataSnapshot.child("type").getValue().toString();
                                switch (string) {
                                    case "Normal Groups":
                                        todaycount = todaycount + 1;
                                        String fpatientcount = String.valueOf(todaycount);
                                        mBusinessgroups.setText(fpatientcount + " Groups");
                                        break;
                                    case "Group Investment Accounts":
                                        todaycount2 = todaycount2 + 1;
                                        String fpatientcount1 = String.valueOf(todaycount2);
                                        mGroupInvestmentgroups.setText(fpatientcount1 + " Groups");
                                        break;
                                    case "Individual Loan Account":
                                        todaycount3 = todaycount3 + 1;
                                        String fpatientcount2 = String.valueOf(todaycount3);
                                        mIndividualloanacoounts.setText(fpatientcount2 + " Groups");
                                        break;
                                    case "Individual Member Investment":
                                        todaycount4 = todaycount4 + 1;
                                        String fpatientcount3 = String.valueOf(todaycount4);
                                        mIndmemberInvAccn.setText(fpatientcount3 + " Groups");
                                        break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.child("Longtermloan").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v8=0;
                v7=0;
                v9=0;
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                            v8=v8+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Longtermloan")){
                            v9=v9+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        v7=v8-v9;
                        l1=(long)v7;
                        mTotalLoans.setText("Kshs "+df.format(l1));
                        mTotalLoans.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mTotalLoans.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Shorttermloans").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    m1=0;
                    m2=0;
                    m3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                            m1=m1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Shorttermloans")){
                            m2=m2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        m3=m1-m2;
                        mMasteradv.setText("Kshs "+df.format(m3));
                        mMasteradv.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mMasteradv.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Membersfund").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    p1=0;
                    p2=0;
                    p3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                            p1=p1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Membersfund")){
                            p2=p2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }


                        p3=p2-p1;
                        mTotalsaings.setText("Kshs "+df.format(p3));
                        mTotalsaings.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mTotalsaings.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Riskfund").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("debitac").getValue().toString().equals("Riskfund")){
                            f1=f1+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Riskfund")){
                            f2=f2+Double.parseDouble(snapshot.child("amount").getValue().toString());
                        }

                        f3=f2-f1;
                        mInsurr.setText("Kshs "+df.format(f3));
                        mInsurr.setBackground(getResources().getDrawable(R.drawable.inputoutline));
                    }
                }else {
                    mInsurr.setText("Kshs 0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
