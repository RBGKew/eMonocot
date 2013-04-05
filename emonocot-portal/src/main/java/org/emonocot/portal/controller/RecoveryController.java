package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.portal.controller.form.RecoveryForm;
import org.emonocot.portal.controller.form.ResetForm;
import org.emonocot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping(value = "/recovery")
public class RecoveryController {
	
	private UserService userService;
	
	private EmailService emailService;
	
	private String baseUrl;
	
	@Autowired
	public void setUserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
   
   @RequestMapping(method = RequestMethod.GET)
   public String show(Model model) {
	   model.addAttribute(new RecoveryForm());
	   return "recovery/show";
   }
   
   @RequestMapping(method = RequestMethod.POST)
   public String reset(@Valid @ModelAttribute RecoveryForm recoveryForm,
		   BindingResult result, RedirectAttributes redirectAttributes) {
	   if (result.hasErrors()) {
           return "recovery/show";
       }
	   String nonce = userService.createNonce(recoveryForm.getUsername());
	   
	   Map<String,String> model = new HashMap<String,String>();
	   model.put("nonce", nonce);
	   model.put("baseUrl", baseUrl);
	   emailService.sendEmail("org/emonocot/portal/controller/ResetPasswordRequest.vm", model, recoveryForm.getUsername(), "Password Reset Request");
	   String[] codes = new String[] { "reset.email.sent" };
	   Object[] args = new Object[] {  };
	   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
	   redirectAttributes.addFlashAttribute("info", message);

       return "redirect:/login";
   }
   
   @RequestMapping(value = "/{nonce}", method = RequestMethod.GET) 
   public String verifyPage(@PathVariable("nonce") String nonce, Model model) {
	   ResetForm resetForm = new ResetForm();
	   model.addAttribute(resetForm);
	   return "recovery/verify";
   }
   
   @RequestMapping(value = "/{nonce}", method = RequestMethod.POST) 
   public String verify(@PathVariable("nonce") String nonce,
		   @Valid @ModelAttribute ResetForm resetForm,
		   Model model,
		   BindingResult result, RedirectAttributes redirectAttributes) {
	   if (result.hasErrors()) {
           return "recovery/verify";
       }
	   if(userService.verifyNonce(resetForm.getUsername(), nonce)) {
		   userService.changePasswordForUser(resetForm.getUsername(),resetForm.getPassword());
		   String[] codes = new String[] { "password.recovery.successful" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   redirectAttributes.addFlashAttribute("info",message);
		   return "redirect:/login";
	   } else {
		   String[] codes = new String[] { "password.recovery.failed" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   model.addAttribute("error",message);
		   return "recovery/verify";
	   }
	   
	   
   }

}