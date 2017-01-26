package internetofeveryone.ioe.Messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom adapter for the ListView in MessengerActivity
 */
public class MessengerAdapter extends BaseAdapter {
    private final ArrayList<Chat> data;
    private Context context;

    /**
     * Instantiates a new MessengerAdapter.
     *
     * @param chatList data
     * @param context
     */
    public MessengerAdapter(Map<String, Chat> chatList, Context context) {
        this.context = context;
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
        viewHolder.tVLastMessage.setText(chat.getMessageList().get(chat.getMessageList().size()-1));

        return convertView;
    }
}