package vn.iotstar.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Integer id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

    long countByRoleid(int roleid);

    long countByStatus(int status);

    @Query("SELECT u FROM User u "
            + "WHERE (:keyword IS NULL OR :keyword = '' "
            + "   OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR LOWER(COALESCE(u.fullname, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR LOWER(COALESCE(u.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
            + "AND (:roleid IS NULL OR u.roleid = :roleid) "
            + "AND (:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("roleid") Integer roleid,
                           @Param("status") Integer status,
                           Pageable pageable);
}
