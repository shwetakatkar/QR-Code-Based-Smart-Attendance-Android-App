package com.example.smartattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    ArrayList<StudentDataModel> dataHolder;

    public StudentAdapter(ArrayList<StudentDataModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_view, parent, false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {

//        if (dataHolder.size() > 0) {
//            final StudentDataModel temp = dataHolder.get(position);
        holder.stname.setText(dataHolder.get(position).getStudentname());
        holder.stid.setText(dataHolder.get(position).getStudentid());
//      }
    }



    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView stname, stid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stname = (TextView) itemView.findViewById(R.id.sname);
            stid = (TextView) itemView.findViewById(R.id.sid);
        }
    }
}