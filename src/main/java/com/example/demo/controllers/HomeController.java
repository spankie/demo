package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.User;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController
 */
@Controller
public class HomeController {

  private PasswordEncoder passwordEncoder;

  @Autowired
  public HomeController(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public String home() {
    return "home";
  }

  @RequestMapping(value = "/signuproute", method = RequestMethod.GET)
  public String signup(Model model) {
    model.addAttribute("users", User.users);
    System.out.println(User.users);
    return "signup";
  }

  // @RequestMapping(value = "/signup", method = RequestMethod.POST)
  @PostMapping("/signup")
  public String signup(User user) {
    System.out.printf("user: %s; password: %s", user.getUsername(), user.getPassword());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User.users.put(user.getUsername(), user);
    return "redirect:/signuproute";
  }

}