package com.seungmoo.springboot.springbootweb;

import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {

    // URL 쿼리스트링에 오늘 name 값을 어떻게 Person 객체로 변환할 것인가
    // Formatter를 사용한다.
    /*
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") Person person) {
        return "hello " + person.getName();
    }
    */

    // URL 쿼리스트링에 오늘 name 값을 어떻게 Person 객체로 변환할 것인가
    // Formatter를 사용한다.
    /*
    @GetMapping("/hello")
    public String helloWithReqParam(@RequestParam("name") Person person) {
        return "hello " + person.getName();
    }
    */

    // DOMAIN DRIVEN CONVERTIING - SPRING DATA JPA에서 도메인 클래스 컨버터를 제공한다.
    // SPRING DATA JPA를 사용하면 따로 Formatter, Converter를 사용하지 않아도 된다
    // JPARepository를 통해서 ID에 해당하는 Entiry를 읽어 온다 (@Id 기반 @Entity)
    @GetMapping("/hello/id/{id}")   // junit 테스트로 진행하는 예제
    public String HelloId(@PathVariable("id") Person person) {
        return "hello " + person.getName();
    }

    // 핸들러 인터셉터
    // 핸들러 맵핑에 핸들러 인터셉터를 설정한다.
    // 핸들러 맵핑이 핸들러를 찾아내고 핸들러가 요청 처리하기 전에 인터셉터가 발동된다.(preHandle)
    // interceptor parameter에 handler가 들어가므로 해당 handler를 활용할 수도 있다.
    // Servlet filter 보다 구체적인 처리가 가능하다.
    // 일반적인 기능은 Servlet filter로 구현(대표: Cross-site-scripting(XSS) 해킹 차단하기, input form에서 script 넣는 행위)
    // Spring MVC 특화된 것은 HandlerInterceptor로 구현한다.
    // 순서 :
    // 1. preHandle - return boolean (true : 다음 intercepter or handler로 req, res 전달, false : 응답처리가 여기서 끝)
    //      preHandle 순차 호출 됨 (preHandle1 -> preHandler2)
    // 2. 핸들러가 요청처리
    // 3. postHandler (역순 호출 : postHandler2 -> postHandler1)
    // 4. 뷰 렌더링 (RestController는 뷰 렌더링 과정 X)
    // 5. afterCompletion (역순 호출 : afterCompletion2 -> afterCompletion1)
    @GetMapping("/hello/id")    // junit 테스트로 진행하는 예제
    public String HelloIdWithReqParam(@RequestParam("id") Person person) {
        return "hello " + person.getName();
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello IntelliJ";
    }

    // HttpMessageConverter는 @RequestBody, @ResponseBody를 사용할 때 적용된다.
    // @RequestBody 를 사용하면 요청 본문에 들어있는 Message를
    // HttpMessageConverter를 사용해서 그에 맞는 Type으로 Conversion한다.
    // @ResponseBody는 헨들러에서 HttpMessageConverter를 통해 리턴하는 값을 응답의 본문으로 넣어준다.
    // but @RestController를 썼기 때문에 @ResponseBody는 필요 X
    // @ResponseBody는 리턴타입에 붙여서 핸들러 선언하기도 한다. (리턴과 밀접하기 관련되기 때문에)
    @GetMapping("/message")
    public @ResponseBody String message(@RequestBody String body) {
        return body;
    }

    @GetMapping("/jsonMessage")
    public Person jsonMessage(@RequestBody Person person) {
        return person;
    }
}
