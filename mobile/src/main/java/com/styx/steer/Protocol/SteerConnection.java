package com.styx.steer.Protocol;

import com.styx.steer.Protocol.action.ScreenCaptureResponseAction;
import com.styx.steer.Protocol.action.SteerAction;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SteerConnection
{
	public static final String BLUETOOTH_UUID = "300ad0a7-059d-4d97-b9a3-eabe5f6af813";
	public static final String DEFAULT_PASSWORD = "steer";
	public boolean active = true;
	private DataInputStream dataInputStream;
	private OutputStream outputStream;
	private SteerAction capAction = new com.styx.steer.Protocol.action.ScreenCaptureResponseAction(new byte[3000000]);
	
	public SteerConnection(InputStream inputStream, OutputStream outputStream)
	{
		this.dataInputStream = new DataInputStream(inputStream);
		this.outputStream = outputStream;
	}
	
	public SteerAction receiveAction() throws IOException
	{
		synchronized (this.dataInputStream)
		{
			try
			{
				byte type = this.dataInputStream.readByte();
				
				// SCREEN_CAPTURE_RESPONSE
				if (type == 7)
				{
					return ((ScreenCaptureResponseAction) capAction).parse_(dataInputStream);
				}
				else
				{
					return SteerAction.parse(this.dataInputStream, type);
				}
			}
			catch (IOException e)
			{
				// Problem with connection (Usually the device disconnected
				active = false;
				throw e;
			}
			
		}
	}
	
	public void sendAction(SteerAction action) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		action.toDataOutputStream(new DataOutputStream(baos));
		
		synchronized (this.outputStream)
		{
			this.outputStream.write(baos.toByteArray());
			this.outputStream.flush();
		}
	}
	
	public void close() throws IOException
	{
		this.dataInputStream.close();
		this.outputStream.close();
	}
}
