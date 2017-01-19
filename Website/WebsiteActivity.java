package internetofeveryone.ioe.Website;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;


public class WebsiteActivity extends AppCompatActivity implements WebsiteView, LoaderManager.LoaderCallbacks<WebsitePresenter> {

    private WebsitePresenter presenter;
    private TextView textView;
    private static final int LOADER_ID = 108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Icepick.restoreInstanceState(this, savedInstanceState);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        textView = (TextView) findViewById(R.id.website);

        /*
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlPassed));
        startActivity(browserIntent);
        */
    }

    public void displayWebsite(String content) {
        // display
        textView.setText(content);
    }

    public void displayErrorMessage() {
        textView.setText("That didn't work :(");
    }

    public void onClickLink(String url) {
        presenter.linkClicked(url); // speicher in FIFO letzte Website um dann wieder zurückspringen zu können
    }

    @Override
    public void dataChanged() {

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

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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

