package com.gramajo.josue.chatbot.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.gramajo.josue.chatbot.objects.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by josuegramajo on 4/10/18.
 */

public class JsonUtil {

    public static JsonUtil INSTANCE = new JsonUtil();

    public String getMessageJson(ArrayList<Message> messages) {
        try {
            /*jsonObj.put("id", message.getId());
            jsonObj.put("message", message.getMessage());
            jsonObj.put("self", message.isSelfMessage());
            jsonObj.put("date", message.getDateTime());

            JSONObject jsonAdd = new JSONObject(); // we need another object to store the address
            jsonAdd.put("address", person.getAddress().getAddress());
            jsonAdd.put("city", person.getAddress().getCity());
            jsonAdd.put("state", person.getAddress().getState());
            jsonObj.put("address", jsonAdd);*/


            JSONObject jsonObj = new JSONObject();

            JSONArray jsonArr = new JSONArray();
            for (Message message : messages ) {
                JSONObject msjObj = new JSONObject();
                msjObj.put("id", message.getId());
                msjObj.put("message", message.getMessage());
                msjObj.put("self", message.isSelfMessage());
                msjObj.put("date", message.getDateTime());
                jsonArr.put(msjObj);
            }
            jsonObj.put("messages", jsonArr);

            return jsonObj.toString();

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public void writeMessageJsonFile(Context context, String json){
        try{
            FileOutputStream fOut = context.openFileOutput("messages.json", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(json);
            osw.flush();
            osw.close();
            /*File root = new File(Environment.getExternalStorageDirectory().toString() + "/ChatBot");
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/ChatBot/messages.json");

            if(!root.exists()){
                root.mkdirs();
            }

            if(!file.exists()){
                file.createNewFile();
            }

            File gpxfile = new File(root, "messages.json");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(json);
            writer.flush();
            writer.close();*/
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<Message> retrieveMessages(Context context) {
        ArrayList<Message> retrievedMessages = new ArrayList<>();
        String json = null;
        try {
            FileInputStream fIn = context.openFileInput("messages.json");

            //File f_path = new File(Environment.getExternalStorageDirectory().toString() + "/ChatBot/messages.json");

            InputStream is = fIn;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return retrievedMessages;
        }

        if(json != null){
            try {
                JSONObject obj = new JSONObject(json);
                JSONArray m_jArry = obj.getJSONArray("messages");
                ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> m_li;

                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    int id = jo_inside.getInt("id");
                    String message = jo_inside.getString("message");
                    boolean self = jo_inside.getBoolean("self");
                    String date = jo_inside.getString("date");

                    retrievedMessages.add(new Message(id, self, message, date));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return retrievedMessages;
    }

    public void copyJsonToClipboard(Context context){
        String json = "";
        try {
            FileInputStream fIn = context.openFileInput("messages.json");

            //File f_path = new File(Environment.getExternalStorageDirectory().toString() + "/ChatBot/messages.json");

            InputStream is = fIn;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject objJson = new JSONObject(json);

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", objJson.toString(4));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Json copiado en portapapeles", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
