package com.example.london.smartserve;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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

public class groupreports extends AppCompatActivity {
    private String yearT,gcount,csv,month;
    private TextView advt,loant,lsft,inct,mReg,mMems;
    private double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;
    private DatabaseReference mD,mD2,m,mD1,mDatabase,mD4;
    private long l1,l2,l3,l4;
    private double totalcl=0,totalcp=0,p1=0,p2=0,p3=0,v3=0,v4=0,v5=0,v6=0,v7=0,v8=0,v9=0,m1=0,m2=0,
            m3=0,totalcf=0,manbal,secbal,cc1,cc2,cc3,i1=0,i2=0,i3,f1=0,f2=0,f3=0;

    private FloatingActionButton fab;
    private ProgressDialog mProgress;

    CSVWriter writer = null;
    private List<String[]> data = new ArrayList<String[]>();
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupreports);
        String string = getIntent().getExtras().getString("date");
        String[] parts = string.split("-");
        month = parts[0];
        String year = parts[1];
        final RecyclerView mGroups = findViewById(R.id.groups);
        mGroups.setHasFixedSize(true);
        mGroups.setLayoutManager(new LinearLayoutManager(this));

        fab=findViewById(R.id.export);

        if (year.equals("18")){
            yearT="2018";
        }else if (year.equals("19")){
            yearT="2019";
        }
        else if (year.equals("20")){
            yearT="2020";
        }
        m=FirebaseDatabase.getInstance().getReference().child("details");
        mD1=FirebaseDatabase.getInstance().getReference().child("groupperfomancesummary");
        mD= FirebaseDatabase.getInstance().getReference().child("Groups");
        mD2=FirebaseDatabase.getInstance().getReference().child("finances");
        mD4=FirebaseDatabase.getInstance().getReference().child("members");
        final DatabaseReference mD7=FirebaseDatabase.getInstance().getReference().child("masterfinance");
        final DatabaseReference mD8=FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("groupperfomancesummary").child(yearT).child(month);
        advt=findViewById(R.id.ttotalAdvancesCarriedForward);
        loant=findViewById(R.id.ttotalLoansCarriedForward);
        lsft=findViewById(R.id.ttotalSavingsCarriedForward);
        inct=findViewById(R.id.tincome);
        mReg=findViewById(R.id.ttotalregs);
        mMems=findViewById(R.id.ttotalmembers);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Generating Report");
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.setIcon(R.drawable.ic_sync_black_24dp);



        final DecimalFormat df = new DecimalFormat("##,###,###.#");


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){

                    if (snapshot.child("income").exists()&&!snapshot.child("income").getValue().toString().equals("-")) {
                        String adv = snapshot.child("advance").getValue().toString();
                        String loan = snapshot.child("loans").getValue().toString();
                        String lsf = snapshot.child("lsf").getValue().toString();
                        String inc = snapshot.child("income").getValue().toString();
                        String reg = snapshot.child("regs").getValue().toString();
                        String mems = snapshot.child("members").getValue().toString();


                        if (!adv.equals("")){
                            val1 = val1 + Double.parseDouble(adv);
                        }else {
                            val1 = val1 + 0;
                        }
                        if (!loan.equals("")){
                            val2 = val2 + Double.parseDouble(loan);
                        }else {
                            val2 = val2 + 0;
                        }
                        if (!lsf.equals("")){
                            val3 = val3 + Double.parseDouble(lsf);
                        }else {
                            val3 = val3+ 0;
                        }
                        val4 = val4 + Double.parseDouble(inc);
                        val5 = val5 + Double.parseDouble(reg);
                        val6 = val6 + Double.parseDouble(mems);


                    }else {

                        if (!snapshot.child("advance").exists()){
                            DatabaseReference rr=mDatabase.child(snapshot.getKey());
                            rr.removeValue();
                            continue;
                        }
                        String adv = snapshot.child("advance").getValue().toString();
                        String loan = snapshot.child("loans").getValue().toString();
                        String lsf = snapshot.child("lsf").getValue().toString();
                        String reg = snapshot.child("regs").getValue().toString();
                        String mems = snapshot.child("members").getValue().toString();
                        String inc = snapshot.child("income").getValue().toString();


                        if (!adv.equals("")){
                            val1 = val1 + Double.parseDouble(adv);
                        }else {
                            val1 = val1 + 0;
                        }
                        if (!loan.equals("")){
                            val2 = val2 + Double.parseDouble(loan);
                        }else {
                            val2 = val2 + 0;
                        }
                        if (!lsf.equals("")){
                            val3 = val3 + Double.parseDouble(lsf);
                        }else {
                            val3 = val3+ 0;
                        }

                        if (!inc.equals("-")){
                            val4 = val4 + Double.parseDouble(inc);
                        }else {
                            val5 = val5 + 0;
                        }


                        if (!reg.equals("-")){
                            val5 = val5 + Double.parseDouble(reg);
                        }else {
                            val5 = val5 + 0;
                        }

                        val6 = val6 + Double.parseDouble(mems);
                    }
                }
                advt.setText("Kshs. "+df.format(val1));
                loant.setText("Kshs. "+df.format(val2));
                lsft.setText("Kshs. "+df.format(val3));
                inct.setText("Kshs. "+df.format(val4));
                mReg.setText("Kshs. "+df.format(val5));
                mMems.setText(df.format(val6)+" Members");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD8.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long count=dataSnapshot.getChildrenCount();
                gcount=String.valueOf(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(groupreports.this);
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

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog.Builder builders = new AlertDialog.Builder(groupreports.this);
                builders.setTitle("Excel Export")
                        .setMessage("Export Comprehensive Report to Excel format.")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                csvops2();
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

                return false;
            }
        });

        mGroups.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (yearT.equals(new SimpleDateFormat("yyyy").format(new Date()))&&month.equals(new SimpleDateFormat("MMM").format(new Date()))&&mGroups.getAdapter().getItemCount()<50){
                    final AlertDialog.Builder builders = new AlertDialog.Builder(groupreports.this);
                    builders.setTitle("New Month")
                            .setMessage(" Confirm list Repopulation")
                            .setCancelable(true)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //updateviews();
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
                    //alert121.show();
                }

                mGroups.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        FirebaseRecyclerAdapter<groupreport,GroupViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<groupreport, GroupViewholder>(

                groupreport.class,
                R.layout.reportlist_row,
                GroupViewholder.class,
                mDatabase.orderByChild("num")
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final groupreport model, final int position) {

                viewHolder.setGroupadv(model.getAdvance());
                viewHolder.setGrouploan(model.getLoans());
                viewHolder.setGrouplsf(model.getLsf());
                if (!model.getIncome().equals("-")){
                    viewHolder.setGroupinc(model.getIncome());
                }else {
                    viewHolder.setGroupinc("0");
                }

                viewHolder.setGroupmwalimu(model.getOfficer());
                viewHolder.setGroupnum(model.getNum());
                viewHolder.setGroupname(model.getGroupname());
                viewHolder.setGroupnumbers(model.getMembers());

                if (!model.getRegs().equals("-")){
                    viewHolder.setGroupregs(model.getRegs());
                }else {
                    viewHolder.setGroupregs("0");
                }


                viewHolder.link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent backtosignin =new Intent(groupreports.this,memberstats.class);
                        Bundle extras = new Bundle();
                        extras.putString("key", getRef(position).getKey());
                        extras.putString("year", yearT);
                        extras.putString("month", month);
                        backtosignin.putExtras(extras);
                        startActivity(backtosignin);
                    }
                });
            }
        };

        mGroups.setAdapter(firebaseRecyclerAdapter);


    }


    private void updateviews() {
        mD1.child(getnextdate()).child(getnextdatemon()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    mDatabase.child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                DatabaseReference newdata=mDatabase.child(dataSnapshot1.getKey());
                                newdata.child("groupname").setValue(dataSnapshot1.child("groupname").getValue().toString());
                                newdata.child("advance").setValue(dataSnapshot1.child("advance").getValue().toString());
                                newdata.child("loans").setValue(dataSnapshot1.child("loans").getValue().toString());
                                newdata.child("lsf").setValue(dataSnapshot1.child("lsf").getValue().toString());
                                newdata.child("members").setValue(dataSnapshot1.child("members").getValue().toString());
                                newdata.child("num").setValue(dataSnapshot1.child("num").getValue());
                                newdata.child("regs").setValue(String.valueOf(dataSnapshot1.child("regs").getValue().toString()));
                                newdata.child("officer").setValue(dataSnapshot1.child("officer").getValue().toString());
                                newdata.child("income").setValue(dataSnapshot1.child("income").getValue().toString());

                            }else {

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


    public static class GroupViewholder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        private TextView link;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;

        DecimalFormat df = new DecimalFormat("##,###,###.#");


        public GroupViewholder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.reportlistcard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            link=mView.findViewById(R.id.totalGroupname);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }




        public void setGroupadv(String groupname){
            long pamnt=Math.round(Double.parseDouble(groupname));
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalAdvancesCarriedForward);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setGrouploan(String groupname){
            long pamnt=Math.round(Double.parseDouble(groupname));
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalLoansCarriedForward);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setGrouplsf(String groupname){
            long pamnt=Math.round(Double.parseDouble(groupname));
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalSavingsCarriedForward);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setGroupinc(String groupname){
            long pamnt=Math.round(Double.parseDouble(groupname));
            TextView mGroupname = (TextView)mView.findViewById(R.id.income);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setGroupname(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalGroupname);
            mGroupname.setText(groupname);
        }
        public void setGroupnumbers(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalmembers);
            mGroupname.setText(groupname);
        }
        public void setGroupmwalimu(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalofficer);
            mGroupname.setText(groupname);
        }
        public void setGroupregs(String groupname){
            long pamnt=Math.round(Double.parseDouble(groupname));
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalregs);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setGroupnum(int groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.totalnum);
            mGroupname.setText(String.valueOf(groupname));
        }
    }


    private String getnextdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.DATE, -7); //Adding30days

        return sdfs.format(c.getTime());
    }
    private String getnextdatemon() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.UK);
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM", Locale.UK);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); //Nowusetodaydate.
        c.add(Calendar.MONTH, -1); //Adding30days

        return sdfs.format(c.getTime());
    }



    private void csvops2() {
        if (ContextCompat.checkSelfPermission(groupreports.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(groupreports.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(groupreports.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
                File f = new File(folder,"/ComprehensiveGroupAnalysis/Excelfiles/");
                File fil = new File(f,"Group Analysis as at "+yearT+" "+month+".csv");
                csv=String.valueOf(fil);
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(groupreports.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                //fo.write(bytes.toByteArray());
                                //oop=1;
                                updatefinances();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(groupreports.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(groupreports.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            //fo.write(bytes.toByteArray());
                            //oop=1;
                            updatefinances();
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
            File f = new File(folder,"/ComprehensiveGroupAnalysis/Excelfiles/");
            File fil = new File(f,"Group Analysis as at "+yearT+" "+month+".csv");
            csv=String.valueOf(fil);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(groupreports.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            //fo.write(bytes.toByteArray());
                            //oop=1;
                            updatefinances();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(groupreports.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(groupreports.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        //fo.write(bytes.toByteArray());
                        //oop=1;
                        updatefinances();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void csvops() {
        if (ContextCompat.checkSelfPermission(groupreports.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(groupreports.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(groupreports.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
                File f = new File(folder,"/GroupAnalysis/Excelfiles/");
                File fil = new File(f,"Group Analysis as at "+yearT+" "+month+".csv");
                csv=String.valueOf(fil);
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(groupreports.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                //fo.write(bytes.toByteArray());
                                //oop=1;
                                updatefinances1();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(groupreports.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(groupreports.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            //fo.write(bytes.toByteArray());
                            //oop=1;
                            updatefinances1();
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
            File f = new File(folder,"/GroupAnalysis/Excelfiles/");
            File fil = new File(f,"Group Analysis as at "+yearT+" "+month+".csv");
            csv=String.valueOf(fil);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(groupreports.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            //fo.write(bytes.toByteArray());
                            //oop=1;
                            updatefinances1();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(groupreports.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(groupreports.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        //fo.write(bytes.toByteArray());
                        //oop=1;
                        updatefinances1();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void updatefinances1() {
        try {
            writer = new CSVWriter(new FileWriter(csv));
            data.add(new String[]{"Group Name","Member Number","Savings","Loans","Advances","Contribution"});
            writer.writeAll(data);

            filldata();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void filldata() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.child("income").exists()&&!snapshot.child("income").getValue().toString().equals("-")) {
                        try {
                            writer = new CSVWriter(new FileWriter(csv));
                            data.add(new String[]{snapshot.child("groupname").getValue().toString(),snapshot.child("num").getValue().toString(),snapshot.child("lsf").getValue().toString(),snapshot.child("loans").getValue().toString(),snapshot.child("advance").getValue().toString(),snapshot.child("income").getValue().toString()});
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
    private void updatefinances() {
        try {
            writer = new CSVWriter(new FileWriter(csv));
            data.add(new String[]{"Group Name","Member Name","Savings","Loans","Advances"});
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mD4.child("allmembers").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                setupmemberstats(snapshot.getKey(),snapshot.child("groupname").getValue().toString());
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
    private void updatefinances2() {
        mD.limitToLast(213).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //(a)
                    mD4.child("allmembers").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                setupmemberstats2(snapshot.getKey(),snapshot.child("name").getValue().toString());
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
    private void setupmemberstats(final String key, final String name) {
        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final String uid=snapshot.child("uid").getValue().toString();
                    final String nn=snapshot.child("name").getValue().toString();
                    mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotw) {
                            try {
                                writer = new CSVWriter(new FileWriter(csv));
                                data.add(new String[]{name,nn,dataSnapshotw.child("savings").child("totalsavings").getValue().toString(),dataSnapshotw.child("loans").child("totalloan").getValue().toString()
                                        ,dataSnapshotw.child("advances").child("currentadvance").getValue().toString()});
                                writer.writeAll(data);

                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //updatefinances2();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setupmemberstats2(final String key, final String name) {
        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final String uid=snapshot.child("uid").getValue().toString();
                    final String nn=snapshot.child("name").getValue().toString();
                    mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotw) {
                            try {
                                writer = new CSVWriter(new FileWriter(csv));
                                data.add(new String[]{name,nn,dataSnapshotw.child("savings").child("totalsavings").getValue().toString(),dataSnapshotw.child("loans").child("totalloan").getValue().toString()
                                        ,dataSnapshotw.child("advances").child("currentadvance").getValue().toString()});
                                writer.writeAll(data);

                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
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
}
