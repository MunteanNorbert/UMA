package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {

    private String userFirstname;
    private String userLastname;
    private String userID;
    private String userEmail;
    private String userType;
    private String type;

    private Class Activity;

    DatabaseReference mDatabase;
    DatabaseReference userRef;
    private ArrayList<EmployeeRequest> sEmployeeRequestList;
    private RecyclerView mRecyclerView;
    private EmployeeRequestAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        userFirstname = getIntent().getStringExtra("userFirstname");
        userLastname = getIntent().getStringExtra("userLastname");
        userID = getIntent().getStringExtra("userID");
        userType = getIntent().getStringExtra("userType");
        userEmail = getIntent().getStringExtra("userEmail");
        type = getIntent().getStringExtra("type");

        mDatabase = FirebaseDatabase.getInstance().getReference("requests");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userID)
                .child("Requests");

        sEmployeeRequestList = new ArrayList<EmployeeRequest>();
        mRecyclerView = findViewById(R.id.recycler_view_employee_requests);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmployeeRequestAdapter(sEmployeeRequestList, userID, type, RequestListActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        if(type.equals("all")) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                        if (employeeSnapshot.exists()) {
                            String date = employeeSnapshot.child("Date").getValue(String.class);
                            String day = employeeSnapshot.child("Day").getValue(String.class);
                            String time = employeeSnapshot.child("Time").getValue(String.class);
                            String message = employeeSnapshot.child("Request").getValue(String.class);
                            String name = employeeSnapshot.child("userName").getValue(String.class);
                            String requesterUserID = employeeSnapshot.child("userID").getValue(String.class);
                            String email = employeeSnapshot.child("Email").getValue(String.class);
                            sEmployeeRequestList.add(new EmployeeRequest(message, name, requesterUserID, email, date, day, time));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(type.equals("own")) {
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                        if (employeeSnapshot.exists()) {
                            String date = employeeSnapshot.child("Date").getValue(String.class);
                            String day = employeeSnapshot.child("Day").getValue(String.class);
                            String time = employeeSnapshot.child("Time").getValue(String.class);
                            String message = employeeSnapshot.child("Request").getValue(String.class);
                            String name = employeeSnapshot.child("userName").getValue(String.class);
                            String userID = employeeSnapshot.child("userID").getValue(String.class);
                            String email = employeeSnapshot.child("Email").getValue(String.class);
                            sEmployeeRequestList.add(new EmployeeRequest(message, name, userID, email, date, day, time));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

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
                    Intent intent = new Intent(RequestListActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(RequestListActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(RequestListActivity.this, FindUserToChatActivity.class);
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
                    Intent intent = new Intent(RequestListActivity.this, RequestListActivity.class);
                    intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                    intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("userID",getIntent().getStringExtra("userID"));
                    intent.putExtra("userType", getIntent().getStringExtra("userType"));
                    intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                    intent.putExtra("type", "all");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(RequestListActivity.this, RequestActivity.class);
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
    }
}