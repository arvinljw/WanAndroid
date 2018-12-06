package net.arvin.wanandroid.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 17:32
 * Function：
 * Desc：
 */
public class NavigationInfoEntity implements Parcelable {
    private List<ArticleEntity> articles;
    private int cid;
    private String name;
    private boolean selected;

    public List<ArticleEntity> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleEntity> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public NavigationInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.articles);
        dest.writeInt(this.cid);
        dest.writeString(this.name);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected NavigationInfoEntity(Parcel in) {
        this.articles = in.createTypedArrayList(ArticleEntity.CREATOR);
        this.cid = in.readInt();
        this.name = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<NavigationInfoEntity> CREATOR = new Creator<NavigationInfoEntity>() {
        @Override
        public NavigationInfoEntity createFromParcel(Parcel source) {
            return new NavigationInfoEntity(source);
        }

        @Override
        public NavigationInfoEntity[] newArray(int size) {
            return new NavigationInfoEntity[size];
        }
    };
}
