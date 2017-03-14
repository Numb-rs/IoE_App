package internetofeveryone.ioe.BrowserTests;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import internetofeveryone.ioe.Browser.FavoritesAdapter;
import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FavoritesAdapterTest {

    FavoritesAdapter adapter;
    String favorite1;

    @Before
    public void setUp() throws Exception {
        favorite1 = "favorite1";
        ArrayList<String> favorites = new ArrayList();
        favorites.add(favorite1);
        adapter = new FavoritesAdapter(favorites, RuntimeEnvironment.application);
    }

    @Test
    public void getFirstItem_returnsCorrectItem() throws Exception {
        assertEquals(adapter.getItem(0), favorite1);
    }

    @Test
    public void getCount_returnsCorrectCount() throws Exception {
        assertEquals(adapter.getCount(), 1);
    }

    @Test
    public void getItemId_returns0() throws Exception {
        assertEquals(adapter.getItemId(0), 0);
    }

    @Test
    public void getView_returnsCorrectViewWithCorrectValues() throws Exception {
        ListView parent = new ListView(RuntimeEnvironment.application);
        View view = adapter.getView(0, null, parent);

        TextView tvWebsiteName = (TextView) view.findViewById(R.id.favorite_website_name);
        Button downloadButton = (Button) view.findViewById(R.id.button_download_website_favorite);
        Button openButton = (Button) view.findViewById(R.id.button_open_website_favorite);

        assertNotNull("View is null. ", view);
        assertNotNull("tvWebsiteName is null. ", tvWebsiteName);
        assertNotNull("downloadButton is null. ", downloadButton);
        assertNotNull("openButton is null. ", openButton);
        assertEquals("tvWebsiteNames don't match.", favorite1, tvWebsiteName.getText());
    }
}
