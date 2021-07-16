package com.example.androidlogin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SideEffectAdapter extends RecyclerView.Adapter<SideEffectAdapter.MyViewHolder>{
    private String drugString;
    private ArrayList<SideDrug> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    private String data = null;
    private Intent intent;
    private String searchString;

    SideEffectAdapter(Context context, ArrayList<SideDrug> mList) {//생성자를 context와 배열로 초기화해줌
        this.mList = mList;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public SideEffectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.side_effect_item, parent, false);
        final SideEffectAdapter.MyViewHolder viewHolder = new SideEffectAdapter.MyViewHolder(view);

        //최초 view에 대한 list item에 대한 view를 생성함.
        //이 onBindViewHolder친구한테 실질적으로 매칭해주는 역할을 함.
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SideEffectAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(mList.get(position).getSide_image()).error(R.drawable.ic_cancel_24)
                .into(holder.side_image);
        holder.side_drugName.setText(mList.get(position).getSide_drugName());
        holder.side_company.setText(mList.get(position).getSide_company());
        holder.side_className.setText(mList.get(position).getSide_className());
        holder.side_ingr.setText(mList.get(position).getSide_ingredient());
        holder.side_detail.setText(mList.get(position).getSide_detail());
        holder.side_age.setText(mList.get(position).getAge());
        holder.side_ageDetail.setText(mList.get(position).getAgeDetail());
    }
    @Override
    public int getItemCount() {
        return (mList != null ? mList.size() : 0);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView side_image;
        public TextView side_drugName;
        public TextView side_company;
        public TextView side_className;
        public TextView side_ingr;
        public TextView side_detail;
        public TextView side_age;
        public TextView side_ageDetail;
        public View sideView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sideView = itemView;
            side_image = itemView.findViewById(R.id.side_image);
            side_drugName = itemView.findViewById(R.id.side_drugName);
            side_company = itemView.findViewById(R.id.side_company);
            side_className = itemView.findViewById(R.id.side_className);
            side_ingr = itemView.findViewById(R.id.side_ingr);
            side_detail = itemView.findViewById(R.id.side_detail);
            side_age = itemView.findViewById(R.id.side_age);
            side_ageDetail = itemView.findViewById(R.id.side_ageDetail);
        }
    }
}