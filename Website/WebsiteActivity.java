package internetofeveryone.ioe.Website;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import icepick.Icepick;
import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import us.feras.mdv.MarkdownView;


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
        MarkdownView markdownView = new MarkdownView(this);
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
    public void onLoaderReset(Loader<WebsitePresenter> loader) {
        presenter = null;

    }

    /**
     * opens a new website
     * @param url of the new website
     */
    public void openNewPage(String url) {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra(BrowserActivity.URL, url);
        startActivity(intent);
    }
}

