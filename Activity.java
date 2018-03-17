public class Activity implements Action{
    //private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    //private int repeatCount;

    public Activity(Entity entity, WorldModel world,
                  ImageStore imageStore)
    {
       // this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
      //  this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        if (entity instanceof  ActiveEntity) {
            ((ActiveEntity)entity).executeActivity(world, imageStore, scheduler);//???
        }
    }

}
