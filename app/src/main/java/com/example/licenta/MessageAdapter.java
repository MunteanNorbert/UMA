package com.example.licenta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> uMessageList;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private LinearLayout chat;

        public MessageViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message1);
            chat = itemView.findViewById(R.id.messageL);
        }
    }

    public MessageAdapter(ArrayList<Message> messageList,Context context) {
        uMessageList = messageList;
        this.context =context;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        MessageAdapter.MessageViewHolder viewHolder = new MessageAdapter.MessageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message currentItem = uMessageList.get(position);

        holder.message.setText(currentItem.getMessage());

        if(currentItem.getSenderID().equals(FirebaseAuth.getInstance().getUid())){
            holder.chat.setBackgroundColor(context.getResources().getColor(R.color.violet));
            holder.message.setTextColor(context.getResources().getColor(R.color.white));
        }
        else{
            holder.chat.setBackgroundColor(context.getResources().getColor(R.color.purple_700));
            holder.message.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return uMessageList.size();
    }
}

