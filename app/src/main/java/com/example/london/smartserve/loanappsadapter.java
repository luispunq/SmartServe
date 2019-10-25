package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class loanappsadapter extends FirebaseRecyclerAdapter<masterloanapps,loanappsholder>{

    private Context context;


    loanappsadapter(Class<masterloanapps> modelClass, int modelLayout, Class<loanappsholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(loanappsholder viewHolder, masterloanapps model, int position) {
        final String key=getRef(position).getKey();
        if (model.getStatus().equals("pending")){
            viewHolder.setPhoto(context,model.getUserid());
            viewHolder.setapplierName(model.getUserid());
            viewHolder.setapplydate(model.getRequestdate());
            viewHolder.setapplyamount(model.getAmountrequested());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userwindow = new Intent(context,loanprocessing.class);
                    userwindow.putExtra("id",key);
                    context.startActivity(userwindow);
                }
            });
        }else{
            viewHolder.Layout_hide();
        }
    }
}
