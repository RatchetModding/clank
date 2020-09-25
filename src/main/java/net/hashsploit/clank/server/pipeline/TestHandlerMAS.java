package net.hashsploit.clank.server.pipeline;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.bouncycastle.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerMAS extends ChannelInboundHandlerAdapter { // (1)
	
	private static final Logger logger = Logger.getLogger("");
	private final MediusClient client;
	
	public TestHandlerMAS(final MediusClient client) {
		super();
		this.client = client;
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel active");
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel inactive");
	}
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	/* s must be an even-length string. */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {      
		final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		logger.finest("RAW: " + Utils.bytesToString(data));

		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (data.length < 3 || data.length > 2048) {
			ctx.close();
			return;
		}

		// Get RT Packet ID
		RTPacketId rtid = null;
		
		for (RTPacketId p : RTPacketId.values()) {
			if (p.getByte() == data[0]) {
				rtid = p;
				break;
			}
		}
		DataPacket packet = new DataPacket(rtid, Arrays.copyOfRange(data, 3, data.length));
	    logger.fine("Packet ID: " + rtid.toString());
	    logger.fine("Packet ID: " + rtid.getByte());
		// =============================================
		//              IF STATEMENTS
		// =============================================	  	    
	    checkRTConnect(ctx, packet);
		
	    checkMediusPackets(ctx, packet);
		
//		
//		if (rtid.getByte() == (byte) 0x0b) {
//			// =============================================
//			// CLIENT_APP_TOSERVER (0x0b)
//			// =============================================
//			if (data[1] == 0x1e) {
//				logger.fine("0x1e auth");
//		        //Random rand = new Random(); 
//				byte[] firstPart = hexStringToByteArray("0a320001043100000000000000000000000000000000000000000000000000000031");
//				//byte[] playerID = hexStringToByteArray(Integer.toString(rand.nextInt(32000))); // TODO: make this non-random. save player ID
//				byte[] playerID = hexStringToByteArray("33"); // TODO: make this non-random. save player ID
//				byte[] lastPart = hexStringToByteArray("000000000000000000000000000000000000");
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//				try {
//					outputStream.write(firstPart);
//					outputStream.write(playerID);
//					outputStream.write(lastPart);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				finalPayload = outputStream.toByteArray();
//			} else if (data[1] == 0x32) { // Second 0x0b response. Seems static
//				logger.fine("0x32 auth");
//				finalPayload = hexStringToByteArray("0a1e0001a400000000000000000000000000000000000000000000000000000000");		
//			} else if (data[1] == 0x52) { // Username and password passed to server. TODO: check with database and don't respond if not valid login
//				// For now, just send success, and the IP of the MLS/NAT
//				// we don't know what this contains really, it just works
//				logger.fine("0x52 auth");
//				
//				RTPacketId resultPacketId = RTPacketId.SERVER_APP;
//				
//				byte[] mlsAddress = Clank.getInstance().getConfig().getProperties().get("MlsIpAddress").toString().getBytes();
//				int mlsNumZeros = 16 - Clank.getInstance().getConfig().getProperties().get("MlsIpAddress").toString().length();
//				String mlsZeroString = new String(new char[mlsNumZeros]).replace("\0", "00");
//				byte[] mlsZeroTrail = hexStringToByteArray(mlsZeroString);
//				
//				byte[] natAddress = Clank.getInstance().getConfig().getProperties().get("NatIpAddress").toString().getBytes();
//				int natNumZeros = 16 - Clank.getInstance().getConfig().getProperties().get("NatIpAddress").toString().length();
//				String natZeroString = new String(new char[natNumZeros]).replace("\0", "00");
//				byte[] natZeroTrail = hexStringToByteArray(natZeroString);
//				
//				
//				
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//				try {
//					outputStream.write(hexStringToByteArray("0108310000000000000000000000000000000000000000000000000000000e00000001000000580000000100000001000000")); // First part of the packet: unknown
//					outputStream.write(mlsAddress); // MLS Server Addr TODO: replace with server IP
//					outputStream.write(mlsZeroTrail); // Zero padding based on server IP size 
//					outputStream.write(hexStringToByteArray("5e27000003000000")); // MLS Port + padding
//					
//					outputStream.write(natAddress); // NAT server Addr
//					outputStream.write(natZeroTrail); // Padding for address
//					outputStream.write(hexStringToByteArray("5627000005000000")); // NAT Port + padding
//					outputStream.write(hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
//					outputStream.write(hexStringToByteArray("3133")); // World ID and Player ID
//					outputStream.write(hexStringToByteArray("000000000000000000000000000000")); // Padding 
//					outputStream.write(hexStringToByteArray("5a657138626b494b77704d6632444f50000000")); // MLS Acess Token
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				byte[] dataPayload = outputStream.toByteArray();
//				DataPacket mlsDataPacket = new DataPacket(resultPacketId, dataPayload);
//				finalPayload = mlsDataPacket.toBytes();
//			}
//			
//			
//
//		}
		

    }
    
    private void checkMediusPackets(ChannelHandlerContext ctx, DataPacket packet) {
	    // ALL OTHER PACKETS THAT ARE MEDIUS PACKETS
    	MediusMessage mm = null;
	    if (packet.getId().toString().contains("APP")) {
	    	
	    	MediusMessage incomingMessage = new MediusMessage(packet.getPayload());

			logger.fine("Found Medius Packet ID: " + Utils.bytesToHex(incomingMessage.getMediusPacketType().getShortByte()));
			logger.fine("Found Medius Packet ID: " + incomingMessage.getMediusPacketType().toString());
			
			// Detect which medius packet is being parsed
		    MediusPacket mediusPacket = client.getMediusMap().get(incomingMessage.getMediusPacketType());
		    
		    // Process this medius packet
		    mediusPacket.read(incomingMessage);
		    mm = mediusPacket.write(client);	    
	    	DataPacket responsepacket = new DataPacket(RTPacketId.SERVER_APP, mm.toBytes());

			byte[] finalPayload = responsepacket.toBytes();
			logger.finest("Final payload: " + Utils.bytesToHex(finalPayload));
			ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
			ctx.write(msg);
			ctx.flush();	
	    }
    }
    
    private void checkRTConnect(ChannelHandlerContext ctx, DataPacket packet) {
		if (packet.getId().getByte() == (byte) 0x00) {
			// =============================================
			// CLIENT_CONNECT_TCP (0x00)
			// =============================================
			String clientIP = client.getIPAddress().substring(1);
			logger.fine(clientIP);
			RTPacketId resultrtid = RTPacketId.SERVER_CONNECT_ACCEPT_TCP;
			
			byte[] header = hexStringToByteArray("01081000000100");
			byte[] ipAddr = clientIP.getBytes();
			int numZeros = 16 - client.getIPAddress().substring(1).length();
			String zeroString = new String(new char[numZeros]).replace("\0", "00");
			byte[] zeroTrail = hexStringToByteArray(zeroString);
			byte[] allByteArray = new byte[header.length + ipAddr.length + zeroTrail.length];
			ByteBuffer buff = ByteBuffer.wrap(allByteArray);
			buff.put(header);
			buff.put(ipAddr);
			buff.put(zeroTrail);
			
			logger.fine("Data header: " + bytesToHex(header));
			logger.fine("IP: " + bytesToHex(ipAddr));
			logger.fine("IP Padding encode/decode: " + bytesToHex(zeroTrail));

			byte[] payload = buff.array();
			
			logger.fine("Payload: " + bytesToHex(payload));
			
			DataPacket dataPacket = new DataPacket(resultrtid, payload);
			byte[] dataPacketBuffer = dataPacket.toBytes();
			
			byte[] extraPacket = hexStringToByteArray("1A02000100");
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			try {
				outputStream.write(dataPacketBuffer);
				outputStream.write(extraPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] finalPayload = outputStream.toByteArray();
			logger.fine("Final payload: " + bytesToHex(finalPayload));
	        ctx.write(Unpooled.copiedBuffer(finalPayload)); // (1)
	        ctx.flush(); // (2)
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    
	private static ByteBuffer toNioBuffer(final ByteBuf buffer) {
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		return ByteBuffer.wrap(bytes);
	}
}