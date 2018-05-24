package innovar.io.hello_world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;


import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class HelloWorldApplication {

    Map<Long,User>users=new HashMap<>();


    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    @Autowired
    UserRepositorio repositorio;
    @Bean
    RouterFunction<ServerResponse> function(){
        return route(GET("/hello"),rq-> ok().body(repositorio.findAll(),User.class))
                .andRoute(POST("/hello"),req->req.bodyToMono(User.class).flatMap(e-> ok().body(repositorio.insert(e),User.class)));
    }
}

@Service
interface UserRepositorio extends ReactiveMongoRepository<User,String> {
}

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
class User{
    @Id
    private String id;
    private String nombre;
    private String apellido;
}