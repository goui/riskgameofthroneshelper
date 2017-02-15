package fr.goui.riskgameofthroneshelper.controller;

import fr.goui.riskgameofthroneshelper.event.TerritoryClickEvent;
import fr.goui.riskgameofthroneshelper.model.Player;
import fr.goui.riskgameofthroneshelper.model.PlayerModel;
import fr.goui.riskgameofthroneshelper.model.Region;
import fr.goui.riskgameofthroneshelper.model.RegionModel;
import fr.goui.riskgameofthroneshelper.model.Territory;

/**
 *
 */
public class TerritoryController {

    private static final int INVALID = -1;

    private PlayerModel mPlayerModel = PlayerModel.getInstance();
    private RegionModel mRegionModel = RegionModel.getInstance();

    private boolean isRegionControlled(Region region) {
        boolean ret = true;
        int firstColorIndex = region.getTerritories().get(0).getColorIndex();
        for (int i = 1; i < region.getTerritories().size(); i++) {
            if (region.getTerritories().get(i).getColorIndex() != firstColorIndex) {
                ret = false;
            }
        }
        return ret;
    }

    // region was controlled by old player and is not anymore => - bonus for old player
    // region was not controlled by old player and is now by new player => + bonus for new player
    // region was not controlled by old player and is not by new player => nothing
    public boolean onTerritoryClick(TerritoryClickEvent event) {
        int oldColorIndex = event.getOldColorIndex();
        int newColorIndex = event.getNewColorIndex();
        Territory territory = event.getTerritory();
        Player oldPlayer = mPlayerModel.findPlayerByColorIndex(oldColorIndex);
        Player newPlayer = mPlayerModel.findPlayerByColorIndex(newColorIndex);
        Region region = mRegionModel.getRegionByIndex(territory.getRegionIndex());

        boolean wasRegionControlledByOldPlayer = isRegionControlled(region);
        territory.setColorIndex(newColorIndex);
        boolean isRegionControlledByNewPlayer = isRegionControlled(region);

        boolean regionHasChanged = false;
        if (oldPlayer != null && newPlayer != null && wasRegionControlledByOldPlayer && isRegionControlledByNewPlayer) {
            oldPlayer.setRegionBonus(oldPlayer.getRegionBonus() - region.getBonus());
            newPlayer.setRegionBonus(newPlayer.getRegionBonus() + region.getBonus());
            region.setColorIndex(newColorIndex);
            regionHasChanged = true;
        } else if (newPlayer != null && !wasRegionControlledByOldPlayer && isRegionControlledByNewPlayer) {
            newPlayer.setRegionBonus(newPlayer.getRegionBonus() + region.getBonus());
            region.setColorIndex(newColorIndex);
            regionHasChanged = true;
        } else if (oldPlayer != null && wasRegionControlledByOldPlayer && !isRegionControlledByNewPlayer) {
            oldPlayer.setRegionBonus(oldPlayer.getRegionBonus() - region.getBonus());
            region.setColorIndex(INVALID);
            regionHasChanged = true;
        }

        if (oldPlayer != null) {
            oldPlayer.setTerritoriesPoints(oldPlayer.getTerritoriesPoints() - territory.getTroopsCount());
        }
        if (newPlayer != null) {
            newPlayer.setTerritoriesPoints(newPlayer.getTerritoriesPoints() + territory.getTroopsCount());
        }

        return regionHasChanged;
    }

    public void endGame() {
        PlayerModel playerModel = PlayerModel.getInstance();
        playerModel.resetPlayers();
        for (Region region : mRegionModel.getRegions()) {
            for (Territory territory : region.getTerritories()) {
                Player player = playerModel.findPlayerByColorIndex(territory.getColorIndex());
                if (player != null) {
                    player.setTerritoriesPoints(player.getTerritoriesPoints() + territory.getEndingTroopsCount());
                }
            }
        }
    }
}
