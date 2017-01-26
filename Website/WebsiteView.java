package internetofeveryone.ioe.Website;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Website page
 */
public interface WebsiteView extends MvpView {

    /**
     * Displays the content of the Website to the user
     *
     * @param content of the website
     */
    public void displayWebsite(String content);

    /**
     * Displays an error message to the user
     */
    public void displayErrorMessage();

    /**
     * Notifies the presenter that a link has been clicked
     *
     * @param url URL of the new Website
     */
    public void onClickLink(String url);

}
