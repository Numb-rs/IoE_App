package internetofeveryone.ioe.DefaultWebsites;

import android.app.Dialog;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the DefaultWebsite fragment
 */
public interface DefaultWebsiteView extends MvpView {

    /**
     * Opens a new AlertDialog that allows users to add a new DefaultWebsite
     *
     * @return AlertDialog
     */
    public Dialog onAddDefaultWebsite();

    /**
     * Opens a new AlertDialog that allows users to edit a DefaultWebsite
     *
     * @param pos position of the selected DefaultWebsite
     * @return AlertDialog
     */
    public Dialog onEditDefaultWebsite(int pos);

}
