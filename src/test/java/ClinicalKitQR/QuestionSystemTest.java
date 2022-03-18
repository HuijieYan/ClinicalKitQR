package ClinicalKitQR;

import ClinicalKitQR.question.controllers.QuestionController;
import ClinicalKitQR.question.models.Question;
import ClinicalKitQR.question.services.QuestionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionSystemTest {
    private Question question;

    @Autowired
    private QuestionController questionController;

    @Autowired
    private QuestionService questionService;

    @Test
    @Order(1)
    public void testAddNewQuestionAndGetAll(){
        int originalSize = questionService.getAll().size();
        questionController.addNew("test question","test answer");
        assertEquals(questionService.getAll().size(),originalSize+1);
        question = questionService.getAll().get(originalSize);
        assertEquals(question.getQuestion(),"test question");
        assertEquals(question.getAnswer(),"test answer");

        assertEquals(questionController.getAll().size(),originalSize+1);
        //testing get all function by measuring the size of the list

        //the code below tests if invalid entry is entered
        questionController.addNew("","test answer");
        questionController.addNew("test question","");
        questionController.addNew("","");
        assertEquals(questionController.getAll().size(),originalSize+1);
    }

    @Test
    @Order(2)
    public void testUpdateQuestion(){
        questionController.update(question.getId(),"updated test question","updated test answer");
        question = questionService.getById(question.getId());
        assertEquals(question.getQuestion(),"updated test question");
        assertEquals(question.getAnswer(),"updated test answer");

        //the code below tests if invalid entry is entered
        questionController.update(question.getId(),"test question","");
        questionController.update(question.getId(),"","test answer");
        questionController.update(question.getId(),"","");
        question = questionService.getById(question.getId());
        assertEquals(question.getQuestion(),"updated test question");
        assertEquals(question.getAnswer(),"updated test answer");
    }

    @Test
    @Order(3)
    public void testDeleteQuestion(){
        int originalSize = questionService.getAll().size();
        questionController.delete(question.getId());
        assertNull(questionService.getById(question.getId()));
        assertEquals(questionController.getAll().size(),originalSize-1);

        //when deleting non-existing question, nothing happens
        questionController.delete(question.getId());
        assertEquals(questionController.getAll().size(),originalSize-1);
    }
}
