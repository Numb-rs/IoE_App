package internetofeveryone.ioe.MainTests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Downloads.DownloadsActivity;
import internetofeveryone.ioe.Main.MainActivity;
import internetofeveryone.ioe.Main.MainPresenter;
import internetofeveryone.ioe.Messenger.MessengerActivity;
import internetofeveryone.ioe.R;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21) // , manifest = "/src/main/AndroidManifest.xml" TODO: remove
public class MainActivityTest {

    MainActivity activity;
    @Mock
    MainPresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doNothing().when(presenter).onMessengerClicked();
        doNothing().when(presenter).onDownloadsClicked();
        doNothing().when(presenter).onBrowserClicked();
        doNothing().when(presenter).setUp();
        controller = Robolectric.buildActivity(MainActivity.class).create();
        activity = (MainActivity) controller.get();
        activity.onLoadFinished(null, presenter); // used to test presenter without the IoE network having to work
        activity = (MainActivity) controller.start().resume().visible().get();
        activity.onLoadFinished(null, presenter);
    }

    @Test
    public void downloadsButtonClicked_presentersOnMessengeClickedCalled() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_messages);
        button.performClick();
        verify(presenter).onMessengerClicked();
    }

    @Test
    public void downloadsButtonClicked_presentersOnDownloadsClickedCalled() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_downloads);
        button.performClick();
        verify(presenter).onDownloadsClicked();
    }

    @Test
    public void browserButtonClicked_presentersOnBrowserClickedCalled() throws Exception {
        Button button = (Button) activity.findViewById(R.id.button_browser);
        button.performClick();
        verify(presenter).onBrowserClicked();
    }

    @Test
    public void goToDownloads_IntentStarted() throws Exception {
        activity.goToDownloads();
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(DownloadsActivity.class.getName()));
    }

    @Test
    public void goToMessenger_IntentStarted() throws Exception {
        activity.goToMessenger();
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(MessengerActivity.class.getName()));
    }

    @Test
    public void goToBrowser_IntentStarted() throws Exception {
        activity.goToBrowser();
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(BrowserActivity.class.getName()));
    }

    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(MainActivity.class).create(bundle).start().resume();
        activity = (MainActivity) controller.get();
    }

    @Test
    public void displayLoaderWorks() throws Exception {
        ProgressDialog dialog = activity.getProgressDialog();
        ProgressDialog spy = Mockito.spy(dialog);
        activity.setProgressDialog(spy);
        activity.displayLoader();
        verify(spy).show();
    }

    @Test
    public void closeLoaderWorks() throws Exception {
        ProgressDialog dialog = activity.getProgressDialog();
        ProgressDialog spy = Mockito.spy(dialog);
        activity.setProgressDialog(spy);
        activity.closeLoader();
        verify(spy).dismiss();
    }
}