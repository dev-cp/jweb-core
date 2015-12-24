package org.jweb.common.activeMQ.convert;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.jweb.common.activeMQ.mode.HttpMsgMode;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * 消息队列中的消息转换器
 * @author wupan
 *
 */
public class ActiveMQHttpConvert implements MessageConverter {

	@Override
	public Message toMessage(Object object, Session session)
			throws JMSException, MessageConversionException {
		
		ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
        msg.setObject((Serializable) object);
        return msg; 

	}

	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		
		HttpMsgMode mode = null;
        if(message instanceof ActiveMQObjectMessage){
            ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage) message; 
            mode = (HttpMsgMode) aMsg.getObject();
        }
        return mode;
	}

}
