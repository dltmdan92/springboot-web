package com.seungmoo.springboot.springbootweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

// WebMvcConfigurer 는 중요한 개념이니 잘 공부 ㄱㄱ

/**
 * WebMvcConfigurer로 셋팅할 수 있는 것
 * formatter, interceptor, resourceHandler, messageConverter
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
    // 스프링부트는 Formatter가 Bean으로 등록되어 있으면 알아서 addFormatter 해준다.
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new PersonFormatter());
    }

    // 컨버터도 등록 가능
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(컨버터 객체);
    }
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 별도 순서지정 없으면 add 한 순서대로 interceptor가 적용된다.
        // registry.addInterceptor(new GreetingInterceptor());
        // registry.addInterceptor(new AnotherInterceptor());

        // order는 숫자가 낮으면 낮을 수록 우선순위
        // 0 보다 -1이 더 우선한다.
        registry.addInterceptor(new GreetingInterceptor()).order(0);
        registry.addInterceptor(new AnotherInterceptor())
                .addPathPatterns("/hi") // 이렇게 특정 path pattern에만 적용할 수 있다.
                .order(-1);
    }

    /**
     * 직접 리소스 핸들러를 등록해보자
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mobile/**") // 어떠한 패턴의 요청을 처리할지
                .addResourceLocations("classpath:/mobile/") // 해당 리소스들의 위치, classpath: -> 안주면 src/main/webapp에서 찾는다.
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES)) // 캐시 10분 설정하기(응답헤더에 캐시 관련 내용이 포함된다.
                .resourceChain(true); // 캐시 설정 여부, 보통 개발중 - false, 운영 - true로 한다.
                // .addResolver()
    }

    // HttpMessageConverter는 @RequestBody, @ResponseBody를 사용할 때 적용된다.
    // configureMessageConverters에 Converter를 추가하게 되면, 기본으로 제공되는 Converter를 사용 못하게 된다.
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    // 만약 기본 Converter에 추가하고 싶다면 extendMessageConverters를 쓰도록 하자.
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }
    // BUT, 위의 두 방식으로 Converter를 추가하기 보다는
    // 의존성 추가로 Converter를 추가하는 방식을 추천한다.
    // 메이븐 또는 그래들 설정에 의존성을 추가하면 그에 따른 컨버터가 자동으로 등록 된다. WebMvcConfigurationSupport(스프링 자체 기능임, 스프링부트 X)
    // WebMvcConfigurationSupport.class에서 classpath에 있는 Dependency를 보고 MessageConverter를 셋팅한다.
    // 스프링부트에서는 기본적으로 JacksonJSON2가 의존성에 들어있다. 즉, JSON용 HTTP 메시지 컨버터가 기본으로 등록되어 있다.
    // --> spring-boot-starter-web > starter-json에서 jackson이 있음

    // XML Converter의 경우, 스프링부트에서 기본으로 추가해주지 않음 -> 직접 등록
    // Object-XML Mapper 라이브러리 - JacksonXML, JAXB (요번에는 JAXB를 의존성으로 추가할 것임)
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(Person.class.getPackage().getName()); // packagesToScan에 XmlRootElement를 셋팅해줘야 jaxb가 Convert할 수 있다.
        return jaxb2Marshaller;
    }

    // hi라는 view를 보여주기 위해서 굳이 별도의 Controller 클래스 생성할 필요없이
    // 여기서 addViewController 처리를 해줄 수 있다.
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("hi").setViewName("hi");
    }
}
