package internetofeveryone.ioe.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import icepick.Icepick;
import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.Downloads.DownloadsActivity;
import internetofeveryone.ioe.Messenger.MessengerActivity;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Main page
 */
public class MainActivity extends AppCompatActivity implements MainView, LoaderManager.LoaderCallbacks<MainPresenter> {

    private MainPresenter presenter;
    private static final int LOADER_ID = 101; // unique identification for the MainActivity-LoaderManager

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restores instance state
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {

        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onStop() {

        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void dataChanged() {} // doesn't have to update anything

    /**
     * Notifies the presenter that the user wants to navigate to the Messenger page
     *
     * @param view
     */
    public void onClickMessenger(View view) {
        presenter.onMessengerClicked();
    }

    /**
     * Navigates to the Messenger page
     */
    public void goToMessenger() {

        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }

    /**
     * Notifies the presenter that the user wants to navigate to the Downloads page
     *
     * @param view
     */
    public void onClickDownloads(View view) {
        presenter.onDownloadsClicked();
    }

    /**
     * Navigates to the Downloads page
     */
    public void goToDownloads() {

        Intent intent = new Intent(this, DownloadsActivity.class);
        startActivity(intent);
    }

    /**
     * Notifies the presenter that the user wants to navigate to the Browser page
     *
     * @param view
     */
    public void onClickBrowser(View view) {
        presenter.onBrowserClicked();
    }

    /**
     * Navigates to the Browser page
     */
    public void goToBrowser() {

        Intent intent = new Intent(this, BrowserActivity.class);
        startActivity(intent);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // saves instance state
    }

    @Override
    public Loader<MainPresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(this, new MainPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<MainPresenter> loader, MainPresenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void onLoaderReset(Loader<MainPresenter> loader) {
        presenter = null;

    }



}
