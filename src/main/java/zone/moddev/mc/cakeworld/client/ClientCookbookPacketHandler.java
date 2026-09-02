package zone.moddev.mc.cakeworld.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientCookbookPacketHandler {
	private ClientCookbookPacketHandler() {
	}

	public static void sync(CompoundTag progress) {
		ClientCookbookState.load(progress);
	}

	public static void open(CompoundTag progress) {
		sync(progress);
		Minecraft.getInstance().setScreen(new CookbookScreen());
	}
}
