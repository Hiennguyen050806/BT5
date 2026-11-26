<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty pageTitle ? pageTitle : 'Admin Panel - Spring Boot 4'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { min-height: 100vh; background-color: #f4f6f9; margin: 0; }
        .admin-wrapper { display: flex; min-height: 100vh; }
        .admin-sidebar { width: 260px; background: #212529; color: #dee2e6; flex-shrink: 0; display: flex; flex-direction: column; }
        .admin-sidebar .brand { font-size: 1.1rem; font-weight: 800; padding: 18px 20px; border-bottom: 1px solid #343a40; color: #fff; display: flex; align-items: center; gap: 10px; text-decoration: none; }
        .admin-sidebar .nav-link-item { color: #c2c7d0; text-decoration: none; padding: 12px 20px; display: flex; align-items: center; gap: 10px; border-left: 3px solid transparent; transition: all 0.15s ease; }
        .admin-sidebar .nav-link-item:hover,
        .admin-sidebar .nav-link-item.active { color: #fff; background: #343a40; border-left-color: #0d6efd; }
        .admin-main { flex: 1; display: flex; flex-direction: column; min-width: 0; }
        .admin-topbar { background: #fff; border-bottom: 1px solid #dee2e6; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; }
        .admin-content { padding: 24px; flex: 1; }
        .admin-footer { background: #fff; border-top: 1px solid #dee2e6; padding: 14px 24px; font-size: 0.85rem; color: #6c757d; }
        .thumb-img { width: 72px; height: 50px; object-fit: cover; border-radius: 6px; border: 1px solid #dee2e6; }
        .avatar-img { width: 40px; height: 40px; object-fit: cover; border-radius: 50%; border: 1px solid #dee2e6; }
        .avatar-placeholder { width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.9rem; }
        @media (max-width: 991.98px) {
            .admin-wrapper { flex-direction: column; }
            .admin-sidebar { width: 100%; }
        }
    </style>
</head>
<body>
<div class="admin-wrapper">
    <!-- Sidebar -->
    <aside class="admin-sidebar">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="brand">
            <i class="bi bi-shield-lock-fill text-primary fs-4"></i>
            <span>ADMIN PANEL</span>
        </a>
        <div class="px-3 py-3 border-bottom border-secondary small">
            <div class="text-secondary">Đang đăng nhập:</div>
            <div class="fw-bold text-white mt-1">
                <i class="bi bi-person-badge me-1 text-info"></i>
                <c:out value="${not empty sessionScope.account.fullname ? sessionScope.account.fullname : sessionScope.account.username}" />
            </div>
            <div class="text-secondary mt-1">@<c:out value="${sessionScope.account.username}" /></div>
        </div>

        <nav class="py-2 flex-grow-1">
            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="nav-link-item ${activeMenu == 'dashboard' ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i> Tổng quan (Dashboard)
            </a>
            <a href="${pageContext.request.contextPath}/admin/categories"
               class="nav-link-item ${activeMenu == 'categories' ? 'active' : ''}">
                <i class="bi bi-folder-fill"></i> Quản lý Danh mục (Category)
            </a>
            <a href="${pageContext.request.contextPath}/admin/users"
               class="nav-link-item ${activeMenu == 'users' ? 'active' : ''}">
                <i class="bi bi-people-fill"></i> Quản lý Người dùng (User)
            </a>
            <hr class="border-secondary mx-3 my-2">
            <a href="${pageContext.request.contextPath}/home" class="nav-link-item">
                <i class="bi bi-house-door"></i> Về Trang Chủ
            </a>
        </nav>

        <div class="p-3 border-top border-secondary">
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit" class="btn btn-outline-danger btn-sm w-100">
                    <i class="bi bi-box-arrow-right me-1"></i> Đăng xuất
                </button>
            </form>
        </div>
    </aside>

    <!-- Main area -->
    <div class="admin-main">
        <header class="admin-topbar shadow-sm">
            <div class="d-flex align-items-center gap-2">
                <h5 class="mb-0 fw-bold text-dark">
                    <c:out value="${not empty pageHeader ? pageHeader : 'Bảng Điều Khiển Quản Trị'}" />
                </h5>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2">
                    <i class="bi bi-cpu me-1"></i> Spring Boot 4 + JSP/JSTL
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                    <i class="bi bi-power me-1"></i> Đăng xuất
                </a>
            </div>
        </header>

        <main class="admin-content">
