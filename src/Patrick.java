import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class Patrick extends ActiveEntity{

    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Patrick(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public boolean moveToPatrick(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionPatrick(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = nextPos.getOccupant(world);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }


    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Obstacle obstacle = new Obstacle("", new Point(0,0),
                imageStore.getImageList(QUAKE_KEY), 0);
        Optional<Entity> patrickTarget = this.position.findNearest(world, obstacle);
        long nextPeriod = this.actionPeriod;

        if (patrickTarget.isPresent())
        {
            Point tgtPos = patrickTarget.get().getPosition();

            if (this.moveToPatrick(world, patrickTarget.get(), scheduler))
            {
                Quake quake = new Quake(QUAKE_ID, tgtPos, imageStore.getImageList(QUAKE_KEY),
                        QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public Point nextPositionPatrick(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(),
                    this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

}

