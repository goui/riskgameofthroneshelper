package fr.goui.riskgameofthroneshelper.model;

import java.util.List;

/**
 *
 */
public class Map {

    private String name;

    private List<Region> regions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
}
