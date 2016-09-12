/**
Copyright (c) 2016. 上海趣医网络科技有限公司 版权所有
Shanghai QuYi Network Technology Co., Ltd. All Rights Reserved.

This is NOT a freeware,use is subject to license terms.
*/

package com.qy.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年4月6日 下午1:04:17   
 */
@Configuration
public class OAuth2SecurityConfigInJdbc {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
        	// only secure channel, https, is allowed
            //http.requiresChannel().anyRequest().requiresSecure();
            http
                .requestMatchers().antMatchers("/login/*")
            .and()
                .authorizeRequests().antMatchers("/users/**").access("#oauth2.hasScope('read')");
        }
        
        
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        	// TODO Auto-generated method stub
        	resources.resourceId("oauth2server");
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private AuthenticationManager authenticationManager;
        
        @Bean
		public DataSource dataSource() {
        	DriverManagerDataSource ds = new DriverManagerDataSource();
        	ds.setDriverClassName("com.mysql.jdbc.Driver");
        	ds.setUrl("jdbc:mysql://localhost:3307/leepon_db");
        	ds.setUsername("root");
        	ds.setPassword("root");
        	return ds;
		}

        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        	clients.jdbc(dataSource());
        }
        
        @Bean
        public JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource()); 
        }


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.allowFormAuthenticationForClients();
        }
        
        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setAccessTokenValiditySeconds(300);
            tokenServices.setRefreshTokenValiditySeconds(6000);
            tokenServices.setTokenStore(tokenStore);
            return tokenServices;
        }

    }

}
