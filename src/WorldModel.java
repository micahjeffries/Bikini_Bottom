import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{

   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String OCTO_KEY = "octo";
   private static final int OCTO_NUM_PROPERTIES = 7;
   private static final int OCTO_ID = 1;
   private static final int OCTO_COL = 2;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String FISH_KEY = "fish";
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_ID = 1;
   private static final int FISH_COL = 2;
   private static final int FISH_ROW = 3;
   private static final int FISH_ACTION_PERIOD = 4;

   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;
   private static final int ATLANTIS_ANIMATION_PERIOD = 70;
   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;

   private static final String CORAL_KEY = "coral";
   private static final int CORAL_NUM_PROPERTIES = 5;
   private static final int CORAL_ID = 1;

   private static final int PROPERTY_KEY = 0;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows(){
	return numRows;
   }

   public int getNumCols(){
        return numCols;
   }

   public Background[][] getBackground(){
        return background;
   }

   public Entity[][] getOccupancy(){
        return occupancy;
   }

   public Set<Entity> getEntities(){
        return entities;
   }



   public void tryAddEntity(Entity entity)
   {

      if (this.isOccupied(entity.getPosition()))
      {

         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);


   }

   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         entity.getPosition().setOccupancyCell(this, entity);
         this.getEntities().add(entity);
      }

   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         oldPos.setOccupancyCell(this, null);
         this.removeEntityAt(pos);
         pos.setOccupancyCell(this, entity);
         entity.setPosition(pos.getX(),pos.getY());
      }


   }

   public void removeEntity(Entity entity)
   {

      this.removeEntityAt(entity.getPosition());
   }

    public void removeEntityAt(Point pos)
    {
        if (this.withinBounds(pos)
                && pos.getOccupancyCell(this) != null)
        {
            Entity entity = pos.getOccupancyCell(this);


            entity.setPosition(-1, -1);
            this.getEntities().remove(entity);
            pos.setOccupancyCell(this, null);


        }
    }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
              pos.getOccupancyCell(this) != null;
   }

   public boolean processLine(String line,ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case OCTO_KEY:
               return this.parseOcto(properties, imageStore);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties, imageStore);
            case FISH_KEY:
               return this.parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return this.parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return this.parseSgrass(properties, imageStore);
            case CORAL_KEY:
               return this.parseSgrass(properties, imageStore);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         pt.setBackground(this,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseOcto(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Octo_not_full octo = new Octo_not_full(properties[OCTO_ID], pt, imageStore.getImageList(OCTO_KEY),
                 Integer.parseInt(properties[OCTO_LIMIT]),Integer.parseInt(properties[OCTO_LIMIT]),
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]));

         this.tryAddEntity(octo);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));

         Obstacle obstacle = new Obstacle(properties[OBSTACLE_ID], pt, imageStore.getImageList(OBSTACLE_KEY), 0);
         this.tryAddEntity(obstacle);

      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));

         Fish fish = new Fish(properties[FISH_ID], pt, imageStore.getImageList(FISH_KEY),
                  Integer.parseInt(properties[FISH_ACTION_PERIOD]), Integer.parseInt(properties[FISH_ACTION_PERIOD]));
         this.tryAddEntity(fish);

      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));

         Atlantis atlantis = new Atlantis(properties[ATLANTIS_ID], pt, imageStore.getImageList(ATLANTIS_KEY), 0,0);

          this.tryAddEntity(atlantis);

      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties, ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));

         Sgrass sgrass = new Sgrass(properties[SGRASS_ID], pt, imageStore.getImageList(SGRASS_KEY),
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]), 0);
         this.tryAddEntity(sgrass);

      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }



}
