<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${isEdit ? 'Chỉnh Sửa Danh Mục' : 'Thêm Danh Mục Mới'} - Admin" scope="request" />
<c:set var="pageHeader" value="${isEdit ? 'Chỉnh Sửa Danh Mục' : 'Thêm Danh Mục Mới'}" scope="request" />
<%@ include file="/WEB-INF/views/common/admin-header.jsp" %>

<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card border-0 shadow-sm rounded-3">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h5 class="mb-0 fw-bold text-primary">
                    <i class="bi ${isEdit ? 'bi-pencil-square' : 'bi-folder-plus'} me-2"></i>
                    ${isEdit ? 'Cập Nhật Thông Tin Danh Mục' : 'Thêm Danh Mục Mới'}
                </h5>
                <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary btn-sm">
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
                        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/categories/update/${category.categoryid}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/categories/save" />
                    </c:otherwise>
                </c:choose>

                <form action="${formAction}" method="post" enctype="multipart/form-data" novalidate>
                    <c:if test="${isEdit}">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Mã danh mục (ID):</label>
                            <input type="text" class="form-control bg-light" value="#${category.categoryid}" readonly />
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label for="categoryname" class="form-label fw-semibold">
                            Tên danh mục <span class="text-danger">*</span>:
                        </label>
                        <input type="text" id="categoryname" name="categoryname"
                               value="<c:out value='${category.categoryname}'/>"
                               class="form-control ${errors.categoryname != null ? 'is-invalid' : ''}"
                               placeholder="Nhập tên danh mục..." required />
                        <c:if test="${errors.categoryname != null}">
                            <div class="invalid-feedback"><c:out value="${errors.categoryname}" /></div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="images" class="form-label fw-semibold">Hình ảnh (URL trực tiếp):</label>
                        <input type="text" id="images" name="images"
                               value="<c:out value='${category.images}'/>"
                               class="form-control"
                               placeholder="https://example.com/image.png" />
                    </div>

                    <c:if test="${not empty category.images}">
                        <div class="mb-3">
                            <label class="form-label fw-semibold d-block">Ảnh hiện tại:</label>
                            <c:choose>
                                <c:when test="${category.images.startsWith('http://') || category.images.startsWith('https://')}">
                                    <c:set var="previewUrl" value="${category.images}" />
                                </c:when>
                                <c:otherwise>
                                    <c:url value="/image" var="previewUrl">
                                        <c:param name="fname" value="${category.images}" />
                                    </c:url>
                                </c:otherwise>
                            </c:choose>
                            <img id="catePreview" src="${previewUrl}" alt="Preview"
                                 class="img-thumbnail rounded shadow-sm"
                                 style="height: 120px; width: 170px; object-fit: cover;"
                                 onerror="this.onerror=null;this.src='https://placehold.co/170x120/e9ecef/6c757d?text=No+Image';" />
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label for="imageFile" class="form-label fw-semibold">Hoặc tải tệp ảnh lên:</label>
                        <input type="file" id="imageFile" name="imageFile"
                               class="form-control ${errors.imageFile != null ? 'is-invalid' : ''}"
                               accept="image/*" />
                        <c:if test="${errors.imageFile != null}">
                            <div class="invalid-feedback"><c:out value="${errors.imageFile}" /></div>
                        </c:if>
                        <div class="form-text small">Hỗ trợ .jpg, .jpeg, .png, .gif, .webp (tối đa 10MB). Tệp tải lên sẽ ưu tiên thay thế URL.</div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold d-block">Trạng thái:</label>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" id="statusActive" name="status" value="1"
                                   ${category.status == 1 ? 'checked' : ''} />
                            <label class="form-check-label text-success fw-semibold" for="statusActive">
                                <i class="bi bi-check-circle me-1"></i> Hoạt động
                            </label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" id="statusLocked" name="status" value="0"
                                   ${category.status != 1 ? 'checked' : ''} />
                            <label class="form-check-label text-danger fw-semibold" for="statusLocked">
                                <i class="bi bi-lock-fill me-1"></i> Khóa
                            </label>
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary px-4 fw-semibold shadow-sm">
                            <i class="bi bi-floppy-fill me-1"></i> ${isEdit ? 'Cập Nhật' : 'Lưu Danh Mục'}
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary px-4">
                            Hủy bỏ
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/admin-footer.jsp" %>
