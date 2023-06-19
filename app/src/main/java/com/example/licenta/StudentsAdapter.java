package com.example.licenta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder> {
    final ArrayList<Student> sStudentsList;

    public static class StudentsViewHolder extends RecyclerView.ViewHolder {
        public TextView sName;
        public TextView sYear;
        public TextView sEmailAddress;

        public StudentsViewHolder(View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.tv_student_name);
            sYear = itemView.findViewById(R.id.tv_student_year);
            sEmailAddress = itemView.findViewById(R.id.tv_student_email_address);
        }
    }

    public StudentsAdapter(ArrayList<Student> studentsList) {
        sStudentsList = studentsList;
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_item, parent, false);
        StudentsViewHolder viewHolder = new StudentsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder holder, int position) {
        Student currentItem = sStudentsList.get(position);

        if(!currentItem.getLastname().equals("Attendance")) {
            holder.sName.setText("Name: " + currentItem.getFirstname());
            holder.sYear.setText("Year: " + currentItem.getYear());
            holder.sEmailAddress.setText("Email address: " + currentItem.getEmail());
        } else {
            holder.sName.setText("Name: " + currentItem.getFirstname());
            holder.sYear.setText("Date: " + currentItem.getYear());
            holder.sEmailAddress.setText("Time: " + currentItem.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return sStudentsList.size();
    }
}