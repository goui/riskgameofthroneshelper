package fr.goui.riskgameofthroneshelper.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.adapter.PlayerAdapter;
import fr.goui.riskgameofthroneshelper.adapter.TerritoryAdapter;
import fr.goui.riskgameofthroneshelper.controller.TerritoryController;
import fr.goui.riskgameofthroneshelper.event.TerritoryClickEvent;
import fr.goui.riskgameofthroneshelper.model.ListItem;

public class MainActivity extends AppCompatActivity implements IMainView {

    private List<ListItem> mListOfRegionsAndTerritories;

    private IMainPresenter mPresenter;

    private TerritoryController mTerritoryController;

    @BindView(R.id.start_game_button)
    ImageView mStartGameButton;

    @BindView(R.id.end_game_button)
    ImageView mEndGameButton;

    @BindView(R.id.player_minus_button)
    Button mMinusButton;

    @BindView(R.id.player_plus_button)
    Button mPlusButton;

    @BindView(R.id.territory_offset_view)
    TextView mOffsetView;

    @BindView(R.id.map_name_text_view)
    TextView mMapNameTextView;

    /* PLAYER */

    @BindView(R.id.player_number_text_view)
    TextView mNbOfPlayersTextView;

    @BindView(R.id.player_progress_bar)
    ProgressBar mPlayerProgressBar;

    @BindView(R.id.player_recycler_view)
    RecyclerView mPlayerRecyclerView;

    private PlayerAdapter mPlayerAdapter;

    /* TERRITORY */

    @BindView(R.id.territory_progress_bar)
    ProgressBar mTerritoryProgressBar;

    @BindView(R.id.territory_recycler_view)
    RecyclerView mTerritoryRecyclerView;

    private TerritoryAdapter mTerritoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mEndGameButton.setEnabled(false);

        mPresenter = new MainPresenter();
        mPresenter.attachView(this);

        mPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlayerRecyclerView.setHasFixedSize(true);
        mPlayerAdapter = new PlayerAdapter(this);
        mPlayerRecyclerView.setAdapter(mPlayerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mTerritoryRecyclerView.setLayoutManager(gridLayoutManager);
        mTerritoryRecyclerView.setHasFixedSize(true);
        mTerritoryAdapter = new TerritoryAdapter(this, mListOfRegionsAndTerritories);
        mTerritoryRecyclerView.setAdapter(mTerritoryAdapter);

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
    }

    @OnClick(R.id.player_minus_button)
    public void onMinusClick() {
        mPresenter.onMinusClick();
    }

    @OnClick(R.id.player_plus_button)
    public void onPlusClick() {
        mPresenter.onPlusClick();
    }

    @OnClick(R.id.start_game_button)
    public void onGameStartClick() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.Start_game_qm)
                .setMessage(R.string.Start_game_caution)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int wich) {
                        startGame();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startGame() {
        mStartGameButton.setEnabled(false);
        mEndGameButton.setEnabled(true);
        mMinusButton.setEnabled(false);
        mPlusButton.setEnabled(false);
        mPlayerAdapter.gameHasStarted(true);
        mOffsetView.setVisibility(View.GONE);
        mMapNameTextView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.end_game_button)
    public void onGameEndClick() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.Game_over_qm)
                .setMessage(R.string.Game_over_caution)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int wich) {
                        endGame();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void endGame() {
        mEndGameButton.setEnabled(false);
        mTerritoryController.endGame();
        mPlayerAdapter.endGame();
        mOffsetView.setText(getString(R.string.Congratulations_to_the_winning_player));
        mOffsetView.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showProgressBar() {
        mPlayerProgressBar.setVisibility(View.VISIBLE);
        mTerritoryProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mPlayerProgressBar.setVisibility(View.GONE);
        mTerritoryProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListOfRegionAndTerritories(List<ListItem> listOfRegionsAndTerritories) {
        mListOfRegionsAndTerritories = listOfRegionsAndTerritories;
        if (mTerritoryAdapter != null) {
            mTerritoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateMapName(String mapName) {
        mMapNameTextView.setText(String.format(getString(R.string.The_map_is_), mapName));
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        mNbOfPlayersTextView.setText("" + numberOfPlayers);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        mTerritoryController = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTerritoryController = new TerritoryController();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTerritoryClickEvent(TerritoryClickEvent event) {
        boolean regionHasChanged = mTerritoryController.onTerritoryClick(event);
        mTerritoryAdapter.notifyItemChanged(event.getAdapterPosition());
        if (regionHasChanged) { // hack to prevent scrolling to top when region changes
            mTerritoryAdapter.notifyItemChanged(event.getTerritory().getGlobalRegionIndex());
        }
        mPlayerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
