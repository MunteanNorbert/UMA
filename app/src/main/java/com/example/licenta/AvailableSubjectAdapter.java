package com.example.licenta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AvailableSubjectAdapter extends RecyclerView.Adapter<AvailableSubjectAdapter.AvailableSubjectViewHolder> {
    final ArrayList<Subject> aSubjectList;
    private final Context context;
    private String firstname;
    private String lastname;
    private String username;
    private DatabaseReference professorRef;
    private DatabaseReference professorRef2;
    private DatabaseReference professorRef3;
    private DatabaseReference subjectRef;


    public static class AvailableSubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView aSubject;
        public TextView aYear;
        public TextView aProfessorName;
        public Button aRequestButton;

        public AvailableSubjectViewHolder(View itemView) {
            super(itemView);
            aSubject = itemView.findViewById(R.id.tv_subject_name);
            aYear = itemView.findViewById(R.id.tv_subject_year);
            aProfessorName = itemView.findViewById(R.id.tv_subject_professor_name);
            aRequestButton = itemView.findViewById(R.id.tv_subject_request);
        }
    }

    public AvailableSubjectAdapter(ArrayList<Subject> subjectList, Context context, String firstname, String lastname, String username) {
        aSubjectList = subjectList;
        this.context = context;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }

    @NonNull
    @Override
    public AvailableSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_subject_item, parent, false);
        AvailableSubjectViewHolder viewHolder = new AvailableSubjectViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableSubjectViewHolder holder, int position) {
        Subject currentItem = aSubjectList.get(position);

        professorRef = FirebaseDatabase.getInstance().getReference("users");
        professorRef2 = FirebaseDatabase.getInstance().getReference("users");
        subjectRef = FirebaseDatabase.getInstance().getReference("subjects");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());


        String subject = currentItem.getName();

        holder.aSubject.setText("Subject: " + currentItem.getName());
        holder.aYear.setText("Year: " + currentItem.getYear());
        holder.aProfessorName.setText("Professor: " + currentItem.getProfessorFirstName() + " " + currentItem.getProfessorLastName());
        holder.aRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to request access to the " + currentItem.getName() + " subject?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = subjectRef.orderByChild("name").equalTo(subject);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                        String professorUsername = subjectSnapshot.child("professorUserName").getValue(String.class);
                                        professorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                List<String> professorUserName = new ArrayList<>();
                                                for (DataSnapshot usernameSnapshot : snapshot.getChildren()) {
                                                    String username = usernameSnapshot.child("username").getValue(String.class);
                                                    professorUserName.add(username);
                                                }

                                                if (professorUserName.contains(professorUsername)) {
                                                    Query query = professorRef2.orderByChild("username").equalTo(professorUsername);
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                professorRef3 = FirebaseDatabase.getInstance().getReference("users");
                                                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                                    professorRef3.child(userSnapshot.getKey())
                                                                            .child("subjectListRequests")
                                                                            .child(currentItem.getName())
                                                                            .child("Requests").child(FirebaseAuth.getInstance().getUid())
                                                                            .setValue(firstname + " " + lastname + " would like to join your class.");

                                                                    professorRef3.child(userSnapshot.getKey()).child("notifications")
                                                                            .child(currentDate + ", " + currentTime)
                                                                            .setValue(firstname + " " + lastname + " would like to join your "
                                                                                    + currentItem.getName() + " class.");
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

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
        return aSubjectList.size();
    }
}