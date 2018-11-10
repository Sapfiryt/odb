package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReconstructionRepository reconstructionRepository;

    @RequestMapping(value={"/account"}, method = RequestMethod.GET)
    public ModelAndView accountView(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        model.put("reconstructions", reconstructionRepository.findByUser((Member)model.get("user")));



        return new ModelAndView("account",model);
    }

}
