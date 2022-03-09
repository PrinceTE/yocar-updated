package com.yocar.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yocar.base.dto.CarDetails;
import com.yocar.base.dto.Response;
import com.yocar.base.model.AuthenticateRequest;
import com.yocar.base.model.AuthenticationResponse;
import com.yocar.base.service.AdminServiceImpl;
import com.yocar.base.service.CarService;
import com.yocar.base.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
//@RequestMapping("/")
@CrossOrigin(allowedHeaders = "*", origins = "*")      
@Slf4j
public class YoCarController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CarService carService;
	@Autowired
	private AdminServiceImpl adminService;

	@PostMapping("/authenticate")
	public Response createAuthenticationToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception {
		Response response = new Response();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticateRequest.getAdminName(), authenticateRequest.getPassword()));
		} catch (AuthenticationException e) {
			throw new Exception("invalid id and password", e);
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateRequest.getAdminName());
		String jwt = jwtUtil.generateToken(userDetails);

		response.setStatusCode(200);
		response.setMsg("Success");
		response.setMsg("jwt token with role authority");
		response.setAdminId(adminService.getAdminId(userDetails.getUsername()));
		log.warn("===> " + userDetails);

		response.setToken(jwt);

		
		return response;

	}

	@GetMapping("/getAllCars")
	public Response getAllCars() {
		Response response = new Response();
		List<CarDetails> details = carService.getAllCar();
		if (details != null) {
			response.setStatusCode(200);
			response.setMsg("Success");
			response.setDiscription("Here are the details");
			response.setAllCars(details);

		} else {
			response.setStatusCode(400);
			response.setMsg("Failure");
			response.setDiscription("No car found");
		}
		return response;

	}

	@GetMapping("/getCarById")
	public Response getCarsById(int id) {
		Response response = new Response();
		CarDetails details = carService.getCarById(id);
		if (details != null) {
			response.setStatusCode(200);
			response.setMsg("Success");
			response.setDiscription("Here are the details");
			response.setCarDetails(details);
		} else {
			response.setStatusCode(400);
			response.setMsg("Failure");
			response.setDiscription("No car found");
		}
		return response;

	}

	@PostMapping("/addCar")
	public Response addCar(@RequestBody CarDetails carDetails) {
		Response response = new Response();
		if (carService.addCar(carDetails)) {
			response.setMsg("Succsess");
			response.setStatusCode(200);
			response.setDiscription("Data added succesfully");
		} else {
			response.setMsg("Failure");
			response.setStatusCode(400);
			response.setDiscription("Something went wrong");
		}
		return response;

	}

	@DeleteMapping("/deleteCar/{carId}")
	public Response deleteCar(@PathVariable int carId) {
		Response response = new Response();
		if (carService.deleteCar(carId)) {
			response.setMsg("Succsess");
			response.setStatusCode(200);
			response.setDiscription("Data deleted added succesfully");
		} else {
			response.setMsg("Failure");
			response.setStatusCode(400);
			response.setDiscription("Something went wrong");
		}
		return response;

	}

	@PostMapping("/updateCar")
	public Response updateCar(@RequestBody CarDetails carDetails) {
		Response response = new Response();
		if (carService.updateCar(carDetails)) {
			response.setMsg("Succsess");
			response.setStatusCode(200);
			response.setDiscription("Data updated succesfully");
		} else {
			response.setMsg("Failure");
			response.setStatusCode(400);
			response.setDiscription("Something went wrong");
		}
		return response;

	}

}
