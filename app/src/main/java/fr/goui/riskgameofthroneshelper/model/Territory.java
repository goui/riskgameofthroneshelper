package fr.goui.riskgameofthroneshelper.model;

/**
 *
 */
public class Territory implements ListItem {

    private String name;

    private int castle;

    private int port;

    private int colorIndex = -1;

    private int regionIndex;

    private int globalRegionIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCastle() {
        return castle;
    }

    public void setCastle(int castle) {
        this.castle = castle;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int getRegionIndex() {
        return regionIndex;
    }

    public void setRegionIndex(int regionIndex) {
        this.regionIndex = regionIndex;
    }

    public int getGlobalRegionIndex() {
        return globalRegionIndex;
    }

    public void setGlobalRegionIndex(int globalRegionIndex) {
        this.globalRegionIndex = globalRegionIndex;
    }

    public int getTroopsCount() {
        return 1 + castle;
    }

    public int getEndingTroopsCount() {
        return 1 + castle + port;
    }
}
