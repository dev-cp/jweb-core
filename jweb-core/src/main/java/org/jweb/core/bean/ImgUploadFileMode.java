package org.jweb.core.bean;

public class ImgUploadFileMode extends UploadFileMode{

	/**相对比例*/
	private String scale;
	/**绝对尺寸*/
	private String imgSize;
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getImgSize() {
		return imgSize;
	}
	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}
	
	
}
