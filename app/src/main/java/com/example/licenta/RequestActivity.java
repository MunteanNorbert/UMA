package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

    private String userFirstname;
    private String userLastname;
    private String userID;
    private String userType;
    private String userEmail;

    private Class Activity;

    private EditText requestEditText;
    private Button requestButton;
    private BottomNavigationView bottomNavigationView;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        userFirstname = getIntent().getStringExtra("userFirstname");
        userLastname = getIntent().getStringExtra("userLastname");
        userID = getIntent().getStringExtra("userID");
        userEmail = getIntent().getStringExtra("userEmail");
        userType = getIntent().getStringExtra("userType");

        requestEditText = findViewById(R.id.requestEditText);
        requestButton = findViewById(R.id.requestButton);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_request);

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
                    Intent intent = new Intent(RequestActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(RequestActivity.this, NotificationsActivity.class);
                    intent.putExtra("userFirstname", userFirstname);
                    intent.putExtra("userLastname", userLastname);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_chat) {
                    Intent intent = new Intent(RequestActivity.this, FindUserToChatActivity.class);
                    intent.putExtra("userFirstname", userFirstname);
                    intent.putExtra("userLastname", userLastname);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userName", getIntent().getStringExtra("userName"));
                    intent.putExtra("type", "own");
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_grades) {
                    if(getIntent().getStringExtra("userType").equals("student")) {
                        Intent intent = new Intent(RequestActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", userFirstname);
                        intent.putExtra("studentLastname", userLastname);
                        intent.putExtra("studentUserID", userID);
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                        return true;
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(RequestActivity.this, ProfessorSubjectsRequestActivity.class);
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
                        Intent intent = new Intent(RequestActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(RequestActivity.this, RequestListActivity.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("type", "own");
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                }
                if (id == R.id.action_request) {
                    return true;
                }
                return false;
            }
        });
    }

    public void sendRequest(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd,MM,yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormat.format(calendar.getTime());
        String currentDay = dayFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        String request = requestEditText.getText().toString().trim();

        mDatabase = FirebaseDatabase.getInstance().getReference("requests");

        if(!request.isEmpty()) {
            mDatabase.child(currentDate + " " + currentTime).child("Request").setValue(request);
            mDatabase.child(currentDate + " " + currentTime).child("Date").setValue(currentDate.replace(',', '.'));
            mDatabase.child(currentDate + " " + currentTime).child("Time").setValue(currentTime);
            mDatabase.child(currentDate + " " + currentTime).child("Day").setValue(currentDay);
            mDatabase.child(currentDate + " " + currentTime).child("userID").setValue(userID);
            mDatabase.child(currentDate + " " + currentTime).child("userName").setValue(userFirstname + " " + userLastname);
            mDatabase.child(currentDate + " " + currentTime).child("Email").setValue(userEmail);

            requestEditText.setText("");
        }
    }
}