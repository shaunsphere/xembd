package com.xec.istory.presentation;
public class ViewItem {
//    private int imageId;
    
    private String displayname;
    private String connectname;
    private String imagelink;
    private String md5;
 
    public ViewItem( String displayname, String connectname, String imagelink, String md5) {
        this.displayname = displayname;
        this.connectname = connectname;
        this.imagelink = imagelink;
        this.md5 = md5;
    }
    public String getdisplayname() {
        return displayname;
    }
    public void setdisplayname(String displayname) {
        this.displayname = displayname;
    }
    public String getconnectname() {
        return connectname;
    }
    public void setconnectname(String connectname) {
        this.connectname = connectname;
    }
    public String getImagelink() {
        return imagelink;
    }
    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
    public String getmd5() {
        return md5;
    }
    public void setmd5(String md5) {
        this.md5 = md5;
    }
//    @Override
//    public String toString() {
//        return displayname + "\n" + connectname;
//    }
}
