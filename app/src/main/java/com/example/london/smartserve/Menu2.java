package com.example.london.smartserve;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Menu2 extends Fragment {
    private RecyclerView mastergrouplist;
    private DatabaseReference mDatabase;
    private EditText mSearch;
    private Query firebaseSearchQuery;
    private LinearLayoutManager linearLayoutManager;
    private mastergrouplistadapter mmastergrouplistadapter;
    private View view;
    private AnyChart mChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_menu_2, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mastergrouplist=view.findViewById(R.id.mastergrouplist);
        mSearch=view.findViewById(R.id.editText2);
        mSearch.addTextChangedListener(filterTextWatcher);
        mastergrouplist.setHasFixedSize(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Groups");

        return view;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

            final String uname=mSearch.getText().toString().trim();
            firebaseSearchQuery = mDatabase.orderByChild("name").startAt(uname.toUpperCase()).endAt(uname.toUpperCase() + "\uf8ff");
            mmastergrouplistadapter=new mastergrouplistadapter(mastergroup.class, R.layout.mastergrouplist_row, mastergrouplistholder.class, firebaseSearchQuery, getContext());
            mastergrouplist.setLayoutManager(linearLayoutManager);
            mastergrouplist.setAdapter(mmastergrouplistadapter);

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Groups");
    }
}
