package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.entity.AttributesInReconstructions;
import com.timirlanyat.odb.dal.repositories.AttributeRepository;
import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.ReconstructionService;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReconstructionService reconstructionService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    @RequestMapping(value="/organizer/reconstructions", method = {RequestMethod.GET})
    public ModelAndView reconstructions(Principal principal, HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);

        Organizer org = (Organizer) model.get("organizer");

        model.put("reconstructions", org.getManagedReconstructions().toArray());

        return new ModelAndView("managedRecs", model);
    }


    @RequestMapping(value="/organizer/create", method = {RequestMethod.GET})
    public ModelAndView createScratch( Principal principal, HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);


        List<Attribute> attributes = new ArrayList<>();
        attributeRepository.findAll().forEach(attribute -> {
            if(attribute.getAmount()>0)
                attributes.add(attribute);
        });

        List<Location> locations = new ArrayList<>();

        for(Location loc: locationRepository.findAll()){
            loc.setImg(Base64.getEncoder().encodeToString(loc.getPhoto()));
            locations.add(loc);
        }

        model.put("attributes",attributes);
        model.put("locations",locations);
        model.put("creationForm",new ReconstructionDto());

        return new ModelAndView("createRec", model);
    }


    @RequestMapping(value="/organizer/create", method = {RequestMethod.POST})
    public ModelAndView create( Principal principal,
                               @ModelAttribute("creationForm") @Valid ReconstructionDto dto,
                               BindingResult result, WebRequest request, Errors errs,
                               HttpServletResponse resp)
            throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);
        Map<String,String> errors= new HashMap<>();
        model.put("errors",errors);
        Organizer org = (Organizer) model.get("organizer");

        List<Attribute> attributes = new ArrayList<>();
        attributeRepository.findAll().forEach(attribute -> {
            if(attribute.getAmount()>0)
                attributes.add(attribute);
        });

        List<Location> locations = new ArrayList<>();

        for(Location loc: locationRepository.findAll()){
            loc.setImg(Base64.getEncoder().encodeToString(loc.getPhoto()));
            locations.add(loc);
        }

        model.put("attributes",attributes);
        model.put("locations",locations);


        if(errs.hasErrors()){
            for(ObjectError err:errs.getAllErrors())
                if(err instanceof FieldError)
                    ((Map<String,String>)model.get("errors"))
                            .put(((FieldError)err).getField(),
                                    StringUtils.capitalize(err.getDefaultMessage()
                                            .replace("null","empty")
                                    )
                            );
                else
                    ((Map<String,String>)model.get("errors"))
                            .put(err.getCode(),
                                    StringUtils.capitalize(err.getDefaultMessage()
                                            .replace("null","empty")
                                    )
                            );

            model.put("creationForm",dto);
            return new ModelAndView("createRec", model);
        }

        Reconstruction created = null;

        try{
            created = reconstructionService.createNewReconstrution(dto,org);
        }
        catch (InvalidAttributeValueException e) {
            ((Map<String,String>)model.get("errors"))
                    .put("locationError",e.getMessage());
        }

        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("creationForm",dto);
            return new ModelAndView("createRec", model);
        }

        model.remove("locations");
        org.getManagedReconstructions().add(created);

        organizerRepository.save(org);

        return new ModelAndView("redirect:/reconstructions/"+created.getId(), model);
    }

    @RequestMapping(value="/reconstructions/{id}/attributes", method = {RequestMethod.GET})
    public ModelAndView attributeList(@PathVariable("id") Integer id, Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);


        Reconstruction rec = reconstructionRepository.findById(id).get();

        model.put("attributes",rec.getAttributes());

        return new ModelAndView("recAttributeList",model);
    }
}
