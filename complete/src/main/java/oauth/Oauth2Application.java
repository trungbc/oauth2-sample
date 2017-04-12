package oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackages="oauth.dao.entity")
@ComponentScan(basePackages="oauth")
public class Oauth2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
	}
	

}
