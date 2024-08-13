package btl.g4.mock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import btl.g4.mock.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndEmail(String username, String email);

    List<User> findAllByEmailAndIdNotIn(String email, List<Integer> ids);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:text% OR u.username LIKE %:text% OR u.fullName LIKE %:text% OR u.phoneNumber LIKE %:text%")
    Page<User> findByText(@Param("text") String text, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_MANAGER'")
    List<User> findUsersByRoleManager();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_RECRUITER'")
    List<User> findUsersByRoleRecruiter();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_ADMIN'")
    List<User> findUsersByRoleAdmin();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_INTERVIEWER'")
    List<User> findUsersByRoleInterviewer();

    @Query("SELECT r.name FROM User u JOIN u.roles r WHERE u.username = :username")
    String getRolesByUsername(@Param("username") String username);
}
