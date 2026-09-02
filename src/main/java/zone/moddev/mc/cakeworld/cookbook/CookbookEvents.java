package zone.moddev.mc.cakeworld.cookbook;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.network.CakeWorldNetwork;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CookbookEvents {
	private CookbookEvents() {
	}

	@SubscribeEvent
	public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer player) {
			CookbookProgress.grantStarterBook(player);
			CakeWorldNetwork.sync(player);
		}
	}

	@SubscribeEvent
	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer player) {
			CakeWorldNetwork.sync(player);
		}
	}

	@SubscribeEvent
	public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getPlayer() instanceof ServerPlayer player) {
			CakeWorldNetwork.sync(player);
		}
	}

	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		CookbookProgress.copyForRespawn(event.getOriginal(), event.getPlayer());
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.player.tickCount % 40 != 0
				|| !(event.player instanceof ServerPlayer player)) {
			return;
		}
		player.level.getBiome(player.blockPosition()).unwrapKey()
				.map(key -> key.location())
				.filter(CookbookEvents::isCakeWorld)
				.ifPresent(id -> CookbookProgress.discover(player,
						DiscoveryType.VISITING, id));
	}

	@SubscribeEvent
	public static void onFinishFood(LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntityLiving() instanceof ServerPlayer player)) {
			return;
		}
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());
		if (isCakeWorld(id) && event.getItem().isEdible()) {
			CookbookProgress.discover(player, DiscoveryType.TASTING, id);
		}
	}

	@SubscribeEvent
	public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayer player)) {
			return;
		}
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(event.getCrafting().getItem());
		if (isCakeWorld(id)) {
			CookbookProgress.discover(player, DiscoveryType.CRAFTING, id);
		}
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayer player)) {
			return;
		}
		ResourceLocation id = ForgeRegistries.BLOCKS.getKey(event.getState().getBlock());
		if (isCakeWorld(id)) {
			CookbookProgress.discover(player, DiscoveryType.MINING, id);
		}
	}

	@SubscribeEvent
	public static void onTrack(PlayerEvent.StartTracking event) {
		if (!(event.getPlayer() instanceof ServerPlayer player)) {
			return;
		}
		ResourceLocation id = Registry.ENTITY_TYPE.getKey(event.getTarget().getType());
		if (isCakeWorld(id)) {
			CookbookProgress.discover(player, DiscoveryType.MEETING, id);
		}
	}

	private static boolean isCakeWorld(ResourceLocation id) {
		return id != null && CakeWorld.MODID.equals(id.getNamespace());
	}
}
