package com.seungmoo.springboot.springbootweb;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

// 스프링부트는 Formatter를 Bean으로 등록하면 알아서 addFormatter 처리 해준다.
// but @Component 로 bean 등록 시 @WebMvcTest(슬라이싱 테스트)가 Web과 관련된 Bean만 등록하므로 이거를 쓰지 않음(테스트 fail)
// 그냥 addFormatter 쓸지 아님, Test소스에서 @SpringBootTest 통합테스트로 바꿀지 판단 필요
//@Component
public class PersonFormatter
        //implements Formatter<Person>
{
    //@Override
    public Person parse(String s, Locale locale) throws ParseException {
        Person person = new Person();
        person.setName(s);
        return person;
    }

    //@Override
    public String print(Person person, Locale locale) {
        return person.toString();
    }
}
