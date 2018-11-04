package com.newrelic.codingchallenge.server.service;

import com.newrelic.codingchallenge.model.Request;
import com.newrelic.codingchallenge.model.RequestImpl;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamPlay {

	private static Request[] arrayOfRequests = {
		new RequestImpl("1", "origin"),
		new RequestImpl("2", "origin"),
			new RequestImpl("5", "stuff")

	};

	public static List<Request> getArrayOfRequests() {
		return Arrays.asList(arrayOfRequests);
	}

	@Test
	public void stuff() {
		getArrayOfRequests().stream();
		Stream.of(arrayOfRequests);
		Stream.of(arrayOfRequests[0]);
	}

	@Test
	public void infiniteStream() {
		Stream.generate(Math::random)
				.limit(5)
				.forEach(System.out::println);
	}

	@Test
	public void testIterator(){
		Stream<Integer> evenNumStream = Stream.iterate(2, i -> i * 2);
		List<Integer> collect = evenNumStream.limit(5).collect(Collectors.toList());
		assertEquals(collect, Arrays.asList(2, 4, 8, 16, 32));
	}

	@Test
	public void writer(){
		String[] words = {
				"124335",
				"8324234",
				"35435",
				"342342589",
				"4113454",
				"3849834"
		};

		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter
				(Paths.get("testFile.txt")))) {
			Stream.of(words).forEach(pw::println);
		} catch (IOException e) {
			System.out.println("could not write:  " + e.getLocalizedMessage());
		}
	}


	@Test
	public void filterMap(){
		List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);
		List<Integer> twoEvenSquares =
				numbers.stream()
				.filter(n -> {
					System.out.println("filtering  " + n);
					return n % 2 == 0;
				})
				.map(n -> {
					System.out.println("mapping:  " + n);
					return n*n;
				})
				.limit(2)
				.collect(Collectors.toList());
		assertTrue(twoEvenSquares.contains(4));
	}

}
