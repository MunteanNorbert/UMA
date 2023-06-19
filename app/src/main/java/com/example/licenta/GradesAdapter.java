package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesViewHolder> {
    final ArrayList<Student> sStudentsList;
    private final Context context;
    private String selectedSubject;
    private String professorUserID;
    private String type;


    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String category;

    private DatabaseReference userRef;


    public static class GradesViewHolder extends RecyclerView.ViewHolder {
        public TextView sName;
        public TextView sYear;
        public TextView sEmailAddress;
        public Button sGradesButton;

        public GradesViewHolder(View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.tv_student_name_grades);
            sYear = itemView.findViewById(R.id.tv_student_year_grades);
            sEmailAddress = itemView.findViewById(R.id.tv_student_email_address_grades);
            sGradesButton = itemView.findViewById(R.id.tv_grades);
        }
    }

    public GradesAdapter(ArrayList<Student> studentsList, Context context, String selectedSubject, String professorUserID, String type) {
        sStudentsList = studentsList;
        this.context = context;
        this.selectedSubject = selectedSubject;
        this.professorUserID = professorUserID;
        this.type = type;
    }

    @NonNull
    @Override
    public GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_grades_item, parent, false);
        GradesViewHolder viewHolder = new GradesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GradesViewHolder holder, int position) {
        Student currentItem = sStudentsList.get(position);

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(professorUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    firstName = snapshot.child("firstname").getValue(String.class);
                    lastName = snapshot.child("lastname").getValue(String.class);
                    username = snapshot.child("username").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if(type.equals("Grades") || type.equals("Attendance")) {
            holder.sName.setText("Name: " + currentItem.getFirstname());
            holder.sYear.setText("Year: " + currentItem.getYear());
            holder.sEmailAddress.setText("Email address: " + currentItem.getEmail());
        }

        if(type.equals("Chat")) {
            holder.sName.setText("Name: " + currentItem.getFirstname() + " " + currentItem.getLastname());
            holder.sYear.setText("Username: " + currentItem.getEmail());
            holder.sEmailAddress.setText("Role: " + currentItem.getYear().substring(0, 1).toUpperCase() + currentItem.getYear().substring(1));
        }

        holder.sGradesButton.setText(type);

        if (type.equals("Grades")){
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GradesActivity.class);
                    intent.putExtra("studentUserID", currentItem.getUserID());
                    intent.putExtra("professorUserID", professorUserID);
                    intent.putExtra("studentName", currentItem.getFirstname());
                    intent.putExtra("studentSubject", selectedSubject);
                    intent.putExtra("studentUsername", currentItem.getLastname());
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",professorUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    context.startActivity(intent);
                }
            });
        }

        if(type.equals("Attendance")) {
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AttendanceActivity.class);
                    intent.putExtra("studentUserID", currentItem.getUserID());
                    intent.putExtra("professorUserID", professorUserID);
                    intent.putExtra("studentName", currentItem.getFirstname());
                    intent.putExtra("studentSubject", selectedSubject);
                    intent.putExtra("studentUsername", currentItem.getLastname());
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",professorUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    context.startActivity(intent);
                }
            });
        }

        if(type.equals("Chat")) {
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("ownUserID",professorUserID);
                    intent.putExtra("selectedUserID", currentItem.getUserID());
                    intent.putExtra("selectedUserFirstname", currentItem.getFirstname());
                    intent.putExtra("selectedUserLastname", currentItem.getLastname());
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",professorUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sStudentsList.size();
    }
}