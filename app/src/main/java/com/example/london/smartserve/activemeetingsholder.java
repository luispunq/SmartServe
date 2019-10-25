package com.example.london.smartserve;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class activemeetingsholder extends RecyclerView.ViewHolder {
  public TextView grname;
  public ImageView grimage;
  View mView;

  public  activemeetingsholder(View itemView) {
    super(itemView);
    mView=itemView;
    grname = (TextView)itemView.findViewById(R.id.rowtitle);
    grimage = (ImageView)itemView.findViewById(R.id.rowimage);
  }
}
