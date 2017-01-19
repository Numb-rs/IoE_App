package internetofeveryone.ioe.Model;

import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.ModelObserver;

public class Model {


    private static Model singleton = null;
    private final ArrayList<ModelObserver> observers = new ArrayList<>();
    private final HashMap<String, Chat> chatList = new HashMap<>();
    private final HashMap<Long, Contact> contactList = new HashMap<>();
    private final TreeMap<Integer, Message> messageList = new TreeMap<>();
    private final HashMap<String, Website> downloadedWebsiteList = new HashMap<>();
    private final HashMap<String, Website> defaultWebsiteList = new HashMap<>();
    private int myUserCode;
    Context context;
    DataBase db;
    DataType chatType = DataType.CHAT;
    DataType contactType = DataType.CONTACT;
    DataType websiteType = DataType.WEBSITE;
    DataType messageType = DataType.MESSAGE;

    private Model(Context context) {
        this.context = context;
        db = new DataBase(context);

        storeCodeInDB(readUserCodeFromFile());

        Contact fabianMartin = new Contact("Fabian Martin", 17695799442L, "testKey");
        addContact(fabianMartin);
        Contact jens = new Contact("Jens Ochsenmeier", 9568890411L, "testKey");
        addContact(jens);
        Contact taher = new Contact("Taher Chettaoui", 9568890511L, "testKey");
        addContact(taher);
        Contact daniel = new Contact("Daniel Berwanger", 9568897411L, "testKey");
        addContact(daniel);
        Contact lennart = new Contact("Lennart Niebuhr", 9568990411L, "testKey");
        addContact(lennart);
        Message msg1 = new Message("user1", "user2", "testText Nummer 1 mit vielen Wörtern, sodass es in mehere Zeilen geht. Sollte bei mir stehen", true);
        Message msg2 = new Message("user2", "user1", "testText Nummer 2 mit vielen Wörtern, sodass es in mehere Zeilen geht. Sollte beim anderen stehen", true);
        Message msg3 = new Message("user1", "user2", "testText Nummer 3 mit vielen Wörtern, sodass es in mehere Zeilen geht. Sollte bei mir stehen", true);
        addMessage(msg1);
        addMessage(msg2);
        addMessage(msg3);
        ArrayList<String> msgList = new ArrayList<>();
        msgList.add(msg1.getMessage());
        msgList.add(msg2.getMessage());
        msgList.add(msg3.getMessage());
        Chat fabmart = new Chat(fabianMartin, msgList);
        addChat(fabmart);
        Website reddit = new Website("Reddit", "http://www.reddit.com/", "Content");
        addDefaultWebsite(reddit);
        addDefaultWebsite(new Website("Google", "https://www.google.com/", "Content"));
        addDefaultWebsite(new Website("Wikipedia", "https://en.wikipedia.org/wiki/Main_Page", "Content"));
        addDefaultWebsite(new Website("KIT", "http://www.kit.edu/", "Content"));
        addDefaultWebsite(new Website("IoE", "https://www.internetofeveryone.com/", "Content"));
        addDownloadedWebsite(reddit);
    }

    //TODO: letzte nachricht
    //TODO: liste der nachrichten die zu einem chat gehören

    public void notify(DataType type, String url) {
        for (ModelObserver o : observers) {
            o.update(type, url);
        }
    }

    public void addChat(Chat chat) {
        this.chatList.put(chat.getContact().getName(), chat);
        notify(chatType, chat.getContact().getName());
    }

    public void addContact(Contact contact) {
        this.contactList.put(contact.getUserCode(), contact);
        notify(contactType, "" + contact.getUserCode());
    }

    public void addMessage(Message message) {
        this.messageList.put(message.getId(), message);
        notify(messageType, "" + message.getId());
    }

    public void addDownloadedWebsite(Website website) {
        // downloadedWebsiteList.put(website.getUrl(), website);
        boolean ins = db.insertDownloadedWebsite(website.getName(), website.getUrl(), website.getContent()); // check if true or false, error if false etc.
        if (ins) {
            System.out.println("An item has been added!");
        }
        notify(websiteType, website.getUrl());
    }

    public Chat getChat(String name) {
        return this.chatList.get(name);
    }

    public Website getDefaultWebsite(String url) {
        return this.defaultWebsiteList.get(url);
    }


    public Contact getContact(long userCode) {
        return this.contactList.get(userCode);
    }

    public Message getMessage(int id) {
        return this.messageList.get(id);
    }

    public Website getDownloadedWebsite(String url) {
        return this.downloadedWebsiteList.get(url);
    }

    public HashMap<String, Chat> getChatList() {
        return chatList;
    }

    public HashMap<Long, Contact> getContactList() {
        return contactList;
    }

    public TreeMap<Integer, Message> getMessageList() {
        return messageList;
    }

    public HashMap<String, Website> getDownloadedWebsiteList() {
        Cursor res = db.getDownloadedWebsite();
        if (res.getCount() == 0){
            System.out.println("No data available");
            return null;
        }
        /*
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Name: " + res.getString(0) + "\n");
            buffer.append("URL: " + res.getString(1) + "\n");
            buffer.append("Content: " + res.getString(2) + "\n");
        }
        */
        HashMap<String, Website> list = new HashMap<>();
        while (res.moveToNext()) {
            Website website = new Website(res.getString(0), res.getString(1), res.getString(2));
            list.put(website.getUrl(), website);
        }
        return list;
        // return downloadedWebsiteList;
    }

    public void addDefaultWebsite(Website website) {
        System.out.println("Added to Model: " + website.getUrl());
        defaultWebsiteList.put(website.getUrl(), website);
        // defaultWebsiteNames.add(website.getName());
        notify(websiteType, website.getUrl());
    }

    public void removeDefaultWebsite(String url) {
        defaultWebsiteList.remove(url);
        notify(websiteType, url);
    }

    public void removeContact(long userCode) {
        contactList.remove(userCode);
        notify(contactType, "" + userCode);
    }

    // TODO: public void updateContact() {}

    public void removeDownloadedWebsite(String url) {
        Website website = downloadedWebsiteList.get(url);
        // downloadedWebsiteList.remove(website);
        Integer deletedRows = db.deleteDownloadedWebsite(url);
        if (deletedRows > 0) {
            System.out.println(deletedRows + " Rows have been deleted!");
        }
        notify(websiteType, url);
    }

    public void removeChat(Chat chat) {
        chatList.remove(chat.getContact().getName());
        notify(chatType, chat.getContact().getName());
        notify(contactType, "" + chat.getContact().getUserCode());
    }

    public HashMap<String, Website> getDefaultWebsiteList() {
        return this.defaultWebsiteList;
    }

    public void addObserver(ModelObserver o) {
        this.observers.add(o);
    }

    public static Model getModel(Context context) {
        if (singleton == null) {
            singleton = new Model(context);
        }
        return singleton;
    }

    public int readUserCodeFromFile() {

        File file = new File("usercodeApp.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            // TODO: Error handling
        }
        return Integer.valueOf(text.toString());
    }

    public void storeCodeInDB(int userCode) {
        boolean ins = db.insertUserCode(userCode); // check if true or false, error if false etc.
        if (ins) {
            System.out.println("The usercode has been stored!");
        }
        this.myUserCode = userCode;
    }

    public long getUserCode() {
        Cursor res = db.getUserCode();
        if (res.getCount() == 0){
            System.out.println("No usercode available");
            return -1;
        }
        String resultAsString = "";
        while (res.moveToNext()) {
            resultAsString = res.getString(0);
        }
        long result = Integer.valueOf(resultAsString);
        return result;

    }

}
