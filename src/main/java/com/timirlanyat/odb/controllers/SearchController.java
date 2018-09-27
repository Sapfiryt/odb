package com.timirlanyat.odb.controllers;


import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.Reconstruction;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController  {


    @Autowired
    private ReconstructionRepository reconstrRepo;
    @Autowired
    private UserService userService;

    @RequestMapping(value="/search", method = RequestMethod.GET)
    public ModelAndView search(Principal principal,
                               @RequestParam(value = "cost", required=false) Object cost,
                               @RequestParam(value = "not_full", required=false) Boolean notFull,
                               @RequestParam(value = "after", required=false) Object after,
                               @RequestParam(value = "befor", required=false) Object befor) {


        if(cost!=null && !cost.equals("")){
            cost=new Long((String) cost);
        }else{
            cost=new Long("1000000");
        }


        if(notFull==null)
            notFull=false;


        if(after!=null && !after.equals("")) {
            String[] parted = ((String) after).split("-");
            after = LocalDate.of(new Integer(parted[0]),new Integer(parted[1]),new Integer(parted[2]));
        }else{
            after=LocalDate.now();
        }


        if(befor!=null && !befor.equals("")) {
            String[] parted = ((String) befor).split("-");
            befor = LocalDate.of(new Integer(parted[0]),new Integer(parted[1]),new Integer(parted[2]));
        }else{
            befor = LocalDate.of(2200,12,31);
        }

        Map<String, Object> model = userService.getUserParameters(principal);

        List<Reconstruction> reconstructions= new ArrayList<>();
        reconstrRepo.findAllByParams((Long)cost,(LocalDate) after,(LocalDate) befor, notFull)
                .forEach(reconstructions::add);

        model.put("reconstructions",reconstructions);

        return new ModelAndView("search",model);
    }
}
