package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.model.Player;

/**
 *
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<Player> mListOfPlayers;

    public PlayerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListOfPlayers = new ArrayList<>();
        addPlayer();
        addPlayer();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerViewHolder(mLayoutInflater.inflate(R.layout.item_player, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = mListOfPlayers.get(position);
        if (player != null) {
            holder.mPlayerTroopsTextView.setText("" + player.getTroops());
            holder.mPlayerTroopsTextView.setBackgroundColor(getColor(position));
        }
    }

    private int getColor(int position) {
        TypedArray ta = mContext.getResources().obtainTypedArray(R.array.player_color);
        int color = ta.getColor(position, -1);
        ta.recycle();
        return color;
    }

    public void addPlayer() {
        Player player = new Player();
        player.setTroops(0);
        mListOfPlayers.add(player);
        notifyDataSetChanged();
    }

    public void deletePlayer() {
        mListOfPlayers.remove(mListOfPlayers.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mListOfPlayers.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.player_troops_text_view)
        TextView mPlayerTroopsTextView;

        PlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
