package fr.goui.riskgameofthroneshelper.event;

import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryClickEvent {

    private Territory territory;

    private int oldColorIndex;

    public TerritoryClickEvent(Territory territory, int oldColorIndex) {
        this.territory = territory;
        this.oldColorIndex = oldColorIndex;
    }

    public Territory getTerritory() {
        return territory;
    }

    public int getOldColorIndex() {
        return oldColorIndex;
    }
}
