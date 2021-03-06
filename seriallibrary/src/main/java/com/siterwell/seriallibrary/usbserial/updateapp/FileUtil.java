
package com.siterwell.seriallibrary.usbserial.updateapp;

import android.os.Environment;


import com.siterwell.seriallibrary.usbserial.util.FunPath;

import java.io.File;
import java.io.IOException;

/**
 * ClassName:FileUtil
 * 作者：Henry on 2017/3/30 16:26
 * 邮箱：xuejunju_4595@qq.com
 * 描述:设置自动更新下载apk的位置
 */
public class FileUtil {
	
	public static File updateDir = null;
	public static File updateFile = null;

	
	public static boolean isCreateFileSucess;


	public static void createFile(String app_name) {
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isCreateFileSucess = true;
			
			updateDir = new File(FunPath.PATH_DEVICE_UPDATE_FILE_PATH);
			updateFile = new File(updateDir + "/" + app_name);

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
}