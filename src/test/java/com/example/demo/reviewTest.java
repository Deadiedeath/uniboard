package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.replyDto;
import com.example.demo.dto.userDto;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.UserService;

import lombok.extern.log4j.Log4j2;


@SpringBootTest
@Log4j2
public class reviewTest {
	
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardService boardService;
//	@Test
//	public void connectiontest(){
//		
//		try {
//			Connection con = dataSource.getConnection();
//			log.info(con.toString());
//			log.info("성공");
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//	@Test
//	public void insert() {
//		User user = User.builder()
//				.id("assd")
//				.pwd("1234")
//				.build();
//		log.info(user);
//		userRepository.save(user);
//	}
//	
//	@Test
//	public void search() {
//		log.info("검색결과는?");
//		log.info(userRepository.findById("assd"));
//	}
//	
//	@Test
//	public void checkid() {
//		boolean check = userService.checkid("assd");
//		log.info("아이디 확인결과");
//		log.info(check);
//	}
//	
//	@Test
//	public void checkpwd() {
//		userDto dto = userDto.builder()
//				.id("assd")
//				.pwd("123")
//				.build();
//		boolean check = userService.checkuser(dto);
//		log.info("아이디 비밀번호 확인 결과");
//		log.info(check);
//	}
//
	@Test
	public void boardinsert() {
		for(int i = 0; i < 80; i++ ) {
			Board board = Board.builder()
					.id("유저"+i)
					.title("제목..."+i)
					.content("내용...."+i)
					.build();
			boardRepository.save(board);
		}
	}
//	
//	@Test
//	public void boardfind() {
//		List<Board> board = boardRepository.findAll();
//		for (Board i : board) {
//			log.info(i.getTitle());
//		}
//	}
//	
//	@Test
//	public void boardinsert() {
//		
//			Board board = Board.builder()
//					.id("유저")
//					.title("제목...")
//					.content("내용....")
//					.build();
//			log.info(boardRepository.save(board));
//			
//	}
//
//	@Test
//	public void deletetest() {
//		boardRepository.deleteById(1);
//	}
//	
//	@Test
//	public void leveltest() {
//		List<User> user = userRepository.findAll();
//	}
//	
//	@Test
//	public void boardtest() {
//		Pageable pageable = PageRequest.of(0,10, Sort.by("num").descending());
//		Page<Board> result = boardRepository.searchBoard(pageable);
//		log.info(result.getContent());
//				}
//	
//	@Test
//	public void passwordEncodetest() {
//		String rawpass = "123";
//		
//		String encodingpass = passwordEncoder.encode(rawpass);
//		
//		assertAll(
//	            () -> assertNotEquals(rawpass, encodingpass),
//	            () -> assertTrue(passwordEncoder.matches(rawpass, encodingpass))
//	      );
//	}
//	
//	@Test
//	@DisplayName("테스트용")
//	public void postingdata() {
//		boardDto dto = boardDto.builder()
//				.num(50)
//				.id("테스트용")
//				.title("테스트")
//				.content("테스트")
//				.msg("테스트")
//				.build();
//		log.info(modelMapper.map(dto, Board.class));
//	}
//	
//	@Test
//	public void dtotest() {
//		boardDto dto = boardService.findbynum(5);
//		log.info(dto);
//	}
//	
//	@Test
//	public void dtosize() {
//		userDto dto = userDto.builder()
//				.id("1")
//				.pwd("12")
//				.level(0).build();
//		log.info(dto);
//	}
//	
//	@Test
//	public void replyCount() {
//		log.info(boardService.replyCount());
//		
//	}
//	
}
