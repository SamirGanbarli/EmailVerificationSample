package com.Email1.controller;


import com.Email1.model.User;
import com.Email1.repository.EmailService;
import com.Email1.repository.UserRepository;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    private String  mailOfCurrentUser;

    @GetMapping("/")
    public String getFirst(Model model){

        User user = new User();

        model.addAttribute(user);

        return "register";
    }

    @PostMapping("/regpro")
    public String getSecond(User user){

        User u = new User();

        u.setName(user.getName());
        u.setLastName(user.getLastName());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setConfirmed(false);
        userRepo.save(u);
        mailOfCurrentUser = u.getEmail();

        //mail sending part
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(user.getEmail());
        m.setFrom("type your mail address");
        m.setText("Click the link please :') "+
                "http://localhost:8080/just-confirm");

        emailService.sendEmail(m);


        //and sending mail to the address we included
        return  "waiting";
    }

    @GetMapping("/just-confirm")
    public String getThird(){

        User u1 = userRepo.findByEmail(mailOfCurrentUser);
       // userRepo.delete(u1);
        u1.setConfirmed(true);
        userRepo.save(u1);

        return "confirmed";
    }
}
