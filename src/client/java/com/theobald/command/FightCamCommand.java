package com.theobald.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.theobald.FightCameraClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FightCamCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(FightCamCommand.class);

    private static final SuggestionProvider<FabricClientCommandSource> PLAYER_SUGGESTIONS = (context, builder) -> {
        for (PlayerEntity player : MinecraftClient.getInstance().world.getPlayers()) {
            if(FightCameraClient.client.player!=null && FightCameraClient.client.player.isSpectator() && player == FightCameraClient.client.player) continue;
            builder.suggest(player.getName().getString());
        }
        return builder.buildFuture();
    };

    public static void register() {
        LOGGER.info("Registering FightCam commands");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("fightcam")
                        .then(ClientCommandManager.literal("distance")
                                .then(ClientCommandManager.argument("distance", StringArgumentType.string())
                                        .executes(context -> {
                                            float f = FloatArgumentType.getFloat(context, "distance");
                                            FightCameraClient.setDistance(f);
                                            sendMessage(context, "Fight camera distance set to " + f);
                                            return 1;
                                        })))
                        .then(ClientCommandManager.literal("height")
                                .then(ClientCommandManager.argument("height", FloatArgumentType.floatArg())
                                        .executes(context -> {
                                            float f = FloatArgumentType.getFloat(context, "height");
                                            FightCameraClient.setHeight(f);
                                            sendMessage(context, "Fight camera height set to " + f);
                                            return 1;
                                        })))
                        .then(ClientCommandManager.literal("lerp")
                                .executes(context -> {
                                    FightCameraClient.toggleLerp();
                                    sendMessage(context, "Fight camera lerp toggled");
                                    return 1;
                                }))
                        .then(ClientCommandManager.literal("players")
                                .then(ClientCommandManager.argument("player1", StringArgumentType.string())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .then(ClientCommandManager.argument("player2", StringArgumentType.string())
                                                .suggests(PLAYER_SUGGESTIONS)
                                                .executes(context -> {
                                                    String name1 = StringArgumentType.getString(context, "player1");
                                                    String name2 = StringArgumentType.getString(context, "player2");

                                                    PlayerEntity p1 = MinecraftClient.getInstance().world.getPlayers()
                                                            .stream().filter(p -> p.getName().getString().equals(name1)).findFirst().orElse(null);
                                                    PlayerEntity p2 = MinecraftClient.getInstance().world.getPlayers()
                                                            .stream().filter(p -> p.getName().getString().equals(name2)).findFirst().orElse(null);

                                                    if (p1 == null || p2 == null) {
                                                        sendMessage(context, "One or both players not found.");
                                                        return 0;
                                                    }

                                                    FightCameraClient.setPlayers(new PlayerEntity[]{p1, p2});
                                                    sendMessage(context, "Fight camera set to " + name1 + " and " + name2);
                                                    return 1;
                                                }))))
                        .then(ClientCommandManager.literal("toggle")
                                .executes(context -> {
                                    FightCameraClient.toggle();
                                    sendMessage(context, "Fight camera toggled");
                                    return 1;
                                }))));
    }

    private static void sendMessage(CommandContext<FabricClientCommandSource> context, String message) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendMessage(Text.literal(message), false);
        }
    }
}
