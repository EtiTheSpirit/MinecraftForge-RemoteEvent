package xancode.modderutils.network;

/**
 * @author Xan<br>
 * Represents an object that needs to be serialized / deserialized for a RemoteEvent<br>
 *<br>
 * @param <T> The class being serialized
 */
public interface IRemoteSerializedObject {
	
	public default String getClassName() {
		return this.getClass().getName();
	}
	
	/**
	 * Returns a byte array representing your class or data. The way you organize this is up to you. 
	 * @return A byte array representing the data in your object.
	 */
	public byte[] getSerializedForm();
	
	/**
	 * This method should populate the data in your class from the byte array
	 * @param serialized The serialized byte data you created
	 */
	public void populateClassFrom(byte[] serialized);
}
