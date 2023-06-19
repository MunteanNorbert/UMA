package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentOwnSubjectsGradesActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<Subject> aSubjectList;
    private RecyclerView mRecyclerView;
    private StudentGradesAdapter mAdapter;
    private String userID;
    private String year;
    private String type;
    private BottomNavigationView bottomNavigationView;

    private Class Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_own_subjects_grades);

        userID = getIntent().getStringExtra("studentUserID");
        year = getIntent().getStringExtra("studentYear");
        type = getIntent().getStringExtra("type");

        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        aSubjectList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view_grades);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StudentGradesAdapter(aSubjectList, StudentOwnSubjectsGradesActivity.this, userID, type);
        mRecyclerView.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("users")
                .child(userID).child("subjects");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    String subjectName = subjectSnapshot.getKey();
                    String professorName = subjectSnapshot.child("Professor").getValue(String.class);
                    aSubjectList.add(new Subject(subjectName, professorName, "", "",""));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentOwnSubjectsGradesActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_grades);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(getIntent().getStringExtra("studentUserType").equals("student")){
                    Activity = StudentActivity.class;
                }
                if(getIntent().getStringExtra("studentUserType").equals("professor")){
                    Activity = ProfessorActivity.class;
                }
                if(getIntent().getStringExtra("studentUserType").equals("admin")){
                    Activity = AdminActivity.class;
                }
                if(getIntent().getStringExtra("studentUserType").equals("employee")){
                    Activity = EmployeeActivity.class;
                }

                int id = item.getItemId();
                if (id == R.id.action_home) {

                    Intent intent = new Intent(StudentOwnSubjectsGradesActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentOwnSubjectsGradesActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(StudentOwnSubjectsGradesActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    Intent intent = new Intent(StudentOwnSubjectsGradesActivity.this, StudentOwnSubjectsGradesActivity.class);
                    intent.putExtra("studentYear", year);
                    intent.putExtra("studentFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("studentLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("studentUserName", getIntent().getStringExtra("studentUserName"));
                    intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("studentUserID",userID);
                    intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("type", "Grades");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(StudentOwnSubjectsGradesActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}