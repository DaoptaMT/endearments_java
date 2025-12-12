package mt.endearments.repository;

import mt.endearments.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class PermissionRepository implements JpaRepository<Permission, Long> {
}
