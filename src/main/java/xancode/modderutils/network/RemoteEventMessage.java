package xancode.modderutils.network;

import java.nio.charset.Charset;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author Xan<br>
 *
 * Represents a message for a RemoteEvent.<br>
 * <br>
 */
public class RemoteEventMessage implements IMessage {
	
	public RemoteEventMessage() { }
	
	private IRemoteSerializedObject[] data = new IRemoteSerializedObject[0];
	private ArrayList<IRemoteSerializedObject> dataList = new ArrayList<IRemoteSerializedObject>();
	public RemoteEventMessage(IRemoteSerializedObject... data) {
		this.data = data;
	}
	
	public IRemoteSerializedObject[] getData() {
		return dataList.toArray(data);
	}
	
	@Override
	public void toBytes(ByteBuf buffer) {
		//Write to packet
		
		/*
		 * The method of storing data is pretty simple.
		 * 1: Write an integer with the length of the class's name.
		 * 2: Write the actual name
		 * 3: Write the length of the user's specified serialzied data (integer)
		 * 4: Write the serialized data.
		 * 5: If applicable, repeat.
		 */
		
		buffer.ensureWritable(1024, true); //Is this needed? I have it here because I'm paranoid.
		for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
			IRemoteSerializedObject obj = data[dataIndex];
			byte[] dataOfObj = obj.getSerializedForm();
			String name = obj.getClassName();
			buffer.writeInt(name.length());
			buffer.writeCharSequence(name, Charset.defaultCharset());
			buffer.writeInt(dataOfObj.length);
			buffer.writeBytes(dataOfObj);
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		//Read from packet
		while (buffer.isReadable()) {
			int sizeOfNextName = buffer.readInt();
			String name = buffer.readCharSequence(sizeOfNextName, Charset.defaultCharset()).toString();
			int sizeOfNextBuffer = buffer.readInt();
			byte[] dataOfObj = new byte[sizeOfNextBuffer];
			buffer.readBytes(dataOfObj);
			createInstanceOf(name, dataOfObj);
		}
	}
	
	
	//I don't particluarly like using the try/catch here as it can cause confusion whenever the event does fire but nothing happens.
	//TODO: Find a better way of reporting failures. Crash?
	private void createInstanceOf(String className, byte[] data) {
		try {
			IRemoteSerializedObject object = (IRemoteSerializedObject)Class.forName(className).newInstance();
			object.populateClassFrom(data);
			dataList.add(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
