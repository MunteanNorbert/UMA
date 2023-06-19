package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class

ProfessorActivity extends AppCompatActivity {

    private Button subjectList;
    private Button subjectRequestsList;
    private Button studentsLists;
    private Button attendanceRequestsList;
    private Button studentsAttendance;
    private BottomNavigationView bottomNavigationView;
    private TextView professorWelcome;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    private String firstname;
    private String lastname;
    private String username;
    private String userID;
    private String email;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        subjectList =  findViewById(R.id.subjects_professors);
        subjectRequestsList =  findViewById(R.id.subjects_requests_professors);
        studentsLists =  findViewById(R.id.students_of_professors);
        attendanceRequestsList = findViewById(R.id.attendance_requests_professors);
        studentsAttendance = findViewById(R.id.student_attendance);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        professorWelcome = findViewById(R.id.professorWelcome);

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

                professorWelcome.setText("Welcome\n" + firstname + " !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        subjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfessorSubjects();
            }
        });

        subjectRequestsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubjectRequests();
            }
        });

        studentsLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentLists();
            }
        });

        attendanceRequestsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openSubjectRequests2(); }
        });

        studentsAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentLists3();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    return true;
                }
                if (id == R.id.action_notifications) {
                    openNotifications();
                    return true;
                }
                if (id == R.id.action_chat) {
                    openChat();
                    return true;
                }
                if (id == R.id.action_grades) {
                    openStudentLists2();
                    return true;
                }
                if (id == R.id.action_request) {
                    openProfessorRequests();
                    return true;
                }
                return false;
            }
        });
    }

    public void openProfessorSubjects(){
        Intent intent = new Intent(this, ProfessorSubjectsListActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openSubjectRequests(){
        Intent intent = new Intent(this, ProfessorSubjectsRequestActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("professorUserID", userID);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type","requests");
        startActivity(intent);
    }

    public void openStudentLists(){
        Intent intent = new Intent(this, ProfessorSubjectsRequestActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("professorUserID", userID);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type","list");
        startActivity(intent);
    }

    public void openStudentLists2(){
        Intent intent = new Intent(this, ProfessorSubjectsRequestActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("professorUserID", userID);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type","grades");
        startActivity(intent);
    }

    public void openSubjectRequests2(){
        Intent intent = new Intent(this, ProfessorSubjectsRequestActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("professorUserID", userID);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type","attendanceRequest");
        startActivity(intent);
    }

    public void openStudentLists3(){
        Intent intent = new Intent(this, ProfessorSubjectsRequestActivity.class);
        intent.putExtra("professorUsername", username);
        intent.putExtra("professorUserID", userID);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type","attendance");
        startActivity(intent);
    }

    public void openProfessorRequests(){
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userEmail", email);
        intent.putExtra("userType", category);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, FindUserToChatActivity.class);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "own");
        startActivity(intent);
    }

    public void openNotifications(){
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.putExtra("userName", username);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "own");
        startActivity(intent);
    }
}