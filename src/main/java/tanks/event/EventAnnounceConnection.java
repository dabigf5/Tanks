package tanks.event;

import io.netty.buffer.ByteBuf;
import tanks.gui.screen.ScreenPartyLobby;
import tanks.network.ConnectedPlayer;
import tanks.network.NetworkUtils;

import java.util.UUID;

public class EventAnnounceConnection implements INetworkEvent
{
	public String name;
	public UUID clientId;
	public boolean joined;
	
	public EventAnnounceConnection()
	{
		
	}
	
	public EventAnnounceConnection(ConnectedPlayer p, boolean joined)
	{
		this.name = p.rawUsername;
		this.clientId = p.clientId;
		this.joined = joined;
	}

	@Override
	public void execute() 
	{
		if (this.joined)
		{
			ScreenPartyLobby.connections.add(new ConnectedPlayer(this.clientId, this.name));
		}
		else
		{
			for (int i = 0; i < ScreenPartyLobby.connections.size(); i++)
			{
				if (ScreenPartyLobby.connections.get(i).clientId.equals(this.clientId))
				{
					ScreenPartyLobby.connections.remove(i);
					i--;
				}
			}
		}
	}

	@Override
	public void read(ByteBuf b)
	{
		this.joined = b.readBoolean();
		this.clientId = UUID.fromString(NetworkUtils.readString(b));
		this.name = NetworkUtils.readString(b);
	}
	
	@Override
	public void write(ByteBuf b)
	{
		b.writeBoolean(this.joined);
		NetworkUtils.writeString(b, this.clientId.toString());
		NetworkUtils.writeString(b, this.name);
	}

}