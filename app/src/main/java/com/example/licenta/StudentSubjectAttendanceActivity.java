package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StudentSubjectAttendanceActivity extends AppCompatActivity {

    private String studentUserID;
    private String studentSubject;
    private String professorName;
    private String professorUserID;
    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email;


    private Button markLectureAttendance;
    private Button markLaboratoryAttendance;
    private Button checkLectureAttendance;
    private Button checkLaboratoryAttendance;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    DatabaseReference subjectsRef;
    DatabaseReference professorRef;
    DatabaseReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_stubject_attendance);

        studentUserID = getIntent().getStringExtra("studentUserID");
        studentSubject = getIntent().getStringExtra("studentSubject");
        professorName = getIntent().getStringExtra("professorName");

        markLectureAttendance = findViewById(R.id.attendance_mark_lecture);
        markLaboratoryAttendance = findViewById(R.id.attendance_mark_laboratory);
        checkLectureAttendance = findViewById(R.id.attendance_check_lecture);
        checkLaboratoryAttendance = findViewById(R.id.attendance_check_laboratory);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        subjectsRef = FirebaseDatabase.getInstance().getReference("subjects");
        professorRef = FirebaseDatabase.getInstance().getReference("users");
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

        Query query = subjectsRef.orderByChild("name").equalTo(studentSubject);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    String professorUsername = subjectSnapshot.child("professorUserName").getValue(String.class);

                    Query query2 = professorRef.orderByChild("username").equalTo(professorUsername);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                professorUserID = userSnapshot.getKey();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        markLectureAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openLectureAttendanceRequest();
            }
        });

        markLaboratoryAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openLaboratoryAttendanceRequest();
            }
        });

        checkLectureAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openLectureAttendance();
            }
        });

        checkLaboratoryAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openLaboratoryAttendance();
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
                    Intent intent = new Intent(StudentSubjectAttendanceActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentSubjectAttendanceActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(StudentSubjectAttendanceActivity.this, FindUserToChatActivity.class);
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
                        Intent intent = new Intent(StudentSubjectAttendanceActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", firstName);
                        intent.putExtra("studentLastname", lastName);
                        intent.putExtra("studentUserID", studentUserID);
                        intent.putExtra("type", "Grades");
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(StudentSubjectAttendanceActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("professorUserID", studentUserID);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(StudentSubjectAttendanceActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",studentUserID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(StudentSubjectAttendanceActivity.this, RequestListActivity.class);
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
                    Intent intent = new Intent(StudentSubjectAttendanceActivity.this, RequestActivity.class);
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

    public void openLectureAttendanceRequest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentSubjectAttendanceActivity.this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to mark your attendance for the " +
                studentSubject +" lecture?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                String currentDate = dateFormat.format(calendar.getTime());
                String currentDate2 = dateFormat2.format(calendar.getTime());
                String currentDay = dayFormat.format(calendar.getTime());
                String currentTime = timeFormat.format(calendar.getTime());

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LectureAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Message")
                        .setValue(firstName + " " + lastName +
                                " marks their attendance for the " + studentSubject +
                                " lecture from " + currentDay + ", " + currentDate + ", at " +
                                currentTime + " o'clock!");

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LectureAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Date")
                        .setValue(currentDate);

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LectureAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Time")
                        .setValue(currentTime);

                userRef.child(professorUserID).child("notifications")
                        .child(currentDate.replace(".",",") + ", " + currentTime)
                        .setValue(firstName + " " + lastName + " marks their attendance for the "
                                + studentSubject + " lecture from " + currentDay + ", "
                                + currentDate2 + ", at " + currentTime + " o'clock!");
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

    public void openLaboratoryAttendanceRequest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentSubjectAttendanceActivity.this);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to mark your attendance for the " +
                studentSubject + " laboratory?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                String currentDate = dateFormat.format(calendar.getTime());
                String currentDate2 = dateFormat2.format(calendar.getTime());
                String currentDay = dayFormat.format(calendar.getTime());
                String currentTime = timeFormat.format(calendar.getTime());

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LaboratoryAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Message")
                        .setValue(firstName + " " + lastName +
                                " marks their attendance for the " + studentSubject +
                                " laboratory from " + currentDay + ", " + currentDate + ", at " +
                                currentTime + " o'clock!");

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LaboratoryAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Date")
                        .setValue(currentDate);

                userRef.child(professorUserID)
                        .child("subjectListRequests")
                        .child(studentSubject)
                        .child("AttendanceRequests").child("LaboratoryAttendanceRequest")
                        .child(studentUserID).child(currentDate.replace(".",",")).child("Time")
                        .setValue(currentTime);

                userRef.child(professorUserID).child("notifications")
                        .child(currentDate.replace(".",",") + ", " + currentTime)
                        .setValue(firstName + " " + lastName + " marks their attendance for the "
                                + studentSubject + " laboratory from " + currentDay + ", "
                                + currentDate2 + ", at " + currentTime + " o'clock!");
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

    public void openLectureAttendance(){
        Intent intent = new Intent(this, StudentAttendanceActivity.class);
        intent.putExtra("studentUserID", studentUserID);
        intent.putExtra("professorUserID", professorUserID);
        intent.putExtra("studentName", firstName + " " + lastName);
        intent.putExtra("studentSubject", studentSubject);
        intent.putExtra("studentUsername",username);
        intent.putExtra("attendanceType","LectureAttendance");
        intent.putExtra("userType","student");
        startActivity(intent);
    }

    public void openLaboratoryAttendance(){
        Intent intent = new Intent(this, StudentAttendanceActivity.class);
        intent.putExtra("studentUserID", studentUserID);
        intent.putExtra("professorUserID", professorUserID);
        intent.putExtra("studentName", firstName + " " + lastName);
        intent.putExtra("studentSubject", studentSubject);
        intent.putExtra("studentUsername",username);
        intent.putExtra("attendanceType","LaboratoryAttendance");
        intent.putExtra("userType","student");
        startActivity(intent);
    }
}