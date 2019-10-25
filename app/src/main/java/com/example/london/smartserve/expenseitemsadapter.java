package com.example.london.smartserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;

public class expenseitemsadapter extends FirebaseRecyclerAdapter<expenseitemadded,expenseitemsholder>{

    private Context context;



    expenseitemsadapter(Class<expenseitemadded> modelClass, int modelLayout, Class<expenseitemsholder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);

        this.context = context;
    }

    @Override
    protected void populateViewHolder(expenseitemsholder viewHolder, final expenseitemadded model, int position) {

        viewHolder.setName(model.getName());
        viewHolder.setCreditac(model.getCreditac());
        viewHolder.setDebitac(model.getDebitac());
        viewHolder.setPeriod(model.getDescription());
        viewHolder.setAmount(model.getAmount());
        viewHolder.viewHide();
    }
}
