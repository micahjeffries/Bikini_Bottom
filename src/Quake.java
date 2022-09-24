import java.util.List;
import processing.core.PImage;

public class Quake extends ActiveEntity{

    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(String id, Point position,
                   List<PImage> images, int actionPeriod, int animationPeriod)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());

    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
