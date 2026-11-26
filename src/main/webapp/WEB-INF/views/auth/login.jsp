<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Đăng Nhập - IoTStar Portal" scope="request" />
<%@ include file="/WEB-INF/views/common/web-header.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0 rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary rounded-circle mb-2" style="width: 56px; height: 56px;">
                            <i class="bi bi-box-arrow-in-right fs-3"></i>
                        </div>
                        <h3 class="fw-bold text-primary mb-1">Đăng Nhập</h3>
                        <p class="text-muted small mb-0">Sử dụng tài khoản của bạn để truy cập hệ thống</p>
                    </div>

                    <c:if test="${not empty alert}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i> <c:out value="${alert}" />
                        </div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success py-2 small" role="alert">
                            <i class="bi bi-check-circle-fill me-1"></i> <c:out value="${success}" />
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
                        <div class="mb-3">
                            <label for="username" class="form-label fw-semibold">Tên đăng nhập hoặc Email:</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                <input type="text" id="username" name="username"
                                       value="<c:out value='${username != null ? username : param.username}'/>"
                                       class="form-control ${errors.username != null ? 'is-invalid' : ''}"
                                       placeholder="Nhập username hoặc email..." required autofocus />
                                <c:if test="${errors.username != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.username}" /></div>
                                </c:if>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-lock"></i></span>
                                <input type="password" id="password" name="password"
                                       class="form-control ${errors.password != null ? 'is-invalid' : ''}"
                                       placeholder="Nhập mật khẩu..." required />
                                <c:if test="${errors.password != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.password}" /></div>
                                </c:if>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-4 small">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="remember" value="on" id="rememberMe">
                                <label class="form-check-label text-muted" for="rememberMe">Ghi nhớ đăng nhập (7 ngày)</label>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold shadow-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Đăng Nhập
                        </button>
                    </form>

                    <div class="alert alert-light border mt-4 mb-0 small text-muted">
                        <div class="fw-semibold text-dark mb-1"><i class="bi bi-info-circle text-primary me-1"></i>Tài khoản mẫu:</div>
                        <div>&bull; Admin: <code>admin</code> / <code>123</code></div>
                        <div>&bull; User: <code>user</code> / <code>123</code></div>
                    </div>

                    <div class="text-center mt-4 small text-muted">
                        Chưa có tài khoản?
                        <a href="${pageContext.request.contextPath}/register" class="fw-semibold text-decoration-none">Đăng ký ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/web-footer.jsp" %>
