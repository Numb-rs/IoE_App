package internetofeveryone.ioe.Downloads;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

public class DownloadsActivity extends AppCompatActivity implements DownloadsView, LoaderManager.LoaderCallbacks<DownloadsPresenter> {

    DownloadsPresenter presenter;
    DownloadsAdapter adapter;
    private static final int LOADER_ID = 102;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        getSupportActionBar().setTitle("Downloads");
        setContentView(R.layout.activity_downloads);

    }

    public void onClickDelete(String name) {
        presenter.deleteClicked(name);
    }
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


    public void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public Loader<DownloadsPresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(this, new DownloadsPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<DownloadsPresenter> loader, DownloadsPresenter presenter) {
        this.presenter = presenter;
        ListView listView = (ListView)findViewById(R.id.downloads_list);
        adapter = new DownloadsAdapter(presenter.getDownloadedWebsiteNames(), this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<DownloadsPresenter> loader) {
        presenter = null;

    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
