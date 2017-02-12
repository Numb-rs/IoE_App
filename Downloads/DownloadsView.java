package internetofeveryone.ioe.Downloads;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Downloads page
 */
public interface DownloadsView extends MvpView {

    /**
     * Notifies the presenter that the user wants to delete a downloaded Website
     *
     * @param name name of the downloaded Website
     */
    public void onClickDelete(String name);

    /**
     * Notifies the presenter that the user wants to open a downloaded Website
     *
     * @param name name of the downloaded Website
     */
    public void onClickOpen(String name);

    /**
     * Opens a Website
     * @param content Content of the Website
     */
    public void displayContent(String content);

}
