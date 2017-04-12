package oauth.security.oauth2.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${config.oauth2.privateKey}")
	private String privateKey;

	@Value("${config.oauth2.publicKey}")
	private String publicKey;

	@Value("${config,oauth2.clientId}")
	private String clientId;

	@Value("${config.oauth2.secret}")
	private String secret;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ClientDetailsService clientDetailsService;

	@Bean
	public JwtAccessTokenConverter tokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(privateKey);
		converter.setVerifierKey(publicKey);

		return converter;
	}

	@Bean
	public InMemoryTokenStore tokenStore() {

		return new InMemoryTokenStore();
	}
	
	@Bean
	public UserApprovalHandler userApprovalHandler() throws Exception {
		ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
	    handler.setApprovalStore(approvalStore());
	    handler.setClientDetailsService(clientDetailsService);
	    handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));

	    return handler;
	}

	
	@Bean
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices services = new DefaultTokenServices();
		services.setTokenStore(tokenStore());
		services.setAccessTokenValiditySeconds(-1);
		services.setTokenEnhancer(tokenEnhancer());
		
		return services;
	}
	
	@Bean
	public ApprovalStore approvalStore() {
	    TokenApprovalStore store = new TokenApprovalStore();
	    store.setTokenStore(tokenStore());
	    return store;
	}

	/**
	 * Defines the security constraints on the token endpoints /oauth/token_key
	 * and /oauth/check_token Client credentials are required to access the
	 * endpoints
	 *
	 * @param oauthServer
	 *            Security configurer
	 * @throws Exception
	 *             when has exception
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
				.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");

	}

	/**
	 * Defines the authorization and token endpoints and the token services
	 *
	 * @param endpoints
	 *            Server endpoints configurer
	 * @throws Exception
	 *             when has exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints

				// Which authenticationManager should be used for the password
				// grant
				// If not provided, ResourceOwnerPasswordTokenGranter is not
				// configured
				.authenticationManager(authenticationManager)

				// Use JwtTokenStore and our jwtAccessTokenConverter
				.userApprovalHandler(userApprovalHandler()).tokenServices(tokenServices())
				;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(clientId).secret(secret).authorities("ROLE_TRUSTED_CLIENT")
				.authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
				.scopes("read", "write").redirectUris("http://localhost:9988/v1/api/pincode/create")
				.accessTokenValiditySeconds(-1).autoApprove(true)
				;

	}

}
