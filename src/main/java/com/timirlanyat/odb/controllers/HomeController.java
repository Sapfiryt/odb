package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.*;
import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/","/home"}, method = RequestMethod.GET)
    public ModelAndView baseMaV(Principal principal) {



        Map<String,Object> model= null;

        if(principal == null) {
            model = new HashMap<>();
            model.put("notLogined", true);

        }else{
            model=userService.getUserParameters(principal);
        }
        return new ModelAndView("home",model);
    }


}
