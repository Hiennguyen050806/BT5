package vn.iotstar.controller.admin;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.FileStorageUtil;
import vn.iotstar.util.PasswordUtil;
import vn.iotstar.util.ValidationUtil;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "username", "fullname", "email", "roleid", "status", "createdDate");

    private final IUserService userService;
    private final FileStorageUtil fileStorageUtil;

    public AdminUserController(IUserService userService, FileStorageUtil fileStorageUtil) {
        this.userService = userService;
        this.fileStorageUtil = fileStorageUtil;
    }

    @GetMapping
    public String listUsers(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "roleid", required = false) Integer roleid,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir,
            Model model) {

        int validPage = Math.max(1, page);
        int validSize = (size == 5 || size == 10 || size == 20 || size == 50) ? size : Constant.DEFAULT_PAGE_SIZE;
        String validSortField = ALLOWED_SORT_FIELDS.contains(sortField) ? sortField : "id";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String normalizedSortDir = direction == Sort.Direction.ASC ? "asc" : "desc";

        Pageable pageable = PageRequest.of(validPage - 1, validSize, Sort.by(direction, validSortField));
        Page<User> userPage = userService.searchUsers(keyword, roleid, status, pageable);

        if (userPage.getTotalPages() > 0 && validPage > userPage.getTotalPages()) {
            validPage = userPage.getTotalPages();
            pageable = PageRequest.of(validPage - 1, validSize, Sort.by(direction, validSortField));
            userPage = userService.searchUsers(keyword, roleid, status, pageable);
        }

        model.addAttribute("activeMenu", "users");
        model.addAttribute("userPage", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        model.addAttribute("roleid", roleid);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("pageSize", validSize);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("sortField", validSortField);
        model.addAttribute("sortDir", normalizedSortDir);
        model.addAttribute("reverseSortDir", "asc".equals(normalizedSortDir) ? "desc" : "asc");

        return "admin/user-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = new User();
        user.setRoleid(Constant.ROLE_USER);
        user.setStatus(Constant.STATUS_ACTIVE);
        model.addAttribute("activeMenu", "users");
        model.addAttribute("userForm", user);
        model.addAttribute("isEdit", false);
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String saveUser(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "avatar", required = false) String avatar,
            @RequestParam(name = "roleid", required = false, defaultValue = "3") int roleid,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isEmpty(username)) {
            errors.put("username", "Tên đăng nhập không được để trống!");
        } else if (!ValidationUtil.isValidUsername(username)) {
            errors.put("username", "Tên đăng nhập từ 3-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới!");
        } else if (userService.existsByUsername(username)) {
            errors.put("username", "Tên đăng nhập đã tồn tại trong hệ thống!");
        }

        if (ValidationUtil.isEmpty(password)) {
            errors.put("password", "Mật khẩu không được để trống!");
        } else if (!ValidationUtil.isValidPassword(password)) {
            errors.put("password", "Mật khẩu phải có độ dài tối thiểu 6 ký tự!");
        }

        if (ValidationUtil.isEmpty(email)) {
            errors.put("email", "Email không được để trống!");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Địa chỉ email không đúng định dạng!");
        } else if (userService.existsByEmail(email)) {
            errors.put("email", "Email này đã được sử dụng!");
        }

        if (ValidationUtil.isEmpty(fullname)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            errors.put("phone", "Số điện thoại phải gồm 10 chữ số hợp lệ (VD: 0908617108)!");
        }

        if (avatarFile != null && !avatarFile.isEmpty() && !ValidationUtil.isValidImageFile(avatarFile.getOriginalFilename())) {
            errors.put("avatarFile", "Ảnh đại diện phải có định dạng .jpg, .jpeg, .png, .gif hoặc .webp!");
        }

        int normalizedRole = (roleid == Constant.ROLE_ADMIN || roleid == Constant.ROLE_MANAGER) ? roleid : Constant.ROLE_USER;
        int normalizedStatus = (status == Constant.STATUS_ACTIVE) ? Constant.STATUS_ACTIVE : Constant.STATUS_LOCKED;

        User user = new User();
        user.setUsername(username != null ? username.trim() : "");
        user.setEmail(email != null ? email.trim() : "");
        user.setFullname(fullname != null ? fullname.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setAvatar(avatar != null ? avatar.trim() : "");
        user.setRoleid(normalizedRole);
        user.setStatus(normalizedStatus);

        if (!errors.isEmpty()) {
            model.addAttribute("activeMenu", "users");
            model.addAttribute("errors", errors);
            model.addAttribute("userForm", user);
            model.addAttribute("isEdit", false);
            return "admin/user-form";
        }

        user.setPassword(PasswordUtil.hashPassword(password));
        user.setPhone(!ValidationUtil.isEmpty(phone) ? phone.trim() : null);
        user.setCreatedDate(new Date());

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                user.setAvatar(fileStorageUtil.storeImage(avatarFile));
            } else if (ValidationUtil.isEmpty(avatar)) {
                user.setAvatar(null);
            }
        } catch (IOException e) {
            model.addAttribute("activeMenu", "users");
            model.addAttribute("alert", "Lỗi khi lưu ảnh đại diện: " + e.getMessage());
            model.addAttribute("userForm", user);
            model.addAttribute("isEdit", false);
            return "admin/user-form";
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("success",
                "Thêm mới người dùng \"" + user.getUsername() + "\" thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID = " + id + "!");
            return "redirect:/admin/users";
        }

        model.addAttribute("activeMenu", "users");
        model.addAttribute("userForm", userOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/user-form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "avatar", required = false) String avatar,
            @RequestParam(name = "roleid", required = false, defaultValue = "3") int roleid,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        Optional<User> existingOpt = userService.findById(id);
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng có ID = " + id + "!");
            return "redirect:/admin/users";
        }

        User existing = existingOpt.get();
        String oldAvatar = existing.getAvatar();
        User currentAdmin = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        boolean isSelf = currentAdmin != null && existing.getId().equals(currentAdmin.getId());

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isEmpty(username)) {
            errors.put("username", "Tên đăng nhập không được để trống!");
        } else if (!ValidationUtil.isValidUsername(username)) {
            errors.put("username", "Tên đăng nhập từ 3-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới!");
        } else if (userService.existsByUsernameAndIdNot(username, id)) {
            errors.put("username", "Tên đăng nhập đã thuộc về người dùng khác!");
        }

        if (!ValidationUtil.isEmpty(password) && !ValidationUtil.isValidPassword(password)) {
            errors.put("password", "Mật khẩu mới phải có độ dài tối thiểu 6 ký tự!");
        }

        if (ValidationUtil.isEmpty(email)) {
            errors.put("email", "Email không được để trống!");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Địa chỉ email không đúng định dạng!");
        } else if (userService.existsByEmailAndIdNot(email, id)) {
            errors.put("email", "Email này đã thuộc về người dùng khác!");
        }

        if (ValidationUtil.isEmpty(fullname)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            errors.put("phone", "Số điện thoại phải gồm 10 chữ số hợp lệ (VD: 0908617108)!");
        }

        if (avatarFile != null && !avatarFile.isEmpty() && !ValidationUtil.isValidImageFile(avatarFile.getOriginalFilename())) {
            errors.put("avatarFile", "Ảnh đại diện phải có định dạng .jpg, .jpeg, .png, .gif hoặc .webp!");
        }

        int normalizedRole = (roleid == Constant.ROLE_ADMIN || roleid == Constant.ROLE_MANAGER) ? roleid : Constant.ROLE_USER;
        int normalizedStatus = (status == Constant.STATUS_ACTIVE) ? Constant.STATUS_ACTIVE : Constant.STATUS_LOCKED;

        if (isSelf && (normalizedRole != Constant.ROLE_ADMIN || normalizedStatus != Constant.STATUS_ACTIVE)) {
            errors.put("roleid", "Không thể tự hạ quyền hoặc khóa tài khoản Admin đang đăng nhập!");
        }

        if (!errors.isEmpty()) {
            User formUser = new User();
            formUser.setId(id);
            formUser.setUsername(username);
            formUser.setEmail(email);
            formUser.setFullname(fullname);
            formUser.setPhone(phone);
            formUser.setAvatar(!ValidationUtil.isEmpty(avatar) ? avatar.trim() : oldAvatar);
            formUser.setRoleid(normalizedRole);
            formUser.setStatus(normalizedStatus);

            model.addAttribute("activeMenu", "users");
            model.addAttribute("errors", errors);
            model.addAttribute("userForm", formUser);
            model.addAttribute("isEdit", true);
            return "admin/user-form";
        }

        existing.setUsername(username.trim());
        existing.setEmail(email.trim());
        existing.setFullname(fullname.trim());
        existing.setPhone(!ValidationUtil.isEmpty(phone) ? phone.trim() : null);
        existing.setRoleid(normalizedRole);
        existing.setStatus(normalizedStatus);

        if (!ValidationUtil.isEmpty(password)) {
            existing.setPassword(PasswordUtil.hashPassword(password));
        }

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String storedFileName = fileStorageUtil.storeImage(avatarFile);
                fileStorageUtil.deleteLocalFile(oldAvatar);
                existing.setAvatar(storedFileName);
            } else if (!ValidationUtil.isEmpty(avatar)) {
                existing.setAvatar(avatar.trim());
            } else {
                existing.setAvatar(oldAvatar);
            }
        } catch (IOException e) {
            model.addAttribute("activeMenu", "users");
            model.addAttribute("alert", "Lỗi khi cập nhật ảnh đại diện: " + e.getMessage());
            model.addAttribute("userForm", existing);
            model.addAttribute("isEdit", true);
            return "admin/user-form";
        }

        User updated = userService.save(existing);
        if (isSelf) {
            session.setAttribute(Constant.SESSION_ACCOUNT, updated);
            session.setAttribute(Constant.SESSION_USERNAME, updated.getUsername());
        }

        redirectAttributes.addFlashAttribute("success",
                "Cập nhật người dùng \"" + updated.getUsername() + "\" thành công!");
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/delete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
    public String deleteUser(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<User> existingOpt = userService.findById(id);
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng cần xóa!");
            return "redirect:/admin/users";
        }

        User target = existingOpt.get();
        User currentAdmin = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (currentAdmin != null && target.getId().equals(currentAdmin.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa tài khoản Admin đang đăng nhập!");
            return "redirect:/admin/users";
        }

        long videoCount = userService.countVideosByUser(id);
        if (videoCount > 0) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa người dùng \"" + target.getUsername()
                            + "\" vì vẫn còn " + videoCount + " video đang liên kết!");
            return "redirect:/admin/users";
        }

        fileStorageUtil.deleteLocalFile(target.getAvatar());
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success",
                "Đã xóa người dùng \"" + target.getUsername() + "\" thành công!");
        return "redirect:/admin/users";
    }
}
