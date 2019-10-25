package com.example.london.smartserve;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class secmpesarcpts extends AppCompatActivity {
    private EditText mFor;
    private TextView mAmount,mFrom,mOfficer,mDate;
    private DatabaseReference databaseReference,mD;
    private Button mProcess,mPrint;
    private String grp=null,meetid=null,unow=null,gname=null;
    private Double tot=0.0;
    private RelativeLayout relativeLayout;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesarcpts);
        Bundle extras = getIntent().getExtras();
        grp = extras.getString("key");
        unow = extras.getString("user");
        //meetid = extras.getString("meet");
        mFor=findViewById(R.id.textView334);
        mOfficer=findViewById(R.id.rctofficer);
        mDate=findViewById(R.id.rctdate);
        mAmount=findViewById(R.id.textView333);
        mFrom=findViewById(R.id.textView33);
        mProcess=findViewById(R.id.process);
        mProcess=findViewById(R.id.print);
        mD=FirebaseDatabase.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Receipts").child(grp).child("secretary").child("Mpesa").child(unow);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFrom.setText(dataSnapshot.child("name").getValue().toString());
                mDate.setText(dataSnapshot.child("date").getValue().toString());
                mOfficer.setText(dataSnapshot.child("officer").getValue().toString());
                mFor.setText(dataSnapshot.child("rsn").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mD.child("details").child(grp).child("groupdetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gname=dataSnapshot.child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String amt=snapshot.child("amount").getValue().toString();
                    Double am=Double.parseDouble(amt);
                    tot=tot+am;
                    mAmount.setText(String.valueOf(tot));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen();
                Intent bac = new Intent(secmpesarcpts.this,secretary.class);
                startActivity(bac);
                finish();
            }
        });
    }
    private void screen() {
        // get view group using reference
        RelativeLayout rlyy=findViewById(R.id.rly2);

        Bitmap bitmap = getBitmapFromView(rlyy,rlyy.getHeight(),rlyy.getWidth());
        Canvas can=new Canvas(bitmap);
        can.drawColor(Color.WHITE);

        Paint paint = new Paint();
        can.drawBitmap(bitmap,0f,0f,paint);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        try {
            String folder = Environment.getExternalStorageDirectory()+"/SmartServe";
            File f = new File(folder,"/MpesaReceipts/"+gname);
            File fil = new File(f, mFrom.getText().toString()+".jpg");
            if (!f.exists()){
                if (f.mkdirs()) {
                    Toast.makeText(this, folder + " has been created.", Toast.LENGTH_SHORT).show();
                    try {
                        boolean sucess = fil.createNewFile();
                        if (sucess){
                            FileOutputStream fo = new FileOutputStream(fil);
                            fo.write(bytes.toByteArray());
                            //printBill();
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
                        //printBill();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(secmpesarcpts.this, "Not Saved", Toast.LENGTH_SHORT).show();
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

                PrintOrder printer = new PrintOrder();
                printer.Print(this,"USB");

                printPhoto(R.mipmap.smartservelogos);
                printCustom("Smart Serve",2,1);
                printCustom("Capital",2,1);
                printCustom("'For your Financial Needs'",0,1);
                printCustom("Thika Business Center 4th Fl, Rm 11",0,1);
                printCustom("Contact: 0723790644",0,1);
                String dateTime[] = getDateTime();
                printText(leftRightAlign(dateTime[0], dateTime[1]));
                printNewLine();
                printCustom("MPESA RECIEPT",1,1);
                printNewLine();
                printText(leftRightAlign("Received from" , mFrom.getText().toString()));
                printNewLine();
                printText(leftRightAlign("Amount" , mAmount.getText().toString()));
                printNewLine();
                printText(leftRightAlign("Payment For" , mFor.getText().toString()));
                printNewLine();

                printCustom("Officer: Secretary ",0,2);

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
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
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
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
            ans = str1 + new String(new char[n]).replace("\0", " ")+ "      " + str2;
        }else {
            ans = str1 + "      " + str2;
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
