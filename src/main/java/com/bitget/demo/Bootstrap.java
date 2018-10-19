package com.bitget.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Bootstrap {

  public static void main(String[] args) {
    SpringApplication.run(Bootstrap.class, args);
  }

  @Value("${uri.market.path}")
  String market;

  @Autowired
  Client client;



  @PostConstruct
  private void init() {

    //行情websocket
    client.connect(market);

  }
}
