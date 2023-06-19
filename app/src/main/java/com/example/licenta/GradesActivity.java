package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class GradesActivity extends AppCompatActivity {

    private String studentUserID;
    private String professorUserID;
    private String studentName;
    private String studentSubject;

    private TextView studentNames;
    private TextView subjectNames;
    private TextView currentLectureGrade;
    private TextView currentLaboratoryGrade;
    private TextView currentSubjectGrade;
    private EditText sLectureGrade;
    private EditText sLaboratoryGrade;
    private Button saveGrades;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    private DatabaseReference studentRef;
    private DatabaseReference professorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        studentUserID = getIntent().getStringExtra("studentUserID");
        studentName = getIntent().getStringExtra("studentName");
        studentSubject = getIntent().getStringExtra("studentSubject");
        professorUserID = getIntent().getStringExtra("professorUserID");

        studentNames = findViewById(R.id.student_name_value);
        subjectNames = findViewById(R.id.subject_name_value);
        currentLectureGrade = findViewById(R.id.current_lecture_grade_value);
        currentLaboratoryGrade = findViewById(R.id.current_laboratory_grade_value);
        currentSubjectGrade = findViewById(R.id.current_subject_grade_value);
        sLectureGrade = findViewById(R.id.lecture_grade_input);
        sLaboratoryGrade = findViewById(R.id.lab_grade_input);
        saveGrades = findViewById(R.id.save_grades_button);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        studentRef = FirebaseDatabase.getInstance().getReference("users").child(studentUserID)
                .child("subjects").child(studentSubject).child("Grades");
        professorRef = FirebaseDatabase.getInstance().getReference("users").child(professorUserID)
                .child("subjectListStudents").child(studentSubject).child("Students")
                .child(studentUserID).child("Grades");

        studentNames.setText(studentName);
        subjectNames.setText(studentSubject);

        professorRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(getIntent().getStringExtra("userType").equals("student")){
                    Activity = StudentActivity.class;
                }
                if(getIntent().getStringExtra("userType").equals("professor")){
                    Activity = ProfessorActivity.class;
                }
                if(getIntent().getStringExtra("userType").equals("admin")){
                    Activity = AdminActivity.class;
                }
                if(getIntent().getStringExtra("userType").equals("employee")){
                    Activity = EmployeeActivity.class;
                }

                int id = item.getItemId();
                if (id == R.id.action_home) {
                    Intent intent = new Intent(GradesActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(GradesActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("userID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(GradesActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("userID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(GradesActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(GradesActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", getIntent().getStringExtra("userNJame"));
                        intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(GradesActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(GradesActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        intent.putExtra("type", "own");
                        startActivity(intent);
                    }
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(GradesActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("userID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        saveGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStudentGrades();
            }
        });
    }
    
    

    public void saveStudentGrades(){
        String lecGrade = sLectureGrade.getText().toString().trim();
        float lectureGrade = Float.parseFloat(lecGrade);
        String labGrade = sLaboratoryGrade.getText().toString().trim();
        float laboratoryGrade = Float.parseFloat(labGrade);
        float subjectMean = (lectureGrade + laboratoryGrade) / 2;


        studentRef.child("lectureGrade").setValue(lectureGrade);
        studentRef.child("laboratoryGrade").setValue(laboratoryGrade);
        studentRef.child("subjectGrade").setValue(Math.round(subjectMean));
        professorRef.child("lectureGrade").setValue(lectureGrade);
        professorRef.child("laboratoryGrade").setValue(laboratoryGrade);
        professorRef.child("subjectGrade").setValue(Math.round(subjectMean));

        sLectureGrade.setText("");
        sLaboratoryGrade.setText("");
    }

}