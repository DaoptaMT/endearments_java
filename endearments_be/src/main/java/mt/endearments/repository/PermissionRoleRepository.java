package mt.endearments.repository;

import mt.endearments.model.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class PermissionRoleRepository implements JpaRepository<PermissionRole, Long> {
}
