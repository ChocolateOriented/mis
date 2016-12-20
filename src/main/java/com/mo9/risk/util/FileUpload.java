package com.mo9.risk.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	//public static final String FILE_PATH = "/uploadImage/";  
	  
    //文件上传  
    public static String uploadFile(MultipartFile file,String fileName,String uploadPath,HttpServletRequest request) throws IOException {  
//        String fileName = new Date().getTime()+"RemittanceMesssage"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')); 
        File tempFile = new File(uploadPath,String.valueOf(fileName));  
        if (!tempFile.getParentFile().exists()) {  
            tempFile.getParentFile().mkdir();  
        }  
        if (!tempFile.exists()) {  
            tempFile.createNewFile();  
        }  
        file.transferTo(tempFile);  
        return "downloadImage?fileName=" + tempFile.getName()+"&filePath="+uploadPath;  
    }  
  
    public static File getFile(String fileName,String filePath) {  
        return new File(filePath, fileName);  
    }  
}
