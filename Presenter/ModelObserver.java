package internetofeveryone.ioe.Presenter;

import internetofeveryone.ioe.Data.DataType;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the Observer for the Observer-Pattern between Model and MvpPresenter
 */
public interface ModelObserver {

    /**
     * Update the observer.
     *
     * @param type the data type that has changed
     */
    public void update(DataType type);
}
