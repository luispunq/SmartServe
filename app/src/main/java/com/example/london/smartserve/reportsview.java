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

public class reportsview extends AppCompatActivity {
    private RecyclerView mGroup;
    private String grpkey=null;
    private DatabaseReference mD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportsview);
        grpkey=getIntent().getExtras().getString("key");
        mGroup=findViewById(R.id.grpmeetings);
        mGroup.setHasFixedSize(true);
        mGroup.setLayoutManager(new LinearLayoutManager(this));
        mD= FirebaseDatabase.getInstance().getReference().child("reports").child(grpkey).child("finalreport");

        FirebaseRecyclerAdapter<reports,ReportsViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<reports,ReportsViewholder>(

                reports.class,
                R.layout.grouplist_row,
                ReportsViewholder.class,
                mD
        )
        {
            @Override
            protected void populateViewHolder(final ReportsViewholder viewHolder, final reports model, int position) {
                final String b_key=grpkey;
                final String ts=model.getMeetdate();

                viewHolder.setGroupName(model.getMeetdate());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nexts = new Intent(reportsview.this,report2.class);
                        Bundle extras = new Bundle();
                        extras.putString("key", b_key);
                        extras.putString("date", ts);
                        nexts.putExtras(extras);
                        startActivity(nexts);
                        finish();
                    }
                });
            }
        };

        mGroup.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    public static class ReportsViewholder extends RecyclerView.ViewHolder{
        View mView;
        private RelativeLayout layout;
        private RelativeLayout.LayoutParams layoutParams;

        public ReportsViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = itemView.findViewById(R.id.grouplistcard);
            layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textView9);
            mGroupname.setText(groupname);
        }
    }
}
