package com.example.apco.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
public class Todo {
    @Setter
    @Getter
    private String text;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    private boolean done;

    @Setter
    @Getter
    private String priority;

    @Setter
    @Getter
    private LocalDate dueDate;

    public Todo(String text, String priority, LocalDate dueDate){
        this.text=text;
        this.done=false;
        this.priority=priority;
        this.dueDate=dueDate;
    }

    public Todo() {
        this.text=text;
        this.done=false;
    }

    public void toggle(){
        this.done=!this.done;
    }
}

/*
1. Controller
/
/add
/delete/{id}
/toggle/{id}
2. HTML
thymeleaf html
 */