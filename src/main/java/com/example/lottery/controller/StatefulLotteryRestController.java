package com.example.lottery.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
@RestController
@SessionScope
@RequestMapping("numbers")
@CrossOrigin
public class StatefulLotteryRestController {
	private static final Logger logger = LoggerFactory.getLogger(StatefulLotteryRestController.class);

	private final List<List<Integer>> numbers = new ArrayList<>();

	// http://localhost:8001/api/v1/lottery/numbers
	@GetMapping
	public List<List<Integer>> getNumbers(HttpSession httpSession) {
		logger.info(String.format("New request is retrieved for the session %s.", httpSession.getId()));
		List<Integer> numbers = ThreadLocalRandom.current().ints(1, 50).distinct().limit(6).sorted().boxed()
				.collect(Collectors.toList());
		this.numbers.add(numbers);
		return this.numbers;
	}
}
