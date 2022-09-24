public abstract class ActiveEntity extends AnimatedEntity{

    public int actionPeriod;
    public int getActionPeriod(){ return actionPeriod; }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {

        scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 0), this.getAnimationPeriod());

    }
}
