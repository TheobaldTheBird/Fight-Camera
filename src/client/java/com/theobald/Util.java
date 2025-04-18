package com.theobald;

import net.minecraft.util.math.Vec3d;

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


}
