package internetofeveryone.ioe.Browser;

import internetofeveryone.ioe.View.MvpView;

public interface BrowserView extends MvpView {

    public void goToURL(String url);
    public void dataChanged();

}
