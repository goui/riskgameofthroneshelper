package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.model.ListItem;
import fr.goui.riskgameofthroneshelper.model.Region;
import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_REGION = 1;
    public static final int TYPE_TERRITORY = 2;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItems.get(position) instanceof Region) {
            Region region = (Region) mItems.get(position);
            if (region != null) {
                RegionViewHolder rvh = (RegionViewHolder) holder;
                rvh.regionNameTextView.setText(region.getName());
                rvh.regionNameTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorCyan)); // TODO assign color of player having it
            }
        } else {
            Territory territory = (Territory) mItems.get(position);
            if (territory != null) {
                TerritoryViewHolder tvh = (TerritoryViewHolder) holder;
                tvh.territoryNameTextView.setText(territory.getName());
                tvh.territoryNameTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorCyan)); // TODO assign color of player having it
            }
        }
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

        public TerritoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class RegionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.region_name_text_view)
        TextView regionNameTextView;

        public RegionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
