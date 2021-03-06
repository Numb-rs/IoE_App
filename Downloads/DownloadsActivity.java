package internetofeveryone.ioe.Downloads;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import us.feras.mdv.MarkdownView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Downloads page
 */
public class DownloadsActivity extends AppCompatActivity implements DownloadsView, LoaderManager.LoaderCallbacks<DownloadsPresenter> {

    private DownloadsPresenter presenter;
    private ListView listView;
    /**
     * Adapter for the ListView
     * It's responsible for displaying data in the list
     */
    private DownloadsAdapter adapter;
    private static final int LOADER_ID = 102; // unique identification for the DownloadsActivity-LoaderManager
    private MarkdownView markdownView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restores instance state
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_downloads);
        }
        setContentView(R.layout.activity_downloads);
    }

    /**
     * Notifies the presenter that the user wants to delete a downloaded Website
     *
     * @param name name of the downloaded Website
     */
    public void onClickDelete(String name) {
        presenter.deleteClicked(name);
    }

    /**
     * Notifies the presenter that the user wants to open a downloaded Website
     *
     * @param name name of the downloaded Website
     */
    public void onClickOpen(String name) {
        presenter.openClicked(name);
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

    /**
     * Opens a Website
     * @param content Content of the Website
     */
    public void displayContent(String content) {
        markdownView = new MarkdownView(this);
        markdownView.setWebViewClient(new WebViewClient()
        {
            // Override URL
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Toast.makeText(DownloadsActivity.this, "Links are not supported in offline version", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        setContentView(markdownView);
        markdownView.loadMarkdown(content);
    }

    /**
     * updates the ListView
     */
    public void dataChanged() {
        listView = (ListView)findViewById(R.id.downloads_list);
        adapter = new DownloadsAdapter(presenter.getDownloadedWebsiteNames(), this);
        listView.setAdapter(adapter);
    }

    @Override
    public Loader<DownloadsPresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(this, new DownloadsPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<DownloadsPresenter> loader, DownloadsPresenter presenter) {

        this.presenter = presenter;
        // sets up the ListView after the load has finished
        listView = (ListView)findViewById(R.id.downloads_list);
        adapter = new DownloadsAdapter(presenter.getDownloadedWebsiteNames(), this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<DownloadsPresenter> loader) {
        presenter = null;

    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // save instance state
    }


    public DownloadsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DownloadsAdapter adapter) {
        this.adapter = adapter;
    }

    public MarkdownView getMarkdownView() {
        return markdownView;
    }

    public void setMarkdownView(MarkdownView markdownView) {
        this.markdownView = markdownView;
    }
}
