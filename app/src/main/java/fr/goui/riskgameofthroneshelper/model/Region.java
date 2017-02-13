package fr.goui.riskgameofthroneshelper.model;

import java.util.List;

/**
 *
 */
public class Region {

    private String name;

    private int bonus;

    private List<Territory> territories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Territory> territories) {
        this.territories = territories;
    }
}
