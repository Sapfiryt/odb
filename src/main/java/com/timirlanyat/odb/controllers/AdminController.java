package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrganizerRepository organizerRepository;


    @RequestMapping(value={"/admin"}, method = RequestMethod.GET)
    public ModelAndView adminView(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        model.put("notApproved", organizerRepository.findNotApproved());


        return new ModelAndView("admin",model);
    }

    @RequestMapping(value={"/admin/{id}"}, method = RequestMethod.POST)
    public String approve(@PathVariable("id") Integer id) {


                organizerRepository.findById(id).ifPresent( org -> organizerRepository
                                                                    .save(org.setApproved(true)));

        return "redirect:/admin";
    }
}
