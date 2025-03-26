import java.util.concurrent.ConcurrentHashMap;

public class WrappedStats {
	private ConcurrentHashMap<Long, StatValues> statsPerThreads;

	public WrappedStats() {
		statsPerThreads = new ConcurrentHashMap<Long, StatValues>();
	}

	public ConcurrentHashMap<Long, StatValues> getStatsMap() {
		return statsPerThreads;
	}

	public void updateStats(long elapsedTime) {
		long id = Thread.currentThread().getId();
		StatValues st = null;
		if (statsPerThreads.containsKey(id)) {
			st = statsPerThreads.get(id);
		} else
			st = new StatValues();
		st.increment(elapsedTime);
		this.statsPerThreads.putIfAbsent(Thread.currentThread().getId(), st);
	}

	public String consolidate() {
		// TODO Auto-generated method stub
		StatValues cs = new StatValues();
		for (StatValues s : statsPerThreads.values()) {
			cs.execCount += s.execCount;
			cs.fastestTime = cs.fastestTime < s.fastestTime ? cs.fastestTime : s.fastestTime;
			cs.slowestTime = cs.slowestTime > s.slowestTime ? cs.slowestTime : s.slowestTime;
			cs.totalTime = cs.totalTime + s.totalTime;
		}
		cs.averageTime = (double) (cs.totalTime / cs.execCount);
		return cs.toString();
	}

}
