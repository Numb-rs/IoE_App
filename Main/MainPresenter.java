package internetofeveryone.ioe.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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
        Log.d(TAG, "setup");
        if ((getModel().getUserCode(getModel().getSql())).equals(DEFAULTUSERCODE)) {
            if (isViewAttached()) {
                Log.d(TAG, "Sends NEWUSER request");

                getView().displayLoader();
                Log.d(TAG, "execute");
                new Connect().execute("");

                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        if (tcpClient != null) {
                            tcpClient.sendMessage(DEFAULTUSERCODE + "\0NEWUSER\u0004");
                        } else {
                            Log.e(TAG, "tcpClient is null! ");
                        }
                        if (isViewAttached()) {
                            getView().closeLoader();
                        }
                    }
                };
                handler.postDelayed(r, 2000); // 2 seconds

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
        protected void onPreExecute() {
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    Log.d(TAG, "message received");
                    publishProgress(message);
                }
            });
        }

        @Override
        protected TcpClient doInBackground(String... message) {
            Log.d(TAG, "started background task");
            // we create a TCPClient object

            if (!tcpClient.run()) {
                Log.e(TAG, "network offline");
                if (isViewAttached()) {
                    getView().displayNetworkErrorMessage();
                }
            }
            return tcpClient;
        }

        @Override
        protected void onPostExecute(TcpClient tcp) {
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
            Log.d(TAG, "onprogressupdate");
            try {
                String response = values[0];
                Log.d(TAG, "response " + response);
                int delimiter = response.indexOf("/");
                String userCode = response.substring(0, delimiter);
                String sessionHash = response.substring(delimiter + 1);
                Log.d(TAG, "userCode = " + userCode);
                Log.d(TAG, "sessionHash = " + sessionHash);

                getModel().insertUserCode(userCode, getModel().getSql());
                if (isViewAttached()) {
                    getView().showUserCode(userCode);
                }
                getModel().insertSessionHash(sessionHash, getModel().getSql());
                if (isViewAttached()) {
                    getView().closeLoader();
                }

            } catch (Exception e) {
                Log.e(TAG, "invalid response");
                tcpClient.stopClient();
            }
        }
    }

}
