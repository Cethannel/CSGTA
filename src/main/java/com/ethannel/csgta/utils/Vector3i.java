package com.ethannel.csgta.utils;

/**
 * Vector3i
 */
public class Vector3i {
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    @Override
    public String toString() {
        return "Vector3i { x: " + this.x + ", y: " + this.y + ", z: " + this.z + "}";
    }
}
