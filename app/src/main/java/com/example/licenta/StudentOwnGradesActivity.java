package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentOwnGradesActivity extends AppCompatActivity {

    private String studentUserID;
    private String professorName;
    private String studentSubject;

    private String year;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String category;

    private Class Activity;

    private TextView professorNames;
    private TextView subjectNames;
    private TextView currentLectureGrade;
    private TextView currentLaboratoryGrade;
    private TextView currentSubjectGrade;
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference studentRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_own_grades);

        studentUserID = getIntent().getStringExtra("studentUserID");
        studentSubject = getIntent().getStringExtra("studentSubject");
        professorName = getIntent().getStringExtra("professorName");

        professorNames = findViewById(R.id.own_subject_professor_value);
        subjectNames = findViewById(R.id.own_subject_name_value);
        currentLectureGrade = findViewById(R.id.current_own_lecture_grade_value);
        currentLaboratoryGrade = findViewById(R.id.current_own_laboratory_grade_value);
        currentSubjectGrade = findViewById(R.id.current_own_subject_grade_value);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);


        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(studentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        studentRef = FirebaseDatabase.getInstance().getReference("users").child(studentUserID)
                .child("subjects").child(studentSubject).child("Grades");

        professorNames.setText(professorName);
        subjectNames.setText(studentSubject);


        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    float lectureGrade1 = snapshot.child("lectureGrade").getValue(Float.class);
                    float laboratoryGrade1 = snapshot.child("laboratoryGrade").getValue(Float.class);
                    float subjectGrade = snapshot.child("subjectGrade").getValue(Float.class);

                    currentLectureGrade.setText(Float.toString(lectureGrade1));
                    currentLaboratoryGrade.setText(Float.toString(laboratoryGrade1));
                    currentSubjectGrade.setText(Float.toString(Math.round(subjectGrade)));
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
                    Intent intent = new Intent(StudentOwnGradesActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentOwnGradesActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstname);
                    intent.putExtra("userLastname", lastname);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(StudentOwnGradesActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", firstname);
                    intent.putExtra("userLastname", lastname);
                    intent.putExtra("userID",studentUserID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    Intent intent = new Intent(StudentOwnGradesActivity.this, StudentOwnSubjectsGradesActivity.class);
                    intent.putExtra("studentYear", year);
                    intent.putExtra("studentFirstname", firstname);
                    intent.putExtra("studentLastname", lastname);
                    intent.putExtra("studentUserName",username);
                    intent.putExtra("studentUserID",studentUserID);
                    intent.putExtra("studentUserType", category);
                    intent.putExtra("type", "Grades");
                    intent.putExtra("studentEmail", email);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(StudentOwnGradesActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", firstname);
                    intent.putExtra("userLastname", lastname);
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