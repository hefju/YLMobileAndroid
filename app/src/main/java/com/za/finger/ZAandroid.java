package com.za.finger;

public class ZAandroid
{
  public final int VendorId0 = 8457;
  public final int ProductId0 = 30264;
  public final int VendorId1 = 8201;
  public final int ProductId1 = 30264;

  public final int PS_OK = 0;
  public final int PS_COMM_ERR = 1;
  public final int PS_NO_FINGER = 2;
  public final int PS_GET_IMG_ERR = 3;
  public final int PS_FP_TOO_DRY = 4;
  public final int PS_FP_TOO_WET = 5;
  public final int PS_FP_DISORDER = 6;
  public final int PS_LITTLE_FEATURE = 7;
  public final int PS_NOT_MATCH = 8;
  public final int PS_NOT_SEARCHED = 9;
  public final int PS_MERGE_ERR = 10;
  public final int PS_ADDRESS_OVER = 11;
  public final int PS_READ_ERR = 12;
  public final int PS_UP_TEMP_ERR = 13;
  public final int PS_RECV_ERR = 14;
  public final int PS_UP_IMG_ERR = 15;
  public final int PS_DEL_TEMP_ERR = 16;
  public final int PS_CLEAR_TEMP_ERR = 17;
  public final int PS_SLEEP_ERR = 18;
  public final int PS_INVALID_PASSWORD = 19;
  public final int PS_RESET_ERR = 20;
  public final int PS_INVALID_IMAGE = 21;
  public final int PS_HANGOVER_UNREMOVE = 23;

  public final int CHAR_BUFFER_A = 1;
  public final int CHAR_BUFFER_B = 2;
  public final int MODEL_BUFFER = 3;

  static
  {
    System.loadLibrary("ZAandroid");
  }

  public native String init();

  public native int card_power_on();

  public native int card_power_off(int paramInt1, int paramInt2);

  public native int finger_power_on();

  public native int finger_power_off();

  public native int ZAZOpenDeviceEx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

  public native int ZAZSetImageSize(int paramInt);

  public native int ZAZCloseDeviceEx();

  public native int ZAZGetImage(int paramInt);

  public native int ZAZGenChar(int paramInt1, int paramInt2);

  public native int ZAZMatch(int paramInt, int[] paramArrayOfInt);

  public native int ZAZSearch(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt);

  public native int ZAZRegModule(int paramInt);

  public native int ZAZStoreChar(int paramInt1, int paramInt2, int paramInt3);

  public native int ZAZLoadChar(int paramInt1, int paramInt2, int paramInt3);

  public native int ZAZSetCharLen(int paramInt);

  public native int ZAZUpChar(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt);

  public native int ZAZDownChar(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);

  public native int ZAZUpImage(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

  public native int ZAZDownImage(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

  public native int ZAZImgData2BMP(byte[] paramArrayOfByte, String paramString);

  public native int ZAZGetImgDataFromBMP(String paramString, byte[] paramArrayOfByte, int[] paramArrayOfInt);

  public native int ZAZDelChar(int paramInt1, int paramInt2, int paramInt3);

  public native int ZAZEmpty(int paramInt);

  public native int ZAZReadParTable(int paramInt, byte[] paramArrayOfByte);

  public native int ZAZHighSpeedSearch(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt);

  public native int ZAZTemplateNum(int paramInt, int[] paramArrayOfInt);

  public native int ZAZSetPwd(int paramInt, byte[] paramArrayOfByte);

  public native int ZAZVfyPwd(int paramInt, byte[] paramArrayOfByte);

  public native int ZAZReadInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public native int ZAZWriteInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public native int ZAZSetBaud(int paramInt1, int paramInt2);

  public native int ZAZSetSecurLevel(int paramInt1, int paramInt2);

  public native int ZAZSetPacketSize(int paramInt1, int paramInt2);

  public native int ZAZUpChar2File(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public native int ZAZDownCharFromFile(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public native int ZAZGetRandomData(int paramInt, byte[] paramArrayOfByte);

  public native int ZAZSetChipAddr(int paramInt, byte[] paramArrayOfByte);

  public native int ZAZBT_rev(byte[] paramArrayOfByte, int paramInt);
}