package net.arvin.wanandroid.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arvinljw on 2018/11/16 16:55
 * Function：
 * Desc：
 */
public class TagEntity implements Parcelable {
    /**
     * name : 公众号
     * url : /wxarticle/list/409/1
     */

    private String name;
    private String url;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    public TagEntity() {
    }

    protected TagEntity(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<TagEntity> CREATOR = new Parcelable.Creator<TagEntity>() {
        @Override
        public TagEntity createFromParcel(Parcel source) {
            return new TagEntity(source);
        }

        @Override
        public TagEntity[] newArray(int size) {
            return new TagEntity[size];
        }
    };
}
