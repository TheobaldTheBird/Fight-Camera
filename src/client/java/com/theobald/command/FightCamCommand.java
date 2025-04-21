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

    private static final SuggestionProvider<FabricClientCommandSource> DISTANCEMODE_SUGGESTIONS = (context, builder) -> {
        builder.suggest("auto");
        return builder.buildFuture();
    };

    private static final SuggestionProvider<FabricClientCommandSource> ANCHORMODE_SUGGESTIONS = (context, builder) -> {
        builder.suggest("avg");
        builder.suggest("p1");
        builder.suggest("p2");
        return builder.buildFuture();
    };

    private static final SuggestionProvider<FabricClientCommandSource> HEIGHTMODE_SUGGESTIONS = (context, builder) -> {
        builder.suggest("avg");
        builder.suggest("ground");
        return builder.buildFuture();
    };

    public static void register() {
        LOGGER.info("Registering FightCam commands");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("fightcam")
                        .then(ClientCommandManager.literal("distance")
                                .then(ClientCommandManager.argument("distance", StringArgumentType.string())
                                        .suggests(DISTANCEMODE_SUGGESTIONS)
                                        .executes(context -> {
                                            String s = StringArgumentType.getString(context, "distance");
                                            FightCameraClient.setDistanceMode(s.toLowerCase());
                                            return 1;
                                        })))
                        .then(ClientCommandManager.literal("anchor")
                                .then(ClientCommandManager.argument("anchor", StringArgumentType.string())
                                        .suggests(ANCHORMODE_SUGGESTIONS)
                                        .executes(context -> {
                                            String s = StringArgumentType.getString(context, "anchor");
                                            FightCameraClient.setAnchorMode(s.toLowerCase());
                                            return 1;
                                        })))
                        .then(ClientCommandManager.literal("height")
                                .then(ClientCommandManager.argument("height", StringArgumentType.string())
                                        .suggests(HEIGHTMODE_SUGGESTIONS)
                                        .executes(context -> {
                                            String s = StringArgumentType.getString(context, "height");
                                            FightCameraClient.setHeightMode(s.toLowerCase());
                                            return 1;
                                        })))
                        .then(ClientCommandManager.literal("smooth")
                                .then(ClientCommandManager.argument("smoothFactor", FloatArgumentType.floatArg())
                                        .executes(context -> {
                                            float f = FloatArgumentType.getFloat(context, "smoothFactor");
                                            FightCameraClient.setSmoothFactor(f);
                                            sendMessage("Fight camera smoothing set to " + f);
                                            return 1;
                                })))
                        .then(ClientCommandManager.literal("players")
                                .then(ClientCommandManager.argument("player1", StringArgumentType.string())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .then(ClientCommandManager.argument("player2", StringArgumentType.string())
                                                .suggests(PLAYER_SUGGESTIONS)
                                                .executes(context -> {
                                                    String name1 = StringArgumentType.getString(context, "player1");
                                                    String name2 = StringArgumentType.getString(context, "player2");

                                                    FightCameraClient.playerStrings = new String[] {name1, name2};
                                                    FightCameraClient.updatePlayers();

                                                    sendMessage("Fight camera set to " + name1 + " and " + name2);
                                                    return 1;
                                                }))))
                        .then(ClientCommandManager.literal("toggle")
                                .executes(context -> {
                                    FightCameraClient.toggle();
                                    sendMessage("Fight camera toggled");
                                    return 1;
                                }))));
    }

    private static void sendMessage(String message) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendMessage(Text.literal(message), false);
        }
    }
}
