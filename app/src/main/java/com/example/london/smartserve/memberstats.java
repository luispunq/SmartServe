package com.example.london.smartserve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class memberstats extends AppCompatActivity {
    private DatabaseReference mDatabasemembers,mD2,mD2x,mD,mDatabasemembersx;
    private RecyclerView mloanhist;
    private TextView advt,loant,lsft,inct,mReg,mMems;
    private double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;
    public String grpkey=null,year,month,namme,csv,insurancee;
    private FloatingActionButton fab;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    CSVWriter writer = null;
    private List<String[]> data = new ArrayList<String[]>();
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        year = extras.getString("year");
        month = extras.getString("month");
        setContentView(R.layout.activity_memberstats);
        advt=findViewById(R.id.totadv);
        loant=findViewById(R.id.totloans);
        lsft=findViewById(R.id.totsav);
        mloanhist=findViewById(R.id.memberstat_list);
        fab=findViewById(R.id.export);
        mloanhist.setHasFixedSize(true);
        mloanhist.setLayoutManager(new LinearLayoutManager(this));
        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("memberstats").child(year).child(month).child(grpkey);
        mDatabasemembersx= FirebaseDatabase.getInstance().getReference().child("memberstats").child(year).child(lastmonth()).child(grpkey);
        mD2=FirebaseDatabase.getInstance().getReference().child("finances");
        mD=FirebaseDatabase.getInstance().getReference().child("members");
        mD2x=FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);

        mD2x.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namme=dataSnapshot.child("groupdetails").child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasemembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("name").exists()){
                            mDatabasemembers.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()){
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            String adv = snapshot.child("advance").getValue().toString();
                                            String loan = snapshot.child("loans").getValue().toString();
                                            String lsf = snapshot.child("savings").getValue().toString();
                                            if (!adv.equals("")){
                                                val1 = val1 + Double.parseDouble(adv);
                                            }
                                            if (!loan.equals("")){
                                                val2 = val2 + Double.parseDouble(loan);
                                            }
                                            if (!lsf.equals("")){
                                                val3 = val3 + Double.parseDouble(lsf);
                                            }
                                        }

                                        advt.setText("Kshs. "+df.format(val1));
                                        loant.setText("Kshs. "+df.format(val2));
                                        lsft.setText("Kshs. "+df.format(val3));

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            FirebaseRecyclerAdapter<memberstat,memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<memberstat,memberstatViewHolder>(

                                    memberstat.class,
                                    R.layout.memberstats_row,
                                    memberstatViewHolder.class,
                                    mDatabasemembers.orderByChild("name")
                            )
                            {
                                @Override
                                protected void populateViewHolder(final memberstatViewHolder viewHolder, final memberstat model, final int position) {

                                    viewHolder.setName(model.getName());
                                    viewHolder.setSav(model.getSavings());
                                    viewHolder.setAdv(model.getAdvance());
                                    viewHolder.setLoa(model.getLoans());
                                    viewHolder.setInsu(getRef(position).getKey());
                                    viewHolder.setArr(getRef(position).getKey());

                                    viewHolder.link.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final AlertDialog.Builder builders = new AlertDialog.Builder(memberstats.this);
                                            builders.setTitle("Options")
                                                    .setMessage("Select Account Type")
                                                    .setCancelable(true)
                                                    .setPositiveButton("General", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent userwinde = new Intent(memberstats.this,memberpage.class);
                                                            userwinde.putExtra("key",getRef(position).getKey());
                                                            startActivity(userwinde);
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("Account", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent userwinde = new Intent(memberstats.this,individualaccount.class);
                                                            userwinde.putExtra("option",getRef(position).getKey());
                                                            startActivity(userwinde);
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alert121 = builders.create();
                                            alert121.show();
                                        }
                                    });


                                }
                            };

                            mloanhist.setAdapter(firebaseRecyclerAdapter);
                        }
                    }
                }else {
                    mDatabasemembersx.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    String adv = snapshot.child("advance").getValue().toString();
                                    String loan = snapshot.child("loans").getValue().toString();
                                    String lsf = snapshot.child("savings").getValue().toString();
                                    if (!adv.equals("")){
                                        val1 = val1 + Double.parseDouble(adv);
                                    }
                                    if (!loan.equals("")){
                                        val2 = val2 + Double.parseDouble(loan);
                                    }
                                    if (!lsf.equals("")){
                                        val3 = val3 + Double.parseDouble(lsf);
                                    }
                                }

                                advt.setText("Kshs. "+df.format(val1));
                                loant.setText("Kshs. "+df.format(val2));
                                lsft.setText("Kshs. "+df.format(val3));

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    FirebaseRecyclerAdapter<memberstat,memberstatViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<memberstat,memberstatViewHolder>(

                            memberstat.class,
                            R.layout.memberstats_row,
                            memberstatViewHolder.class,
                            mDatabasemembersx.orderByChild("name")
                    )
                    {
                        @Override
                        protected void populateViewHolder(final memberstatViewHolder viewHolder, final memberstat model, final int position) {

                            viewHolder.setName(model.getName());
                            viewHolder.setSav(model.getSavings());
                            viewHolder.setAdv(model.getAdvance());
                            viewHolder.setLoa(model.getLoans());
                            viewHolder.setInsu(getRef(position).getKey());
                            viewHolder.setArr(getRef(position).getKey());

                            viewHolder.link.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder builders = new AlertDialog.Builder(memberstats.this);
                                    builders.setTitle("Options")
                                            .setMessage("Select Account Type")
                                            .setCancelable(true)
                                            .setPositiveButton("General", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent userwinde = new Intent(memberstats.this,memberpage.class);
                                                    userwinde.putExtra("key",getRef(position).getKey());
                                                    startActivity(userwinde);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Account", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent userwinde = new Intent(memberstats.this,individualaccount.class);
                                                    userwinde.putExtra("option",getRef(position).getKey());
                                                    startActivity(userwinde);
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert121 = builders.create();
                                    alert121.show();
                                }
                            });


                        }
                    };

                    mloanhist.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(memberstats.this);
                builders.setTitle("Excel Export")
                        .setMessage("Export Current Report to Excel format.")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                csvops();
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
        });


    }

    private void csvops() {
        if (ContextCompat.checkSelfPermission(memberstats.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(memberstats.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(memberstats.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
                File f = new File(folder,"/GroupAnalysis/"+namme+"/Excelfiles/");
                File fil = new File(f,"Group Analysis as at "+new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".csv");
                csv=String.valueOf(fil);
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(memberstats.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                filldata();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(memberstats.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(memberstats.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            filldata();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
            File f = new File(folder,"/GroupAnalysis/"+namme+"/Excelfiles/");
            File fil = new File(f,"Group Analysis as at "+new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".csv");
            csv=String.valueOf(fil);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(memberstats.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            filldata();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(memberstats.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(memberstats.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();
                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        filldata();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void filldata() {

        try {
            writer = new CSVWriter(new FileWriter(csv));
            data.add(new String[]{"Member Name","Savings","Advance","Loans","Risk Fund"});
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDatabasemembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    mDatabasemembers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    mD.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).exists()&&!dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString().equals("0")){
                                                insurancee=dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    try {
                                        writer = new CSVWriter(new FileWriter(csv));
                                        data.add(new String[]{snapshot.child("name").getValue().toString(),snapshot.child("savings").getValue().toString(),snapshot.child("advance").getValue().toString(),
                                                snapshot.child("loans").getValue().toString(),insurancee});
                                        writer.writeAll(data);

                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    mDatabasemembersx.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    mD.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).exists()&&!dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString().equals("0")){
                                                insurancee=dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    try {
                                        writer = new CSVWriter(new FileWriter(csv));
                                        data.add(new String[]{snapshot.child("name").getValue().toString(),snapshot.child("savings").getValue().toString(),snapshot.child("advance").getValue().toString(),
                                                snapshot.child("loans").getValue().toString(),insurancee});
                                        writer.writeAll(data);

                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public static class memberstatViewHolder extends RecyclerView.ViewHolder{
        private TextView link;
        View mView;
        private DatabaseReference check,mD;
        private String iid;

         DecimalFormat df = new DecimalFormat("##,###,###.#");



        public memberstatViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            link=mView.findViewById(R.id.mstatname);

        }


        public void setName(String date){
            TextView loandate = (TextView)mView.findViewById(R.id.mstatname);
            loandate.setText(date);
        }
        public void setSav(String detail){
            long pamnt=Math.round(Double.parseDouble(detail));
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatsav);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setAdv(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatadv);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setLoa(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView amountgiven = (TextView)mView.findViewById(R.id.mstatloan);
            amountgiven.setText("Kshs. "+df.format(pamnt));
        }
        public void setInsu(final String amount){
            mD=FirebaseDatabase.getInstance().getReference().child("members");

            mD.child(amount).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).exists()&&!dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString().equals("0")){
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk);
                        amountgiven.setText("Paid on "+dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("date").getValue().toString()+ " Kshs."+dataSnapshot.child("insurance").child("all").child(new SimpleDateFormat("yyyy").format(new Date())).child("amount").getValue().toString());
                    }else {
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk);
                        amountgiven.setText("Not Paid.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        public void setArr(final String amount){

            //check=FirebaseDatabase.getInstance().getReference().child("members").child("allmembers").child(gkey);
            mD=FirebaseDatabase.getInstance().getReference().child("loanarreas");

            //TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk);
            //amountgiven.setText(amount);

            mD.child(amount).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk2);
                        amountgiven.setText("Kshs. "+dataSnapshot.child("arreasamount").getValue().toString());
                    }else {
                        TextView amountgiven = (TextView)mView.findViewById(R.id.mstatrisk2);
                        amountgiven.setText("Kshs. 0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private String lastyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, -7); //Adding30days

        return sdfs.format(c.getTime());
    }
    private String lastmonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.MONTH, -1); //Adding30days

        return sdfs.format(c.getTime());
    }
}
