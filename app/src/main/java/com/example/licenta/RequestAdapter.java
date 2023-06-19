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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    final ArrayList<StudentRequests> sRequestList;
    private final Context context;
    private DatabaseReference studentRef;
    private DatabaseReference professorRef;
    private DatabaseReference professorRef2;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    private String userID;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String category;

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        public TextView sRequest;
        public TextView sName;
        public Button rAcceptButton;
        public Button rDenyButton;

        public RequestViewHolder(View itemView) {
            super(itemView);
            sRequest = itemView.findViewById(R.id.tv_request_text);
            sName = itemView.findViewById(R.id.tv_studentUserNameRequest_text);
            rAcceptButton = itemView.findViewById(R.id.tv_request_accept);
            rDenyButton = itemView.findViewById(R.id.tv_request_deny);
        }
    }

    public RequestAdapter(ArrayList<StudentRequests> requestList, Context context) {
        sRequestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_request_item, parent, false);
        RequestViewHolder viewHolder = new RequestViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        userRef = FirebaseDatabase.getInstance().getReference("users");
        studentRef = FirebaseDatabase.getInstance().getReference("users");
        professorRef2 = FirebaseDatabase.getInstance().getReference("users");
        StudentRequests currentItem = sRequestList.get(position);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    firstname = snapshot.child("firstname").getValue(String.class);
                    lastname = snapshot.child("lastname").getValue(String.class);
                    username = snapshot.child("username").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        holder.sRequest.setText(currentItem.getRequest());
        holder.sName.setText(currentItem.getSelectedSubject());

        String selectedSubject = currentItem.getSelectedSubject();
        String professorUserID = currentItem.getProfessorUserID();
        String studentUserID = currentItem.getUsername();

        professorRef = FirebaseDatabase.getInstance().getReference("users")
                .child(professorUserID).child("subjectListRequests").child(selectedSubject)
                .child("Requests");

        holder.rAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to accept the request?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        userRef.child(professorUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String professorFirstName = snapshot.child("firstname").getValue(String.class);
                                    String professorLastName = snapshot.child("lastname").getValue(String.class);

                                studentRef.child(studentUserID).child("subjects").child(selectedSubject)
                                        .child("Professor").setValue(professorFirstName + " " + professorLastName);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        userRef.child(studentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                    String firstname = snapshot.child("firstname").getValue(String.class);
                                    String lastname = snapshot.child("lastname").getValue(String.class);

                                    professorRef2.child(professorUserID).child("subjectListStudents")
                                            .child(selectedSubject)
                                            .child("Students").child(studentUserID)
                                            .child("Name").setValue(firstname + " " + lastname);

                                DatabaseReference professorRef2 = professorRef.child(studentUserID);
                                professorRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        studentRef.child(studentUserID).child("notifications")
                                .child(currentDate + ", " + currentTime)
                                .setValue("Your request to join the " + selectedSubject
                                        + " subject has been approved.");

                        Intent intent = new Intent(context, SubjectRequestsActivity.class);
                        intent.putExtra("selectedSubject", selectedSubject);
                        intent.putExtra("professorUserID2", professorUserID);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("userFirstname", firstname);
                        intent.putExtra("userLastname", lastname);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("type", "requests");
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
                builder.setMessage("Are you sure you want to deny the request?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference professorRef2 = professorRef.child(studentUserID);
                        professorRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        studentRef = FirebaseDatabase.getInstance().getReference("users");
                        studentRef.child(studentUserID).child("notifications")
                                .child(currentDate + ", " + currentTime)
                                .setValue("Your request to join the " + selectedSubject
                                        + " subject has been denied.");

                        Intent intent = new Intent(context, SubjectRequestsActivity.class);
                        intent.putExtra("selectedSubject", selectedSubject);
                        intent.putExtra("professorUserID2", professorUserID);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("userFirstname", firstname);
                        intent.putExtra("userLastname", lastname);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("type", "requests");
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
    }

    @Override
    public int getItemCount() {
        return sRequestList.size();
    }
}