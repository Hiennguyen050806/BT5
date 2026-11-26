package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Nationalized
    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "phone", length = 20)
    private String phone;

    @Nationalized
    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "roleid")
    private int roleid = 3; // 1: Admin, 2: Manager, 3: User

    @Column(name = "status")
    private int status = 1; // 1: Hoạt động, 0: Khóa

    @Column(name = "otpCode", length = 10)
    private String otpCode;

    @Column(name = "otpExpiry")
    private Date otpExpiry;

    @Column(name = "createdDate")
    private Date createdDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String email, String fullname, String phone, String avatar,
                int roleid, int status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.avatar = avatar;
        this.roleid = roleid;
        this.status = status;
        this.createdDate = new Date();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = new Date();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Date getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Date otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Video> getVideos() {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getRoleName() {
        return switch (roleid) {
            case 1 -> "Admin";
            case 2 -> "Manager";
            default -> "User";
        };
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", fullname=" + fullname
                + ", roleid=" + roleid + ", status=" + status + "]";
    }
}
