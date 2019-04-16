# MinecraftForge-RemoteEvent
Implements something similar to Roblox's RemoteEvent class. https://developer.roblox.com/api-reference/class/RemoteEvent

This isn't 100% necessary but I thought I'd do it for giggles.

# Make sure to select the proper branch for your version. Master represents 1.12.2

# Usage and API
Just drag 'n' drop the source code into your mod.

## API:

Your class should extend `RemoteEvent`. The Abstract class will require you to register two methods, `OnServerEvent` and `OnClientEvent`. Depending on the side of your event, you will need to populate one or both of these methods. `OnServerEvent` fires on the serverside when a client calls `FireServer`. Likewise, `OnClientEvent` fires when the server runs some `FireClient` event that affects the player in question.

Entire classes can be serialized using `IRemoteSerializedObject`. The encoding and decoding is handled by you. Create a class that implements this interface. This class is the one that will be transferred across the client/server boundary, similarly to the `IMessage` class Forge offers.
