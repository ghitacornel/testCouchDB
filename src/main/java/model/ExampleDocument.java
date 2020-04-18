package model;

public class ExampleDocument {

    // required by DB
    private String _id;

    // required by DB
    private String _rev;

    private String content;
    private boolean isExample;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isExample() {
        return isExample;
    }

    public void setExample(boolean example) {
        isExample = example;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    @Override
    public String toString() {
        return "ExampleDocument{" +
                "_id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", content='" + content + '\'' +
                ", isExample=" + isExample +
                '}';
    }
}
