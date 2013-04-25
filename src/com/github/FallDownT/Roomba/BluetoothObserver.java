package com.github.FallDownT.Roomba;

/**
 * Watches BluetoothReader and updates all observers.
 */
public interface BluetoothObserver {
    public void update(byte[] incPacket);
}
