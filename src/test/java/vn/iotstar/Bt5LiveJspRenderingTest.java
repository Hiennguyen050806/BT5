package vn.iotstar;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;

/**
 * Kiểm thử biên dịch và render thực tế toàn bộ các trang JSP/JSTL trên Embedded Tomcat 11 (Spring Boot 4).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class Bt5LiveJspRenderingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testAllJspPagesCompileAndRenderWithTomcatJasperAndJstl() throws Exception {
        CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        // 1. Trang chủ, Login, Register render JSP chuẩn (HTTP 200, không lỗi Jasper, không lộ thẻ JSTL)
        assertJspRendered(client, "/home", 200, "Danh Mục Đang Hoạt Động");
        assertJspRendered(client, "/login", 200, "Đăng Nhập");
        assertJspRendered(client, "/register", 200, "Đăng Ký Tài Khoản");

        // 2. Đăng nhập bằng tài khoản User thường -> truy cập /admin bị chuyển hướng /access-denied (403)
        HttpResponse<String> userLoginResp = postForm(client, "/login", "username=user&password=123");
        assertThat(userLoginResp.statusCode()).isEqualTo(302);
        assertThat(userLoginResp.headers().firstValue("Location").orElse("")).endsWith("/home");

        HttpResponse<String> forbiddenRedirect = get(client, "/admin/categories");
        assertThat(forbiddenRedirect.statusCode()).isEqualTo(302);
        assertThat(forbiddenRedirect.headers().firstValue("Location").orElse("")).endsWith("/access-denied");
        assertJspRendered(client, "/access-denied", 403, "403 - Từ Chối Truy Cập");

        // 3. Đăng xuất User thường
        HttpResponse<String> logoutResp = postForm(client, "/logout", "");
        assertThat(logoutResp.statusCode()).isEqualTo(302);
        assertThat(logoutResp.headers().firstValue("Location").orElse("")).endsWith("/login");

        // 4. Đăng nhập bằng tài khoản Admin -> truy cập toàn bộ trang Admin JSP/JSTL
        HttpResponse<String> adminLoginResp = postForm(client, "/login", "username=admin&password=123");
        assertThat(adminLoginResp.statusCode()).isEqualTo(302);
        assertThat(adminLoginResp.headers().firstValue("Location").orElse("")).endsWith("/admin/dashboard");

        assertJspRendered(client, "/admin/dashboard", 200, "Tổng Quan Hệ Thống (Dashboard)");

        // Category List + Search + Pagination
        assertJspRendered(client, "/admin/categories?page=1&size=5", 200, "Danh Sách Danh Mục");
        assertJspRendered(client, "/admin/categories?page=2&size=5", 200, "Trang 2/");
        String encodedCatSearch = URLEncoder.encode("Công Nghệ", StandardCharsets.UTF_8);
        assertJspRendered(client, "/admin/categories?keyword=" + encodedCatSearch, 200, "Công Nghệ &amp; Lập Trình");
        assertJspRendered(client, "/admin/categories/add", 200, "Thêm Danh Mục Mới");

        Category sampleCate = categoryRepository.findAll().get(0);
        assertJspRendered(client, "/admin/categories/edit/" + sampleCate.getCategoryid(), 200, "Cập Nhật Thông Tin Danh Mục");

        // User List + Search + Filter + Pagination (kiểm tra cả c:, fmt:formatDate, fn:toUpperCase)
        assertJspRendered(client, "/admin/users?page=1&size=5", 200, "Danh Sách Người Dùng");
        assertJspRendered(client, "/admin/users?page=2&size=5", 200, "Trang 2/");
        assertJspRendered(client, "/admin/users?keyword=admin&roleid=1&status=1", 200, "admin@iotstar.vn");
        assertJspRendered(client, "/admin/users/add", 200, "Thêm Người Dùng Mới");

        User sampleUser = userRepository.findByUsername("user").orElseThrow();
        assertJspRendered(client, "/admin/users/edit/" + sampleUser.getId(), 200, "Cập Nhật Thông Tin Người Dùng");
    }

    private void assertJspRendered(HttpClient client, String path, int expectedStatus, String expectedContent) throws Exception {
        HttpResponse<String> resp = get(client, path);
        assertThat(resp.statusCode())
                .as("HTTP status for %s (body preview: %s)", path, resp.body().substring(0, Math.min(400, resp.body().length())))
                .isEqualTo(expectedStatus);
        String body = resp.body();
        assertThat(body).contains(expectedContent);
        assertThat(body).doesNotContain("<c:forEach");
        assertThat(body).doesNotContain("<fmt:formatDate");
        assertThat(body).doesNotContain("Whitelabel Error Page");
    }

    private HttpResponse<String> get(HttpClient client, String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url(path))).GET().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> postForm(HttpClient client, String path, String formBody) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url(path)))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(formBody, StandardCharsets.UTF_8))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
}
