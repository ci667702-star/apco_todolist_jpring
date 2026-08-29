package com.example.apco.controller;

import com.example.apco.model.Todo;
import com.example.apco.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class TodoController {

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
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        todoService.deleteTodo(id);
        return "redirect:/";
    }

    //update
    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id){
        todoService.toggleTodo(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Todo todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String text,
                       @RequestParam String priority,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate){
        todoService.updateTodo(id, text, priority, dueDate);
        return "redirect:/";
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

//    @GetMapping("/sort")
//    public String sort(Model model){
//        model.addAttribute("todos", todoService.findAll());
//        return "index";
//    }

    @GetMapping("/sort/priority")
    public String priority(@RequestParam(required = false, defaultValue = "") String keyword,
                           @RequestParam("value") String priority,
                           Model model){
        model.addAttribute("todos", todoService.findByPriority(priority, keyword));
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

    @GetMapping("/todo-due")
    public String todo_due(@RequestParam(required = false, defaultValue = "") String keyword,
                           @RequestParam(defaultValue = "dueDateAsc") String sortType,
                           Model model){
        model.addAttribute("todos",todoService.searchAndSrt(keyword, sortType));
        model.addAttribute("keyword", keyword);
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