package ink;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableIgniteRepositories(repositoryFactoryBeanClass = INKRepositoryFactoryBean.class)
public class SpringAppCfg {
    /**
     * Creating Apache Ignite instance bean. A bean will be passed
     * to IgniteRepositoryFactoryBean to initialize all Ignite based Spring Data repositories and connect to a cluster.
     */
    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        // Setting some custom name for the node.
        cfg.setIgniteInstanceName("springDataNode");

        // Enabling peer-class loading feature.
        cfg.setPeerClassLoadingEnabled(true);

        CacheConfiguration<Integer, SampleEntity> ccfg1 = new CacheConfiguration<>("SAMPLE_1");
        ccfg1.setIndexedTypes(Integer.class, SampleEntity.class);
        CacheConfiguration<Integer, SampleEntity> ccfg2 = new CacheConfiguration<>("SAMPLE_2");
        ccfg2.setIndexedTypes(Integer.class, SampleEntity.class);
        CacheConfiguration<Integer, SampleEntity> ccfg3 = new CacheConfiguration<>("SAMPLE_3");
        ccfg3.setIndexedTypes(Integer.class, SampleEntity.class);
        cfg.setCacheConfiguration(ccfg1, ccfg2, ccfg3);

        return Ignition.start(cfg);
    }
}