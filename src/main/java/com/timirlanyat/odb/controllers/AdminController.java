package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.LocationService;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private LocationService locationService;


    @RequestMapping(value={"/admin"}, method = RequestMethod.GET)
    public ModelAndView adminView(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        model.put("notApproved", organizerRepository.findNotApproved());


        return new ModelAndView("admin",model);
    }

    @RequestMapping(value={"/admin/{id}"}, method = RequestMethod.POST)
    public String approve(@PathVariable("id") Integer id) {


                organizerRepository.findById(id).ifPresent( org -> organizerRepository
                                                                    .save(org.setApproved(true)
                                                                            .addRole("REC_ORG")));

        return "redirect:/admin";
    }



    @RequestMapping(value={"/admin/create"}, method = RequestMethod.GET)
    public ModelAndView createLocationView(Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);
        model.put("creationForm",new LocationDto());


        return new ModelAndView("createLocation",model);
    }

    @RequestMapping(value={"/admin/create"}, method = RequestMethod.POST)
    public ModelAndView createLocation(@RequestParam("photo") MultipartFile photo, Principal principal,
                                       @ModelAttribute("creationForm") @Valid LocationDto dto,
                                       BindingResult result, WebRequest request, Errors errs, HttpServletResponse resp)
    {
        Map<String,Object> model = userService.getUserParameters(principal);

        Map<String,String> errors= new HashMap<>();
        model.put("errors",errors);

        if(errs.hasErrors()){
            for(FieldError err:errs.getFieldErrors())
                ((Map<String,String>)model.get("errors"))
                        .put(err.getField(),
                                StringUtils.capitalize(err.getDefaultMessage()
                                        .replace("null","empty")
                                )
                        );

            model.put("creationForm",dto);
            return new ModelAndView("createLocation", model);
        }

        Location created = null;

        try{
            created = locationService.createNewLocation(dto);
        }
        catch (InvalidAttributeValueException e) {
            ((Map<String,String>)model.get("errors"))
                    .put("locationError","Location not exist!");
        }

        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("creationForm",dto);
            return new ModelAndView("createRec", model);
        }

        return new ModelAndView("redirect:/admin",model);
    }
}
