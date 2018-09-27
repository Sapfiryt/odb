package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.dal.repositories.UserRepository;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.ReconstructionService;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class OrgnizerController {

    @Autowired
    private UserService userService;
    @Autowired
    private ReconstructionService reconstructionService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrganizerRepository organizerRepository;

    @RequestMapping(value="/{id}/reconstructions", method = {RequestMethod.GET})
    public ModelAndView reconstructions(@PathVariable("id") Integer id, Principal principal, HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);
        Organizer org = checkCurrentUser(model,id,resp);


        model.put("reconstructions", org.getReconstructions().toArray());

        return new ModelAndView("managedRecs", model);
    }


    @RequestMapping(value="/{id}/create", method = {RequestMethod.GET})
    public ModelAndView createScratch(@PathVariable("id") Integer id, Principal principal,
                                      HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);
        checkCurrentUser(model,id,resp);


        List<Location> locations = new ArrayList<>();

        for(Location loc: locationRepository.findAll()){
            loc.setImg(Base64.getEncoder().encodeToString(loc.getPhoto()));
            locations.add(loc);
        }

        model.put("locations",locations);
        model.put("creationForm",new ReconstructionDto());

        return new ModelAndView("createRec", model);
    }


    @RequestMapping(value="/{id}/create", method = {RequestMethod.POST})
    public ModelAndView create(@PathVariable("id") Integer id, Principal principal,
                               @ModelAttribute("creationForm") @Valid ReconstructionDto dto,
                               BindingResult result, WebRequest request, Errors errs,
                               HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);
        Map<String,String> errors= new HashMap<>();
        model.put("errors",errors);
        Organizer org = checkCurrentUser(model,id,resp);


        locationRepository.findAll();

        if(errs.hasErrors()){
            for(FieldError err:errs.getFieldErrors())
                ((Map<String,String>)model.get("errors"))
                        .put(err.getField(),err.getDefaultMessage());

            model.put("creationForm",dto);
            return new ModelAndView("createRec", model);
        }

        Reconstruction created = null;

        try{
            created = reconstructionService.createNewReconstrution(dto);
        }
        catch (InvalidAttributeValueException e) {
            ((Map<String,String>)model.get("errors"))
                    .put("locationError","Location not exist!");
        }

        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("creationForm",dto);
            return new ModelAndView("createRec", model);
        }

        org.getReconstructions().add(created);
        organizerRepository.save(org);

        return new ModelAndView("redirect:/reconstructions/"+created.getId(), model);
    }





    private Organizer checkCurrentUser(Map<String, Object> model, Integer id, HttpServletResponse resp)
            throws IOException {

        Organizer org = (Organizer) model.get("organizer");

        if ( org == null || !org.getId().equals(id)) {
            resp.sendError(403);
            return null;
        }

        return org;
    }


}