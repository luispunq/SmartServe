package com.example.london.smartserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Menu4 extends Fragment {
    private RecyclerView masterloanslist;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private loanappsadapter adapter;
    private View view;
    private CardView c1,c2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_menu_4, container, false);
        c1=view.findViewById(R.id.loreportcard);
        c2=view.findViewById(R.id.defalterscard);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        masterloanslist=view.findViewById(R.id.masloanapplist);
        masterloanslist.setHasFixedSize(true);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("loanrequests");
        adapter=new loanappsadapter(masterloanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, getContext());
        masterloanslist.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        masterloanslist.setAdapter(adapter);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyre=new Intent(getContext(),loansf.class);
                startActivity(dailyre);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyre=new Intent(getContext(),formssf.class);
                startActivity(dailyre);
            }
        });

        return view;


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Approvals");
    }
}