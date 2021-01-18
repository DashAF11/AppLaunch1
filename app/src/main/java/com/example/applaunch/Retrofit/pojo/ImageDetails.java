
package com.example.applaunch.Retrofit.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDetails {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("totalHits")
    @Expose
    private Integer totalHits;
    @SerializedName("hits")
    @Expose
    private List<ImageInfo> hits = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public List<ImageInfo> getHits() {
        return hits;
    }

    public void setHits(List<ImageInfo> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "ImageDetails{" +
                "total=" + total +
                ", totalHits=" + totalHits +
                ", hits=" + hits +
                '}';
    }
}
