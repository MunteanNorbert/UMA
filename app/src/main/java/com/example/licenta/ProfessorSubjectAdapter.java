package com.example.licenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfessorSubjectAdapter extends RecyclerView.Adapter<ProfessorSubjectAdapter.ProfessorSubjectViewHolder> {
    final ArrayList<Subject> sSubjectList;

    public static class ProfessorSubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView sSubject;
        public TextView sYear;

        public ProfessorSubjectViewHolder(View itemView) {
            super(itemView);
            sSubject = itemView.findViewById(R.id.tv_professor_subject_name);
            sYear = itemView.findViewById(R.id.tv_professor_subject_year);
        }
    }

    public ProfessorSubjectAdapter(ArrayList<Subject> subjectList) {
        sSubjectList = subjectList;
    }

    @NonNull
    @Override
    public ProfessorSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_subject_item, parent, false);
        ProfessorSubjectViewHolder viewHolder = new ProfessorSubjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorSubjectViewHolder holder, int position) {
        Subject currentItem = sSubjectList.get(position);

        holder.sSubject.setText("Subject: " + currentItem.getName());
        holder.sYear.setText("Year: " + currentItem.getYear());
    }

    @Override
    public int getItemCount() {
        return sSubjectList.size();
    }
}

