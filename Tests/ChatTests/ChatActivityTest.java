package internetofeveryone.ioe.ChatTests;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

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

import java.util.TreeMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Chat.ChatActivity;
import internetofeveryone.ioe.Chat.ChatAdapter;
import internetofeveryone.ioe.Chat.ChatPresenter;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.R;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ChatActivityTest {

    ChatActivity activity;
    @Mock
    ChatPresenter presenter;

    ActivityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(presenter.isChatEncrypted(any(String.class))).thenReturn(false);
        when(presenter.getContact(any(String.class))).thenReturn(new Contact("name", "7-7", "key", true));
        controller = Robolectric.buildActivity(ChatActivity.class).create();
        activity = (ChatActivity) controller.get();
        activity.onLoadFinished(null, presenter); // used to test presenter without the IoE network having to work
    }


    @Test
    public void icepickInstanceStateSuccessfullyRestored() throws Exception {
        Bundle bundle = new Bundle();
        activity.onSaveInstanceState(bundle);
        controller.pause().stop().destroy();
        controller = Robolectric.buildActivity(ChatActivity.class).create(bundle);
        activity = (ChatActivity) controller.get();
    }

    @Test
    public void dataChanged_newAdapterCreatedAndOldOneReplaced() throws Exception {
        ChatAdapter oldAdapter = activity.getAdapter();
        activity.dataChanged();
        assertNotEquals(oldAdapter, activity.getAdapter());
    }


    @Test
    public void setAdapter_adapterChanged() throws Exception {
        ChatAdapter old = activity.getAdapter();
        activity.setAdapter(new ChatAdapter(new TreeMap<Long, Message>(), RuntimeEnvironment.application, "2-2"));
        assertNotEquals(activity.getAdapter(), old);
    }

    @Test
    public void encryptionButtonClicked_presentersEncryptChangedCalledWithCorrectParameters() throws Exception {
        activity.setUserCode("8-8");
        ToggleButton encryption = (ToggleButton) activity.findViewById(R.id.button_encryption);
        boolean old = encryption.isChecked();
        encryption.performClick();
        verify(presenter).encryptChanged(activity.getUserCode(), !old);
        assertNotEquals(old, encryption.isChecked());
    }

    @Test
    public void sendButtonClicked_presentersSendMessageCalledWithCorrectParameters() throws Exception {
        final String testMessage = "testMessage";
        Button send = (Button) activity.findViewById(R.id.button_chat_send);
        final EditText message = (EditText) activity.findViewById(R.id.message_to_send);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                message.setText(testMessage);
            }
        });
        send.performClick();
        verify(presenter).sendMessage(activity.getUserCode(), testMessage);
    }
}
