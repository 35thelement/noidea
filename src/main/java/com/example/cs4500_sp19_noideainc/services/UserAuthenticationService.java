package com.example.cs4500_sp19_noideainc.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.cs4500_sp19_noideainc.models.User;
import com.example.cs4500_sp19_noideainc.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials="true")
public class UserAuthenticationService {
	@Autowired
    UserRepository userRepository;
	
	@PostMapping("/api/login")
	public User login(@RequestBody User credentials, HttpSession session) {
		//System.out.println(session);
		User findUser = userRepository.findByUserEmail(credentials.getEmail());
		if (findUser == null) {
			return null;
		}
		if (credentials.getPassword().equals(findUser.getPassword())) {
			session.setAttribute("currentUser", findUser);
			return findUser;
		} else {
			return null;
		}
	}
	

	@PostMapping("/api/register")
    public User register(@RequestBody User user, HttpSession session) {    	
    	User findUser = userRepository.findByUserEmail(user.getEmail());
		if (findUser == null) {
			session.setAttribute("currentUser", user);
			return userRepository.save(user);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Email duplicate");
		}
    }
  
  @PostMapping("/api/logout")
	public void logout(HttpSession session) {
		//System.out.println(session);
		session.invalidate();
	}

	@GetMapping("/api/checkLogin")
	public User checkLogin(HttpSession session) {
		//System.out.println(session);
		User currentUser = (User) session.getAttribute("currentUser");
		//System.out.println("-----------------------");
		return currentUser;
	}

}
