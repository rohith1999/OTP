package com.example.otp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {
    private List<ComplaintsDatabaseModel> complaintList;

    public ComplaintAdapter(List complaintList){
        this.complaintList=complaintList;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_complaint_item,parent,false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {

        ComplaintsDatabaseModel c=complaintList.get(position);
        String itemName=c.getProblemStatement();
        String itemTime=c.getTimeStamp();

        holder.singleItemName.setText(itemName);
        holder.singleItemTime.setText(itemTime);

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {

        TextView singleItemName;
        TextView singleItemTime;
        public ComplaintViewHolder(@NonNull View itemView) {

            super(itemView);
            singleItemName=itemView.findViewById(R.id.single_item_name);
            singleItemTime=itemView.findViewById(R.id.single_item_time);
        }
    }
}

