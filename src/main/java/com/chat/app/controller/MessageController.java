package com.chat.app.controller;


import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
																																																																																																																																																																					
import com.chat.app.model.Message;
import com.chat.app.repository.MessageRepository;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	private MessageRepository messagerepository;

	@MessageMapping("/send")
	@SendTo("/topic/messages")
	public Message sendMessage(@Payload Message message) {
		message.setTimestamp(new Date());
		messagerepository.save(message);
		return message;
	}
}