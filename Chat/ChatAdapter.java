package internetofeveryone.ioe.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.R;


public class ChatAdapter extends BaseAdapter {

    private final ArrayList<Message> data;
    private Context context;
    private long contactUserCode;

    /**
     * Instantiates a new ChatAdapter.
     *
     * @param chatList data
     * @param context
     */
    public ChatAdapter(TreeMap<Long, Message> chatList, Context context, long contactUserCode) {
        this.context = context;
        this.contactUserCode = contactUserCode;
        data = new ArrayList<>();
        data.addAll(chatList.values());
    }

    private static class ViewHolder { // holds the elements that make up one item of the list
        private TextView tVMessage; // content of the message
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        ChatAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ChatAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            // If receiver is the other person (the message has been sent by me) align to right
            if ((data.get(position)).getReceiverID() == contactUserCode) {
                convertView = inflater.inflate(R.layout.item_chat_right, parent, false);
            }
            // If not mine then align to left
            else {
                convertView = inflater.inflate(R.layout.item_chat_left, parent, false);
            }

            viewHolder.tVMessage = (TextView) convertView.findViewById(R.id.txt_msg);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChatAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tVMessage.setText(data.get(position).getContent());

        return convertView;
    }

}