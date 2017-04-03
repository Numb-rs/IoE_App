package internetofeveryone.ioe.Downloads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom adapter for the ListView in DownloadsActivity
 */
public class DownloadsAdapter extends BaseAdapter {
    private final ArrayList<String> data;
    private final Context context;
    private Animation animAlpha;

    /**
     * Instantiates a new DownloadsAdapter.
     *
     * @param downloads data
     * @param context the context
     */
    public DownloadsAdapter(ArrayList<String> downloads, Context context) {
        this.context = context;
        data = downloads;
        animAlpha = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);
    }

    private static class ViewHolder { // holds the elements that make up one item of the list

        private TextView tvWebsiteName; // name of the Website
        private Button openButton; // button to open the Website
        private Button deleteButton; // button to delete the Website
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String string = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_item_downloads, parent, false);

            viewHolder.tvWebsiteName = (TextView) convertView.findViewById(R.id.downloaded_website_name);
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.button_delete_downloaded_website);
            viewHolder.openButton = (Button) convertView.findViewById(R.id.button_open_downloaded_website);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String newString = string;
        if (newString.length() > 20) {
            newString = newString.substring(0, 17) + "...";
        }
        viewHolder.tvWebsiteName.setText(newString);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(context instanceof DownloadsView){
                    ((DownloadsView)context).onClickDelete(string);
                }
            }
        });viewHolder.openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(context instanceof DownloadsView){
                    ((DownloadsView)context).onClickOpen(getItem(position));
                }
            }
        });

        return convertView;
    }
}