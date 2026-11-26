package vn.iotstar.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

/**
 * Khởi tạo dữ liệu mẫu tương thích từ D:\web (BaiTap1_ServletJDBC & BaiTap2_JPA)
 * khi bảng còn trống, giúp kiểm thử đăng nhập, tìm kiếm và phân trang ngay lập tức.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
    }

    private void seedUsers() {
        if (!userRepository.existsByUsernameIgnoreCase("admin")) {
            userRepository.save(new User(
                    "admin",
                    PasswordUtil.hashPassword("123"),
                    "admin@iotstar.vn",
                    "Nguyễn Hữu Trung (Admin)",
                    "0908617108",
                    null,
                    Constant.ROLE_ADMIN,
                    Constant.STATUS_ACTIVE
            ));
        }

        if (userRepository.count() <= 1) {
            List<User> sampleUsers = List.of(
                    new User("manager", PasswordUtil.hashPassword("123"), "manager@iotstar.vn",
                            "Trần Minh Quản (Manager)", "0912345678", null, Constant.ROLE_MANAGER, Constant.STATUS_ACTIVE),
                    new User("user", PasswordUtil.hashPassword("123"), "user@iotstar.vn",
                            "Lê Hoàng Khách", "0987654321", null, Constant.ROLE_USER, Constant.STATUS_ACTIVE),
                    new User("nguyenvana", PasswordUtil.hashPassword("123456"), "nguyenvana@gmail.com",
                            "Nguyễn Văn An", "0901112223", null, Constant.ROLE_USER, Constant.STATUS_ACTIVE),
                    new User("tranthib", PasswordUtil.hashPassword("123456"), "tranthib@gmail.com",
                            "Trần Thị Bình", "0902223334", null, Constant.ROLE_USER, Constant.STATUS_ACTIVE),
                    new User("levanc", PasswordUtil.hashPassword("123456"), "levanc@gmail.com",
                            "Lê Văn Cường", "0903334445", null, Constant.ROLE_MANAGER, Constant.STATUS_ACTIVE),
                    new User("phamthid", PasswordUtil.hashPassword("123456"), "phamthid@gmail.com",
                            "Phạm Thị Dung", "0904445556", null, Constant.ROLE_USER, Constant.STATUS_LOCKED),
                    new User("hoangvane", PasswordUtil.hashPassword("123456"), "hoangvane@gmail.com",
                            "Hoàng Văn Em", "0905556667", null, Constant.ROLE_USER, Constant.STATUS_ACTIVE),
                    new User("vuthif", PasswordUtil.hashPassword("123456"), "vuthif@gmail.com",
                            "Vũ Thị Phương", "0906667778", null, Constant.ROLE_USER, Constant.STATUS_ACTIVE)
            );
            for (User u : sampleUsers) {
                if (!userRepository.existsByUsernameIgnoreCase(u.getUsername())
                        && !userRepository.existsByEmailIgnoreCase(u.getEmail())) {
                    userRepository.save(u);
                }
            }
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> sampleCategories = List.of(
                    new Category("Công Nghệ & Lập Trình", "https://picsum.photos/seed/cat-tech/320/200", Constant.STATUS_ACTIVE),
                    new Category("Quần Áo Nam", "https://picsum.photos/seed/cat-men/320/200", Constant.STATUS_ACTIVE),
                    new Category("Quần Áo Nữ", "https://picsum.photos/seed/cat-women/320/200", Constant.STATUS_ACTIVE),
                    new Category("Điện Thoại & Phụ Kiện", "https://picsum.photos/seed/cat-phone/320/200", Constant.STATUS_ACTIVE),
                    new Category("Giải Trí & Âm Nhạc", "https://picsum.photos/seed/cat-music/320/200", Constant.STATUS_ACTIVE),
                    new Category("Thể Thao & Sức Khỏe", "https://picsum.photos/seed/cat-sport/320/200", Constant.STATUS_ACTIVE),
                    new Category("Giáo Dục & Khóa Học", "https://picsum.photos/seed/cat-edu/320/200", Constant.STATUS_ACTIVE),
                    new Category("Ẩm Thực & Đời Sống", "https://picsum.photos/seed/cat-food/320/200", Constant.STATUS_ACTIVE),
                    new Category("Du Lịch & Khám Phá", "https://picsum.photos/seed/cat-travel/320/200", Constant.STATUS_ACTIVE),
                    new Category("Thiết Kế Đồ Họa", "https://picsum.photos/seed/cat-design/320/200", Constant.STATUS_LOCKED),
                    new Category("Kinh Doanh & Khởi Nghiệp", "https://picsum.photos/seed/cat-biz/320/200", Constant.STATUS_ACTIVE),
                    new Category("Sách & Văn Phòng Phẩm", "https://picsum.photos/seed/cat-book/320/200", Constant.STATUS_ACTIVE)
            );
            categoryRepository.saveAll(sampleCategories);
        }
    }
}
