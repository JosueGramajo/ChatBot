package com.gramajo.josue.chatbot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gramajo.josue.chatbot.adapters.ChatAdapter;
import com.gramajo.josue.chatbot.objects.Message;
import com.gramajo.josue.chatbot.utils.JsonUtil;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private ListView chatListView;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<Message> chatHistory;

    public static int messageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        chatListView = (ListView) findViewById(R.id.lv_chat);
        messageEditText = (EditText) findViewById(R.id.et_message);

        chatHistory = new ArrayList<Message>();

        adapter = new ChatAdapter(MainActivity.this, new ArrayList<Message>());
        chatListView.setAdapter(adapter);

        checkForExistingMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(){
        String text = messageEditText.getText().toString();
        if(text == ""){
            return;
        }

        Message message = new Message();
        message.setId(MainActivity.messageId++);
        message.setMessage(text);
        message.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        message.setSelfMessage(true);

        messageEditText.setText("");

        displayMessage(message);

        chatHistory.add(message);

        JsonUtil.INSTANCE.writeMessageJsonFile(this, JsonUtil.INSTANCE.getMessageJson(chatHistory));
    }

    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void checkForExistingMessages(){
        ArrayList<Message> readed = JsonUtil.INSTANCE.retrieveMessages(this);
        for(Message m : readed){
            displayMessage(m);
            chatHistory.add(m);
        }
    }

    private void scroll() {
        chatListView.setSelection(chatListView.getCount() - 1);
    }
}
