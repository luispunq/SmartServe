package com.example.london.smartserve;

import android.Manifest;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class loandefaultsperfomanceanalysis extends AppCompatActivity {
    private RecyclerView mTrans;
    private DatabaseReference mDatabase,mD,mD2,mD3;
    private String grpkey,meetid,date,gname;
    private double totamount=0,lsfcm=0,loaninstcm=0,advpaycm=0,intadvpaidamnt=0,intadvbf=0,riskcm=0,total=0,wcommcm=0,advncf=0,nettotal=0,advintrestcm=0,loancashtot=0,
            finescm=0,advinttot=0,cashtotal=0,cashgivend=0,totalloanfee=0,totaladvfee=0,memfee=0;
    private String st2=null,st4=null,st3=null,st1=null,st5=null,st6=null,dirpath,filename="image";
    private TextView tamnt,tlsf,tadpaid,tadgn,tlpaid,tlgvn,tadvint,lsfcf,lcf,adcf,pGname,pFac,pDate;
    private LinearLayout mtopdetails;
    private FloatingActionButton fab;
    private int height;
    private ByteArrayOutputStream bytes,bytesw;
    private Bitmap b1,b2;
    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    private String mdate;
    private String fname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfomanceanalysis);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("gkey");
        meetid = extras.getString("meet");
        date = extras.getString("date");
        tamnt = findViewById(R.id.totrepaid);
        mtopdetails=findViewById(R.id.topdetails);
        pGname=findViewById(R.id.pgname);
        pFac=findViewById(R.id.pfac);
        pDate=findViewById(R.id.pdate);
        tlsf = findViewById(R.id.totlsf);
        lsfcf=findViewById(R.id.totlsfcf);
        lcf=findViewById(R.id.totloancf);
        adcf=findViewById(R.id.totadvcf);
        tadpaid = findViewById(R.id.totadvinst);
        tadgn = findViewById(R.id.totadvgvn);
        tlpaid = findViewById(R.id.totloaninst);
        tlgvn = findViewById(R.id.totloangvn);
        tadvint = findViewById(R.id.totinterest);
        mTrans = findViewById(R.id.transactionlist);
        fab=findViewById(R.id.floatingActionButton3);
        mTrans.setHasFixedSize(true);
        mTrans.setItemViewCacheSize(50);
        mTrans.setDrawingCacheEnabled(true);
        mTrans.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mTrans.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("grouptransactions").child(meetid);
        mD = FirebaseDatabase.getInstance().getReference().child("Employees");
        mD3 = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);

        mTrans.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        screen(mTrans);
                    }
                });
                mTrans.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });




        mD3.child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   gname=dataSnapshot.child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<reporttrans,transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<reporttrans, transViewHolder>(

                reporttrans.class,
                R.layout.reporttrans_row,
                transViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final transViewHolder viewHolder, final reporttrans model, int position) {

                final String rk=getRef(position).getKey();

                viewHolder.setLoangivn(model.getLoangvn());
                viewHolder.setLoancf(model.getLoancf());
                viewHolder.setLsfcf(model.getLsfcf());
                viewHolder.setName(model.getMemberid());
                viewHolder.setTotalRepaid(model.getTotalamount());
                viewHolder.setLSF(model.getLsf());
                viewHolder.setLoanInst(model.getLoaninstallments());
                viewHolder.setPaymode(model.getPaymentmode());

            }
        };

        mTrans.setAdapter(firebaseRecyclerAdapter);
        mD3.child("meetings").child(meetid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdate=dataSnapshot.child("date").getValue().toString();
                pDate.setText("Date: "+mdate);
                mD.child(dataSnapshot.child("facid").getValue().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fname=dataSnapshot.child("name").getValue().toString();
                        pFac.setText("Facilitator: "+fname);
                        pGname.setText("Group Name: "+gname);
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


    private void screen(RecyclerView view) {

        View u = findViewById(R.id.tebo);
        View w = findViewById(R.id.tebototals);
        View y = mtopdetails;

        b1 = getBitmapFromView(u,u.getHeight(),u.getWidth());
        b2 = getBitmapFromView(w,w.getHeight(),w.getWidth());
        Bitmap b3=getBitmapFromView(y,y.getHeight(),y.getWidth());

        RelativeLayout z = (RelativeLayout) findViewById(R.id.top);
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

            bigCanvas.drawBitmap(b3,0f,0f,paint);
            bigCanvas.drawBitmap(b1,0f,b3.getHeight(),paint);
            iHeight=iHeight+b1.getHeight()+b3.getHeight();

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

            bigCanvas.drawBitmap(b2,0f,iHeight,paint);

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
                File f = new File(folder,"/ProjectAccountAnalysis for /"+gname);
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
            File f = new File(folder,"/PerfomanceAnalysis/"+gname);
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





        //imageToPDF();
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

    public static class transViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public transViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.transaccard);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }



        private void Layout_hide(){
            layoutParams.height=0;
            layout.setLayoutParams(layoutParams);
        }

        public void setName(String name){
            TextView mGroupname = mView.findViewById(R.id.gridmembername);
            mGroupname.setText(name);
        }
        public void setTotalRepaid(String totalrepaid){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdtotalrepaid);
            mGroupname.setText("Kshs. "+totalrepaid);
        }
        public void setLSF(String lsf){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdlsf);
            mGroupname.setText("Kshs. "+lsf);
        }
        public void setLoanInst(String loaninst){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdloaninst);
            mGroupname.setText("Kshs. "+loaninst);
        }
        public void setAdvance(String advance){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvinst);
            mGroupname.setText("Kshs. "+advance);
        }
        public void setPaymode(String paymode){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdpaymode);
            mGroupname.setText(paymode);
        }
        public void setInsu1(String insu1){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvanceint);
            mGroupname.setText("Kshs. "+insu1);
        }
        public void setLoangivn(final String loangiv){
            TextView mGroupame = mView.findViewById(R.id.grdloangivn);
            mGroupame.setText("Kshs. "+loangiv);
        }
        public void setAdvcgvn(final String advcgvn){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdadvancegiven);
            mGrouname.setText("Kshs. "+advcgvn);
        }
        public void setLoancf(final String loancf){
            TextView mGroupame = mView.findViewById(R.id.grdloancf);
            mGroupame.setText("Kshs. "+loancf);
        }
        public void setAdvccf(final String advccf){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdadvancecf);
            mGrouname.setText("Kshs. "+advccf);
        }
        public void setLsfcf(final String lsfcf){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdlsfcf);
            mGrouname.setText("Kshs. "+lsfcf);
        }
    }
}
