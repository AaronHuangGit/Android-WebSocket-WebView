package com.websocket.handshake;

public interface ServerHandshake extends Handshakedata {
	public short getHttpStatus();
	public String getHttpStatusMessage();
}
