package fr.goui.riskgameofthroneshelper.event;

import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryClickEvent {

    private Territory territory;

    private int oldColorIndex;

    private int newColorIndex;

    private int adapterPosition;

    public TerritoryClickEvent(Territory territory, int adapterPosition, int oldColorIndex, int newColorIndex) {
        this.territory = territory;
        this.oldColorIndex = oldColorIndex;
        this.newColorIndex = newColorIndex;
        this.adapterPosition = adapterPosition;
    }

    public Territory getTerritory() {
        return territory;
    }

    public int getOldColorIndex() {
        return oldColorIndex;
    }

    public int getNewColorIndex() {
        return newColorIndex;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}
