package internetofeveryone.ioe.DefaultWebsites;

import android.app.Dialog;

import internetofeveryone.ioe.View.MvpView;

public interface DefaultWebsiteView extends MvpView {

    public Dialog onAddDefaultWebsite();
    public Dialog onEditDefaultWebsite(int pos);
    public void dataChanged();

}
