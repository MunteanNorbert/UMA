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

public class StudentListsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private DatabaseReference usersRef;
    private ArrayList<Student> sStudents;
    private ArrayList<Student> sStudents2;
    private RecyclerView mRecyclerView;
    private StudentsAdapter mAdapter;
    private GradesAdapter gAdapter;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    private String selectedSubject;
    private String userID;
    private String type;
    private String studentUserID;
    private String studentUsername;
    private String email;
    private String year;
    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lists);

        selectedSubject = getIntent().getStringExtra("selectedSubject");
        userID = getIntent().getStringExtra("professorUserID2");
        type = getIntent().getStringExtra("type");
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        sStudents = new ArrayList<>();
        sStudents2 = new ArrayList<>();

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabase = FirebaseDatabase.getInstance().getReference("users")
                .child(userID).child("subjectListStudents").child(selectedSubject).child("Students");

        if(type.equals("list")) {
            mRecyclerView = findViewById(R.id.recycler_view_students);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new StudentsAdapter(sStudents);
            mRecyclerView.setAdapter(mAdapter);
        }
        if(type.equals("grades")){
            mRecyclerView = findViewById(R.id.recycler_view_students);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            gAdapter = new GradesAdapter(sStudents2, StudentListsActivity.this, selectedSubject, userID, "Grades");
            mRecyclerView.setAdapter(gAdapter);
        }
        if(type.equals("attendance")){
            mRecyclerView = findViewById(R.id.recycler_view_students);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            gAdapter = new GradesAdapter(sStudents2, StudentListsActivity.this, selectedSubject, userID, "Attendance");
            mRecyclerView.setAdapter(gAdapter);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot sSnapshot : snapshot.getChildren()) {
                    String student = sSnapshot.child("Name").getValue(String.class);
                    String username = sSnapshot.getKey();

                    usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                studentUserID = snapshot.getKey();
                                email = snapshot.child("email").getValue(String.class);
                                year = snapshot.child("year").getValue(String.class);
                                studentUsername = snapshot.child("username").getValue(String.class);
                                sStudents.add(new Student(student,"", email, year, studentUserID));
                                sStudents2.add(new Student(student,studentUsername, email, year, studentUserID));
                                if (type.equals("list")) {
                                    mAdapter.notifyDataSetChanged();
                                }
                                if (type.equals("grades") || type.equals("attendance")) {
                                    gAdapter.notifyDataSetChanged();
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
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    firstName = snapshot.child("firstname").getValue(String.class);
                    lastName = snapshot.child("lastname").getValue(String.class);
                    username = snapshot.child("username").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    email2 = snapshot.child("email").getValue(String.class);
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
                    Intent intent = new Intent(StudentListsActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentListsActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email2);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(StudentListsActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email2);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(StudentListsActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", firstName);
                        intent.putExtra("studentLastname", lastName);
                        intent.putExtra("studentUserID", studentUserID);
                        intent.putExtra("studentUserType", category);
                        intent.putExtra("studentEmail", email2);
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(StudentListsActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("professorUserID", studentUserID);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email2);
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(StudentListsActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userName", username);
                        intent.putExtra("userEmail", email2);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(StudentListsActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email2);
                        intent.putExtra("userName", username);
                        intent.putExtra("type", "own");
                        startActivity(intent);
                    }
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(StudentListsActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userName", username);
                    intent.putExtra("userEmail", email2);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}
