package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.replyDto;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

	private final BoardRepository boardRepository;
	private final ModelMapper modelMapper;
	private final ReplyRepository replyRepository;
	
	@Override
	public List<Board> findall() {
		List<Board> board = boardRepository.findAll();
		
		return board;
	}


	@Override
	public boardDto findbynum(int num) {
		Optional<Board> board = boardRepository.findById(num);
		boardDto dto = modelMapper.map(board, boardDto.class);
		
		List<Reply> reply = replyRepository.findByNum(num);
		List<replyDto> rdto = reply.stream().map(Reply -> modelMapper.map(Reply, replyDto.class))
				.collect(Collectors.toList());
//		log.info(rdto);
		dto.addreply(rdto);
//		log.info(dto);
		return dto;
	}


	@Override
	public boardDto posting(boardDto dto) {
		Board board = modelMapper.map(dto, Board.class);
		try {
			Board ref = boardRepository.save(board);
			boardDto sdto = boardDto.builder()
					.num(ref.getNum())
					.msg("sucess")
					.build();
			return sdto;
		} catch (Exception e) {
			e.printStackTrace();
			boardDto sdto = boardDto.builder()
					.msg("fail")
					.build();
			return sdto;
		}
		
	}


	@Override
	public String deletepost(int num) {
		boardRepository.deleteById(num);
		return "sucess";
	}


	@Override
	public String updatepost(boardDto dto) {
		Optional<Board> result = boardRepository.findById(dto.getNum());
		Board board = modelMapper.map(result, Board.class);
		board.change(dto.getTitle(), dto.getContent());
		boardRepository.save(board);
		return "sucess";
	}


	@Override
	public pageResponseDTO<boardDto> list(pageRequestDTO pageRequestDTO) {
		
		Pageable pageable = pageRequestDTO.getPageable("num");
		String keyword = pageRequestDTO.getKeyword();
		String type = pageRequestDTO.getType();
		Page<Board> result = boardRepository.searchBoard(pageable, type, keyword);
		
		List<boardDto> dtoList = result.getContent().stream()
				.map(board -> modelMapper.map(board, boardDto.class)).collect(Collectors.toList());
		
		return pageResponseDTO.<boardDto>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int) result.getTotalElements())
				.build();
	}


	@Override
	public void addReply(replyDto dto) {
		Reply reply = modelMapper.map(dto, Reply.class);
		replyRepository.save(reply);
		
	}
	
	@Override
	public void deleteReply(int rnum, String msg) {	
		Optional<Reply> rep = replyRepository.findById(rnum);
		Reply reply = modelMapper.map(rep, Reply.class);
		reply.delete(msg);
		replyRepository.save(reply);
	}


	@Override
	public Reply findbyrnum(int rnum) {
		Optional<Reply> rep = replyRepository.findById(rnum);
		Reply reply = modelMapper.map(rep, Reply.class);
		return reply;
	}


	@Override
	public List<replyDto> replyCount() {
		List<replyDto> count = replyRepository.replyCount();
		
		return count;
	}

}
