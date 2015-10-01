package appx_homescreen.appx;

public class ListData {
    private int _id;
    private String _listTitle;
    private String _listDate;
    private String _listAbout;
    private String _listAuthor;

    public ListData(String _listTitle, String _listDate, String _listAbout){
        this._listTitle = _listTitle;
        this._listDate = _listDate;
        this._listAbout = _listAbout;
        this._listAuthor = "N/A";
    }

    public ListData(String _listTitle, String _listDate, String _listAbout, String _listAuthor){
        this._listTitle = _listTitle;
        this._listDate = _listDate;
        this._listAbout = _listAbout;
        this._listAuthor = _listAuthor;
    }

    /** Setters */
    public void set_id(int _id) { this._id = _id; }
    public void set_listTitle(String _listTitle) { this._listTitle = _listTitle; }
    public void set_listDate(String _listDate) {this._listDate = _listDate;}
    public void set_listAbout(String _listAbout) { this._listAbout = _listAbout; }
    public void set_listAuthor(String _listAuthor) { this._listAuthor = _listAuthor; }

    /** Getters */
    public int get_id() {
        return _id;
    }
    public String get_listTitle() { return _listTitle; }
    public String get_listDate() {return _listDate;}
    public String get_listAbout() { return _listAbout; }
    public String get_listAuthor() { return _listAuthor; }
}