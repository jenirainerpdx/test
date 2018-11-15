package com.jentest.sockets.server.service;

public interface TallyService{
	Integer getNewUniques();

	Integer getNewDupes();

	Integer getRunningTotalUnique();

	void snapshot();

	void stopService();

}
