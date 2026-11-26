<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="403 - Không Có Quyền Truy Cập" scope="request" />
<%@ include file="/WEB-INF/views/common/web-header.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card border-0 shadow-sm rounded-4 text-center p-5">
                <div class="text-danger mb-3">
                    <i class="bi bi-shield-x" style="font-size: 4rem;"></i>
                </div>
                <h2 class="fw-bold text-danger">403 - Từ Chối Truy Cập</h2>
                <p class="text-muted mt-2 mb-4">
                    Tài khoản của bạn không có quyền <strong>Admin</strong> để truy cập khu vực quản trị này.
                    Vui lòng đăng nhập bằng tài khoản Quản trị viên hoặc quay lại trang chủ.
                </p>
                <div class="d-flex justify-content-center gap-2">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary px-4">
                        <i class="bi bi-house-door me-1"></i> Về Trang Chủ
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger px-4">
                        <i class="bi bi-box-arrow-right me-1"></i> Đổi tài khoản
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/web-footer.jsp" %>
