package com.seungmoo.springboot.springbootweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest // @Component는 bean 등록하지 않는다.
// @Component로 bean 등록된 Formatter를 사용하기 위해.. SpringBootTest로 통합테스트 셋팅
@SpringBootTest
@AutoConfigureMockMvc // @SpringBootTest의 경우 MockMvc가 등록되지 않으므로 따로 셋팅해준다.
public class SampleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    // jackson의 ObjectMapper를 통해 json string을 만들 수 있다.(스프링 부트에서는 jackson 기본 장착)
    @Autowired
    ObjectMapper objectMapper;

    // WebConfig에서 Marshaller를 Injection 해준다.
    @Autowired
    Marshaller marshaller;

    /*
    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/hello/seungmoo"))
                    .andDo(print())
                    .andExpect(content().string("hello seungmoo"));
    }

    @Test
    public void helloWithReqParam() throws Exception {
        this.mockMvc.perform(get("/hello").param("name", "seungmoo"))
                    .andDo(print())
                    .andExpect(content().string("hello seungmoo"));
    }
    */

    @Test
    public void helloId() throws Exception {
        Person person = new Person();
        person.setName("seungmoo");
        Person savedPerson = personRepository.save(person);

        this.mockMvc.perform(get("/hello/id/" + savedPerson.getId().toString()))
                .andDo(print())
                .andExpect(content().string("hello seungmoo"));
    }

    @Test
    public void helloIdWithReqParam() throws Exception {
        Person person = new Person();
        person.setName("seungmoo");
        Person savedPerson = personRepository.save(person);

        this.mockMvc.perform(get("/hello/id").param("id", savedPerson.getId().toString()))
                .andDo(print())
                .andExpect(content().string("hello seungmoo"));
    }

    @Test
    public void helloStatic() throws Exception {
        this.mockMvc.perform(get("/index.html"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(Matchers.containsString("Hello Index")));
    }

    @Test
    public void mobileStatic() throws Exception {
        this.mockMvc.perform(get("/mobile/index.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Hello Mobile")))
                .andExpect(header().exists(HttpHeaders.CACHE_CONTROL)); // 헤더에 캐시 관련 내용이 있는지 체크해본다.
    }

    @Test
    public void stringMessage() throws Exception {
        this.mockMvc.perform(get("/message").content("hello"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("hello"));
    }

    @Test
    public void jsonMessage() throws Exception {
        Person person = new Person();
        person.setId(2019l);
        person.setName("seungmoo");

        String jsonString = objectMapper.writeValueAsString(person);

        this.mockMvc.perform(get("/jsonMessage")
                            .contentType(MediaType.APPLICATION_JSON) // Request Header에 Request정보가 어떤 타입인지 명시
                            .accept(MediaType.APPLICATION_JSON) // Request에 대한 Response로 어떠한 DataType을 원한다를 명시
                            .content(jsonString))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2019)) // jsonPath를 통해 json의 내용을 체크
                    .andExpect(jsonPath("$.name").value("seungmoo"));
    }

    @Test
    public void xmlMessage() throws Exception {
        Person person = new Person();
        person.setId(2019l);
        person.setName("seungmoo");

        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        marshaller.marshal(person, result);
        String xmlString = stringWriter.toString();

        this.mockMvc.perform(get("/jsonMessage")
                .contentType(MediaType.APPLICATION_XML) // Request Header에 Request정보가 어떤 타입인지 명시
                .accept(MediaType.APPLICATION_XML) // Request에 대한 Response로 어떠한 DataType을 원한다를 명시
                // spring mvc에서는 request Header가 중요하다. 원하는 응답을 제대로 받으려면 accept header를 명시해주는게 좋다.
                .content(xmlString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("person/name").string("seungmoo"))
                .andExpect(xpath("person/id").number(2019d));
    }
}