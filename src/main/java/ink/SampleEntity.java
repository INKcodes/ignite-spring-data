package ink;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class SampleEntity {

    @QuerySqlField
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SampleEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
