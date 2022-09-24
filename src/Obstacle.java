import java.util.List;
import processing.core.PImage;

public class Obstacle extends AnimatedEntity{

    public Obstacle(String id, Point position,
                   List<PImage> images, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }

}
