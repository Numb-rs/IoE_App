package internetofeveryone.ioe.Chat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MessagingPresenter;
import internetofeveryone.ioe.Requests.TcpClient;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Contact fragment
 */
public class ChatPresenter extends MessagingPresenter<ChatView> {

    private Contact contact;
    private Chat chat;
    private TcpClient tcpClientSend;
    private static final String TAGSend = "ChatPresenter";

    /**
     * Instantiates a new ChatPresenter.
     *
     * @param context the context
     */
    public ChatPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {
        if (type.equals(DataType.MESSAGE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    /**
     * Gets the contact for a given user code from the Model .
     *
     * @param userCode the user code
     */
    public Contact getContact(String userCode) {
        return getModel().getContactByID(userCode);
    }

    /**
     * Changes the encryption state
     *
     * @param isChecked true if encryption is currently activated, false if not
     */
    public void encryptChanged(String userCode, boolean isChecked) {
        getModel().updateChat(userCode, isChecked);
    }

    /**
     * Gets encryption state from the chat
     *
     * @return true if encryption is currently activated, false if not
     */
    public boolean isChatEncrypted(String userCode) {
        return getModel().getChatByID(userCode).isEncrypted();
    }

    /**
     * Adds a new Message
     *
     * @param msgPassed the message
     */
    public void sendMessage(String userCode, String msgPassed) {
        String content = msgPassed;
        boolean encrypt = getModel().getChatByID(userCode).isEncrypted();
        if (encrypt) {
            try {
                content = Message.encrypt(msgPassed, getModel().getContactByID(userCode).getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getModel().addMessage(getModel().getUserCode(), getModel().getContactByID(userCode).getUserCode(), content, encrypt);

        new ConnectSend().execute("");

        if (tcpClientSend != null) {
            tcpClientSend.sendMessage("MSGSEND\0" + getModel().getUserCode() + "\0" + userCode  +  "\0" + msgPassed  + "\0" + getModel().getSessionHash()  + "\u0004");
        }
    }

    /**
     * Gets all messages from the Model
     *
     * @return TreeMap of all messages
     */
    public TreeMap<Long, Message> getMessageList(String userCode) {
        String key = getModel().getContactByID(userCode).getKey();
        TreeMap<Long, Message> messages = getModel().getChatByID(userCode).getMessageList();
        if (!key.equals("")) {
            for (Message m : messages.values()) {
                if (m.isEncrypted()) {
                    try {
                        m.setContent(Message.decrypt(m.getContent(), key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return messages;
    }

    /**
     * Gets name of contact from the Model
     *
     * @return name
     */
    public String getContactName(String userCode) {
        return getModel().getContactByID(userCode).getName();
    }

    private class ConnectSend extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            // we create a TCPClient object
            tcpClientSend = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });

            tcpClientSend.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);

            String response = values[0];
            Log.d(TAGSend, "response " + response);
            // response?
            tcpClientSend.stopClient();
        }
    }

}
