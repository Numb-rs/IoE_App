package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

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
     * @param context
     */
    public MessagingPresenter(Context context) {
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
    public boolean isViewAttached() {
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

        if (tcpClient != null) {
            tcpClient.sendMessage("MSGPULL\0" + getModel().getUserCode() + "\0" + getModel().getSessionHash()  + "\u0004");
        }
    }

    public class Connect extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

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

            String[] data = response.split("\0");
            for (int i = 0; i < data.length; i = 3*i) {
                String myUserCode = getModel().getUserCode();
                getModel().addMessage(data[i], myUserCode, data[i+2], Boolean.valueOf(data[i+1]));
            }

            tcpClient.stopClient();
        }
    }
}
