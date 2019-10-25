package com.example.london.smartserve;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class meetingreceipt extends AppCompatActivity {
    private TextView mDate,mGroupname,mLSF,mRegfee,mOther,mloannadv,mFinespen,mLoanproc,
            mCollcash,mAdvgvn,mLoanGivn,mTotagivn,mNetcash,mOffcer,mSF,mPesa;
    private DatabaseReference mDatabase;
    private String grpkey=null,meetid=null,date=null,rdate,rgname,rlfsfcm,SF,rregfees,rother,rloanandadv,rloansfee,rfinesandpens,rcollcash,radvgive,rloangvn,rtotpay,rnetcash,rofficer,rmpesa;
    private ByteArrayOutputStream bytes;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingreceipt);
        Bundle extras = getIntent().getExtras();
        grpkey = extras.getString("key");
        meetid = extras.getString("meet");
        mDate=findViewById(R.id.rctdate);
        mGroupname=findViewById(R.id.rctgrpname);
        mLSF=findViewById(R.id.allgroups);
        mRegfee=findViewById(R.id.type1grp);
        mOther=findViewById(R.id.type2grp);
        mSF=findViewById(R.id.nettsf);
        mloannadv=findViewById(R.id.type3grp);
        mFinespen=findViewById(R.id.type4grp);
        mPesa=findViewById(R.id.type4grpe);
        mLoanproc=findViewById(R.id.type5grp);
        mCollcash=findViewById(R.id.type6grp);
        mLoanGivn=findViewById(R.id.totalloans);
        mAdvgvn=findViewById(R.id.totalsavings);
        mTotagivn=findViewById(R.id.masadvn);
        mNetcash=findViewById(R.id.nett);
        mOffcer=findViewById(R.id.offname);
        Button head=findViewById(R.id.print);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Receipts").child(grpkey).child(meetid).child("MeetingTransferReciept");
        //mDatabase.keepSynced(true);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDate.setText(dataSnapshot.child("date").getValue().toString());
                mGroupname.setText(dataSnapshot.child("groupname").getValue().toString());
                mLSF.setText(dataSnapshot.child("lsfcm").getValue().toString());
                mRegfee.setText(dataSnapshot.child("regfees").getValue().toString());
                mOther.setText(dataSnapshot.child("other").getValue().toString());
                mloannadv.setText(dataSnapshot.child("loanandadv").getValue().toString());
                mFinespen.setText(dataSnapshot.child("finesandpenalties").getValue().toString());
                mLoanproc.setText(dataSnapshot.child("loanprocfee").getValue().toString());
                mCollcash.setText(dataSnapshot.child("collcash").getValue().toString());
                mAdvgvn.setText(dataSnapshot.child("advgive").getValue().toString());
                mLoanGivn.setText(dataSnapshot.child("loangvn").getValue().toString());
                mTotagivn.setText(dataSnapshot.child("totapaymnt").getValue().toString());
                mNetcash.setText(dataSnapshot.child("netcash").getValue().toString());
                mSF.setText(dataSnapshot.child("sfees").getValue().toString());
                mOffcer.setText(dataSnapshot.child("officer").getValue().toString());
                mPesa.setText(dataSnapshot.child("mpesa").getValue().toString());

                rdate=dataSnapshot.child("date").getValue().toString();
                rgname=dataSnapshot.child("groupname").getValue().toString();
                rlfsfcm=dataSnapshot.child("lsfcm").getValue().toString();
                rregfees=dataSnapshot.child("regfees").getValue().toString();
                rother=dataSnapshot.child("other").getValue().toString();
                rloanandadv=dataSnapshot.child("loanandadv").getValue().toString();
                rfinesandpens=dataSnapshot.child("finesandpenalties").getValue().toString();
                rloansfee=dataSnapshot.child("loanprocfee").getValue().toString();
                rcollcash=dataSnapshot.child("collcash").getValue().toString();
                radvgive=dataSnapshot.child("advgive").getValue().toString();
                rloangvn=dataSnapshot.child("loangvn").getValue().toString();
                rtotpay=dataSnapshot.child("totapaymnt").getValue().toString();
                rnetcash=dataSnapshot.child("netcash").getValue().toString();
                rofficer=dataSnapshot.child("officer").getValue().toString();
                rmpesa=dataSnapshot.child("mpesa").getValue().toString();
                SF=dataSnapshot.child("sfees").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout=findViewById(R.id.supportheader);
                relativeLayout.setVisibility(View.GONE);
                screen();
            }
        });


    }
    private void screen() {
        try {
            // get view group using reference
            ScrollView rlyy=findViewById(R.id.sc);
            CardView c1=findViewById(R.id.card1);
            CardView c2=findViewById(R.id.card2);
            CardView c3=findViewById(R.id.card3);
            RelativeLayout c4=findViewById(R.id.supportheader);
            Bitmap bitt=getBitmapFromView(c4,c4.getHeight(),c4.getWidth());
            Bitmap bitmap = getBitmapFromView(c1,c1.getHeight(),c1.getWidth());
            Bitmap bitmaps = getBitmapFromView(c2,c2.getHeight(),c2.getWidth());
            Bitmap bitmapss = getBitmapFromView(c3,c3.getHeight(),c3.getWidth());

            float ff=bitmap.getHeight();
            float g=bitmaps.getHeight();

            Paint paint = new Paint();

            Bitmap b = getBitmapFromView(rlyy,rlyy.getChildAt(0).getHeight(),rlyy.getChildAt(0).getWidth());
            Canvas can=new Canvas(b);
            can.drawColor(Color.WHITE);

            can.drawBitmap(bitt,0f,0f,paint);
            can.drawBitmap(bitmap,0f,bitt.getHeight(),paint);
            can.drawBitmap(bitmaps,0f,bitt.getHeight()+ff,paint);
            can.drawBitmap(bitmapss,0f,bitt.getHeight()+ff+g,paint);

            bytes = new ByteArrayOutputStream();

            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/MeetingReceipt/"+mGroupname.getText().toString());
            File fil = new File(f, mDate.getText().toString()+".jpg");
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                            printBill();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, folder + " can't be created.", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(this, f+ " already exits.", Toast.LENGTH_SHORT).show();
                try {
                    boolean sucess  = fil.createNewFile();

                    if (sucess){
                        FileOutputStream fo = new FileOutputStream(fil);
                        fo.write(bytes.toByteArray());
                        printBill();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(meetingreceipt.this, "Not Saved", Toast.LENGTH_SHORT).show();
        }

        printBill();
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

    protected void printBill() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;
            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);
                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime[] = getDateTime();
                printText(leftRightAlign(dateTime[0], dateTime[1]));
                printNewLine();
                printCustom("Cash Transfer/Reciept Note",1,1);
                printNewLine();
                printText(leftRightAlign("Group Name" , rgname));
                printNewLine();
                printNewLine();
                printCustom("Collections",1,0);
                printNewLine();
                printText(leftRightAlign("Member Contribution" , rlfsfcm));
                printNewLine();
                printText(leftRightAlign("Reg Fees" , rregfees));
                printNewLine();
                printText(leftRightAlign("Others" , rother));
                printNewLine();
                printText(leftRightAlign("Loan/Advance Payment" , rloanandadv));
                printNewLine();
                printText(leftRightAlign("Fines and Penalties" , rfinesandpens));
                printNewLine();
                printText(leftRightAlign("Loan Processing Fees" , rloansfee));
                printNewLine();
                printText(leftRightAlign("MPESA" , rmpesa));
                printNewLine();
                printText(leftRightAlign("Total Day's Receipt" , rmpesa));
                printNewLine();
                printNewLine();
                printCustom("Payments",1,0);
                printNewLine();
                printText(leftRightAlign("Advances Given" , radvgive));
                printNewLine();
                printText(leftRightAlign("Loans Given" , rloangvn));
                printNewLine();
                printText(leftRightAlign("Total Payments" , rtotpay));
                printNewLine();
                printText(leftRightAlign("Net Cash to office" , rnetcash));
                printNewLine();
                printText(leftRightAlign("School Fees" , SF));
                printNewLine();
                printNewLine();
                printCustom("Treasurer: ____________________",0,2);
                printNewLine();
                printCustom("Officer "+rofficer,0,2);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(str1.length() <20){
            int n = (20 - str1.length());
            ans = str1 + "      " + str2;
        }else {
            ans = str1 + "  " + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = DeviceList.getSocket();
            if(btsocket != null){
                printBill();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
