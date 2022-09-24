import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class Shark extends ActiveEntity{

    public Shark(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public boolean moveToShark(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }

        else
        {
            Point nextPos = this.nextPositionShark(world, target.getPosition());

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
        Crab crab = new Crab("", new Point(0,0),
                imageStore.getImageList("quake"), 0,0);
        Optional<Entity> sharkTarget = this.position.findNearest(world, crab);
        long nextPeriod = this.actionPeriod;

        if (sharkTarget.isPresent())
        {
            Point tgtPos = sharkTarget.get().getPosition();

            if (this.moveToShark(world, sharkTarget.get(), scheduler))
            {
                nextPeriod += this.actionPeriod;
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public Point nextPositionShark(WorldModel world, Point destPos)
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

