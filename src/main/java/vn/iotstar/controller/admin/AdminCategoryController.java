package vn.iotstar.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.FileStorageUtil;
import vn.iotstar.util.ValidationUtil;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("categoryid", "categoryname", "status");

    private final ICategoryService categoryService;
    private final FileStorageUtil fileStorageUtil;

    public AdminCategoryController(ICategoryService categoryService, FileStorageUtil fileStorageUtil) {
        this.categoryService = categoryService;
        this.fileStorageUtil = fileStorageUtil;
    }

    @GetMapping
    public String listCategories(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            @RequestParam(name = "sortField", required = false, defaultValue = "categoryid") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir,
            Model model) {

        int validPage = Math.max(1, page);
        int validSize = (size == 5 || size == 10 || size == 20 || size == 50) ? size : Constant.DEFAULT_PAGE_SIZE;
        String validSortField = ALLOWED_SORT_FIELDS.contains(sortField) ? sortField : "categoryid";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String normalizedSortDir = direction == Sort.Direction.ASC ? "asc" : "desc";

        Pageable pageable = PageRequest.of(validPage - 1, validSize, Sort.by(direction, validSortField));
        Page<Category> categoryPage = categoryService.searchCategories(keyword, status, pageable);

        // Nếu số trang vượt quá tổng số trang thực tế (ví dụ sau khi xóa), lùi về trang cuối
        if (categoryPage.getTotalPages() > 0 && validPage > categoryPage.getTotalPages()) {
            validPage = categoryPage.getTotalPages();
            pageable = PageRequest.of(validPage - 1, validSize, Sort.by(direction, validSortField));
            categoryPage = categoryService.searchCategories(keyword, status, pageable);
        }

        model.addAttribute("activeMenu", "categories");
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        model.addAttribute("status", status);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("pageSize", validSize);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("sortField", validSortField);
        model.addAttribute("sortDir", normalizedSortDir);
        model.addAttribute("reverseSortDir", "asc".equals(normalizedSortDir) ? "desc" : "asc");

        return "admin/category-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Category category = new Category();
        category.setStatus(Constant.STATUS_ACTIVE);
        model.addAttribute("activeMenu", "categories");
        model.addAttribute("category", category);
        model.addAttribute("isEdit", false);
        return "admin/category-form";
    }

    @PostMapping("/save")
    public String saveCategory(
            @RequestParam(name = "categoryname", required = false) String categoryname,
            @RequestParam(name = "images", required = false) String images,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isEmpty(categoryname)) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (categoryname.trim().length() > 255) {
            errors.put("categoryname", "Tên danh mục không được vượt quá 255 ký tự!");
        } else if (categoryService.existsByName(categoryname)) {
            errors.put("categoryname", "Tên danh mục này đã tồn tại!");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            if (!ValidationUtil.isValidImageFile(imageFile.getOriginalFilename())) {
                errors.put("imageFile", "Ảnh tải lên phải có định dạng .jpg, .jpeg, .png, .gif hoặc .webp!");
            }
        }

        Category category = new Category();
        category.setCategoryname(categoryname != null ? categoryname.trim() : "");
        category.setImages(images != null ? images.trim() : "");
        category.setStatus(status == Constant.STATUS_ACTIVE ? Constant.STATUS_ACTIVE : Constant.STATUS_LOCKED);

        if (!errors.isEmpty()) {
            model.addAttribute("activeMenu", "categories");
            model.addAttribute("errors", errors);
            model.addAttribute("category", category);
            model.addAttribute("isEdit", false);
            return "admin/category-form";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String storedFileName = fileStorageUtil.storeImage(imageFile);
                category.setImages(storedFileName);
            } else if (ValidationUtil.isEmpty(images)) {
                category.setImages(null);
            }
        } catch (IOException e) {
            model.addAttribute("activeMenu", "categories");
            model.addAttribute("alert", "Lỗi khi lưu tệp hình ảnh: " + e.getMessage());
            model.addAttribute("category", category);
            model.addAttribute("isEdit", false);
            return "admin/category-form";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success",
                "Thêm mới danh mục \"" + category.getCategoryname() + "\" thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục có ID = " + id + "!");
            return "redirect:/admin/categories";
        }

        model.addAttribute("activeMenu", "categories");
        model.addAttribute("category", categoryOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/category-form";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(
            @PathVariable("id") Integer id,
            @RequestParam(name = "categoryname", required = false) String categoryname,
            @RequestParam(name = "images", required = false) String images,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        Optional<Category> existingOpt = categoryService.findById(id);
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục có ID = " + id + "!");
            return "redirect:/admin/categories";
        }

        Category existing = existingOpt.get();
        String oldImage = existing.getImages();
        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isEmpty(categoryname)) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (categoryname.trim().length() > 255) {
            errors.put("categoryname", "Tên danh mục không được vượt quá 255 ký tự!");
        } else if (categoryService.existsByNameAndIdNot(categoryname, id)) {
            errors.put("categoryname", "Tên danh mục này đã thuộc về một danh mục khác!");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            if (!ValidationUtil.isValidImageFile(imageFile.getOriginalFilename())) {
                errors.put("imageFile", "Ảnh tải lên phải có định dạng .jpg, .jpeg, .png, .gif hoặc .webp!");
            }
        }

        if (!errors.isEmpty()) {
            Category formCategory = new Category();
            formCategory.setCategoryid(id);
            formCategory.setCategoryname(categoryname);
            formCategory.setImages(!ValidationUtil.isEmpty(images) ? images.trim() : oldImage);
            formCategory.setStatus(status == Constant.STATUS_ACTIVE ? Constant.STATUS_ACTIVE : Constant.STATUS_LOCKED);

            model.addAttribute("activeMenu", "categories");
            model.addAttribute("errors", errors);
            model.addAttribute("category", formCategory);
            model.addAttribute("isEdit", true);
            return "admin/category-form";
        }

        existing.setCategoryname(categoryname.trim());
        existing.setStatus(status == Constant.STATUS_ACTIVE ? Constant.STATUS_ACTIVE : Constant.STATUS_LOCKED);

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String storedFileName = fileStorageUtil.storeImage(imageFile);
                fileStorageUtil.deleteLocalFile(oldImage);
                existing.setImages(storedFileName);
            } else if (!ValidationUtil.isEmpty(images)) {
                existing.setImages(images.trim());
            } else {
                existing.setImages(oldImage);
            }
        } catch (IOException e) {
            model.addAttribute("activeMenu", "categories");
            model.addAttribute("alert", "Lỗi khi cập nhật tệp hình ảnh: " + e.getMessage());
            model.addAttribute("category", existing);
            model.addAttribute("isEdit", true);
            return "admin/category-form";
        }

        categoryService.save(existing);
        redirectAttributes.addFlashAttribute("success",
                "Cập nhật danh mục \"" + existing.getCategoryname() + "\" thành công!");
        return "redirect:/admin/categories";
    }

    @RequestMapping(value = "/delete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<Category> existingOpt = categoryService.findById(id);
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục cần xóa!");
            return "redirect:/admin/categories";
        }

        Category category = existingOpt.get();
        long videoCount = categoryService.countVideosByCategory(id);
        if (videoCount > 0) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa danh mục \"" + category.getCategoryname()
                            + "\" vì vẫn còn " + videoCount + " video đang liên kết!");
            return "redirect:/admin/categories";
        }

        fileStorageUtil.deleteLocalFile(category.getImages());
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("success",
                "Đã xóa danh mục \"" + category.getCategoryname() + "\" thành công!");
        return "redirect:/admin/categories";
    }
}
