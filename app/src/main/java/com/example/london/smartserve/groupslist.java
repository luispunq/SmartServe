package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class groupslist extends AppCompatActivity {
    private RecyclerView mGroups;
    private DatabaseReference mDatabase,mData;
    private String user;
    private int count=0;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupslist);
        mGroups =findViewById(R.id.groupslist);
        user=getIntent().getExtras().getString("user");
        mGroups.setHasFixedSize(true);
        mGroups.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        mProgress=new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<groups,GroupViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groups, GroupViewholder>(

                groups.class,
                R.layout.groupslist_row,
                GroupViewholder.class,
                mDatabase.orderByChild("name")
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final groups model, int position) {
                final String b_key=getRef(position).getKey();
                viewHolder.setGroupName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent next = new Intent(groupslist.this,groupstats.class);
                        next.putExtra("key",b_key);
                        startActivity(next);
                        finish();*/
                        Intent next = new Intent(groupslist.this,coordinatoractivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key", b_key);
                        extras.putString("user", user);
                        next.putExtras(extras);
                        startActivity(next);
                        finish();
                    }
                });
            }
        };


        mGroups.setAdapter(firebaseRecyclerAdapter);
    }

    public static class GroupViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public GroupViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.groupslistcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.textView9);
            mGroupname.setText(groupname);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent loggetout=new Intent(groupslist.this,Main.class);
            startActivity(loggetout);
            finish();
        }

        return true;
    }

}
