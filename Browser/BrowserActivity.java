package internetofeveryone.ioe.Browser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import icepick.Icepick;
import internetofeveryone.ioe.DefaultWebsites.DefaultWebsiteFragment;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Website.WebsiteActivity;
// TODO: abfangen, wenn der user eine invalide url eingibt (zB durch prüfen des statuscodes der serverresponse)
/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Browser page
 */
public class BrowserActivity extends AppCompatActivity implements BrowserView, LoaderManager.LoaderCallbacks<BrowserPresenter> {

    private BrowserPresenter presenter;
    private ListView favoritesList;
    private FavoritesAdapter favoritesAdapter;
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
     * Notifies the presenter that the user wants to open a favorite Website
     *
     * @param
     */
    public void onClickOpenSearch(View view) {
        TextView textView = (TextView) findViewById(R.id.search_website_name);
        String name = textView.getText().toString();
        EditText editText = (EditText) findViewById(R.id.editText_searchterm);
        String searchTerm = editText.getText().toString();
        presenter.onOpenClickedSearch(name, searchTerm);
    }

    /**
     * Notifies the presenter that the user wants to download a favorite Website
     *
     * @param
     */
    public void onClickDownloadSearch(View view) {
        TextView textView = (TextView) findViewById(R.id.search_website_name);
        String name = textView.getText().toString();
        EditText editText = (EditText) findViewById(R.id.editText_searchterm);
        String searchTerm = editText.getText().toString();
        if (searchTerm.equals("")) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show(); // TODO: text auslagern in strings.xmk
            return;
        }
        presenter.onDownloadClickedSearch(name, searchTerm);
    }

    /**
     * Notifies the presenter that the user wants to open a favorite Website
     *
     * @param
     */
    public void onClickOpenFavorite(String name) {
        presenter.onOpenClickedFavorite(name);
    }

    /**
     * Notifies the presenter that the user wants to download a favorite Website
     *
     * @param
     */
    public void onClickDownloadFavorite(String name) {
        presenter.onDownloadClickedFavorite(name);
    }

    /**
     * Notifies the presenter that the user wants to open a Website by URL
     *
     * @param
     */
    public void onClickOpenURL(View view) {
        EditText editText = (EditText) findViewById(R.id.editText_browser_url);
        String url = editText.getText().toString();
        editText.setText("");
        presenter.onOpenClickedURL(url);
    }

    /**
     * Notifies the presenter that the user wants to download a Website by URL
     *
     * @param
     */
    public void onClickDownloadURL(View view) {
        EditText editText = (EditText) findViewById(R.id.editText_browser_url);
        String url = editText.getText().toString();
        editText.setText("");
        presenter.onDownloadClickedURL(url);
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

    // TODO: javadoc
    public void sendSearchRequest(String engine, String searchTerm) {

        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra("ENGINE", engine);
        intent.putExtra("SEARCHTERM", searchTerm); // TODO: als konstante auslagern (bei methode obdendrüber auch)
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
        /*
        searchList = (ListView)findViewById(R.id.browser_list_search);
        searchAdapter = new SearchAdapter(searchEngines, this);
        searchList.setAdapter(searchAdapter);
        */
        favoritesList = (ListView)findViewById(R.id.browser_list_favorites);
        favoritesAdapter = new FavoritesAdapter(presenter.getDefaultWebsiteNames(), this);
        favoritesList.setAdapter(favoritesAdapter);

        setListViewHeightBasedOnChildren(favoritesList);

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
        favoritesList = (ListView)findViewById(R.id.browser_list_favorites);
        favoritesAdapter = new FavoritesAdapter(presenter.getDefaultWebsiteNames(), this);
        favoritesList.setAdapter(favoritesAdapter);

        setListViewHeightBasedOnChildren(favoritesList);
    }

    @Override
    public void onLoaderReset(Loader<BrowserPresenter> loader) {
        presenter = null;

    }

    /*
    * Displays a message to the user
    */
    public void displayMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /* Sets the Height of the ListView dynamically.
    * Fixes issue of not showing all the items of the ListView when placed inside a ScrollView
    */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
