package com.za.finger;

public abstract interface IUsbConnState
{
  public abstract void onUsbConnected();

  public abstract void onUsbPermissionDenied();

  public abstract void onDeviceNotFound();
}