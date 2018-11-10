package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.AttributeRepository;
import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.*;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
public class AttributeController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private UserService userService;

    @RequestMapping(value={"/attributes/reserve"}, method = RequestMethod.POST)
    public ModelAndView createLocation(Principal principal,
                                       @ModelAttribute("attributesForm")AttributeReserveDto dto,
                                       BindingResult result, WebRequest request, Errors errs, HttpServletResponse resp)
    {
        Map<String,Object> model = userService.getUserParameters(principal);
        User user = (User) model.get("user");

        Optional<Reconstruction> foundRec = reconstructionRepository.findById(dto.getReconstructionId());


        if(foundRec.isPresent())
            for(int i=0;i<dto.getAttributesId().size();i++) {

                Optional<Attribute> foundAttr = attributeRepository.findById(dto.getAttributesId().get(i));

                if(foundAttr.isPresent())
                    user.addAttribute(foundAttr.get(),foundRec.get(),dto.getAmount().get(i));
            }


        memberRepository.save((Member)user);

        return new ModelAndView("redirect:/reconstructions/"+dto.getReconstructionId(),model);
    }
}
