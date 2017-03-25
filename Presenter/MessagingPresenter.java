package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import internetofeveryone.ioe.Model.MessageModel;
import internetofeveryone.ioe.Requests.TcpClient;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This abstract class represents the presenter for the part of the application that handles chatting and messaging
 */
public abstract class MessagingPresenter<V extends MvpView> extends MvpPresenter implements ModelObserver {

    private MessageModel model;
    private WeakReference<V> view;
    private TcpClient tcpClient;
    private static final String TAG = "MessagingPresenter";

    /**
     * Instantiates a new MessagePresenter.
     *
     * @param context the context
     */
    protected MessagingPresenter(Context context) {
        super();
        model = new MessageModel(context);
        registerObserver(model);
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public V getView() { // should always call isViewAttached before calling getView
        return this.view == null ? null : this.view.get();
    }

    /**
     * Is view attached boolean.
     *
     * @return the boolean
     */
    protected boolean isViewAttached() {
        return this.view != null && this.view.get() != null;
    }

    /**
     * Attach view.
     *
     * @param view the view
     */
    public void attachView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    /**
     * Detach view.
     */
    public void detachView() {
        if (this.view != null) {
            this.view.clear();
            this.view = null;
        }
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public MessageModel getModel() {
        return model;
    }

    public void setModel(MessageModel model) {
        this.model = model;
    }

    /**
     * starts request to fetch new messages from server
     */
    public void fetchMessagesFromServer() {

        new Connect().execute("");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tcpClient != null) {
            tcpClient.sendMessage(getModel().getUserCode() + "\0MSGPULL\0" + getModel().getUserCode() + "\0" + getModel().getSessionHash()  + "\u0004");
        } else {
            Log.e(TAG, "tcpclient is null");
        }
    }

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
            try {
                String[] data = response.split("\0");
                Log.d(TAG, ""  + data.length);
                if ((data.length % 3) != 0) {
                    throw new Exception();
                }
                for (int i = 0; i < data.length; i += 3) {
                    Log.d(TAG, "start adding messages");
                    String myUserCode = getModel().getUserCode();
                    getModel().addMessage(data[i], myUserCode, data[i + 2], Boolean.valueOf(data[i + 1]));
                    Log.d(TAG, "successfully added messages");
                }
            } catch (Exception e) {
                Log.e(TAG, "invalid response");
                tcpClient.stopClient();
            }

        }
    }
}
