package internetofeveryone.ioe.Website;

import internetofeveryone.ioe.View.MvpView;

public interface WebsiteView extends MvpView {

    public void displayWebsite(String url);
    public void displayErrorMessage();
    public void dataChanged();
    public void onClickLink(String url);

}
