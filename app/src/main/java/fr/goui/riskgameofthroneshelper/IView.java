package fr.goui.riskgameofthroneshelper;

import android.content.Context;

/**
 * Interface for the default view.
 */
public interface IView {

    Context getContext();

    void showProgressBar();

    void hideProgressBar();

    void showMessage(String message);
}
