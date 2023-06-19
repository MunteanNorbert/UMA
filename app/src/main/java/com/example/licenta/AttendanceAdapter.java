package com.example.licenta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    final ArrayList<StudentAttendance> sRequestList;
    private final Context context;
    private String professorUserID;
    private String selectedSubject;
    private String type;
    private String type2;
    private DatabaseReference studentRef;
    private DatabaseReference professorRef;

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        public TextView sRequest;
        public Button rAcceptButton;
        public Button rDenyButton;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            sRequest = itemView.findViewById(R.id.tv_attendance_request_text);
            rAcceptButton = itemView.findViewById(R.id.tv_attendance_request_accept);
            rDenyButton = itemView.findViewById(R.id.tv_attendance_request_deny);

        }
    }

    public AttendanceAdapter(ArrayList<StudentAttendance> requestList, Context context, String professorUserID, String selectedSubject, String type, String type2) {
        sRequestList = requestList;
        this.context = context;
        this.professorUserID = professorUserID;
        this.selectedSubject = selectedSubject;
        this.type = type;
        this.type2 = type2;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_request_item, parent, false);
        AttendanceViewHolder viewHolder = new AttendanceViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        StudentAttendance currentItem = sRequestList.get(position);

        holder.sRequest.setText(currentItem.getMessage());

        String date = currentItem.getDate();
        String time = currentItem.getTime();
        String studentUserID = currentItem.getStudentUserID();

        String[] parts = type2.split("(?=[A-Z])");

        String part1 = parts[0];
        String part2 = parts[1];

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        professorRef = FirebaseDatabase.getInstance().getReference("users")
                .child(professorUserID).child("subjectListRequests").child(selectedSubject)
                .child("AttendanceRequests").child(type).child(studentUserID);

        studentRef = FirebaseDatabase.getInstance().getReference("users");

        holder.rAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to accept the attendance request?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference professorRef2 = professorRef.child(date.replace(".",","));
                        professorRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        studentRef.child(studentUserID).child("subjects").child(selectedSubject)
                                .child("Attendance").child(type2).child(date.replace(".",","))
                                .child("Date").setValue(date);
                        studentRef.child(studentUserID).child("subjects").child(selectedSubject)
                                .child("Attendance").child(type2).child(date.replace(".",","))
                                .child("Time").setValue(time);
                        studentRef.child(professorUserID).child("subjectListStudents").child(selectedSubject)
                                .child("Students").child(studentUserID)
                                .child("Attendance").child(type2)
                                .child(date.replace(".",","))
                                .child("Date").setValue(date);
                        studentRef.child(professorUserID).child("subjectListStudents").child(selectedSubject)
                                .child("Students").child(studentUserID)
                                .child("Attendance").child(type2)
                                .child(date.replace(".",","))
                                .child("Time").setValue(time);

                        studentRef.child(studentUserID).child("notifications")
                                .child(currentDate + ", " + currentTime)
                                .setValue("Your " + selectedSubject + " " + part1 + " " + part2 + ", from "
                                + date + " at " + time + " o'clock has been accepted.");

                        Intent intent = new Intent(context, ProfessorSubjectAttendanceRequestActivity.class);
                        intent.putExtra("selectedSubject", selectedSubject);
                        intent.putExtra("professorUserID2", professorUserID);
                        intent.putExtra("type",type);
                        intent.putExtra("type2",type2);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.rDenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to deny the attendance request?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference professorRef2 = professorRef.child(date.replace(".",","));
                        professorRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        Intent intent = new Intent(context, ProfessorSubjectAttendanceRequestActivity.class);
                        intent.putExtra("selectedSubject", selectedSubject);
                        intent.putExtra("professorUserID2", professorUserID);
                        intent.putExtra("type",type);
                        intent.putExtra("type2",type2);
                        context.startActivity(intent);

                        studentRef.child(studentUserID).child("notifications")
                                .child(currentDate + ", " + currentTime)
                                .setValue("Your " + selectedSubject + " " + part1 + " " + part2 + ", from "
                                        + date + " at " + time + " o'clock has been denied.");
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sRequestList.size();
    }
}