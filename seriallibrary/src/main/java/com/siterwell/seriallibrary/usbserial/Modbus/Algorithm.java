package com.siterwell.seriallibrary.usbserial.Modbus;

import com.siterwell.seriallibrary.usbserial.util.HexDump;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * 转化为一位小数,若无小数则取整
 * @author Administrator
 *
 */
public class Algorithm {
	



   public static String toOneDecimal(String x){
	   
	   String a="";
	   
	   
	   if(x.indexOf(".")==-1){
		   return x;
	   }
	   else
	   {
		  DecimalFormat dsa = new DecimalFormat("0.0"); 
		  a=dsa.format(Double.valueOf(x));		  
		  return a;
	   }
	   
	  
   }
   
   public static int daysBetween(String smdate,String bdate) throws ParseException{  
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
       Calendar cal = Calendar.getInstance();    
       cal.setTime(sdf.parse(smdate));    
       long time1 = cal.getTimeInMillis();                 
       cal.setTime(sdf.parse(bdate));    
       long time2 = cal.getTimeInMillis();         
       long between_days=(time2-time1)/(1000*3600*24);  
           
      return Integer.parseInt(String.valueOf(between_days));     
   } 
  
   //生成圆形数组
  public static int[] circle(int r, int offsetX, int offsetY) { 
	    int[] ring = new int[8 * r + 4]; 
	    // x^2 + y^2 = r^2 
	    for (int i = 0; i < 2 * r + 1; i++) { 
	      int x = i - r; 
	      int y = (int) Math.sqrt(r * r - x * x); 
	      ring[2 * i] = offsetX + x; 
	      ring[2 * i + 1] = offsetY + y; 
	      ring[8 * r - 2 * i - 2] = offsetX + x; 
	      ring[8 * r - 2 * i - 1] = offsetY - y; 
	    } 
	    return ring; 
	  }

  
  /**
   * @说明 CRC16MODBUS校验算法
   * @param bufData
   * @param buflen
   * @return
   */
  public static byte[] get_crc16 (byte[] bufData, int buflen)  
  {  
   byte[] re = new byte[2];
      int CRC = 0x0000ffff;  
      int POLYNOMIAL = 0x0000a001;  
      int i, j;  



      for (i = 0; i < buflen; i++)  
      {  
          CRC ^= ((int)bufData[i] & 0x000000ff);  
          for (j = 0; j < 8; j++)  
          {  
              if ((CRC & 0x00000001) != 0)  
              {  
                  CRC >>= 1;  
                  CRC ^= POLYNOMIAL;  
              }  
              else  
              {  
                  CRC >>= 1;  
              }  
          }  
          //System.out.println(Integer.toHexString(CRC));  
      }  
        
      System.out.println(Integer.toHexString(CRC));  
      re[0] = (byte)(CRC & 0x00ff);  
      re[1] = (byte)(CRC >> 8);  

      return re;  
  }
  
  public static byte[] toByteArray(int iSource, int iArrayLen) {
	    byte[] bLocalArr = new byte[iArrayLen];
	    for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
	        bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
	    }
	    return bLocalArr;
	}

//将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
  public static int toInt(byte[] bRefArr) {
      int iOutcome = 0;
      byte bLoop;

      for (int i = 0; i < bRefArr.length; i++) {
          bLoop = bRefArr[i];
          iOutcome += (bLoop & 0xFF) << (8 * i);
      }
      return iOutcome;
  }

  public static void main(String args[]){
//     byte[] ds = toByteArray(1322313132, 3);
//
//     for(int i=0;i<ds.length;i++){
//         System.out.println(ds[i]);
//     }
      ArrayList<Byte> ds = new ArrayList<>();
      byte ds4 = -84;
      Byte ds2 = new Byte(String.valueOf(ds4));
      byte ds3 = ds2.byteValue();
      System.out.println(ds3);
  }
}
