package xancode.modderutils.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Xan<br>
 * <br>
 * Shows how to create a RemoteEvent and how to use it.
 */
public class ExampleClassRemoteEvent extends RemoteEvent {
			
	public ExampleClassRemoteEvent() { }
	
	/**
	 * Add an event that listens for when the player chats on the clientside.
	 * @param event
	 */
	@SubscribeEvent
	public void doReallyCoolStuff(ClientChatEvent event) {
		if (!event.getOriginalMessage().contains("cool and good")) return;
		
		//If the user says "cool and good" then we'll create a new serialized object and set some values in it.
		ExampleClassSerializedObject myCoolObject = new ExampleClassSerializedObject();
		myCoolObject.reallyCoolByteValue = 9;
		myCoolObject.otherReallyCoolByteValue = 12;
		
		//Finally, after we're done setting up the data, we need to call FireServer to send this data to the server.
		FireServer(myCoolObject);
	}

	@Override
	public void OnServerEvent(EntityPlayerMP clientThatFired, IRemoteSerializedObject... args) {
		//We know we fired the server with one argument of myCoolObject.
		//In light of this, we know that's definitely what's coming in.
		//It's safe to cast the incoming argument(s) based on what you know you sent, as the same order is guaranteed.
		
		ExampleClassSerializedObject theCoolObjectSentByTheClient = (ExampleClassSerializedObject)args[0];
		
		//Add diamonds and gold based on the amount we specified on the clientside. Very dangerous. But still works as an example, nonetheless.
		clientThatFired.addItemStackToInventory(new ItemStack(Item.getByNameOrId("diamond"), theCoolObjectSentByTheClient.reallyCoolByteValue));
		clientThatFired.addItemStackToInventory(new ItemStack(Item.getByNameOrId("gold_ingot"), theCoolObjectSentByTheClient.otherReallyCoolByteValue));
	}
	
	@Override
	//Not used, but still needs to be specified due to the RemoteEvent class being abstract.
	public void OnClientEvent(IRemoteSerializedObject... args) { }
	
	@Override
	//Forge's stock handler. This code here should be common for every new instance of RemoteEvent you make, as it needs to fire the appropriate handler events.
	public IMessage onMessage(RemoteEventMessage message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			OnServerEvent(ctx.getServerHandler().player, message.getData());
		} else {
			OnClientEvent(message.getData());
		}
		return null;
	}	
}
