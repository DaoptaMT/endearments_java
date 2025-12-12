package mt.endearments.repository;

import mt.endearments.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class RoleRepository implements JpaRepository<Role, Long> {
}
