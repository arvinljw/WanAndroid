package net.arvin.wanandroid.entities;

import android.os.Parcel;
import android.os.Parcelable;

import net.arvin.wanandroid.uis.uihelpers.IPageTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 17:07
 * Function：
 * Desc：
 */
public class TreeEntity implements Parcelable, IPageTitle {

    /**
     * courseId : 13
     * id : 150
     * name : 开发环境
     * order : 1
     * parentChapterId : 0
     * visible : 1
     */
    private List<TreeEntity> children;
    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private int visible;

    public List<TreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity> children) {
        this.children = children;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.children);
        dest.writeInt(this.courseId);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.order);
        dest.writeInt(this.parentChapterId);
        dest.writeInt(this.visible);
    }

    public TreeEntity() {
    }

    protected TreeEntity(Parcel in) {
        this.children = new ArrayList<TreeEntity>();
        in.readList(this.children, TreeEntity.class.getClassLoader());
        this.courseId = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        this.order = in.readInt();
        this.parentChapterId = in.readInt();
        this.visible = in.readInt();
    }

    public static final Parcelable.Creator<TreeEntity> CREATOR = new Parcelable.Creator<TreeEntity>() {
        @Override
        public TreeEntity createFromParcel(Parcel source) {
            return new TreeEntity(source);
        }

        @Override
        public TreeEntity[] newArray(int size) {
            return new TreeEntity[size];
        }
    };

    @Override
    public String getTitle() {
        return getName();
    }
}
