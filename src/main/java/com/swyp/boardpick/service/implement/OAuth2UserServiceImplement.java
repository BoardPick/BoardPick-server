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
import java.util.Optional;
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

        if (oauthClientName.equals("kakao")) {

            String userCode = "kakao_" + oAuth2User.getAttributes().get("id");
            User user = userRepository.findByCode(userCode)
                    .orElseGet(() -> new User(userCode));

            Map<String, Object> attributes = oAuth2User.getAttributes();

            String nickname = Optional.ofNullable(attributes)
                    .map(attrs -> attrs.get("kakao_account"))
                    .filter(kakaoAccount -> kakaoAccount instanceof Map)
                    .map(kakaoAccount -> ((Map<?, ?>) kakaoAccount).get("profile"))
                    .filter(profile -> profile instanceof Map)
                    .map(profile -> ((Map<?, ?>) profile).get("nickname"))
                    .filter(nicknameObj -> nicknameObj instanceof String)
                    .map(nicknameObj -> (String) nicknameObj)
                    .orElse("보드픽");

            String profileImage = Optional.ofNullable(attributes)
                    .map(attrs -> attrs.get("kakao_account"))
                    .filter(kakaoAccount -> kakaoAccount instanceof Map)
                    .map(kakaoAccount -> ((Map<?, ?>) kakaoAccount).get("profile"))
                    .filter(profile -> profile instanceof Map)
                    .map(profile -> ((Map<?, ?>) profile).get("profile_image_url"))
                    .filter(profileImageUrl -> profileImageUrl instanceof String)
                    .map(profileImageUrl -> (String) profileImageUrl)
                    .orElse("http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640");

            user.setNickname(nickname);
            user.setProfileImage(profileImage);
            userRepository.save(user);

            return new CustomOAuth2User(userCode, authorities);
        }

        return null;
    }

}
