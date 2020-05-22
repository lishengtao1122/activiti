package com.lesent.activiti.business.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


public class OSSClientUtilLsdk {
	private static Logger logger = LoggerFactory.getLogger(OSSClientUtilLsdk.class);
	private String endpoint = "";
	private String accessKeyId = "";
	private String accessKeySecret = "";
	private String bucketName = "";//

	private OSSClient ossClient;

	public OSSClientUtilLsdk(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		this.bucketName = bucketName;
		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public String uploadByByteToOSS(byte[] bcyte, String fileName, String filedir) {
		String resultStr = null;
		Long fileSize = (long) bcyte.length;
		// 创建上传Object的Metadata
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(fileSize);
		// 指定该Object被下载时的网页的缓存行为
		metadata.setCacheControl("no-cache");
		// 指定该Object下设置Header
		metadata.setHeader("Pragma", "no-cache");
		// 指定该Object被下载时的内容编码格式
		metadata.setContentEncoding("utf-8");
		// 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
		// 如果没有扩展名则填默认值application/octet-stream
		metadata.setContentType(getContentType(fileName));
		// 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
		String filePath = filedir + fileName;
		// 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
		metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
		ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(bcyte),
				metadata);
		// -------------------
		endpoint = endpoint.replace("-internal", "");
		StringBuilder sb = new StringBuilder(endpoint + "/" + filePath);// 构造一个StringBuilder对象
		sb.insert(7, bucketName + ".");// 在指定的位置插入指定的字符串
		resultStr = sb.toString();
		// -------------------

		return resultStr;
	}
	/**
	 * 上传图片至OSS
	 * @return String 返回的唯一MD5数字签名
	 */
	public String uploadObject2OSS(InputStream is, String fileName, String filedir) {
		String resultStr = null;
		try {
			// 创建上传Object的Metadata
			ObjectMetadata metadata = new ObjectMetadata();
			// 上传的文件的长度
			metadata.setContentLength(is.available());
			// 指定该Object被下载时的网页的缓存行为
			metadata.setCacheControl("no-cache");
			// 指定该Object下设置Header
			metadata.setHeader("Pragma", "no-cache");
			// 指定该Object被下载时的内容编码格式
			metadata.setContentEncoding("utf-8");
			// 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
			// 如果没有扩展名则填默认值application/octet-stream
			metadata.setContentType(getContentType(fileName));
			// 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
			String filePath = filedir + fileName;
			metadata.setContentDisposition("filename/filesize=" + (filePath));
			// 上传文件 (上传文件流的形式)
			PutObjectResult putResult = ossClient.putObject(bucketName, filePath, is, metadata);
			// 解析结果
			// PutObjectResult putResult = ossClient.putObject(bucketName, filePath, is, metadata);
			resultStr = putResult.getETag();
			StringBuilder sb = new StringBuilder(endpoint + "/" + filePath);// 构造一个StringBuilder对象
			endpoint = endpoint.replace("-internal", "");
			sb.insert(7, bucketName + ".");// 在指定的位置插入指定的字符串
			resultStr = sb.toString();
			// 关闭OSSClient。
			//
		} catch (Exception e) {
			logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
		}
		return resultStr;
	}
	/**
	 * 上传到OSS服务器 如果同名文件会覆盖服务器上的
	 * @param instream 文件流
	 * @param fileName 文件名称 包括后缀名
	 * @return 出错返回"" ,唯一MD5数字签名
	 */
	public String uploadFile2OSS(InputStream instream, String fileName, String filedir) {
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			String filePath = filedir + fileName;
			// 上传文件
			ossClient.putObject(bucketName, filePath, instream, objectMetadata);
			StringBuilder sb = new StringBuilder(endpoint + "/" + filePath);// 构造一个StringBuilder对象
			sb.insert(7, bucketName + ".");
			ret = sb.toString();
			// ret = putResult.getETag();
			// 关闭OSSClient。

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 通过文件名判断并获取OSS服务文件上传时文件的contentType
	 * @param fileName 文件名
	 * @return 文件的contentType
	 */
	public static String getContentType(String fileName) {
		logger.info("getContentType:" + fileName);
		// 文件的后缀名
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));
		if (".bmp".equalsIgnoreCase(fileExtension)) {
			return "image/bmp";
		}
		if (".gif".equalsIgnoreCase(fileExtension)) {
			return "image/gif";
		}
		if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
				|| ".png".equalsIgnoreCase(fileExtension)) {
			return "image/jpeg";
		}
		if (".html".equalsIgnoreCase(fileExtension)) {
			return "text/html";
		}
		if (".txt".equalsIgnoreCase(fileExtension)) {
			return "text/plain";
		}
		if (".vsd".equalsIgnoreCase(fileExtension)) {
			return "application/vnd.visio";
		}
		if (".ppt".equalsIgnoreCase(fileExtension) || ".pptx".equalsIgnoreCase(fileExtension)) {
			return "application/vnd.ms-powerpoint";
		}
		if (".doc".equalsIgnoreCase(fileExtension) || ".docx".equalsIgnoreCase(fileExtension)) {
			return "application/msword";
		}
		if (".xml".equalsIgnoreCase(fileExtension)) {
			return "text/xml";
		}
		if (".pdf".equalsIgnoreCase(fileExtension)) {
			return "application/pdf";
		}


		//音频
		if(fileExtension.equalsIgnoreCase(".mp3")){
			return "audio/mp3";
		}
		if(fileExtension.equalsIgnoreCase(".wav")){
			return "audio/wav";
		}
		if(fileExtension.equalsIgnoreCase(".wma")){
			return "audio/x-ms-wma";
		}
		if(fileExtension.equalsIgnoreCase(".ra")){
			return "audio/vnd.rn-realaudio";
		}
		//视频
		if(fileExtension.equalsIgnoreCase(".mp4")){
			return "video/mpeg4";
		}
		if(fileExtension.equalsIgnoreCase(".avi")){
			return "video/avi";
		}
		if(fileExtension.equalsIgnoreCase(".rmvb")){
			return "application/vnd.rn-realmedia-vbr";
		}
		if(fileExtension.equalsIgnoreCase(".rm")){
			return "application/vnd.rn-realmedia";
		}
		// 默认返回类型
		return "image/jpeg";
	}

	/**
	 * 获得图片路径
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl, String filedir) {
		System.out.println(fileUrl);
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("/");
			return this.getUrl(filedir + split[split.length - 1]);
		}
		return null;
	}
	/**
	 * 获得url链接
	 * @param key
	 * @return
	 */
	public String getUrl(String key) {
		// 设置URL过期时间为10年 3600l* 1000*24*365*10
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}

	/**
	 * @Title: getByteByFileUrl
	 * @Description: 根据文件路径获取Byte流
	 * @param fileName
	 * @return
	 * @return: byte[]
	 */
	public byte[] getByteByFileUrl(String fileName, String filedir) {
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(bucketName, filedir + fileName);
		InputStream inputStream = ossObject.getObjectContent();
		byte[] input2byte = null;
		return input2byte;
	}
	/**
	 * @Title: getInputStreamByFileUrl
	 * @Description: 根据文件路径获取InputStream流
	 * @param fileName
	 * @return
	 * @return: InputStream
	 */
	public InputStream getInputStreamByFileUrl(String fileName, String filedir) {
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(bucketName, filedir + fileName);
		return ossObject.getObjectContent();
	}

	/**
	 * 根据文件名获取OSS私有文件
	 * @param fileName
	 * @return
	 */
	public String getOssSignUrl(String fileName , String filedir){
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		// 设置URL过期时间为1小时。
		Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
		// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
		URL url = ossClient.generatePresignedUrl(bucketName, filedir + fileName, expiration);
		logger.info("签名URL:"+url);
		ossClient.shutdown();
		return url.toString();
	}

	/**
	 * 下载文件
	 * @param filePath
	 */
	public InputStream downloadFile(String filePath) {
		final String aliUrl="http://lsdk.oss-cn-hangzhou.aliyuncs.com/";
		filePath=filePath.replaceAll(aliUrl,"");
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(bucketName, filePath);
		return ossObject.getObjectContent();
	}

    /**
	 * 删除文件
	 * @param filePath
	 * @throws IOException
	 */
	public void deleteFile(String filePath){
		final String aliUrl="http://lsdk.oss-cn-hangzhou.aliyuncs.com/";
		filePath=filePath.replaceAll(aliUrl,"");
		ossClient.deleteObject(bucketName,filePath);
	}

	public boolean existsFile(String filePath){
		final String aliUrl="http://lsdk.oss-cn-hangzhou.aliyuncs.com/";
		filePath=filePath.replaceAll(aliUrl,"");
		return ossClient.doesObjectExist(bucketName,filePath);
	}

}
