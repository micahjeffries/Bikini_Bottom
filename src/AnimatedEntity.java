public abstract class AnimatedEntity extends Entity{
    public int animationPeriod;
    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
