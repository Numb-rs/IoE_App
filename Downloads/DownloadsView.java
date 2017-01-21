package internetofeveryone.ioe.Downloads;

import internetofeveryone.ioe.View.MvpView;

public interface DownloadsView extends MvpView {

    public void onClickDelete(String name);
    public void onClickOpen(String name);
    public void openWebsite(String url);
    public void dataChanged();

}
