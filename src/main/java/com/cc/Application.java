package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author Caleb.Cheng
 *
 */
@SpringBootApplication
@ComponentScan
public class Application {
	
	/**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
