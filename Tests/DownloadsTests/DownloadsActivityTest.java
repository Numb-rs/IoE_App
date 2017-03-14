package internetofeveryone.ioe.DownloadsTests;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Downloads.DownloadsActivity;
import internetofeveryone.ioe.Downloads.DownloadsAdapter;
import internetofeveryone.ioe.Downloads.DownloadsPresenter;
import us.feras.mdv.MarkdownView;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21) // , manifest = "/src/main/AndroidManifest.xml"
public class DownloadsActivityTest {

    DownloadsActivity activity;
    @Mock
    DownloadsPresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(DownloadsActivity.class).create().start().resume().visible();
        activity = (DownloadsActivity) controller.get();
        activity.onLoadFinished(null, presenter); // to test presenter without the ioe network having to work
        doNothing().when(presenter).deleteClicked(any(String.class));
        doNothing().when(presenter).openClicked(any(String.class));
    }

    @Test
    public void onClickDelete_presentersOpenClickedCalledWithCorrectParameter() throws Exception {
        String name = "testName";
        activity.onClickDelete(name);
        verify(presenter).deleteClicked(name);
    }

    @Test
    public void onClickOpen_presentersDeleteClickedCalledWithCorrectParameter() throws Exception {
        String name = "testName";
        activity.onClickOpen(name);
        verify(presenter).openClicked(name);
    }

    @Test
    public void setAdapter_adapterChanged() throws Exception {
        DownloadsAdapter old = activity.getAdapter();
        activity.setAdapter(new DownloadsAdapter(new ArrayList<String>(), RuntimeEnvironment.application));
        assertNotEquals(activity.getAdapter(), old);
    }

    @Test
    public void setMarkdownView_markdownViewChanged() throws Exception {
        MarkdownView old = activity.getMarkdownView();
        activity.setMarkdownView(new MarkdownView(RuntimeEnvironment.application));
        assertNotEquals(activity.getMarkdownView(), old);
    }

    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(DownloadsActivity.class).create(bundle).start().resume();
        activity = (DownloadsActivity) controller.get();
    }

    @Test
    public void dataChanged_newAdapterCreatedAndOldOneReplaced() throws Exception {
        DownloadsAdapter oldAdapter = activity.getAdapter();
        activity.dataChanged();
        assertNotEquals(oldAdapter, activity.getAdapter());
    }

    @Test
    public void displayContent_MarkdownViewChanged() throws Exception {
        String content = "testContent";
        MarkdownView old = activity.getMarkdownView();
        activity.displayContent(content);
        assertNotEquals(activity.getMarkdownView(), old);
    }
}