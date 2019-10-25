package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class employeelistadapter2 extends FirebaseRecyclerAdapter<employee,employeesholder>{

    private Context context;


    public employeelistadapter2(Class<employee> modelClass, int modelLayout, Class<employeesholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(employeesholder viewHolder, employee model, int position) {
        final String key=getRef(position).getKey();
        
        viewHolder.empname.setText(model.getName());
        Glide.with(context).load(model.getImage()).into(viewHolder.empimage);
        viewHolder.empstatus.setText(model.getStatus());

        if (model.getStatus().equals("Not Engaged")){
            viewHolder.empstatus.setTextColor(RED);
        }else{
            viewHolder.empstatus.setTextColor(GREEN);
        }

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Options")
                        .setCancelable(true)
                        .setItems(R.array.Options32, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent usewinde = new Intent(context,emloyeelist.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("test", "back");
                                        extras.putString("user", key);
                                        usewinde.putExtras(extras);
                                        context.startActivity(usewinde);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        Intent userwinde2 = new Intent(context,emloyeelist.class);
                                        Bundle extras1 = new Bundle();
                                        extras1.putString("test", "to");
                                        extras1.putString("user", key);
                                        userwinde2.putExtras(extras1);
                                        context.startActivity(userwinde2);
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

    }
}
