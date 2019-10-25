package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class fieldstartmanager extends AppCompatActivity {
    private EditText mCahgiven,mMisc;
    private DatabaseReference mCash,mEmployees,mDatabase,mData,mD,mAl,reference;
    private ImageView empImage;
    private TextView empname,mtitle;
    private FirebaseAuth mAuth;
    private String user,empid=null,fkey=null,g1=null,g2=null,g3=null,g4=null,name=null;
    private Button done,start;
    private RecyclerView upcoming;
    private ProgressDialog mProgress;
    private double tt=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldstartmanager);
        Bundle extras = getIntent().getExtras();
        empid = extras.getString("id");
        fkey = extras.getString("fid");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        empname=findViewById(R.id.employeename);
        mtitle=findViewById(R.id.textView22);
        empImage=findViewById(R.id.employeeimage);
        mtitle=findViewById(R.id.textView22);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mCash= FirebaseDatabase.getInstance().getReference().child("fieldwork").child(empid);
        mEmployees=FirebaseDatabase.getInstance().getReference().child("Employees").child(empid);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule");
        mData=FirebaseDatabase.getInstance().getReference().child("fieldwork").child(empid);
        mD=FirebaseDatabase.getInstance().getReference().child("details");
        mAl=FirebaseDatabase.getInstance().getReference().child("Groups");
        reference=FirebaseDatabase.getInstance().getReference();
        mCahgiven=findViewById(R.id.moneygiven);
        mMisc=findViewById(R.id.moneygiven2);
        done=findViewById(R.id.button6);
        start=findViewById(R.id.button7);
        upcoming=findViewById(R.id.upcomingmeetings);
        upcoming.setHasFixedSize(true);
        upcoming.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        mProgress=new ProgressDialog(this);

        mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("amountgiven")){
                    mCahgiven.setVisibility(View.GONE);
                    mMisc.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                    mtitle.setText("Kshs. "+dataSnapshot.child("amountgiven").getValue().toString());
                }else{
                    mCahgiven.setVisibility(View.VISIBLE);
                    mMisc.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mEmployees.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue().toString();
                String imge=dataSnapshot.child("image").getValue().toString();
                empname.setText(name);
                Picasso.with(fieldstartmanager.this).load(imge).into(empImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //done.setVisibility(View.VISIBLE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bac = new Intent(fieldstartmanager.this,masterhome.class);
                startActivity(bac);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<upcomingmeeting,upcomingMeetingsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<upcomingmeeting, upcomingMeetingsViewHolder>(
                upcomingmeeting.class,
                R.layout.upcomingmeeting_row,
                upcomingMeetingsViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final upcomingMeetingsViewHolder viewHolder, final upcomingmeeting model, int position) {
                final String b_key=model.getGroupid();
                final String kk=getRef(position).getKey();
                final String gkey=model.getGroupid();

                if (model.getAssign().equals("unassigned")&&model.getDate().equals(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())))
                {
                    viewHolder.setGroupName(model.getGroupName());
                    viewHolder.setVenue(model.getVenue());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setAmount(model.getGroupid());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCash.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").hasChild(b_key)){
                                    final DatabaseReference ass=mDatabase.child(kk);
                                    mD.child(gkey).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("togivefac").exists()){
                                            tt=tt+Double.parseDouble(dataSnapshot.child("togivefac").getValue().toString());
                                            mCahgiven.setText(String.valueOf(tt));
                                            }
                                            else{
                                                tt=0;
                                                mCahgiven.setText(String.valueOf(tt));
                                            }
                                            final EditText misccash=new EditText(fieldstartmanager.this);
                                            final AlertDialog.Builder builders = new AlertDialog.Builder(fieldstartmanager.this);
                                            builders.setTitle("Confirm Assignment")
                                                    .setMessage("Assign "+name+" Kshs. "+String.valueOf(tt))
                                                    .setCancelable(true)
                                                    .setView(misccash)
                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            final DatabaseReference newfield=mCash.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                            mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    misccash.setText("0");
                                                                    if (dataSnapshot.child("amountgiven").exists()){
                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                        double amb4d=Double.parseDouble(amb4);

                                                                        double newamnt=Double.parseDouble(mCahgiven.getText().toString());

                                                                        double updated=newamnt+amb4d;
                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                        newfield.child("misccash").setValue(misccash.getText().toString());
                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                        DatabaseReference endmeetnot=reference.child("Notifications").child("FieldStart");
                                                                        endmeetnot.child(empid).setValue(gkey);

                                                                    }else {
                                                                        String amb4="0";
                                                                        double amb4d=Double.parseDouble(amb4);

                                                                        double newamnt=Double.parseDouble(mCahgiven.getText().toString());

                                                                        double updated=newamnt+amb4d;
                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                        newfield.child("misccash").setValue(misccash.getText().toString());
                                                                        newfield.child("date").setValue(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date()));

                                                                        DatabaseReference endmeetnot=reference.child("Notifications").child("FieldStart");
                                                                        endmeetnot.child(empid).setValue(gkey);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                            w.child("cashfromoffice").setValue(String.valueOf(tt));

                                                            DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven").child(model.getGroupid());
                                                            putfielddata.child("name").setValue(model.getGroupName());
                                                            putfielddata.child("groupid").setValue(model.getGroupid());
                                                            putfielddata.child("status").setValue("pending");
                                                            DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                                            flagss.child("gaflags").setValue("flags");

                                                            DatabaseReference updatefkey=mEmployees.child("fieldid");
                                                            updatefkey.setValue(fkey);
                                                            ass.child("assign").setValue("assigned");
                                                            viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                                                            Toast.makeText(fieldstartmanager.this,"Group Added for "+name,Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alert121 = builders.create();
                                            alert121.show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }else{
                                    DatabaseReference putfielddata=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey).child("groupsgiven");
                                    putfielddata.child(b_key).removeValue();
                                    DatabaseReference flagss=mData.child(new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())).child(fkey);
                                    flagss.child("gaflags").setValue("flag");
                                    DatabaseReference ass=mDatabase.child(kk);
                                    ass.child("assign").setValue("not assigned");
                                    viewHolder.mView.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                    Toast.makeText(fieldstartmanager.this,"Group Removed",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        upcoming.setAdapter(firebaseRecyclerAdapter);

    }

    public static class upcomingMeetingsViewHolder extends RecyclerView.ViewHolder{
        private final CardView layout;
        final CardView.LayoutParams layoutParams;
        View mView;
        private DatabaseReference m;


        public upcomingMeetingsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            m=FirebaseDatabase.getInstance().getReference().child("details");
            layout =  itemView.findViewById(R.id.upcmngcard);
            layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setGroupName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.upgrpname);
            mGroupname.setText(groupname);
        }
        public void setVenue(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpven);
            mGrouploc.setText(venue);
        }
        public void setDate(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpdate);
            mGrouploc.setText(date);
        }
        public void setAmount(String amount){
            m.child(amount).child("groupdetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("togivefac").exists()){
                    String n=dataSnapshot.child("togivefac").getValue().toString();
                    TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                    mGrouploc.setText(n);
                    }else {
                        String n="0";
                        TextView mGrouploc = (TextView)mView.findViewById(R.id.upgrpamt);
                        mGrouploc.setText(n);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
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
}
