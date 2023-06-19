package com.example.licenta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeRequestAdapter extends RecyclerView.Adapter<EmployeeRequestAdapter.EmployeeRequestViewHolder> {
    final ArrayList<EmployeeRequest> rEmployeeRequestList;
    public final Context context;
    private String employeeUserID;
    private String type;
    private DatabaseReference employeeRef;
    private DatabaseReference userRef;

    private String firstName;
    private String lastName;
    private String username;
    private String category;
    private String email;

    public static class EmployeeRequestViewHolder extends RecyclerView.ViewHolder {
        public TextView eRequestMessage;
        public TextView eRequestName;
        public TextView eRequestEmail;
        public TextView eRequestDate;
        public TextView eRequestTime;
        public Button requestButton;

        public EmployeeRequestViewHolder(View itemView) {
            super(itemView);
            eRequestMessage = itemView.findViewById(R.id.tv_employee_request_message);
            eRequestName = itemView.findViewById(R.id.tv_employee_request_sender_name);
            eRequestEmail = itemView.findViewById(R.id.tv_employee_request_sender_email);
            eRequestDate = itemView.findViewById(R.id.tv_employee_request_date);
            eRequestTime = itemView.findViewById(R.id.tv_employee_request_time);
            requestButton = itemView.findViewById(R.id.tv_employee_request_button);

        }
    }

    public EmployeeRequestAdapter(ArrayList<EmployeeRequest> employeeRequestList, String employeeUserID, String type, Context context) {
        rEmployeeRequestList = employeeRequestList;
        this.employeeUserID = employeeUserID;
        this.type = type;
        this.context =context;
    }

    @NonNull
    @Override
    public EmployeeRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_request_item, parent, false);
        EmployeeRequestViewHolder viewHolder = new EmployeeRequestViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeRequestViewHolder holder, int position) {
        EmployeeRequest currentItem = rEmployeeRequestList.get(position);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());


        holder.eRequestMessage.setText("Request Message: " + currentItem.getMessage());
        holder.eRequestName.setText("Sender Name: " + currentItem.getName());
        holder.eRequestEmail.setText("Sender Email: " + currentItem.getEmail());
        holder.eRequestDate.setText("Request Date: " + currentItem.getDay() + ", " +currentItem.getDate());
        holder.eRequestTime.setText("Request Time: " + currentItem.getTime());

        String date = currentItem.getDate();
        String time = currentItem.getTime();
        String day = currentItem.getDay();
        String requesterUserID = currentItem.getUserID();

        employeeRef = FirebaseDatabase.getInstance().getReference("requests");

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(employeeUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    firstName = snapshot.child("firstname").getValue(String.class);
                    lastName = snapshot.child("lastname").getValue(String.class);
                    username = snapshot.child("username").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if(type.equals("all")){
            holder.requestButton.setText("Process Request");
            holder.requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure that you want to process this request?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference employeeRef2 = employeeRef.child(date.replace(".",",") + " " + time);
                            employeeRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("Date").setValue(date);
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("Day").setValue(day);
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("Email").setValue(currentItem.getEmail());
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("Request").setValue(currentItem.getMessage());
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("Time").setValue(time);
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("userID").setValue(requesterUserID);
                            userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time)
                                    .child("userName").setValue(currentItem.getName());

                            userRef.child(requesterUserID).child("notifications")
                                    .child(currentDate + ", " + currentTime)
                                    .setValue("Your request from, " + date + " at " + time
                                            + ", will be processed shortly.");

                            Intent intent = new Intent(context, RequestListActivity.class);
                            intent.putExtra("userFirstname", firstName);
                            intent.putExtra("userLastname", lastName);
                            intent.putExtra("userName", username);
                            intent.putExtra("userID",employeeUserID);
                            intent.putExtra("userType", category);
                            intent.putExtra("userEmail", email);
                            intent.putExtra("type", "all");
                            context.startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }
        if(type.equals("own")){
            holder.requestButton.setText("Request Processed");
            holder.requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure that this request has been processed?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference userRef2 = userRef.child(employeeUserID).child("Requests").child(date.replace(".",",") + " " + time);
                            userRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                            userRef.child(requesterUserID).child("notifications")
                                    .child(currentDate + ", " + currentTime)
                                    .setValue("Your request from, " + date + " at " + time
                                            + ", has been processed.");

                            Intent intent = new Intent(context, RequestListActivity.class);
                            intent.putExtra("userFirstname", firstName);
                            intent.putExtra("userLastname", lastName);
                            intent.putExtra("userName", username);
                            intent.putExtra("userID",employeeUserID);
                            intent.putExtra("userType", category);
                            intent.putExtra("userEmail", email);
                            intent.putExtra("type", "own");
                            context.startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rEmployeeRequestList.size();
    }
}