package ink;

import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    @Autowired SampleRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void afterInit() {
        SampleEntity e = new SampleEntity();
        e.setName("e");
        repo.save(1, e);
        List<SampleEntity> l = new ArrayList<>();
        repo.findAll().forEach(l::add);
        System.out.println(l);
    }
}
