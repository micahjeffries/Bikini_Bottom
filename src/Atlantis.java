import java.util.List;
import processing.core.PImage;

public class Atlantis extends ActiveEntity{

    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(String id, Point position,
                   List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
            scheduler.scheduleEvent(this,
                    Animation.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());

    }
}

