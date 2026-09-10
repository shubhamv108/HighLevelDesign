package SystemDesign.DistributedSystems.Clock;

public class LamportClock {

    private int latestTime;

    public LamportClock(final int latestTime) {
     }

    public int tick(int requestTime) {
        latestTime = Integer.max(latestTime, requestTime);
        latestTime++;
        return latestTime;
    }
}
