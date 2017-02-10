package internetofeveryone.ioe.Main;

import android.content.Context;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Main page
 */
public class MainPresenter extends BrowsingPresenter<MainView> {

    /**
     * Instantiates a new Main presenter.
     *
     * @param context
     */
    public MainPresenter(Context context) {
        super(context);
    }

    /**
     * Sends a request to the view to navigate to the Messenger page
     */
    public void onMessengerClicked() {

        if (isViewAttached()) {
            getView().goToMessenger();
        } else {
            // ErrorHandling
        }
    }

    /**
     * Sends a request to the view to navigate to the Downloads page
     */
    public void onDownloadsClicked() {

        if (isViewAttached()) {
            getView().goToDownloads();
        } else {
            // ErrorHandling
        }
    }

    /**
     * Sends a request to the view to navigate to the Browser page
     */
    public void onBrowserClicked() {

        if (isViewAttached()) {
            getView().goToBrowser();
        } else {
            // ErrorHandling
        }
    }

    @Override
    public void update(DataType type) { } // doesn't have to update anything

}
