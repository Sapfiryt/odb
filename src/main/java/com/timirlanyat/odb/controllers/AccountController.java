package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.UserDto;
import com.timirlanyat.odb.model.UserEditDto;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReconstructionRepository reconstructionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping(value={"/account"}, method = RequestMethod.GET)
    public ModelAndView accountView(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        model.put("reconstructions", reconstructionRepository.findByUser((Member)model.get("user")));



        return new ModelAndView("account",model);
    }


    @RequestMapping(value={"/account/edit"}, method = RequestMethod.POST)
    public ModelAndView edit(Principal principal, @ModelAttribute("editUser") @Valid UserEditDto dto) {

        Map<String,Object> model = userService.getUserParameters(principal);

        Member mem = (Member)model.get("user");

        if(dto.getFirstName()!=null&&!dto.getFirstName().equals(""))
            mem.setFirstName(dto.getFirstName());

        if(dto.getLastName()!=null&&!dto.getLastName().equals(""))
            mem.setLastName(dto.getLastName());

        memberRepository.save(mem);

        return new ModelAndView("redirect:/account");
    }

    @RequestMapping(value={"/account/delete"}, method = RequestMethod.GET)
    public ModelAndView delete(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        Member mem = (Member)model.get("user");


        memberRepository.delete(mem);

        return new ModelAndView("redirect:/logout");
    }

}
