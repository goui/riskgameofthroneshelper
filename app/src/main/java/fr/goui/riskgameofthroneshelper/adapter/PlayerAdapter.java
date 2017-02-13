package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<Integer> mPickedColors;

    public PlayerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListOfPlayers = new ArrayList<>();
        mPickedColors = new HashSet<>();
        addPlayer();
        addPlayer();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerViewHolder(mLayoutInflater.inflate(R.layout.item_player, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {
        final Player player = mListOfPlayers.get(position);
        if (player != null) {
            holder.mPlayerTroopsTextView.setText("" + player.getTroops());
            holder.mPlayerTroopsTextView.setBackgroundColor(getColor(player.getColorIndex()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListOfPlayers.size() < 7 && mListOfPlayers.size() > 2) { // preventing picking color when max players or only 2 players
                        holder.mPlayerTroopsTextView.setBackgroundColor(pickNextColor(player));
                    }
                }
            });
        }
    }

    /**
     * Gets a color from the player_color xml resource.
     *
     * @param position the color position
     * @return the color's resource id
     */
    private int getColor(int position) {
        TypedArray ta = mContext.getResources().obtainTypedArray(R.array.player_color);
        int color = ta.getColor(position, -1);
        ta.recycle();
        return color;
    }

    /**
     * Picks next available color.
     *
     * @param player the clicked player
     * @return the new color's resource id
     */
    private int pickNextColor(Player player) {
        int colorIndex = player.getColorIndex();
        do {
            colorIndex++;
            if (colorIndex > 6) {
                colorIndex = 0;
            }
        } while (mPickedColors.contains(colorIndex));

        mPickedColors.remove(player.getColorIndex());
        player.setColorIndex(colorIndex);
        mPickedColors.add(colorIndex);
        return getColor(colorIndex);
    }

    /**
     * Adds a new player.
     */
    public void addPlayer() {
        int colorIndex = 0;
        for (int i = 0; i < 7; i++) {
            if (!mPickedColors.contains(i)) {
                colorIndex = i;
                break; // sorry Cyril
            }
        }

        Player player = new Player();
        player.setTroops(0);
        player.setColorIndex(colorIndex);
        mListOfPlayers.add(player);
        mPickedColors.add(colorIndex);

        notifyDataSetChanged();
    }

    /**
     * Deletes last player.
     */
    public void deletePlayer() {
        Player player = mListOfPlayers.get(mListOfPlayers.size() - 1);
        mListOfPlayers.remove(player);
        mPickedColors.remove(player.getColorIndex());

        if (mListOfPlayers.size() == 2) {
            reset();
        }

        notifyDataSetChanged();
    }

    /**
     * Resets the default color assignement.
     */
    private void reset() {
        mListOfPlayers.get(0).setColorIndex(0);
        mListOfPlayers.get(1).setColorIndex(1);
        mPickedColors.clear();
        mPickedColors.add(0);
        mPickedColors.add(1);
    }

    @Override
    public int getItemCount() {
        return mListOfPlayers.size();
    }

    /**
     * View holder for the player item.
     */
    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.player_troops_text_view)
        TextView mPlayerTroopsTextView;

        PlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
