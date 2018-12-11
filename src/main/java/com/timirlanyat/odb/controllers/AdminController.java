package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.AttributeRepository;
import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.AttributeService;
import com.timirlanyat.odb.services.LocationService;
import com.timirlanyat.odb.services.UserService;
import org.postgresql.util.PSQLException;
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
import java.util.*;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ReconstructionRepository reconstructionRepository;

    @RequestMapping(value={"/admin"}, method = RequestMethod.GET)
    public ModelAndView adminView(Principal principal) {

        Map<String,Object> model = userService.getUserParameters(principal);

        model.put("notApproved", organizerRepository.findNotApproved());


        return new ModelAndView("admin",model);
    }

    @RequestMapping(value={"/admin"}, method = RequestMethod.POST)
    public String approve(@RequestParam("id") Integer id) {


        organizerRepository.findById(id).ifPresent( org -> organizerRepository
                                                            .save(org.setApproved(true)
                                                                    .addRole("REC_ORG")));

        return "redirect:/admin";
    }



    @RequestMapping(value={"/admin/create/location"}, method = RequestMethod.GET)
    public ModelAndView createLocationView(Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);
        model.put("creationForm",new LocationDto());


        return new ModelAndView("createLocation",model);
    }

    @RequestMapping(value={"/admin/create/location"}, method = RequestMethod.POST)
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


        created = locationService.createNewLocation(dto);


        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("creationForm",dto);
            return new ModelAndView("createLocation", model);
        }

        return new ModelAndView("redirect:/admin",model);
    }



    @RequestMapping(value={"/admin/create/attribute"}, method = RequestMethod.GET)
    public ModelAndView createAttributeView(Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);
        model.put("types", Arrays.stream(AttributeType.values()).map((type)->
                StringUtils.capitalize(type.name().toLowerCase()))
                .toArray());

        model.put("creationForm",new AttributeDto());


        return new ModelAndView("createAttribute",model);
    }

    @RequestMapping(value={"/admin/create/attribute"}, method = RequestMethod.POST)
    public ModelAndView createAttribute(Principal principal,
                                       @ModelAttribute("creationForm") @Valid AttributeDto dto,
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
            return new ModelAndView("createAttribute", model);
        }

        Attribute created = null;


        created = attributeService.createNewAttribute(dto);


        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("creationForm",dto);
            return new ModelAndView("createAttribute", model);
        }

        return new ModelAndView("redirect:/admin",model);
    }


    @RequestMapping(value={"/admin/attributes"}, method = RequestMethod.GET)
    public ModelAndView attributeList(Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);

        List<Attribute> attrs = new ArrayList<>();
        attributeRepository.findAll().forEach(attrs::add);
        List<Reconstruction> recs = new ArrayList<>();
        reconstructionRepository.findAll().forEach(recs::add);

        for(Reconstruction rec:recs)
            for(Attribute attr:attrs)
                rec.getAttributes().forEach( air -> {
                    if(air.getAttribute().equals(attr)) attr.setCannotDelete(true);
                });




        model.put("attributes", attrs);

        return new ModelAndView("attributeList",model);
    }

    @RequestMapping(value={"/admin/locations"}, method = RequestMethod.GET)
    public ModelAndView locationList(Principal principal){

        Map<String,Object> model = userService.getUserParameters(principal);
        List<Location> locations = new ArrayList<>();

        for(Location loc: locationRepository.findAll()){
            loc.setImg(Base64.getEncoder().encodeToString(loc.getPhoto()));
            locations.add(loc);
        }
        model.put("locations",locations);

        return new ModelAndView("locationList",model);
    }

    @RequestMapping(value={"/admin/attributes/delete"}, method = RequestMethod.POST)
    public ModelAndView delete(Principal principal, @RequestParam("id") Integer id) {


        attributeRepository.deleteById(id);



        return new ModelAndView("redirect:/admin/attributes");
    }
}
