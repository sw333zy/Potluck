package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    DishRepository dishRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listDishes(Model model){
        model.addAttribute("dishes", dishRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String newDish(Model model){
        model.addAttribute("dish", new Dish());
        return "form";
    }

    @PostMapping("/add")
    public String processDish(@ModelAttribute Dish dish,
                              @RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return "redirect:add/";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            dish.setDishpic(uploadResult.get("url").toString());
            dishRepository.save(dish);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return  "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("dish", dishRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("dish", dishRepository.findById(id).get());
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        dishRepository.deleteById(id);
        return "redirect:/";
    }
}
