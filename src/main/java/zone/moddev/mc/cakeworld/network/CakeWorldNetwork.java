package zone.moddev.mc.cakeworld.network;

import java.util.Optional;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.cookbook.CookbookProgress;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class CakeWorldNetwork {
	private static final String PROTOCOL = "1";
	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(CakeWorld.MODID, "main"),
			() -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

	private CakeWorldNetwork() {
	}

	public static void register() {
		CHANNEL.registerMessage(0, OpenCookbookPacket.class,
				OpenCookbookPacket::encode, OpenCookbookPacket::decode,
				OpenCookbookPacket::handle,
				Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(1, SyncCookbookPacket.class,
				SyncCookbookPacket::encode, SyncCookbookPacket::decode,
				SyncCookbookPacket::handle,
				Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

	public static void sync(ServerPlayer player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
				new SyncCookbookPacket(CookbookProgress.snapshot(player)));
	}

	public static void openCookbook(ServerPlayer player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
				new OpenCookbookPacket(CookbookProgress.snapshot(player)));
	}
}
