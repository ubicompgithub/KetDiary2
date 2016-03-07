package com.ubicomp.ketdiary.test.bluetoothle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public interface BluetoothListener {

	public Activity activity = null;
	
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /* BLE is not supported in this device */
    void bleNotSupported();

    /* BLE connection establishment timeout (10sec) */
    void bleConnectionTimeout();

    /* BLE connection established successfully */
    void bleConnected();

    /* BLE disconnected */
    void bleDisconnected();

    /* BLE write state success */
    void bleWriteCharacteristic1Success();

    /* BLE write state fail */
    void bleWriteStateFail();

    /* Can not detect test plug */
    void bleNoPlug();

    /* Test plug is detected with its ID */
    void blePlugInserted(int cassetteId);

    /* Update battery level */
    void bleUpdateBattLevel(int level);

    /* Update saliva voltage */
    void bleUpdateSalivaVolt(int volt);

    /* Image is retrieved successfully */
    void bleGetImageSuccess(Bitmap bitmap);

    /* Failed to retrieve image */
    void bleGetImageFailure(float dropoutRate);
}
