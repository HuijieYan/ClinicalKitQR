package team7.demo.question.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.question.models.Question;
import team7.demo.question.services.QuestionService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService service;

    @Autowired
    public QuestionController(QuestionService service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<Question> getALl(){
        return service.getAll();
    }

    @PostMapping("/new")
    public void addNew(@RequestParam("question")String question,@RequestParam("answer") String answer){
        service.save(new Question(question,answer));
    }

    @PostMapping("/update")
    public void update(@RequestParam("id")long id,@RequestParam("question")String question,@RequestParam("answer") String answer){
        if (service.getById(id)==null){
            return;
        }
        service.update(id, question, answer);
    }

    @PostMapping("/delete")
    public void update(@RequestParam("id")long id){
        if (service.getById(id)==null){
            return;
        }
        service.delete(id);
    }
}
