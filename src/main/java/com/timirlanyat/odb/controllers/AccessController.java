package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.Sex;
import com.timirlanyat.odb.model.User;
import com.timirlanyat.odb.model.UserDto;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Controller
public class AccessController {


    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationManager authenticationManager;


    private User createUserAccount(UserDto accountDto) throws Exception {
        User registered = null;

        registered = service.registerNewUserAccount(accountDto);

        return registered;
    }


    public void authWithAuthManager(HttpServletRequest request, UserDto userDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getEmail(),
                userDto.getPassword());

        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @RequestMapping(value="/registration", method = {RequestMethod.POST})
    public ModelAndView check(@ModelAttribute("newUser") @Valid UserDto dto,
                              BindingResult result, WebRequest request, HttpServletRequest httpRequest, Errors errs)
    {

        Map<String,Object> model= new HashMap<>();
        Map<String,String> errors= new HashMap<>();

        model.put("errors",errors);
        model.put("sexes", Arrays.stream(Sex.values()).map((sex)->
                StringUtils.capitalize(sex.name().toLowerCase()))
                .toArray());

        if(errs.hasErrors()){
            for(FieldError err:errs.getFieldErrors())
                ((Map<String,String>)model.get("errors"))
                        .put(err.getField(), err.getDefaultMessage());

            for(ObjectError err:errs.getAllErrors())
                if(!((Map<String,String>)model.get("errors")).values()
                        .contains(err.getDefaultMessage()))
                    ((Map<String,String>)model.get("errors"))
                            .put("passwordNotMatchedErr",err.getDefaultMessage());
            Map<String,String> errrs = ((Map<String,String>)model.get("errors"));
            for(String key:errrs.keySet()){

                errrs.replace(key,StringUtils.capitalize(errrs.get(key).replace("null","empty")+"!"));
            }
            model.replace("errors",errrs);
            model.put("userNew",dto);
            return new ModelAndView("registration", model);
        }


        User registered = null;

        try {


            registered = createUserAccount(dto);

        }catch (EmailExistsException exp){
            ((Map<String,String>)model.get("errors"))
                    .put("emailError","Email already exists!");
        }catch (Exception exp){
            ((Map<String,String>)model.get("errors"))
                    .put("phoneError","Phone already exists!");
        }
        if (!((Map<String,String>)model.get("errors")).isEmpty()) {
            model.put("newUser",dto);
            return new ModelAndView("registration", model);
        }
        else {
            authWithAuthManager(httpRequest, dto);
            return new ModelAndView("redirect:/", "", null);
        }
    }

    @RequestMapping(value="/registration", method = {RequestMethod.GET})
    public ModelAndView registration() {

        UserDto user= new UserDto();
        Map<String, Object> model = new HashMap<>();
        model.put("newUser",user);
        model.put("notLogined",true);
        model.put("sexes", Arrays.stream(Sex.values()).map((sex)->
                StringUtils.capitalize(sex.name().toLowerCase().replace('_',' ')))
                .toArray());

        return new ModelAndView("registration",model);
    }


    @RequestMapping(value="/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(HttpServletRequest request, Principal principal, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        model.put("notLogined",true);

        request.getParameterMap().forEach( (name,value) -> {
            if(name != null)
                if(name.equals("error")){
                    model.put("error",true);
                }
        });

        if(principal != null){
            String referrer = request.getHeader("Referer");
            if(referrer == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }
            }else {
                String path = referrer.substring(referrer.lastIndexOf("/"));
                return new ModelAndView("redirect:" + path, new HashMap<>());
            }
        }else{

        }

        return new ModelAndView("login",model);
    }
}
