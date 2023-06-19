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

public class StudentOwnSubjectsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<Subject> aSubjectList;
    private RecyclerView mRecyclerView;
    private StudentSubjectAdapter mAdapter;
    private String userID;
    private String year;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_own_subjects);

        userID = getIntent().getStringExtra("studentUserID");
        year = getIntent().getStringExtra("studentYear");

        aSubjectList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view_student_subjects);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StudentSubjectAdapter(aSubjectList);
        mRecyclerView.setAdapter(mAdapter);

        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

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
                Toast.makeText(StudentOwnSubjectsActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
            }
        });

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
                    Intent intent = new Intent(StudentOwnSubjectsActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(StudentOwnSubjectsActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(StudentOwnSubjectsActivity.this, FindUserToChatActivity.class);
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
                    Intent intent = new Intent(StudentOwnSubjectsActivity.this, StudentOwnSubjectsGradesActivity.class);
                    intent.putExtra("studentYear", year);
                    intent.putExtra("studentFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("studentLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("studentUserName",getIntent().getStringExtra("studentUserName"));
                    intent.putExtra("studentUserID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("type", "Grades");
                    intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(StudentOwnSubjectsActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}