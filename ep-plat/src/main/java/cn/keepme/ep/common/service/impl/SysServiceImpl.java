package cn.keepme.ep.common.service.impl;

import org.jweb.core.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.keepme.ep.common.service.SysServiceI;

@Service("sysService")
@Transactional
public class SysServiceImpl extends CommonServiceImpl implements SysServiceI{

}
