package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.event.PlayerClickEvent;
import fr.goui.riskgameofthroneshelper.event.TerritoryClickEvent;
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
                rvh.regionNameTextView.setBackgroundColor(getColor(region.getColorIndex()));
            }
        } else {
            final Territory territory = (Territory) mItems.get(position);
            if (territory != null) {
                TerritoryViewHolder tvh = (TerritoryViewHolder) holder;
                tvh.territoryNameTextView.setText(territory.getName());
                tvh.territoryNameTextView.setBackgroundColor(getColor(territory.getColorIndex()));
                tvh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        EventBus.getDefault().post(new TerritoryClickEvent(territory));
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerClickEvent(PlayerClickEvent event) {
        // TODO player click
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromRecyclerView(recyclerView);
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
