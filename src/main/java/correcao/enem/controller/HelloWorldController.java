package correcao.enem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("hello")
    public String helloWorld(@RequestParam String name) {
        return String.format("Hello world, %s", name);
    }
}
