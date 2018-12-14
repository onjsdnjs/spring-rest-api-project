package io.anymobi.configs;

import io.anymobi.accounts.Account;
import io.anymobi.accounts.AccountRole;
import io.anymobi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {


    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {

                Account onjsdnjs = Account.builder()
                        .email("onjsdnjs@gmail.com")
                        .password("1234")
                        .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                        .build();

                accountService.saveAccount(onjsdnjs);

            }
        };
    }


}
