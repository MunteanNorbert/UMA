package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfessorSubjectAttendanceRequestActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private ArrayList<StudentAttendance> sRequestAttendanceList;
    private RecyclerView mRecyclerView;
    private AttendanceAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    private String selectedSubject;
    private String userID;
    private String type;
    private String type2;

    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subject_attendance_request);

        selectedSubject = getIntent().getStringExtra("selectedSubject");
        userID = getIntent().getStringExtra("professorUserID2");
        type = getIntent().getStringExtra("type");
        type2 = getIntent().getStringExtra("type2");

        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        sRequestAttendanceList = new ArrayList<StudentAttendance>();
        mRecyclerView = findViewById(R.id.recycler_view_attendance_requests);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AttendanceAdapter(sRequestAttendanceList, ProfessorSubjectAttendanceRequestActivity.this, userID, selectedSubject, type, type2);
        mRecyclerView.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userID)
                .child("subjectListRequests").child(selectedSubject).child("AttendanceRequests");

        if(type.equals("LectureAttendanceRequest")){
            mDatabase.child("LectureAttendanceRequest").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot attendanceSnapshot : snapshot.getChildren()) {
                        if (attendanceSnapshot.exists()) {
                            String userID = attendanceSnapshot.getKey();
                            mDatabase.child("LectureAttendanceRequest").child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {
                                                String date = dataSnapshot.child("Date").getValue(String.class);
                                                String time = dataSnapshot.child("Time").getValue(String.class);
                                                String message = dataSnapshot.child("Message").getValue(String.class);
                                                sRequestAttendanceList.add(new StudentAttendance(message, date, time, userID));
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        if(type.equals("LaboratoryAttendanceRequest")){
            mDatabase.child("LaboratoryAttendanceRequest").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot attendanceSnapshot : snapshot.getChildren()) {
                        if (attendanceSnapshot.exists()) {
                            String userID = attendanceSnapshot.getKey();
                            mDatabase.child("LaboratoryAttendanceRequest").child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {
                                            String date = dataSnapshot.child("Date").getValue(String.class);
                                            String time = dataSnapshot.child("Time").getValue(String.class);
                                            String message = dataSnapshot.child("Message").getValue(String.class);
                                            sRequestAttendanceList.add(new StudentAttendance(message, date, time, userID));
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
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

        bottomNavigationView.setSelectedItemId(R.id.action_grades);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(category.equals("student")){
                    Activity = StudentActivity.class;
                }
                if(category.equals("professor")){
                    Activity = ProfessorActivity.class;
                }
                if(category.equals("admin")){
                    Activity = AdminActivity.class;
                }
                if(category.equals("employee")){
                    Activity = EmployeeActivity.class;
                }

                int id = item.getItemId();
                if (id == R.id.action_home) {
                    Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", firstName);
                        intent.putExtra("studentLastname", lastName);
                        intent.putExtra("userID", userID);
                        intent.putExtra("studentUserType", category);
                        intent.putExtra("studentEmail", email);
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("professorUserID", userID);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",userID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",userID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        intent.putExtra("type", "own");
                        startActivity(intent);
                    }
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(ProfessorSubjectAttendanceRequestActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userName", username);
                    intent.putExtra("userEmail", email);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}