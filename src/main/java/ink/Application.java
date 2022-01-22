package ink;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    @Autowired
    ApplicationContext ctx;

    @Autowired
    INKRepositoryFactoryBean<SampleRepository, SampleEntity, Integer> bean;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void afterInit() {
        doCacheFun("SAMPLE_1");
        doCacheFun("SAMPLE_2");
        doCacheFun("SAMPLE_3");
    }

    private void doCacheFun(String cacheName) {
        var repo = bean.getRepo(cacheName);
        SampleEntity e = new SampleEntity();
        String name = System.currentTimeMillis() + "";
        e.setName(name);
        repo.save(1, e);
        System.out.println("repo cacheName: " + repo.cache().getName() + " / findAllIterator: " + ImmutableList.copyOf(repo.findAllByName(name)));
    }
}
