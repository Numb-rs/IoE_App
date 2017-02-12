package internetofeveryone.ioe.Browser;

import android.view.View;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Browser page
 */
public interface BrowserView extends MvpView {

    /**
     * Opens the Website with the given URL
     *
     * @param url the URL of the requested Website
     */
    public void goToURL(String url);

    /**
     * Notifies the presenter that the user wants to start a search request
     * @param view
     */
    public void onClickOpenSearch(View view);

    /**
     * Notifies the presenter that the user wants to download a search request
     * @param view
     */
    public void onClickDownloadSearch(View view);

    /**
     * Notifies the presenter that the user wants to open a favorite website
     * @param name
     */
    public void onClickOpenFavorite(String name);

    /**
     * Notifies the presenter that the user wants to download a favorite website
     * @param name
     */
    public void onClickDownloadFavorite(String name);

    /**
     * sends a search request
      * @param engine
     * @param searchTerm
     */
    public void sendSearchRequest(String engine, String searchTerm);

    /**
     * displays a mesasge to the user
     * @param text
     */
    public void displayMessage(String text);

}
