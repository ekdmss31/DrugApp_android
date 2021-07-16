package com.example.androidlogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecomMyAdapter extends RecyclerView.Adapter<RecomMyAdapter.MyViewHolder> {
    private ArrayList<Recom> mList;
    private LayoutInflater mInflate;
    private Context mContext;


    RecomMyAdapter(Context context, ArrayList<Recom> mList) {
        this.mList = mList;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.recom_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        //최초 view에 대한 list item에 대한 view를 생성함.
        //이 onBindViewHolder친구한테 실질적으로 매칭해주는 역할을 함.
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .load(mList.get(position).getImage())
                .error(R.drawable.ic_cancel_24)
                .into(holder.re_image);

        holder.re_name.setText(mList.get(position).getName());
        holder.re_company.setText(mList.get(position).getCompany());
        holder.re_effect.setText(mList.get(position).getSymptom());

    }

    @Override
    public int getItemCount() {
        return (mList != null ? mList.size() : 0);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView re_name;
        public TextView re_company;
        public TextView re_effect;
        public ImageView re_image;
        public View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            re_name = itemView.findViewById(R.id.re_name);
            re_company = itemView.findViewById(R.id.re_company);
            re_effect = itemView.findViewById(R.id.re_effect);
            re_image = itemView.findViewById(R.id.re_image);
        }
    }
}