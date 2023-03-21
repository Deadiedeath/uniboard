package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.userDto;
import com.example.demo.dto.userinfo;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	//아이디 확인 
	@Override
	public boolean checkid(String id) {
		
		return userRepository.existsById(id);
	}

	//로그인을 하기 위해서 아이디를 확인 후 비밀번호를 확인 
	@Override
	public userinfo checkuser(String id, String pwd) {
		
		if(userRepository.existsById(id)) {
			
			Optional<User> udto = userRepository.findById(id);
			
			if(passwordEncoder.matches(pwd, udto.get().getPwd())) {
				userinfo dto = userinfo.builder()
						.id(udto.get().getId())
						.level(udto.get().getLevel())
						.check(true)
						.build();
				return dto; //아이디 및 비밀번호 확인 존재하면 로그인 
			}
			else {
				userinfo dto = userinfo.builder()
						.check(false)
						.build();
				return dto; // 비밀번호가 틀렸을 경우
			}
			
		}
		else {
			userinfo dto = userinfo.builder()
					.check(false)
					.build();
			return dto; // 아이디가 틀렸을 경우
		}
	}

	@Override
	public String joinuser(@Valid userDto dto) {
		User user = modelMapper.map(dto, User.class);
		userRepository.save(user);
		return "success";

	}

	@Override
	public List<User> findbylevel(int level) {
		List<User> user = userRepository.findBylevel(level);
		return user;
	}
	
	

	@Override
	public void promoteN(String id, int level) {
		Optional<User> result = userRepository.findById(id);
		User user = modelMapper.map(result, User.class);
		user.promote(level);
		userRepository.save(user);
		
	}

	@Override
	public List<User> findbyAll() {
		List<User> user = userRepository.findAll();
		return user;
	}

	@Override
	public void deleteUser(String id) {
		userRepository.deleteById(id);
		
	}

	@Override
	public User findbyid(String id) {
		Optional<User> result = userRepository.findById(id);
		User user = modelMapper.map(result, User.class);
		return user;
	}

	@Override
	public pageResponseDTO<userDto> list(pageRequestDTO pageRequestDTO) {
		Pageable pageable = pageRequestDTO.getPageable("id");
		String type = pageRequestDTO.getType();
		int level;
		
		if(type == null) {
			level = 3;
		}
		else {
			level = Integer.parseInt(type);
		}
		
		String keyword = pageRequestDTO.getKeyword();
		System.out.println(level + keyword);
		
		Page<User> result = userRepository.searchUser(pageable, level, keyword);
		
		List<userDto> dtoList = result.getContent().stream()
				.map(user -> modelMapper.map(user, userDto.class)).collect(Collectors.toList());
		
		return pageResponseDTO.<userDto>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int) result.getTotalElements())
				.build();
	}


	
}
