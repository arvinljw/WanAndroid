package net.arvin.wanandroid.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 16:53
 * Function：
 * Desc：
 */
public class PageList<T> {


    /**
     * curPage : 2
     * offset : 20
     * over : false
     * pageCount : 284
     * size : 20
     * total : 5680
     */
    @SerializedName("datas")
    private List<T> data;
    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean hasNext() {
        return curPage < pageCount && notEmpty();
    }

    public boolean hasNextStartWithZero() {
        return curPage + 1 < pageCount && notEmpty();
    }

    public boolean notEmpty() {
        return data != null && data.size() > 0;
    }
}
