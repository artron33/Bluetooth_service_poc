package com.valotec.neurocomm_mobile_app.BytesHelper;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteHelper {
    private static final String TAG = ByteHelper.class.getCanonicalName();

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static int extractUInt8(byte[] data) {
        if (data == null) {
            Log.e(TAG, "null byte array");
            return 0;
        }

        if (data.length < 1) {
            Log.e(TAG, "byte array to short: " + data.length);
            return 0;
        }

        return data[0];
    }

    public static int extractUInt16(byte[] data) {
        if (data == null) {
            Log.e(TAG, "null byte array");
            return 0;
        }

        if (data.length < 2) {
            Log.e(TAG, "byte array to short: " + data.length);
            return 0;
        }

        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return (bb.getShort() & 0xffff);
    }

    public static int extractSInt16(byte[] data) {
        if (data == null) {
            Log.e(TAG, "null byte array");
            return 0;
        }

        if (data.length < 2) {
            Log.e(TAG, "byte array to short: " + data.length);
            return 0;
        }

        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return (bb.getShort());
    }


    public static long byteUnsignerToLong(byte[] payload) {
        long l = 0;
        l |= payload[3] & 0xFF;
        l <<= 8;
        l |= payload[2] & 0xFF;
        l <<= 8;
        l |= payload[1] & 0xFF;
        l <<= 8;
        l |= payload[0] & 0xFF;

        return l;
    }


    public static long extractUInt32(byte[] data) {
        if (data == null) {
            Log.e(TAG, "null byte array");
            return 0;
        }

        if (data.length < 4) {
            Log.e(TAG, "byte array to short: " + data.length);
            return 0;
        }

        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return (bb.getInt() & 0xffffffff);
    }
    public static long getUInt32___ALT(byte[] bytes) throws EOFException, IOException {
        long value =
                ((bytes[0] & 0xFF) <<  0) |
                        ((bytes[1] & 0xFF) <<  8) |
                        ((bytes[2] & 0xFF) << 16) |
                        ((bytes[3] & 0xFF) << 24);
        return value;
    }

    public static float extractFloat(byte[] data) {
        if (data == null) {
            Log.e(TAG, "null byte array");
            return 0;
        }

        if (data.length < 2) {
            Log.e(TAG, "byte array to short: " + data.length);
            return 0;
        }

        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return (bb.getFloat());
    }

    public static String extractString(byte[] data) {
        String res = "";
        try {
            res = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encoding error: " + e.toString());
        }

        return res;
    }


    //====================

}
