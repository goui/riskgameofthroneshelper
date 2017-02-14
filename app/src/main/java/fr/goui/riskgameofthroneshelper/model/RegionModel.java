package fr.goui.riskgameofthroneshelper.model;

import java.util.List;

/**
 *
 */
public class RegionModel {

    private static RegionModel instance;

    private List<Region> regions;

    private RegionModel() {
    }

    public static RegionModel getInstance() {
        if (instance == null) {
            instance = new RegionModel();
        }
        return instance;
    }

    public Region getRegionByIndex(int index) {
        return regions.get(index);
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
}
