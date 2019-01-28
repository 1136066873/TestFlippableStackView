package com.haier.demo.testflippablestackview;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by 01438511 on 2019/1/10.
 */

public class Util {

    public static final String TAG = "Util";

    public static void copyDataFromAssetsToSDcard(Context context,String pathLocal,String filename,
                                                  CallBackWhenCopyDataFromAssetsToSDcard callback) {
        try {
            InputStream myInput;
            File file = new File(pathLocal);
            if (!file.exists()){
                file.mkdir();
            }
            File targetFile = new File(pathLocal + filename);
            OutputStream myOutput = new FileOutputStream(targetFile);
            myInput = context.getAssets().open(filename);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
            callback.onCopyDataSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Exception ->" + e.getLocalizedMessage());
            callback.onCopyDataFailed(e.getLocalizedMessage());
        }
    }

     /**
      * 解压缩功能.
      * 将zipFile文件解压到folderPath目录下.
      *
      * @throws Exception
      */
     public static void upZipFile(File zipFile, String folderPath,CallBackWhenUpZipFile callback){

         try {
             ZipFile zfile=new ZipFile(zipFile);
             Enumeration zList=zfile.entries();
             ZipEntry ze=null;
             byte[] buf=new byte[1024];
             while(zList.hasMoreElements()){
                 ze=(ZipEntry)zList.nextElement();
                 if(ze.isDirectory()){
                     Log.d("upZipFile", "ze.getName() = "+ze.getName());
                     String dirstr = folderPath + ze.getName();
                     //dirstr.trim();
                     dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                     Log.d("upZipFile", "str = "+dirstr);
                     File f=new File(dirstr);
                     f.mkdir();
                     continue;
                 }
                 Log.d("upZipFile", "ze.getName() = "+ze.getName());
                 OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                 InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                 int readLen=0;
                 while ((readLen=is.read(buf, 0, 1024))!=-1) {
                     os.write(buf, 0, readLen);
                 }
                 is.close();
                 os.close();
             }
             zfile.close();
             Log.d("upZipFile", "finishssssssssssssssssssss");
             callback.onUpZipFileSuccessful();
         } catch (IOException e) {
             e.printStackTrace();
             Log.e(TAG,"IOException ->" + e.getLocalizedMessage());
             callback.onUpZipFileFailed(e.getLocalizedMessage());
         }
     }


    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs=absFileName.split("/");
        String lastDir=baseDir;
        if(dirs.length>1) {
            for (int i = 0; i < dirs.length-1;i++) {
                lastDir +=(dirs[i]+"/");
                File dir =new File(lastDir);
                if(!dir.exists()) {
                    dir.mkdirs();
                    Log.d("getRealFileName", "create dir = "+(lastDir+"/"+dirs[i]));
                }
            }
            File ret = new File(lastDir,dirs[dirs.length-1]);
            Log.d("upZipFile", "2ret = "+ret);
            return ret;
        } else {
            return new File(baseDir,absFileName);
        }
    }
}
