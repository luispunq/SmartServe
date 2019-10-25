package com.example.london.smartserve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.LruCache;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loansf extends AppCompatActivity {
    private RecyclerView masterloanslist,declined,accepted,disbursed;
    private DatabaseReference mDatabase,mD1,mD2;
    private LinearLayoutManager linearLayoutManager,linearLayoutMana,linearLayoutManag,linearLayoutManage;
    private loanappsadapter adapter;
    private Spinner mPeriod;
    private String choise,monthx,yearx;
    private HorizontalScrollView app,acc,dec,dis;
    private static OutputStream outputStream;
    private ByteArrayOutputStream bytes,bytesw;
    private FloatingActionButton fab,fab2;
    private static final int REQUEST_WRITE_STORAGE = 112;

    private TextView total1,total2,total3,inst1,inst2,inst3;
    private double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loansf);

        acc=findViewById(R.id.lacc);
        dec=findViewById(R.id.ldec);
        dis=findViewById(R.id.ldis);
        fab=findViewById(R.id.floatingActionButton8);
        fab2=findViewById(R.id.floatingActionButtondate);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManage = new LinearLayoutManager(this);
        linearLayoutManag = new LinearLayoutManager(this);
        linearLayoutMana = new LinearLayoutManager(this);
        masterloanslist=findViewById(R.id.loansapplied);
        declined=findViewById(R.id.loandeclined);
        accepted=findViewById(R.id.loansapproved);
        disbursed=findViewById(R.id.loandisbursed);
        declined.setHasFixedSize(true);
        accepted.setHasFixedSize(true);
        disbursed.setHasFixedSize(true);
        masterloanslist.setHasFixedSize(true);
        mPeriod=findViewById(R.id.spinner);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("loanrequests");
        mD1=FirebaseDatabase.getInstance().getReference().child("loanapprovals");
        mD2=FirebaseDatabase.getInstance().getReference().child("loandisbursed");


        adapter=new loanappsadapter(masterloanapps.class, R.layout.masterloanapp_row, loanappsholder.class, mDatabase, this);
        masterloanslist.setLayoutManager(linearLayoutManager);
        accepted.setLayoutManager(linearLayoutManage);
        declined.setLayoutManager(linearLayoutManag);
        disbursed.setLayoutManager(linearLayoutMana);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        total1=findViewById(R.id.totalamounnt1);
        total2=findViewById(R.id.totalamounnt2);
        total3=findViewById(R.id.totalamounnt3);


        inst1=findViewById(R.id.totalinstallment1);
        inst2=findViewById(R.id.totalinstallment2);
        inst3=findViewById(R.id.totalinstallment3);

        masterloanslist.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.loanoptions,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriod.setAdapter(adapter);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(10, 5, 5, 0);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dis.getVisibility()==View.VISIBLE){
                    screen1(disbursed);
                } else if (dec.getVisibility()==View.VISIBLE) {
                    screen2(declined);
                } else if (acc.getVisibility()==View.VISIBLE) {
                    screen3(accepted);
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builders = new AlertDialog.Builder(loansf.this);
                Spinner years=new Spinner(loansf.this);
                final Spinner months=new Spinner(loansf.this);

                ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(loansf.this,R.array.yearsx,android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                years.setAdapter(adapter);

                ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(loansf.this,R.array.Months,android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                months.setAdapter(adapter2);

                years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        yearx="'"+parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        yearx="'19";
                    }
                });

                months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        monthx=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        monthx=new SimpleDateFormat("MMM").format(new Date());
                    }
                });

                LinearLayout layout=new LinearLayout(loansf.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(years);
                years.setLayoutParams(params);
                layout.addView(months);
                months.setLayoutParams(params);

                builders.setTitle("Select Period")
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choise=parent.getItemAtPosition(position).toString();

                                        switch (choise) {
                                            case "Loans Approved":
                                                dec.setVisibility(View.GONE);
                                                acc.setVisibility(View.VISIBLE);
                                                dis.setVisibility(View.GONE);
                                                loanapproved(yearx,monthx);
                                                break;
                                            case "Loans Declined":
                                                dec.setVisibility(View.VISIBLE);
                                                acc.setVisibility(View.GONE);
                                                dis.setVisibility(View.GONE);
                                                loansdeclined(yearx,monthx);
                                                break;
                                            default:
                                                dis.setVisibility(View.VISIBLE);
                                                acc.setVisibility(View.GONE);
                                                dec.setVisibility(View.GONE);
                                                loansdisbursed(yearx,monthx);
                                                break;
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        dec.setVisibility(View.GONE);
                                        acc.setVisibility(View.GONE);
                                        dis.setVisibility(View.GONE);
                                    }
                                });

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

    private void loansdisbursed(final String yearx, final String monthx) {

        mD2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().contains(yearx)&&snapshot.child("date").getValue().toString().contains(monthx)){
                            if (!snapshot.child("status").getValue().toString().equals("disbursed")) {
                                String adv = snapshot.child("amountcash").getValue().toString();
                                String loan = snapshot.child("monthlypay").getValue().toString();


                                val1 = val1 + Double.parseDouble(adv);
                                val2 = val2 + Double.parseDouble(loan);


                            }
                        }

                    }

                    total3.setText("Kshs. "+df.format(val1));
                    inst3.setText("Kshs. "+df.format(val2));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<loanreport,declinedViewHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<loanreport,declinedViewHolder>(

                loanreport.class,
                R.layout.loanreport_row,
                declinedViewHolder.class,
                mD2
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final loanreport model, int position) {

                if (model.getDate().contains(yearx)&&model.getDate().contains(monthx)){
                    viewHolder.setAmount(model.getAmountcash());
                    viewHolder.setInstallment(model.getMonthlypay());
                    viewHolder.setName(model.getUsername());
                    viewHolder.setStatus(model.getStatus());
                    viewHolder.setGroup(model.getGname());
                    viewHolder.setDate(model.getDate());
                }else {
                    viewHolder.Layout_hide();
                }
            }
        };

        disbursed.setAdapter(firebaseRecyclerAdaptr);
    }

    private void loansdeclined(final String yearx, final String monthx) {

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().contains(yearx)&&snapshot.child("date").getValue().toString().contains(monthx)){
                            if (snapshot.child("status").getValue().toString().contains("declined")) {
                                String adv = snapshot.child("amountcash").getValue().toString();
                                String loan = snapshot.child("monthlypay").getValue().toString();


                                val1 = val1 + Double.parseDouble(adv);
                                val2 = val2 + Double.parseDouble(loan);

                            }
                        }
                        total2.setText("Kshs. "+df.format(val1));
                        inst2.setText("Kshs. "+df.format(val2));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<loanreport,declinedViewHolder> irebaseRecyclerAdapter = new FirebaseRecyclerAdapter<loanreport,declinedViewHolder>(

                loanreport.class,
                R.layout.loanreport_row,
                declinedViewHolder.class,
                mD1
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final loanreport model, int position) {

                if (model.getDate().contains(yearx)&&model.getDate().contains(monthx)){
                    if (model.getStatus().equals("declined")){
                        viewHolder.setAmount(model.getAmountcash());
                        viewHolder.setInstallment(model.getMonthlypay());
                        viewHolder.setName(model.getUsername());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setGroup(model.getGname());
                        viewHolder.setDate(model.getDate());
                    }else {
                        viewHolder.Layout_hide();
                    }
                }else {
                    viewHolder.Layout_hide();
                }


            }
        };

        declined.setAdapter(irebaseRecyclerAdapter);
    }

    private void loanapproved(final String yearx, final String monthx) {

        mD1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("date").getValue().toString().contains(yearx)&&snapshot.getValue().toString().contains(monthx)){
                            if (snapshot.child("status").getValue().toString().equals("pending")) {
                                String adv = snapshot.child("amountcash").getValue().toString();
                                String loan = snapshot.child("monthlypay").getValue().toString();


                                val1 = val1 + Double.parseDouble(adv);
                                val2 = val2 + Double.parseDouble(loan);

                            }
                        }

                        total1.setText("Kshs. "+df.format(val1));
                        inst1.setText("Kshs. "+df.format(val2));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<loanreport,declinedViewHolder> firebaseRecyclerAdapte = new FirebaseRecyclerAdapter<loanreport,declinedViewHolder>(

                loanreport.class,
                R.layout.loanreport_row,
                declinedViewHolder.class,
                mD1.orderByChild("amountrequested")
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final loanreport model, int position) {

                if (model.getStatus().equals("declined")) {

                }
                if (!model.getDate().isEmpty()){
                if (model.getDate().contains(yearx)&&model.getDate().contains(monthx)){
                    if (model.getStatus().equals("pending")){
                        viewHolder.setAmount(model.getAmountcash());
                        viewHolder.setInstallment(model.getMonthlypay());
                        viewHolder.setName(model.getUsername());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setGroup(model.getGname());
                        viewHolder.setDate(model.getDate());
                    }else {
                        viewHolder.Layout_hide();
                    }
                }else {
                    viewHolder.Layout_hide();
                }
                }else {
                    viewHolder.Layout_hide();
                }
            }
        };

        accepted.setAdapter(firebaseRecyclerAdapte);
    }

    public static class declinedViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;
        DecimalFormat df = new DecimalFormat("##,###,###.#");


        public declinedViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout =  itemView.findViewById(R.id.loanrep);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.loansdates);
            mGroupname.setText(groupname);
        }

        public void setName(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.loanmember);
            mGroupname.setText(groupname);
        }
        public void setAmount(String venue){
            long pamnt=Math.round(Double.parseDouble(venue));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.loanamount);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
        public void setGroup(String venue){
            TextView mGroupname = (TextView)mView.findViewById(R.id.loangroup);
            mGroupname.setText(venue);
        }
        public void setInstallment(String date){
            long pamnt=Math.round(Double.parseDouble(date));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.moinstalemnt);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
        public void setStatus(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.loansstatus);
            mGrouploc.setText(amount);
        }
    }

    private void screen1(RecyclerView view) {

        View u = findViewById(R.id.tebodis);


        Bitmap b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());


        RelativeLayout z = (RelativeLayout) findViewById(R.id.ldistop);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        RecyclerView.Adapter adapter = view.getAdapter();

        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = getBitmapFromView(holder.itemView,holder.itemView.getHeight(),totalWidth);
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height+(height/2), Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            bigCanvas.drawBitmap(b1,0f,0f,paint);

            iHeight=iHeight+b1.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }


            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/LoanReports/");
                File fil = new File(f,"loansdisbursed.jpg");
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                fo.write(bytes.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
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
            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/LoanReports/");
            File fil = new File(f,"loansdisbursed.jpg");
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void screen2(RecyclerView view) {

        View u = findViewById(R.id.tebodec);


        Bitmap b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());


        RelativeLayout z = (RelativeLayout) findViewById(R.id.ldectop);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        RecyclerView.Adapter adapter = view.getAdapter();

        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = getBitmapFromView(holder.itemView,holder.itemView.getHeight(),totalWidth);
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height+(height/2), Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            bigCanvas.drawBitmap(b1,0f,0f,paint);

            iHeight=iHeight+b1.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }


            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/LoanReports/");
                File fil = new File(f,"loansDeclined.jpg");
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                fo.write(bytes.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
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
            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/LoanReports/");
            File fil = new File(f,"loansdisbursed.jpg");
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void screen3(RecyclerView view) {

        View u = findViewById(R.id.teboacc);


        Bitmap b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());


        RelativeLayout z = (RelativeLayout) findViewById(R.id.lacctop);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        RecyclerView.Adapter adapter = view.getAdapter();

        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = getBitmapFromView(holder.itemView,holder.itemView.getHeight(),totalWidth);
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height+(height/2), Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            bigCanvas.drawBitmap(b1,0f,0f,paint);

            iHeight=iHeight+b1.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }


            bytes = new ByteArrayOutputStream();
            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/LoanReports/");
                File fil = new File(f,"loansaccepted.jpg");
                if (!f.exists()) {
                    if (f.mkdirs()) {
                        Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                        try {
                            boolean sucess = fil.createNewFile();
                            if (sucess){
                                FileOutputStream fo = new FileOutputStream(fil);
                                fo.write(bytes.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                    try {
                        boolean sucess  = fil.createNewFile();

                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
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
            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/LoanReports/");
            File fil = new File(f,"loansdisbursed.jpg");
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, fil+ " has been created.", Toast.LENGTH_SHORT).show();

                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
