package study.httpserver.io.impl;

import java.util.concurrent.ThreadFactory;

public class DefaultThreadFactory implements ThreadFactory {
	private String name;
	private int count;

	public DefaultThreadFactory() {
		super();
		name = "executor-thread-";
		count = 1;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, name + (count++));
		thread.setDaemon(false);
		thread.setPriority(8);
		return thread;
	}

}
