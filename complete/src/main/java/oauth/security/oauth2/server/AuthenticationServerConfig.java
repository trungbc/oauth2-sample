package oauth.security.oauth2.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import oauth.dao.repository.UserRepository;
import oauth.dao.repository.UsersTypeRepository;

/**
 * Authentication configuration
 */
@Configuration
public class AuthenticationServerConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository usersRepository;
	
	@Autowired
	private UsersTypeRepository usersTypeRepository;
	
	
	@Override
	@Autowired // DO NOT REMOVE THIS OR SPRING WILL USE DEFAULT AUTHENTICATION
				// MANAGER
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AntOauth2Provider(usersRepository, usersTypeRepository));
	}

}
