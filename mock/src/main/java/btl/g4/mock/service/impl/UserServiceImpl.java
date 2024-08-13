package btl.g4.mock.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import btl.g4.mock.dto.BaseMessageDto;
import btl.g4.mock.dto.PaginationDto;
import btl.g4.mock.entity.Role;
import btl.g4.mock.entity.User;
import btl.g4.mock.repository.RoleRepository;
import btl.g4.mock.repository.UserRepository;
import btl.g4.mock.service.UserService;
import btl.g4.mock.type.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public PaginationDto<User> getAll(String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (StringUtils.hasText(text)) {
            userPage = userRepository.findByText(text.trim(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        return new PaginationDto<>(userPage.getContent(), userPage.getTotalPages(), userPage.getTotalElements());
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public BaseMessageDto saveUser(User userRequest) {
        Optional<User> userOpt = userRepository.findByUsername(userRequest.getUsername());
        if (userOpt.isPresent()) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("User không được tạo vì username đã tồn tại")
                    .build();
        }
        userOpt = userRepository.findByEmail(userRequest.getEmail());
        if (userOpt.isPresent()) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("User không được tạo vì email đã tồn tại")
                    .build();
        }

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(userRequest);
        return null;
    }

    @Override
    public BaseMessageDto updateUser(User userRequest) {
        List<User> users = userRepository.findAllByEmailAndIdNotIn(userRequest.getEmail(),
                List.of(userRequest.getId()));
        if (users.size() > 0) {
            return BaseMessageDto.builder()
                    .status(0)
                    .message("User không được tạo vì email này đã được liên kết với 1 tài khoản khác")
                    .build();
        }

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(userRequest);
        return null;
    }

    @Override
    public void deleteUser(List<Integer> userIds) {
        userRepository.deleteAllById(userIds);
    }

    @Override
    public void updateStatus(Integer userId, UserStatus status) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setStatus(status);
            userRepository.save(user.get());
        }
    }

    @Override
    public List<User> getManagers() {
        return userRepository.findUsersByRoleManager();
    }

    @Override
    public List<User> getRecruiter() {
        return userRepository.findUsersByRoleRecruiter();
    }

    @Override
    public List<User> getAdmin() {
        return userRepository.findUsersByRoleAdmin();
    }

    @Override
    public List<User> getInterviewer() {
        return userRepository.findUsersByRoleInterviewer();
    }

    @Override
    public String getRole(String username) {
        return userRepository.getRolesByUsername(username);
    }

    @Override
    public List<User> findAllById(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }


}
