package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmailAddActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button addButton;
    private DatabaseReference databaseRef;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;
    private Spinner rSpinner;
    private Spinner ySpinner;

    private ArrayList<String> users;
    private ArrayList<String> emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_add);

        emailEditText = findViewById(R.id.insertEmail);
        firstNameEditText = findViewById(R.id.insertFirstName);
        lastNameEditText = findViewById(R.id.insertLastName);
        addButton = findViewById(R.id.emailAddButton);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);
        rSpinner = findViewById(R.id.spinner_role);
        ySpinner = findViewById(R.id.spinner_year);

        users = new ArrayList<>();
        emails = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference("accepted_emails");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersSnapshot : snapshot.getChildren()) {
                    users.add(usersSnapshot.getKey());
                    emails.add(usersSnapshot.child("email").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.year, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(adapter2);

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
                    Intent intent = new Intent(EmailAddActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(EmailAddActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(EmailAddActivity.this, FindUserToChatActivity.class);
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
                        Intent intent = new Intent(EmailAddActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("studentUserType", getIntent().getStringExtra("studentUserType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("studentEmail"));
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(EmailAddActivity.this, ProfessorSubjectsRequestActivity.class);
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
                        Intent intent = new Intent(EmailAddActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(EmailAddActivity.this, RequestListActivity.class);
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
                    Intent intent = new Intent(EmailAddActivity.this, RequestActivity.class);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String role = rSpinner.getSelectedItem().toString();
                String year = ySpinner.getSelectedItem().toString();
                String user = email.substring(0, email.indexOf("@")).replace(".", "") + role;


                if(!users.contains(user) && !emails.contains(email) && (email.contains("@student.com") || email.contains("@professor.com")|| email.contains("@employee.com") || email.contains("@admin.com"))) {
                    if (!email.isEmpty() || !role.isEmpty() || !firstName.isEmpty() || !lastName.isEmpty()) {
                        databaseRef.child(user).child("email").setValue(email);
                        databaseRef.child(user).child("first_name").setValue(firstName);
                        databaseRef.child(user).child("last_name").setValue(lastName);
                        databaseRef.child(user).child("role").setValue(role.toLowerCase());
                        if (role.equals("Student")) {
                            databaseRef.child(user).child("year").setValue(year);
                        }

                        Toast.makeText(EmailAddActivity.this, "Email added successfully", Toast.LENGTH_SHORT).show();

                        emailEditText.setText("");
                        firstNameEditText.setText("");
                        lastNameEditText.setText("");

                    } else {
                        Toast.makeText(EmailAddActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmailAddActivity.this, "This email is not accepted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
