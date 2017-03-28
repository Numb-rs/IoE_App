package internetofeveryone.ioe.Website;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Website page
 */
interface WebsiteView extends MvpView {

    /**
     * Displays the content of the Website to the user
     *
     * @param content of the website
     */
    void displayWebsite(String content);

    /**
     * Displays a message to the user
     */
    void displayMessage(String text);

    void displayNetworkErrorMessage();

    void displayLoader();

    void closeLoader();

}
