final class Animation extends Action
{
    private AnimatedEntity entity;
    private int repeatCount;

    public Animation(AnimatedEntity entity, WorldModel world,
                     ImageStore imageStore, int repeatCount)
    {
        super(world, imageStore);
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    entity.createAnimationAction(
                            Math.max(repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }

}
