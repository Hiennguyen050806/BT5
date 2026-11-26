<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Quản Lý Danh Mục (Category) - Admin" scope="request" />
<c:set var="pageHeader" value="Quản Lý Danh Mục (Category CRUD)" scope="request" />
<%@ include file="/WEB-INF/views/common/admin-header.jsp" %>

<!-- Flash Alerts -->
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

<div class="card border-0 shadow-sm rounded-3">
    <div class="card-header bg-white py-3 d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div>
            <h5 class="mb-0 fw-bold text-primary">
                <i class="bi bi-folder-fill me-2"></i>Danh Sách Danh Mục
            </h5>
            <small class="text-muted">Tìm kiếm, lọc trạng thái và phân trang với Spring Data JPA</small>
        </div>
        <a href="${pageContext.request.contextPath}/admin/categories/add" class="btn btn-primary fw-semibold shadow-sm">
            <i class="bi bi-plus-circle me-1"></i> Thêm Danh Mục
        </a>
    </div>

    <div class="card-body">
        <!-- Search & Filter Form -->
        <form action="${pageContext.request.contextPath}/admin/categories" method="get" class="row g-2 align-items-end mb-4">
            <input type="hidden" name="sortField" value="${sortField}" />
            <input type="hidden" name="sortDir" value="${sortDir}" />

            <div class="col-md-5">
                <label for="keyword" class="form-label small fw-semibold text-muted mb-1">Từ khóa tìm kiếm</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="bi bi-search"></i></span>
                    <input type="text" id="keyword" name="keyword" value="<c:out value='${keyword}'/>"
                           class="form-control" placeholder="Tìm theo tên danh mục..." />
                </div>
            </div>

            <div class="col-md-3">
                <label for="status" class="form-label small fw-semibold text-muted mb-1">Trạng thái</label>
                <select id="status" name="status" class="form-select">
                    <option value="" ${status == null ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                    <option value="1" ${status == 1 ? 'selected' : ''}>Hoạt động</option>
                    <option value="0" ${status == 0 ? 'selected' : ''}>Khóa</option>
                </select>
            </div>

            <div class="col-md-2">
                <label for="size" class="form-label small fw-semibold text-muted mb-1">Hiển thị / trang</label>
                <select id="size" name="size" class="form-select">
                    <option value="5" ${pageSize == 5 ? 'selected' : ''}>5 dòng</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 dòng</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20 dòng</option>
                    <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 dòng</option>
                </select>
            </div>

            <div class="col-md-2 d-flex gap-2">
                <button type="submit" class="btn btn-primary flex-grow-1">
                    <i class="bi bi-funnel me-1"></i> Lọc
                </button>
                <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary" title="Đặt lại bộ lọc">
                    <i class="bi bi-arrow-counterclockwise"></i>
                </a>
            </div>
        </form>

        <!-- Category Table -->
        <div class="table-responsive">
            <table class="table table-hover table-bordered align-middle mb-0">
                <thead class="table-light text-center">
                    <tr>
                        <th style="width: 60px;">STT</th>
                        <th style="width: 90px;">
                            <c:url value="/admin/categories" var="sortIdUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="categoryid" />
                                <c:param name="sortDir" value="${sortField == 'categoryid' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortIdUrl}" class="text-decoration-none text-dark">
                                ID
                                <c:if test="${sortField == 'categoryid'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-numeric-down' : 'bi-sort-numeric-up-alt'} text-primary"></i>
                                </c:if>
                            </a>
                        </th>
                        <th style="width: 110px;">Hình ảnh</th>
                        <th class="text-start">
                            <c:url value="/admin/categories" var="sortNameUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="categoryname" />
                                <c:param name="sortDir" value="${sortField == 'categoryname' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortNameUrl}" class="text-decoration-none text-dark">
                                Tên danh mục
                                <c:if test="${sortField == 'categoryname'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-alpha-down' : 'bi-sort-alpha-up-alt'} text-primary"></i>
                                </c:if>
                            </a>
                        </th>
                        <th style="width: 140px;">
                            <c:url value="/admin/categories" var="sortStatusUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="status" />
                                <c:param name="sortDir" value="${sortField == 'status' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortStatusUrl}" class="text-decoration-none text-dark">
                                Trạng thái
                                <c:if test="${sortField == 'status'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-down-alt' : 'bi-sort-up'} text-primary"></i>
                                </c:if>
                            </a>
                        </th>
                        <th style="width: 170px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${categories}" var="cate" varStatus="stt">
                        <tr>
                            <td class="text-center text-muted">${(currentPage - 1) * pageSize + stt.index + 1}</td>
                            <td class="text-center"><span class="badge bg-light text-dark border">#${cate.categoryid}</span></td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${not empty cate.images && (cate.images.startsWith('http://') || cate.images.startsWith('https://'))}">
                                        <c:set var="imgUrl" value="${cate.images}" />
                                    </c:when>
                                    <c:when test="${not empty cate.images}">
                                        <c:url value="/image" var="imgUrl">
                                            <c:param name="fname" value="${cate.images}" />
                                        </c:url>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="imgUrl" value="" />
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${not empty imgUrl}">
                                        <img src="${imgUrl}" alt="<c:out value='${cate.categoryname}'/>" class="thumb-img"
                                             onerror="this.onerror=null;this.src='https://placehold.co/72x50/e9ecef/6c757d?text=Img';" />
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-secondary border px-2 py-2"><i class="bi bi-image"></i></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="fw-semibold"><c:out value="${cate.categoryname}" /></td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${cate.status == 1}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                            <i class="bi bi-check-circle me-1"></i>Hoạt động
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">
                                            <i class="bi bi-lock-fill me-1"></i>Khóa
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <div class="d-inline-flex gap-1">
                                    <a href="${pageContext.request.contextPath}/admin/categories/edit/${cate.categoryid}"
                                       class="btn btn-sm btn-outline-primary" title="Chỉnh sửa">
                                        <i class="bi bi-pencil-square me-1"></i>Sửa
                                    </a>
                                    <form action="${pageContext.request.contextPath}/admin/categories/delete/${cate.categoryid}"
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Bạn có chắc chắn muốn xóa danh mục này không?');">
                                        <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa">
                                            <i class="bi bi-trash me-1"></i>Xóa
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="6" class="text-center py-5 text-muted">
                                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                Không tìm thấy danh mục nào phù hợp.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Pagination & Summary -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mt-4 gap-3">
            <div class="small text-muted">
                <c:choose>
                    <c:when test="${totalItems > 0}">
                        Hiển thị <strong>${(currentPage - 1) * pageSize + 1}</strong> -
                        <strong>${(currentPage - 1) * pageSize + categories.size()}</strong>
                        trên tổng <strong>${totalItems}</strong> danh mục (Trang ${currentPage}/${totalPages})
                    </c:when>
                    <c:otherwise>
                        Tổng cộng <strong>0</strong> danh mục
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${totalPages > 1}">
                <nav aria-label="Phân trang danh mục">
                    <ul class="pagination pagination-sm mb-0">
                        <!-- First -->
                        <c:url value="/admin/categories" var="firstUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                            <c:param name="page" value="1" />
                            <c:param name="size" value="${pageSize}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${firstUrl}" title="Trang đầu">&laquo;</a>
                        </li>

                        <!-- Previous -->
                        <c:url value="/admin/categories" var="prevUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="size" value="${pageSize}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${prevUrl}">Trước</a>
                        </li>

                        <!-- Page Numbers -->
                        <c:forEach begin="1" end="${totalPages}" var="p">
                            <c:url value="/admin/categories" var="pageUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="${p}" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="${sortField}" />
                                <c:param name="sortDir" value="${sortDir}" />
                            </c:url>
                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                <a class="page-link" href="${pageUrl}">${p}</a>
                            </li>
                        </c:forEach>

                        <!-- Next -->
                        <c:url value="/admin/categories" var="nextUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="size" value="${pageSize}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${nextUrl}">Sau</a>
                        </li>

                        <!-- Last -->
                        <c:url value="/admin/categories" var="lastUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                            <c:param name="page" value="${totalPages}" />
                            <c:param name="size" value="${pageSize}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${lastUrl}" title="Trang cuối">&raquo;</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/admin-footer.jsp" %>
