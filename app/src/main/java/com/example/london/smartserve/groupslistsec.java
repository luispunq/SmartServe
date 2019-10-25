package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class groupslistsec extends AppCompatActivity {
    private RecyclerView mGroups;
    private DatabaseReference mDatabase,mD1;
    private String user;
    private int count=0;
    private ProgressDialog mProgress;
    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupslist);
        mGroups =findViewById(R.id.groupslist);
        //user=getIntent().getExtras().getString("user");
        mGroups.setHasFixedSize(true);
        mGroups.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        mD1=FirebaseDatabase.getInstance().getReference().child("members").child("allmembers");
        mProgress=new ProgressDialog(this);

        firebaseuserserach();
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

    private void firebaseuserserach() {
        //super.onStart();
        Query firebaseSearchQuery = mDatabase.orderByChild("name");

        FirebaseRecyclerAdapter<group,GroupViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<group, GroupViewholder>(
                group.class,
                R.layout.groupslist_row,
                GroupViewholder.class,
                firebaseSearchQuery
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final group model, int position) {

                final String b_key=getRef(position).getKey();
                final String bname=model.getName();


                viewHolder.setGroupName(model.getName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RecyclerView recyclerView=new RecyclerView(groupslistsec.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(groupslistsec.this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder(groupslistsec.this);
                        builder.setTitle("Select Member")
                                .setCancelable(true)
                                .setView(recyclerView);
                        recycleradapt(recyclerView,b_key,bname);
                        alert11 = builder.create();
                        alert11.show();
                    }
                });

            }
        };
        mGroups.setAdapter(firebaseRecyclerAdapter);
    }
    private void recycleradapt(RecyclerView r, final String b_key, final String bname) {
        FirebaseRecyclerAdapter<member, mpesapayment2.membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<member, mpesapayment2.membersViewHolder>(

                member.class,
                R.layout.member_row,
                mpesapayment2.membersViewHolder.class,
                mD1.child(b_key)
        )
        {
            @Override
            protected void populateViewHolder(final mpesapayment2.membersViewHolder viewHolder, final member model, int position) {
                final String u_key=model.getUid();

                viewHolder.setMemberPhoto(getApplicationContext(),model.getProfileImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bac = new Intent(groupslistsec.this,paymentsec.class);
                        bac.putExtra("key", u_key);
                        startActivity(bac);
                    }
                });


            }
        };
        r.setAdapter(firebaseRecyclerAdapter);

    }

    public static class membersViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.membercardname);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.membercardphoto);
            Picasso.with(ctx).load(image).into(memberphoto);
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
            Intent loggetout=new Intent(groupslistsec.this,Main.class);
            startActivity(loggetout);
            finish();
        }

        return true;
    }

}
