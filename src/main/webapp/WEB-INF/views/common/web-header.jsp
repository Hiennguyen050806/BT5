<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty pageTitle ? pageTitle : 'Hệ Thống Quản Lý - Spring Boot 4'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { min-height: 100vh; display: flex; flex-direction: column; background-color: #f4f6f9; }
        .main-wrapper { flex: 1; }
        .navbar-brand { font-weight: 800; letter-spacing: 0.4px; }
        .hero-gradient { background: linear-gradient(135deg, #0d6efd 0%, #4f46e5 100%); }
        .card-hover { transition: transform 0.2s ease, box-shadow 0.2s ease; }
        .card-hover:hover { transform: translateY(-4px); box-shadow: 0 .5rem 1.25rem rgba(0, 0, 0, .1) !important; }
        footer { background: #1e242b; color: #adb5bd; }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container">
        <a class="navbar-brand text-info" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-layers-fill me-1"></i> IOTSTAR PORTAL
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${activeNav == 'home' ? 'active fw-semibold' : ''}" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house-door me-1"></i> Trang chủ
                    </a>
                </li>
                <c:if test="${sessionScope.account != null && sessionScope.account.roleid == 1}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-warning fw-semibold" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-shield-lock-fill me-1"></i> Quản trị (Admin)
                        </a>
                        <ul class="dropdown-menu shadow">
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                    <i class="bi bi-speedometer2 me-2 text-primary"></i> Bảng điều khiển
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/categories">
                                    <i class="bi bi-tags-fill me-2 text-success"></i> Quản lý Danh mục (Category)
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/users">
                                    <i class="bi bi-people-fill me-2 text-info"></i> Quản lý Người dùng (User)
                                </a>
                            </li>
                        </ul>
                    </li>
                </c:if>
            </ul>

            <div class="d-flex align-items-center gap-2">
                <c:choose>
                    <c:when test="${sessionScope.account == null}">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Đăng nhập
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">
                            <i class="bi bi-person-plus me-1"></i> Đăng ký
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span class="text-light small me-2">
                            <i class="bi bi-person-circle me-1 text-info"></i>
                            <strong><c:out value="${not empty sessionScope.account.fullname ? sessionScope.account.fullname : sessionScope.account.username}" /></strong>
                            <c:choose>
                                <c:when test="${sessionScope.account.roleid == 1}">
                                    <span class="badge bg-danger ms-1">Admin</span>
                                </c:when>
                                <c:when test="${sessionScope.account.roleid == 2}">
                                    <span class="badge bg-warning text-dark ms-1">Manager</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary ms-1">User</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <c:if test="${sessionScope.account.roleid == 1}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-warning btn-sm fw-semibold">
                                <i class="bi bi-gear-fill me-1"></i> Admin
                            </a>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline">
                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                <i class="bi bi-box-arrow-right me-1"></i> Đăng xuất
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
<main class="main-wrapper">
