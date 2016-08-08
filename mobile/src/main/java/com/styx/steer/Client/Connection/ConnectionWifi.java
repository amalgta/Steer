package com.styx.steer.Client.Connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.IOException;

import com.styx.steer.Client.Activity.connection.ConnectionWifiEditActivity;
import com.styx.steer.Client.App.Steer;
import com.styx.steer.Protocol.SteerConnection;
import com.styx.steer.Protocol.tcp.SteerConnectionTcp;


public class ConnectionWifi extends Connection
{
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	
	public ConnectionWifi()
	{
		super();
		this.host = "";
		this.port = SteerConnectionTcp.DEFAULT_PORT;
	}
	
	public static ConnectionWifi load(SharedPreferences preferences, int position)
	{
		ConnectionWifi connection = new ConnectionWifi();
		
		connection.host = preferences.getString("connection_" + position + "_host", null);
		
		connection.port = preferences.getInt("connection_" + position + "_port", 0);
		
		return connection;
	}
	
	public void save(Editor editor, int position)
	{
		super.save(editor, position);
		
		editor.putInt("connection_" + position + "_type", WIFI);
		
		editor.putString("connection_" + position + "_host", this.host);
		
		editor.putInt("connection_" + position + "_port", this.port);
	}
	
	public void edit(Context context)
	{
		Intent intent = new Intent(context, ConnectionWifiEditActivity.class);
		this.edit(context, intent);
	}
	
	public SteerConnection connect(Steer application) throws IOException
	{
		return SteerConnectionTcp.create(this.host, this.port);
	}
	
	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
}
