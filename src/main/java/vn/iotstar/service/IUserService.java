package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.User;

public interface IUserService {

    Optional<User> authenticate(String usernameOrEmail, String rawPassword);

    User register(String username, String rawPassword, String email, String fullname, String phone);

    Page<User> searchUsers(String keyword, Integer roleid, Integer status, Pageable pageable);

    List<User> findAll();

    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

    User save(User user);

    void deleteById(Integer id);

    long countAll();

    long countByRole(int roleid);

    long countActive();

    long countVideosByUser(Integer userId);
}
