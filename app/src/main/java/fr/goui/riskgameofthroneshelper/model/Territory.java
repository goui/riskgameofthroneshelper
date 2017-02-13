package fr.goui.riskgameofthroneshelper.model;

/**
 *
 */
public class Territory {

    private String name;

    private int castle;

    private int port;

    public Territory(String name, int castle, int port) {
        this.name = name;
        this.castle = castle;
        this.port = port;
    }

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
}
