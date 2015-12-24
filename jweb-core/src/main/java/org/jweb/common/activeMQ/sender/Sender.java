package org.jweb.common.activeMQ.sender;

import javax.jms.Destination;

import org.jweb.common.activeMQ.mode.HttpMsgMode;
import org.springframework.jms.core.JmsTemplate;

/**
 * 消息发送器，将消息发送到消息队列中
 * @author wupan
 *
 */
public class Sender {


	private JmsTemplate jmsTemplate;

	private Destination destination ;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}



	/**
	 * 发送消息方法，将httpMsgMode发送到消息队列中
	 * @param httpMsgMode
	 * @return
	 */
	public boolean send(HttpMsgMode httpMsgMode){
		
		jmsTemplate.convertAndSend(destination, httpMsgMode);
		
		return true;
	}
}
