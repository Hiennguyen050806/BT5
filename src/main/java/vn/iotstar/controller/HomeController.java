package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

@Controller
public class HomeController {

    private final ICategoryService categoryService;
    private final IUserService userService;

    public HomeController(ICategoryService categoryService, IUserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping({ "/", "/home" })
    public String home(HttpServletRequest request, Model model) {
        // Tự động khôi phục session nếu có Remember-Me cookie
        HttpSession session = request.getSession(false);
        if ((session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName()) && !ValidationUtil.isEmpty(cookie.getValue())) {
                    Optional<User> userOpt = userService.findByUsername(cookie.getValue());
                    if (userOpt.isPresent() && userOpt.get().getStatus() == Constant.STATUS_ACTIVE) {
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute(Constant.SESSION_ACCOUNT, userOpt.get());
                        newSession.setAttribute(Constant.SESSION_USERNAME, userOpt.get().getUsername());
                    }
                    break;
                }
            }
        }

        model.addAttribute("categories", categoryService.findActiveCategories());
        model.addAttribute("totalCategories", categoryService.countAll());
        model.addAttribute("totalUsers", userService.countAll());
        return "web/home";
    }

    @GetMapping("/access-denied")
    public String accessDenied(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "error/access-denied";
    }
}
