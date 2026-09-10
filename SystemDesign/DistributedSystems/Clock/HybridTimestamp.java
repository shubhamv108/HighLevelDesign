package SystemDesign.DistributedSystems.Clock;

public class HybridTimestamp implements Comparable<HybridTimestamp> {
    private final long wallClockTime;
    private final int ticks;

    public HybridTimestamp(final long systemTime, final int ticks) {
        this.wallClockTime = systemTime;
        this.ticks = ticks;
    }

    public static HybridTimestamp fromSystemTime(final long systemTime) {
        //initializing with -1 so that addTicks resets it to 0
        return new HybridTimestamp(systemTime, -1);
    }

    public HybridTimestamp max(HybridTimestamp other) {
        if (this.getWallClockTime() == other.getWallClockTime()) {
            return this.getTicks() > other.getTicks() ? this : other;
        }
        return this.getWallClockTime() > other.getWallClockTime() ? this : other;
    }

    public long getWallClockTime() {
        return wallClockTime;
    }

    public HybridTimestamp addTicks(final int ticks) {
        return new HybridTimestamp(wallClockTime, this.ticks + ticks);
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public int compareTo(HybridTimestamp other) {
        if (this.wallClockTime == other.wallClockTime) {
            return Integer.compare(this.ticks, other.ticks);
        }
        return Long.compare(this.wallClockTime, other.wallClockTime);
    }
}

