package com.simats.prepace;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simats.prepace.adapter.ChatAdapter;
import com.simats.prepace.model.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        View btnSend = findViewById(R.id.btnSend);
        View btnBack = findViewById(R.id.btnBack);
        
        // Chip Listeners
        findViewById(R.id.chip1).setOnClickListener(v -> sendMessage(((TextView)v).getText().toString()));
        findViewById(R.id.chip2).setOnClickListener(v -> sendMessage(((TextView)v).getText().toString()));

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(chatAdapter);

        // Initial Greeting
        addBotMessage("Hi! I'm your PrepAce AI Assistant.\nHow can I help you today?");

        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                etMessage.setText("");
            }
        });
    }

    private void sendMessage(String msg) {
        // Add User Message
        messageList.add(new ChatMessage(msg, true, System.currentTimeMillis()));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);

        // Simulate Bot Typing/Reply
        new Handler().postDelayed(() -> {
            String response = getBotResponse(msg);
            addBotMessage(response);
        }, 1000);
    }

    private void addBotMessage(String msg) {
        messageList.add(new ChatMessage(msg, false, System.currentTimeMillis()));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
    }

    private String getBotResponse(String userMsg) {
        return com.simats.prepace.utils.AppKnowledgeBase.getResponse(userMsg);
    }
}
