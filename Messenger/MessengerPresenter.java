package internetofeveryone.ioe.Messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MessagingPresenter;
import internetofeveryone.ioe.Requests.TcpClient;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Messenger page
 */
public class MessengerPresenter extends MessagingPresenter<MessengerView> {

    public static final String TAG = "MessengerPresenter";
    private TcpClient tcpClient;

    /**
     * Instantiates a new MessengerPresenter.
     *
     * @param context the context
     */
    public MessengerPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {

        if (type.equals(DataType.CHAT) || type.equals(DataType.MESSAGE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    /**
     * Sends a request to the view to open a Chat
     *
     * @param chat the chat
     */
    public void onChatClicked(Chat chat) {
        if(isViewAttached()) {
            getView().openChat(chat.getContact());
        } else {
            attachView(new MessengerActivity());
            onChatClicked(chat);
        }
    }

    /**
     * Sends a request to the model to delete a Chat
     *
     * @param chat the chat
     */
    public void onChatDeleteClicked(Chat chat) {
        if(isViewAttached()) {
           getModel().deleteChat(chat.getContact().getUserCode());
        } else {
            attachView(new MessengerActivity());
            onChatDeleteClicked(chat);
        }
    }

    /**
     * Gets the list of all chats from the Model
     *
     * @return the chat list
     */
    public HashMap<String, Chat> getChatList() {
        HashMap<String, Chat> chatList = new HashMap<>();
        List<Chat> chats = getModel().getAllChats();
        for (Chat c : chats) {
            TreeMap<Long, Message> msgList = (getModel().getAllMessagesByContact(c.getContact().getUserCode()));
            c.setMessageList(msgList);
            chatList.put(c.getContact().getName(), c);
        }
        return chatList;
    }

    @Override
    public void fetchMessagesFromServer() {

        Log.e(TAG, "fetches");

        new Connect().execute("");

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (tcpClient != null) {
                    if (!tcpClient.sendMessage(getModel().getUserCode() + "\0MSGPULL\0" + getModel().getUserCode() + "\0" + getModel().getSessionHash()  + "\u0004")) {
                        Log.e(TAG, "fetch didn't work due to connection issues");
                        getView().displayNetworkErrorMessage();
                    }
                } else {
                    Log.e(TAG, "tcpclient is null");
                }
                if (isViewAttached()) {
                    getView().closeLoader();
                }
            }
        };
        handler.postDelayed(r, 2000); // 2 seconds
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

            if (!tcpClient.run()) {
                connectionFailed();
            }
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
                    String myUserCode = getModel().getUserCode();
                    getModel().addMessage(data[i], myUserCode, data[i + 2], (data[i + 1].equals("1")));
                    Log.d(TAG, "successfully added messages");
                }
            } catch (Exception e) {
                Log.e(TAG, "invalid response " + response);
                tcpClient.stopClient();
            }

        }
    }

}
