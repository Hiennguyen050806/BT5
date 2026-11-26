package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface ICategoryService {

    Page<Category> searchCategories(String keyword, Integer status, Pageable pageable);

    List<Category> findAll();

    List<Category> findActiveCategories();

    Optional<Category> findById(Integer id);

    Category save(Category category);

    void deleteById(Integer id);

    boolean existsByName(String categoryname);

    boolean existsByNameAndIdNot(String categoryname, Integer id);

    long countAll();

    long countActive();

    long countVideosByCategory(Integer categoryId);
}
