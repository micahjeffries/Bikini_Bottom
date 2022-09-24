import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import processing.core.PImage;

final class Point
{

   private final int x;
   private final int y;
   private static final int FISH_REACH = 1;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public int getX(){
 	return x;
   }

   public int getY(){
        return y;
   }

   public static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
   }

   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   private Optional<Entity> nearestEntity(List<Entity> entities)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {

         Entity nearest = entities.get(0);

         int nearestDistance = distanceSquared(nearest.getPosition(), this);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), this);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public Optional<Entity> findNearest(WorldModel world, Entity kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.getEntities())
      {
         if (entity.getClass().equals(kind.getClass()))
         {
            ofType.add(entity);
         }
      }

      return this.nearestEntity(ofType);
   }

   public Optional<PImage> getBackgroundImage(WorldModel world)
   {
      if (world.withinBounds(this))
      {
         return Optional.of(this.getBackgroundCell(world).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setBackground(WorldModel world, Background background)
   {
      if (world.withinBounds(this))
      {
         this.setBackgroundCell(world, background);
      }
   }

   public Optional<Entity> getOccupant(WorldModel world)
   {
      if (world.isOccupied(this))
      {
         return Optional.of(this.getOccupancyCell(world));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell(WorldModel world)
   {
      return world.getOccupancy()[this.y][this.x];
   }

   public void setOccupancyCell(WorldModel world,Entity entity)
   {
      world.getOccupancy()[this.y][this.x] = entity;
   }

   private Background getBackgroundCell(WorldModel world)
   {
      return world.getBackground()[this.y][this.x];
   }

   private void setBackgroundCell(WorldModel world, Background background)
   {
      world.getBackground()[this.y][this.x] = background;
   }


   public Optional<Point> findOpenAround(WorldModel world)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(this.x + dx, this.y + dy);
            if (world.withinBounds(newPt) &&
                    !world.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }



}
