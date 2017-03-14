package internetofeveryone.ioe.MainTests;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Main.MainActivity;
import internetofeveryone.ioe.Main.MainPresenter;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainPresenterTest {

    private MainPresenter presenter;
    Context context;
    @Mock
    MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(context);
    }

    @Test
    public void viewAttachedAndOnBrowserClicked_viewGoToBrowserGetsCalled() throws Exception {
        presenter.attachView(mainActivity);
        presenter.onBrowserClicked();
        verify(mainActivity).goToBrowser();
    }

    @Test
    public void viewAttachedAndOnDownloadsClicked_viewGoToDownloadsGetsCalled() throws Exception {
        presenter.attachView(mainActivity);
        presenter.onDownloadsClicked();
        verify(mainActivity).goToDownloads();
    }

    @Test
    public void viewAttachedAndOnMessengerClicked_viewGoToMessengerGetsCalled() throws Exception {
        presenter.attachView(mainActivity);
        presenter.onMessengerClicked();
        verify(mainActivity).goToMessenger();
    }

    /* TODO: can't test that outside of the IoE
    @Test
    public void setUp_test() throws Exception {
        presenter.attachView(mainActivity);
        presenter.setUp();
    }
    */

    @Test
    public void viewNotAttachedOnBrowserClicked_newViewGetsAttached() throws Exception {
        presenter.onBrowserClicked();
        assertNotNull(presenter.getView());
    }

    @Test
    public void viewNotAttachedOnDownloadsClicked_newViewGetsAttached() throws Exception {
        presenter.onDownloadsClicked();
        assertNotNull(presenter.getView());
    }

    @Test
    public void viewNotAttachedOnMessengerClicked_newViewGetsAttached() throws Exception {
        presenter.onMessengerClicked();
        assertNotNull(presenter.getView());
    }
}
