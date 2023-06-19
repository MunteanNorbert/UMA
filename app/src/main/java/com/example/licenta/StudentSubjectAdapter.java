package com.example.licenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentSubjectAdapter extends RecyclerView.Adapter<StudentSubjectAdapter.StudentSubjectViewHolder> {
    final ArrayList<Subject> sSubjectList;

    public static class StudentSubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView sSubject;
        public TextView sYear;

        public StudentSubjectViewHolder(View itemView) {
            super(itemView);
            sSubject = itemView.findViewById(R.id.tv_professor_subject_name);
            sYear = itemView.findViewById(R.id.tv_professor_subject_year);
        }
    }

    public StudentSubjectAdapter(ArrayList<Subject> subjectList) {
        sSubjectList = subjectList;
    }

    @NonNull
    @Override
    public StudentSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_subject_item, parent, false);
        StudentSubjectViewHolder viewHolder = new StudentSubjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentSubjectViewHolder holder, int position) {
        Subject currentItem = sSubjectList.get(position);

        holder.sSubject.setText("Subject: " + currentItem.getName());
        holder.sYear.setText("Professor: " + currentItem.getYear());
    }

    @Override
    public int getItemCount() {
        return sSubjectList.size();
    }
}

