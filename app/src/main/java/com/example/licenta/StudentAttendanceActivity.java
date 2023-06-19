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

public class StudentAttendanceActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private ArrayList<Student> sStudents;
    private RecyclerView mRecyclerView;
    private StudentsAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;
    private String studentSubject;
    private String studentUserID;
    private String attendanceType;
    private String userType;
    private String professorUserID;
    private String studentName;
    private String studentUsername;


    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        studentUserID = getIntent().getStringExtra("studentUserID");
        professorUserID = getIntent().getStringExtra("professorUserID");
        studentName = getIntent().getStringExtra("studentName");
        studentSubject = getIntent().getStringExtra("studentSubject");
        studentUsername = getIntent().getStringExtra("studentUsername");
        attendanceType = getIntent().getStringExtra("attendanceType");
        userType = getIntent().getStringExtra("userType");

        sStudents = new ArrayList<Student>();

        mRecyclerView = findViewById(R.id.recycler_view_student_attendance);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StudentsAdapter(sStudents);
        mRecyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        if(userType.equals("professor")) {
            mDatabase = FirebaseDatabase.getInstance().getReference("users")
                .child(professorUserID).child("subjectListStudents")
                .child(studentSubject).child("Students")
                .child(studentUserID).child("Attendance")
                .child(attendanceType);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        String date = dateSnapshot.child("Date").getValue(String.class);
                        String time = dateSnapshot.child("Time").getValue(String.class);
                        sStudents.add(new Student(studentName,"Attendance", time, date, studentName));
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

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
        }
        if(userType.equals("student")){
            mDatabase = FirebaseDatabase.getInstance().getReference("users")
                    .child(studentUserID).child("subjects")
                    .child(studentSubject).child("Attendance").child(attendanceType);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        String date = dateSnapshot.child("Date").getValue(String.class);
                        String time = dateSnapshot.child("Time").getValue(String.class);
                        sStudents.add(new Student(studentName,"Attendance", time, date, studentName));
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

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
        }

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
                    Intent intent = new Intent(StudentAttendanceActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentAttendanceActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(StudentAttendanceActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", firstName);
                        intent.putExtra("studentLastname", lastName);
                        intent.putExtra("studentUserID", studentUserID);
                        intent.putExtra("studentUserType", category);
                        intent.putExtra("studentEmail", email);
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("professorUserID", studentUserID);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        intent.putExtra("type", "own");
                        startActivity(intent);
                    }
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(StudentAttendanceActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}