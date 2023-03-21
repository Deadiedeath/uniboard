package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.replyDto;
import com.example.demo.dto.userDto;
import com.example.demo.dto.userinfo;
import com.example.demo.service.BoardService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	
	@GetMapping("/list")//게시판 초기 화면
	public String mainpage(Model model, HttpServletResponse response, HttpServletRequest request, pageRequestDTO pageRequestDTO) {
		
		HttpSession session = request.getSession(false);
		
		if(session!=null) { //세션에 로그인 정보가 있으면 실행
			log.info("게시판 실행");
			
			pageResponseDTO<boardDto> board = boardService.list(pageRequestDTO); //페이징 게시물 받아오기
			model.addAttribute("board", board);
			
			List<replyDto> count = boardService.replyCount();//댓글 갯수 받아오기
			model.addAttribute("count", count);
			
			return "/board/list";
		}
		
		else { //세션에 로그인 정보가 없으면 로그인 화면으로 넘어간다
			log.info("로그인이 필요합니다");
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/read")//게시물 확인 
	public String read(int num, Model model, HttpServletResponse response, HttpServletRequest request, pageRequestDTO pageRequestDTO) {
		HttpSession session = request.getSession(false);
		if(session != null) { //세션값이 있을 경우
			log.info(num+" 게시물 확인");
			boardDto dto = boardService.findbynum(num);
			
			model.addAttribute("dto", dto);
			model.addAttribute("pageRequestDTO", pageRequestDTO);
			return "/board/read";
		}
		else return "redirect:/user/login";
		
	}
	
	@GetMapping("/register")//작성페이지로 이동
	public String registerpage(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) { //세션 값이 있는 경우
			log.info("게시물 작성 페이지로 이동");					
			return "/board/register";
		}
		else return "redirect:/user/login";
	}
	
	@PostMapping("/register")//작성하기 버튼을 누르면 저장
	public String registing(Model model, String title, String content, HttpServletResponse response, HttpServletRequest request, RedirectAttributes rett) {
		log.info("게시물 저장 진행");
		HttpSession session = request.getSession(false);
		userinfo info = (userinfo) session.getAttribute("loginfo");
		String id = info.getId();
		
		if(title =="" || content=="") { //전달받은 string, title의 값이 공란인 경우
			log.info("제목 혹은 내용이 없습니다");
			rett.addFlashAttribute("msg", "제목 혹은 내용이 없습니다");
			rett.addFlashAttribute("title", title);
			rett.addFlashAttribute("content", content);
			return "redirect:/board/register"; //다시 작성페이지로 상태 메세지와 함께 이동
		}
		
		if(id!=null) { //세션에서 받은 로그인 정보중 id 값이 null이 아니면 정상적으로 진행
		boardDto dto = boardDto.builder()
				.id(id) 
				.title(title)
				.content(content)
				.build();
		boardDto refdto = boardService.posting(dto);
		String message = refdto.getMsg();
		
			if(message == "sucess") {
				log.info(dto.getTitle()+" 게시물 작성 완료");
				return "redirect:/board/read?num="+refdto.getNum();
			}
			else {
				log.info("작성 실패");
				return "/board/register";
			}
			
		}
		else return "redirect:/user/login"; //id 정보가 null이면 로그인 페이지로 이동
	}
	
	@GetMapping("/delete") // 게시물 삭제
	@ResponseBody
	public String delete(int num, HttpServletResponse response, HttpServletRequest request) {
		log.info("게시물 삭제 진행");
		HttpSession session = request.getSession(false);
		
		userinfo info = (userinfo) session.getAttribute("loginfo");
		String id = info.getId();
		int level = info.getLevel();
		boardDto dto = boardService.findbynum(num);
//		log.info(id + "   " + dto.getId());
		if(id.equals(dto.getId())) { //세션에 저장되어있는 회원 정보와 게시물 정보상의 id 값이 같으면 삭제 진행
			log.info("아이디 확인후 삭제");
			String message = boardService.deletepost(num);
			log.info(message);
			return "success";
		}
		else if(level == 2) { //사용자 권한이 2이면 어떤 게시물이든 삭제 가능
			String message = boardService.deletepost(num);
			log.info(message);
			return "success";
		}
		else {
			log.info("삭제 실패");
			return "fail"; 
		}
			
	}
	
	@GetMapping("/modify") 
	public String modify(Model model, int num, HttpServletResponse response, HttpServletRequest request) {
		log.info("게시물 수정 데이터 로딩");
		HttpSession session = request.getSession(false);
		userinfo info = (userinfo) session.getAttribute("loginfo");
		String id = info.getId();
		if(session != null) {//세션 값이 있는 경우
			boardDto dto = boardService.findbynum(num);
			if(id.equals(dto.getId())) {//세션에 저장되어 있는 id 값과 게시물 번호로 찾은 데이터 상의 id 값이 같을 경우 
				model.addAttribute("dto", dto);
				return "/board/modify";
			}
			else return "redirect:/board/read?num="+num; //id 값이 다른 경우
		}
		else return "redirect:/user/login"; //세션값이 없는 경우
	}
	
	@PostMapping("/update") //게시물 수정
	@ResponseBody
	public String update(int num, String title, String content, Model model, HttpServletResponse response, HttpServletRequest request)
	throws Exception{
	
		log.info("게시물 수정");
		HttpSession session = request.getSession(false);
		if(session != null) { //세션 값이 있는 경우
			
			userinfo info = (userinfo) session.getAttribute("loginfo");
			String id = info.getId();
			boardDto dto = boardDto.builder()
					.id(id)
					.title(title)
					.content(content)
					.num(num)
					.build();
			String message = boardService.updatepost(dto);
			log.info(message);
			return "success";//제대로 수정 했을 경우 success 리턴
		}
		return "redirect:/user/login"; //세션 값이 없는경우
		
	}
	
	@PostMapping("/addReply") //댓글 추가하기
	public String addReply(int num, String id, String content, Model model, HttpServletResponse response, HttpServletRequest request, int page) {
		log.info("코멘트 추가");
		log.info(page);
		replyDto dto = replyDto.builder()
				.id(id)
				.num(num)
				.content(content)
				.build();
		boardService.addReply(dto);
		return "redirect:/board/read?num="+dto.getNum();
	}
	
	@PostMapping("/deleteReply") //댓글 삭제 하기
	public String deleteReply(int rnum, HttpServletResponse response, HttpServletRequest request, RedirectAttributes rett) {
		log.info("코멘트 삭제");
		HttpSession session = request.getSession(false);
		userinfo info = (userinfo) session.getAttribute("loginfo");
		String id = info.getId();
		Reply reply = boardService.findbyrnum(rnum);
		
		if(reply.getId().equals(id)) { //작성자와 사용자 id가 같을 경우
			String msg = "사용자가 삭제하였습니다";
			boardService.deleteReply(rnum, msg);
			log.info("댓글 삭제 완료");
			return "redirect:/board/read?num="+reply.getNum();
		}
		
		else if(info.getLevel() == 2) { //사용자 level이 2인경우 
			String msg = "관리자가 삭제하였습니다";
			boardService.deleteReply(rnum, msg);
			log.info("댓글 삭제 완료");
			return "redirect:/board/read?num="+reply.getNum();
		}
		
		else {			//조건에 만족하지 않을 경우
			log.info("댓글 삭제 실패");
			return "redirect:/board/read?num="+reply.getNum();
		}
		
		
	}
}
