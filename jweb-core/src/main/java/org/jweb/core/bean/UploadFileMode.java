package org.jweb.core.bean;

/**
 * 上传文件模型，用于存放文件上传后的各种属性信息
 * @author wupan
 *
 */
public class UploadFileMode {

	 /**文件存放的临时目录路径"webpage/tisdweb/upload/temp/20140327/",相对路径*/
	private String tempDirPath;
	/**tempDirRealPath：c:/workspace/tisd_web/webpage/tisdweb/upload/temp/20140327/,绝对路径*/
	private String tempDirRealPath;
	/**fileExtend:文件扩展名，如jpg,png*/
	private String fileExtend;
	/**文件在临时目录中存放时的名称，如dog.jpg*/
	private String fileName;
	/**文件全路径名，如c:/workspace/tisd_web/webpage/tisdweb/upload/temp/20140327/dog.jpg*/
	private String fileFullName;
	/**文件存放在远程文件服务器上的临时url*/
	private String tempUrl;
	
	public String getTempDirPath() {
		return tempDirPath;
	}
	public void setTempDirPath(String tempDirPath) {
		this.tempDirPath = tempDirPath;
	}
	public String getTempDirRealPath() {
		return tempDirRealPath;
	}
	public void setTempDirRealPath(String tempDirRealPath) {
		this.tempDirRealPath = tempDirRealPath;
	}
	public String getFileExtend() {
		return fileExtend;
	}
	public void setFileExtend(String fileExtend) {
		this.fileExtend = fileExtend;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileFullName() {
		return fileFullName;
	}
	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	public String getTempUrl() {
		return tempUrl;
	}
	public void setTempUrl(String tempUrl) {
		this.tempUrl = tempUrl;
	}
	
	

}
