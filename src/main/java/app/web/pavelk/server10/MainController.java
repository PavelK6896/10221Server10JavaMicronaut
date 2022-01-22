package app.web.pavelk.server10;

import app.web.pavelk.server10.dto.User;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.http.server.types.files.SystemFile;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Controller("/")
public class MainController {

    @Inject
    ResourceResolver resourceResolver;
    List<User> users;
    Integer id = 4;

    public MainController() {
        users = new ArrayList<>();
        users.add(User.builder().id(1).name("Bob").email("d@g").state("N").code("432").zip(133).build());
        users.add(User.builder().id(2).name("Jon").email("d@y").state("F").code("465").zip(534).build());
        users.add(User.builder().id(3).name("Ford").email("a@r").state("H").code("654").zip(654).build());
        users.add(User.builder().id(4).name("Tom").email("d@t").state("G").code("23").zip(23).build());
    }

    @Get("/s")
    public String server10() {
        log.info("server 10");
        return "server 10";
    }

    @Get(uris = {"/index-.html", "/"})
    @Produces(MediaType.TEXT_HTML)
    public HttpResponse<StreamedFile> index1(HttpRequest<?> httpRequest) {
        log.info(httpRequest.getMethodName());
        Optional<URL> resource = resourceResolver.getResource("classpath:index1.html");
        StreamedFile indexFile = new StreamedFile(resource.orElseThrow(() -> new RuntimeException("no index")));
        return HttpResponse.ok(indexFile);
    }

    @Get("/d")
    public SystemFile download() {
        File file = new File("src/main/resources/index1.html");
        log.info("d");
        return new SystemFile(file).attach("index.html");
    }

    @Get("/1")
    public List<User> getOne() {
        return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

    @Post("/1")
    public List<User> posOne(@Body User user) {
        log.info(user.toString());
        ++id;
        user.setId(id);
        user.setCode(ThreadLocalRandom.current().nextFloat() + "");
        user.setState(ThreadLocalRandom.current().nextBoolean() + "");
        user.setZip(ThreadLocalRandom.current().nextInt());
        users.add(user);
        return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

    @Put("/1")
    public List<User> putOne(@Body User user) {
        log.info(user.toString());
        if (users.removeIf(existingUser -> existingUser.getId().equals(user.getId()))) {
            users.add(user);
        }
        return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

    @Patch("/1")
    public List<User> patchOne(@Body User user) {
        log.info(user.toString());
        users.stream()
                .filter(f -> f.getId().equals(user.getId()))
                .findFirst()
                .ifPresent(f -> f.setZip(user.getZip()));
        return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

    @Delete("/1")
    public List<User> deleteOne(@Body User user) {
        log.info(user.toString());
        users.removeIf(existingUser -> existingUser.getId().equals(user.getId()));
        return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
    }

}