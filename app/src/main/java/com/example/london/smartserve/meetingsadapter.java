package com.example.london.smartserve;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.london.smartserve.activemeetings;
import com.example.london.smartserve.activemeetingsholder;
import com.example.london.smartserve.meetpopup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class meetingsadapter extends FirebaseRecyclerAdapter<activemeetings,activemeetingsholder>{

    private Context context;



    meetingsadapter(Class<activemeetings> modelClass, int modelLayout, Class<activemeetingsholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;

    }

    @Override
    protected void populateViewHolder(activemeetingsholder viewHolder, final activemeetings model, int position) {
        final String key=model.getGroupid();
        viewHolder.grname.setText(model.getGroupName());
        Glide.with(context).load(model.getGroupImage()).into(viewHolder.grimage);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtosignin =new Intent(context,meetpopup.class);
                Bundle extras = new Bundle();
                extras.putString("groupid", key);
                extras.putString("group", model.getGroupName());
                extras.putString("groupimg", model.getGroupImage());
                extras.putString("officer", model.getFacilitator());
                backtosignin.putExtras(extras);
                context.startActivity(backtosignin);
            }
        });

    }
}
