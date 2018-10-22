package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.Reconstruction;
import com.timirlanyat.odb.model.User;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Base64;
import java.util.Map;

@Controller
public class ReconstructionController {

    @Autowired
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private UserService userService;


    @RequestMapping(value="/reconstructions/{id}", method = {RequestMethod.GET})
    public ModelAndView reconstruction(@PathVariable("id") Integer id, Principal principal){

        Map<String,Object> model= userService.getUserParameters(principal);

        Reconstruction rec = reconstructionRepository.findById(id).get();
        rec.getLocation().setImg(Base64.getEncoder().encodeToString(rec.getLocation().getPhoto()));


        model.put("reconstruction", rec);
        model.put("numberOfParticipant",rec.getParticipants().size());


        if(model.get("user")!=null&&rec.getParticipants().contains((User)model.get("user")))
            model.put("joined",true);

        if(model.get("organizer")!=null&&((Organizer)model.get("organizer")).getManagedReconstructions().contains(rec))
            model.put("managed",true);

        return new ModelAndView("reconstruction",model);
    }

    @RequestMapping(value="/reconstructions/{id}/join", method = {RequestMethod.GET})
    public ModelAndView addParticipant(@PathVariable("id") Integer id, Principal principal){

        Map<String,Object> model= userService.getUserParameters(principal);

        Reconstruction rec = reconstructionRepository.findById(id).get();
        rec.getLocation().setImg(Base64.getEncoder().encodeToString(rec.getLocation().getPhoto()));
        rec.getParticipants().add((Member)model.get("user"));

        reconstructionRepository.save(rec);

        model.put("numberOfParticipant",rec.getParticipants().size());
        model.put("joined",true);
        model.put("reconstruction", rec);


        return new ModelAndView("redirect:/reconstructions/"+id, model);
    }
}
