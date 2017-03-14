package internetofeveryone.ioe.BrowserTests;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.Browser.BrowserPresenter;
import internetofeveryone.ioe.Browser.FavoritesAdapter;
import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Website.WebsiteActivity;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BrowserActivityTest {

    BrowserActivity activity;
    @Mock
    BrowserPresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(BrowserActivity.class).create().start().resume().visible();
        activity = (BrowserActivity) controller.get();
        activity.onLoadFinished(null, presenter); // to test presenter without the ioe network having to work
    }


    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(BrowserActivity.class).create(bundle).start().resume();
        activity = (BrowserActivity) controller.get();
    }

    @Test
    public void dataChanged_newAdapterCreatedAndOldOneReplaced() throws Exception {
        FavoritesAdapter oldAdapter = activity.getFavoritesAdapter();
        activity.dataChanged();
        assertNotEquals(oldAdapter, activity.getFavoritesAdapter());
    }


    @Test
    public void setAdapter_adapterChanged() throws Exception {
        FavoritesAdapter old = activity.getFavoritesAdapter();
        activity.setFavoritesAdapter(new FavoritesAdapter(new ArrayList<String>(), RuntimeEnvironment.application));
        assertNotEquals(activity.getFavoritesAdapter(), old);
    }

    @Test
    public void goToURL_IntentStarted() throws Exception {
        String url = "url";
        activity.goToURL(url);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        startedIntent.putExtra("URL", url);
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(WebsiteActivity.class.getName()));
    }

    @Test
    public void sendSearchRequest_IntentStarted() throws Exception {
        String url = "url";
        String engine = "engine";
        activity.sendSearchRequest(engine, url);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        startedIntent.putExtra("ENGINE", engine);
        startedIntent.putExtra("URL", url);
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(WebsiteActivity.class.getName()));
    }

    @Test
    public void onOptionsItemSelectedAndAddChatOption_FragmentCreated() throws Exception {
        MenuItem menuItem = new RoboMenuItem(R.id.change_default_websites);
        activity.onOptionsItemSelected(menuItem);
        assertNotNull(activity.getFragment());
    }

    @Test
    public void onClickOpenSearch_presentersOnOpenClickedSearchCalledWithCorrectParameters() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_open_website_search);
        button.performClick();

        TextView textView = (TextView) activity.findViewById(R.id.search_website_name);
        String name = textView.getText().toString();
        EditText editText = (EditText) activity.findViewById(R.id.editText_searchterm);
        String searchTerm = editText.getText().toString();
        verify(presenter).onOpenClickedSearch(name, searchTerm);
    }

    @Test
    public void onClickDownloadSearch_presentersOnDownloadClickedSearchCalledWithCorrectParameters() throws Exception {
        final String testSearchTerm = "testSearchTerm";
        Button button = (Button) activity.findViewById(R.id.button_download_website_search);
        final EditText editText = (EditText) activity.findViewById(R.id.editText_searchterm);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                editText.setText(testSearchTerm);
            }
        });
        button.performClick();
        TextView textView = (TextView) activity.findViewById(R.id.search_website_name);
        String name = textView.getText().toString();
        String searchTerm = editText.getText().toString();
        verify(presenter).onDownloadClickedSearch(name, testSearchTerm);
    }

    @Test
    public void onClickOpenURL_presentersOnOpenClickedURLCalledWithCorrectParameters() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_url_open);
        EditText editText = (EditText) activity.findViewById(R.id.editText_browser_url);
        String url = editText.getText().toString();
        button.performClick();
        verify(presenter).onOpenClickedURL(url);
    }

    @Test
    public void onClickDownloadURL_presentersOnDownloadClickedURLCalledWithCorrectParameters() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_url_download);
        EditText editText = (EditText) activity.findViewById(R.id.editText_browser_url);
        String url = editText.getText().toString();
        button.performClick();
        verify(presenter).onDownloadClickedURL(url);
    }

    @Test
    public void onClickOpenFavorite_presentersOnOpenClickedFavoriteCalledWithCorrectParameters() throws Exception {
        String name = "name";
        activity.onClickOpenFavorite(name);
        verify(presenter).onOpenClickedFavorite(name);
    }

    @Test
    public void onClickDownloadFavorite_presentersOnDownloadClickedFavoriteCalledWithCorrectParameters() throws Exception {
        String name = "name";
        activity.onClickDownloadFavorite(name);
        verify(presenter).onDownloadClickedFavorite(name);
    }

}

