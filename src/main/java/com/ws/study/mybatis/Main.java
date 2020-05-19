package com.ws.study.mybatis;

import com.ws.study.mybatis.generator.GeneratorBuilder;

/**
 * Description：
 *
 * @author wangsong
 * @version 1.0
 * @since 5/18/2020 3:44 PM
 */
public class Main {
    public static void main(String[] args) {
        GeneratorBuilder.getInstance()
                .setUrl("jdbc:mysql://localhost:3306/springboot-mybatis?characterEncoding=utf8&useSSL=false")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("ws123456")
                .setPackageName("com.ws.study.mybatis.test")
                .setTablePrefix("edu")
                .setIncludeTableNames("edu_clazz")
                .setAuthor("ws")
                .generate();
    }
}
