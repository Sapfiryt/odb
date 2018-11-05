package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.Reconstruction;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class ParticipantsController {


    @Autowired
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private UserService userService;


    @RequestMapping(value="/reconstructions/{id}/participants", method = {RequestMethod.GET})
    public ModelAndView reconstruction(@PathVariable("id") Integer id, Principal principal,
                                       HttpServletResponse resp) throws IOException {

        Map<String, Object> model = null;
        Reconstruction rec;

        try {
            model = userService.getUserParameters(principal);
            rec = reconstructionRepository.findById(id).get();
            Organizer org =(Organizer) model.get("organizer");
            if ( org == null || !org.getManagedReconstructions().contains(rec)) {
                resp.sendError(403);
                return null;
            }
        }catch (NoSuchElementException e){
            resp.sendError(403);
            return null;
        }

         model.put("participants",rec.getParticipants().toArray());

        return new ModelAndView("participants",model);
    }
}
