package ll.demo.webflux;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.sql.DataSourceDefinition;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Log4j2
class DemoController {

    @Autowired
    private DemoRepository demoRepository;

    @RequestMapping(value = "testFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux testFlux() {
        Flux<Object> ret = null;
        Flux<DemoEntity> deFlux = Flux.interval(Duration.ofSeconds(1))
                .map(item -> new DemoEntity(null, "name " + item));
        ret = deFlux.flatMap(this.demoRepository::save);
        return ret;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class DemoEntity {
    @Id
    private Integer id;

    private String name;
}

@Repository
interface DemoRepository extends ReactiveCrudRepository<DemoEntity, Integer> {
}