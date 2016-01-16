package com.avirudoi.radpad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avirudoi.radpad.R;
import com.avirudoi.radpad.listener.RecycleViewClickListener;
import com.avirudoi.radpad.module.RentObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by avirudoi on 1/13/16.*/


public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentViewAdapter> {

    Context context;
    private List<RentObject> rentList = Collections.emptyList();
    public static RecycleViewClickListener recycleViewClickListener;

    public RentAdapter(List<RentObject> rentList, RecycleViewClickListener recycleViewClickListener){

        this.rentList = rentList;
        this.recycleViewClickListener = recycleViewClickListener;
    }


    @Override
    public RentViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()). inflate(R.layout.rent_card, parent, false);

        return new RentViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(RentViewAdapter holder, int position) {

        RentObject rentView = rentList.get(position);
        holder.tvCity.setText(rentView.getCity());
        holder.tvStreet.setText(rentView.getStreet());
        holder.tvDates.setText(rentView.getFullDate());

    }

    @Override
    public int getItemCount() {
        return rentList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class RentViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView tvCity;
        protected TextView tvStreet;
        protected TextView tvDates;
        protected LinearLayout llRentCard;

        public RentViewAdapter(View v) {
            super(v);
            v.setOnClickListener(this);
            tvCity =  (TextView) v.findViewById(R.id.tvCity);
            tvStreet = (TextView)  v.findViewById(R.id.tvStreet);
            tvDates = (TextView)  v.findViewById(R.id.tvDates);
            llRentCard = (LinearLayout) v.findViewById(R.id.llRentCard);
        }

        @Override
        public void onClick(View v) {
            recycleViewClickListener.getViewClick(this.getAdapterPosition());
        }
    }

}
