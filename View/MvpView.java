package internetofeveryone.ioe.View;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the whole application
 */
public interface MvpView {

    /**
     * Handles the start of a view
     */
    void onStart();

    /**
     * Handles the stopping of an view
     */
    void onStop();

    /**
     * Handles the pausing of an view
     */
    void onPause();

    /**
     * Updates the data
     */
    void dataChanged();

}
