package com.atp.bdss.controllers;

/*
import com.atp.bds.entities.Root;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

*/


/*
@RestController
@RequestMapping("/ATP_BDS")
public class AuthController {

    private final OAuth2AuthorizedClientService clientService;

    public AuthController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/sign_in")
    public Map<String, Object> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        */
/*System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getEmail());
        System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getName());
        System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getPicture());
        System.out.println(oAuth2AuthenticationToken);*//*


        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }

    @GetMapping("/user-info")
    public String getUserInfo(Authentication authentication) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(),
                authentication.getName());

        OAuth2AccessToken accessToken = client.getAccessToken();
        String tokenValue = accessToken.getTokenValue();
        System.out.println(tokenValue);
        // Sử dụng tokenValue cho các mục đích cần thiết, như truy cập các API bảo vệ bằng OAuth 2.0
        return "Access Token: " + tokenValue;
    }



    public Root toPerson(Map<String, Object> map) {
        if (map == null)
            return null;
        Root root =new Root();
        root.setEmail((String) map.get("email"));
        root.setName((String) map.get("name"));
        root.setPicture((String) map.get("picture"));
        return root;
    }
}
*/
