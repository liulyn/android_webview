package com.cloud.tao.util;

import android.text.TextUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import com.cloud.tao.framwork.Logger;

/**
 * Created by janecer on 2014/12/9.
 */
public class FileUtil {

    /**
     * 通过递归调用删除一个文件夹及下面的所有文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
            }
        }
    }

    /**
     * 将字符写入到指定的文件下面
     * @param content
     * @param path
     */
    public static void writeLog2Sdcard(String content ,String path) {
        if(TextUtils.isEmpty(path)){
            return;
        }

        File file = new File(path) ;
        if(!file.getParentFile().exists()){
            file.mkdirs() ;
        }
        Logger.msg(content , " path : " + path );
        FileWriter fileWriter = null ;
        try {
            if(!file.exists()){
                file.createNewFile() ;
            }
            fileWriter = new FileWriter(file,true) ;
            fileWriter.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(null != fileWriter){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将异常信息写入sdcard
     * @param ex
     * @param path
     */
    public static void writeException2Sdcard(Exception ex,String path) {
        if(null == ex) {
            return ;
        }
        PrintWriter printWriter = null;
        FileWriter fileWriter = null ;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            }
            file.createNewFile();
            fileWriter = new FileWriter(file,true) ;
            printWriter = new PrintWriter(fileWriter) ;
            ex.printStackTrace(printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != printWriter ){
                printWriter.close();
            }
            if(null != fileWriter){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文件转化为字节数组
     * @param path
     * @return
     */
    public static byte[]  file2Bytes(String path) {
        File file = new File(path) ;
        if(!file.exists()) {
            return null ;
        }
        try {
            FileInputStream is  = new FileInputStream(file) ;
            return is2Bytes(is) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * @param is
     * @return
     */
    public static byte[] is2Bytes(InputStream is) {
        ByteArrayOutputStream bos = null;
        try{
            bos = new ByteArrayOutputStream() ;
            int len = 0 ;
            byte[] buffer = new byte[1024] ;
            while ((len = is.read(buffer , 0 ,1024)) > 0){
                bos.write(buffer,0 ,len);
            }
            bos.flush();
            return bos.toByteArray() ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null ;
    }

}
