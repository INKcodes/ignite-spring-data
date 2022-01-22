package ink;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

@RepositoryConfig(autoCreateCache = true, cacheName = "SAMPLE")
public interface SampleRepository extends IgniteRepository<SampleEntity, Integer> {
}
