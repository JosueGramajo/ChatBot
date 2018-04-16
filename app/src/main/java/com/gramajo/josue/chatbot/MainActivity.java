package com.gramajo.josue.chatbot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gramajo.josue.chatbot.Adapters.ChatAdapter;
import com.gramajo.josue.chatbot.Objects.Message;
import com.gramajo.josue.chatbot.Objects.JsonObjects.Messages;
import com.gramajo.josue.chatbot.Utils.FirebaseUtils;
import com.gramajo.josue.chatbot.Utils.GlobalAccess;
import com.gramajo.josue.chatbot.Utils.JsonUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private ListView chatListView;
    private ChatAdapter adapter;
    private TextView chatState, chatName;

    private Messages chatHistory;

    private String currentMessage = "";

    boolean waitingConfirmation = false;

    SharedPreferences settings;

    private enum ResponseType{
        QUESTION,
        CONFIRMATION,
        CARD_NUMBER,
        CARD_DATE,
        CARD_SECURITY_NUMBER,
        DEFAULT
    }
    private enum ContextType{
        CONSULT,
        QUESTION,
        BLOCK_CARD,
        ACTIVATE_CARD,
        ACTIVATE_ABROAD_CARD,
        POINTS_INFORMATION,
        BALANCE_INFORMATION,
        MOVEMENT_INFORMATION
    }

    private ResponseType expectedResponseType = ResponseType.DEFAULT;

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
                if(!text.equals("")){
                    if(GlobalAccess.USER.equals("")){
                        requestUser();
                    }else{
                        sendMessage(true, text);
                    }
                }
            }
        });

        chatHistory = new Messages();

        adapter = new ChatAdapter(MainActivity.this, new ArrayList<Message>());
        chatListView.setAdapter(adapter);

        settings = getSharedPreferences("HAL_CHAT_BOT", Context.MODE_PRIVATE);
        GlobalAccess.USER = settings.getString("tester_user","");
        if(GlobalAccess.USER.equals("")){
            requestUser();
        }else{
            FirebaseUtils.INSTANCE.checkForExistingData();
        }

        checkForExistingMessages();

        FirebaseUtils.INSTANCE.getMessages();
    }

    private void requestUser(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 5, 10, 5);

        final EditText edittext = new EditText(this);
        edittext.setLayoutParams(lp);

        alert.setMessage("Ingresar usuario");
        alert.setTitle("Favor de ingresar un nombre de usuario para iniciar las pruebas");

        container.addView(edittext);

        alert.setView(container);

        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = edittext.getText().toString();
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("tester_user", value);
                editor.commit();

                GlobalAccess.USER = value;

                FirebaseUtils.INSTANCE.checkForExistingData();
            }
        });

        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_messages) {
            JsonUtil.INSTANCE.copyJsonContentToClipboard(this, JsonUtil.FILE_TYPE.MESSAGES);
            return true;
        }else if (id == R.id.action_questions){
            JsonUtil.INSTANCE.copyJsonContentToClipboard(this, JsonUtil.FILE_TYPE.QUESTIONS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(boolean selfMessage, String strMessage){
        Message message = new Message();
        message.setId(GlobalAccess.MESSAGE_ID++);
        message.setMessage(strMessage);
        message.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
        message.setSelfMessage(selfMessage);

        chatHistory.addMessage(message);

        currentMessage = messageEditText.getText().toString();

        JsonUtil.INSTANCE.writeJson(this, chatHistory, JsonUtil.FILE_TYPE.MESSAGES);

        displayMessage(message);

        if(selfMessage){
            messageEditText.setText("");
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

        if(text.contains("hola") || text.contains("buenos dias") || text.contains("buen dia")){
            response = ((hourOfDay >= 12) ? "Buenas tardes" : "Buenos dias") + ", como puedo ayudarlo?";
            expectedResponseType = ResponseType.DEFAULT;
            return response;
        }else if(text.contains("bloquear") || text.contains("bloqueo") || text.contains("bloqueen")){
            response = "El bloqueo de tarjeta no puede realizarse por chat, desea lo comunique con un ascesor?";
            expectedResponseType = ResponseType.CONFIRMATION;
            return response;
        }else if(text.contains("puntos") && text.contains("cuantos")){
            response = "Con gusto puedo indicarle cuantos puntos tiene acumulados, seria tan amable de brindarme el numero de tarjeta?";
            expectedResponseType = ResponseType.CARD_NUMBER;
            return response;
        }

        FirebaseUtils.INSTANCE.saveUnansweredQuestion(text);
        return "Lo siento, no puedo entender la pregunta";
    }

    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scrollToLastMessage();
    }

    private void checkForExistingMessages(){
        try{
            Messages readed = JsonUtil.INSTANCE.readJSON(this, JsonUtil.FILE_TYPE.MESSAGES, Messages.class);
            for(Message m : readed.getMessages()){
                displayMessage(m);
                chatHistory.addMessage(m);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void scrollToLastMessage() {
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
                int millis = isWriting ? 3000 : 1000;
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
