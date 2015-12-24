/**
 * 
 */
package org.jweb.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xiekaiji
 * 
 */
public class ZipUtil {
	private static Log log = LogFactory.getLog(ZipUtil.class);

	/**
	 * 压缩单个文件或�?目录
	 * 
	 * @param zipFileName
	 *            to
	 * @param inputFile
	 *            from
	 * @throws Exception
	 */
	public static void zip(String zipFileName, String inputFile) {
		zip(zipFileName, new File(inputFile));
	}

	/**
	 * 传入指定多个文件集合进行压缩
	 * 
	 * @param zipFileName
	 * @param files
	 */
	public static void zip(String zipFileName, List<File> files) {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			for (File f : files) {
				zip(out, f, f.getName());
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(out);
		}
	}

	/**
	 * 定义压缩文件及目录为zip文件的方�?重写下面的zip方法
	 * 
	 * @param zipFileName
	 * @param inputFile
	 * @throws Exception
	 */
	private static void zip(String zipFileName, File inputFile) {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zip(out, inputFile, inputFile.getName());
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(out);
		}
	}

	/**
	 * 定义压缩文件及目录为zip文件的方�?
	 * 
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private static void zip(ZipOutputStream out, File f, String base) {
		// 判断File是否为目�?
		try {
			if (f.isDirectory()) {
				// 获取f目录下所有文件及目录,作为�?��File数组返回
				File[] fl = f.listFiles();
				out.putNextEntry(new ZipEntry(base + "/"));
				base = base.length() == 0 ? "" : base + "/";
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], base + fl[i].getName());
				}
			} else {
				out.putNextEntry(new ZipEntry(base));
				FileInputStream in = null;
				try {
					in = new FileInputStream(f);
					int b;
					while ((b = in.read()) != -1) {
						// System.out.println("File reading");
						out.write(b);
					}
				} catch (Exception e) {
					log.error(e, e);
				} finally {
					StreamUtil.close(in);
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * 定义解压缩zip文件的方�?
	 * 
	 * @param zipFileName
	 * @param outputDirectory
	 */
	public static List<File> unzip(String zipFileName, String outputDirectory) {
		ZipInputStream in = null;
		try {
			List<File> files = new ArrayList<File>();
			in = new ZipInputStream(new FileInputStream(zipFileName));
			// 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry�?
			// 当getNextEntry方法的返回�?为null，则代表ZipInputStream中没有下�?��ZipEntry�?
			// 输入流读取完成；
			ZipEntry z = in.getNextEntry();
			while (z != null) {
				// 创建以zip包文件名为目录名的根目录
				File f = new File(outputDirectory);
				f.mkdir();
				if (z.isDirectory()) {
					String name = z.getName();
					name = name.substring(0, name.length() - 1);
					// System.out.println("name " + name);
					f = new File(outputDirectory + File.separator + name);
					f.mkdir();
					// System.out.println("mkdir " + outputDirectory
					// + File.separator + name);
				} else {
					f = new File(outputDirectory + File.separator + z.getName());
					f.createNewFile();
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(f);
						int b;
						while ((b = in.read()) != -1) {
							out.write(b);
						}
						files.add(f);
					} catch (Exception e) {
						log.error(e, e);
					} finally {
						StreamUtil.close(out);
					}
				}
				// 读取下一个ZipEntry
				z = in.getNextEntry();
			}
			return files;
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			StreamUtil.close(in);
		}
		return null;
	}
}
