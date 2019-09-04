package com.valotec.neurocomm_mobile_app.source.ble.generic;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.Nullable;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class BleOperationExec {
    private final static String TAG = BleOperationExec.class.getSimpleName();

    private LinkedList<BleCharacteristic> mOperations = new LinkedList<>();

    public void addOperation(List<BleCharacteristic> mOperations) {
        this.mOperations.addAll(mOperations);
    }

    public void clearOperations(){
        mOperations.clear();
    }

    public void exec(BluetoothGatt bluetoothGatt){
        while (!mOperations.isEmpty()) {
            Log.e(TAG, "IMPLANT BLE operation is being executed mOperations.size ="+mOperations.size());

            BleCharacteristic operation = mOperations.getFirst();
            BluetoothGattCharacteristic characteristic = getCharacteristicFromService(
                    bluetoothGatt,
                    operation.getCharacteristic(),
                    operation.getService()
            );

            Log.e(TAG, "IMPLANT BLE operation is being executed::::  operation.type             ="+operation.getOperationType());
            Log.e(TAG, "IMPLANT BLE operation is being executed::::  operation.characteristic   ="+operation.getCharacteristic());

            mOperations.pop();
            switch (operation.getOperationType()) {
                case READ:
                    if (operation.read(characteristic, bluetoothGatt)) return;
                    else break;
                case NOTIFICATION:
                    if (operation.notification(characteristic, bluetoothGatt)) return;
                    else break;
                case WRITE:
                    if (operation.write(characteristic, bluetoothGatt)) return;
                    else break;

                    default:
                        Log.e(TAG, "IMPLANT BLE operation type vanished, ERROR");
                        break;

            }
        }
        Log.e(TAG, "IMPLANT BLE operation exec failed, popping");
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    @Nullable
    private BluetoothGattCharacteristic getCharacteristicFromService(BluetoothGatt bluetoothGatt,
                                                                     String characteristic_uuid,
                                                                     String service_uuid) {
        if (bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return null;
        }

        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(service_uuid));
        if (service == null) {
            Log.w(TAG, "No such service: " + service_uuid);
            return null;
        }

        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(UUID.fromString(characteristic_uuid));
        if (characteristic == null) {
            Log.w(TAG, "No such characteristic: " + characteristic_uuid);
            return null;
        }

        return characteristic;
    }

}
