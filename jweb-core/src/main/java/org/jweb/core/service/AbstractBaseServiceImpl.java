package org.jweb.core.service;

import java.util.ArrayList;
import java.util.List;

import org.jweb.core.bean.BaseVO;
import org.jweb.core.bean.SearchForm;
import org.jweb.core.util.StringUtil;

/**
 * 抽象父类service实现，提供一些通用方法和工具方法
 * @author administ
 *
 */
public abstract class AbstractBaseServiceImpl {

	/**
	 * 用于list场景的entity到vo转换的私有方法
	 * @return
	 */
	private <T> List<T> entity2vo4list(Class<T> targetTypeClazz,List poiList){
		List<T> voList = new ArrayList<T>();
		// entity转vo
		for (Object obj : poiList) {
			try {
				T vo = targetTypeClazz.newInstance();
				
				if(vo instanceof BaseVO){
					BaseVO targetVO = (BaseVO)vo;
					targetVO.copyEntity(obj);
					voList.add((T)targetVO);
					
				}
			} catch (InstantiationException   e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);//如果在转换时发生异常，则抛出为运行时异常，该处抛异常主要用于测试，如果经过完全测试再上线，代码是不会出现异常的。
			}  catch (IllegalAccessException   e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);//如果在转换时发生异常，则抛出为运行时异常，该处抛异常主要用于测试，如果经过完全测试再上线，代码是不会出现异常的。
		}
			
			
		}

		return voList;
	}
	
	private int[] getPageNoAndPageSize(SearchForm searchForm){
		int pageNo = StringUtil.toInt(searchForm.getPageNo());
		int pageSize = StringUtil.toInt(searchForm.getPageSize());
		if(pageNo == 0){
			pageNo = 1;
		}
		if(pageSize == 0){
			pageSize = 10;
		}
		
		return new int[]{pageNo,pageSize};
	}
}
