package com.example.london.smartserve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class banktransactionsspecific extends AppCompatActivity {
    private DatabaseReference mDatabasemembers,mD1,mD2;
    private RecyclerView mloanhist;
    private String type,date,datw,datem,datey,yearT,yearx,monthx,dayx,grpkey,meetid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransactions);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("gkey");
        meetid = extras.getString("meet");
        date = extras.getString("date");

        long dat=Long.parseLong(date);

        yearx=new SimpleDateFormat("yyyy").format(dat);
        monthx=new SimpleDateFormat("MMM").format(dat);
        dayx=new SimpleDateFormat("EEE").format(dat);
        datey=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);
        //date=new SimpleDateFormat("EEE, MMM d, ''yy").format(dat);




        mloanhist=findViewById(R.id.memberstat_list);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));

        mD2=FirebaseDatabase.getInstance().getReference().child("Accounting").child("BankCash").child(grpkey).child("Trans").child(yearx).child(monthx).child(datey);

        FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<account, accountholder>(

                account.class,
                R.layout.banktransactions_row,
                accountholder.class,
                mD2
        ) {
            @Override
            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                String compdate=new SimpleDateFormat("EEE, MMM d, ''yy").format(model.getTimestamp());
                if (model.getType().equals("red")) {
                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.absent));
                } else {
                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.present));
                }


                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setDebit(model.getDebitac());
                viewHolder.setCredit(model.getCreditac());

                if (model.getAmount().equals("0")) {
                    viewHolder.Layout_hide();
                }

                if (!compdate.equals(datey)){
                    viewHolder.Layout_hide();
                }

                if (!model.getMeet().equals(meetid)){
                    viewHolder.Layout_hide();
                }

            }
        };

        mloanhist.setAdapter(firebaseRecyclerAdapter);


    }

    public static class accountholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        private TextView link;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public accountholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.bnktrs);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            link=mView.findViewById(R.id.btrname);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.btrname);
            loandate.setText(date);
        }
        public void setDesc(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.btrdesc);
            amountgiven.setText(amount);
        }
        public void setAmount(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.btramt);
            amountgiven.setText(amount);
        }
        public void setDebit(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.btrdebit);
            amountgiven.setText(amount);
        }
        public void setCredit(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.btrcredit);
            amountgiven.setText(amount);
        }
    }
}
