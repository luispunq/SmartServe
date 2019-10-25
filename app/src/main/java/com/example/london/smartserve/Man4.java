package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Man4 extends Fragment {
    private TextView mAntot,mMotot,mDaytot,mBills;
    private CardView daily,monthly,annual,expense;
    private RecyclerView mDay,mMonth,mYear;
    private View view;
    private expenseitemsadapter mAdapter;
    private DatabaseReference mDatabase,mD1,mD2,mD;
    private LinearLayoutManager linearLayoutManager,linearLayoutManagr,linearLayoutManage,inearLayoutManager;
    private FloatingActionButton b1,b2,b3;
    private HorizontalScrollView an,mo,da;
    private double v1=0,v2=0,v3=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_man_4, container, false);
        mDay=view.findViewById(R.id.dailyitemsin);
        mMonth=view.findViewById(R.id.monthlyitemsin);
        mYear=view.findViewById(R.id.annualitemsin);
        mAntot=view.findViewById(R.id.budgettot);
        mMotot=view.findViewById(R.id.monbudgettot);
        mDaytot=view.findViewById(R.id.daiybudgettot);
        b1=view.findViewById(R.id.expandannual);
        b2=view.findViewById(R.id.expandmonthly);
        b3=view.findViewById(R.id.expanddaily);
        an=view.findViewById(R.id.annualitems);
        mo=view.findViewById(R.id.monthitems);
        da=view.findViewById(R.id.dailyitems);
        daily=view.findViewById(R.id.dailycard);
        monthly=view.findViewById(R.id.monthlycard);
        annual=view.findViewById(R.id.annualcard);
        expense=view.findViewById(R.id.expensecard);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManagr = new LinearLayoutManager(getActivity());
        linearLayoutManage = new LinearLayoutManager(getActivity());
        inearLayoutManager = new LinearLayoutManager(getActivity());

        mD= FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily");
        mD1= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly");
        mD2= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("annual");

        mAdapter=new expenseitemsadapter(expenseitemadded.class, R.layout.expenseitemadded_row, expenseitemsholder.class, mDatabase, getContext());
        mDay.setHasFixedSize(true);
        mDay.setLayoutManager(inearLayoutManager);
        mDay.setAdapter(mAdapter);

        mAdapter=new expenseitemsadapter(expenseitemadded.class, R.layout.expenseitemadded_row, expenseitemsholder.class, mD1, getContext());
        mMonth.setHasFixedSize(true);
        mMonth.setLayoutManager(linearLayoutManage);
        mMonth.setAdapter(mAdapter);

        mAdapter=new expenseitemsadapter(expenseitemadded.class, R.layout.expenseitemadded_row, expenseitemsholder.class, mD2, getContext());
        mYear.setHasFixedSize(true);
        mYear.setLayoutManager(linearLayoutManagr);
        mYear.setAdapter(mAdapter);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(getContext(),dailybudgetting.class);
                startActivity(loans);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(getContext(),monthlybudgetting.class);
                startActivity(loans);
            }
        });

        annual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(getContext(),annualbudgetting.class);
                startActivity(loans);
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(getContext(),expenseitems.class);
                startActivity(loans);
            }
        });

        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child("items").child("daily").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v1=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("amount").exists()){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            v1=v1+iamt;
                        }
                    }
                }

                mDaytot.setText("Kshs. "+String.valueOf(v1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("items").child("monthly").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v2=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("amount").exists()){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            v2=v2+iamt;
                        }
                    }
                }

                mMotot.setText("Kshs. "+String.valueOf(v2));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD.child("Expenses").child(new SimpleDateFormat("yyyy").format(new Date())).child("items").child("annual").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v3=0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("amount").exists()){
                            double iamt=Double.parseDouble(snapshot.child("amount").getValue().toString());
                            v3=v3+iamt;
                        }
                    }
                }

                mAntot.setText("Kshs. "+String.valueOf(v3));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (an.getVisibility()==View.VISIBLE){
                    an.setVisibility(View.GONE);
                    b1.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }else {
                    an.setVisibility(View.VISIBLE);
                }
            }
        });

        if (an.getVisibility()==View.VISIBLE){
            b1.setBackground(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
        }else {
            b1.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mo.getVisibility()==View.VISIBLE){
                    mo.setVisibility(View.GONE);
                    b2.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }else {
                    mo.setVisibility(View.VISIBLE);
                    //b2.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }
            }
        });

        if (mo.getVisibility()==View.VISIBLE){
            b2.setBackground(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
        }else {
            b2.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        }


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (da.getVisibility()==View.VISIBLE){
                    da.setVisibility(View.GONE);
                    b3.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }else {
                    da.setVisibility(View.VISIBLE);
                    //b3.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                }
            }
        });

        if (da.getVisibility()==View.VISIBLE){
            b3.setBackground(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
        }else {
            b3.setBackground(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        }


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Budgeting & Expenses");
    }
}