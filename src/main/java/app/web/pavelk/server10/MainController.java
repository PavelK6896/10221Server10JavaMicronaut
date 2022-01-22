package app.web.pavelk.server10;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Controller("/")
@Slf4j
public class MainController {

    @Get
    public String index() {
        log.info("server 10");
        return "server 10";
    }

}