package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private Integer categoryid;

    @Nationalized
    @NotBlank(message = "Tên danh mục không được để trống!")
    @Size(max = 255, message = "Tên danh mục không được vượt quá 255 ký tự!")
    @Column(name = "CategoryName", length = 255, nullable = false)
    private String categoryname;

    @Nationalized
    @Column(name = "Images", length = 500)
    private String images;

    @Column(name = "Status")
    private int status = 1; // 1: Hoạt động, 0: Khóa

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    public Category() {
    }

    public Category(String categoryname, String images, int status) {
        this.categoryname = categoryname;
        this.images = images;
        this.status = status;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getCategoryId() {
        return categoryid;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryid = categoryId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Video> getVideos() {
        if (this.videos == null) {
            this.videos = new ArrayList<>();
        }
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Category [categoryid=" + categoryid + ", categoryname=" + categoryname + ", images=" + images
                + ", status=" + status + "]";
    }
}
