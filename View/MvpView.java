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
    public void onStart();

    /**
     * Handles the stopping of an view
     */
    public void onStop();

    /**
     * Handles the pausing of an view
     */
    public void onPause();

    /**
     * Updates the data
     */
    public void dataChanged();

}
