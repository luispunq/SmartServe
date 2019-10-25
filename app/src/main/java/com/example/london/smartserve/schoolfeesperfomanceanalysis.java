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

public class schoolfeesperfomanceanalysis extends AppCompatActivity {
    private RecyclerView mTrans;
    private DatabaseReference mDatabase,mD,mD2,mD3,mD4,mD5;;
    private String grpkey,meetid,date,gname;
    private double totamount=0,lsfcm=0,loaninstcm=0,advpaycm=0,intadvpaidamnt=0,intadvbf=0,riskcm=0,total=0,wcommcm=0,advncf=0,nettotal=0,advintrestcm=0,loancashtot=0,
            finescm=0,advinttot=0,cashtotal=0,cashgivend=0,totalloanfee=0,totaladvfee=0,memfee=0,ag=0,sacff=0,adcff=0;
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
        setContentView(R.layout.activity_schoolfeesperfomanceanalysis);
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey).child("schoolfees").child("transactions").child(meetid);
        mD = FirebaseDatabase.getInstance().getReference();
        mD2 = FirebaseDatabase.getInstance().getReference().child("finances").child(grpkey);
        mD3 = FirebaseDatabase.getInstance().getReference().child("details").child(grpkey);
        mD4=FirebaseDatabase.getInstance().getReference().child("members");
        mD5=FirebaseDatabase.getInstance().getReference().child("masterfinance");




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



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String amount=(String)snapshot.child("totalamount").getValue();
                    String lsfamount=(String)snapshot.child("lsf").getValue();
                    String advpayamount=(String)snapshot.child("advancepayment").getValue();
                    String advpayintrest=(String)snapshot.child("advanceinterest").getValue();
                    String advgi=(String)snapshot.child("advgvn").getValue();
                    String sacf=(String)snapshot.child("lsfcf").getValue();
                    String adcfs=(String)snapshot.child("advcf").getValue();

                    double val1=Double.parseDouble(amount);
                    double val2=Double.parseDouble(lsfamount);
                    double val4=Double.parseDouble(advpayamount);
                    double val5=Double.parseDouble(advpayintrest);
                    double val7=Double.parseDouble(advgi);
                    double val8=Double.parseDouble(sacf);
                    double val10=Double.parseDouble(adcfs);


                    advintrestcm=advintrestcm+val5;
                    totamount=totamount+val1;
                    lsfcm=lsfcm+val2;
                    advpaycm=advpaycm+val4;
                    ag=ag+val7;
                    sacff=sacff+val8;
                    adcff=adcff+val10;

                    st1=String.valueOf(totamount);
                    st2=String.valueOf(lsfcm);
                    st4=String.valueOf(advpaycm);
                    st5=String.valueOf(totamount);
                    st6=String.valueOf(advintrestcm);

                    tamnt.setText("Kshs. "+st5);
                    tlsf.setText("Kshs. "+st2);
                    tadpaid.setText("Kshs. "+st4);
                    tadvint.setText("Kshs. "+st6);
                    tadgn.setText("Kshs. "+String.valueOf(ag));
                    lsfcf.setText("Kshs. "+String.valueOf(sacff));
                    adcf.setText("Kshs. "+String.valueOf(adcff));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        FirebaseRecyclerAdapter<schoolfeesreporttrans,transViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<schoolfeesreporttrans, transViewHolder>(

                schoolfeesreporttrans.class,
                R.layout.reporttrans2_row,
                transViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(final transViewHolder viewHolder, final schoolfeesreporttrans model, int position) {

                final String rk=getRef(position).getKey();

                viewHolder.setAdvccf(model.getAdvcf());
                viewHolder.setLsfcf(model.getLsfcf());
                viewHolder.setAdvcgvn(model.getAdvgvn());
                viewHolder.setName(model.getMemberid());
                viewHolder.setAdvanceint(model.getAdvanceinterest());
                viewHolder.setTotalRepaid(model.getTotalamount());
                viewHolder.setLSF(model.getLsf());
                viewHolder.setAdvance(model.getAdvancepayment());
                viewHolder.setPaymode(model.getPaymentmode());




                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builders = new AlertDialog.Builder(schoolfeesperfomanceanalysis.this);
                        builders.setTitle("Reverse Transaction")
                                .setCancelable(true)
                                .setPositiveButton("Revert", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rollback(model.getId(),grpkey);
                                        DatabaseReference reference=mDatabase.child(rk);
                                        reference.removeValue();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        };

        mTrans.setAdapter(firebaseRecyclerAdapter);

        mD.child("details").child(grpkey).child("meetings").child(meetid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdate=dataSnapshot.child("date").getValue().toString();
                pDate.setText("Date: "+mdate);
                mD.child("Employees").child(dataSnapshot.child("facid").getValue().toString()).addValueEventListener(new ValueEventListener() {
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

    private void rollback(final String memberid, final String grpkey) {
        try{
            mD4.child(memberid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String toups=dataSnapshot.child("savings").child("schoolfees").child("totalsavings").getValue().toString();
                    final String toupa=dataSnapshot.child("advances").child("schoolfees").child("currentadvance").getValue().toString();

                    final String tmps=dataSnapshot.child("savings").child("schoolfees").child("temp").child("tempsavings").getValue().toString();
                    final String tmpa=dataSnapshot.child("advances").child("schoolfees").child("temp").child("tempadv").getValue().toString();

                    final double tous=Double.parseDouble(toups);
                    final double toua=Double.parseDouble(toupa);

                    double us=tous-Double.parseDouble(tmps);
                    double ua=toua+Double.parseDouble(tmpa);

                    double uint=ua*0.1;

                    DatabaseReference databaseReference=mD4.child(memberid);
                    if (dataSnapshot.child("savings").child("schoolfees").child("temp").child("tempssid").exists()){
                        databaseReference.child("savings").child("schoolfees").child("savings").child(dataSnapshot.child("savings").child("schoolfees").child("temp").child("tempssid").getValue().toString()).removeValue();
                    }
                    databaseReference.child("savings").child("schoolfees").child("totalsavings").setValue(String.valueOf(us));
                    databaseReference.child("advances").child("currentadvance").setValue(String.valueOf(ua));
                    databaseReference.child("advances").child("currpenalty").setValue(String.valueOf(uint));

                    mD2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String gtoups=dataSnapshot.child("savings").child("schoolfees").child("totalsavings").getValue().toString();
                            String gtoupa=dataSnapshot.child("advances").child("schoolfees").child("currentadvance").getValue().toString();

                            double gtous=Double.parseDouble(gtoups);
                            double gtoua=Double.parseDouble(gtoupa);

                            double gus=gtous-Double.parseDouble(tmps);
                            double gua=gtoua+Double.parseDouble(tmpa);

                            DatabaseReference databaseReference1=mD2;
                            databaseReference1.child("savings").child("schoolfees").child("totalsavings").setValue(String.valueOf(gus));
                            databaseReference1.child("advances").child("schoolfees").child("currentadvance").setValue(String.valueOf(gua));

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
        }catch (Exception e){
            Toast.makeText(this,"Transaction has not been reversed.", Toast.LENGTH_SHORT).show();
        }


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
            Bitmap bigBitmap = Bitmap.createBitmap(z.getMeasuredWidth(), height*2, Bitmap.Config.ARGB_8888);
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



        isExternalStorageReadable();
        isExternalStorageWritable();

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
                File f = new File(folder,"/SchoolfeesPerfomanceAnalysis/"+gname);
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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;

        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static class transViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams layoutParams;
        View mView;


        public transViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            layout = (RelativeLayout) itemView.findViewById(R.id.transaccard2);
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

        public void setAdvance(String advance){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvinst);
            mGroupname.setText("Kshs. "+advance);
        }

        public void setAdvanceint(String advance){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdadvanceint);
            mGroupname.setText("Kshs. "+advance);
        }

        public void setPaymode(String paymode){
            TextView mGroupname = (TextView)mView.findViewById(R.id.grdpaymode);
            mGroupname.setText(paymode);
        }

        public void setAdvcgvn(final String advcgvn){
            TextView mGrouname = (TextView) mView.findViewById(R.id.grdadvancegiven);
            mGrouname.setText("Kshs. "+advcgvn);
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
