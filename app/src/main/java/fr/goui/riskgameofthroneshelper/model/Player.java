package fr.goui.riskgameofthroneshelper.model;

/**
 *
 */
public class Player {

    private static final int MIN_TROOPS = 3;

    private int territoriesPoints;

    private int regionBonus;

    private int troops = 3;

    private int colorIndex;

    public int getTerritoriesPoints() {
        return territoriesPoints;
    }

    public void setTerritoriesPoints(int territoriesPoints) {
        this.territoriesPoints = territoriesPoints;
        updateTroops();
    }

    public int getRegionBonus() {
        return regionBonus;
    }

    public void setRegionBonus(int regionBonus) {
        this.regionBonus = regionBonus;
        updateTroops();
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int getTroops() {
        return troops;
    }

    private void updateTroops() {
        troops = Math.max(territoriesPoints / MIN_TROOPS + regionBonus, MIN_TROOPS);
    }
}
