package fr.goui.riskgameofthroneshelper.main;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.goui.riskgameofthroneshelper.R;
import fr.goui.riskgameofthroneshelper.model.ListItem;
import fr.goui.riskgameofthroneshelper.model.Map;
import fr.goui.riskgameofthroneshelper.model.PlayerModel;
import fr.goui.riskgameofthroneshelper.model.Region;
import fr.goui.riskgameofthroneshelper.model.RegionModel;
import fr.goui.riskgameofthroneshelper.model.Territory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */
class MainPresenter implements IMainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int MAP_ESSOS = 0;
    private static final int MAP_WESTEROS = 1;
    private static final int MAP_ESSOS_AND_WESTEROS = 2;

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 7;

    private int mOldNumberOfPlayers;
    private int mNewNumberOfPlayers;

    private PlayerModel mPlayerModel;

    private Map mWesterosMap;
    private Map mEssosMap;

    private List<Region> mListOfRegions;
    private List<ListItem> mListOfRegionsAndTerritories;

    private boolean mIsOK;
    private CompositeSubscription mSubscriptions;
    private Subscription mGettingMapSubscription;
    private Subscription mSettingItemListSubscription;

    private IMainView mView;

    @Override
    public void attachView(IMainView view) {
        mView = view;
        init();
    }

    /**
     * Initialization method.
     */
    private void init() {
        mSubscriptions = new CompositeSubscription();

        mPlayerModel = PlayerModel.getInstance();
        mOldNumberOfPlayers = MIN_PLAYERS;
        mNewNumberOfPlayers = MIN_PLAYERS;

        mListOfRegions = new ArrayList<>();
        mListOfRegionsAndTerritories = new ArrayList<>();
        mView.updateListOfRegionAndTerritories(mListOfRegionsAndTerritories);

        getMaps();
    }

    /**
     * Observable to get all the maps.
     *
     * @return observable to subscribe to
     */
    private Observable<Boolean> getMapsObservable() {
        Observable observable = Observable.create(new Observable.OnSubscribe<Subscriber<Boolean>>() {
            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext(getWesterosMap());
                subscriber.onNext(getEssosMap());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * Observable to get all the list items.
     *
     * @param map for which map
     * @return observable to subscribe to
     */
    private Observable<Boolean> getListItem(final int map) {
        Observable observable = Observable.create(new Observable.OnSubscribe<Subscriber<Boolean>>() {
            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext(updateListOfRegions(map));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // subscribeOn the I/O thread
                .observeOn(AndroidSchedulers.mainThread()); // observeOn the UI Thread
        return observable;
    }

    /**
     * Gets the maps from local json file in background.
     */
    private void getMaps() {
        if (mGettingMapSubscription != null) {
            mGettingMapSubscription.unsubscribe();
        }
        mGettingMapSubscription = getMapsObservable().subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                if (mIsOK) {
                    setItemList(MAP_ESSOS);
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.getMessage());
            }

            @Override
            public void onNext(Boolean success) {
                mIsOK = success;
            }
        });
        mSubscriptions.add(mGettingMapSubscription);
    }

    /**
     * Gets the regions and territories from the specific map.
     *
     * @param map the specific map
     */
    private void setItemList(int map) {
        if (mSettingItemListSubscription != null) {
            mSettingItemListSubscription.unsubscribe();
        }
        mSettingItemListSubscription = getListItem(map).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                if (mIsOK) {
                    mView.updateListOfRegionAndTerritories(mListOfRegionsAndTerritories);
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.getMessage());
            }

            @Override
            public void onNext(Boolean success) {
                mIsOK = success;
            }
        });
        mSubscriptions.add(mSettingItemListSubscription);
    }

    @Override
    public void onMinusClick() {
        if (mOldNumberOfPlayers > MIN_PLAYERS) {
            mNewNumberOfPlayers = mOldNumberOfPlayers - 1;
            mPlayerModel.removePlayer();
            update();
        }
    }

    @Override
    public void onPlusClick() {
        if (mOldNumberOfPlayers < MAX_PLAYERS) {
            mNewNumberOfPlayers = mOldNumberOfPlayers + 1;
            mPlayerModel.addPlayer();
            update();
        }
    }

    /**
     * After a player adding or removing, updates the list of territories if needeed.
     */
    private void update() {
        mView.showProgressBar();

        // if number of players was 2 and now it's 3
        // if number of players was 6 and now it's 5
        if ((mOldNumberOfPlayers == 2 && mNewNumberOfPlayers == 3)
                || (mOldNumberOfPlayers == 6 && mNewNumberOfPlayers == 5)) { // only westeros
            setItemList(MAP_WESTEROS);
        }
        // if number of players was 3 and now it's 2
        else if (mOldNumberOfPlayers == 3 && mNewNumberOfPlayers == 2) { // only essos
            setItemList(MAP_ESSOS);
        }
        // if number of players was 5 and now it's 6
        else if (mOldNumberOfPlayers == 5 && mNewNumberOfPlayers == 6) { // essos and westeros
            setItemList(MAP_ESSOS_AND_WESTEROS);
        }

        mView.setNumberOfPlayers(mNewNumberOfPlayers);
        mView.hideProgressBar();

        mOldNumberOfPlayers = mNewNumberOfPlayers;
    }

    /**
     * Updates the list of regions because the map has changed.
     *
     * @param map the new map
     * @return the success boolean
     */
    private boolean updateListOfRegions(int map) {
        switch (map) {
            case MAP_ESSOS:
                mListOfRegions.clear();
                mListOfRegions.addAll(mEssosMap.getRegions());
                break;
            case MAP_WESTEROS:
                mListOfRegions.clear();
                mListOfRegions.addAll(mWesterosMap.getRegions());
                break;
            case MAP_ESSOS_AND_WESTEROS:
                mListOfRegions.clear();
                mListOfRegions.addAll(mWesterosMap.getRegions());
                mListOfRegions.addAll(mEssosMap.getRegions());
                break;
        }
        RegionModel.getInstance().setRegions(mListOfRegions);
        return flatten();
    }

    /**
     * Flattens the list of regions and territories to put them on the same level.
     *
     * @return the success boolean
     */
    private boolean flatten() {
        mListOfRegionsAndTerritories.clear();
        int globalIndex = 0;
        for (int i = 0; i < mListOfRegions.size(); i++) {
            Region region = mListOfRegions.get(i);
            mListOfRegionsAndTerritories.add(region);
            for (Territory territory : region.getTerritories()) {
                territory.setRegionIndex(i);
                territory.setGlobalRegionIndex(globalIndex);
                mListOfRegionsAndTerritories.add(territory);
            }
            globalIndex += region.getTerritories().size() + 1;
        }
        return true;
    }

    /**
     * Gets the westeros map from a local json file.
     *
     * @return the success boolean
     */
    private boolean getWesterosMap() {
        InputStream inputStream = mView.getContext().getResources().openRawResource(R.raw.westeros_territories);
        String jsonString = readJsonFile(inputStream);
        Gson gson = new Gson();
        mWesterosMap = gson.fromJson(jsonString, Map.class);
        return true;
    }

    /**
     * Gets the essos map from a local json file.
     *
     * @return the success boolean
     */
    private boolean getEssosMap() {
        InputStream inputStream = mView.getContext().getResources().openRawResource(R.raw.essos_territories);
        String jsonString = readJsonFile(inputStream);
        Gson gson = new Gson();
        mEssosMap = gson.fromJson(jsonString, Map.class);
        return true;
    }

    /**
     * Reads json file.
     *
     * @param inputStream the input stream
     * @return the json string
     */
    private String readJsonFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte bufferByte[] = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(bufferByte)) != -1) {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return outputStream.toString();
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }
}
