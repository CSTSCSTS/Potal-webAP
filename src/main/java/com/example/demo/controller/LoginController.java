package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.model.LoginSession;
import com.example.demo.dto.AuthInfoDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForLoginDto;
import com.example.demo.exception.LoginFailureException;
import com.example.demo.service.LoginService;
import com.google.common.base.Objects;

@Controller
public class LoginController {

	@Autowired
	public LoginService loginService;

	@Autowired
	public LoginSession loginSession;

	@Autowired
	protected MessageSource messageSource;

	@Autowired
 private DiscoveryClient dc;


	// ログイン画面を表示する
	@GetMapping("/login")
 public String loginPage() {
     return "login";
 }

	@GetMapping("/auth-info")
	@ResponseBody
 public AuthInfoDto getAuthInfo() {
			return new AuthInfoDto(loginSession.getUserId().get(), loginSession.getUserName().get(), loginSession.isOauthUser());
 }

	@RequestMapping("/")
	public String get() {
		return "index";
	}

 // 認証失敗時に、ここにフォワードしてくる。
 @PostMapping("/login/error")
 public void error() throws LoginFailureException {
   throw new LoginFailureException(messageSource.getMessage("login.fali", null, Locale.JAPAN));
 }

//認証成功時に、ここにフォワードしてくる。
@PostMapping("/login/success")
public String success(String userName, String password) throws LoginFailureException {
		UserForLoginDto dto = loginService.loginBonus(userName);
  //セッションオブジェクトにログインユーザーの情報を格納
		loginSession.setUserId(dto.getUserId());
		loginSession.setUserName(userName);
		return "redirect:/";
}


//Oauth認証成功時に、ここにフォワードしてくる。
@RequestMapping("/oauth2loginSuccess")
public String oauth() throws Exception {
  // ユーザーが登録されていない場合はユーザー登録を実施。
 	RestTemplate restTemplate = new RestTemplate();
 	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
  ServiceInstance userApInstance = userApServiceList.get(0);
		String getOAuthUserUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/oauth-user?userName={userName}";
		UserDto user;
		try {
				user = restTemplate.getForEntity(getOAuthUserUrl, UserDto.class, userName).getBody();
		} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(getOAuthUserUrl);
		}
		if(Objects.equal(user, null)) {
				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				map.add("userName", userName);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
				String getAddOAuthUserUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/oauth-user";
				try {
						restTemplate.postForEntity(getAddOAuthUserUrl, request, UserDto.class).getBody();
				} catch (HttpClientErrorException e) {
						throw new Exception();
				}
		}
		return "redirect:/";
}

}
