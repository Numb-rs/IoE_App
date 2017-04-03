package internetofeveryone.ioe.Website;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import icepick.Icepick;
import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import us.feras.mdv.MarkdownView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Website page
 */
public class WebsiteActivity extends AppCompatActivity implements WebsiteView, LoaderManager.LoaderCallbacks<WebsitePresenter> {

    private WebsitePresenter presenter;
    private static final int LOADER_ID = 108; // unique identification for the WebsiteActivity-LoaderManager
    private MarkdownView markdownView;
    private ProgressBar spinner;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        spinner = (ProgressBar)findViewById(R.id.progressBarWebsite);
        spinner.setVisibility(VISIBLE);
    }

    /**
     * Displays the content of the Website to the user
     *
     * @param content of the website
     */
    public void displayWebsite(String content) {
        markdownView = new MarkdownView(this);
        markdownView.setWebViewClient(new WebViewClient()
        {
            // Override URL
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                openNewPage(url);
                return true;
            }
        });
        setContentView(markdownView);
        markdownView.loadMarkdown(content);
    }

    /**
     * Displays a message to the user
     */
    public void displayMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void displayNetworkErrorMessage() {
        errorMessage = this.getString(R.string.networkFailure);
        WebsiteActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(WebsiteActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void dataChanged() {}

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);

        String urlPassed = getIntent().getStringExtra(BrowserActivity.URL);
        String enginePassed = getIntent().getStringExtra(BrowserActivity.ENGINE);
        String searchTermPassed = getIntent().getStringExtra(BrowserActivity.SEARCHTERM);
        if (urlPassed != null) {
            presenter.onURLPassed(urlPassed);
        } else {
            presenter.onSearchRequest(enginePassed, searchTermPassed);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
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

    @Override public void onSaveInstanceState(Bundle outState) {

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


    }

    @Override
    public void onLoaderReset(Loader<WebsitePresenter> loader) {
        presenter = null;

    }

    /**
     * opens a new website
     * @param url of the new website
     */
    private void openNewPage(String url) {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra(BrowserActivity.URL, url);
        startActivity(intent);
    }

    public MarkdownView getMarkdownView() {
        return markdownView;
    }

    /**
     * displays loading bar
     */
    public void displayLoader() {
        spinner.setVisibility(VISIBLE);
    }

    /**
     * closes loading bar
     */
    public void closeLoader() {
        spinner.setVisibility(GONE);
    }
}

