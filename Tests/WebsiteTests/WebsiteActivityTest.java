package internetofeveryone.ioe.WebsiteTests;


import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Website.WebsiteActivity;
import internetofeveryone.ioe.Website.WebsitePresenter;
import us.feras.mdv.MarkdownView;

import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class WebsiteActivityTest {

    WebsiteActivity activity;
    @Mock
    WebsitePresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(WebsiteActivity.class).create().start().resume().visible();
        activity = (WebsiteActivity) controller.get();
        activity.onLoadFinished(null, presenter); // used to test presenter without the IoE network having to work
    }

    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(WebsiteActivity.class).create(bundle).start().resume().visible();
        activity = (WebsiteActivity) controller.get();
    }

    @Test
    public void displayWebsite_MarkdownViewChanged() throws Exception {
        String content = "testContent";
        MarkdownView old = activity.getMarkdownView();
        activity.displayWebsite(content);
        assertNotEquals(activity.getMarkdownView(), old);
    }

}
