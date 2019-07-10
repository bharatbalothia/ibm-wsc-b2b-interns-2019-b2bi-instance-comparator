package com.instance.comparator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.instance.comparator.model.FormSubmit;
import com.instance.comparator.model.RESTCalls;
import com.instance.comparator.model.TPRESTCalls;

@Controller
public class InstanceController{
	@RequestMapping(value = {"/formsubmit", "/", ""}, method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("form", "formsubmit", new FormSubmit());
	}
	
	@RequestMapping(value = {"/tradingpartners"}, method = RequestMethod.GET)
	public String showTP() {
		return "TPvalues";
	}
	
	@RequestMapping(value = {"/businessprocess"}, method = RequestMethod.GET)
	public String showBP() {
		return "values";
	}
	@RequestMapping(value = {"/form", "/form/"}, method = RequestMethod.POST)
    public String submit(@Validated @ModelAttribute("formsubmit")FormSubmit form, 
      BindingResult result, ModelMap model ) throws InterruptedException {
        if (result.hasErrors()) {
            return "error";
        }
        model.addAttribute("UserName", form.getUserName());
        model.addAttribute("Password", form.getPassword());
        model.addAttribute("HostName", form.getHostName());
        model.addAttribute("UserName2", form.getUserName2());
        model.addAttribute("Password2", form.getPassword2());
        model.addAttribute("HostName2", form.getHostName2());
        
        
        RESTCalls.Data((model.get("UserName")).toString(), (model.get("Password")).toString(),(model.get("HostName")).toString(),
        		(model.get("UserName2")).toString(), (model.get("Password2")).toString(),(model.get("HostName2")).toString());
        TPRESTCalls.Data((model.get("UserName")).toString(), (model.get("Password")).toString(),(model.get("HostName")).toString(),
        		(model.get("UserName2")).toString(), (model.get("Password2")).toString(),(model.get("HostName2")).toString());
        return "values";
    }
	
	
//	
//	@RequestMapping(value = {"/formsubmit2"}, method = RequestMethod.GET)
//	public ModelAndView showForm2() {
//		return new ModelAndView("Form2", "formsubmit2", new FormSubmit());
//	}
//	
//	@RequestMapping(value = {"/form2", "/form2/"}, method = RequestMethod.POST)
//    public String submit2(@Validated @ModelAttribute("formsubmit2")FormSubmit form, 
//      BindingResult result, ModelMap model ) throws InterruptedException {
//        if (result.hasErrors()) {
//            return "error";
//        }
//        model.addAttribute("UserName", form.getUserName());
//        model.addAttribute("Password", form.getPassword());
//        model.addAttribute("HostName", form.getHostName());
//        model.addAttribute("UserName2", form.getUserName2());
//        model.addAttribute("Password2", form.getPassword2());
//        model.addAttribute("HostName2", form.getHostName2());
//        
//        System.out.println("second form processing");
//        TPRESTCalls.Data((model.get("UserName")).toString(), (model.get("Password")).toString(),(model.get("HostName")).toString(),
//        		(model.get("UserName2")).toString(), (model.get("Password2")).toString(),(model.get("HostName2")).toString());
//        return "TPvalues";
//    }
}



