package ClinicalKitQR.question.models;

import javax.persistence.*;

/**
 * Represents the question and answers in FAQ section
 *
 * @value id a long type integer that uniquely identifies the question
 * @value question not null
 * @value answer not null
 */

@Entity(name = "Question")
@Table(name = "Question")
public class Question {
    @Id
    @SequenceGenerator(
            name = "questionIdGen",
            sequenceName = "questionSeq",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "questionIdGen")
    @Column(columnDefinition = "bigint not null")
    private long id;

    private String question;

    private String answer;

    public Question(){}

    public Question(String question,String answer){
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
