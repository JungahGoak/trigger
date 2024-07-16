package koa.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import koa.demo.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final KakaoLoginService kakaoLoginService;

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoApiKey+"&redirect_uri="+kakaoRedirectUri;
        model.addAttribute("location", location);

        return "login";
    }

    @RequestMapping("/login/oauth2")
    public String kakaoLogin(@RequestParam String code){
        // 1. 인가 코드 받기

        // 2. 토큰 받기
        String accessToken = kakaoLoginService.getAccessToken(code);

        // 3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        return "redirect:/result";
    }


}
