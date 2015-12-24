package org.jweb.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class RandomUtil {

	private static final Logger logger = Logger
			.getLogger(RandomUtil.class);
	
	private static List<String> randomCharList = new ArrayList<String>();
	static{
		String[] strArr = new String[]{
				"a","b","c","d","e","f",
				"g","h","i","j","k","l",
				"m","n","o","p","q","r",
				"s","t","u","v","w","x",
				"y","z","1","2","3","4",
				"5","6","7","8","9","0"
				};
		
		Collections.addAll(randomCharList, strArr);
	}
	
	public static String generateRandom(int bitNum){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < bitNum ; i++){
			int randomNum = (int)Math.floor(Math.random()*36);//取整函数只取整数，不会做四舍五入操作，random函数不会返回1.0,但会返回0.0
			if(randomNum >= 36){
				randomNum = 35;
				logger.warn("generateRandom() method need to optimize,because the random may generate number larger than 35,however,the max index of the seed list 'randomCharList' is 35.");
			}
			sb.append(randomCharList.get(randomNum));
		}
		
		return sb.toString();
	}
}
