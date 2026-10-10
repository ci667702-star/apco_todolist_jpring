package com.example.apco.controller;

import com.example.apco.model.Todo;
import com.example.apco.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class TodoController {
    private String redirectToList(
            String keyword,
            String sortType,
            RedirectAttributes redirectAttributes
            ) {
        redirectAttributes.addAttribute("keyword", keyword);
        redirectAttributes.addAttribute("sortType", sortType);
        return "redirect:/sort";
    }

    private final TodoService todoService;
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    //Read
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("todos", todoService.findAll());
        return "index";
    }

    //Create
    @PostMapping("/add")
    public String addTodo(@RequestParam String text,
                          @RequestParam String priority,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate){
        //할 일 html에서 데이터 받아오기
        todoService.addTodo(text, priority, dueDate);
        return "redirect:/";
    }

    //Delete
    // POST /delete/3
    // Todo 상태 변경(toggle, delete)
    // 302 redirect
    // GET /sort?keyword="keyword"&sortType="sortType"
    // 목록 화면 출력
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "") String keyword,
                         @RequestParam(defaultValue = "dueDateAsc") String sortType,
                         RedirectAttributes redirectAttributes) {
        //get은 원칙적으로 데이터를 조회하는 안전한 요청이어야한다.
        //서버 상태를 변경하는 작업에는 사용하지 않는 것이 맞다.
        //post는 서버에 데이터를 보내 상태 변경이나 부수 효과를 요청할 때 사용한다.
        todoService.deleteTodo(id);

        return redirectToList(keyword, sortType, redirectAttributes);

    }

    //update
    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id,
                         @RequestParam(defaultValue = "") String keyword,
                         @RequestParam(defaultValue = "dueDateAsc") String sortType, //defaultValue를 적용 시켜야하는 이유
                                                                                     // 검색 화면이 아닌 곳에서 수정 화면에 접근해도 400 오류가 발생하지 앟기 위해
                         RedirectAttributes redirectAttributes){
        todoService.toggleTodo(id);
        return redirectToList(keyword, sortType, redirectAttributes);
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           @RequestParam(defaultValue = "") String keyword,
                           @RequestParam(defaultValue = "dueDateAsc") String sortType,
                           Model model){
        Todo todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortType", sortType);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "dueDateAsc") String sortType,
                       @RequestParam String text,
                       @RequestParam String priority,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,

                       RedirectAttributes redirectAttributes){
        todoService.updateTodo(id, text, priority, dueDate);
        return redirectToList(keyword, sortType, redirectAttributes);
    }

    @GetMapping("/search")
    public String search(@RequestParam
                                String keyword,
                                Model model){
        String sortType = "dueDateAsc";
        model.addAttribute("todos", todoService.searchAndSrt(keyword, sortType));
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/sort")
    public String duedate(@RequestParam(required = false, defaultValue = "") String keyword,
                          @RequestParam(defaultValue = "dueDateAsc") String sortType,
                          Model model){
        model.addAttribute("todos", todoService.searchAndSrt(keyword, sortType));
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortType", sortType);
        return "index";
    }

    // 중요도(priority), 마감일(dueTime)
    // Todo entity에 priority, dueDate 필드 추가하기
    // html 입력 폼에서 중요도와 마감일 받기
    // 컨트롤러, 서비스, 레포 등 수정
    // MYSQL에 실제 컬럼과 데이터가 반영되는지 확인
    // 요구사항이 바뀌면 entity, 화면, 컨트롤러, 서비스, DB가 함께 바뀐다.

    /* 전체 흐름
       entity 수정 -> service 수정 -> controller -> html -> 실행테스트 -> mysql 확인
     */
}