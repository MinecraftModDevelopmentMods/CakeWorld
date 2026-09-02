package zone.moddev.mc.cakeworld.network;

import java.util.function.Supplier;

import zone.moddev.mc.cakeworld.client.ClientCookbookPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public final class SyncCookbookPacket {
	private final CompoundTag progress;

	public SyncCookbookPacket(CompoundTag progress) {
		this.progress = progress.copy();
	}

	public static void encode(SyncCookbookPacket packet, FriendlyByteBuf buffer) {
		buffer.writeNbt(packet.progress);
	}

	public static SyncCookbookPacket decode(FriendlyByteBuf buffer) {
		CompoundTag progress = buffer.readNbt();
		return new SyncCookbookPacket(progress == null ? new CompoundTag() : progress);
	}

	public static void handle(SyncCookbookPacket packet,
			Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> ClientCookbookPacketHandler.sync(packet.progress)));
		context.setPacketHandled(true);
	}
}
