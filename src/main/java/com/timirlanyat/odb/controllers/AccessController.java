package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.User;
import com.timirlanyat.odb.model.UserDto;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@Controller
public class AccessController {


    @Autowired
    private UserService service;

    private User createUserAccount(UserDto accountDto) {
        User registered = null;
        try {
            registered = service.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }

    @RequestMapping(value="/registration", method = {RequestMethod.POST})
    public ModelAndView check(@ModelAttribute("newUser") @Valid UserDto dto,
                              BindingResult result, WebRequest request, Errors errs)
    {

        Map<String,Object> model= new HashMap<>();
        Map<String,String> errors= new HashMap<>();

        model.put("errors",errors);


        if(errs.hasErrors()){
            for(FieldError err:errs.getFieldErrors())
                ((Map<String,String>)model.get("errors"))
                        .put(err.getField(),err.getDefaultMessage());

            for(ObjectError err:errs.getAllErrors())
                if(!((Map<String,String>)model.get("errors")).values()
                        .contains(err.getDefaultMessage()))
                    ((Map<String,String>)model.get("errors"))
                            .put("passwordNotMatchedErr",err.getDefaultMessage());

            model.put("userNew",dto);
            return new ModelAndView("registration", model);
        }


        User registered = null;

        registered = createUserAccount(dto);


        if (registered == null) {
            ((Map<String,String>)model.get("errors"))
                    .put("emailError","Email already exists!");
        }
        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("newUser",dto);
            return new ModelAndView("registration", model);
        }
        else {
            return new ModelAndView("redirect:/", "", null);
        }
    }

    @RequestMapping(value="/registration", method = {RequestMethod.GET})
    public ModelAndView registration() {

        UserDto user= new UserDto();
        Map<String, Object> model = new HashMap<>();
        model.put("newUser",user);
        model.put("notLogined",true);

        return new ModelAndView("registration",model);
    }


    @RequestMapping(value="/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(HttpServletRequest request, Principal principal) {

        if(principal != null){
            String referrer = request.getHeader("Referer");
            String path = referrer.substring(referrer.lastIndexOf("/"),referrer.length());
            return new ModelAndView("redirect:"+path,new HashMap<>());
        }

        return new ModelAndView("login","notLogined",true);
    }
}
