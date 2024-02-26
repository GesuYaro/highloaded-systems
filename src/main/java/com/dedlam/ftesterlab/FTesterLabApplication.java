package com.dedlam.ftesterlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FTesterLabApplication {

  public static void main(String[] args) {
    SpringApplication.run(FTesterLabApplication.class, args);
  }

}
