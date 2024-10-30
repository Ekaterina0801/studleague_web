package com.studleague.studleague.controllers;


import com.studleague.studleague.forms.RegistrationForm;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.implementations.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private PasswordEncoder passwordEncoder;

    private final UserService userService;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder,
            UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }
    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult bindingResult, Model model) {
        if (!form.getPassword().equals(form.getConfirm())) {
            bindingResult.rejectValue("confirm", "error.confirm", "Пароли не совпадают");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "registration";
        }

        userService.createUser(form.toUserDTO(passwordEncoder));
        return "redirect:/login";
    }


}
