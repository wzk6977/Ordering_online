package com.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.po.Food;
import com.po.Form;
import com.po.Manager;
import com.po.Table;
import com.service.Manager_service;
import com.service.User_service;


@Controller
public class Manager_controller
{
	@Autowired
	private Manager_service manager_service;
	@Autowired
	private User_service user_service;

	@RequestMapping("/login")
	public String manager_login(Model model,Manager manager,HttpSession session)
	{
		System.out.println("controller");
		Manager manager1 = manager_service.login(manager);
			
		List<Food> food_list=manager_service.get_foodInfo();
		model.addAttribute("menuList", food_list);
		
		if (manager1 != null) {	
			
			session.setAttribute("LOGIN_USER", manager);
			System.out.println("session.setAttribute");
			
			return "manage_menu";
		} else {
			return "redirect:/manager_login.jsp";
		}
	}
	
	@RequestMapping("/turn_manage_menu")
	public String turn_manage_menu(Model model)
	{
		
		List<Food> food_list=manager_service.get_foodInfo();
		model.addAttribute("menuList", food_list);
		
		return "manage_menu";
	}
	
	@RequestMapping("/turn_manage_table")
	public String turn_manage_table(Model model)
	{
		List<Table> table_list=manager_service.get_tableInfo();
		model.addAttribute("table_list", table_list);
		return  "manage_table";
	}
	
	@RequestMapping("/turn_manage_form")
	public String turn_manage_form(Model model)
	{
		List<Form> form_list1=manager_service.get_FormInfo();
		Map<String,Form> map = new TreeMap<String, Form>();
		
		for (Form form : form_list1) {
			
			Form form1 = map.get(form.getOrder_id());
			if(form1 != null){
				Food food = new Food();
				food.setFood_id(form.getFood_id());
				food.setFood_name(form.getFood_name());
				food.setFood_amount(form.getAmount());
				form1.getList().add(food);
			}else{
				form1 = new Form();
				form1.setOrder_id(form.getOrder_id());
				form1.setPeople_number(form.getPeople_number());
				form1.setTable_id(form.getTable_id());
				form1.setEvaluate(form.getEvaluate());
				form1.setIs_accounted(form.getIs_accounted());
				form1.setTime(form.getTime());
				form1.setSum(form.getSum());
				Food food = new Food();
				food.setFood_id(form.getFood_id());
				food.setFood_name(form.getFood_name());
				food.setFood_amount(form.getAmount());
				List<Food> food_list = new ArrayList<Food>();
				food_list.add(food);
				form1.setList(food_list);
			}
			map.put(form.getOrder_id(), form1);
		}
		
		model.addAttribute("form_list", map);
		return "manage_form";
	}
	
	
	
	@RequestMapping("/delete_food")
	public String delete_food(Model model, String food_id)
	{
		manager_service.delete_food(food_id);
		return turn_manage_menu(model);
	}
	
	@RequestMapping("/add_food")
	public String add_food(Model model, Food food)
	{
		manager_service.add_food(food);
		return turn_manage_menu(model);
	}
	
	@RequestMapping("/update_food")
	public String update_food(Model model, Food food)
	{
		manager_service.update_food(food);
		return turn_manage_menu(model);
	}
	
	@RequestMapping("/delete_table")
	public String delete_table(Model model, String table_id)
	{
		manager_service.delete_table(table_id);
		return turn_manage_table(model);
	}
	
	@RequestMapping("/add_table")
	public String add_table(Model model, String table_id)
	{
		manager_service.add_table(table_id);
		return turn_manage_table(model);
	}
	
	@RequestMapping("/delete_form")
	public String delete_forms(Model model, String order_id, String table_id)
	{
		manager_service.delete_form_food(order_id);
		manager_service.delete_form(order_id);
		user_service.change_table_free(table_id);
		
		return turn_manage_form(model);
	}
	
	
}