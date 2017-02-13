package fr.goui.riskgameofthroneshelper.adapter;

import android.content.Context;
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
import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryAdapter extends RecyclerView.Adapter<TerritoryAdapter.TerritoryViewHolder> {

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<Territory> mListOfTerritories;

    public TerritoryAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListOfTerritories = new ArrayList<>();
        init(); // TODO remove later
    }

    private void init() {
        Territory t1 = new Territory("King's Landing", 1, 1);
        Territory t2 = new Territory("Sandstone", 1, 1);
        Territory t3 = new Territory("Winterfell", 1, 0);
        Territory t4 = new Territory("Pyke", 1, 1);
        Territory t5 = new Territory("Cracklow Point", 0, 0);
        Territory t6 = new Territory("The Mander", 1, 0);
        Territory t7 = new Territory("Stony Shore", 0, 0);
        Territory t8 = new Territory("Riverrun", 1, 0);
        Territory t9 = new Territory("Harlaw", 0, 1);
        Territory t10 = new Territory("Highgarden", 1, 0);
        Territory t11 = new Territory("The Gift", 0, 0);
        Territory t12 = new Territory("Barrowlands", 1, 0);
        Territory t13 = new Territory("Tarth", 0, 0);
        Territory t14 = new Territory("Widow's Watch", 0, 1);
        mListOfTerritories.add(t1);
        mListOfTerritories.add(t2);
        mListOfTerritories.add(t3);
        mListOfTerritories.add(t4);
        mListOfTerritories.add(t5);
        mListOfTerritories.add(t6);
        mListOfTerritories.add(t7);
        mListOfTerritories.add(t8);
        mListOfTerritories.add(t9);
        mListOfTerritories.add(t10);
        mListOfTerritories.add(t11);
        mListOfTerritories.add(t12);
        mListOfTerritories.add(t13);
        mListOfTerritories.add(t14);
    }

    @Override
    public TerritoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TerritoryViewHolder(mLayoutInflater.inflate(R.layout.item_territory, parent, false));
    }

    @Override
    public void onBindViewHolder(TerritoryViewHolder holder, int position) {
        Territory territory = mListOfTerritories.get(position);
        if (territory != null) {
            holder.mNameTextView.setText(territory.getName());
            holder.mNameTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorCyan)); // TODO assign color of player having it
        }
    }

    @Override
    public int getItemCount() {
        return mListOfTerritories.size();
    }

    static class TerritoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.territory_name_text_view)
        TextView mNameTextView;

        public TerritoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
