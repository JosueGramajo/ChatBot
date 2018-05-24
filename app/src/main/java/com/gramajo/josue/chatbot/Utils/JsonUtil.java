package com.gramajo.josue.chatbot.Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gramajo.josue.chatbot.Objects.BaseObject;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by josuegramajo on 4/10/18.
 */

    public class JsonUtil {

    public static JsonUtil INSTANCE = new JsonUtil();

    public enum FILE_TYPE{
        MESSAGES("messages"),
        QUESTIONS("unanswered"),
        DECISION_TREE("tree");

        private String value;

        FILE_TYPE(String value){
            this.value = value;
        }

        public String rawValue(){
            return this.value;
        }
    }

    public void writeJson(Context context, BaseObject object, FILE_TYPE type){
        try{
            FileOutputStream fOut = context.openFileOutput(type.rawValue() + ".json", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(osw, object);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public <T> T readJSON(final Context context, final FILE_TYPE type, final Class<T> responseClass) throws IOException {
        InputStream fIn = context.openFileInput( type.rawValue() + ".json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(fIn, responseClass);
    }

    public void copyJsonContentToClipboard(Context context, FILE_TYPE type){
        try {
            InputStream is = context.openFileInput(type.rawValue() + ".json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject objJson = new JSONObject(json);

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", objJson.toString(4));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Json copiado en portapapeles", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void copyObjectAsJsonString(BaseObject obj, Context context){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String strJson = mapper.writeValueAsString(obj);
            JSONObject objJson = new JSONObject(strJson);

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", objJson.toString(4));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Json copiado en portapapeles", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
