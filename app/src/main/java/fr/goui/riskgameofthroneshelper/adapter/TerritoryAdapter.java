package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.event.TerritoryClickEvent;
import fr.goui.riskgameofthroneshelper.model.ListItem;
import fr.goui.riskgameofthroneshelper.model.PlayerModel;
import fr.goui.riskgameofthroneshelper.model.Region;
import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_REGION = 1;
    private static final int TYPE_TERRITORY = 2;

    private static final int INVALID = -1;

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<ListItem> mItems;

    public TerritoryAdapter(Context context, List<ListItem> items) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REGION) {
            return new RegionViewHolder(mLayoutInflater.inflate(R.layout.item_region, parent, false));
        } else {
            return new TerritoryViewHolder(mLayoutInflater.inflate(R.layout.item_territory, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (mItems.get(position) instanceof Region) {
            Region region = (Region) mItems.get(position);
            if (region != null) {
                RegionViewHolder rvh = (RegionViewHolder) holder;
                rvh.regionNameTextView.setText(region.getName());
                if (region.getColorIndex() > -1) {
                    rvh.regionNameTextView.setBackgroundColor(getColor(region.getColorIndex()));
                } else {
                    rvh.regionNameTextView.setBackgroundResource(android.R.color.transparent);
                }
            }
        } else {
            final Territory territory = (Territory) mItems.get(position);
            if (territory != null) {
                final TerritoryViewHolder tvh = (TerritoryViewHolder) holder;
                tvh.territoryNameTextView.setText(territory.getName());
                if (territory.getColorIndex() > -1) {
                    tvh.territoryNameTextView.setBackgroundColor(getColor(territory.getColorIndex()));
                } else {
                    tvh.territoryNameTextView.setBackgroundResource(android.R.color.transparent);
                }
                tvh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int oldColorIndex = territory.getColorIndex();
                        int newColorIndex = pickNextColorIndex(territory);
                        EventBus.getDefault().post(new TerritoryClickEvent(territory, tvh.getAdapterPosition(), oldColorIndex, newColorIndex));
                    }
                });
                tvh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int oldColorIndex = territory.getColorIndex();
                        tvh.territoryNameTextView.setBackgroundResource(android.R.color.transparent);
                        EventBus.getDefault().post(new TerritoryClickEvent(territory, tvh.getAdapterPosition(), oldColorIndex, INVALID));
                        return true;
                    }
                });
            }
        }
    }

    /**
     * Picks next available color.
     *
     * @param territory the clicked territory
     * @return the new color's index
     */
    private int pickNextColorIndex(Territory territory) {
        int colorIndex = territory.getColorIndex();
        do {
            colorIndex++;
            if (colorIndex > 6) {
                colorIndex = 0;
            }
        } while (!PlayerModel.getInstance().getPickedColors().contains(colorIndex));
        return colorIndex;
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

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof Region) {
            return TYPE_REGION;
        } else {
            return TYPE_TERRITORY;
        }
    }

    static class TerritoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.territory_name_text_view)
        TextView territoryNameTextView;

        TerritoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class RegionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.region_name_text_view)
        TextView regionNameTextView;

        RegionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
