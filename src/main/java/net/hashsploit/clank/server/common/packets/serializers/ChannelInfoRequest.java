package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelInfoRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] worldID = new byte[4];
	
	public ChannelInfoRequest(byte[] data) {
		super(MediusMessageType.ChannelInfo, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		buf.getShort(); // buffer
		buf.get(worldID);
	}
	
	public String toString() {
		return "ChannelInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"worldID: " + Utils.bytesToHex(worldID);		
	}
	
	public synchronized byte[] getMessageID() {
		return messageID;
	}
	
	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}
	
	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}
	
	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public synchronized byte[] getWorldID() {
		return worldID;
	}
	
	public synchronized void setWorldID(byte[] worldID) {
		this.worldID = worldID;
	}

	
}
