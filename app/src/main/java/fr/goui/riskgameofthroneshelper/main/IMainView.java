package fr.goui.riskgameofthroneshelper.main;

import java.util.List;

import fr.goui.riskgameofthroneshelper.IView;
import fr.goui.riskgameofthroneshelper.model.ListItem;

/**
 *
 */
interface IMainView extends IView {

    void updateListOfRegionAndTerritories(List<ListItem> listOfRegionsAndTerritories);

    void updateMapName(String mapName);

    void setNumberOfPlayers(int numberOfPlayers);
}
