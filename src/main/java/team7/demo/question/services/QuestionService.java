package team7.demo.question.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.question.models.Question;
import team7.demo.question.repositories.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    @Autowired
    public QuestionService(QuestionRepository repository){
        this.repository = repository;
    }

    public List<Question> getAll(){
        return repository.findAll();
    }

    public void save(Question question){
        repository.save(question);
    }

    public void update(long id,String question,String answer){
        repository.update(id, question, answer);
    }

    public Question getById(long id){
        return repository.getById(id);
    }

    public void delete(long id){
        repository.delete(id);
    }
}
