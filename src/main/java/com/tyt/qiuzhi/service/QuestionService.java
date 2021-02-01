package com.tyt.qiuzhi.service;

import com.tyt.qiuzhi.dao.QuestionDAO;
import com.tyt.qiuzhi.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question){
        //处理HTML标签
        question.setDescription(HtmlUtils.htmlEscape(question.getDescription()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //处理敏感词
        question.setDescription(sensitiveService.filter(question.getDescription()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDAO.addQuestion(question);
    }

    public List<Question> selectLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

    public int updateCommentCount(int id,int commentCount){
        return questionDAO.updateCommentCount(id,commentCount);
    }

    public Question selectById(int id){
        return questionDAO.selectById(id);
    }

}