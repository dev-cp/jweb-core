package org.jweb.core.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

/**
 * java.util.Date类型转换器，基于apache commons beanutils中类型转换编写的
 * 该类型转换器最终需要注册到apache的ConvertUtils工具类才有效，一般情况，在应用程序
 * 启动时就进行注册操作，通常，我们在servlet上下文监听器中做这样的注册操作
 * @author wupan
 *
 */
public class UtilDateConverter implements Converter{

	private static final String[][] PARR = new String[][]{
			  {
				  "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}",//yyyy-MM-dd HH:mm:ss
				  "yyyy-MM-dd HH:mm:ss"
			  },
			  {
				  "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}",//yyyy-MM-dd HH:mm
				  "yyyy-MM-dd HH:mm"
			  },
			  {
				  "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}",//yyyy-MM-dd hh
				  "yyyy-MM-dd HH"
			  },
			  {
				  "[0-9]{4}-[0-9]{2}-[0-9]{2}",//yyyy-MM-dd
				  "yyyy-MM-dd"
			  },
			  {
				  "[0-9]{4}-[0-9]{2}",//yyyy-MM
				  "yyyy-MM"
			  },
			  {
				  "[0-9]{4}",//yyyy
				  "yyyy"
			  },
			  {
				  "[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}",//yyyy/MM/dd hh:mm:ss
				  "yyyy/MM/dd HH:mm:ss"
			  },
			  {
				  "[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}",//yyyy/MM/dd hh:mm
				  "yyyy/MM/dd HH:mm"
			  },
			  {
				  "[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}",//yyyy/MM/dd hh
				  "yyyy/MM/dd HH"
			  },
			  {
				  "[0-9]{4}/[0-9]{2}/[0-9]{2}",//yyyy/MM/dd
				  "yyyy/MM/dd"
			  },
			  {
				  "[0-9]{4}\\[0-9]{2}\\[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}",//yyyy\MM\dd hh:mm:ss
				  "yyyy\\MM\\dd HH:mm:ss"
			  },
			  {
				  "[0-9]{4}\\[0-9]{2}\\[0-9]{2} [0-9]{2}:[0-9]{2}",//yyyy\MM\dd hh:mm
				  "yyyy\\MM\\dd HH:mm"
			  },
			  {
				  "[0-9]{4}\\[0-9]{2}\\[]{2} [0-9]{2}",//yyyy\MM\dd hh
				  "yyyy\\MM\\dd HH"
			  },
			  {
				  "[0-9]{4}\\[0-9]{2}\\[0-9]{2}",//yyyy\MM\dd
				  "yyyy\\MM\\dd"
			  },
			  {
				  "[0-9]{4}\\[0-9]{2}",//yyyy\MM
				  "yyyy\\MM\\dd"
			  }
			  
	  };
	  
      public Object convert(Class type, Object value) {
          if (value == null) {
              return value;
          }
          if (value instanceof Date) {
              return value;
          }
          if (value instanceof String) {
        	  //根据时间字串格式判断格式化模板
        	  String p = getTimePatternFromValueStr((String)value);
              try {
            	  SimpleDateFormat format = new SimpleDateFormat(p);
                  return format.parse((String)value);
              } catch (ParseException e) {
                  e.printStackTrace();
              }
          }
          return null;
      }
      
      private String getTimePatternFromValueStr(String time){
    	 
    	 
    	  
    	  
    	  if(time != null && !"".equals(time)){
    		  for(int i = 0 ; i < PARR.length ; i++){
    			  String[] p = PARR[i];
    			  if(time.matches(p[0])){
    				  return p[1];
    			  }
    		  }
    	  }
    	  
    	  return PARR[0][1];
      }
      
      
}
