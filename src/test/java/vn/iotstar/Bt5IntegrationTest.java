package vn.iotstar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.util.Constant;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Bt5IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testRegisterLoginLogoutFlow() throws Exception {
        // 1. Đăng ký tài khoản mới
        mockMvc.perform(post("/register")
                        .param("username", "sinhvien01")
                        .param("email", "sinhvien01@hcmute.edu.vn")
                        .param("fullname", "Nguyễn Sinh Viên")
                        .param("phone", "0908123456")
                        .param("password", "secret123")
                        .param("confirmPassword", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        assertThat(userRepository.existsByUsernameIgnoreCase("sinhvien01")).isTrue();

        // 2. Đăng nhập bằng tài khoản vừa đăng ký
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .param("username", "sinhvien01")
                        .param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        MockHttpSession userSession = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(userSession).isNotNull();
        User loggedUser = (User) userSession.getAttribute(Constant.SESSION_ACCOUNT);
        assertThat(loggedUser.getUsername()).isEqualTo("sinhvien01");

        // 3. Tài khoản User thường không được vào /admin/categories -> redirect /access-denied
        mockMvc.perform(get("/admin/categories").session(userSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/access-denied"));

        // 4. Đăng xuất
        mockMvc.perform(post("/logout").session(userSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // 5. Sau khi đăng xuất, vào /admin/categories phải chuyển về /login
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?authRequired=true"));
    }

    @Test
    void testAdminCategoryCrudSearchAndPagination() throws Exception {
        MockHttpSession adminSession = loginAsAdmin();

        // List + Pagination (12 seeded categories, size=5 -> 3 pages)
        mockMvc.perform(get("/admin/categories")
                        .param("page", "1")
                        .param("size", "5")
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category-list"))
                .andExpect(model().attribute("totalPages", 3))
                .andExpect(model().attribute("currentPage", 1));

        // Create Category
        mockMvc.perform(multipart("/admin/categories/save")
                        .param("categoryname", "Spring Boot 4 Masterclass")
                        .param("images", "https://example.com/sb4.png")
                        .param("status", "1")
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        Category created = categoryRepository.findByCategorynameIgnoreCase("Spring Boot 4 Masterclass").orElseThrow();

        // Search Category
        mockMvc.perform(get("/admin/categories")
                        .param("keyword", "Masterclass")
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItems", 1L));

        // Update Category
        mockMvc.perform(multipart("/admin/categories/update/" + created.getCategoryid())
                        .param("categoryname", "Spring Boot 4 Updated")
                        .param("images", "https://example.com/sb4-v2.png")
                        .param("status", "0")
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        Category updated = categoryRepository.findById(created.getCategoryid()).orElseThrow();
        assertThat(updated.getCategoryname()).isEqualTo("Spring Boot 4 Updated");
        assertThat(updated.getStatus()).isEqualTo(0);

        // Delete Category
        mockMvc.perform(post("/admin/categories/delete/" + created.getCategoryid())
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        assertThat(categoryRepository.findById(created.getCategoryid())).isEmpty();
    }

    @Test
    void testAdminUserCrudSearchAndPagination() throws Exception {
        MockHttpSession adminSession = loginAsAdmin();

        // List + Pagination
        mockMvc.perform(get("/admin/users")
                        .param("page", "1")
                        .param("size", "5")
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-list"))
                .andExpect(model().attribute("currentPage", 1));

        // Create User
        mockMvc.perform(multipart("/admin/users/save")
                        .param("username", "crud_test_user")
                        .param("password", "password123")
                        .param("email", "crud_test@iotstar.vn")
                        .param("fullname", "Kiểm Thử CRUD")
                        .param("phone", "0909998887")
                        .param("roleid", "2")
                        .param("status", "1")
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        User created = userRepository.findByUsername("crud_test_user").orElseThrow();
        assertThat(created.getRoleid()).isEqualTo(2);

        // Search User by keyword + role filter
        mockMvc.perform(get("/admin/users")
                        .param("keyword", "crud_test")
                        .param("roleid", "2")
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItems", 1L));

        // Update User (blank password preserves existing hash)
        String originalHash = created.getPassword();
        mockMvc.perform(multipart("/admin/users/update/" + created.getId())
                        .param("username", "crud_test_user")
                        .param("password", "")
                        .param("email", "crud_test@iotstar.vn")
                        .param("fullname", "Kiểm Thử CRUD Updated")
                        .param("phone", "0909998887")
                        .param("roleid", "3")
                        .param("status", "0")
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        User updated = userRepository.findById(created.getId()).orElseThrow();
        assertThat(updated.getFullname()).isEqualTo("Kiểm Thử CRUD Updated");
        assertThat(updated.getRoleid()).isEqualTo(3);
        assertThat(updated.getStatus()).isEqualTo(0);
        assertThat(updated.getPassword()).isEqualTo(originalHash);

        // Delete User
        mockMvc.perform(post("/admin/users/delete/" + created.getId())
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        assertThat(userRepository.findById(created.getId())).isEmpty();
    }

    private MockHttpSession loginAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"))
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
