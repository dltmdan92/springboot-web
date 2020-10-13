package com.seungmoo.springboot.springbootweb;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
@Entity
public class Person {
    @Id @GeneratedValue// id 자동생성
    private Long id;

    private String name;

}
