package net.arvin.wanandroid.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 17:30
 * Function：
 * Desc：
 */
public class LoginResultEntity implements Parcelable {
    private String username;
    private String password;
    private String icon;
    private int type;
    private List<Integer> collectIds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.icon);
        dest.writeInt(this.type);
        dest.writeList(this.collectIds);
    }

    public LoginResultEntity() {
    }

    protected LoginResultEntity(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.icon = in.readString();
        this.type = in.readInt();
        this.collectIds = new ArrayList<Integer>();
        in.readList(this.collectIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<LoginResultEntity> CREATOR = new Parcelable.Creator<LoginResultEntity>() {
        @Override
        public LoginResultEntity createFromParcel(Parcel source) {
            return new LoginResultEntity(source);
        }

        @Override
        public LoginResultEntity[] newArray(int size) {
            return new LoginResultEntity[size];
        }
    };
}
