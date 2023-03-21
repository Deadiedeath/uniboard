package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.userDto;
import com.example.demo.dto.userinfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/manager")
@Log4j2
@RequiredArgsConstructor
public class ManagerController {

	private final UserService userService;
	
	@GetMapping("/manager")
	public String main(Model model, HttpServletResponse response, 
			HttpServletRequest request, pageRequestDTO pageRequestDTO) {
		log.info("관리자 페이지");	
		log.info(pageRequestDTO);
		HttpSession session = request.getSession(false);
		
		
		if(session == null) { //세션값이 없을 경우 로그인 페이지로 이동
			return "redirect:/user/login";
		}
		userinfo info = (userinfo) session.getAttribute("loginfo");
		int level = info.getLevel();
		if(level != 2) { //권한 레벨이 2가 아니면 게시판 페이지로 이동 
			return "redirect:/board/list";
		}
//		int type = 3; //초기값 저장
//		if(level != null) { //전달받은 level 값이 null이 아닐경우 int 타입으로 변경
//			type = Integer.parseInt(level);
//		}		
		
//		if(level !=null) { // 검색을 했을 경우
//			
//			if(type == 3) //검색 조건으로 전체 선택 했을시 3으로 입력 되어 전체 검색 시작
//			{
////				List<User> user = userService.findbyAll();
//				
//				pageResponseDTO<userDto> user = userService.list(pageRequestDTO, type);
//				model.addAttribute("user", user);
//				return "/manager/manager";
//			}
//			else { // 검색 조건 0 1 2 일 경우 조건 검색 시작
////				List<User> user = userService.findbylevel(type);
//				pageResponseDTO<userDto> user = userService.list(pageRequestDTO, type);
//				model.addAttribute("user", user);
//				return "/manager/manager";
//			}			
//		}		
//		else { //초기 level 전달 값을 null로 받았을 경우
////			List<User> user = userService.findbyAll();
//			pageResponseDTO<userDto> user = userService.list(pageRequestDTO, type);
//			model.addAttribute("user", user);
//			return "/manager/manager";
//		}
		
		pageResponseDTO<userDto> user = userService.list(pageRequestDTO);
		
		model.addAttribute("user", user);
		String link = pageRequestDTO.getLink();
		return "/manager/manager";
				
	}
	
	@PostMapping("/promote") //신규유저를 일반 유저로 바꿈
	public String promote(@RequestParam(required = false) List<String> userId, HttpServletResponse response, HttpServletRequest request, RedirectAttributes rett
			, pageRequestDTO pageRequestDTO) {
		
		String link = pageRequestDTO.getLink();
		if(userId == null) {//선택된 사람이 없는 경우 
			log.info("0명 선택");
			rett.addFlashAttribute("msg", "선택된 사람이 없습니다");
			return "redirect:/manager/manager?"+link;
		}
		
		for(String id : userId) { //선택한 유저를 일반유저인 level 1로 update
				userService.promoteN(id, 1);
			}
		rett.addFlashAttribute("msg", userId.size()+"명을 일반 유저로 바꾸었습니다");
		return "redirect:/manager/manager?"+link;
		
		
	}
	
	@PostMapping("/promoteM") //유저를 관리자로 바꿈
	public String promoteM(@RequestParam(required = false) List<String> userId, Model model, HttpServletResponse response, HttpServletRequest request, RedirectAttributes rett, 
			pageRequestDTO pageRequestDTO) {
		
		String link = pageRequestDTO.getLink();
		
		if(userId == null) {//선택된 사람이 없는 경우
			log.info("0명 선택");
			rett.addFlashAttribute("msg", "선택된 사람이 없습니다");
			return "redirect:/manager/manager?"+link;
		}
		
		List<User> user = userService.findbylevel(2); //db에 저장되어 있는 level 2인 데이터를 조회
		if(user.size()>=3) {//데이터 조회시 3건 이상일 경우
			log.info("관리자가 3명 이상입니다");
			rett.addFlashAttribute("msg", "관리자가 3명 이상입니다");
			return "redirect:/manager/manager?"+link;
		}
		else { //데이터가 3건 미만일 경우
			for(String id : userId) {
				if(userService.findbyid(id).getLevel()==1) { //지정된 유저가 level 1일 경우 
					userService.promoteN(id, 2);
					rett.addFlashAttribute("msg", "관리자가 지정되었습니다");
					return "redirect:/manager/manager?"+link;
				}
				
			}
			rett.addFlashAttribute("msg", "신규유저를 관리자로 지정할수 없습니다"); // level 2인데이터가 3건 이하이지만 level이 1인 경우
			return "redirect:/manager/manager?"+link;
		}
	}
	
	@PostMapping("/kickout") //탈퇴 시키기
	public String kickout(@RequestParam(required = false) List<String> userId, String kick, Model model, HttpServletResponse response, HttpServletRequest request, RedirectAttributes rett,
			pageRequestDTO pageRequestDTO) {

		String link = pageRequestDTO.getLink();
		log.info(link);
		
		if(userId == null) {//선택된 사람이 없는 경우
			log.info("0명 선택");
			rett.addFlashAttribute("msg", "선택된 사람이 없습니다");
			return "redirect:/manager/manager?"+link;
		}
		
		if(kick.equals("false")) { //전달받은 kick의 값이 false인 경우 다시 관리자 페이지로
			return "redirect:/manager/manager?"+link;
		}
		else {//전달받은 kick의 값이 true인 경우
			for(String id : userId) { 
				userService.deleteUser(id);
			}
			rett.addFlashAttribute("msg", userId.size() + "명의 회원을 탈퇴 시켰습니다");
			return "redirect:/manager/manager?"+link;
		}
		
		
	}
	
}
