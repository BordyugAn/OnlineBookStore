package ru.bookstore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bookstore.domain.GenreEntity;
import ru.bookstore.domain.PublishHouseEntity;
import ru.bookstore.form.Genre;
import ru.bookstore.form.PublishHouse;
import ru.bookstore.repos.GenreRepo;
import ru.bookstore.repos.PublishHouseRepo;

import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private PublishHouseRepo houseRepo;

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "/index";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @GetMapping("/catalog")
    public String catalog() { return "/catalog"; }

    @GetMapping("/adminpanel")
    public String adminpanel() { return "/adminpanel"; }

    @GetMapping("/newbook")
    public String newbook() { return "/newbook"; }


    @RequestMapping(value = "/characteristic", method = RequestMethod.GET)
    public String showAllcharacteristic(Model model, Genre genre, PublishHouse publishHouse) {
        model.addAttribute("genres", genreRepo.findAll());
        model.addAttribute("publish", houseRepo.findAll());
        return "/characteristic";
    }

    @RequestMapping(value = "/characteristicGenre", method = RequestMethod.POST)
    public String addNewGenre(@Valid Genre genre, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        genreRepo.save(new GenreEntity(genre.getName()));
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicPublish", method = RequestMethod.POST)
    public String addNewPublish(@Valid PublishHouse publishHouse, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        houseRepo.save(new PublishHouseEntity(publishHouse.getName(), publishHouse.getCountry(), publishHouse.getCity()));
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicGenreDel", method = RequestMethod.POST)
    public String deleteGenre(@Valid Genre genre, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        genreRepo.deleteByName(genre.getName());
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicPublishDel", method = RequestMethod.POST)
    public String deletePublish(@Valid PublishHouse publishHouse, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        houseRepo.deleteByName(publishHouse.getName());
        return "redirect:/characteristic";
    }
}
