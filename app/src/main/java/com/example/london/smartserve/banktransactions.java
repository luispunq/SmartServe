package com.example.london.smartserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Date;

public class banktransactions extends AppCompatActivity {
    private DatabaseReference mDatabasemembers,mD1,mD2;
    private RecyclerView mloanhist;
    private String type,date,datem,datey,yearT,yearx,monthx,dayx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransactions);
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        date = extras.getString("date");





        if (type.equals("daily")){
            String[] dateparts = date.split("-");
            yearx = dateparts[0];
            monthx = dateparts[1];
            dayx = dateparts[2];
        }else {
            String string = date;
            String[] parts = string.split("-");
            datem = parts[0];
            datey = parts[1];

            switch (datey) {
                case "18":
                    yearT = "2018";
                    break;
                case "19":
                    yearT = "2019";
                    break;
                case "20":
                    yearT = "2020";
                    break;
            }
        }



        mloanhist=findViewById(R.id.memberstat_list);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));

        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("Accounting").child("BankCash").child("Trans").child("all");


        switch (type) {
            case "daily": {
                mD2=FirebaseDatabase.getInstance().getReference().child("Accounting").child("BankCash").child("Trans").child(yearx).child(monthx).child(dayx);

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.banktransactions_row,
                        accountholder.class,
                        mD2
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                        if (model.getAmount().equals("0.0")) {
                            viewHolder.Layout_hide();
                        }else {

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

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent backtosignin =new Intent(banktransactions.this,banktransactionsspecific.class);
                                Bundle extras = new Bundle();
                                extras.putString("gkey", model.getGroup());
                                extras.putString("meet", model.getMeet());
                                extras.putString("date", String.valueOf(model.getTimestamp()));
                                backtosignin.putExtras(extras);
                                startActivity(backtosignin);
                            }
                        });
                        }

                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);
                break;
            }
            case "month": {
                mD1=FirebaseDatabase.getInstance().getReference().child("Accounting").child("BankCash").child("Trans").child(yearT).child(datem).child("all");

                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.banktransactions_row,
                        accountholder.class,
                        mD1
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {
                        if (model.getAmount().equals("0.0")) {
                            viewHolder.Layout_hide();
                        }else {

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

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent backtosignin =new Intent(banktransactions.this,banktransactionsspecific.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("gkey", model.getGroup());
                                    extras.putString("meet", model.getMeet());
                                    extras.putString("date", String.valueOf(model.getTimestamp()));
                                    backtosignin.putExtras(extras);
                                    startActivity(backtosignin);
                                }
                            });
                        }

                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);
                break;
            }
            default: {
                FirebaseRecyclerAdapter<account, accountholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<account, accountholder>(

                        account.class,
                        R.layout.banktransactions_row,
                        accountholder.class,
                        mDatabasemembers
                ) {
                    @Override
                    protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {
                        if (model.getType().equals("red")) {
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.absent));
                        } else {
                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.present));
                        }

                        if (model.getAmount().equals("0")) {
                            viewHolder.Layout_hide();
                        }

                        viewHolder.setName(model.getName());
                        viewHolder.setDesc(model.getDescription());
                        viewHolder.setAmount(model.getAmount());
                        viewHolder.setDebit(model.getDebitac());
                        viewHolder.setCredit(model.getCreditac());

                    }
                };

                mloanhist.setAdapter(firebaseRecyclerAdapter);
                break;
            }
        }




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
            double advpy = Math.round(Double.parseDouble(amount)*100)/100;

            TextView amountgiven = (TextView)mView.findViewById(R.id.btramt);
            amountgiven.setText(String.valueOf(advpy));
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
