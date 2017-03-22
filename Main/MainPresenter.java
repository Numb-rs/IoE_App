package internetofeveryone.ioe.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MessagingPresenter;
import internetofeveryone.ioe.Requests.TcpClient;

import static internetofeveryone.ioe.Model.Model.DEFAULTUSERCODE;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Main page
 */
public class MainPresenter extends MessagingPresenter<MainView> {

    private TcpClient tcpClient;
    private static final String TAG = "MainPresenter";

    /**
     * Instantiates a new Main presenter.
     *
     * @param context the context
     */
    public MainPresenter(Context context) {
        super(context);
    }

    /**
     * sets up the usercode
     */
    public void setUp() {
        if ((getModel().getUserCode(getModel().getSql())).equals(DEFAULTUSERCODE)) {
            if (isViewAttached()) {
                Log.d(TAG, "Sends NEWUSER request");
                /* getView().displayLoader();
                                              TODO: BOTH DISABLED FOR TEST PURPOSES
                */
                new Connect().execute("");


                if (tcpClient != null) {
                    tcpClient.sendMessage("NEWUSER\u0004");
                }
            } else {
                attachView(new MainActivity());
                setUp();
            }
        }
    }

    /**
     * Sends a request to the view to navigate to the Messenger page
     */
    public void onMessengerClicked() {

        if (isViewAttached()) {
            getView().goToMessenger();
        } else {
            attachView(new MainActivity());
        }
    }

    /**
     * Sends a request to the view to navigate to the Downloads page
     */
    public void onDownloadsClicked() {

        if (isViewAttached()) {
            getView().goToDownloads();
        } else {
            attachView(new MainActivity());
        }
    }

    /**
     * Sends a request to the view to navigate to the Browser page
     */
    public void onBrowserClicked() {

        if (isViewAttached()) {
            getView().goToBrowser();
        } else {
            attachView(new MainActivity());
        }
    }

    @Override
    public void update(DataType type) { } // doesn't need to update anything

    private class Connect extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            Log.d(TAG, "started background task");
            // we create a TCPClient object
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });

            tcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);

            String response = values[0];
            Log.d(TAG, "response " + response);
            int delimiter = response.indexOf("/");
            String userCode = response.substring(delimiter);
            String sessionHash = response.substring(delimiter+1);

            getModel().insertUserCode(userCode, getModel().getSql());
            getModel().insertSessionHash(sessionHash, getModel().getSql());
            getView().closeLoader();
            tcpClient.stopClient();
        }
    }

}
