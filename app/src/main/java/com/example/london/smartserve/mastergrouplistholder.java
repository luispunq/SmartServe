package com.example.london.smartserve;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mastergrouplistholder extends RecyclerView.ViewHolder {
    private static final String TAG = mastergrouplistholder.class.getSimpleName();
    public CardView layout;
    public TextView grname;
    public ImageView grimage;
    public TextView grnumbers;
    public TextView grlocation;
    public TextView grmeetdate;
    View mView;

    public mastergrouplistholder(View itemView) {
        super(itemView);
        mView=itemView;
        grname = (TextView)mView.findViewById(R.id.masgrpname);
        //grimage = (ImageView)mView.findViewById(R.id.masgrpimage);
        grnumbers=mView.findViewById(R.id.masnumbers);
        grlocation=mView.findViewById(R.id.masvenue);
        grmeetdate=mView.findViewById(R.id.masmeetingdate);


    }


}
