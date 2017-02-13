package fr.goui.riskgameofthroneshelper.event;

import fr.goui.riskgameofthroneshelper.model.Player;

/**
 *
 */
public class PlayerClickEvent {

    private Player player;

    private int oldColorIndex;

    public PlayerClickEvent(Player player, int oldColorIndex) {
        this.player = player;
        this.oldColorIndex = oldColorIndex;
    }

    public Player getPlayer() {
        return player;
    }

    public int getOldColorIndex() {
        return oldColorIndex;
    }
}
