package com.jentest.sockets.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ValueMap {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValueMap.class);

	private static Integer lastSnapshotMax;

	private static ConcurrentSkipListMap<Integer, Integer> events;

	public ValueMap(){
		lastSnapshotMax = 0;
		events = new ConcurrentSkipListMap<>();
	}

	public void acceptEvent(Request event) {
		LOGGER.debug("calling put with : " + event.getRequestId() + ":  " + event.getIntegerMessage());
		events.put(event.getRequestId(), event.getIntegerMessage());
	}

	// will this be threadsafe?
	public ConcurrentNavigableMap<Integer, Integer> getEventsFromLastTenSeconds() {

		LOGGER.debug("How many in map: " + events.size());
		ConcurrentNavigableMap<Integer, Integer> snapshot =
				events.tailMap(lastSnapshotMax, false);
		LOGGER.debug("How many in snapshot: " + snapshot.size());
		try {
			lastSnapshotMax = snapshot.lastKey();
		} catch (NoSuchElementException nothingInSet) {
			// there was nothing in the snapshot.  Leave lastSnapshotMax as it was.
		}
		return snapshot;
	}

	public Boolean valueExists(Integer integerValue) {
		return events.containsValue(integerValue);
	}

	public int size() {
		return events.size();
	}

	public void showme(){
		for (Integer i : events.values()) {
			LOGGER.debug("" + i);
		}
	}
}
