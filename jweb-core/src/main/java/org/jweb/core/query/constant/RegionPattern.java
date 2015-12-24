package org.jweb.core.query.constant;

/**
 * 区间模式枚举常量，@Begin、@End注解使用该枚举来表名取值范围是否包含起始值
 * @author wupan
 *
 */
public enum RegionPattern {

	//开区间，闭区间，与数学坐标中的开区间和闭区间概念一样，指一个坐标点包含还是不包含
	OPENREGION("OpenRegion"),CLOSEREGION("CloseRegion");
	
	private String regionPattern;
	
	private RegionPattern(String regionPattern){
		this.regionPattern = regionPattern;
	}
	
	public String toString(){
		return this.regionPattern;
	}
}
