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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

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

        if(LocalDateTime.now().isAfter(rec.getDateOf())){
            if(rec.getParticipants().size()<rec.getMinParticipant()||LocalDateTime.now().isAfter(rec.getDateOf().plusDays(1)))
                rec.setStatus("closed");
            else
                rec.setStatus("in progress");

            reconstructionRepository.save(rec);
        }


        rec.getLocation().setImg(Base64.getEncoder().encodeToString(rec.getLocation().getPhoto()));

        rec.getAttributesInUse().forEach(attrInUse -> rec.getAttributes().stream()
                .filter( attr -> attrInUse.getAttribute().equals(attr.getAttribute()))
                .forEach( item -> item.setAmountOf(item.getAmountOf()-attrInUse.getAmount())));

        model.put("attributes", rec.getAttributes().stream().filter(attr -> attr.getAmountOf()>0).toArray());
        model.put("reconstruction", rec);
        model.put("numberOfParticipant",rec.getParticipants().size());

        model.put("dateOfReconstruction",rec.getDateOf().format(DateTimeFormatter.ofPattern("d MMMM yy  hh:mm a")));


        if(model.get("user")!=null&&rec.getParticipants().contains((User)model.get("user"))||!rec.getStatus().equals("open"))
            model.put("joined",true);

        if(model.get("organizer")!=null&&((Organizer)model.get("organizer")).getManagedReconstructions().contains(rec))
            model.put("managed",true);

        return new ModelAndView("reconstruction",model);
    }

    @RequestMapping(value="/reconstructions/{id}/join", method = {RequestMethod.GET})
    public ModelAndView addParticipant(@PathVariable("id") Integer id, Principal principal){

        Map<String,Object> model= userService.getUserParameters(principal);

        Reconstruction rec = reconstructionRepository.findById(id).get();
        rec.getParticipants().add((Member)model.get("user"));

        if (rec.getParticipants().size()>=rec.getMaxParticipant())
            rec.setStatus("recruitment closed");

        reconstructionRepository.save(rec);

        model.put("joined",true);
        model.put("recId",id);

        return new ModelAndView("join", model);
    }



}
