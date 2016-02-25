package org.agle4j.framework.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 将图片转换为Base64<br>
 * 将base64编码字符串解码成img图片
 * 
 * @author hyx
 * 
 */
public final class ImageUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class) ;
	
	// JGP格式
    public static final String JPG = "jpeg";
    // GIF格式
    public static final String GIF = "gif";
    // PNG格式
    public static final String PNG = "png";
    // BMP格式
    public static final String BMP = "bmp";
	
	/**
	 * 将图片转换成Base64编码
	 * 
	 * @param imgFile
	 * @return
	 */
	public static String getImgBase64(String imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;

		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data));
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean generateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) { // 图像数据为空
			return false;
		}
		try {
			byte[] b = Base64.decodeBase64(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			File imgFile = createFile(imgFilePath) ;
			
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFile);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * image 格式 转换
	 * @param srcImgFile
	 * @param format
	 * @param tarImgFile
	 */
	public static void converter(File srcImgFile, String format,File tarImgFile) {
		try {
			BufferedImage image = ImageIO.read(srcImgFile) ;
			ImageIO.write(image, format, tarImgFile) ;
		} catch (Exception e) {
			LOG.error("converter img file failure",e);
			throw new RuntimeException(e) ;
		}
	}
	
	private static File createFile(String filePath) {
		File file ;
		try {
			file = new File(filePath) ;
			File parentDir = file.getParentFile() ;
			if (!parentDir.exists()) {
				FileUtils.forceMkdir(parentDir) ;
			}
		} catch (Exception e) {
			LOG.error("create file failure", e);
			throw new RuntimeException(e) ;
		}
		return file ;
	}
	
	public static void main(String[] args) {
		File srcFile = new File("D:\\Desert.jpg") ;
		File tarFile = new File("D:\\tarDesert.jpg") ;
		converter(srcFile, BMP, tarFile);
	}
}
