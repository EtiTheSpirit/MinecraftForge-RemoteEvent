package xancode;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import xancode.modderutils.network.ExampleClassRemoteEvent;
import xancode.modderutils.network.RemoteEvent;

@Mod(modid=Main.MOD_ID,version=Main.VERSION)
public class Main {
	public static final String MOD_ID = "networkexample";
	public static final String VERSION = "69"; //Comedy gold! Fun fact: I accidentally typed "Comedgy" and debated on keeping it for way too long.
	
	public Main() {
		MinecraftForge.EVENT_BUS.register(new ExampleClassRemoteEvent());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		RemoteEvent.registerEventHandlers();
	}
}
