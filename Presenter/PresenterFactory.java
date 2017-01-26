package internetofeveryone.ioe.Presenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents Factory that created Presenters in an 'abstract factory' pattern
 * @param <T> the presenter
 */
public interface PresenterFactory<T extends MvpPresenter> {
    /**
     * Create a new presenter.
     *
     * @return the new presenter
     */
    T create();
}