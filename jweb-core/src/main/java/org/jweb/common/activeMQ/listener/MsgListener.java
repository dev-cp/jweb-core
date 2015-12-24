package org.jweb.common.activeMQ.listener;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.jweb.common.activeMQ.entity.HttpResponseEntity;
import org.jweb.common.activeMQ.mode.HttpMsgMode;
import org.jweb.common.activeMQ.service.ActiveMQServiceI;
import org.jweb.common.activeMQ.utils.HttpClientUtil4ActiveMQ;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息队列监听器，用于监听消息队列，并从消息队列中获取待处理的消息
 * 
 * @author wupan
 * 
 */
public class MsgListener implements MessageListener {
	private static Log log = LogFactory.getLog(MsgListener.class);

	ExecutorService executorService = Executors.newFixedThreadPool(50);// 线程池执行器

	@Autowired
	private ActiveMQServiceI actionMQService;

	@Override
	public void onMessage(final Message message) {
		if (message instanceof ObjectMessage) {

			executorService.submit(new Runnable() {

				@Override
				public void run() {
					ObjectMessage objectMessage = (ObjectMessage) message;

					try {
						HttpMsgMode mode = (HttpMsgMode) objectMessage
								.getObject();
						
						String result = HttpClientUtil4ActiveMQ.sendPost(mode);
						
						// 将处理结果封装到entity中，存放到数据库
						HttpResponseEntity entity = new HttpResponseEntity();
						entity.setResult(result);
						entity.setSessionId(mode.getSessionId());
						entity.setUserId(mode.getUserId());

						actionMQService.save(entity);

					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		}

	}
}
