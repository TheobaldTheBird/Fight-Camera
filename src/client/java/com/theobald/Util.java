package com.theobald;

import net.minecraft.util.math.Vec3d;
import org.apache.commons.math3.analysis.function.Sigmoid;

public class Util
{
    public static Vec3d Average(Vec3d a, Vec3d b)
    {
        float x = (float) (a.x + b.x) / 2;
        float y = (float) (a.y + b.y) / 2;
        float z = (float) (a.z + b.z) / 2;

        return new Vec3d(x,y,z);
    }

    public static Vec3d Orthoganal(Vec3d a)
    {
        return new Vec3d(a.z, 0, -a.x);
    }

    public static float FindAutoDistance(float playerDist, int fov) {
        return Math.abs((float)((playerDist/2) / Math.tan(Math.toRadians((double) fov/2))));
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

        return (k / (1 + (float) Math.pow(Math.E, a + (b * x)))) + z;
    }
}
