package internetofeveryone.ioe.Website;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;


/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Website page
 */
public class WebsiteActivity extends AppCompatActivity implements WebsiteView, LoaderManager.LoaderCallbacks<WebsitePresenter> {

    private WebsitePresenter presenter;
    private TextView textView;
    private static final int LOADER_ID = 108; // unique identification for the WebsiteActivity-LoaderManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        textView = (TextView) findViewById(R.id.website);
    }

    /**
     * Displays the content of the Website to the user
     *
     * @param content of the website
     */
    public void displayWebsite(String content) {
        // TODO: display the content
        textView.setText(content);
    }

    /**
     * Displays an error message to the user
     */
    public void displayErrorMessage() {
        textView.setText(R.string.error_message_website);
    }

    /**
     * Notifies the presenter that a link has been clicked
     *
     * @param url URL of the new Website
     */
    public void onClickLink(String url) {
        presenter.linkClicked(url);
    }

    @Override
    public void dataChanged() {}

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

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // saves instance state
    }

    @Override
    public Loader<WebsitePresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(this, new WebsitePresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<WebsitePresenter> loader, WebsitePresenter presenter) {

        this.presenter = presenter;
        String urlPassed = getIntent().getStringExtra("URL");
        presenter.onURLPassed(urlPassed);
    }

    @Override
    public void onLoaderReset(Loader<WebsitePresenter> loader) {
        presenter = null;

    }
}

