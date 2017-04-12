package oauth.security.oauth2.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import oauth.dao.entity.User;
import oauth.dao.entity.UsersType;
import oauth.dao.repository.UserRepository;
import oauth.dao.repository.UsersTypeRepository;

public class AntOauth2Provider implements AuthenticationProvider {

	private UserRepository usersRepository;
	
	private UsersTypeRepository usersTypeRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AntOauth2Provider.class);

	public AntOauth2Provider(UserRepository usersRepository, UsersTypeRepository usersTypeRepository) {
		this.usersRepository = usersRepository;
		this.usersTypeRepository = usersTypeRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Check authentication
		String account = authentication.getName();
		String sessionKey = authentication.getCredentials().toString();
		String[] sessionCredential = sessionKey.split(";");
		
		LOGGER.info(new StringBuilder("Start oauth account : ").append(account).toString());
		User user = usersRepository.findByAccountAndTypeIdAndCredential(account, Integer.valueOf(sessionCredential[1]),
				sessionCredential[0]);

		if (user == null) {
			LOGGER.debug(new StringBuilder("Can not find this account : ").append(account).append("with credential : ").append(sessionKey).toString());
			throw new AuthorizationServiceException("Can not find this account :" + account);
		}
		// Get authority for this account
		UsersType userType = usersTypeRepository.findOne(user.getTypeId());
		if (userType == null) {
			LOGGER.debug(new StringBuilder("Can not find authority for this account : ").append(account).append("with credential : ").append(sessionKey).toString());
			throw new AuthorizationServiceException("Can not find authority for this account :" + account);
		}
		// Generate token
		String info = new StringBuilder(account).append(";").append(user.getTypeId()).toString();
		//Get Role 
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userType.getRole());
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);
		//Generate token
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(info, sessionKey, authorities);
		
		LOGGER.info(new StringBuilder("Finish oauth account : ").append(account).toString());
		return token;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}