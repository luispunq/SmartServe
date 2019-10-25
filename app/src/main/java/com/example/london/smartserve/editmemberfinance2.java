package com.example.london.smartserve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class editmemberfinance2 extends AppCompatActivity {
    private EditText ad,curad,loan,loanin,sav,adg,curadg,loang,loaning,savg;
    private Button b;
    private DatabaseReference md,mD1,mD2;
    private String user,grp;
    private RelativeLayout r1,r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmemberfinance);
        Bundle extras = getIntent().getExtras();
        grp = extras.getString("gkey");
        user = extras.getString("user");
        //user=getIntent().getExtras().getString("key");
        ad=findViewById(R.id.editText6);
        curad=findViewById(R.id.editText5);
        loan=findViewById(R.id.editText4);
        loanin=findViewById(R.id.editText3);
        sav=findViewById(R.id.editText2);
        adg=findViewById(R.id.editText60);
        curadg=findViewById(R.id.editText50);
        loang=findViewById(R.id.editText40);
        loaning=findViewById(R.id.editText30);
        savg=findViewById(R.id.editText20);
        r1=findViewById(R.id.rela1);
        r2=findViewById(R.id.rela2);
        b=findViewById(R.id.button9);
        md= FirebaseDatabase.getInstance().getReference().child("members").child(user);

        if (grp.equals("ga")) {
            r2.setVisibility(View.GONE);

            ad.setText("0");
            curad.setText("0");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        DatabaseReference databaseReference = md.child("project").child("savings");
                        databaseReference.child("totalsavings").setValue(sav.getText().toString());


                        DatabaseReference databaseReference2 = md;
                        databaseReference2.child("project").child("loans").child("totalloan").setValue(loan.getText().toString());
                        databaseReference2.child("project").child("loans").child("installment").setValue(loanin.getText().toString());

                        Toast.makeText(editmemberfinance2.this,user+" data edited.",Toast.LENGTH_LONG).show();

                }
            });
        }else {
            r2.setVisibility(View.VISIBLE);
            r1.setVisibility(View.GONE);

            loang.setText("0");
            loaning.setText("0");


            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = md.child("savings").child("schoolfees");
                    databaseReference.child("totalsavings").setValue(savg.getText().toString());


                    DatabaseReference databaseReference4 = md;
                    databaseReference4.child("advances").child("schoolfees").child("currentadvance").setValue(adg.getText().toString());
                    databaseReference4.child("advances").child("schoolfees").child("currpenalty").setValue(curadg.getText().toString());



                    Toast.makeText(editmemberfinance2.this,user+" data edited.",Toast.LENGTH_LONG).show();

                }
            });
        }







    }
}
