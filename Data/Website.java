package internetofeveryone.ioe.Data;

public class Website {

    private String name;
    private String url;
    private String content;

    public Website(String name, String url, String content) {
        this.name = name;
        this.url = url;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
