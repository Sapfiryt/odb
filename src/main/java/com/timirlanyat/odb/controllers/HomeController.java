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
import java.util.*;

@Controller
public class HomeController {


    @RequestMapping(value={"/","/home"}, method = RequestMethod.GET)
    public String baseMaV() {

        return "home";
    }




    @RequestMapping(value="/hello", method = RequestMethod.GET)
    public String hello() {

        return "hello";
    }



}
