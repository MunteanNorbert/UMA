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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableSubjectListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private ArrayList<Subject> aSubjectList;
    private ArrayList<String> subjects;
    private RecyclerView mRecyclerView;
    private AvailableSubjectAdapter mAdapter;
    private String year;
    private String firstname;
    private String lastname;
    private String username;

    private Class Activity;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_list);

        year = getIntent().getStringExtra("studentYear");
        firstname = getIntent().getStringExtra("studentFirstname");
        lastname = getIntent().getStringExtra("studentLastname");
        username = getIntent().getStringExtra("studentUserName");

        aSubjectList = new ArrayList<>();
        subjects = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view_available_subjects);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AvailableSubjectAdapter(aSubjectList, AvailableSubjectListActivity.this, firstname, lastname, username);
        mRecyclerView.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("subjects");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(getIntent().getStringExtra("studentUserID")).child("subjects");

        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot subjectsSnapshot : snapshot.getChildren()) {
                    subjects.add(subjectsSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Query query = mDatabase.orderByChild("year").equalTo(year);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot sSnapshot : snapshot.getChildren()) {
                    String subject = sSnapshot.child("name").getValue(String.class);
                    String firstName = sSnapshot.child("professorFirstName").getValue(String.class);
                    String lastName = sSnapshot.child("professorLastName").getValue(String.class);

                    if(!subjects.contains(subject)) {
                        aSubjectList.add(new Subject(subject, year, firstName, lastName, ""));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AvailableSubjectListActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(AvailableSubjectListActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(AvailableSubjectListActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("studentUserName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(AvailableSubjectListActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("studentUserName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    Intent intent = new Intent(AvailableSubjectListActivity.this, StudentOwnSubjectsGradesActivity.class);
                    intent.putExtra("studentYear", year);
                    intent.putExtra("studentFirstname", firstname);
                    intent.putExtra("studentLastname", lastname);
                    intent.putExtra("studentUserName",username);
                    intent.putExtra("studentUserID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("type", "Grades");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(AvailableSubjectListActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("studentFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("studentLastname"));
                    intent.putExtra("userID",getIntent().getStringExtra("studentUserID"));
                    intent.putExtra("userType", getIntent().getStringExtra("studentUserType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("studentEmail"));
                    intent.putExtra("userName", getIntent().getStringExtra("studentUserName"));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}