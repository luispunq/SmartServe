package com.example.london.smartserve;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
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

public class formssf extends AppCompatActivity {
    private RecyclerView declined,accepted,disbursed;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutMana,linearLayoutManag,linearLayoutManage;
    private Spinner mPeriod;
    private String choise,date=new SimpleDateFormat("EEE, MMM d, ''yy").format(new Date());
    private HorizontalScrollView app,acc,dec,dis;
    private static OutputStream outputStream;
    private ByteArrayOutputStream bytes,bytesw;
    private FloatingActionButton fab;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;

    DecimalFormat df = new DecimalFormat("##,###,###.#");

    private TextView total1,total2,total3,cash1,cash2,cash3,inst1,inst2,inst3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formssf);

        acc=findViewById(R.id.lacc);
        dec=findViewById(R.id.ldec);
        dis=findViewById(R.id.ldis);
        fab=findViewById(R.id.floatingActionButton8);

        linearLayoutManage = new LinearLayoutManager(this);
        linearLayoutManag = new LinearLayoutManager(this);
        linearLayoutMana = new LinearLayoutManager(this);

        declined=findViewById(R.id.loandeclined);
        accepted=findViewById(R.id.loansapproved);
        disbursed=findViewById(R.id.loandisbursed);

        declined.setHasFixedSize(true);
        accepted.setHasFixedSize(true);
        disbursed.setHasFixedSize(true);

        total1=findViewById(R.id.totalamounnt);
        total2=findViewById(R.id.totalamount2);
        total3=findViewById(R.id.totalamount3);

        cash1=findViewById(R.id.totalcash);
        cash2=findViewById(R.id.totalamountcash2);
        cash3=findViewById(R.id.totalamountcash3);

        inst1=findViewById(R.id.totalinstallment);
        inst2=findViewById(R.id.totalinstallment2);
        inst3=findViewById(R.id.totalinstallment3);

        mPeriod=findViewById(R.id.spinner);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("forms").child(new SimpleDateFormat("yyyy").format(new Date())).child(new SimpleDateFormat("MMM").format(new Date())).child("all");



        accepted.setLayoutManager(linearLayoutManage);
        declined.setLayoutManager(linearLayoutManag);
        disbursed.setLayoutManager(linearLayoutMana);



        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.formoptions,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriod.setAdapter(adapter);

        mPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choise=parent.getItemAtPosition(position).toString();

                switch (choise) {
                    case "Booked":
                        dec.setVisibility(View.GONE);
                        acc.setVisibility(View.VISIBLE);
                        dis.setVisibility(View.GONE);
                        loanapproved();
                        break;
                    case "Filled":
                        dec.setVisibility(View.VISIBLE);
                        acc.setVisibility(View.GONE);
                        dis.setVisibility(View.GONE);
                        loansdeclined();
                        break;
                    default:
                        dis.setVisibility(View.VISIBLE);
                        acc.setVisibility(View.GONE);
                        dec.setVisibility(View.GONE);
                        loansdisbursed();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    }

    private void loansdisbursed() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("status").getValue().toString().equals("disbursed")) {
                            if (snapshot.child("total").exists()&&snapshot.child("amount").exists()&&snapshot.child("installments").exists()){
                                String adv = snapshot.child("total").getValue().toString();
                                String loan = snapshot.child("amount").getValue().toString();
                                String lsf = snapshot.child("installments").getValue().toString();

                                val1 = val1 + Double.parseDouble(adv);
                                val2 = val2 + Double.parseDouble(loan);
                                val3 = val3 + Double.parseDouble(lsf);
                            }
                        }

                    }

                    total3.setText("Kshs. "+df.format(val1));
                    cash3.setText("Kshs. "+df.format(val2));
                    inst3.setText("Kshs. "+df.format(val3));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<forms,declinedViewHolder> firebaseRecyclerAdaptr = new FirebaseRecyclerAdapter<forms,declinedViewHolder>(

                forms.class,
                R.layout.forms_row,
                declinedViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final forms model, int position) {

                if (model.getStatus().equals("disbursed")){
                viewHolder.setGroup(model.getGroupname());
                viewHolder.setName(model.getName());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setMonths(model.getMonths());
                viewHolder.setInstallmets(model.getInstallments());
                viewHolder.setTotalAmount(model.getTotal());
                viewHolder.setDate(model.getDate());
                viewHolder.setOfficer(model.getOfficer());
                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        disbursed.setAdapter(firebaseRecyclerAdaptr);
    }

    private void loansdeclined() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("status").getValue().toString().equals("filled")) {
                            String adv = snapshot.child("total").getValue().toString();
                            String loan = snapshot.child("amount").getValue().toString();
                            String lsf = snapshot.child("installments").getValue().toString();


                            val1 = val1 + Double.parseDouble(adv);
                            val2 = val2 + Double.parseDouble(loan);
                            val3 = val3 + Double.parseDouble(lsf);
                        }

                    }


                    total2.setText("Kshs. "+df.format(val1));
                    cash2.setText("Kshs. "+df.format(val2));
                    inst2.setText("Kshs. "+df.format(val3));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<forms,declinedViewHolder> irebaseRecyclerAdapter = new FirebaseRecyclerAdapter<forms,declinedViewHolder>(

                forms.class,
                R.layout.forms_row,
                declinedViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final forms model, int position) {

                if (model.getStatus().equals("filled")){
                    viewHolder.setGroup(model.getGroupname());
                    viewHolder.setName(model.getName());
                    viewHolder.setAmount(model.getAmount());
                    viewHolder.setMonths(model.getMonths());
                    viewHolder.setInstallmets(model.getInstallments());
                    viewHolder.setTotalAmount(model.getTotal());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setOfficer(model.getOfficer());
                }else {
                    viewHolder.Layout_hide();
                }

            }
        };

        declined.setAdapter(irebaseRecyclerAdapter);
    }

    private void loanapproved() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    val1=0;
                    val2=0;
                    val3=0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        if (snapshot.child("status").getValue().toString().equals("booked")) {
                            String adv = snapshot.child("total").getValue().toString();
                            String loan = snapshot.child("amount").getValue().toString();
                            String lsf = snapshot.child("installments").getValue().toString();


                            val1 = val1 + Double.parseDouble(adv);
                            val2 = val2 + Double.parseDouble(loan);
                            val3 = val3 + Double.parseDouble(lsf);
                        }

                    }


                    total1.setText("Kshs. "+df.format(val1));
                    cash1.setText("Kshs. "+df.format(val2));
                    inst1.setText("Kshs. "+df.format(val3));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<forms,declinedViewHolder> firebaseRecyclerAdapte = new FirebaseRecyclerAdapter<forms,declinedViewHolder>(

                forms.class,
                R.layout.forms_row,
                declinedViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final declinedViewHolder viewHolder, final forms model, int position) {

                if (model.getStatus().equals("booked")){
                    viewHolder.setGroup(model.getGroupname());
                    viewHolder.setName(model.getName());
                    viewHolder.setAmount(model.getAmount());
                    viewHolder.setMonths(model.getMonths());
                    viewHolder.setInstallmets(model.getInstallments());
                    viewHolder.setTotalAmount(model.getTotal());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setOfficer(model.getOfficer());
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
            layout =  itemView.findViewById(R.id.savingshistory1_row);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setDate(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.formdate);
            mGroupname.setText(groupname);
        }
        public void setGroup(String groupname){
            TextView mGroupname = (TextView)mView.findViewById(R.id.formgroup);
            mGroupname.setText(groupname);
        }
        public void setName(String venue){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.formname);
            mGrouploc.setText(venue);
        }
        public void setAmount(String venue){
            long pamnt=Math.round(Double.parseDouble(venue));
            TextView mGroupname = (TextView)mView.findViewById(R.id.formcashamount);
            mGroupname.setText("Kshs. "+df.format(pamnt));
        }
        public void setMonths(String date){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.formmonths);
            mGrouploc.setText(date);
        }
        public void setInstallmets(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.forminstallments);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
        public void setTotalAmount(String amount){
            long pamnt=Math.round(Double.parseDouble(amount));
            TextView mGrouploc = (TextView)mView.findViewById(R.id.formtotalamount);
            mGrouploc.setText("Kshs. "+df.format(pamnt));
        }
        public void setOfficer(String amount){
            TextView mGrouploc = (TextView)mView.findViewById(R.id.formofficer);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/Forms/Disbursed/");
                File fil = new File(f,date+".jpg");
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
            File f = new File(folder,"/Forms/Disbursed/");
            File fil = new File(f,date+".jpg");
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/Forms/Filled/");
                File fil = new File(f,date+".jpg");
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
            File f = new File(folder,"/Forms/Filled/");
            File fil = new File(f,date+".jpg");
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
                String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
                File f = new File(folder,"/Forms/Booked/");
                File fil = new File(f,date+".jpg");
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
            File f = new File(folder,"/Forms/Booked/");
            File fil = new File(f,date+".jpg");
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
