package com.valotec.neurocomm_mobile_app.source.ble.generic;

public interface BleConnectionStatus {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_FAILED = -1;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_NOTIFY = 4;
    public static final int MESSAGE_CHARACTERISTIC_CHANGE = 5;
    public static final int MESSAGE_DESCRIPTOR_WRITE = 6;

    public void onBleConnecting();
    public void onBleConnected();
    public void onBleDisconnected();
    public void onDescriptorWriteFinish(String charactUiid);

    public interface BleClose {
        public void onBleDisconnected();
    }
}
