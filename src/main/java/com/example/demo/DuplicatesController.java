package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DuplicatesController {

  @GetMapping("/duplicates")
  public String duplicates(Model model) {
    FindDuplicates fd = new FindDuplicates("normal.csv");

    model.addAttribute("duplicates", fd.findDups());
    return "duplicates";
  }

  @GetMapping("/advanced-duplicates")
  public String duplicatesAdvanced(Model model) {
    FindDuplicates fd = new FindDuplicates("advanced.csv");

    model.addAttribute("advancedDuplicates", fd.findDups());
    return "advanced-duplicates";
  }

}