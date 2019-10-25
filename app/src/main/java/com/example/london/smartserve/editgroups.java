package com.example.london.smartserve;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class editgroups extends AppCompatActivity {
    private DatabaseReference mD,m,mD1,mD2,mD3,mD4,mD5,mD0,mD8,mD9,mDatabase,mDatabasemembers,mD11,mDx;
    private ProgressDialog mProgress;
    private StorageReference storageReference,mProfileImageStore;
    private String gn,gi,gid,gf,gm,guid,key,csv;
    private double n1,n2,n3;
    private AlertDialog alert11;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    private double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;
    private double c1=0,c2=0,c3=0;
    private int q=0;
    private long l1=0,l2=0,l3=0;
    private List<String[]> data = new ArrayList<String[]>();
    private List<String> members = new ArrayList<String>();
    CSVWriter writer = null;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgroups);
        mD= FirebaseDatabase.getInstance().getReference().child("Groups");
        mDx= FirebaseDatabase.getInstance().getReference().child("Accounting");
        m=FirebaseDatabase.getInstance().getReference().child("details");
        mD2=FirebaseDatabase.getInstance().getReference().child("finances");
        mD4=FirebaseDatabase.getInstance().getReference().child("members");
        mD1=FirebaseDatabase.getInstance().getReference().child("groupperfomancesummary");
        mD3=FirebaseDatabase.getInstance().getReference().child("masterfinance");
        mD5=FirebaseDatabase.getInstance().getReference().child("meetings");
        mD0=FirebaseDatabase.getInstance().getReference().child("Employees");
        mD8=FirebaseDatabase.getInstance().getReference().child("Accounts");
        mD9=FirebaseDatabase.getInstance().getReference().child("Accounting");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("masterfinance");
        mProfileImageStore= FirebaseStorage.getInstance().getReference().child("stockmemberphoto");
        mDatabasemembers= FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date()));
        mD11=FirebaseDatabase.getInstance().getReference().child("secretaryhandovers");

        //addmemberstocompanysum();

        //initiateemployeetrack();

        //groupanalysis();//--3

        //setupmaster();//--2

        //updatefinances();//--1(a),(b)

        ///initiategroupaccounts();

        //memberaccount();

        //bankaccounts();

        //csvops();

        //eliminateduplicates();

        //replacephotos();

        mD4.child("allmembers").child("-LNnV0dkg6HF5rYHBPx5").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                    String uid=snapshot1.child("uid").getValue().toString();
                    Log.e("idremoved",snapshot1.getKey());
                    DatabaseReference rm=mD4.child(uid).child("temptrs");
                    rm.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void eliminateduplicates() {
        //eliminatingDuplicates

        mD.limitToLast(212).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mD4.child("allmembers").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                String uid=snapshot1.child("uid").getValue().toString();
                                if (members.contains(uid)){
                                    Log.e("idremoved",snapshot1.getKey());
                                    DatabaseReference rm=mD4.child("allmembers").child(snapshot1.getKey());
                                    rm.removeValue();
                                }else {
                                    Log.e("idadded",snapshot1.getKey());
                                    members.add(uid);
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

    private void csvops() {
                if (ContextCompat.checkSelfPermission(editgroups.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(editgroups.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(editgroups.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
                File f = new File(folder,"/GroupAnalysis/Excelfiles/");
                File fil = new File(f,new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".csv");
                csv=String.valueOf(fil);
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(editgroups.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(editgroups.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(editgroups.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

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
                Log.e("file", folder);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/SmartServe";
            File f = new File(folder,"/GroupAnalysis/Excelfiles/");
            File fil = new File(f,new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date())+".csv");
            csv=String.valueOf(fil);
            Log.d("STATE", Environment.getExternalStorageState());
            Log.d("PATH", f.getAbsolutePath());
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(editgroups.this, folder + " has been created.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(editgroups.this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(editgroups.this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

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
            Log.e("file", folder);
        }
    }

    private void bankaccounts() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.e("grp",snapshot.getKey());
                    String totalsavings=snapshot.child("totalsavings").getValue().toString();
                    String totalloans=snapshot.child("totalloans").getValue().toString();
                    String totaladvances=snapshot.child("totaladvances").getValue().toString();


                    c1=c1+Double.parseDouble(totalsavings);
                    c2=c2+Double.parseDouble(totalloans);
                    c3=c3+Double.parseDouble(totaladvances);
                }

                l1=(long)c1;
                l2= (long) c2;
                l3=(long)c3;

                DatabaseReference bank=mD9.child("BankCash").child("Trans").child("all").push();
                bank.child("name").setValue("LSF");
                bank.child("amount").setValue(String.valueOf(l1));
                bank.child("type").setValue("blue");
                bank.child("meet").setValue("-");
                bank.child("group").setValue("-");
                bank.child("debitac").setValue("BankCash");
                bank.child("creditac").setValue("Membersfund");
                bank.child("description").setValue("Deposit");
                bank.child("timestamp").setValue(ServerValue.TIMESTAMP);

                DatabaseReference bankw2=mD9.child("Membersfund").child("Trans").child("all").push();
                bankw2.child("name").setValue("LSF");
                bankw2.child("amount").setValue(String.valueOf(l1));
                bankw2.child("type").setValue("blue");
                bankw2.child("meet").setValue("-");
                bankw2.child("group").setValue("-");
                bankw2.child("debitac").setValue("BankCash");
                bankw2.child("creditac").setValue("Membersfund");
                bankw2.child("description").setValue("Deposit");
                bankw2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                DatabaseReference stl2=mD9.child("BankCash").child("Trans").child("all").push();
                stl2.child("name").setValue("Short Term Loan");
                stl2.child("amount").setValue(String.valueOf(l3));
                stl2.child("type").setValue("red");
                stl2.child("meet").setValue("-");
                stl2.child("group").setValue("-");
                stl2.child("debitac").setValue("Shorttermloans");
                stl2.child("creditac").setValue("BankCash");
                stl2.child("description").setValue("Advance Given in Groups");
                stl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                DatabaseReference stl2l=mD9.child("Shorttermloans").child("Trans").child("all").push();
                stl2l.child("name").setValue("Short Term Loan");
                stl2l.child("amount").setValue(String.valueOf(l3));
                stl2l.child("type").setValue("red");
                stl2l.child("meet").setValue("-");
                stl2l.child("group").setValue("-");
                stl2l.child("debitac").setValue("Shorttermloans");
                stl2l.child("creditac").setValue("BankCash");
                stl2l.child("description").setValue("Advance Given in Groups");
                stl2l.child("timestamp").setValue(ServerValue.TIMESTAMP);

                DatabaseReference ltl2=mD9.child("BankCash").child("Trans").child("all").push();
                ltl2.child("name").setValue("Loan");
                ltl2.child("amount").setValue(String.valueOf(l2));
                ltl2.child("type").setValue("red");
                ltl2.child("meet").setValue("-");
                ltl2.child("group").setValue("-");
                ltl2.child("debitac").setValue("Longtermloan");
                ltl2.child("creditac").setValue("BankCash");
                ltl2.child("description").setValue("Loan Given in Groups");
                ltl2.child("timestamp").setValue(ServerValue.TIMESTAMP);

                DatabaseReference ltl2l=mD9.child("Longtermloan").child("Trans").child("all").push();
                ltl2l.child("name").setValue("Loan");
                ltl2l.child("amount").setValue(String.valueOf(l2));
                ltl2l.child("type").setValue("blue");
                ltl2l.child("meet").setValue("-");
                ltl2l.child("group").setValue("-");
                ltl2l.child("debitac").setValue("Longtermloan");
                ltl2l.child("creditac").setValue("BankCash");
                ltl2l.child("description").setValue("Loan Given in Groups");
                ltl2l.child("timestamp").setValue(ServerValue.TIMESTAMP);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void memberaccount() {
        mD.limitToFirst(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mD4.child("allmembers").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                                final String uid=snapshot.child("uid").getValue().toString();
                                final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid).child("account");
                                mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshotw) {
                                        Log.e("memeid",snapshot.child("uid").getValue().toString());
                                        if (!dataSnapshotw.child("advances").child("currentadvance").getValue().toString().equals("")&&Double.parseDouble(dataSnapshotw.child("advances").child("currentadvance").getValue().toString())>1)
                                        {
                                            DatabaseReference peracc=memstat.push();
                                            peracc.child("name").setValue("Short Term Loan");
                                            peracc.child("amount").setValue(dataSnapshotw.child("advances").child("currentadvance").getValue().toString());
                                            peracc.child("type").setValue("blue");
                                            peracc.child("meet").setValue("-");
                                            peracc.child("group").setValue(snapshot.getKey());
                                            peracc.child("debitac").setValue("member");
                                            peracc.child("creditac").setValue("BankCash");
                                            peracc.child("description").setValue("Advance Given");
                                            peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                        }
                                        if (!dataSnapshotw.child("loans").child("totalloan").getValue().toString().equals("")&&Double.parseDouble(dataSnapshotw.child("loans").child("totalloan").getValue().toString())>1)
                                        {

                                            DatabaseReference peracc=memstat.push();
                                            peracc.child("name").setValue("Loan");
                                            peracc.child("amount").setValue(dataSnapshotw.child("loans").child("totalloan").getValue().toString());
                                            peracc.child("type").setValue("blue");
                                            peracc.child("meet").setValue("-");
                                            peracc.child("group").setValue(snapshot.getKey());
                                            peracc.child("debitac").setValue("member");
                                            peracc.child("creditac").setValue("BankCash");
                                            peracc.child("description").setValue("Loan for "+snapshot.child("name").getValue().toString());
                                            peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);

                                        }

                                        DatabaseReference peracc=memstat.push();
                                        peracc.child("name").setValue("LSF");
                                        peracc.child("amount").setValue(dataSnapshotw.child("savings").child("totalsavings").getValue().toString());
                                        peracc.child("type").setValue("red");
                                        peracc.child("meet").setValue("-");
                                        peracc.child("group").setValue(snapshot.getKey());
                                        peracc.child("debitac").setValue("Membersfund");
                                        peracc.child("creditac").setValue("member");
                                        peracc.child("description").setValue("LSF");
                                        peracc.child("timestamp").setValue(ServerValue.TIMESTAMP);


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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initiategroupaccounts() {
        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    checkmembers(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkmembers(final String key) {
        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final String k=snapshot.child("uid").getValue().toString();
                    mD4.child(k).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("details").child("clearance").getValue().toString().equals("groupAC")){
                                startprocess(k,key);
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

    private void startprocess(final String k, String key) {
        Log.e("group",key);
        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String ii=snapshot.child("uid").getValue().toString();
                    Log.e("member",ii);
                    mD4.child(ii).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("project").child("loans").child("totalloan").exists()){
                                final double fig=Double.parseDouble(dataSnapshot.child("project").child("loans").child("totalloan").getValue().toString());

                                mD4.child(k).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        double pri=Double.parseDouble(dataSnapshot.child("loans").child("totalloan").getValue().toString());
                                        double newpri=pri+fig;
                                        DatabaseReference uppri=mD4.child(k).child("loans");
                                        uppri.child("totalloan").setValue(String.valueOf(newpri));
                                        Log.i(k,String.valueOf(newpri));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }else {
                                final double fig=0;

                                mD4.child(k).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        double pri=Double.parseDouble(dataSnapshot.child("loans").child("totalloan").getValue().toString());
                                        double newpri=pri+fig;
                                        DatabaseReference uppri=mD4.child(k).child("loans");
                                        uppri.child("totalloan").setValue(String.valueOf(newpri));
                                        Log.i(k,String.valueOf(newpri));
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updatefinances() {
        /*try {
            writer = new CSVWriter(new FileWriter(csv));
            data.add(new String[]{"Group Name","Member Name","Savings","Loans","Advances"});
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mD.limitToLast(200).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //(a)
                    mD4.child("allmembers").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){

                                Log.e("name",snapshot.child("name").getValue().toString());
                                setupmemberstats(snapshot.getKey(),snapshot.child("name").getValue().toString());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//(b)
                    //Log.e("pree",snapshot.getKey());
                    mDatabasemembers.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                Log.e("group",snapshot.getKey());
                                val1=0;
                                val2=0;
                                val3=0;
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Log.e("member",snapshot.getKey());
                                    String adv = snapshot.child("advance").getValue().toString();
                                    String loan = snapshot.child("loans").getValue().toString();
                                    String lsf = snapshot.child("savings").getValue().toString();

                                    DatabaseReference rem=mD4.child(snapshot.getKey());
                                    rem.child("accounts").removeValue();

                                    if (Double.parseDouble(adv)<1){
                                        DatabaseReference upad=mD4.child(snapshot.getKey()).child("advances").child("currentadvance");
                                        upad.setValue("0");
                                    }

                                    if (Double.parseDouble(loan)<1){
                                        DatabaseReference upad=mD4.child(snapshot.getKey()).child("loans").child("totalloan");
                                        upad.setValue("0");
                                    }

                                    if (!adv.equals("")&&!loan.equals("")&&!lsf.equals("")){
                                        if (loan.contains("-")||adv.contains("-")){
                                            Log.i("memberless",snapshot.getKey());
                                        }else {
                                            val1 = val1 + Double.parseDouble(adv);
                                            val2 = val2 + Double.parseDouble(loan);
                                            val3 = val3 + Double.parseDouble(lsf);
                                        }
                                    }else {
                                        Log.i("groupless",snapshot.getKey());
                                        val1 = val1 + 0;
                                        val2 = val2 + 0;
                                        val3 = val3 + 0;
                                    }

                                }
                                DatabaseReference ref=mD2.child(snapshot.getKey());
                                ref.child("advances").child("currentadvance").setValue(String.valueOf(val1));
                                ref.child("advances").child("currentadvancebf").setValue(String.valueOf(val1));
                                ref.child("loans").child("currentloan").setValue(String.valueOf(val2));
                                ref.child("loans").child("currentloanbf").setValue(String.valueOf(val2));
                                ref.child("savings").child("totalsavings").setValue(String.valueOf(val3));
                            }else {
                                Log.i("fail",snapshot.getKey());
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

    private void setloansforgroup() {
        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot3) {
                for (DataSnapshot snapshot:dataSnapshot3.getChildren()){
                    key=snapshot.getKey();

                    mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                final String uid=snapshot.child("uid").getValue().toString();

                                mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("details").child("clearance").getValue().toString().equals("groupAC")){
                                            guid=uid;
                                        }

                                        if (dataSnapshot.child("project").child("loans").exists()){
                                            n1=n1+Double.parseDouble(dataSnapshot.child("project").child("loans").child("totalloan").getValue().toString());
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            DatabaseReference updateloans=FirebaseDatabase.getInstance().getReference().child("members").child(guid).child("loans");
                            updateloans.child("totalloan").setValue(String.valueOf(n1));
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

    private void toUppercasenames() {
        //final DatabaseReference mDatabase3= FirebaseDatabase.getInstance().getReference().child("Groups");
        mD3= FirebaseDatabase.getInstance().getReference().child("Groups");

        mD3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String newname=snapshot.child("name").getValue().toString().toUpperCase();
                    String iid=snapshot.getKey();

                    DatabaseReference newmem=mD3.child(iid);
                    newmem.child("name").setValue(newname);

                    //mDatabase3.child(iid).child("details").child("name").setValue(newname);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void replacephotos() {
        final String dUrl="https://firebasestorage.googleapis.com/v0/b/smartserve-8a951.appspot.com/o/stockphoto%2Fsmartservelogos-web.png?alt=media&token=4981ea82-f5b9-40b7-99cd-b5f377e970a6";


        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String uid=snapshot.child("uid").getValue().toString();

                    DatabaseReference newmember=mD4.child("allmembers").child(key).child(snapshot.getKey());
                    newmember.child("profileImage").setValue(dUrl);

                    DatabaseReference rp=mD4.child(uid).child("details");
                    rp.child("profileImage").setValue(dUrl);
                    Log.e("member",uid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initiateemployeetrack() {
        mD0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DatabaseReference newloan=mD0.child(snapshot.getKey()).child("basicpays");
                    newloan.child("basicpay").setValue("0");
                    newloan.child("comms").setValue("0");
                    newloan.child("other").setValue("0");

                    DatabaseReference newloane=mD0.child(snapshot.getKey()).child("deductions");
                    newloane.child("nssf").setValue("0");
                    newloane.child("nhif").setValue("0");
                    newloane.child("paye").setValue("0");

                    DatabaseReference newloanl=mD0.child(snapshot.getKey()).child("loans");
                    newloanl.child("totalloan").setValue("0");

                    DatabaseReference newloana=mD0.child(snapshot.getKey()).child("advances");
                    newloana.child("currentadvance").setValue("0");

                    Log.e("empid",snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void iniateaccounts() {
        mD0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DatabaseReference initiate=mD8.child(snapshot.getKey());
                    initiate.child("Balance").setValue("1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addmemberstocompanysum() {
        final RecyclerView recyclerView=findViewById(R.id.allmeetings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(editgroups.this));
        recycleradapt(recyclerView);
    }

    private void setupmemberstats(final String key,final String name) {
        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.e("group",name);
                    final String uid=snapshot.child("uid").getValue().toString();
                    final String nn=snapshot.child("name").getValue().toString();
                    Log.e("member",nn);
                    final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("memberstats").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(key).child(uid);
                    mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotw) {
                            memstat.child("name").setValue(nn);
                            memstat.child("savings").setValue(dataSnapshotw.child("savings").child("totalsavings").getValue().toString());
                            if (!dataSnapshotw.child("advances").child("currentadvance").getValue().toString().equals("")&&Double.parseDouble(dataSnapshotw.child("advances").child("currentadvance").getValue().toString())>1)
                            {
                                memstat.child("advance").setValue(dataSnapshotw.child("advances").child("currentadvance").getValue().toString());
                            }else {
                                memstat.child("advance").setValue("0");
                            }
                            if (!dataSnapshotw.child("loans").child("totalloan").getValue().toString().equals("")&&Double.parseDouble(dataSnapshotw.child("loans").child("totalloan").getValue().toString())>1)
                            {
                                memstat.child("loans").setValue(dataSnapshotw.child("loans").child("totalloan").getValue().toString());
                            }else {
                                memstat.child("loans").setValue("0");
                            }

                            DatabaseReference rem=mD4.child(uid);
                            //rem.child("account").removeValue();

                            /*try {
                                writer = new CSVWriter(new FileWriter(csv));
                                data.add(new String[]{name,nn,dataSnapshotw.child("savings").child("totalsavings").getValue().toString(),dataSnapshotw.child("loans").child("totalloan").getValue().toString()
                                        ,dataSnapshotw.child("advances").child("currentadvance").getValue().toString()});
                                writer.writeAll(data);

                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/

                            //memstat.child("riskfund").setValue("0");
                            //memstat.child("arreas").setValue("0");
                            Log.e("member2",nn);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    /*mD4.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("loans").child("totalloan").getValue().toString().equals("0")){
                                //q=q+1;

                                DatabaseReference db=mD2.child(key).child("loans").child("loanon").push();
                                db.child("user").setValue(uid);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupmemberstat(final String key) {
        m.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String date=dataSnapshot.child("groupdetails").child("nextdate").getValue().toString();

                mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot3) {
                        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    String uid=snapshot.child("uid").getValue().toString();
                                    String nn=snapshot.child("name").getValue().toString();
                                    Log.e("member",nn);
                                    final DatabaseReference memstat=FirebaseDatabase.getInstance().getReference().child("members").child(uid);
                                    memstat.child("name").setValue(nn);
                                    if (dataSnapshot3.child(uid).child("loans").child("loanflag").exists()){
                                        memstat.child("loans").child("loanflag").setValue(date);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        m.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String date=dataSnapshot.child("groupdetails").child("nextdate").getValue().toString();

                mD4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot3) {
                        mD4.child("allmembers").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    String uid=snapshot.child("uid").getValue().toString();
                                    String nn=snapshot.child("name").getValue().toString();
                                    Log.e("member",nn);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setpupdatedmasterreports() {
        m.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot2) {
                mD5.child("master").child("2018").child("Dec").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            gid=snapshot.child("groupid").getValue().toString();
                            gm=snapshot.child("meetid").getValue().toString();
                            Log.e("idd",gid+" "+gm);

                            if (dataSnapshot2.child(gid).child("meetings").child(gm).child("date").exists()) {
                                DatabaseReference reference = mD5.child("master").child("2018").child("Dec").child(gid);
                                reference.child("date").setValue(dataSnapshot2.child(gid).child("meetings").child(gm).child("date").getValue().toString());
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

    private void setpmasterreports() {
        mProgress=new ProgressDialog(this);
        mProgress.show();
        mD5.child("master").child("2018").child("Dec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    gn=snapshot.child("groupName").getValue().toString();
                    gi=snapshot.child("groupImage").getValue().toString();
                    gid=snapshot.child("groupid").getValue().toString();
                    gf=snapshot.child("facilitator").getValue().toString();
                    gm=snapshot.child("meetid").getValue().toString();

                    Log.e("gid",gid);

                    DatabaseReference reference=mD5.child("master").child("2018").child("Dec").child(gid);
                    reference.child("groupName").setValue(gn);
                    reference.child("groupImage").setValue(gi);
                    reference.child("groupid").setValue(gid);
                    reference.child("facilitator").setValue(gf);
                    reference.child("meetid").setValue(gm);

                    DatabaseReference reference1=mD5.child("master").child("2018").child("Dec").child(snapshot.getKey());
                    reference1.removeValue();
                    Log.e("gid remove",snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgress.dismiss();
    }

    private void uploadupdate() {
        String folder = Environment.getExternalStorageDirectory()+"/SmartServe/apk";
        final File f = new File(folder,"app-release.apk");

        Uri file = Uri.fromFile(f);
        StorageReference riversRef = storageRef.child("apks/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("upload","Upload fail.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.e("upload","Upload done.");
            }
        });
    }

    private void nums() {
        mD1.child("2019").child("Jan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String numb=snapshot.child("num").getValue().toString();
                    int numbs=Integer.parseInt(numb);
                    DatabaseReference fer=mD1.child("2019").child("Jan").child(snapshot.getKey());
                    fer.child("num").setValue(numbs);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupmaster() {
        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mD2.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference ref=mD3.child(snapshot.getKey());
                            if (dataSnapshot.child("savings").child("totalsavings").exists()){
                                Log.e("group",snapshot.getKey());
                                ref.child("totalsavings").setValue(dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                Log.e(snapshot.getKey(),dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                ref.child("totalloans").setValue(dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                Log.e(snapshot.getKey()+" loans",dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                ref.child("totaladvances").setValue(dataSnapshot.child("advances").child("currentadvance").getValue().toString());
                                Log.e(snapshot.getKey()+" advance",dataSnapshot.child("advances").child("currentadvance").getValue().toString());
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

    private void groupanalysis() {
        mD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Log.e("continue",snapshot.getKey());
                    final DatabaseReference refs=mD1.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child(snapshot.getKey());
                    refs.child("groupname").setValue(snapshot.child("name").getValue().toString());

                    Log.e("midway","midway");

                    mD1.child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child(snapshot.getKey()).hasChild("regs")){
                                m.child(snapshot.getKey()).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("ujinga1",snapshot.getKey());
                                        refs.child("members").setValue(dataSnapshot.child("numbers").getValue().toString());
                                        refs.child("num").setValue(Integer.parseInt(dataSnapshot.child("num").getValue().toString()));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mD2.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("ujinga2",snapshot.getKey());
                                        refs.child("lsf").setValue(dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                        Log.e(snapshot.getKey(),dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                        refs.child("loans").setValue(dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                        Log.e(snapshot.getKey()+" loans",dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                        refs.child("advance").setValue(dataSnapshot.child("advances").child("currentadvance").getValue().toString());
                                        Log.e(snapshot.getKey()+" advance",dataSnapshot.child("advances").child("currentadvance").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                refs.child("income").setValue("-");
                                refs.child("regs").setValue("-");
                                refs.child("officer").setValue("-");
                                Log.e("near","near1");
                            }else {
                                m.child(snapshot.getKey()).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("ujinga1",snapshot.getKey());
                                        //refs.child("members").setValue(dataSnapshot.child("numbers").getValue().toString());
                                        //refs.child("num").setValue(Integer.parseInt(dataSnapshot.child("num").getValue().toString()));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mD2.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("ujinga2",snapshot.getKey());
                                        //refs.child("lsf").setValue(dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                        Log.e(snapshot.getKey(),dataSnapshot.child("savings").child("totalsavings").getValue().toString());
                                        //refs.child("loans").setValue(dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                        Log.e(snapshot.getKey()+" loans",dataSnapshot.child("loans").child("currentloan").getValue().toString());
                                        //refs.child("advance").setValue(dataSnapshot.child("advances").child("currentadvance").getValue().toString());
                                        Log.e(snapshot.getKey()+" advance",dataSnapshot.child("advances").child("currentadvance").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //refs.child("income").setValue("-");
                                //refs.child("regs").setValue("-");
                                //refs.child("officer").setValue("-");
                                Log.e("near","near2");
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

    private void meetid(){
        m.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("groupdetails").child("meetid").exists()){
                        Log.e("group", " naah Found");
                        DatabaseReference reference=m.child(snapshot.getKey()).child("groupdetails");
                        reference.child("meetid").removeValue();
                    }else {
                        Log.e(snapshot.getKey(), snapshot.getKey());
                        DatabaseReference dd=m.child(snapshot.getKey()).child("groupdetails");
                        dd.child("type").setValue("Normal Groups");
                        Log.e("group", "Found");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recycleradapt(RecyclerView r) {
        FirebaseRecyclerAdapter<group,GroupViewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<group,GroupViewholder>(
                group.class,
                R.layout.groupslist_row,
                GroupViewholder.class,
                mD.orderByChild("name")
        )
        {
            @Override
            protected void populateViewHolder(final GroupViewholder viewHolder, final group model, int position) {
                final String u_key=getRef(position).getKey();

                viewHolder.setGroupName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alert11.dismiss();
                        //setupmemberstats(u_key);
                        setupmemberstats(u_key,model.getName());
                    }
                });


            }
        };
        r.setAdapter(firebaseRecyclerAdapter);

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

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
