package internetofeveryone.ioe.Main;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Main page
 */
interface MainView extends MvpView {

    /**
     * Navigates to the Messenger page
     */
    void goToMessenger();

    /**
     * Navigates to the Downloads page
     */
    void goToDownloads();

    /**
     * Navigates to the Browser page
     */
    void goToBrowser();

    /**
     * displays loading message
     */
    void displayLoader();

    /**
     * closes loading message
     */
    void closeLoader();

    /**
     * displays usercode on screen
     */
    void showUserCode(String userCode);

}
