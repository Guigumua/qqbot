package qqbot.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
public class Test {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(Test.class, args);
		RobotProperties bean = run.getBean(RobotProperties.class);
		System.out.println(bean);
	}
}
