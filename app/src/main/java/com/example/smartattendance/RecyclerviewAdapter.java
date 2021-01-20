package com.example.smartattendance;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.viewHolder> {

    ArrayList<DataModel> dataHolder;

    public RecyclerviewAdapter(ArrayList<DataModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final DataModel temp=dataHolder.get(position);

        holder.img.setImageResource(dataHolder.get(position).getImg());
        holder.cname.setText(dataHolder.get(position).getCname());

//        holder.s_total.setText(dataHolder.get(position).getStotal());
//        holder.t_total.setText(dataHolder.get(position).getTtotal());
//        holder.sub_total.setText(dataHolder.get(position).getSubtotal()
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),ClassPopup.class);
                intent.putExtra("ClassName",temp.getCname());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView cname, s_total, t_total, sub_total;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.class_icon);
            cname = itemView.findViewById(R.id.classes_name);
            t_total = itemView.findViewById(R.id.total_teachers);
            s_total = itemView.findViewById(R.id.total_students);
            sub_total = itemView.findViewById(R.id.total_subjects);


        }
    }
}
