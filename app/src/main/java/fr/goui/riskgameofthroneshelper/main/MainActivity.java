package fr.goui.riskgameofthroneshelper.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import fr.goui.riskgameofthroneshelper.event.PlayerClickEvent;
import fr.goui.riskgameofthroneshelper.event.TerritoryClickEvent;
import fr.goui.riskgameofthroneshelper.model.ListItem;

public class MainActivity extends AppCompatActivity implements IMainView {

    private List<ListItem> mListOfRegionsAndTerritories;

    private IMainPresenter mPresenter;

    private TerritoryController mTerritoryController;

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
    public void onPlayerClickEvent(PlayerClickEvent event) {
        // TODO player clicked => change all his territories color *optional*
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
