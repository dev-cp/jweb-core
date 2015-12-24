package org.jweb.common.activeMQ.service.impl;

import org.jweb.common.activeMQ.entity.HttpResponseEntity;
import org.jweb.common.activeMQ.service.ActiveMQServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("activeMQService")
@Transactional
public class ActiveMQServiceImpl   implements ActiveMQServiceI {

	@Override
	public void save(HttpResponseEntity entity) {
		// TODO Auto-generated method stub
		
	}

}
