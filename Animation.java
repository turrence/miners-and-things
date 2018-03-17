public class Animation implements Action {
    private Entity entity;
    private int repeatCount;

    public Animation(Entity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((AnimationEntity)entity).nextImage();

        if (repeatCount != 1)
        {   if (entity instanceof AnimationEntity){
            scheduler.scheduleEvent(entity,
                    Create.createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    ((AnimationEntity)entity).getAnimationPeriod());}
        }

    }

}

