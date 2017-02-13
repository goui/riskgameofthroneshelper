package fr.goui.riskgameofthroneshelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.riskgameofthroneshelper.adapter.PlayerAdapter;
import fr.goui.riskgameofthroneshelper.model.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Map mWesterosMap;

    private Map mEssosMap;

    @BindView(R.id.player_number_text_view)
    TextView mNbOfPlayersTextView;

    private int mNumberOfPlayers = 2;

    @BindView(R.id.player_recycler_view)
    RecyclerView mPlayerRecyclerView;

    private PlayerAdapter mPlayerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMaps();

        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlayerRecyclerView.setHasFixedSize(true);
        mPlayerAdapter = new PlayerAdapter(this);
        mPlayerRecyclerView.setAdapter(mPlayerAdapter);
    }

    @OnClick(R.id.player_minus_button)
    public void onMinusClick() {
        if (mNumberOfPlayers > 2) {
            mNumberOfPlayers--;
            mPlayerAdapter.deletePlayer();
            update();
        }
    }

    @OnClick(R.id.player_plus_button)
    public void onPlusClick() {
        if (mNumberOfPlayers < 7) {
            mNumberOfPlayers++;
            mPlayerAdapter.addPlayer();
            update();
        }
    }

    private void update() {
        mNbOfPlayersTextView.setText("" + mNumberOfPlayers);
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
