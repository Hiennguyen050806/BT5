<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Admin Dashboard - Spring Boot 4" scope="request" />
<c:set var="pageHeader" value="Tổng Quan Hệ Thống (Dashboard)" scope="request" />
<%@ include file="/WEB-INF/views/common/admin-header.jsp" %>

<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i><c:out value="${success}" />
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- Stat Cards -->
<div class="row g-3 mb-4">
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-body d-flex align-items-center justify-content-between">
                <div>
                    <div class="text-muted small text-uppercase fw-semibold">Tổng Danh mục</div>
                    <div class="fs-2 fw-bold text-primary mt-1">${totalCategories}</div>
                    <div class="small text-success"><i class="bi bi-check-circle me-1"></i>${activeCategories} đang hoạt động</div>
                </div>
                <div class="bg-primary bg-opacity-10 text-primary rounded-3 p-3">
                    <i class="bi bi-folder-fill fs-3"></i>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-body d-flex align-items-center justify-content-between">
                <div>
                    <div class="text-muted small text-uppercase fw-semibold">Tổng Người dùng</div>
                    <div class="fs-2 fw-bold text-success mt-1">${totalUsers}</div>
                    <div class="small text-success"><i class="bi bi-person-check me-1"></i>${activeUsers} đang hoạt động</div>
                </div>
                <div class="bg-success bg-opacity-10 text-success rounded-3 p-3">
                    <i class="bi bi-people-fill fs-3"></i>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-body d-flex align-items-center justify-content-between">
                <div>
                    <div class="text-muted small text-uppercase fw-semibold">Quản trị &amp; Quản lý</div>
                    <div class="fs-2 fw-bold text-danger mt-1">${adminCount + managerCount}</div>
                    <div class="small text-muted">${adminCount} Admin &bull; ${managerCount} Manager</div>
                </div>
                <div class="bg-danger bg-opacity-10 text-danger rounded-3 p-3">
                    <i class="bi bi-shield-lock-fill fs-3"></i>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-6 col-xl-3">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-body d-flex align-items-center justify-content-between">
                <div>
                    <div class="text-muted small text-uppercase fw-semibold">Thành viên (User)</div>
                    <div class="fs-2 fw-bold text-info mt-1">${userCount}</div>
                    <div class="small text-muted">Tài khoản người dùng thường</div>
                </div>
                <div class="bg-info bg-opacity-10 text-info rounded-3 p-3">
                    <i class="bi bi-person-lines-fill fs-3"></i>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Quick Management Tables -->
<div class="row g-4">
    <!-- Recent Categories -->
    <div class="col-lg-6">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h6 class="mb-0 fw-bold text-primary"><i class="bi bi-folder2-open me-2"></i>Danh mục mới nhất</h6>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/categories/add" class="btn btn-sm btn-primary">
                        <i class="bi bi-plus-lg"></i> Thêm
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-sm btn-outline-secondary">
                        Xem tất cả
                    </a>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Tên danh mục</th>
                            <th class="text-center">Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentCategories}" var="c">
                            <tr>
                                <td><span class="badge bg-light text-dark border">#${c.categoryid}</span></td>
                                <td class="fw-semibold"><c:out value="${c.categoryname}" /></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${c.status == 1}">
                                            <span class="badge bg-success-subtle text-success">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger-subtle text-danger">Khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentCategories}">
                            <tr><td colspan="3" class="text-center text-muted py-4">Chưa có danh mục nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Recent Users -->
    <div class="col-lg-6">
        <div class="card border-0 shadow-sm rounded-3 h-100">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h6 class="mb-0 fw-bold text-success"><i class="bi bi-people me-2"></i>Người dùng mới nhất</h6>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/users/add" class="btn btn-sm btn-success">
                        <i class="bi bi-person-plus"></i> Thêm
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-sm btn-outline-secondary">
                        Xem tất cả
                    </a>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>Username</th>
                            <th>Họ tên</th>
                            <th class="text-center">Vai trò</th>
                            <th class="text-center">Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentUsers}" var="u">
                            <tr>
                                <td class="fw-semibold">@<c:out value="${u.username}" /></td>
                                <td><c:out value="${u.fullname}" /></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.roleid == 1}"><span class="badge bg-danger">Admin</span></c:when>
                                        <c:when test="${u.roleid == 2}"><span class="badge bg-warning text-dark">Manager</span></c:when>
                                        <c:otherwise><span class="badge bg-info text-dark">User</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.status == 1}"><span class="badge bg-success-subtle text-success">Hoạt động</span></c:when>
                                        <c:otherwise><span class="badge bg-secondary-subtle text-secondary">Khóa</span></c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentUsers}">
                            <tr><td colspan="4" class="text-center text-muted py-4">Chưa có người dùng nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/admin-footer.jsp" %>
