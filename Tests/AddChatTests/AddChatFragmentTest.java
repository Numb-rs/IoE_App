package internetofeveryone.ioe.AddChatTests;

import android.widget.ArrayAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.AddChat.AddChatFragment;
import internetofeveryone.ioe.BuildConfig;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AddChatFragmentTest {

    AddChatFragment fragment;
    @Mock
    ArrayAdapter<String> adapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        fragment = new AddChatFragment();
    }

    @Test
    public void notNull() throws Exception {
        startFragment( fragment );
        assertNotNull( fragment );
    }

    @Test
    public void setAdapter_adapterChanged() throws Exception {
        ArrayAdapter<String> oldAdapter = fragment.getAdapter();
        fragment.setAdapter(adapter);
        ArrayAdapter<String> newAdapter = fragment.getAdapter();
        assertNotEquals(oldAdapter, newAdapter);
    }

    @Test
    public void dataChanged_adaptersNotifyDataSetChangedCalled() throws Exception {
        fragment.setAdapter(adapter);
        fragment.dataChanged();
        verify(adapter).notifyDataSetChanged();
    }
}
