class StatValues {
	Integer execCount;
	Double averageTime;
	Long totalTime;
	Long fastestTime;
	Long slowestTime;

	public String toString() {
		return "ExecCount " + execCount + " averagetime " + averageTime + "total time " + totalTime + " fastesttime "
				+ fastestTime + " slowestTime " + slowestTime;
	}
	
	public StatValues() {
		execCount = 0;
		totalTime = (long) 0;
		fastestTime = (long) Long.MAX_VALUE;
		slowestTime = 0l;
	}

	public void increment(long elapsedTime) {
		this.execCount++;
		this.totalTime+=elapsedTime;
		this.fastestTime = this.fastestTime < elapsedTime? this.fastestTime:elapsedTime;
		this.slowestTime = this.slowestTime > elapsedTime? this.slowestTime:elapsedTime;
		this.averageTime = (double)(this.totalTime/this.execCount);
		
	}
}