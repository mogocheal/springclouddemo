package com.hxgis.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Created by Cici on 2017/7/10.
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
//                .requestMatchers()
        .authorizeRequests().antMatchers("/api/**").permitAll();
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/station/**").access("#oauth2.hasScope('read')")
//                .antMatchers("/api/surf/**").access("#oauth2.hasScope('read')")
////                .antMatchers("/api/surf/**").hasAnyAuthority("管理员")
//                .antMatchers("/api/oracle_101/**").access("#oauth2.hasScope('read')")
//                .antMatchers("/api/oracle_36/**").access("#oauth2.hasScope('read')")
//                .antMatchers("/api/miss/**").access("#oauth2.hasScope('read')")
//                .antMatchers("/api/cassadra/**").access("#oauth2.hasScope('read')")
//                .antMatchers("/api/thunder/**").access("#oauth2.hasScope('read')");
        // @formatter:on
    }


    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

}
