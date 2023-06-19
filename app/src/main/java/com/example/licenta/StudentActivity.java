package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity {

    private Button subjectList;
    private Button ownSubjects;
    private Button ownAttendance;
    private BottomNavigationView bottomNavigationView;
    private TextView studentWelcome;


    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    private String year;
    private String firstname;
    private String lastname;
    private String username;
    private String userID;
    private String email;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        subjectList = findViewById(R.id.subjectList);
        ownSubjects = findViewById(R.id.ownSubjects);
        ownAttendance = findViewById(R.id.ownAttendance);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        studentWelcome = findViewById(R.id.studentWelcome);

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
                    year = snapshot.child("year").getValue(String.class);
                }

                studentWelcome.setText("Welcome\n" + firstname + " !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        subjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAvailableSubjectList();
            }
        });

        ownSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOwnSubjects();
            }
        });

        ownAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOwnAttendance();
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
                    openOwnGrades();
                    return true;
                }
                if (id == R.id.action_request) {
                    openStudentRequests();
                    return true;
                }
                return false;
            }
        });
    }



    public void openAvailableSubjectList(){
        Intent intent = new Intent(this, AvailableSubjectListActivity.class);
        intent.putExtra("studentYear", year);
        intent.putExtra("studentFirstname", firstname);
        intent.putExtra("studentLastname", lastname);
        intent.putExtra("studentUserName",username);
        intent.putExtra("studentUserID",userID);
        intent.putExtra("studentUserType", category);
        intent.putExtra("studentEmail", email);
        startActivity(intent);
    }

    public void openOwnSubjects(){
        Intent intent = new Intent(this, StudentOwnSubjectsActivity.class);
        intent.putExtra("studentYear", year);
        intent.putExtra("studentFirstname", firstname);
        intent.putExtra("studentLastname", lastname);
        intent.putExtra("studentUserName",username);
        intent.putExtra("studentUserID",userID);
        intent.putExtra("studentUserType", category);
        intent.putExtra("studentEmail", email);
        startActivity(intent);
    }

    public void openOwnGrades(){
        Intent intent = new Intent(this, StudentOwnSubjectsGradesActivity.class);
        intent.putExtra("studentYear", year);
        intent.putExtra("studentFirstname", firstname);
        intent.putExtra("studentLastname", lastname);
        intent.putExtra("studentUserName",username);
        intent.putExtra("studentUserID",userID);
        intent.putExtra("studentUserType", category);
        intent.putExtra("type", "Grades");
        intent.putExtra("studentEmail", email);
        startActivity(intent);
    }

    public void openOwnAttendance(){
        Intent intent = new Intent(this, StudentOwnSubjectsGradesActivity.class);
        intent.putExtra("studentYear", year);
        intent.putExtra("studentFirstname", firstname);
        intent.putExtra("studentLastname", lastname);
        intent.putExtra("studentUserName",username);
        intent.putExtra("studentUserID",userID);
        intent.putExtra("studentUserType", category);
        intent.putExtra("type", "Attendance");
        startActivity(intent);
    }

    public void openStudentRequests(){
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, FindUserToChatActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "own");
        startActivity(intent);
    }

    public void openNotifications(){
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "own");
        startActivity(intent);
    }
}