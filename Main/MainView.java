package internetofeveryone.ioe.Main;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Main page
 */
public interface MainView extends MvpView {

    /**
     * Navigates to the Messenger page
     */
    public void goToMessenger();

    /**
     * Navigates to the Downloads page
     */
    public void goToDownloads();

    /**
     * Navigates to the Browser page
     */
    public void goToBrowser();

}
