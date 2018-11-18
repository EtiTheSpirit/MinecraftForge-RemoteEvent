package xancode.modderutils.network;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Xan
 * <br>
 * Inspired by Roblox's RemoteEvent class, this provides an even simpler wrapper for getting data across the client/server boundary.<br>
 * <br>
 * Uses very similar API -- {@link http://wiki-origin.roblox.com/index.php?title=API:Class/RemoteEvent} <br>
 * <em>(If that site is down, given that it is the old version of the site, use {@link https://developer.roblox.com/api-reference/class/RemoteEvent} instead)</em>
 */
public abstract class RemoteEvent implements IMessageHandler<RemoteEventMessage, IMessage> {

	private static int event_index = 0;
	private static final Side THIS_SIDE = FMLCommonHandler.instance().getSide(); //To do: Find a way to not use this or just go with it, idgaf. Whatever works
	private static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("RemoteEvent" + (event_index++)); //To do: Make this not so nasty.
	
	/**
	 * <strong>Should only be used on the client!</strong><br>
	 * Sends a request to the server
	 * 
	 * @param args The objects being sent to the server
	 * @throws IllegalStateException When this method called from the serverside.
	 */
	public void FireServer(IRemoteSerializedObject... args) {
		if (THIS_SIDE.isServer()) {
			throw new IllegalStateException("Attempt to call FireServer from the serverside! This method should be called from clients only.");
		}

		NETWORK_INSTANCE.sendToServer(new RemoteEventMessage(args));
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to a specific client.  This uses the network's {@code sendTo} method.
	 * 
	 * @param recipient The player this is going to.
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireClient(EntityPlayerMP recipient, IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireClient from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendTo(new RemoteEventMessage(args), recipient);
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to every client currently connected to the server. This uses the network's {@code sendToAll} method.
	 * 
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireAllClients(IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireAllClients from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendToAll(new RemoteEventMessage(args));
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to every client around {@code point}. This uses the network's {@code sendToAllAround} method. See {@link NetworkRegistry.TargetPoint} for more details.
	 * 
	 * @param point The {@link NetworkRegistry.TargetPoint}
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireAllClientsAround(NetworkRegistry.TargetPoint point, IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireAllClientsAround from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendToAllAround(new RemoteEventMessage(args), point);
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to every client tracking {@code point}. This uses the network's {@code sendToAllTracking} method. See {@link NetworkRegistry.TargetPoint} for more details.
	 * 
	 * @param point The {@link NetworkRegistry.TargetPoint}
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireAllClientsTracking(NetworkRegistry.TargetPoint point, IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireAllClientsTracking from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendToAllTracking(new RemoteEventMessage(args), point);
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to every client tracking {@code entity}. This uses the network's {@code sendToAllTracking} method. See {@link Entity} for more details.
	 * 
	 * @param entity The {@link Entity} being tracked.
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireAllClientsTracking(Entity entity, IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireAllClientsTracking from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendToAllTracking(new RemoteEventMessage(args), entity);
	}
	
	/**
	 * <strong>Should only be used on the server!</strong><br>
	 * Sends data to every client within the dimension with the ID {@code dimensionId} . This uses the network's {@code sendToDimension} method. See {@link Entity} for more details.
	 * 
	 * @param dimensionId The ID of the dimension. This is the same as world ID (e.g. overworld is 0, nether is -1, end is 1, etc. for mod worlds)
	 * @param args The objects being sent to the client
	 * @throws IllegalStateException When this method is called from the clientside
	 */
	public void FireAllClientsInDimension(int dimensionId, IRemoteSerializedObject... args) {
		if (THIS_SIDE.isClient()) {
			throw new IllegalStateException("Attempt to call FireAllClientsInDimension from the clientside! This method should be called from the server only.");
		}
		
		NETWORK_INSTANCE.sendToDimension(new RemoteEventMessage(args), dimensionId);
	}
	
	/**
	 * A method that is called on the serverside whenever a client sends a request to the server.<br>
	 * The order of the arguments coming in from {@code args} is guaranteed to be identical to the order specified in {@code FireServer()}.
	 * <br>
	 * This means that: <br>
	 * Inputting two arguments where the first of these arguments is {@code foo} and the second of these arguments is {@code bar} guarantees that the arguments from this method will be listed in the order {@code foo, bar}
	 * @param clientThatFired The Player that fired the event.
	 * @param args The arguments sent with the event.
	 */
	public abstract void OnServerEvent(EntityPlayerMP clientThatFired, IRemoteSerializedObject... args);
	
	/**
	 * A method that is called on the clientside whenever the server sends data to this client.<br>
	 * The order of the arguments coming in from {@code args} is guaranteed to be identical to the order specified in {@code FireClient()}.
	 * <br>
	 * This means that: <br>
	 * Inputting two arguments where the first of these arguments is {@code foo} and the second of these arguments is {@code bar} guarantees that the arguments from this method will be listed in the order {@code foo, bar}
	 * @param args The arguments sent with the event.
	 */
	public abstract void OnClientEvent(IRemoteSerializedObject... args);
	
	/**
	 * Register all RemoteEvents to Forge's network bus.<br>
	 * This must be called from the {@link FMLInitializationEvent} method in your main mod class.
	 */
	public static void registerEventHandlers() {
		int idx = 0;
		try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
			ClassInfoList subs = scanResult.getSubclasses(RemoteEvent.class.getName());
			for (ClassInfo clsInfo : subs) {
				RemoteEvent evtObject = (RemoteEvent) Class.forName(clsInfo.getName()).newInstance();
				NETWORK_INSTANCE.registerMessage(evtObject, RemoteEventMessage.class, idx++, Side.SERVER);
				NETWORK_INSTANCE.registerMessage(evtObject, RemoteEventMessage.class, idx++, Side.CLIENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
