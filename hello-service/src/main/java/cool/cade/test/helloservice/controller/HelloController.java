package cool.cade.test.helloservice.controller;

import org.apache.tomcat.jni.Time;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author :Ander
 * @date : 2022/8/4
 */

@RestController("/hello")
public class HelloController {
    @GetMapping("/sayhello")
    public String sayHello(String name){
        return "hello " + name;
    }

    @GetMapping("/time")
    public Date getTime(){
        return new Date();
    }

}
