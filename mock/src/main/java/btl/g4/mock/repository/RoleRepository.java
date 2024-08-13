package btl.g4.mock.repository;

import btl.g4.mock.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
