package zss.ntu.com.demo5_tagview;

/**
 * TagItem
 */
public class TagItem
{
    // position
    public int x, y;
    // text
    public String text;
    // direction
    public Direction direction = Direction.UNDEFINED;


    // direction enum
    public enum Direction
    {
        UNDEFINED,
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }


    // animationDirection enum
    public enum AnimationDirection
    {
        UP,
        DOWN
    }


    public TagItem()
    {
    }

    public TagItem(int x, int y, String text)
    {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public TagItem(int x, int y, String text, Direction direction)
    {
        this.x = x;
        this.y = y;
        this.text = text;
        this.direction = direction;
    }
}
