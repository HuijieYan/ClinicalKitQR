package team7.demo.question.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.question.models.Question;

import javax.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Transactional
    @Modifying
    @Query("update Question q set q.question = ?2, q.answer = ?3 where q.id =?1")
    void update(long id,String question,String answer);

    @Transactional
    @Modifying
    @Query("delete from Question q where q.id =?1")
    void delete(long id);

    @Query("select q from Question q where q.id=?1")
    Question findById(long id);
}
