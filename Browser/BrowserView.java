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

    // TODO: javadoc
    public void onClickOpenSearch(View view);
    public void onClickDownloadSearch(View view);
    public void onClickOpenFavorite(String name);
    public void onClickDownloadFavorite(String name);
    public void sendSearchRequest(String engine, String searchTerm);
    public void displayMessage(String text);

}
