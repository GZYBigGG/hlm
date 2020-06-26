package com.web;

import java.util.FormatFlagsConversionMismatchException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dao.ToNeo4j;

@Controller

public class HlmController {
	@Autowired
	private ToNeo4j tn;
	public static boolean flag=false;
	
	@RequestMapping("/lovess")
	public String show() {
	
		return "show";
	}
	@RequestMapping("/find")
	public String showResult(Model model,HttpServletRequest request) {
		String name1 = request.getParameter("name1");
		String name2 = request.getParameter("name2");
		model.addAttribute("name1", name1);
		model.addAttribute("name2", name2);	
		return "show";
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/node")
	@ResponseBody
	public Map reload(String name1,String name2) {
		Map sMap;
		if (flag == false) {
		sMap = tn.toMap(name1,name2);
		}else {
			sMap = tn.toAllMap();
			flag = false;
		}
	
		
		return sMap;

	}
	@RequestMapping("/all")
	public String showAllResult() {
		flag = true;
		return "show";
	}
}
