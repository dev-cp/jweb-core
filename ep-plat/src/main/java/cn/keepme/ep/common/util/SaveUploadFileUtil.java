package cn.keepme.ep.common.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jweb.core.bean.UploadFileMode;
import org.jweb.core.util.DateUtil;
import org.jweb.core.util.FileUtil;
import org.jweb.core.util.StringUtil;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用于scs上传文件的保存操作
 * @author administ
 *
 */
public class SaveUploadFileUtil {

	public static UploadFileMode saveTempImg(HttpServletRequest request,
			MultipartFile mf,String relativeSavePath) throws IOException {

//		Map<String, String> paramMap = new HashMap<String, String>();
		UploadFileMode uploadFileMode = new UploadFileMode();

		String tempDirPath = relativeSavePath;
		

		// 文件数据库保存路径
		String realPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ tempDirPath;

		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdirs();// 创建根目录
		}
		
		//用日期分别存放临时文件，以便后期采用定时任务清理临时文件限定清理范围
		String dateDirName = DateUtil.getDataString(DateUtil.yyyyMMdd);
		tempDirPath = tempDirPath + dateDirName + "/";
		realPath += dateDirName + "/";
		file = new File(realPath);
		if (!file.exists()) {
			file.mkdir();// 创建文件时间子目录
		}
		
		uploadFileMode.setTempDirPath(tempDirPath);// 将存放临时文件的目录放入返回mode中
		uploadFileMode.setTempDirRealPath(realPath);
		
		
		// 设置文件上传路径
		String noextfilename = "";// 不带扩展名

		String fileName = mf.getOriginalFilename();// 获取文件名
		String extend = FileUtil.getExtend(fileName);// 获取文件扩展名
		uploadFileMode.setFileExtend(extend);
		
		// 重命名
		noextfilename = DateUtil.getDataString(DateUtil.yyyymmddhhmmss)
				+ StringUtil.random(8);// 自定义文件名称
		String myfilename = noextfilename + "." + extend;// 自定义文件名称

		String savePath = realPath + myfilename;// 文件保存全路径

		uploadFileMode.setFileName(myfilename);
		uploadFileMode.setFileFullName(savePath);

		File savefile = new File(savePath);

		// 文件拷贝到指定硬盘目录
		FileCopyUtils.copy(mf.getBytes(), savefile);

		return uploadFileMode;
	}
}
