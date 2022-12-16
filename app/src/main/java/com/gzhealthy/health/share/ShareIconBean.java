package com.gzhealthy.health.share;

//import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;

public class ShareIconBean implements Serializable {
    private int pic;
    private String title;
//    private SHARE_MEDIA type;

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


//    public SHARE_MEDIA getType() {
//        return type;
//    }
//
//    public void setType(SHARE_MEDIA type) {
//        this.type = type;
//    }
//
//    public ShareIconBean(int pic, String title, SHARE_MEDIA type) {
//        this.pic = pic;
//        this.title = title;
//        this.type = type;
//    }
}
