package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeActivity extends AppCompatActivity {

    private Button openRequests;
    private Button ownRequests;
    private BottomNavigationView bottomNavigationView;
    private TextView employeeWelcome;

    private String userID;
    private String firstname;
    private String lastname;
    private String username;
    private String category;
    private String email;

    private FirebaseAuth mAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        openRequests = findViewById(R.id.openRequests);
        ownRequests = findViewById(R.id.ownRequests);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        employeeWelcome = findViewById(R.id.employeeWelcome);

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
                }

                employeeWelcome.setText("Welcome\n" + firstname + " !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                    openOwnRequestList();
                    return true;
                }
                if (id == R.id.action_request) {
                    openEmployeeRequests();
                    return true;
                }
                return false;
            }
        });

        openRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOpenRequestList();
            }
        });

        ownRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOwnRequestList();
            }
        });
    }

    public void openOpenRequestList(){
        Intent intent = new Intent(this, RequestListActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "all");
        startActivity(intent);
    }

    public void openOwnRequestList(){
        Intent intent = new Intent(this, RequestListActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        intent.putExtra("type", "own");
        startActivity(intent);
    }

    public void openEmployeeRequests(){
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