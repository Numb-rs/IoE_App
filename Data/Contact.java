package internetofeveryone.ioe.Data;

public class Contact {

    private String name;
    private long userCode;
    private String key;
    private boolean openChat;

    public Contact(String name, long userCode, String key) {
        this.name = name;
        this.userCode = userCode;
        this.key = key;
        this.openChat = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserCode() {
        return userCode;
    }

    public void serUserCode(long userCode) {
        this.userCode = userCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean hasOpenChat() {
        return openChat;
    }

    public void setOpenChat(boolean openChat) {
        this.openChat = openChat;
    }
}
