package com.timirlanyat.odb.controllers;

import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.dal.repositories.PaymentRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.Member;
import com.timirlanyat.odb.model.Payment;
import com.timirlanyat.odb.model.Reconstruction;
import com.timirlanyat.odb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PaymentController {

    @Autowired
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/payment/{id}", method = {RequestMethod.GET})
    public ModelAndView paymentFrom(@PathVariable("id") Integer id, Principal principal, HttpServletResponse resp) throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);

        float payedSum = 0F;

        Optional<Reconstruction> found = reconstructionRepository.findById(id);
        model.put("reconstruction_id",id);

        if(found.isPresent()) {
            for (Payment payment : paymentRepository.findAllByReconstructionAndMember(
                    (Member) model.get("user"), found.get()))
                payedSum += payment.getTotal();
            model.put("cost",found.get().getCost()-payedSum);
        }else{
            resp.sendError(404);
        }

        return new ModelAndView("payment", model);
    }



    @RequestMapping(value = "/payment/{id}", method = {RequestMethod.POST})
    public ModelAndView payment(@PathVariable("id") Integer id,
                                @RequestParam("total") Float total,
                                Principal principal, HttpServletResponse resp) throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);

        float payedSum = 0F;

        Optional<Reconstruction> found = reconstructionRepository.findById(id);


        if(found.isPresent()) {

            Payment partPayment = new Payment().setDateOf(LocalDateTime.now())
                    .setMember((Member) model.get("user"))
                    .setReconstruction(found.get())
                    .setSettlementNumber(UUID.randomUUID())
                    .setTotal(total);
            paymentRepository.save(partPayment);

            model.put("check",partPayment);
            model.put("recId",partPayment.getReconstruction().getId());

            for (Payment payment : paymentRepository.findAllByReconstructionAndMember(
                    (Member) model.get("user"), found.get()))
                payedSum += payment.getTotal();


            if ((found.get().getCost()-payedSum) == 0) {
                model.put("joined", true);
                return new ModelAndView("check", model);
            }
            else
                return new ModelAndView("check", model);

        }else{
            resp.sendError(404);
        }

        return new ModelAndView("redirect:/reconstructions/"+id, model);
    }

    @RequestMapping(value = "/checks/{id}", method = {RequestMethod.GET})
    public ModelAndView checksFrom(@PathVariable("id") Integer id, Principal principal, HttpServletResponse resp) throws IOException {

        Map<String, Object> model = userService.getUserParameters(principal);


        Optional<Member> found = memberRepository.findById(id);
        List<Payment> checks = new ArrayList<>();

        if(found.isPresent()) {
            for (Payment payment : paymentRepository.findAllByMember((Member) model.get("user")))
                checks.add(payment);
            model.put("checks",checks);
        }else{
            resp.sendError(404);
        }

        return new ModelAndView("checks", model);
    }
}
