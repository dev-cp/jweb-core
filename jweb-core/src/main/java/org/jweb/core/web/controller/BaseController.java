package org.jweb.core.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {
	protected Log log = LogFactory.getLog(this.getClass());
//	@Autowired
//	protected Validator validator;

	

}
