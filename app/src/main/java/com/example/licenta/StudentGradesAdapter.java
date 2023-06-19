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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentGradesAdapter extends RecyclerView.Adapter<StudentGradesAdapter.StudentGradesViewHolder> {
    final ArrayList<Subject> sSubjectList;
    private final Context context;
    private String studentUserID;
    private String type;
    private DatabaseReference notificationsRef;
    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email;
    DatabaseReference userRef;

    public static class StudentGradesViewHolder extends RecyclerView.ViewHolder {
        public TextView sSubject;
        public TextView sProfessor;
        public Button sGradesButton;

        public StudentGradesViewHolder(View itemView) {
            super(itemView);
            sSubject = itemView.findViewById(R.id.tv_grades_subject_name);
            sProfessor = itemView.findViewById(R.id.tv_grades_subject_professor_name);
            sGradesButton = itemView.findViewById(R.id.tv_see_grades);
        }
    }

    public StudentGradesAdapter(ArrayList<Subject> subjectList, Context context, String studentUserID, String type) {
        sSubjectList = subjectList;
        this.context = context;
        this.studentUserID = studentUserID;
        this.type = type;
    }

    @NonNull
    @Override
    public StudentGradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grades_item, parent, false);
        StudentGradesViewHolder viewHolder = new StudentGradesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentGradesViewHolder holder, int position) {
        Subject currentItem = sSubjectList.get(position);

        notificationsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(studentUserID).child("notifications");

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child(studentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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

        if(!type.equals("OK")) {
            holder.sSubject.setText("Subject: " + currentItem.getName());
            holder.sProfessor.setText("Professor: " + currentItem.getYear());
        } else {
            holder.sSubject.setText(currentItem.getName());
            holder.sProfessor.setText("Date: " + currentItem.getYear().replace(",","."));
        }

        holder.sGradesButton.setText(type);

        if(type.equals("Grades")) {
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, StudentOwnGradesActivity.class);
                    intent.putExtra("studentUserID", studentUserID);
                    intent.putExtra("studentSubject", currentItem.getName());
                    intent.putExtra("professorName", currentItem.getYear());
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("type", "own");
                    context.startActivity(intent);
                }
            });
        }

        if (type.equals("Attendance")) {
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StudentSubjectAttendanceActivity.class);
                    intent.putExtra("studentUserID", studentUserID);
                    intent.putExtra("studentSubject", currentItem.getName());
                    intent.putExtra("professorName", currentItem.getYear());
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("type", "own");
                    context.startActivity(intent);
                }
            });
        }

        if (type.equals("OK")) {
            holder.sGradesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference notificationsRef2 = notificationsRef.child(currentItem.getYear());
                    notificationsRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                    Intent intent = new Intent(context, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userName", username);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sSubjectList.size();
    }
}

