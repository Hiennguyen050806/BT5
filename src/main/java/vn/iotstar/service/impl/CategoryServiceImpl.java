package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.VideoRepository;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final VideoRepository videoRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, VideoRepository videoRepository) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> searchCategories(String keyword, Integer status, Pageable pageable) {
        String normalizedKeyword = (keyword != null) ? keyword.trim() : null;
        return categoryRepository.searchCategories(normalizedKeyword, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findActiveCategories() {
        return categoryRepository.findByStatusOrderByCategoryidDesc(Constant.STATUS_ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String categoryname) {
        return categoryRepository.existsByCategorynameIgnoreCase(categoryname.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndIdNot(String categoryname, Integer id) {
        return categoryRepository.existsByCategorynameIgnoreCaseAndCategoryidNot(categoryname.trim(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return categoryRepository.countByStatus(Constant.STATUS_ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long countVideosByCategory(Integer categoryId) {
        return videoRepository.countByCategory_Categoryid(categoryId);
    }
}
