package konkuk.aiku.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ec2TestController {

    @GetMapping("/ec2-test")
    public String ec2Test(){
        return "test ok";
    }
}
