import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.Random;

public class Sgrass extends ActiveEntity{

    private static final Random rand = new Random();
    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    public Sgrass(String id, Point position,
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
        Optional<Point> openPt = this.position.findOpenAround(world);

        if (openPt.isPresent())
        {
            Fish fish = new Fish(FISH_ID_PREFIX + this.id, openPt.get(), imageStore.getImageList(FISH_KEY),
                    FISH_CORRUPT_MIN +
                            rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),0);

            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

}

