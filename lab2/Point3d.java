/**
 * A tree-dimensional point class.
 **/
public class Point3d {

    /** X coordinate of the point **/
    private double xCoord;

    /** Y coordinate of the point **/
    private double yCoord;

    /** Z coordinate of the point **/
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. **/
    public Point3d(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. **/
    public Point3d() {
        // Call three-argument constructor and specify the origin.
        this(0, 0, 0);
    }

    /** Return the X coordinate of the point. **/
    public double getX() {
        return xCoord;
    }

    /** Return the Y coordinate of the point. **/
    public double getY() {
        return yCoord;
    }

    /** Return the Z coordinate of the point. **/
    public double getZ() {
        return zCoord;
    }

    /** Set the X coordinate of the point. **/
    public void setX(double val) {
        xCoord = val;
    }

    /** Set the Y coordinate of the point. **/
    public void setY(double val) {
        yCoord = val;
    }

    /** Set the Z coordinate of the point. **/
    public void setZ(double val) {
        zCoord = val;
    }

    /** Check if points are equal **/
    public boolean equals(Point3d other) {
        return xCoord == other.xCoord &&
               yCoord == other.yCoord &&
               zCoord == zCoord;
    }

    /** Calculate distance between two points **/
    public double distanceTo(Point3d other) {
        return Math.sqrt(
                   Math.pow(xCoord - other.xCoord, 2) +
                   Math.pow(yCoord - other.yCoord, 2) +
                   Math.pow(zCoord - other.zCoord, 2)
                );
    }

}

