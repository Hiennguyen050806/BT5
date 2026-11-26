package vn.iotstar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.VideoRepository;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public UserServiceImpl(UserRepository userRepository, VideoRepository videoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> authenticate(String usernameOrEmail, String rawPassword) {
        if (usernameOrEmail == null || rawPassword == null) {
            return Optional.empty();
        }
        String key = usernameOrEmail.trim();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(key, key);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.checkPassword(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public User register(String username, String rawPassword, String email, String fullname, String phone) {
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(PasswordUtil.hashPassword(rawPassword));
        user.setEmail(email.trim());
        user.setFullname(fullname != null ? fullname.trim() : "");
        user.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
        user.setRoleid(Constant.ROLE_USER);
        user.setStatus(Constant.STATUS_ACTIVE);
        user.setCreatedDate(new Date());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String keyword, Integer roleid, Integer status, Pageable pageable) {
        String normalizedKeyword = (keyword != null) ? keyword.trim() : null;
        return userRepository.searchUsers(normalizedKeyword, roleid, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsernameAndIdNot(String username, Integer id) {
        return userRepository.existsByUsernameIgnoreCaseAndIdNot(username.trim(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailAndIdNot(String email, Integer id) {
        return userRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByRole(int roleid) {
        return userRepository.countByRoleid(roleid);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return userRepository.countByStatus(Constant.STATUS_ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long countVideosByUser(Integer userId) {
        return videoRepository.countByUser_Id(userId);
    }
}
