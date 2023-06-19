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

public class AdminActivity extends AppCompatActivity {

    private Button addEmails;
    private Button addSubjects;
    private Button personLists;
    private BottomNavigationView bottomNavigationView;
    private TextView adminWelcome;

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String userID;
    private String category;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addEmails= findViewById(R.id.addEmails);
        addSubjects = findViewById(R.id.addSubjects);
        personLists = findViewById(R.id.person_lists);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        adminWelcome = findViewById(R.id.adminWelcome);

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

                adminWelcome.setText("Welcome\n" + firstname + " !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailAdder();
            }
        });

        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubjectAdder();
            }
        });

        personLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonLists();
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
                    openSubjectLists();
                    return true;
                }
                if (id == R.id.action_request) {
                    openAdminRequests();
                    return true;
                }
                return false;
            }
        });
    }

    public void openEmailAdder(){
        Intent intent = new Intent(this, EmailAddActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openSubjectAdder(){
        Intent intent = new Intent(this, SubjectAddActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openPersonLists(){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openSubjectLists(){
        Intent intent = new Intent(this, ListActivity2.class);
        intent.putExtra("userFirstname", firstname);
        intent.putExtra("userLastname", lastname);
        intent.putExtra("userName", username);
        intent.putExtra("userID",userID);
        intent.putExtra("userType", category);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

    public void openAdminRequests(){
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