<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${isEdit ? 'Chỉnh Sửa Người Dùng' : 'Thêm Người Dùng Mới'} - Admin" scope="request" />
<c:set var="pageHeader" value="${isEdit ? 'Chỉnh Sửa Người Dùng' : 'Thêm Người Dùng Mới'}" scope="request" />
<%@ include file="/WEB-INF/views/common/admin-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h5 class="mb-0 fw-bold text-success">
                    <i class="bi ${isEdit ? 'bi-person-gear' : 'bi-person-plus-fill'} me-2"></i>
                    ${isEdit ? 'Cập Nhật Thông Tin Người Dùng' : 'Thêm Người Dùng Mới'}
                </h5>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách
                </a>
            </div>

            <div class="card-body p-4">
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger py-2 small" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-1"></i> <c:out value="${alert}" />
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${isEdit}">
                        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/users/update/${userForm.id}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/users/save" />
                    </c:otherwise>
                </c:choose>

                <form action="${formAction}" method="post" enctype="multipart/form-data" novalidate>
                    <c:if test="${isEdit}">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Mã người dùng (ID):</label>
                            <input type="text" class="form-control bg-light" value="#${userForm.id}" readonly />
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="username" class="form-label fw-semibold">
                                Tên đăng nhập <span class="text-danger">*</span>:
                            </label>
                            <input type="text" id="username" name="username"
                                   value="<c:out value='${userForm.username}'/>"
                                   class="form-control ${errors.username != null ? 'is-invalid' : ''}"
                                   placeholder="VD: nguyenvana" required />
                            <c:if test="${errors.username != null}">
                                <div class="invalid-feedback"><c:out value="${errors.username}" /></div>
                            </c:if>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="password" class="form-label fw-semibold">
                                Mật khẩu <c:if test="${!isEdit}"><span class="text-danger">*</span></c:if>:
                            </label>
                            <input type="password" id="password" name="password"
                                   class="form-control ${errors.password != null ? 'is-invalid' : ''}"
                                   placeholder="${isEdit ? 'Để trống nếu giữ nguyên mật khẩu cũ' : 'Tối thiểu 6 ký tự'}" />
                            <c:if test="${errors.password != null}">
                                <div class="invalid-feedback"><c:out value="${errors.password}" /></div>
                            </c:if>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fullname" class="form-label fw-semibold">
                                Họ và tên <span class="text-danger">*</span>:
                            </label>
                            <input type="text" id="fullname" name="fullname"
                                   value="<c:out value='${userForm.fullname}'/>"
                                   class="form-control ${errors.fullname != null ? 'is-invalid' : ''}"
                                   placeholder="Nguyễn Văn A" required />
                            <c:if test="${errors.fullname != null}">
                                <div class="invalid-feedback"><c:out value="${errors.fullname}" /></div>
                            </c:if>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                            <input type="text" id="phone" name="phone"
                                   value="<c:out value='${userForm.phone}'/>"
                                   class="form-control ${errors.phone != null ? 'is-invalid' : ''}"
                                   placeholder="0908617108" />
                            <c:if test="${errors.phone != null}">
                                <div class="invalid-feedback"><c:out value="${errors.phone}" /></div>
                            </c:if>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">
                            Email <span class="text-danger">*</span>:
                        </label>
                        <input type="email" id="email" name="email"
                               value="<c:out value='${userForm.email}'/>"
                               class="form-control ${errors.email != null ? 'is-invalid' : ''}"
                               placeholder="email@example.com" required />
                        <c:if test="${errors.email != null}">
                            <div class="invalid-feedback"><c:out value="${errors.email}" /></div>
                        </c:if>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="roleid" class="form-label fw-semibold">Vai trò (Role):</label>
                            <select id="roleid" name="roleid" class="form-select ${errors.roleid != null ? 'is-invalid' : ''}">
                                <option value="1" ${userForm.roleid == 1 ? 'selected' : ''}>Admin (Quản trị viên)</option>
                                <option value="2" ${userForm.roleid == 2 ? 'selected' : ''}>Manager (Quản lý)</option>
                                <option value="3" ${userForm.roleid == 3 || userForm.roleid == 0 ? 'selected' : ''}>User (Thành viên)</option>
                            </select>
                            <c:if test="${errors.roleid != null}">
                                <div class="invalid-feedback"><c:out value="${errors.roleid}" /></div>
                            </c:if>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold d-block">Trạng thái tài khoản:</label>
                            <div class="mt-2">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="uStatusActive" name="status" value="1"
                                           ${userForm.status == 1 ? 'checked' : ''} />
                                    <label class="form-check-label text-success fw-semibold" for="uStatusActive">
                                        <i class="bi bi-check-circle me-1"></i> Hoạt động
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="uStatusLocked" name="status" value="0"
                                           ${userForm.status != 1 ? 'checked' : ''} />
                                    <label class="form-check-label text-danger fw-semibold" for="uStatusLocked">
                                        <i class="bi bi-lock-fill me-1"></i> Khóa
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="avatar" class="form-label fw-semibold">Ảnh đại diện (URL):</label>
                        <input type="text" id="avatar" name="avatar"
                               value="<c:out value='${userForm.avatar}'/>"
                               class="form-control" placeholder="https://example.com/avatar.png" />
                    </div>

                    <div class="mb-4">
                        <label for="avatarFile" class="form-label fw-semibold">Hoặc tải ảnh đại diện lên:</label>
                        <input type="file" id="avatarFile" name="avatarFile"
                               class="form-control ${errors.avatarFile != null ? 'is-invalid' : ''}"
                               accept="image/*" />
                        <c:if test="${errors.avatarFile != null}">
                            <div class="invalid-feedback"><c:out value="${errors.avatarFile}" /></div>
                        </c:if>
                        <div class="form-text small">Hỗ trợ .jpg, .jpeg, .png, .gif, .webp.</div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success px-4 fw-semibold shadow-sm">
                            <i class="bi bi-floppy-fill me-1"></i> ${isEdit ? 'Cập Nhật' : 'Lưu Người Dùng'}
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-secondary px-4">
                            Hủy bỏ
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/admin-footer.jsp" %>
