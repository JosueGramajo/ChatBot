package com.gramajo.josue.chatbot;

import android.os.AsyncTask;
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
import android.widget.TextView;

import com.gramajo.josue.chatbot.adapters.ChatAdapter;
import com.gramajo.josue.chatbot.objects.Message;
import com.gramajo.josue.chatbot.utils.JsonUtil;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private ListView chatListView;
    private ChatAdapter adapter;
    private ArrayList<Message> chatHistory;
    private TextView chatState, chatName;

    private String currentMessage = "";

    public static int messageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        messageEditText = (EditText) findViewById(R.id.et_message);

        chatState = (TextView) findViewById(R.id.tv_chatbot_state);

        chatListView = (ListView) findViewById(R.id.lv_chat);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageEditText.getText().toString();
                if(text != ""){
                    sendMessage(true, text);
                }
            }
        });

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

    private void sendMessage(boolean selfMessage, String strMessage){
        Message message = new Message();
        message.setId(MainActivity.messageId++);
        message.setMessage(strMessage);
        message.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        message.setSelfMessage(selfMessage);

        this.currentMessage = messageEditText.getText().toString();

        messageEditText.setText("");

        displayMessage(message);

        chatHistory.add(message);

        JsonUtil.INSTANCE.writeMessageJsonFile(this, JsonUtil.INSTANCE.getMessageJson(chatHistory));

        if(selfMessage){
            new RespondeAsynchronously(false).execute();
        }
    }

    private String decideResponse(){
        String text = this.currentMessage.toLowerCase().trim();
        String response = "";

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(text.contains("hola")){
            response = ((hourOfDay >= 12) ? "Buenas tardes" : "Buenos dias") + ", como puedo ayudarlo?";
            return response;
        }else if(text.contains("buenos dias") || text.contains("buen dia")){
            response = ((hourOfDay >= 12) ? "Buenas tardes" : "Buenos dias") + ", como puedo ayudarlo?";
            return response;
        }

        return "Lo siento, no puedo entender la pregunta";
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

    private class RespondeAsynchronously extends AsyncTask<String, Void, String> {

        boolean isWriting = false;

        public RespondeAsynchronously(boolean isWriting){
            this.isWriting = isWriting;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                int millis = isWriting ? 4000 : 1000;
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if(isWriting){
                chatState.setText("En Linea");
                sendMessage(false, decideResponse());
            }else{
                chatState.setText("Escribiendo...");
                new RespondeAsynchronously(true).execute();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
