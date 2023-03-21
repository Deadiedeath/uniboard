package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.User;
import com.example.demo.dto.userDto;
import com.example.demo.dto.userinfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/user")
@Log4j2
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/login") //로그인 메인 페이지로 
	public String mainpage(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			log.info("로그인 상태입니다");
			return "redirect:/board/list";
		}
		
		else return "/user/login";
	}
	
	@GetMapping("/userjoin") //회원 가입 페이지로
	public String userjoin(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			log.info("로그인 상태입니다");
			return "redirect:/board/list";
		}
		
		else return "/user/userjoin";
	}
	
	@PostMapping("/logincheck") // 아이디 비밀번호 확인 후 로그인 진행
	public String checkuser(Model model, String id, String pwd, RedirectAttributes re, HttpServletResponse response, HttpServletRequest request) {
		log.info("로그인 작업을 시작합니다");
		
		userinfo dto = userService.checkuser(id, pwd);
		if(dto.isCheck()) {
			if(dto.getLevel() !=0) {
				HttpSession session = request.getSession();
				session.setAttribute("loginfo", dto); //세션에 로그인 정보 저장
				log.info("로그인 성공");
				return "redirect:/board/list";
			}
			else {
				log.info("관리자의 승인이 필요합니다");
				model.addAttribute("status", "관리자 승인 필요"); //로그인은 성공했지만 level이 0이어서 초기페이지로
				return "/user/login";
			}
		}
		else {
			log.info("로그인 실패");
			model.addAttribute("status", "로그인 실패"); //로그인시 아이디 혹은 비밀번호가 틀림
			return "/user/login";
		}
	}
	
	@PostMapping("/idcheck")
	@ResponseBody //아이디 중복 검사
	public String idcheck(String memberId) throws Exception{
		log.info("id중복 검사 진행");
		boolean check = userService.checkid(memberId);
		if(check) {
			return "fail"; //중복 아이디가 존재
		} 
		else return "success"; //중복 아이디가 없음
		
	}
	
	@PostMapping("/join") //회원 가입
	@ResponseBody
	public String joinuser(Model model, String pwd, String id, RedirectAttributes re) {
		
		
		if(id.equals("")||id == null) {//아이디가 빈 공란일 경우
			
			return "idEmpty";
		}
		else if(id.contains(" ")) {//아이디에 띄어쓰기가 있는경우
			
			return "idSpace";
		}
		else if(id.length() <3 || id.length() >15) {//아이디 글자수 제한
			
			return "idLength";
		}
		else if(pwd.length() <4 || pwd.length() >15) {//비밀번호 글자수 제한
			
			return "pwdLength";
		}
		else {
			userDto user = userDto.builder()
					.id(id)
					.pwd(passwordEncoder.encode(pwd))
					.build();
			String message = userService.joinuser(user);
			log.info(message);
			
			return "0";
		}
		
	}
	
	@GetMapping("/logout")//로그아웃 진행
	public String logout(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) { //세션이 있으면 로그아웃 진행
			session.invalidate();
			log.info("로그아웃 되었습니다");
		}
		return "redirect:/user/login";
	}
	
	@GetMapping("/out") //회원 탈퇴 페이지로 이동
	public String out(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if(session != null) { //세션이 있으면 회원 탈퇴 페이지로 이동
			log.info("로그인 상태입니다");
			return "/user/out";
		}
		
		else return "redirect:/board/list";
	}
	
	@PostMapping("/out") //회원탈퇴 
	@ResponseBody
	public String userout(String pwd, boolean check, HttpServletResponse response, HttpServletRequest request, RedirectAttributes re) {
		HttpSession session = request.getSession(false);
		userinfo info = (userinfo) session.getAttribute("loginfo");
		String id = info.getId();
		User user = userService.findbyid(id);
		if(check == false) {
			log.info("삭제를 취소합니다");
			return "cancel";
		}
		if(passwordEncoder.matches(pwd, user.getPwd())) { //비밀번호가 맞을 경우
			log.info("회원 탈퇴 진행");
			userService.deleteUser(id); //db에서 회원 정보 삭제
			session.invalidate(); //세션 invalidate 로그아웃 진행
			log.info("로그아웃"); 
								
			return "success";
		}
		else { //비밀번호가 틀렸을 경우 
			log.info("비밀번호가 틀림");
			return "wrongPwd";
		}
	}
	
}
