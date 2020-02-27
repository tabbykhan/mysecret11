package com.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.PermissionChecker;

import com.app.ui.MyApplication;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by bitu on 31/8/17.
 */

public class DeviceUtil {

    public static String getUniqueDeviceId(Context context) {

        String androidId=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(androidId!=null && androidId.trim().length()>0){
            return androidId;
        }

        String m_szDevIDShort = "35"
                + (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);


        String serial = getSerialNumber();

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
            serialNumber = (String) get.invoke(c, "gsm.sn1");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");

            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;


            if (serialNumber.equals("") || serialNumber.equalsIgnoreCase(Build.UNKNOWN)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (PermissionChecker.checkSelfPermission(MyApplication.instance,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        serialNumber = Build.getSerial();
                    } else {
                        serialNumber = "serial";
                    }
                }
            }

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = "serial";
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = "serial";
        }
        return serialNumber;
    }

}
