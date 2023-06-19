package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private TextView textViewLogin;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference emailsRef;

    private String year;
    private String firstname;
    private String lastname;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.registerUsername);
        regEmail = findViewById(R.id.registerEmail);
        regPassword = findViewById(R.id.registerPassword);
        regConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        emailsRef = FirebaseDatabase.getInstance().getReference("accepted_emails");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = regUsername.getText().toString().trim();
                String email = regEmail.getText().toString().trim();

                if(username.isEmpty()) {
                    regUsername.setError("An username is required");
                    regUsername.requestFocus();
                }

                if(email.isEmpty()) {
                    regEmail.setError("An email is required");
                    regEmail.requestFocus();
                }

                emailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> acceptedEmails = new ArrayList<>();
                        for (DataSnapshot emailSnapshot : snapshot.getChildren()) {
                            String emails = emailSnapshot.child("email").getValue(String.class);
                            acceptedEmails.add(emails);
                        }

                        if (acceptedEmails.contains(email)) {

                            Query query = emailsRef.child("accepted_emails").orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            year = child.child("year").getValue(String.class);
                                            firstname = child.child("first_name").getValue(String.class);
                                            lastname = child.child("last_name").getValue(String.class);
                                            category = child.child("role").getValue(String.class);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean userExists = false;
                                    boolean emailExists = false;

                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        String email1 = userSnapshot.child("email").getValue(String.class);
                                        String username = userSnapshot.child("username").getValue(String.class);

                                        if (username.equals(username)) {
                                            userExists = true;
                                            break;
                                        }
                                        if (email1.equals(email)) {
                                            emailExists = true;
                                            break;
                                        }
                                    }

                                    if (userExists) {
                                        regUsername.setError("Username already exists");
                                        regUsername.requestFocus();
                                    } else if (emailExists) {
                                        regEmail.setError("Email already exists");
                                        regEmail.requestFocus();
                                    } else {
                                        String password = regPassword.getText().toString().trim();
                                        String confirmPassword = regConfirmPassword.getText().toString().trim();

                                        if(password.isEmpty()){
                                            regPassword.setError("Insert a password");
                                            regPassword.requestFocus();
                                        }

                                        if(confirmPassword.isEmpty()){
                                            regConfirmPassword.setError("Insert the password again");
                                            regConfirmPassword.requestFocus();
                                        }

                                        if(password.length() < 6){
                                            regPassword.setError("The password must be at least of length 6");
                                            regPassword.requestFocus();
                                        }

                                        if(confirmPassword.length() < 6){
                                            regConfirmPassword.setError("The password must be at least of length 6");
                                            regConfirmPassword.requestFocus();
                                        }


                                        if (!password.equals(confirmPassword)) {
                                            regConfirmPassword.setError("The passwords do not match");
                                            regConfirmPassword.requestFocus();
                                        } else {
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                String userId = mAuth.getCurrentUser().getUid();
                                                                User newUser = new User(username, firstname, lastname, email, category);
                                                                mDatabase.child(userId).setValue(newUser);
                                                                if(category.equals("student")) {
                                                                    mDatabase.child(userId).child("year").setValue(year);
                                                                }
                                                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            } else {
                                                                Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(RegisterActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "This email address is not accepted.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}