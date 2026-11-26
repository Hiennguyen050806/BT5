<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Quản Lý Người Dùng (User) - Admin" scope="request" />
<c:set var="pageHeader" value="Quản Lý Người Dùng (User CRUD)" scope="request" />
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
            <h5 class="mb-0 fw-bold text-success">
                <i class="bi bi-people-fill me-2"></i>Danh Sách Người Dùng
            </h5>
            <small class="text-muted">Tìm kiếm đa trường (username, họ tên, email, SĐT), lọc vai trò/trạng thái và phân trang</small>
        </div>
        <a href="${pageContext.request.contextPath}/admin/users/add" class="btn btn-success fw-semibold shadow-sm">
            <i class="bi bi-person-plus-fill me-1"></i> Thêm Người Dùng
        </a>
    </div>

    <div class="card-body">
        <!-- Search & Filter Form -->
        <form action="${pageContext.request.contextPath}/admin/users" method="get" class="row g-2 align-items-end mb-4">
            <input type="hidden" name="sortField" value="${sortField}" />
            <input type="hidden" name="sortDir" value="${sortDir}" />

            <div class="col-lg-4 col-md-6">
                <label for="keyword" class="form-label small fw-semibold text-muted mb-1">Từ khóa tìm kiếm</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="bi bi-search"></i></span>
                    <input type="text" id="keyword" name="keyword" value="<c:out value='${keyword}'/>"
                           class="form-control" placeholder="Username, họ tên, email, SĐT..." />
                </div>
            </div>

            <div class="col-lg-2 col-md-3">
                <label for="roleid" class="form-label small fw-semibold text-muted mb-1">Vai trò</label>
                <select id="roleid" name="roleid" class="form-select">
                    <option value="" ${roleid == null ? 'selected' : ''}>-- Tất cả --</option>
                    <option value="1" ${roleid == 1 ? 'selected' : ''}>Admin</option>
                    <option value="2" ${roleid == 2 ? 'selected' : ''}>Manager</option>
                    <option value="3" ${roleid == 3 ? 'selected' : ''}>User</option>
                </select>
            </div>

            <div class="col-lg-2 col-md-3">
                <label for="status" class="form-label small fw-semibold text-muted mb-1">Trạng thái</label>
                <select id="status" name="status" class="form-select">
                    <option value="" ${status == null ? 'selected' : ''}>-- Tất cả --</option>
                    <option value="1" ${status == 1 ? 'selected' : ''}>Hoạt động</option>
                    <option value="0" ${status == 0 ? 'selected' : ''}>Khóa</option>
                </select>
            </div>

            <div class="col-lg-2 col-md-6">
                <label for="size" class="form-label small fw-semibold text-muted mb-1">Hiển thị / trang</label>
                <select id="size" name="size" class="form-select">
                    <option value="5" ${pageSize == 5 ? 'selected' : ''}>5 dòng</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 dòng</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20 dòng</option>
                    <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 dòng</option>
                </select>
            </div>

            <div class="col-lg-2 col-md-6 d-flex gap-2">
                <button type="submit" class="btn btn-success flex-grow-1">
                    <i class="bi bi-funnel me-1"></i> Lọc
                </button>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-secondary" title="Đặt lại bộ lọc">
                    <i class="bi bi-arrow-counterclockwise"></i>
                </a>
            </div>
        </form>

        <!-- Users Table -->
        <div class="table-responsive">
            <table class="table table-hover table-bordered align-middle mb-0">
                <thead class="table-light text-center">
                    <tr>
                        <th style="width: 55px;">STT</th>
                        <th style="width: 70px;">
                            <c:url value="/admin/users" var="sortIdUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="id" />
                                <c:param name="sortDir" value="${sortField == 'id' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortIdUrl}" class="text-decoration-none text-dark">
                                ID
                                <c:if test="${sortField == 'id'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-numeric-down' : 'bi-sort-numeric-up-alt'} text-success"></i>
                                </c:if>
                            </a>
                        </th>
                        <th class="text-start">
                            <c:url value="/admin/users" var="sortUsernameUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="username" />
                                <c:param name="sortDir" value="${sortField == 'username' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortUsernameUrl}" class="text-decoration-none text-dark">
                                Tài khoản
                                <c:if test="${sortField == 'username'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-alpha-down' : 'bi-sort-alpha-up-alt'} text-success"></i>
                                </c:if>
                            </a>
                        </th>
                        <th class="text-start">Email &amp; SĐT</th>
                        <th style="width: 110px;">
                            <c:url value="/admin/users" var="sortRoleUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
                                <c:if test="${status != null}"><c:param name="status" value="${status}" /></c:if>
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="sortField" value="roleid" />
                                <c:param name="sortDir" value="${sortField == 'roleid' ? reverseSortDir : 'asc'}" />
                            </c:url>
                            <a href="${sortRoleUrl}" class="text-decoration-none text-dark">
                                Vai trò
                                <c:if test="${sortField == 'roleid'}">
                                    <i class="bi ${sortDir == 'asc' ? 'bi-sort-down-alt' : 'bi-sort-up'} text-success"></i>
                                </c:if>
                            </a>
                        </th>
                        <th style="width: 120px;">Trạng thái</th>
                        <th style="width: 120px;">Ngày tạo</th>
                        <th style="width: 160px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="u" varStatus="stt">
                        <tr>
                            <td class="text-center text-muted">${(currentPage - 1) * pageSize + stt.index + 1}</td>
                            <td class="text-center"><span class="badge bg-light text-dark border">#${u.id}</span></td>
                            <td>
                                <div class="d-flex align-items-center gap-2">
                                    <c:choose>
                                        <c:when test="${not empty u.avatar && (u.avatar.startsWith('http://') || u.avatar.startsWith('https://'))}">
                                            <img src="${u.avatar}" alt="Avatar" class="avatar-img"
                                                 onerror="this.onerror=null;this.style.display='none';" />
                                        </c:when>
                                        <c:when test="${not empty u.avatar && u.avatar != 'avatar.png'}">
                                            <c:url value="/image" var="avtUrl">
                                                <c:param name="fname" value="${u.avatar}" />
                                            </c:url>
                                            <img src="${avtUrl}" alt="Avatar" class="avatar-img"
                                                 onerror="this.onerror=null;this.style.display='none';" />
                                        </c:when>
                                        <c:otherwise>
                                            <span class="avatar-placeholder bg-primary-subtle text-primary">
                                                <c:out value="${fn:toUpperCase(fn:substring(u.username, 0, 1))}" />
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                    <div>
                                        <div class="fw-bold text-dark">
                                            <c:out value="${not empty u.fullname ? u.fullname : u.username}" />
                                            <c:if test="${sessionScope.account != null && sessionScope.account.id == u.id}">
                                                <span class="badge bg-primary ms-1" style="font-size: 0.7rem;">Bạn</span>
                                            </c:if>
                                        </div>
                                        <div class="small text-muted">@<c:out value="${u.username}" /></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="small"><i class="bi bi-envelope text-muted me-1"></i><c:out value="${u.email}" /></div>
                                <c:if test="${not empty u.phone}">
                                    <div class="small text-muted"><i class="bi bi-telephone me-1"></i><c:out value="${u.phone}" /></div>
                                </c:if>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.roleid == 1}">
                                        <span class="badge bg-danger">Admin</span>
                                    </c:when>
                                    <c:when test="${u.roleid == 2}">
                                        <span class="badge bg-warning text-dark">Manager</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info text-dark">User</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.status == 1}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                            <i class="bi bi-check-circle me-1"></i>Hoạt động
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-2 py-1">
                                            <i class="bi bi-lock-fill me-1"></i>Khóa
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center small text-muted">
                                <c:choose>
                                    <c:when test="${u.createdDate != null}">
                                        <fmt:formatDate value="${u.createdDate}" pattern="dd/MM/yyyy" />
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <div class="d-inline-flex gap-1">
                                    <a href="${pageContext.request.contextPath}/admin/users/edit/${u.id}"
                                       class="btn btn-sm btn-outline-primary" title="Chỉnh sửa">
                                        <i class="bi bi-pencil-square me-1"></i>Sửa
                                    </a>
                                    <c:choose>
                                        <c:when test="${sessionScope.account != null && sessionScope.account.id == u.id}">
                                            <button type="button" class="btn btn-sm btn-outline-secondary" disabled
                                                    title="Không thể xóa tài khoản đang đăng nhập">
                                                <i class="bi bi-trash me-1"></i>Xóa
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="${pageContext.request.contextPath}/admin/users/delete/${u.id}"
                                                  method="post" class="d-inline"
                                                  onsubmit="return confirm('Bạn có chắc chắn muốn xóa người dùng này không?');">
                                                <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa">
                                                    <i class="bi bi-trash me-1"></i>Xóa
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="8" class="text-center py-5 text-muted">
                                <i class="bi bi-people fs-1 d-block mb-2"></i>
                                Không tìm thấy người dùng nào phù hợp.
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
                        <strong>${(currentPage - 1) * pageSize + users.size()}</strong>
                        trên tổng <strong>${totalItems}</strong> người dùng (Trang ${currentPage}/${totalPages})
                    </c:when>
                    <c:otherwise>
                        Tổng cộng <strong>0</strong> người dùng
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${totalPages > 1}">
                <nav aria-label="Phân trang người dùng">
                    <ul class="pagination pagination-sm mb-0">
                        <!-- First -->
                        <c:url value="/admin/users" var="firstUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
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
                        <c:url value="/admin/users" var="prevUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
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
                            <c:url value="/admin/users" var="pageUrl">
                                <c:param name="keyword" value="${keyword}" />
                                <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
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
                        <c:url value="/admin/users" var="nextUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
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
                        <c:url value="/admin/users" var="lastUrl">
                            <c:param name="keyword" value="${keyword}" />
                            <c:if test="${roleid != null}"><c:param name="roleid" value="${roleid}" /></c:if>
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
