package btl.g4.mock.service;

import btl.g4.mock.dto.BaseMessageDto;
import btl.g4.mock.dto.PaginationDto;
import btl.g4.mock.entity.Role;
import btl.g4.mock.entity.User;
import btl.g4.mock.type.UserStatus;

import java.util.List;

public interface UserService {

    User getUser(Integer id);

    PaginationDto<User> getAll(String text, int page, int size);

    List<Role> getAllRole();

    BaseMessageDto saveUser(User userRequest);

    BaseMessageDto updateUser(User userRequest);

    void deleteUser(List<Integer> userIds);

    void updateStatus(Integer userId, UserStatus status);

    List<User> getManagers();

    List<User> getRecruiter();
    List<User> getAdmin();
    List<User> getInterviewer();
    String getRole(String username);
    List<User> findAllById(List<Integer> ids);
    User getUserByUsername(String username);
}
