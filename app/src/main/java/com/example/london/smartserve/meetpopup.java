package com.example.london.smartserve;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class meetpopup extends AppCompatActivity {
    private String userkey=null;
    private ImageView mPopImage;
    private TextView mPopname,mPopcah,mpOp;
    private Button mPopedituser;
    private String user_name=null,cash=null,grpname=null,grpimage=null,grp;
    private ProgressDialog mDialog;
    private View mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetpopup);
        mDialog=new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        grp = extras.getString("groupid");
        grpname = extras.getString("group");
        grpimage=extras.getString("groupimg");
        user_name=extras.getString("officer");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mview=findViewById(R.id.popup);

        int width=dm.widthPixels;
        int height=dm.heightPixels;


        getWindow().setLayout((int)(width),(int)(height*0.5));
        mPopImage=(ImageView)findViewById(R.id.imageView9);
        mPopname=(TextView)findViewById(R.id.popupname);
        mpOp=findViewById(R.id.popupengaged);
        mPopcah=(TextView)findViewById(R.id.popupcash);
        mPopedituser=(Button)findViewById(R.id.modifyuser);

        mPopname.setText(grpname);
        mPopcah.setText("Officer Name: "+user_name);
        mpOp.setText("in session");
        Picasso.with(meetpopup.this).load(grpimage).into(mPopImage);


        mPopedituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usermodify = new Intent(meetpopup.this,report1.class);
                usermodify.putExtra("key",grp);
                startActivity(usermodify);
            }
        });


    }
}
