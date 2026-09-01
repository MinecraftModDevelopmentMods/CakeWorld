package com.mcmoddev.cakeworld.network;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.client.ClientCookbookPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public final class OpenCookbookPacket {
	private final CompoundTag progress;

	public OpenCookbookPacket(CompoundTag progress) {
		this.progress = progress.copy();
	}

	public static void encode(OpenCookbookPacket packet, FriendlyByteBuf buffer) {
		buffer.writeNbt(packet.progress);
	}

	public static OpenCookbookPacket decode(FriendlyByteBuf buffer) {
		CompoundTag progress = buffer.readNbt();
		return new OpenCookbookPacket(progress == null ? new CompoundTag() : progress);
	}

	public static void handle(OpenCookbookPacket packet,
			Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> ClientCookbookPacketHandler.open(packet.progress)));
		context.setPacketHandled(true);
	}
}
