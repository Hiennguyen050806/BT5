package vn.iotstar.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

@Controller
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(name = "authRequired", required = false) String authRequired,
            @RequestParam(name = "locked", required = false) String locked,
            HttpServletRequest request,
            Model model) {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            User current = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            return current.getRoleid() == Constant.ROLE_ADMIN ? "redirect:/admin/dashboard" : "redirect:/home";
        }

        // Kiểm tra Remember-Me Cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName()) && !ValidationUtil.isEmpty(cookie.getValue())) {
                    Optional<User> userOpt = userService.findByUsername(cookie.getValue());
                    if (userOpt.isPresent() && userOpt.get().getStatus() == Constant.STATUS_ACTIVE) {
                        User user = userOpt.get();
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute(Constant.SESSION_ACCOUNT, user);
                        newSession.setAttribute(Constant.SESSION_USERNAME, user.getUsername());
                        return user.getRoleid() == Constant.ROLE_ADMIN ? "redirect:/admin/dashboard" : "redirect:/home";
                    }
                }
            }
        }

        if ("true".equals(authRequired)) {
            model.addAttribute("alert", "Vui lòng đăng nhập bằng tài khoản Admin để truy cập trang quản trị!");
        } else if ("true".equals(locked)) {
            model.addAttribute("alert", "Phiên đăng nhập không hợp lệ hoặc tài khoản đã bị khóa!");
        }

        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "remember", required = false) String remember,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Model model) {

        Map<String, String> errors = new HashMap<>();
        if (ValidationUtil.isEmpty(username)) {
            errors.put("username", "Tên đăng nhập hoặc email không được để trống!");
        }
        if (ValidationUtil.isEmpty(password)) {
            errors.put("password", "Mật khẩu không được để trống!");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("username", username);
            return "auth/login";
        }

        Optional<User> userOpt = userService.authenticate(username, password);
        if (userOpt.isEmpty()) {
            model.addAttribute("alert", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            model.addAttribute("username", username);
            return "auth/login";
        }

        User user = userOpt.get();
        if (user.getStatus() != Constant.STATUS_ACTIVE) {
            model.addAttribute("alert", "Tài khoản của bạn đang bị khóa! Vui lòng liên hệ Quản trị viên.");
            model.addAttribute("username", username);
            return "auth/login";
        }

        // Lưu thông tin vào Session
        HttpSession session = request.getSession(true);
        session.setAttribute(Constant.SESSION_ACCOUNT, user);
        session.setAttribute(Constant.SESSION_USERNAME, user.getUsername());

        // Xử lý Remember-Me Cookie (7 ngày)
        if ("on".equals(remember)) {
            Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, user.getUsername());
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            response.addCookie(cookie);
        }

        redirectAttributes.addFlashAttribute("success",
                "Đăng nhập thành công! Chào mừng " + (user.getFullname() != null && !user.getFullname().isBlank() ? user.getFullname() : user.getUsername()) + ".");

        if (user.getRoleid() == Constant.ROLE_ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            return "redirect:/home";
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
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

        if (ValidationUtil.isEmpty(email)) {
            errors.put("email", "Email không được để trống!");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Địa chỉ email không đúng định dạng!");
        } else if (userService.existsByEmail(email)) {
            errors.put("email", "Email này đã được sử dụng!");
        }

        if (ValidationUtil.isEmpty(fullname)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        } else if (fullname.trim().length() > 100) {
            errors.put("fullname", "Họ và tên không được vượt quá 100 ký tự!");
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            errors.put("phone", "Số điện thoại phải gồm 10 chữ số hợp lệ (VD: 0908617108)!");
        }

        if (ValidationUtil.isEmpty(password)) {
            errors.put("password", "Mật khẩu không được để trống!");
        } else if (!ValidationUtil.isValidPassword(password)) {
            errors.put("password", "Mật khẩu phải có độ dài tối thiểu 6 ký tự!");
        }

        if (ValidationUtil.isEmpty(confirmPassword)) {
            errors.put("confirmPassword", "Vui lòng xác nhận mật khẩu!");
        } else if (password != null && !confirmPassword.equals(password)) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp!");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("fullname", fullname);
            model.addAttribute("phone", phone);
            return "auth/register";
        }

        userService.register(username, password, email, fullname, phone);
        redirectAttributes.addFlashAttribute("success",
                "Đăng ký tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
        return "redirect:/login";
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(Constant.SESSION_ACCOUNT);
            session.removeAttribute(Constant.SESSION_USERNAME);
            session.invalidate();
        }

        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(cookie);

        redirectAttributes.addFlashAttribute("success", "Bạn đã đăng xuất khỏi hệ thống thành công!");
        return "redirect:/login";
    }
}
