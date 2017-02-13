package fr.goui.riskgameofthroneshelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.riskgameofthroneshelper.adapter.PlayerAdapter;
import fr.goui.riskgameofthroneshelper.adapter.TerritoryAdapter;
import fr.goui.riskgameofthroneshelper.model.ListItem;
import fr.goui.riskgameofthroneshelper.model.Map;
import fr.goui.riskgameofthroneshelper.model.PlayerModel;
import fr.goui.riskgameofthroneshelper.model.Region;
import fr.goui.riskgameofthroneshelper.model.Territory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 7;

    private Map mWesterosMap;

    private Map mEssosMap;

    private List<Region> mListOfRegions;

    private List<ListItem> mListOfRegionsAndTerritories;

    /* PLAYER */

    @BindView(R.id.player_number_text_view)
    TextView mNbOfPlayersTextView;

    private int mOldNumberOfPlayers;
    private int mNewNumberOfPlayers;

    @BindView(R.id.player_recycler_view)
    RecyclerView mPlayerRecyclerView;

    private PlayerAdapter mPlayerAdapter;

    private PlayerModel mPlayerModel = PlayerModel.getInstance();

    /* TERRITORY */

    @BindView(R.id.territory_recycler_view)
    RecyclerView mTerritoryRecyclerView;

    private TerritoryAdapter mTerritoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();

        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlayerRecyclerView.setHasFixedSize(true);
        mPlayerAdapter = new PlayerAdapter(this);
        mPlayerRecyclerView.setAdapter(mPlayerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int span = 1;
                if (mTerritoryAdapter.getItemViewType(position) == TerritoryAdapter.TYPE_REGION) {
                    span = 3;
                }
                return span;
            }
        });
        mTerritoryRecyclerView.setLayoutManager(gridLayoutManager);
        mTerritoryRecyclerView.setHasFixedSize(true);
        mTerritoryAdapter = new TerritoryAdapter(this, mListOfRegionsAndTerritories);
        mTerritoryRecyclerView.setAdapter(mTerritoryAdapter);
    }

    @OnClick(R.id.player_minus_button)
    public void onMinusClick() {
        if (mOldNumberOfPlayers > MIN_PLAYERS) {
            mNewNumberOfPlayers = mOldNumberOfPlayers - 1;
            mPlayerModel.removePlayer();
            update();
        }
    }

    @OnClick(R.id.player_plus_button)
    public void onPlusClick() {
        if (mOldNumberOfPlayers < MAX_PLAYERS) {
            mNewNumberOfPlayers = mOldNumberOfPlayers + 1;
            mPlayerModel.addPlayer();
            update();
        }
    }

    private void update() {
        mNbOfPlayersTextView.setText("" + mNewNumberOfPlayers);

        if (mNewNumberOfPlayers == MIN_PLAYERS && mOldNumberOfPlayers == 3) {
            mListOfRegions = mEssosMap.getRegions();
            flatten();
            mTerritoryAdapter.notifyDataSetChanged();
        } else if ((mNewNumberOfPlayers > MIN_PLAYERS && mNewNumberOfPlayers <= MAX_PLAYERS - MIN_PLAYERS)
                && (mOldNumberOfPlayers == MIN_PLAYERS || mOldNumberOfPlayers > MAX_PLAYERS - MIN_PLAYERS)) {
            mListOfRegions = mWesterosMap.getRegions();
            flatten();
            mTerritoryAdapter.notifyDataSetChanged();
        } else if (mNewNumberOfPlayers > MAX_PLAYERS - MIN_PLAYERS && mOldNumberOfPlayers == MAX_PLAYERS - MIN_PLAYERS) {
            mListOfRegions = mWesterosMap.getRegions();
            mListOfRegions.addAll(mEssosMap.getRegions());
            flatten();
            mTerritoryAdapter.notifyDataSetChanged();
        }

        mOldNumberOfPlayers = mNewNumberOfPlayers;
    }

    private void flatten() {
        mListOfRegionsAndTerritories.clear();
        for (Region region : mListOfRegions) {
            mListOfRegionsAndTerritories.add(region);
            for (Territory territory : region.getTerritories()) {
                mListOfRegionsAndTerritories.add(territory);
            }
        }
    }

    private void init() {
        getMaps();
        mOldNumberOfPlayers = MIN_PLAYERS;
        mListOfRegions = mEssosMap.getRegions();
        mListOfRegionsAndTerritories = new ArrayList<>();
        flatten();
    }

    /**
     * Parses the json files for Westeros and Essos maps.
     */
    private void getMaps() {
        InputStream inputStream = this.getResources().openRawResource(R.raw.westeros_territories);
        String westerosJsonString = readJsonFile(inputStream);

        inputStream = getResources().openRawResource(R.raw.essos_territories);
        String essosJsonString = readJsonFile(inputStream);

        Gson gson = new Gson();
        mWesterosMap = gson.fromJson(westerosJsonString, Map.class);
        mEssosMap = gson.fromJson(essosJsonString, Map.class);
    }

    /**
     * Reads json file.
     *
     * @param inputStream the input stream
     * @return the json string
     */
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
