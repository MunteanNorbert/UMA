package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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

public class FindUserToChatActivity extends AppCompatActivity {

    private AutoCompleteTextView searchAutoCompleteTextView;
    private Button startChat;
    private Button oldChats;
    private BottomNavigationView bottomNavigationView;

    private String selectedUser;
    private String selectedUserID;
    private String selectedUserFirstname;
    private String selectedUserLastname;
    private String userID;
    private ArrayList<String> usernames;

    private Class Activity;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_to_chat);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        searchAutoCompleteTextView = findViewById(R.id.searchUserToChat);
        startChat = findViewById(R.id.startChat);
        oldChats = findViewById(R.id.oldChats);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        userID = getIntent().getStringExtra("userID");

        usernames = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usernameSnapshot : snapshot.getChildren()){
                    usernames.add(usernameSnapshot.child("username").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, usernames);

        searchAutoCompleteTextView.setAdapter(adapter);

        searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (String) parent.getItemAtPosition(position);

                Query query = mDatabase.orderByChild("username").equalTo(selectedUser);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            selectedUserID = userSnapshot.getKey();
                            selectedUserFirstname = userSnapshot.child("firstname").getValue(String.class);
                            selectedUserLastname = userSnapshot.child("lastname").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUserToChatActivity.this, ChatActivity.class);
                intent.putExtra("ownUserID",userID);
                intent.putExtra("selectedUserID", selectedUserID);
                intent.putExtra("selectedUserFirstname", selectedUserFirstname);
                intent.putExtra("selectedUserLastname", selectedUserLastname);
                intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                intent.putExtra("userName", getIntent().getStringExtra("userLastname"));
                intent.putExtra("userID", userID);
                intent.putExtra("userType", getIntent().getStringExtra("userType"));
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
            }
        });
        oldChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUserToChatActivity.this, OldChatsActivity.class);
                intent.putExtra("ownUserID",userID);
                intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                intent.putExtra("userName", getIntent().getStringExtra("userLastname"));
                intent.putExtra("userID", userID);
                intent.putExtra("userType", getIntent().getStringExtra("userType"));
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_chat);

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
                    Intent intent = new Intent(FindUserToChatActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(FindUserToChatActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(FindUserToChatActivity.this, FindUserToChatActivity.class);
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
                        Intent intent = new Intent(FindUserToChatActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")){
                        Intent intent = new Intent(FindUserToChatActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                        intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        intent.putExtra("type","grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {

                        Intent intent = new Intent(FindUserToChatActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(FindUserToChatActivity.this, RequestListActivity.class);
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
                    Intent intent = new Intent(FindUserToChatActivity.this, RequestActivity.class);
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