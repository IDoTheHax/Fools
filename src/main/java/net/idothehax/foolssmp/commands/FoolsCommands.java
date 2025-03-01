package net.idothehax.foolssmp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.idothehax.foolssmp.events.achievements.LavaDeathTracker;
import net.idothehax.foolssmp.ComponentInitializer;

public class FoolsCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        // Clear Lava Deaths Command
        dispatcher.register(CommandManager.literal("clearlavadeaths")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "player");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity targetPlayer = source.getServer().getPlayerManager().getPlayer(playerName);

                            if (targetPlayer != null) {
                                resetLavaDeaths(targetPlayer);
                                source.sendFeedback(() -> Text.literal("Cleared lava deaths for " + playerName), true);
                                return 1;
                            } else {
                                source.sendError(Text.literal("Player " + playerName + " not found."));
                                return 0;
                            }
                        })
                )
        );
    }

    private static void resetLavaDeaths(ServerPlayerEntity player) {
        LavaDeathTracker tracker = ComponentInitializer.LAVA_DEATH_TRACKER.get(player);
        tracker.setLavaDeaths(0); // Reset lava deaths using the method

        // Sync the component to client (if needed)
        ComponentInitializer.LAVA_DEATH_TRACKER.sync(player);
    }
}

