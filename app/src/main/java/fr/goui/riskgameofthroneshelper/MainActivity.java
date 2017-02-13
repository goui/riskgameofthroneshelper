package fr.goui.riskgameofthroneshelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.goui.riskgameofthroneshelper.model.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Map mWesterosMap;

    private Map mEssosMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMaps();


    }

    private void getMaps() {
        InputStream inputStream = this.getResources().openRawResource(R.raw.westeros_territories);
        String westerosJsonString = readJsonFile(inputStream);

        inputStream = getResources().openRawResource(R.raw.essos_territories);
        String essosJsonString = readJsonFile(inputStream);

        Gson gson = new Gson();
        mWesterosMap = gson.fromJson(westerosJsonString, Map.class);
        mEssosMap = gson.fromJson(essosJsonString, Map.class);
    }

    private String readJsonFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte bufferByte[] = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(bufferByte)) != -1) {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return outputStream.toString();
    }
}
