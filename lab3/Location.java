/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Location otherLocation = (Location)obj;

        return otherLocation.xCoord == xCoord
            && otherLocation.yCoord == yCoord;
    }

    /** Cantor pairing function **/
    @Override
    public int hashCode()
    {
        return (xCoord + yCoord) * (xCoord + yCoord + 1) / 2 + xCoord;
    }
}
