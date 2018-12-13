package io.anymobi.accounts;


import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {

        // Given
        String password = "1234";
        String username = "onjsdnjs@gmail.com";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.USER, AccountRole.ADMIN)).build();

        // When
        accountService.saveAccount(account);

        UserDetailsService userDetailsService =  accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        // Then
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();

    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFail() {

        String username = "random@gmail.com";
        accountService.loadUserByUsername(username);

    }

    @Test
    public void findByUsernameFail2() {

        String username = "random@gmail.com";
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e) {
            assertThat(e instanceof UsernameNotFoundException).isTrue();
            assertThat(e.getMessage()).containsSequence(username);
        }

    }

    @Test
    public void findByUsernameFail3() {

        String username = "random@gmail.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        accountService.loadUserByUsername(username);

    }

}