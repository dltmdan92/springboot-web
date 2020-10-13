package com.seungmoo.springboot.springbootweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {

    // hi라는 view를 보여주기 위해 이렇게 Controller를 작성할 필요 없이
    // WebMvcConfigurer에서 viewController를 등록해주면 된다.
    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }
}
