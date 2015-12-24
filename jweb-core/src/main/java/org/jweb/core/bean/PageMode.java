package org.jweb.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页模型
 * @author wupan
 *
 */

public class PageMode<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7390781402284060611L;
	
	/**总页数*/
	private int pageCount;
	/**当前页码*/
	private int pageNo;
	/**每页显示条数*/
	private int pageSize;
	/**总记录数*/
	private int total;
	/**结果集*/
	private List<T> result;
	
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
}