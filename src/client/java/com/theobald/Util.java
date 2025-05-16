package com.theobald;

import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Util
{
    public static Vec3d Average(Vec3d a, Vec3d b) {
        float x = (float) (a.x + b.x) / 2;
        float y = (float) (a.y + b.y) / 2;
        float z = (float) (a.z + b.z) / 2;

        return new Vec3d(x,y,z);
    }

    public static Vec3d Average(List<Vec3d> vecs) {
        Vec3d sum = new Vec3d(0, 0, 0);
        int size = vecs.size();

        if (size == 0) {
            return sum;
        }

        for (Vec3d vec : vecs) {
            sum = sum.add(vec);
        }
        return new Vec3d(sum.x/size, sum.y/size, sum.z/size);
    }

    public static Vec3d Orthoganal(Vec3d a)
    {
        return new Vec3d(a.z, 0, -a.x);
    }

    public static float FindAutoDistance(Vec3d playerDist, int fov) {
        float hFov = fov * FightCameraClient.aspectRatio * .8f;
        double hDist = playerDist.horizontalLength();
        float hAutoDistance = Math.abs((float)((hDist/2) / Math.tan(Math.toRadians((double) hFov/2))));
        if (FightCameraClient.heightMode == FightCameraClient.HeightMode.GROUND) {
            return hAutoDistance;
        }

        float vFov = fov;
        double vDist = playerDist.y;
        float vAutoDistance = Math.abs((float)((vDist/2) / Math.tan(Math.toRadians((double) vFov/2))));

        return Math.max(hAutoDistance, vAutoDistance);
    }

    public static Vec3d SmoothStep(Vec3d a, Vec3d b, float t) {
        double x = a.x + ((b.x - a.x) * t);
        double y = a.y + ((b.y - a.y) * t);
        double z = a.z + ((b.z - a.z) * t);

        return new Vec3d(x, y, z);
    }

    public static float DistanceSmoothingCoeff(float x)
    {
        float z = .2f;
        float a = 4f;
        float b = -4.2f;
        float k = 1 - z;

        //sigmoid
        return (k / (1 + (float) Math.pow(Math.E, a + (b * x)))) + z;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static List<Vec3d> GetPlayerPearlsPos(World world, PlayerEntity player) {
        if (world == null || player == null) return Collections.emptyList();

        List<Vec3d> pearlsPos = new ArrayList<>();
        Vec3d playerPos = player.getPos();
        Box worldBox = new Box(
                playerPos.x-500, -64, playerPos.z-500,
                playerPos.x+500, 320, playerPos.z+500
        );

        for (Entity entity : world.getEntitiesByType(EntityType.ENDER_PEARL, worldBox, e -> true)) {
            if (entity instanceof EnderPearlEntity pearl) {
                Entity owner = pearl.getOwner();
                if (owner != null && owner.equals(player)) {
                    pearlsPos.add(pearl.getPos());
                }
            }
        }

        return pearlsPos;
    }

    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180F);
        float g = -yaw * ((float)Math.PI / 180F);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static Vec3d getInterpolatedPos(Entity entity, float tickDelta) {
        double x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        double y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        double z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(x, y, z);
    }

    public static Vec3d getInterpolatedLook(Entity entity, float tickDelta) {
        float yaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        float pitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

        return getRotationVector(pitch, yaw);
    }
}
