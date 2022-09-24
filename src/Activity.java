public class Activity implements Action{

   private Entity entity;
   private WorldModel world;
   private ImageStore imageStore;
   private int repeatCount;

   public Activity(Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeActivityAction(EventScheduler scheduler)
   {
      if (this.entity instanceof ActiveEntity)
         ((ActiveEntity)this.entity).executeActivity(this.world, this.imageStore, scheduler);
   }

   public static Activity createActivityAction(Entity entity, WorldModel world,ImageStore imageStore)
   {
      return new Activity(entity, world, imageStore, 0);
   }
}

