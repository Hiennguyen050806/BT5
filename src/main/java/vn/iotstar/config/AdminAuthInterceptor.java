package vn.iotstar.config;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;

/**
 * Interceptor bảo vệ toàn bộ route /admin/** — chỉ cho phép tài khoản Admin (roleid == 1) đang hoạt động.
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final IUserService userService;

    public AdminAuthInterceptor(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute(Constant.SESSION_ACCOUNT) : null;

        // Thử khôi phục từ Remember-Me cookie nếu chưa có session
        if (sessionUser == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    Optional<User> cookieUser = userService.findByUsername(cookie.getValue());
                    if (cookieUser.isPresent() && cookieUser.get().getStatus() == Constant.STATUS_ACTIVE) {
                        sessionUser = cookieUser.get();
                        session = request.getSession(true);
                        session.setAttribute(Constant.SESSION_ACCOUNT, sessionUser);
                        session.setAttribute(Constant.SESSION_USERNAME, sessionUser.getUsername());
                    }
                    break;
                }
            }
        }

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?authRequired=true");
            return false;
        }

        // Xác thực lại trạng thái và quyền từ CSDL
        Optional<User> freshUserOpt = userService.findById(sessionUser.getId());
        if (freshUserOpt.isEmpty() || freshUserOpt.get().getStatus() != Constant.STATUS_ACTIVE) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?locked=true");
            return false;
        }

        User freshUser = freshUserOpt.get();
        if (session != null) {
            session.setAttribute(Constant.SESSION_ACCOUNT, freshUser);
            session.setAttribute(Constant.SESSION_USERNAME, freshUser.getUsername());
        }

        if (freshUser.getRoleid() != Constant.ROLE_ADMIN) {
            response.sendRedirect(request.getContextPath() + "/access-denied");
            return false;
        }

        return true;
    }
}
