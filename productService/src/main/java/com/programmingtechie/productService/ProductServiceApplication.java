package com.programmingtechie.productService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductServiceApplication {
    
        private static Logger log = LoggerFactory.getLogger(ProductServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
