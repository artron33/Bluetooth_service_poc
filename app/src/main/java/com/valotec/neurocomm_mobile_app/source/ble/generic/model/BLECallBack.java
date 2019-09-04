package com.valotec.neurocomm_mobile_app.source.ble.generic.model;

public interface BLECallBack {

    void onCharacteristicRead(byte[] array);
}
