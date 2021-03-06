package internetofeveryone.ioe.Messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom adapter for the ListView in MessengerActivity
 */
public class MessengerAdapter extends BaseAdapter {
    private final ArrayList<Chat> data;

    /**
     * Instantiates a new MessengerAdapter.
     *
     * @param chatList data
     */
    public MessengerAdapter(Map<String, Chat> chatList) {
        data = new ArrayList<>();
        data.addAll(chatList.values());
    }

    private static class ViewHolder { // holds the elements that make up one item of the list
        private TextView tVContact; // name of the contact
        private TextView tVLastMessage; // preview of the most recent message
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Chat getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chat = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_item_messenger, parent, false);

            viewHolder.tVContact = (TextView) convertView.findViewById(R.id.contact);
            viewHolder.tVLastMessage = (TextView) convertView.findViewById(R.id.lastMsg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tVContact.setText(chat.getContact().getName());
        Message lastMsg = chat.getLastMessage();
        if (lastMsg != null) {
            String content = lastMsg.getContent();
            if (lastMsg.isEncrypted()) {
                content = Message.decrypt(lastMsg.getContent(), chat.getContact().getKey());
            }
            String lastMessagePreview = lastMsg == null ? "" : content;
            lastMessagePreview = lastMessagePreview.replace("\n", " ");
            if (lastMessagePreview.length() > 47) {
                lastMessagePreview = lastMessagePreview.substring(0, 47) + "...";
            }
            viewHolder.tVLastMessage.setText(lastMessagePreview);
        } else {
            viewHolder.tVLastMessage.setText("");
        }
        return convertView;
    }
}