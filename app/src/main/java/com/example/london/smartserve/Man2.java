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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class Man2 extends Fragment {
    private RecyclerView mastergrouplist;
    private DatabaseReference mDatabase;
    private EditText mSearch;
    private Query firebaseSearchQuery;
    private LinearLayoutManager linearLayoutManager;
    private upmeetingsadapter mmastergrouplistadapter;
    private View view;
    private CalendarView mCalenderView;
    private String dateFormated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_man_2, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mastergrouplist=view.findViewById(R.id.todaygroupslist);

        mastergrouplist.setHasFixedSize(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule");


        mCalenderView=(CalendarView) view.findViewById(R.id.calendarView2);
        mCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar fdate=new GregorianCalendar(year,month,dayOfMonth);
                SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM d, ''yy");
                fmt.setCalendar(fdate);
                dateFormated=fmt.format(fdate.getTime());

                mmastergrouplistadapter=new upmeetingsadapter(upcomingmeeting.class, R.layout.upcomingmeeting_row, upcomingMeetingsViewHolder.class, mDatabase.child(dateFormated), getContext(),dateFormated);
                mastergrouplist.setLayoutManager(linearLayoutManager);
                mastergrouplist.setAdapter(mmastergrouplistadapter);
            }
        });



        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Calendar");
    }
}
