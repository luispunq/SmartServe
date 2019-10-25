package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class expenseitems extends AppCompatActivity {
    private EditText nname;
    private Spinner ncreditaccount,mPeriod;
    private String saccount,speriod;
    private DatabaseReference mD;
    private RecyclerView mItemms;
    private Button mAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseitems);
        nname=findViewById(R.id.nnname);
        ncreditaccount=findViewById(R.id.crdaccnt);
        mPeriod=findViewById(R.id.period);
        mAdd=findViewById(R.id.addtiem);
        mItemms=findViewById(R.id.itemsin);
        mItemms.setHasFixedSize(true);
        mItemms.setLayoutManager(new LinearLayoutManager(this));
        mD= FirebaseDatabase.getInstance().getReference().child("Accounting").child("Expenses");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Accounts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ncreditaccount.setAdapter(adapter);

        ncreditaccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saccount=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapters=ArrayAdapter.createFromResource(this,R.array.periods,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriod.setAdapter(adapters);

        mPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speriod=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addexp();
            }
        });

        FirebaseRecyclerAdapter<expenseitem,expenseitemsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<expenseitem,expenseitemsViewHolder>(

                expenseitem.class,
                R.layout.expenseitems_row,
                expenseitemsViewHolder.class,
                mD.child("items")
        )
        {
            @Override
            protected void populateViewHolder(final expenseitemsViewHolder viewHolder, final expenseitem model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setCreditac(model.getCreditac());
                viewHolder.setDebitac(model.getDebitac());
                viewHolder.setPeriod(model.getPeriod());
            }
        };

        mItemms.setAdapter(firebaseRecyclerAdapter);


    }



    private void addexp() {
        String nam=nname.getText().toString().trim();
        String crdacc=saccount;

        DatabaseReference reference=mD.child("items").push();
        reference.child("name").setValue(nam);
        reference.child("debitac").setValue("Expense");
        reference.child("creditac").setValue(crdacc);
        reference.child("period").setValue(speriod);

        nname.setText("");

    }

    public static class expenseitemsViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public expenseitemsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout =  itemView.findViewById(R.id.expitems);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.expitmname);
            mGroupname.setText(groupname);
        }
        public void setDebitac(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmdebitac);
            mGrouploc.setText(venue);
        }
        public void setCreditac(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmcreditac);
            mGrouploc.setText(date);
        }
        public void setPeriod(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmperiod);
            mGrouploc.setText(amount);
        }
    }
}
