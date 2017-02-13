package fr.goui.riskgameofthroneshelper.event;

import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryClickEvent {

    private Territory territory;

    public TerritoryClickEvent(Territory territory) {
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }
}
