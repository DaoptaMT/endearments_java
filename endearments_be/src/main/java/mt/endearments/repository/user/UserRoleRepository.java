package mt.endearments.repository.user;

import mt.endearments.model.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<RoleUser, Long> {
}
