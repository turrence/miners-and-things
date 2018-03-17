final class Activity extends Action
{

    private ActiveEntity entity;

    public Activity(ActiveEntity entity, WorldModel world,
                    ImageStore imageStore)
   {
       super(world, imageStore);
       this.entity = entity;
   }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(getWorld(), getImageStore(), scheduler);
    }
}
