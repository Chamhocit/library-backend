package com.example.aptech.spring.library.Service;

import com.example.aptech.spring.library.Request.AdminResponseQuestion;
import com.example.aptech.spring.library.dao.MessagesRepository;
import com.example.aptech.spring.library.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {
    private MessagesRepository messagesRepository;
    @Autowired
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public void postMessage(Message messageRequest, String userEmail){
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messagesRepository.save(message);
    }

    public void putMessage(AdminResponseQuestion adminResponseQuestion, String userEmail) throws Exception{
        Optional<Message> message = messagesRepository.findById(adminResponseQuestion.getId());
        if(!message.isPresent()){
            throw new  Exception("Message not found");
        };
        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminResponseQuestion.getResponse());
        message.get().setClosed(true);
        messagesRepository.save(message.get());
    }


}
