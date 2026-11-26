package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategorynameIgnoreCase(String categoryname);

    boolean existsByCategorynameIgnoreCase(String categoryname);

    boolean existsByCategorynameIgnoreCaseAndCategoryidNot(String categoryname, Integer categoryid);

    List<Category> findByStatusOrderByCategoryidDesc(int status);

    long countByStatus(int status);

    @Query("SELECT c FROM Category c "
            + "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(c.categoryname) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
            + "AND (:status IS NULL OR c.status = :status)")
    Page<Category> searchCategories(@Param("keyword") String keyword,
                                    @Param("status") Integer status,
                                    Pageable pageable);
}
