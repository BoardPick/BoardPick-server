package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.Role;
import com.swyp.boardpick.domain.User;
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
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getDescription()));

        User user = null;
        String userCode = null;

        if (oauthClientName.equals("kakao")) {

            userCode = "kakao_" + oAuth2User.getAttributes().get("id");

            if (userRepository.findByCode(userCode).isPresent())
                return new CustomOAuth2User(userCode, authorities);

            user = new User(userCode);

            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String nickname = (String) profile.get("nickname");
            String profileImage = (String) profile.get("profile_image_url");

            user.setNickname(nickname);
            user.setProfileImage(profileImage);
            userRepository.save(user);
        }

        return new CustomOAuth2User(userCode, authorities);
    }

}
