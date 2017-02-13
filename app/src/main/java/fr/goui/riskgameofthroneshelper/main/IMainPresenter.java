package fr.goui.riskgameofthroneshelper.main;

import fr.goui.riskgameofthroneshelper.IPresenter;

/**
 *
 */
interface IMainPresenter extends IPresenter<IMainView> {

    void onMinusClick();

    void onPlusClick();
}
