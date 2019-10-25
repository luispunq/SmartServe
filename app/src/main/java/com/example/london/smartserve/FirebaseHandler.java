package com.example.london.smartserve;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Timer;
import java.util.TimerTask;

public class FirebaseHandler extends Application {
    private ProgressDialog mProgress;

    @Override
    public void onCreate() {
        super.onCreate();
        mProgress=new ProgressDialog(getApplicationContext());
        mProgress.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        final Handler hand=new Handler();
        hand.post(new Runnable() {
            @Override
            public void run() {
                mProgress.setTitle("Loading..");
                mProgress.setMessage("Please Wait Establishing Connection..");
                mProgress.setCanceledOnTouchOutside(false);
                //mProgress.show();


                DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);

                        if (connected) {
                            System.out.println("connected");
                            FirebaseDatabase.getInstance().goOnline();
                            //mProgress.dismiss();
                        } else {
                            System.out.println("not connected");
                            FirebaseDatabase.getInstance().goOnline();
                            //mProgress.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Listener was cancelled");
                        mProgress.dismiss();
                    }
                });
            }
        });


        class MyApplication extends MultiDexApplication {
            @Override
            protected void attachBaseContext(Context context) {
                super.attachBaseContext(context);
                MultiDex.install(this);
            }
        }

        MyApplication application=new MyApplication();
        application.attachBaseContext(getApplicationContext());
    }

    public void goOffline(){

    }


}

