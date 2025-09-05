package com.oneplane;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class mainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        log.info("Index URL 접근");
        return "index";
    }

    @GetMapping("/recommend")
    public String recommendPage() {
        return "recommend/main";
    }

    @GetMapping("/recommend/input")
    public String inputPage() {
        return "recommend/input";
    }

    @GetMapping("/recommend/loading")
    public String recommendLoadingPage() {
        return "recommend/loading";
    }

    @GetMapping("/recommend/result")
    public String recommendResultPage() {
        return "recommend/result";
    }
}
