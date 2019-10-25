package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class grouplist extends AppCompatActivity {
    private RecyclerView mGroups;
    private DatabaseReference mDatabase,mData,m;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        mGroups =findViewById(R.id.groupslist);
        user=getIntent().getExtras().getString("user");
        mGroups.setHasFixedSize(true);
        mGroups.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        m=FirebaseDatabase.getInstance().getReference().child("Employees").child(user);
        mData=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(user);


        m.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fid=dataSnapshot.child("fieldid").getValue().toString();
                DatabaseReference reference=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fid).child("groupsgiven");

                FirebaseRecyclerAdapter<groupsgiven,GroupsViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupsgiven, GroupsViewholder>(

                        groupsgiven.class,
                        R.layout.grouplist_row,
                        GroupsViewholder.class,
                        reference
                )
                {
                    @Override
                    protected void populateViewHolder(final GroupsViewholder viewHolder, final groupsgiven model, int position) {
                        final String b_key=model.getGroupid();

                        viewHolder.setGroupName(model.getName());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent next = new Intent(grouplist.this,coordinatoractivity.class);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public static class GroupsViewholder extends RecyclerView.ViewHolder{
        //private final RelativeLayout layout;
        //final RelativeLayout.LayoutParams layoutParams;
        View mView;
        private RelativeLayout layout;
        private RelativeLayout.LayoutParams layoutParams;

        public GroupsViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = itemView.findViewById(R.id.grouplistcard);
            //layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              //      ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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
            Intent loggetout=new Intent(grouplist.this,Main.class);
            startActivity(loggetout);
            finish();
        }

        return true;
    }

}
