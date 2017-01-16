package internetofeveryone.ioe.Presenter;

public interface PresenterFactory<T extends MvpPresenter> {
    T create();
}