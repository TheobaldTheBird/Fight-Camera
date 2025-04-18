package com.theobald;

import com.theobald.command.FightCamCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FightCameraClient implements ClientModInitializer {
	public static MinecraftClient client;
	public boolean keybindPressed = false;
	public static boolean active = false;
	public static boolean lerp = true;
	public static AnchorMode anchor = AnchorMode.AVERAGE;
	public static PlayerEntity[] players;
	public static double smoothFactor = 0.1;
	public static float distance = 3;
	public static Vec3d lastTargetPos = Vec3d.ZERO;
	public static Vec3d currentTargetPos = Vec3d.ZERO;
	public static float lastTargetYaw = 0;
	public static float currentTargetYaw = 0;
	public static float height = 2;
	public static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	public static DistanceMode distanceMode;

	public enum AnchorMode {
		P1,
		P2,
		AVERAGE
	}

	public enum DistanceMode {
		STATIC,
		AUTO
	}

	@Override
	public void onInitializeClient() {
		client = MinecraftClient.getInstance();

		var toggleBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Toggle", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_SEMICOLON, // The keycode of the key
				"FightCam" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			onUpdate();
			if (toggleBind.isPressed()) {
				if (!keybindPressed) {
					toggle();
					keybindPressed = true;
				}
			}
			else {
				keybindPressed = false;
			}
		});
		WorldRenderEvents.START.register(context -> onRender());

		FightCamCommand.register();
		if (FabricLoader.getInstance().isModLoaded("flashback"));
	}

	private void onUpdate() {
		if (active) {
			updateTarget(CalculateCameraPos());
			updateYawTarget(CalculateCameraYaw());
		}
	}

	private void onRender() {
		Camera camera = client.gameRenderer.getCamera();
		if (camera != null && active) {
			Vec3d camPos;
			float camYaw;

			if (lerp) {
				camPos = getInterpolated(camera.getLastTickDelta());
				camYaw = lerpAngle(lastTargetYaw,currentTargetYaw,camera.getLastTickDelta());
			} else {
				camPos = currentTargetPos;
				camYaw = currentTargetYaw;
			}

			camera.setPos(camPos);
			client.player.setYaw(camYaw);

			if (client.player.isSpectator())
			{
				Vec3d spectatorPos = lerp ? lastTargetPos.lerp(currentTargetPos, camera.getLastTickDelta()) : currentTargetPos;
				client.player.setPos(spectatorPos.x, spectatorPos.y, spectatorPos.z);
			}
		}

	}

	public static Vec3d CalculateCameraPos() {
		Vec3d camPos = new Vec3d(0, 0, 0);
		Vec3d avg = Util.Average(players[0].getPos(),players[1].getPos());
		Vec3d distanceVec = players[0].getPos().subtract(players[1].getPos());
		Vec3d orthVec = Util.Orthoganal(distanceVec).normalize();

		avg = avg.add(0, 2, 0);
		avg = avg.add(new Vec3d(orthVec.x, 0, orthVec.z).multiply(distance));

		return avg;
	}

	public static float CalculateCameraYaw() {
		Vec3d distanceVec = players[0].getPos().subtract(players[1].getPos());
		Vec3d orthVec = distanceVec.normalize();
		double yaw = ((float) Math.atan2(orthVec.z, orthVec.x));
		return (float) Math.toDegrees(yaw);
	}

	public static void toggle() {
		active = !active;
		if (active && client.player != null) {
			client.player.sendMessage(Text.literal("Fight cam enabled!"), true);
		}
		else {
            assert client.player != null;
            client.player.sendMessage(Text.literal("Fight cam disabled!"), true);
		}
	}

	public static boolean getActive() {
		return active;
	}

	static void setAnchorMode(AnchorMode mode) {
		anchor = mode;
	}

	static AnchorMode getAnchorMode() {
		return anchor;
	}

	public static void setPlayers(PlayerEntity[] p) {
		players = p;
	}

	public PlayerEntity[] getPlayers() {
		return players;
	}

	public static double getSmoothFactor() {
		return smoothFactor;
	}

	public static void setDistance(float f) {
		distance = f;
	}

	public static void updateTarget(Vec3d newPos) {
		lastTargetPos = currentTargetPos;
		currentTargetPos = newPos;
	}

	public static void updateYawTarget(float newYaw) {
		lastTargetYaw = currentTargetYaw;
		currentTargetYaw = newYaw;
	}

	public static float lerpAngle(float a, float b, float t) {
		float delta = ((b - a + 180f) % 360f + 360f) % 360f - 180f;
		return a + delta * t;
	}

	public static Vec3d getInterpolated(double tickDelta) {
		return lastTargetPos.lerp(currentTargetPos, tickDelta);
	}

	public static void toggleLerp() {
		lerp = !lerp;
		if (lerp) {
			client.player.sendMessage(Text.literal("Fight lerp enabled"), true);
		} else {
			client.player.sendMessage(Text.literal("Fight lerp disabled"), true);
		}
	}

	public static void setHeight(float f) {
		height = f;
	}

	public static void setDistanceMode(String s)
	{
		try {
			var f = Float.parseFloat(s);
			distance = f;
			distanceMode = DistanceMode.STATIC;
		}
		catch (NumberFormatException e) {
			switch (s)
			{
				case "AUTO":
					distanceMode = DistanceMode.AUTO;
					return;


			}
		}
	}
}