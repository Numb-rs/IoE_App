package internetofeveryone.ioe.Browser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import icepick.Icepick;
import internetofeveryone.ioe.DefaultWebsites.DefaultWebsiteFragment;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Website.WebsiteActivity;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Browser page
 */
public class BrowserActivity extends AppCompatActivity implements BrowserView, LoaderManager.LoaderCallbacks<BrowserPresenter> {

    private String url = ""; // URL that gets entered into the EditText
    private EditText txtDescription; // description on how to use the Browser
    private BrowserPresenter presenter;
    /**
     * Adapter for the Spinner (dropdown menu)
     * It's responsible for displaying data in the Spinner
     */
    private ArrayAdapter<String> spinnerArrayAdapter;
    private Spinner spinner; // dropdown menu
    private static final int LOADER_ID = 106; // unique identification for the BrowserActivity-LoaderManager

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restores instance state
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
        if (id == R.id.change_default_websites) { // if the default website option from the menu gets clicked
            DefaultWebsiteFragment fragment = new DefaultWebsiteFragment();
            fragment.show(getSupportFragmentManager(), "changeDefaultWebsites"); // shows DefaultWebsiteFragment
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Notifies the presenter that the user has entered a URL in the EditText
     *
     * @param view
     */
    public void onClickEnter(View view) {
        Toast toast = Toast.makeText(this, R.string.browser_enter_toast, Toast.LENGTH_SHORT);
        toast.show();
        url = txtDescription.getText().toString();
        presenter.websiteSelectedByURL(url);
    }

    /**
     * Notifies the presenter that the user wants to open a Website
     *
     * @param view
     */
    public void onClickOpen(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.open_website).startAnimation(shake);
        presenter.onOpenClicked();
    }

    /**
     * Notifies the presenter that the user wants to download a Website
     *
     * @param view
     */
    public void onClickDownload(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.download_website).startAnimation(shake);
        presenter.onDownloadClicked();
    }

    /**
     * Opens the Website with the given URL
     *
     * @param url the URL of the requested Website
     */
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

    /**
     * Updates the data in the spinner by creating a new spinner
     * and replacing the old one so that the update will be instantly visible to the user
     */
    public void dataChanged() {

        spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                presenter.getDefaultWebsiteNames());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // restore selected Website in dropdown menu
        SharedPreferences sharedPref = getSharedPreferences("SpinnerPref", MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if(spinnerValue != -1)
            // set the value of the spinner
            spinner.setSelection(spinnerValue);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // saves instance state
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
                // save selected Website in dropdown menu
                int userChoice = spinner.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("SpinnerPref", 0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("userChoiceSpinner", userChoice);
                prefEditor.commit();
                String chosenWebsite = adapterView.getItemAtPosition(i).toString();
                presenter.websiteSelectedByName(chosenWebsite);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // ErrorHandling
            }
        });

        // sets up the spinner after the load has finished
        spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                presenter.getDefaultWebsiteNames());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // restore selected Website in dropdown menu
        SharedPreferences sharedPref = getSharedPreferences("SpinnerPref", MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if(spinnerValue != -1)
            // set the value of the spinner
            spinner.setSelection(spinnerValue);

        txtDescription = (EditText) findViewById(R.id.textView_url);
    }

    @Override
    public void onLoaderReset(Loader<BrowserPresenter> loader) {
        presenter = null;

    }
}
