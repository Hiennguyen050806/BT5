<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Đăng Ký Tài Khoản - IoTStar Portal" scope="request" />
<%@ include file="/WEB-INF/views/common/web-header.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow-sm border-0 rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center bg-success bg-opacity-10 text-success rounded-circle mb-2" style="width: 56px; height: 56px;">
                            <i class="bi bi-person-plus-fill fs-3"></i>
                        </div>
                        <h3 class="fw-bold text-success mb-1">Đăng Ký Tài Khoản</h3>
                        <p class="text-muted small mb-0">Điền đầy đủ thông tin để tạo tài khoản mới</p>
                    </div>

                    <c:if test="${not empty alert}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i> <c:out value="${alert}" />
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post" novalidate>
                        <div class="mb-3">
                            <label for="username" class="form-label fw-semibold">Tên đăng nhập <span class="text-danger">*</span>:</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                <input type="text" id="username" name="username"
                                       value="<c:out value='${username != null ? username : param.username}'/>"
                                       class="form-control ${errors.username != null ? 'is-invalid' : ''}"
                                       placeholder="Ví dụ: nguyenvana (3-30 ký tự)" required />
                                <c:if test="${errors.username != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.username}" /></div>
                                </c:if>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold">Email <span class="text-danger">*</span>:</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                                <input type="email" id="email" name="email"
                                       value="<c:out value='${email != null ? email : param.email}'/>"
                                       class="form-control ${errors.email != null ? 'is-invalid' : ''}"
                                       placeholder="email@example.com" required />
                                <c:if test="${errors.email != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.email}" /></div>
                                </c:if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="fullname" class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span>:</label>
                                <input type="text" id="fullname" name="fullname"
                                       value="<c:out value='${fullname != null ? fullname : param.fullname}'/>"
                                       class="form-control ${errors.fullname != null ? 'is-invalid' : ''}"
                                       placeholder="Nguyễn Văn A" required />
                                <c:if test="${errors.fullname != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.fullname}" /></div>
                                </c:if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                                <input type="text" id="phone" name="phone"
                                       value="<c:out value='${phone != null ? phone : param.phone}'/>"
                                       class="form-control ${errors.phone != null ? 'is-invalid' : ''}"
                                       placeholder="0901234567" />
                                <c:if test="${errors.phone != null}">
                                    <div class="invalid-feedback"><c:out value="${errors.phone}" /></div>
                                </c:if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="password" class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span>:</label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="bi bi-lock"></i></span>
                                    <input type="password" id="password" name="password"
                                           class="form-control ${errors.password != null ? 'is-invalid' : ''}"
                                           placeholder="Tối thiểu 6 ký tự" required />
                                    <c:if test="${errors.password != null}">
                                        <div class="invalid-feedback"><c:out value="${errors.password}" /></div>
                                    </c:if>
                                </div>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="confirmPassword" class="form-label fw-semibold">Xác nhận mật khẩu <span class="text-danger">*</span>:</label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="bi bi-shield-check"></i></span>
                                    <input type="password" id="confirmPassword" name="confirmPassword"
                                           class="form-control ${errors.confirmPassword != null ? 'is-invalid' : ''}"
                                           placeholder="Nhập lại mật khẩu" required />
                                    <c:if test="${errors.confirmPassword != null}">
                                        <div class="invalid-feedback"><c:out value="${errors.confirmPassword}" /></div>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-success w-100 py-2 fw-semibold mt-2 shadow-sm">
                            <i class="bi bi-check2-circle me-1"></i> Đăng Ký Tài Khoản
                        </button>
                    </form>

                    <div class="text-center mt-4 small text-muted">
                        Đã có tài khoản?
                        <a href="${pageContext.request.contextPath}/login" class="fw-semibold text-decoration-none">Đăng nhập ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/web-footer.jsp" %>
