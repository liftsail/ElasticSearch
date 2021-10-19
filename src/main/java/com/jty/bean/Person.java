package com.jty.bean;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Author zf
 * @Description
 * @Date 2021/10/18
 */
@Data
@ToString
public class Person {
    private Integer id;
    private String name;
    private Integer age;
    private Date birthday;

    public Person() {
    }

    public Person(Integer id, String name, Integer age, Date birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }
}
