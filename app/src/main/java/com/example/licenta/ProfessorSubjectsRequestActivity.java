package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfessorSubjectsRequestActivity extends AppCompatActivity {


    private Button sButton;
    private Spinner sSpinner;
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference subjectsRef;

    private String username;
    private String userID;
    private String type;
    private ArrayList<String> subjectList;
    private Class Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subjects_request);

        subjectList = new ArrayList<>();

        username = getIntent().getStringExtra("professorUsername");
        userID = getIntent().getStringExtra("professorUserID");
        type = getIntent().getStringExtra("type");

        subjectsRef = FirebaseDatabase.getInstance().getReference("subjects");

        sSpinner = findViewById(R.id.spinner_professor_subject);
        sButton = findViewById(R.id.button_show_professor_subjects);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        Query subjectsQuery = subjectsRef.orderByChild("professorUserName").equalTo(username);
        subjectsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    String subjectName = subjectSnapshot.child("name").getValue(String.class);
                    subjectList.add(subjectName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ProfessorSubjectsRequestActivity.this, android.R.layout.simple_spinner_item, subjectList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.notifyDataSetChanged();
                sSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("professorUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("professorUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("type", "Grades");
                        intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                        intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, RequestListActivity.class);
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
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("professorUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSubject = String.valueOf(sSpinner.getSelectedItem());

                if(type.equals("requests")) {
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, SubjectRequestsActivity.class);
                    intent.putExtra("selectedSubject", selectedSubject);
                    intent.putExtra("professorUserID2", userID);
                    intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
                else
                if(type.equals("list") || type.equals("grades") || type.equals("attendance")){
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, StudentListsActivity.class);
                    intent.putExtra("selectedSubject", selectedSubject);
                    intent.putExtra("professorUserID2", userID);
                    intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                    intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
                else
                if(type.equals("attendanceRequest")){
                    Intent intent = new Intent(ProfessorSubjectsRequestActivity.this, ProfessorAttendanceRequestActivity.class);
                    intent.putExtra("selectedSubject", selectedSubject);
                    intent.putExtra("professorUserID2", userID);
                    intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                    intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }
        });
    }
}