package com.swyp.boardpick.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.boardpick.entity.CustomOAuth2User;
import com.swyp.boardpick.entity.UserEntity;
import com.swyp.boardpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }

        UserEntity userEntity = null;
        String userId = null;

        if (oauthClientName.equals("kakao")) {
            userId = "kakao_" + oAuth2User.getAttributes().get("id");
            if (userRepository.findByUserId(userId) != null) {
                userEntity = new UserEntity(userId, "kakao");
                userRepository.save(userEntity);
            }
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomOAuth2User(userId, authorities);
    }
}
