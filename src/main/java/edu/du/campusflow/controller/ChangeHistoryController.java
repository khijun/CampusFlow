package edu.du.campusflow.controller;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.service.ChangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChangeHistoryController {
    private final ChangeHistoryService changeHistoryService;


    @GetMapping("/iframe/academic/admin/history_view")
    public String historyView(Model model) {
        List<ChangeHistory> changeHistoryList = changeHistoryService.findAll();
        model.addAttribute("changeHistoryList", changeHistoryList);
        return "view/iframe/academic/admin/admin-change-history-list";
    }
}
