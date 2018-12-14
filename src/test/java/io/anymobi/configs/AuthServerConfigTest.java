package io.anymobi.configs;

import io.anymobi.accounts.Account;
import io.anymobi.accounts.AccountRole;
import io.anymobi.accounts.AccountService;
import io.anymobi.common.AppSecurityProperties;
import io.anymobi.common.BaseControllerTest;
import io.anymobi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {


    @Autowired
    AccountService accountService;

    @Autowired
    AppSecurityProperties appSecurityProperties;

    @Test
    @TestDescription("인증 토큰을 발급받는 테슽")
    public void getAuthToken() throws Exception {

        // Given
        String username = "onsjdnjs@gmail.com";
        String password = "1234";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                .build();
        accountService.saveAccount(account);

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appSecurityProperties.getDefaultClientId(), appSecurityProperties.getDefaultClientSecret()))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());


    }

}