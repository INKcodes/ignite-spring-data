package ink;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

import java.util.Collection;

@RepositoryConfig
public interface OtherRepository extends IgniteRepository<OtherEntity, Integer> {
    Collection<OtherEntity> findAllByName(String name);
}
