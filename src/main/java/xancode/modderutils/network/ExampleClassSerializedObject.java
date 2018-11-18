package xancode.modderutils.network;

/**
 * @author Xan<br>
 * <br>
 * An example of how to use the RemoteEvent class. This is an IRemoteSerializedObject, and works in conjunction
 * with the other example.
 */
public class ExampleClassSerializedObject implements IRemoteSerializedObject {
	
	public byte reallyCoolByteValue = 0;
	public byte otherReallyCoolByteValue = 0;
	
	//A default constructor MUST exist.
	public ExampleClassSerializedObject() { }

	@Override
	public byte[] getSerializedForm() {
		//Populate a byte array with my values.
		return new byte[] {reallyCoolByteValue, otherReallyCoolByteValue};
	}

	@Override
	public void populateClassFrom(byte[] serialized) {
		//Set the values from the byte array. Note that the order is identical.
		reallyCoolByteValue = serialized[0];
		otherReallyCoolByteValue = serialized[1];
	}

}
