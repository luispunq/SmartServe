package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class upmeetingsadapter extends FirebaseRecyclerAdapter<upcomingmeeting,upcomingMeetingsViewHolder>{
    private Context context;
    private String date;
    private DatabaseReference mD1;
    private DatabaseReference mCash,mEmployees,mDatabase,mData,mD,mAl,reference,mD5,mD4;
    private AlertDialog alert11;
    private double tt=0;

    upmeetingsadapter(Class<upcomingmeeting> modelClass, int modelLayout, Class<upcomingMeetingsViewHolder> viewHolderClass, DatabaseReference ref, Context context,String date) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.date=date;
        mD1= FirebaseDatabase.getInstance().getReference().child("Employees");
        mCash= FirebaseDatabase.getInstance().getReference().child("fieldwork");
        mEmployees=FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("meetings").child("schedule");
        mData=FirebaseDatabase.getInstance().getReference().child("fieldwork");
        mD=FirebaseDatabase.getInstance().getReference().child("details");
        mAl=FirebaseDatabase.getInstance().getReference().child("Groups");
        reference=FirebaseDatabase.getInstance().getReference().child("Notifications").child("FieldStart");
        mD5= FirebaseDatabase.getInstance().getReference().child("Accounts");
        mD4=FirebaseDatabase.getInstance().getReference().child("members");
    }

    @Override
    protected void populateViewHolder(upcomingMeetingsViewHolder viewHolder, final upcomingmeeting model, int position) {

        final String gname=model.getGroupName();


        viewHolder.setGroupName(model.getGroupName());
        viewHolder.setVenue(model.getVenue());
        viewHolder.setDate(model.getDate());
        viewHolder.setAmount(model.getGroupid());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent viewstats = new Intent(context,groupinfo.class);
                viewstats.putExtra("key",key);
                context.startActivity(viewstats);*/
                String key=model.getGroupid();

                final RecyclerView recyclerView=new RecyclerView(context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Employee")
                        .setCancelable(true)
                        .setView(recyclerView);
                recycleradapt(recyclerView,key,date,gname);
                alert11 = builder.create();
                alert11.show();
            }
        });
    }

    private void recycleradapt(RecyclerView recyclerView, final String b_key, final String date, final String gname) {
        FirebaseRecyclerAdapter<employee, membersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<employee, membersViewHolder>(
                employee.class,
                R.layout.masteremployees_row,
                membersViewHolder.class,
                mD1
        )
        {
            @Override
            protected void populateViewHolder(final membersViewHolder viewHolder, final employee model, int position) {
                final String u_key=getRef(position).getKey();

                viewHolder.setMemberPhoto(context,model.getImage());
                viewHolder.setMemberName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.dismiss();

                        mD5.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Balance").exists()){
                                    if (Double.parseDouble(dataSnapshot.child("Balance").getValue().toString())<1.0){
                                        mCash.child(u_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.child(date).child(model.getFieldid()).child("groupsgiven").hasChild(b_key)){

                                                    mD.child(b_key).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child("togivefac").exists()){
                                                                tt=tt+Double.parseDouble(dataSnapshot.child("togivefac").getValue().toString());
                                                            }
                                                            else{
                                                                tt=0;
                                                            }
                                                            LinearLayout layout=new LinearLayout(context);
                                                            layout.setOrientation(LinearLayout.VERTICAL);
                                                            final EditText taskedit=new EditText(context);
                                                            taskedit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                            final EditText taskedit2=new EditText(context);
                                                            taskedit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                            taskedit.setHint("Enter Cash Assigned");
                                                            taskedit2.setHint("Enter Misc. Cash");
                                                            layout.addView(taskedit);
                                                            layout.addView(taskedit2);
                                                            final AlertDialog.Builder builders = new AlertDialog.Builder(context);
                                                            builders.setTitle("Confirm Assignment")
                                                                    .setMessage("Assign "+model.getName()+" Kshs. "+String.valueOf(tt))
                                                                    .setView(layout)
                                                                    .setCancelable(true)
                                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            final DatabaseReference newfield=mCash.child(u_key).child(date).child(model.getFieldid());
                                                                            mData.child(u_key).child(date).child(model.getFieldid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (dataSnapshot.child("amountgiven").exists()){
                                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                                        String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                                        double amb4d=Double.parseDouble(amb4);
                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());
                                                                                        double miscb4d=Double.parseDouble(miscb4);
                                                                                        double newmisc=Double.parseDouble(taskedit2.getText().toString());

                                                                                        double updated=newamnt+amb4d;
                                                                                        double updatedmisc=newmisc+miscb4d;

                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                                        newfield.child("date").setValue(date);
                                                                                        DatabaseReference endmeetnot=reference;
                                                                                        endmeetnot.child(u_key).setValue(b_key);
                                                                                    }else {
                                                                                        String amb4="0";
                                                                                        double amb4d=Double.parseDouble(amb4);

                                                                                        double newamnt=Double.parseDouble(taskedit.getText().toString());

                                                                                        double updated=newamnt+amb4d;
                                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                                        newfield.child("misccash").setValue(taskedit2.getText().toString());
                                                                                        newfield.child("date").setValue(date);

                                                                                        DatabaseReference endmeetnot=reference;
                                                                                        endmeetnot.child(u_key).setValue(b_key);
                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            DatabaseReference w=mD.child(b_key).child("groupdetails");
                                                                            w.child("cashfromoffice").setValue(taskedit.getText().toString());
                                                                            w.child("transport").setValue(taskedit2.getText().toString());

                                                                            DatabaseReference putfielddata=mData.child(u_key).child(date).child(model.getFieldid()).child("groupsgiven").child(b_key);
                                                                            putfielddata.child("name").setValue(gname);
                                                                            putfielddata.child("groupid").setValue(b_key);
                                                                            putfielddata.child("status").setValue("pending");
                                                                            putfielddata.child("meetid").setValue(date);
                                                                            putfielddata.child("amount").setValue(taskedit.getText().toString());
                                                                            DatabaseReference flagss=mData.child(u_key).child(date).child(model.getFieldid());
                                                                            flagss.child("gaflags").setValue("flags");

                                                                            runThread(date,b_key);

                                                                            DatabaseReference updatefkey=mEmployees.child(u_key).child("fieldid");
                                                                            updatefkey.setValue(model.getFieldid());
                                                                            Toast.makeText(context,"Assigned!", Toast.LENGTH_LONG).show();
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
                                                    Toast.makeText(context,"Group Added for "+model.getName(),Toast.LENGTH_LONG).show();
                                                }else{
                                                    DatabaseReference putfielddata=mData.child(u_key).child(date).child(model.getFieldid()).child("groupsgiven");
                                                    putfielddata.child(b_key).removeValue();
                                                    DatabaseReference flagss=mData.child(date).child(model.getFieldid());
                                                    flagss.child("gaflags").setValue("flag");

                                                    mD.child(b_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final double transi=Double.parseDouble(dataSnapshot.child("groupdetails").child("transport").getValue().toString());
                                                            final double amb=Double.parseDouble(dataSnapshot.child("groupdetails").child("cashfromoffice").getValue().toString());
                                                            final DatabaseReference newfield=mCash.child(u_key).child(date).child(model.getFieldid());
                                                            mData.child(u_key).child(date).child(model.getFieldid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.child("amountgiven").exists()){
                                                                        String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                        double miscb4d=Double.parseDouble(miscb4);
                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                        double amb4d=Double.parseDouble(amb4);


                                                                        double updatedmisc=miscb4d-transi;
                                                                        double updated=amb4d-amb;

                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                        newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                        newfield.child("date").setValue(date);

                                                                    }else {
                                                                        String miscb4=dataSnapshot.child("misccash").getValue().toString();
                                                                        double miscb4d=Double.parseDouble(miscb4);
                                                                        String amb4=dataSnapshot.child("amountgiven").getValue().toString();
                                                                        double amb4d=Double.parseDouble(amb4);


                                                                        double updatedmisc=miscb4d-transi;
                                                                        double updated=amb4d-amb;

                                                                        newfield.child("amountgiven").setValue(String.valueOf(updated));
                                                                        newfield.child("misccash").setValue(String.valueOf(updatedmisc));
                                                                        newfield.child("date").setValue(date);

                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    Toast.makeText(context,"Group Removed",Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }else {
                                        Toast.makeText(context,"Please make sure you handed over previous banking",Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    DatabaseReference reference=mD5.child(u_key);
                                    reference.child("Balance").setValue("0");
                                    Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class membersViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public membersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setMemberName(String name){
            TextView membername = (TextView)mView.findViewById(R.id.masempname);
            membername.setText(name);
        }
        public void setMemberPhoto(Context ctx, String image){
            ImageView memberphoto = (ImageView) mView.findViewById(R.id.masempimage);
            Picasso.with(ctx).load(image).into(memberphoto);
        }
    }

    private void runThread(final String date, final String gkey) {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                setupmemberstat(date,gkey);
            }
        });
        thread.start();
    }

    private void setupmemberstat(final String date, final String key) {
        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot3) {


                mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String uid=snapshot.child("uid").getValue().toString();
                            String nn=snapshot.child("name").getValue().toString();
                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                            if (dataSnapshot3.child(uid).child("loans").child("loanflag").exists()){
                                memstat.child("loans").child("loanflag").setValue(date);
                            }
                            if (dataSnapshot3.child(uid).child("advances").child("currpenalty").exists()){
                                memstat.child("advances").child("nextadvpaydate").setValue(date);
                            }
                            memstat.child("sftemptrs").removeValue();
                            memstat.child("temptrs").removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot3) {
                mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String uid=snapshot.child("uid").getValue().toString();
                            String nn=snapshot.child("name").getValue().toString();
                            final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                            memstat.child("name").setValue(nn);
                            if (dataSnapshot3.child(uid).child("advances").child("nextadvpaydate").exists()){
                                memstat.child("advances").child("nextadvpaydate").setValue(date);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
