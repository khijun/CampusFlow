package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DeptController {

   @Autowired
   private DeptService deptService;

   @GetMapping("/departments-page")
   public String getDepartmentsPage(Model model) {
      List<Dept> departments = deptService.findAllDepartments();
      model.addAttribute("departments", departments);
      return "view/departments"; // `view/departments.html`
   }
}
