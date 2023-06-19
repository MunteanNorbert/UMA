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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubjectAddActivity extends AppCompatActivity {

    private EditText subjectEditText;
    private Button subjectAddButton;
    private BottomNavigationView bottomNavigationView;
    private Class Activity;
    private Spinner pSpinner;
    private Spinner ySpinner;

    private DatabaseReference mDatabase;
    private DatabaseReference professorRef;

    private ArrayList<String> professorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

        subjectEditText = findViewById(R.id.subject_name);
        ySpinner = findViewById(R.id.year);
        pSpinner = findViewById(R.id.professorUsername);
        subjectAddButton = findViewById(R.id.add_subject_button);
        bottomNavigationView = findViewById(R.id.studentBottomNavigation);

        professorList = new ArrayList<>();

        professorRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabase = FirebaseDatabase.getInstance().getReference("subjects");

        Query query = professorRef.orderByChild("category").equalTo("professor");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot professorSnapshot : snapshot.getChildren()) {
                        String firstname = professorSnapshot.child("firstname").getValue(String.class);
                        String lastname = professorSnapshot.child("lastname").getValue(String.class);
                        String username = professorSnapshot.child("username").getValue(String.class);
                        String name = firstname + " " + lastname + " - " + username;
                        professorList.add(name);
                    }

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                            SubjectAddActivity.this, android.R.layout.simple_spinner_item, professorList);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter2.notifyDataSetChanged();
                    pSpinner.setAdapter(adapter2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(adapter);

        subjectAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectToDatabase();
            }
        });
    }

    private void addSubjectToDatabase() {

        String subjectName;
        String year;
        String selectedProfessor;

        subjectName = subjectEditText.getText().toString().trim();
        year = ySpinner.getSelectedItem().toString();
        selectedProfessor = String.valueOf(pSpinner.getSelectedItem());

        String[] parts = selectedProfessor.split(" - ");

        String part1 = parts[0];
        String part2 = parts[1];

        if (subjectName.isEmpty() || year.isEmpty() || selectedProfessor.isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            professorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot usernameSnapshot : snapshot.getChildren()) {
                        String username = usernameSnapshot.child("username").getValue(String.class);
                        if (username.equals(part2)) {
                            String professorUserID = usernameSnapshot.getKey();
                            String professorFirstname = usernameSnapshot.child("firstname").getValue(String.class);
                            String professorLastname = usernameSnapshot.child("lastname").getValue(String.class);

                            professorRef.child(professorUserID).child("subjectListRequests").child(subjectName).setValue(subjectName);
                            professorRef.child(professorUserID).child("subjectListStudents").child(subjectName).setValue(subjectName);
                            professorRef.child(professorUserID).child("subjectList").child(subjectName).setValue(subjectName);

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                            String currentDate = dateFormat.format(calendar.getTime());
                            String currentTime = timeFormat.format(calendar.getTime());

                            professorRef.child(professorUserID).child("notifications")
                                    .child(currentDate + ", " + currentTime)
                                    .setValue("The " + subjectName + " subject has been added to your subject list");

                            Subject subject = new Subject(subjectName, year, professorFirstname, professorLastname, part2);
                            mDatabase.child(subjectName).setValue(subject);

                            Toast.makeText(SubjectAddActivity.this, "Subject added successfully", Toast.LENGTH_SHORT).show();

                            subjectEditText.setText("");
                        }
                        finish();
                    }
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
                    Intent intent = new Intent(SubjectAddActivity.this, Activity);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.action_notifications) {
                    Intent intent = new Intent(SubjectAddActivity.this, NotificationsActivity.class);
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
                    Intent intent = new Intent(SubjectAddActivity.this, FindUserToChatActivity.class);
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
                        Intent intent = new Intent(SubjectAddActivity.this, StudentOwnSubjectsGradesActivity.class);
                        intent.putExtra("studentFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("studentLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("studentUserID", getIntent().getStringExtra("userID"));
                        intent.putExtra("studentUserType", getIntent().getStringExtra("userType"));
                        intent.putExtra("studentEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("type", "Grades");
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("professor")) {
                        Intent intent = new Intent(SubjectAddActivity.this, ProfessorSubjectsRequestActivity.class);
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
                        Intent intent = new Intent(SubjectAddActivity.this, ListActivity2.class);
                        intent.putExtra("userFirstname", getIntent().getStringExtra("userFirstname"));
                        intent.putExtra("userLastname", getIntent().getStringExtra("userLastname"));
                        intent.putExtra("userID",getIntent().getStringExtra("userID"));
                        intent.putExtra("userType", getIntent().getStringExtra("userType"));
                        intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                        intent.putExtra("userName", getIntent().getStringExtra("userName"));
                        startActivity(intent);
                    }
                    if(getIntent().getStringExtra("userType").equals("employee")) {
                        Intent intent = new Intent(SubjectAddActivity.this, RequestListActivity.class);
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
                    Intent intent = new Intent(SubjectAddActivity.this, RequestActivity.class);
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