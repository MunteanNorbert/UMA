package com.example.licenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    final ArrayList<Person> pPersonList;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView sUsername;
        public TextView sName;
        public TextView sEmail;
        public TextView sCategory;

        public PersonViewHolder(View itemView) {
            super(itemView);
            sUsername = itemView.findViewById(R.id.tv_person_username);
            sName = itemView.findViewById(R.id.tv_person_name);
            sEmail = itemView.findViewById(R.id.tv_person_email);
            sCategory = itemView.findViewById(R.id.tv_person_category);
        }
    }

    public PersonAdapter(ArrayList<Person> personList) {
        pPersonList = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person currentItem = pPersonList.get(position);

        holder.sUsername.setText("User: " + currentItem.getUserName());
        holder.sName.setText("Name: " + currentItem.getFirstName() + " " + currentItem.getLastName());
        holder.sEmail.setText("Email: " + currentItem.getEmail());
        holder.sCategory.setText("Role: " + currentItem.getCategory());

    }

    @Override
    public int getItemCount() {
        return pPersonList.size();
    }
}

