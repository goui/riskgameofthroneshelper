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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.adapter.PlayerAdapter;
import fr.goui.riskgameofthroneshelper.adapter.TerritoryAdapter;
import fr.goui.riskgameofthroneshelper.model.ListItem;

public class MainActivity extends AppCompatActivity implements IMainView {

    private List<ListItem> mListOfRegionsAndTerritories;

    private IMainPresenter mPresenter;

    /* PLAYER */

    @BindView(R.id.player_number_text_view)
    TextView mNbOfPlayersTextView;

    @BindView(R.id.player_progress_bar)
    ProgressBar mPlayerProgressBar;

    @BindView(R.id.player_recycler_view)
    RecyclerView mPlayerRecyclerView;

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
        mPlayerRecyclerView.setAdapter(new PlayerAdapter(this));

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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
