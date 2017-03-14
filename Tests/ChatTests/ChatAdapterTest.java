package internetofeveryone.ioe.ChatTests;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.TreeMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Chat.ChatAdapter;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ChatAdapterTest {

    ChatAdapter chatAdapter;
    Message msg;
    String userCode;

    @Before
    public void setUp() throws Exception {
        userCode = "1-1";
        msg = new Message(3, "1-1", "2-2", "content", false, "2-2");
        TreeMap<Long, Message> msgList = new TreeMap<>();
        msgList.put(3L, msg);
        chatAdapter = new ChatAdapter(msgList, RuntimeEnvironment.application, userCode);
    }

    @Test
    public void getFirstItem_returnsCorrectItem() throws Exception {
        assertEquals(chatAdapter.getItem(0), msg);
    }

    @Test
    public void getCount_returnsCorrectCount() throws Exception {
        assertEquals(chatAdapter.getCount(), 1);
    }

    @Test
    public void getItemId_returns0() throws Exception {
        assertEquals(chatAdapter.getItemId(0), 0);
    }

    @Test
    public void getView_returnsCorrectViewWithCorrectValues() throws Exception {
        ListView parent = new ListView(RuntimeEnvironment.application);
        View view = chatAdapter.getView(0, null, parent);

        TextView tvMessage = (TextView) view.findViewById(R.id.txt_msg);

        assertNotNull("View is null. ", view);
        assertNotNull("tvMessage is null. ", tvMessage);
        assertEquals("tvMessages don't match.", msg.getContent(), tvMessage.getText());
    }
}
