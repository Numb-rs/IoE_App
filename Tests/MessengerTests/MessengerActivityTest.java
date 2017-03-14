package internetofeveryone.ioe.MessengerTests;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

import java.util.HashMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Chat.ChatActivity;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Messenger.MessengerActivity;
import internetofeveryone.ioe.Messenger.MessengerAdapter;
import internetofeveryone.ioe.Messenger.MessengerPresenter;
import internetofeveryone.ioe.R;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MessengerActivityTest {

    MessengerActivity activity;
    @Mock
    MessengerPresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(MessengerActivity.class).create().start().resume().visible();
        activity = (MessengerActivity) controller.get();
        activity.onLoadFinished(null, presenter); // to test presenter without the ioe network having to work
    }


    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(MessengerActivity.class).create(bundle).start().resume();
        activity = (MessengerActivity) controller.get();
    }

    @Test
    public void dataChanged_newAdapterCreatedAndOldOneReplaced() throws Exception {
        MessengerAdapter oldAdapter = activity.getAdapter();
        activity.dataChanged();
        assertNotEquals(oldAdapter, activity.getAdapter());
    }


    @Test
    public void setAdapter_adapterChanged() throws Exception {
        MessengerAdapter old = activity.getAdapter();
        activity.setAdapter(new MessengerAdapter(new HashMap<String, Chat>(), RuntimeEnvironment.application));
        assertNotEquals(activity.getAdapter(), old);
    }

    @Test
    public void openChat_IntentStarted() throws Exception {
        Contact contact = new Contact("name", "1-1", "key", false);
        activity.openChat(contact);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(ChatActivity.class.getName()));
    }

    @Test
    public void onOptionsItemSelectedAndAddChatOption_FragmentCreated() throws Exception {
        MenuItem menuItem = new RoboMenuItem(R.id.add_chat_option);
        activity.onOptionsItemSelected(menuItem);
        assertNotNull(activity.getAddChatFragment());
    }

    @Test
    public void onOptionsItemSelectedAndAddContactOption_FragmentCreated() throws Exception {
        MenuItem menuItem = new RoboMenuItem(R.id.add_contact_option);
        activity.onOptionsItemSelected(menuItem);
        assertNotNull(activity.getContactFragment());
    }
}
