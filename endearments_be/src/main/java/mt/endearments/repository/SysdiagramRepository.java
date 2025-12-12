package mt.endearments.repository;

import mt.endearments.model.Sysdiagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class SysdiagramRepository implements JpaRepository<Sysdiagram, Long> {
}
