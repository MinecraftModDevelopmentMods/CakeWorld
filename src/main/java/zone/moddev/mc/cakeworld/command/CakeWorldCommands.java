package zone.moddev.mc.cakeworld.command;

import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Read-only CakeWorld diagnostics built only on OreSpawn's public API. */
public final class CakeWorldCommands {
	public static final String STATUS_KEY =
			"command.cakeworld.orespawn_status";
	public static final String NO_PROFILE_KEY =
			"command.cakeworld.orespawn_status.no_profile";
	public static final String NO_TEMPLATE_KEY =
			"command.cakeworld.orespawn_status.no_template";

	private CakeWorldCommands() {
	}

	public static void register(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher =
				event.getDispatcher();
		dispatcher.register(Commands.literal("cakeworld")
				.then(Commands.literal("orespawn")
						.executes(context -> status(context.getSource()))));
	}

	private static int status(CommandSourceStack source) {
		Optional<GeologyProfileView> active = OreSpawnApi.getActiveProfile(
				source.getServer());
		if (active.isEmpty()) {
			source.sendFailure(new TranslatableComponent(NO_PROFILE_KEY));
			return 0;
		}
		source.sendSuccess(statusComponent(active.orElseThrow()), false);
		return 1;
	}

	public static Component statusComponent(GeologyProfileView profile) {
		Object template = profile.selectedTemplate()
				.<Object>map(ResourceLocation::toString)
				.orElseGet(() -> new TranslatableComponent(NO_TEMPLATE_KEY));
		return new TranslatableComponent(STATUS_KEY,
				template,
				profile.geologyMode(),
				profile.rockIds().size(),
				profile.geomeIds().size(),
				profile.oreIds().size(),
				profile.fluidDepositIds().size(),
				profile.biomePaletteIds().size(),
				profile.terrainDimensions().size());
	}
}
