package com.valotec.neurocomm_mobile_app.source.ble.generic;

import com.valotec.neurocomm_mobile_app.source.ble.BleController;

import java.io.Serializable;
import java.util.ArrayList;

public class BleSingleton  implements Serializable {

    private static final long serialVersionUID = -621464692201738125L;
    private BleController mBleController = new BleController();
    private BleSingleton(){}

    private static class SingletonHelper{
        private static final BleSingleton instance = new BleSingleton();
    }

    public static BleSingleton getInstance(){
        return SingletonHelper.instance;
    }

    public BleController initBleController(BleConnectionStatus bleState) {
        if (mBleController == null) {
            //mBleController = new BleController(new ArrayList<>(), bleState);
        }
        return mBleController;
    }

    public BleController getBleController() {
        return mBleController;
    }

}
