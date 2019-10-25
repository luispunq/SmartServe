package com.example.london.smartserve;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class jounalentries extends AppCompatActivity {
    private TextView mAccname,mBalfigdr,mBalfigcr,mTotalDr,mTotalCr,mBalDr,mBalCr;
    private RecyclerView mList;
    private DatabaseReference mD1,mD2,mD3;
    private String option,month,year,modyea;
    private FloatingActionButton fab,fab2;
    private long l1,l2,l3,l4;
    private double r1=0,r2=0;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jounalentries);

        mAccname=findViewById(R.id.accountname);
        mBalfigdr=findViewById(R.id.balancingpartdr);
        mBalfigcr=findViewById(R.id.balancingpartcr);
        mTotalDr=findViewById(R.id.totalsdr);
        mTotalCr=findViewById(R.id.totalscr);
        mBalDr=findViewById(R.id.balancedowndr);
        mBalCr=findViewById(R.id.balancedowncr);
        fab=findViewById(R.id.accselectFaB);
        fab2=findViewById(R.id.accselectFaB2);

        mList=findViewById(R.id.entrieslist);
        mList.setHasFixedSize(true);
        mList.setItemViewCacheSize(50);
        mList.setDrawingCacheEnabled(true);

        mList.setLayoutManager(new LinearLayoutManager(this));

        mAccname.setText("GENERAL JOUNAL");

        mD1= FirebaseDatabase.getInstance().getReference().child("Accounting");

        mD1.child("Suspense").child("Trans").child("all").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    r1=0;
                    r2=0;
                    mBalfigcr.setText("");
                    mBalfigdr.setText("");
                    mTotalCr.setText("");
                    mTotalDr.setText("");
                    mBalCr.setText("");
                    mBalDr.setText("");
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        if (snapshot.child("debitac").getValue().toString().equals("Suspense")){
                            r1=r1+Double.parseDouble(snapshot.child("amount").getValue().toString());

                            mTotalDr.setText("Kshs. "+df.format(r1));
                        }

                        if (!snapshot.child("debitac").getValue().toString().equals("Suspense")){
                            r2=r2+Double.parseDouble(snapshot.child("amount").getValue().toString());

                            mTotalCr.setText("Kshs. "+df.format(r2));
                        }
                    }
                    if (r1>r2){
                        double vb=r1-r2;
                        //l4=(long)vb;
                        mBalfigcr.setText("Kshs. "+df.format(vb));
                        mBalDr.setText("Kshs. "+df.format(vb));
                        mTotalCr.setText("Kshs. "+df.format(r1));
                    }else {
                        double vb=r2-r1;
                        //l4=(long)vb;
                        mBalfigdr.setText("Kshs. "+df.format(vb));
                        mBalCr.setText("Kshs. "+df.format(vb));
                        mTotalDr.setText("Kshs. "+df.format(r2));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<account,accountholder> firebaseRecyclerAdapterlsf = new FirebaseRecyclerAdapter<account, accountholder>(

                account.class,
                R.layout.jounalentries_row,
                accountholder.class,
                mD1.child("Suspense").child("Trans").child("all").limitToLast(50)
        ) {
            @Override
            protected void populateViewHolder(final accountholder viewHolder, final account model, final int position) {

                viewHolder.setDate(model.getTimestamp());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setDescDr(model.getDebitac());
                viewHolder.setDescCr(model.getCreditac());
                if (model.getDebitac().equals("Suspense")){
                    viewHolder.setDebit(model.getAmount());
                    viewHolder.setCredit(model.getAmount());
                }else {
                    viewHolder.setDebit(model.getAmount());
                    viewHolder.setCredit(model.getAmount());
                }


                if (Double.parseDouble(model.getAmount())<1.0){
                    viewHolder.Layout_hide();
                }

            }
        };

        mList.setAdapter(firebaseRecyclerAdapterlsf);

    }

    public static class accountholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        //private TextView link;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        DecimalFormat df = new DecimalFormat("##,###,###.#");


        public accountholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.jentry);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //link=mView.findViewById(R.id.btrname);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(long date){
            TextView loandate = (TextView)mView.findViewById(R.id.entrydate);
            loandate.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(date));
        }
        public void setDesc(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydetail);
            amountgiven.setText(amount);
        }

        public void setDescDr(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydetaildr);
            amountgiven.setText(amount);
        }

        public void setDescCr(String amount){
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydetailcr);
            amountgiven.setText(amount);
        }

        public void setDebit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrydr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setCredit(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.entrycr);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
    }

    private String years(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        return sdfs.format(date);
    }
    private String months(long date) {
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        return sdfs.format(date);
    }
    private String formateddate(long date){
        SimpleDateFormat sdfs = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        return sdfs.format(date);
    }
    private String modyear(String date){
        switch (date) {
            case "2018": {
                modyea = "'18";
                //return modyea;
                break;
            }
            case "2019": {
                modyea = "'19";
                //return modyea;
                break;
            }
            case "2020": {
                modyea = "'20";
                //return modyea;
                break;
            }
        }
        return modyea;
    }
}
