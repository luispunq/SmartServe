package com.example.london.smartserve;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class expenseitemsholder extends RecyclerView.ViewHolder {
    private final RelativeLayout layout;
    final RelativeLayout.LayoutParams layoutParams;
    private TextView remove;
    View mView;

    public expenseitemsholder(View itemView) {
        super(itemView);

        mView = itemView;
        remove=itemView.findViewById(R.id.btn);
        layout =  itemView.findViewById(R.id.expitemslist);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void Layout_hide(){
        layoutParams.height=0;
        layout.setLayoutParams(layoutParams);
    }

    public void setName(String groupname){
        TextView mGroupname = (TextView)mView.findViewById(R.id.expitmname);
        mGroupname.setText(groupname);
    }
    public void setDebitac(String venue){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmdebitac);
        mGrouploc.setText(venue);
    }
    public void setCreditac(String date){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmcreditac);
        mGrouploc.setText(date);
    }
    public void setPeriod(String amount){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmdesc);
        mGrouploc.setText(amount);
    }
    public void setAmount(String amount){
        TextView mGrouploc = (TextView)mView.findViewById(R.id.expitmamt);
        mGrouploc.setText(amount);
    }
    public void viewHide(){
        remove.setVisibility(View.INVISIBLE);
    }
}
