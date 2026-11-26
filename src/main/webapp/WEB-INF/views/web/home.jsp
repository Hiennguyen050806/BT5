<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Trang Chủ - IoTStar Portal (Spring Boot 4)" scope="request" />
<c:set var="activeNav" value="home" scope="request" />
<%@ include file="/WEB-INF/views/common/web-header.jsp" %>

<!-- Hero Section -->
<section class="hero-gradient text-white py-5">
    <div class="container">
        <div class="row align-items-center g-4">
            <div class="col-lg-8">
                <span class="badge bg-warning text-dark mb-2 px-3 py-2">
                    <i class="bi bi-stars me-1"></i> Spring Boot 4 &bull; JSP / JSTL &bull; Spring Data JPA
                </span>
                <h1 class="display-6 fw-bold mb-3">Hệ Thống Quản Lý Danh Mục &amp; Người Dùng</h1>
                <p class="lead opacity-90 mb-4">
                    Xây dựng trên dữ liệu từ bài tập <code>D:\web</code> với chức năng Đăng ký, Đăng nhập, Đăng xuất và
                    Admin CRUD đầy đủ cho <strong>Category</strong> &amp; <strong>User</strong> kèm Tìm kiếm và Phân trang.
                </p>
                <div class="d-flex flex-wrap gap-2">
                    <c:choose>
                        <c:when test="${sessionScope.account == null}">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-light fw-semibold px-4">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Đăng nhập ngay
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light fw-semibold px-4">
                                <i class="bi bi-person-plus me-1"></i> Tạo tài khoản mới
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.account.roleid == 1}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-warning fw-semibold px-4">
                                <i class="bi bi-speedometer2 me-1"></i> Vào trang Quản trị Admin
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-light fw-semibold px-4">
                                <i class="bi bi-folder-fill me-1"></i> Quản lý Category
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-light fw-semibold px-4">
                                <i class="bi bi-people-fill me-1"></i> Quản lý User
                            </a>
                        </c:when>
                        <c:otherwise>
                            <div class="bg-white bg-opacity-10 rounded-3 px-4 py-2 d-inline-flex align-items-center gap-2">
                                <i class="bi bi-check-circle-fill text-warning"></i>
                                <span>Xin chào <strong><c:out value="${sessionScope.account.fullname}" /></strong>! Bạn đang đăng nhập với quyền <strong><c:out value="${sessionScope.account.roleName}" /></strong>.</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card border-0 shadow rounded-4 text-dark">
                    <div class="card-body p-4">
                        <h6 class="fw-bold text-uppercase text-muted mb-3">
                            <i class="bi bi-bar-chart-fill text-primary me-1"></i> Thống kê hệ thống
                        </h6>
                        <div class="d-flex justify-content-between align-items-center py-2 border-bottom">
                            <span><i class="bi bi-folder2-open text-primary me-2"></i>Tổng danh mục</span>
                            <span class="badge bg-primary rounded-pill fs-6">${totalCategories}</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center py-2">
                            <span><i class="bi bi-people text-success me-2"></i>Tổng tài khoản</span>
                            <span class="badge bg-success rounded-pill fs-6">${totalUsers}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Flash messages -->
<div class="container mt-4">
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><c:out value="${success}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${error}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Active Categories Grid -->
    <div class="d-flex justify-content-between align-items-center border-bottom pb-2 mb-4">
        <h4 class="fw-bold mb-0 text-dark">
            <i class="bi bi-grid-fill text-primary me-2"></i>Danh Mục Đang Hoạt Động
        </h4>
        <c:if test="${sessionScope.account != null && sessionScope.account.roleid == 1}">
            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-sm btn-outline-primary">
                Quản lý tất cả <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </c:if>
    </div>

    <div class="row g-4">
        <c:forEach items="${categories}" var="cate">
            <div class="col-sm-6 col-md-4 col-lg-3">
                <div class="card h-100 border-0 shadow-sm rounded-3 overflow-hidden card-hover">
                    <c:choose>
                        <c:when test="${not empty cate.images && (cate.images.startsWith('http://') || cate.images.startsWith('https://'))}">
                            <c:set var="cateImg" value="${cate.images}" />
                        </c:when>
                        <c:when test="${not empty cate.images}">
                            <c:url value="/image" var="cateImg">
                                <c:param name="fname" value="${cate.images}" />
                            </c:url>
                        </c:when>
                        <c:otherwise>
                            <c:set var="cateImg" value="" />
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${not empty cateImg}">
                            <img src="${cateImg}" class="card-img-top" alt="<c:out value='${cate.categoryname}'/>"
                                 style="height: 150px; object-fit: cover;"
                                 onerror="this.onerror=null;this.src='https://placehold.co/320x160/e9ecef/6c757d?text=Category';">
                        </c:when>
                        <c:otherwise>
                            <div class="bg-light d-flex align-items-center justify-content-center text-muted" style="height: 150px;">
                                <i class="bi bi-image fs-1"></i>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body">
                        <h6 class="card-title fw-bold mb-1 text-truncate">
                            <c:out value="${cate.categoryname}" />
                        </h6>
                        <span class="badge bg-success-subtle text-success border border-success-subtle">
                            <i class="bi bi-check-circle me-1"></i>Hoạt động
                        </span>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty categories}">
            <div class="col-12">
                <div class="alert alert-light border text-center py-5 text-muted">
                    <i class="bi bi-folder-x fs-1 d-block mb-2"></i>
                    Hiện chưa có danh mục hoạt động nào.
                </div>
            </div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/web-footer.jsp" %>
