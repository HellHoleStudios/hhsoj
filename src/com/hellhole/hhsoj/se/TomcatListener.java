package com.hellhole.hhsoj.se;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TomcatListener implements ServletContextListener{
	public ManagerThread mThread;
	
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO make a ServerManager safely stop
		// mThread.m.stop();
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		mThread=new ManagerThread(7512);
		mThread.start();
	}
}
