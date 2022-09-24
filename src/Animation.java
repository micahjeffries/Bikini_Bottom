public class Animation implements Action{

   private Entity entity;
   private WorldModel world;
   private ImageStore imageStore;
   private int repeatCount;

   public Animation(Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeAnimationAction(EventScheduler scheduler)
   {

      ((AnimatedEntity)this.entity).nextImage();

      if (this.repeatCount != 1)
      {
         scheduler.scheduleEvent(this.entity,
                 createAnimationAction(this.entity,
                         Math.max(this.repeatCount - 1, 0)),
                 ((AnimatedEntity)this.entity).getAnimationPeriod());
      }

   }

   public static Animation createAnimationAction(Entity entity, int repeatCount)
   {
      return new Animation(entity, null, null, repeatCount);
   }
}
