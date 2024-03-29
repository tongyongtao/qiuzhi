package com.tyt.qiuzhi.dao;


import com.tyt.qiuzhi.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, description, label, reward, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;


    @Select({"select count(id) from ", TABLE_NAME})
    int selectQuestionsCount();

    @Select({"select ", SELECT_FIELDS ," from ", TABLE_NAME})
    List<Question> selectAll();

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{description},#{label},#{reward},#{createdDate},#{userId},#{commentCount})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set description=#{description} where id=#{id}"})
    boolean updateDescription(@Param("id") int id, @Param("description") String description);

    @Select({"select ", SELECT_FIELDS ," from ", TABLE_NAME, " where id=#{id}"})
    Question selectById(int id);

    List<Question> selectByLabel(@Param("label") String label, @Param("offset") int offset,
                                 @Param("limit") int limit);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    boolean deleteQuestion(int id);

}
