import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.Random;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;

    public Point getPosition(){
        return position;
    }

    public void setPosition(int x, int y){
        position = new Point(x,y);
    }

    public List<PImage> getImages(){
        return images;
    }

    public int getImageIndex(){
        return imageIndex;
    }

    public PImage getCurrentImage(){
        return this.getImages().get(this.getImageIndex());
    }

}
