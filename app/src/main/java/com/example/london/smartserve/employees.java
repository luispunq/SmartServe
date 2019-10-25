package com.example.london.smartserve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class employees extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private employeelistadapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        recyclerView=findViewById(R.id.emlist);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabase.keepSynced(true);
        adapter=new employeelistadapter2(employee.class, R.layout.masteremployees_row, employeesholder.class, mDatabase, employees.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
