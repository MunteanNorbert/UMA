package com.example.licenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    final ArrayList<Subject> sSubjectList;

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView sSubject;
        public TextView sYear;
        public TextView sProfessorName;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            sSubject = itemView.findViewById(R.id.tv_subject_name);
            sYear = itemView.findViewById(R.id.tv_subject_year);
            sProfessorName = itemView.findViewById(R.id.tv_subject_professor_name);
        }
    }

    public SubjectAdapter(ArrayList<Subject> subjectList) {
        sSubjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        SubjectViewHolder viewHolder = new SubjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject currentItem = sSubjectList.get(position);

        holder.sSubject.setText("Subject: " + currentItem.getName());
        holder.sYear.setText("Year: " + currentItem.getYear());
        holder.sProfessorName.setText("Name: " + currentItem.getProfessorFirstName() + " " + currentItem.getProfessorLastName());
    }

    @Override
    public int getItemCount() {
        return sSubjectList.size();
    }
}

