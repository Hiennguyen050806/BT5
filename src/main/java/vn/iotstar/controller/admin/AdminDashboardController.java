package vn.iotstar.controller.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ICategoryService categoryService;
    private final IUserService userService;

    public AdminDashboardController(ICategoryService categoryService, IUserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping({ "", "/", "/dashboard" })
    public String dashboard(Model model) {
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("totalCategories", categoryService.countAll());
        model.addAttribute("activeCategories", categoryService.countActive());
        model.addAttribute("totalUsers", userService.countAll());
        model.addAttribute("activeUsers", userService.countActive());
        model.addAttribute("adminCount", userService.countByRole(Constant.ROLE_ADMIN));
        model.addAttribute("managerCount", userService.countByRole(Constant.ROLE_MANAGER));
        model.addAttribute("userCount", userService.countByRole(Constant.ROLE_USER));

        model.addAttribute("recentCategories",
                categoryService.searchCategories(null, null, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "categoryid"))).getContent());
        model.addAttribute("recentUsers",
                userService.searchUsers(null, null, null, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"))).getContent());

        return "admin/dashboard";
    }
}
