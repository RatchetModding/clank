package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class LadderPositionResponse extends MediusPacket {
	
	private final byte[] messageId;

	public LadderPositionResponse(byte[] messageId) {
		super(MediusPacketType.LadderPositionResponse);
		
		this.messageId = messageId;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytesLittle(1));
			outputStream.write(Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
}
