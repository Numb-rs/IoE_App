package internetofeveryone.ioe.Browser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import icepick.Icepick;
import internetofeveryone.ioe.DefaultWebsites.DefaultWebsiteFragment;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Website.WebsiteActivity;

public class BrowserActivity extends AppCompatActivity implements BrowserView, LoaderManager.LoaderCallbacks<BrowserPresenter> {

    private String url = "";
    private EditText txtDescription;
    private BrowserPresenter presenter;
    ArrayAdapter<String> spinnerArrayAdapter;
    Spinner spinner;
    private static final int LOADER_ID = 106;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        getSupportActionBar().setTitle("Browser");
        setContentView(R.layout.activity_browser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default_websites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.change_default_websites) {
            DefaultWebsiteFragment fragment = new DefaultWebsiteFragment();
            fragment.show(getSupportFragmentManager(), "changeDefaultWebsites");
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickEnter(View view) {
        url = txtDescription.getText().toString();
        presenter.websiteSelectedbyURL(url);
    }

    public void onClickOpen(View view) {
        presenter.onOpenClicked();
    }

    public void onClickDownload(View view) {
        presenter.onDownloadClicked();
    }

    public void goToURL(String url) {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
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

    public void dataChanged() {
        spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                presenter.getDefaultWebsiteNames());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public Loader<BrowserPresenter> onCreateLoader(int id, Bundle arg){

        return new PresenterLoader<>(this, new BrowserPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<BrowserPresenter> loader, final BrowserPresenter presenter) {
        this.presenter = presenter;
        spinner = (Spinner) findViewById(R.id.dropdown_url);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String chosenWebsite = adapterView.getItemAtPosition(i).toString();
                presenter.websiteSelectedbyName(chosenWebsite);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                presenter.getDefaultWebsiteNames());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        txtDescription = (EditText) findViewById(R.id.textView_url);
    }

    @Override
    public void onLoaderReset(Loader<BrowserPresenter> loader) {
        presenter = null;

    }
}
