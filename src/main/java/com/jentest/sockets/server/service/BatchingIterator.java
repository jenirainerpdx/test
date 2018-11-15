package com.jentest.sockets.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public class BatchingIterator<T> implements Iterator<List<T>> {

	private int batchSize;
	private List<T> currentBatch;
	private Iterator<T> sourceIter;

	public BatchingIterator(Iterator<T> iterator, int batchSize) {
			this.sourceIter = iterator;
			this.batchSize = batchSize;
	}

	public static <T> Stream<List<T>> batchedStreamOf(Stream<T> sourceStream,
															   int batchSize) {
		return asStream(new BatchingIterator<>(sourceStream.iterator(), batchSize));
	}

	private static <T> Stream<T> asStream(Iterator<T> iterator) {
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, ORDERED),
				false);
	}

	@Override
	public boolean hasNext() {
		prepareNext();
		return currentBatch != null && !currentBatch.isEmpty();
	}

	private void prepareNext() {
		currentBatch = new ArrayList<>(batchSize);
		while(sourceIter.hasNext() && currentBatch.size() < batchSize) {
			currentBatch.add(sourceIter.next());
		}
	}


	@Override
	public List<T> next() {
		return null;
	}
}
