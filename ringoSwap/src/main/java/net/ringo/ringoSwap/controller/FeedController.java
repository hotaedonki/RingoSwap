package net.ringo.ringoSwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("feed")
public class FeedController 
{
	@GetMapping("feedMain")
	public String feedMain()
	{
		return "feed/feedMain";
	}
}