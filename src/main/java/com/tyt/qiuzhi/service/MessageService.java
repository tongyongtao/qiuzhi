package com.tyt.qiuzhi.service;

import com.tyt.qiuzhi.dao.MessageDAO;
import com.tyt.qiuzhi.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int updateMessageRead(int id){
        return messageDAO.updateMessageRead(id);
    }

    public int updateMessageStatus(int id){
        return messageDAO.updateMessageStatus(id);
    }

    public int updateMessageByConversationIdStatus(String conversationId){
        return messageDAO.updateMessageByConversationIdStatus(conversationId);
    }

    public int getMessageCount(String conversationId){
        return messageDAO.getMessageCount(conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()).get(0).toString());
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConvesationUnreadCount(userId, conversationId);
    }


}
