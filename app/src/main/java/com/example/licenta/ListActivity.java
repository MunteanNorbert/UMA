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

public class ListActivity extends AppCompatActivity {

    private Spinner pSpinner;
    private Button pButton;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        pSpinner = findViewById(R.id.spinner_role);
        pButton = findViewById(R.id.button_show_list);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pSpinner.setAdapter(adapter);

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
                    Intent intent = new Intent(ListActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(ListActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(ListActivity.this, FindUserToChatActivity.class);
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
                        Intent intent = new Intent(ListActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                        return true;
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(ListActivity.this, ProfessorSubjectsRequestActivity.class);
                        intent.putExtra("professorUsername", getIntent().getStringExtra("userName"));
                        intent.putExtra("professorUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("type","grades");
                        startActivity(intent);
                        return true;
                    }
                    if(getIntent().getStringExtra("userType").equals("admin")) {
                        Intent intent = new Intent(ListActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                        return true;
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(ListActivity.this, RequestListActivity.class);
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
                }
                if (id == R.id.action_request) {
                    Intent intent = new Intent(ListActivity.this, RequestActivity.class);
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


        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedRole = pSpinner.getSelectedItem().toString();

                Intent intent = new Intent(ListActivity.this, PersonListActivity.class);
                intent.putExtra("role", selectedRole);
                intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                intent.putExtra("userID",getIntent().getStringExtra("userID"));
                intent.putExtra("userType", getIntent().getStringExtra("userType"));
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
            }
        });
    }
}