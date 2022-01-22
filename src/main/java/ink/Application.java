package ink;

import com.google.common.collect.ImmutableList;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class Application {
    @Autowired
    ApplicationContext ctx;

    @Autowired
    List<? extends INKRepositoryFactoryBean<?, ?, ?>> bean;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void afterInit() {
        doOtherCache("OTHER_1");
        doOtherCache("OTHER_2");
        doOtherCache("OTHER_3");
        doSampleCache("SAMPLE_1");
        doSampleCache("SAMPLE_2");
        doSampleCache("SAMPLE_3");
    }

    private void doOtherCache(String cacheName) {
        var repo = getInkRepositoryFactoryBean(OtherRepository.class).getRepo(cacheName);
        OtherEntity e = new OtherEntity();
        String name = System.currentTimeMillis() + "";
        e.setName(name);
        repo.save(1, e);
        System.out.println("repo cacheName: " + repo.cache().getName() + " / findAllIterator: " + ImmutableList.copyOf(repo.findAllByName(name)));
    }

    private void doSampleCache(String cacheName) {
        var repo = getInkRepositoryFactoryBean(SampleRepository.class).getRepo(cacheName);
        SampleEntity e = new SampleEntity();
        String name = System.currentTimeMillis() + "";
        e.setName(name);
        repo.save(1, e);
        System.out.println("repo cacheName: " + repo.cache().getName() + " / findAllIterator: " + ImmutableList.copyOf(repo.findAllByName(name)));
    }

    private <T extends IgniteRepository<?, ?>>INKRepositoryFactoryBean<T, ?, ?> getInkRepositoryFactoryBean(Class<T> clazz) {
        return (INKRepositoryFactoryBean<T, ?, ?>) bean.stream().filter(s -> s.getRepositoryInterface().equals(clazz)).findFirst().orElseThrow();
    }
}
