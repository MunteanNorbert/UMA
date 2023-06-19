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

public class ProfessorSubjectsListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private ArrayList<Subject> aSubjectList;
    private RecyclerView mRecyclerView;
    private ProfessorSubjectAdapter mAdapter;
    private String username;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    private String firstName;
    private String lastName;
    private String username2;
    private String category;
    private String email;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subjects_list);

        username = getIntent().getStringExtra("professorUsername");

        aSubjectList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view_professor_subjects);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProfessorSubjectAdapter(aSubjectList);
        mRecyclerView.setAdapter(mAdapter);

        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        mDatabase = FirebaseDatabase.getInstance().getReference("subjects");
        Query query = mDatabase.orderByChild("professorUserName").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot professorSnapshot : snapshot.getChildren()) {
                    String subject = professorSnapshot.child("name").getValue(String.class);
                    String firstname = professorSnapshot.child("professorFirstName").getValue(String.class);
                    String lastname = professorSnapshot.child("professorLastName").getValue(String.class);
                    String year1 = professorSnapshot.child("year").getValue(String.class);
                    userID = professorSnapshot.getKey();
                    aSubjectList.add(new Subject(subject, year1, firstname, lastname,""));
                    userRef = FirebaseDatabase.getInstance().getReference("users");

                    userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                firstName = snapshot.child("firstname").getValue(String.class);
                                lastName = snapshot.child("lastname").getValue(String.class);
                                username2 = snapshot.child("username").getValue(String.class);
                                category = snapshot.child("category").getValue(String.class);
                                email = snapshot.child("email").getValue(String.class);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfessorSubjectsListActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(ProfessorSubjectsListActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(ProfessorSubjectsListActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username2);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(ProfessorSubjectsListActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username2);
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(ProfessorSubjectsListActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", firstName);
                        intent.putExtra("studentLastname", lastName);
                        intent.putExtra("studentUserID", userID);
                        intent.putExtra("studentUserType", category);
                        intent.putExtra("studentEmail", email);
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(ProfessorSubjectsListActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", username);
                        intent.putExtra("professorUserID", userID);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(ProfessorSubjectsListActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",userID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username2);
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(ProfessorSubjectsListActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", firstName);
                        intent.putExtra("userLastname", lastName);
                        intent.putExtra("userID",userID);
                        intent.putExtra("userType", category);
                        intent.putExtra("userEmail", email);
                        intent.putExtra("userName", username2);
                        intent.putExtra("type", "own");
                        startActivity(intent);
                    }
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(ProfessorSubjectsListActivity.this, RequestActivity.class);
                    intent.putExtra("userFirstname", firstName);
                    intent.putExtra("userLastname", lastName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", category);
                    intent.putExtra("userEmail", email);
                    intent.putExtra("userName", username2);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}