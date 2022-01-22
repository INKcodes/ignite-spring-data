package ink;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

import java.util.Collection;

@RepositoryConfig(cacheName = "SAMPLE_1")
public interface SampleRepository extends IgniteRepository<SampleEntity, Integer> {
    Collection<SampleEntity> findAllByName(String name);
}
