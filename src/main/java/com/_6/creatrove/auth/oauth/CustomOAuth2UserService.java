package com._6.creatrove.auth.oauth;

import com._6.creatrove.auth.oauth.info.GoogleUserInfo;
import com._6.creatrove.auth.oauth.info.OAuth2UserInfo;
import com._6.creatrove.user.domain.User;
import com._6.creatrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = switch (registrationId) {
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("지원하지 않는 provider: " + registrationId);
        };

        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .name(userInfo.getName())
                                .email(userInfo.getEmail())
                                .build()
                ));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}