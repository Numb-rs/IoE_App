package internetofeveryone.ioe.Browser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom adapter for the Favorites-ListView in BrowserActivity
 */
public class FavoritesAdapter extends BaseAdapter {
    private final ArrayList<String> data;
    private Context context;

    /**
     * Instantiates a new DownloadsAdapter.
     *
     * @param favorites data
     * @param context
     */
    public FavoritesAdapter(ArrayList<String> favorites, Context context) {
        this.context = context;
        data = favorites;
    }

    private static class ViewHolder { // holds the elements that make up one item of the list

        private TextView tvWebsiteName; // name of the Website
        private Button openButton; // button to open the Website
        private Button downloadButton; // button to download the Website
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
        final String name = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_item_browser_favorites, parent, false);

            viewHolder.tvWebsiteName = (TextView) convertView.findViewById(R.id.favorite_website_name);
            viewHolder.downloadButton = (Button) convertView.findViewById(R.id.button_download_website_favorite);
            viewHolder.openButton = (Button) convertView.findViewById(R.id.button_open_website_favorite);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvWebsiteName.setText(name);
        viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof BrowserView){
                    ((BrowserView)context).onClickDownloadFavorite(name);
                } else {
                    // ErrorHandling
                }
            }
        });viewHolder.openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof BrowserView){
                    ((BrowserView)context).onClickOpenFavorite(name);
                } else {
                    // ErrorHandling
                }
            }
        });

        return convertView;
    }
}