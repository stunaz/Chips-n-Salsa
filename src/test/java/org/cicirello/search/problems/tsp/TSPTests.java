/*
 * Chips-n-Salsa: A library of parallel self-adaptive local search algorithms.
 * Copyright (C) 2002-2022 Vincent A. Cicirello
 *
 * This file is part of Chips-n-Salsa (https://chips-n-salsa.cicirello.org/).
 * 
 * Chips-n-Salsa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Chips-n-Salsa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package org.cicirello.search.problems.tsp;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.SplittableRandom;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.SolutionCostPair;

/**
 * JUnit tests for the TSP class and its nested subclasses.
 */
public class TSPTests {
	
	@Test
	public void testConstructorWithDistanceFunction() {
		class TSPSubClass extends TSP {
			private final int LENGTH;
			private final double WIDTH;
			
			public TSPSubClass(int n, double w) {
				super(
					n,
					w,
					(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
					new SplittableRandom(42)
				);
				LENGTH = n;
				WIDTH = w;
			}
			
			@Override
			public double edgeCostForHeuristics(int i, int j) {
				// Tests don't use this.
				return -1;
			}
			
			@Override
			public double costAsDouble(Permutation c) {
				// tests don't use this
				return 1;
			}
			
			@Override
			public SolutionCostPair<Permutation> getSolutionCostPair(Permutation p) {
				// Tests don't use this.
				return new SolutionCostPair<Permutation>(p, -1, false);
			}
			
			public void validateDistanceFunction() {
				assertEquals(4.0, d.distance(2, 0, 6, 0), 1E-10);
				assertEquals(5.0, d.distance(7, 0, 2, 0), 1E-10);
				assertEquals(4.0, d.distance(0, 2, 0, 6), 1E-10);
				assertEquals(5.0, d.distance(0, 7, 0, 2), 1E-10);
				assertEquals(4, d.distanceAsInt(2, 0, 6, 0));
				assertEquals(5, d.distanceAsInt(7, 0, 2, 0));
				assertEquals(4, d.distanceAsInt(0, 2, 0, 6));
				assertEquals(5, d.distanceAsInt(0, 7, 0, 2));
				assertEquals(6.0, d.distance(2, 3, 6, 1), 1E-10);
				assertEquals(7.0, d.distance(7, 1, 2, 3), 1E-10);
				assertEquals(6.0, d.distance(3, 2, 1, 6), 1E-10);
				assertEquals(7.0, d.distance(1, 7, 3, 2), 1E-10);
				assertEquals(6, d.distanceAsInt(2, 3, 6, 1));
				assertEquals(7, d.distanceAsInt(7, 1, 2, 3));
				assertEquals(6, d.distanceAsInt(1, 2, 3, 6));
				assertEquals(7, d.distanceAsInt(3, 7, 1, 2));
			}
			
			public void validateCoordinates() {
				assertEquals(LENGTH, x.length);
				assertEquals(LENGTH, y.length);
				assertEquals(LENGTH, length());
				for (int i = 0; i < LENGTH; i++) {
					assertTrue(x[i] >= 0);
					assertTrue(y[i] >= 0);
					assertTrue(x[i] < WIDTH);
					assertTrue(y[i] < WIDTH);
					assertEquals(x[i], getX(i), 0.0);
					assertEquals(y[i], getY(i), 0.0);
				}
			}
		}
		TSPSubClass tsp = new TSPSubClass(10, 5);
		tsp.validateDistanceFunction();
		tsp.validateCoordinates();
		tsp = new TSPSubClass(5, 20);
		tsp.validateCoordinates();
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSPSubClass(1, 0.00001)
		);
		
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSPSubClass(2, 0.0)
		);
	}
	
	@Test
	public void testConstructorWithoutDistanceFunction() {
		class TSPSubClass extends TSP {
			private final int LENGTH;
			private final double WIDTH;
			
			public TSPSubClass(int n, double w) {
				super(
					n,
					w,
					new SplittableRandom(42)
				);
				LENGTH = n;
				WIDTH = w;
			}
			
			@Override
			public double edgeCostForHeuristics(int i, int j) {
				// Tests don't use this.
				return -1;
			}
			
			@Override
			public double costAsDouble(Permutation c) {
				// tests don't use this
				return 1;
			}
			
			@Override
			public SolutionCostPair<Permutation> getSolutionCostPair(Permutation p) {
				// Tests don't use this.
				return new SolutionCostPair<Permutation>(p, -1, false);
			}
			
			public void validateDistanceFunction() {
				assertEquals(4.0, d.distance(2, 0, 6, 0), 1E-10);
				assertEquals(5.0, d.distance(7, 0, 2, 0), 1E-10);
				assertEquals(4.0, d.distance(0, 2, 0, 6), 1E-10);
				assertEquals(5.0, d.distance(0, 7, 0, 2), 1E-10);
				assertEquals(4, d.distanceAsInt(2, 0, 6, 0));
				assertEquals(5, d.distanceAsInt(7, 0, 2, 0));
				assertEquals(4, d.distanceAsInt(0, 2, 0, 6));
				assertEquals(5, d.distanceAsInt(0, 7, 0, 2));
				assertEquals(Math.sqrt(20), d.distance(2, 3, 6, 1), 1E-10);
				assertEquals(Math.sqrt(29), d.distance(7, 1, 2, 3), 1E-10);
				assertEquals(Math.sqrt(20), d.distance(3, 2, 1, 6), 1E-10);
				assertEquals(Math.sqrt(29), d.distance(1, 7, 3, 2), 1E-10);
				assertEquals(4, d.distanceAsInt(2, 3, 6, 1));
				assertEquals(5, d.distanceAsInt(7, 1, 2, 3));
				assertEquals(4, d.distanceAsInt(1, 2, 3, 6));
				assertEquals(5, d.distanceAsInt(3, 7, 1, 2));
			}
			
			public void validateCoordinates() {
				assertEquals(LENGTH, x.length);
				assertEquals(LENGTH, y.length);
				assertEquals(LENGTH, length());
				for (int i = 0; i < LENGTH; i++) {
					assertTrue(x[i] >= 0);
					assertTrue(y[i] >= 0);
					assertTrue(x[i] < WIDTH);
					assertTrue(y[i] < WIDTH);
					assertEquals(x[i], getX(i), 0.0);
					assertEquals(y[i], getY(i), 0.0);
				}
			}
		}
		TSPSubClass tsp = new TSPSubClass(10, 5);
		tsp.validateDistanceFunction();
		tsp.validateCoordinates();
		tsp = new TSPSubClass(5, 20);
		tsp.validateCoordinates();
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSPSubClass(1, 0.00001)
		);
		
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSPSubClass(2, 0.0)
		);
	}
	
	// test cases for TSP.Double nested class
	
	@Test
	public void testDoubleCostFromArrays() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		double[][] expected = {
			{ 0.0, 4.0, Math.sqrt(52.0) },
			{ 4.0, 0.0, 6.0 },
			{ Math.sqrt(52.0), 6.0, 0.0 }
		};
		TSP.Double tsp = new TSP.Double(x, y);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		double expectedCost = 10 + Math.sqrt(52.0);
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(new double[1], new double[1])
		);
	}
	
	@Test
	public void testDoubleCostFromArraysWithDistance() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		double[][] expected = {
			{ 0.0, 4.0, 10.0 },
			{ 4.0, 0.0, 6.0 },
			{ 10.0, 6.0, 0.0 }
		};
		TSP.Double tsp = new TSP.Double(
			x, 
			y,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		double expectedCost = 20.0;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(new double[1], new double[1])
		);
	}
	
	@Test
	public void testDoubleCost() {
		int W = 5;
		int N = 10;
		TSP.Double tsp = new TSP.Double(N, W);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Double(N, W);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				double expected = Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(4, 6).cost(new Permutation(3))
		);
	}
	
	@Test
	public void testDoubleCostSeed() {
		int W = 5;
		int N = 10;
		TSP.Double tsp = new TSP.Double(N, W, 42);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Double(N, W, 42);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				double expected = Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Double(4, 6, 42).cost(new Permutation(5))
		);
	}
	
	@Test
	public void testDoubleCostWithDistance() {
		int W = 5;
		int N = 10;
		TSP.Double tsp = new TSP.Double(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Double(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]);
			for (int j = 0; j < N; j++) {
				double expected = Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]);
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
	}
	
	@Test
	public void testDoubleCostSeedWithDistance() {
		int W = 5;
		int N = 10;
		TSP.Double tsp = new TSP.Double(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(0.0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Double(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]);
			for (int j = 0; j < N; j++) {
				double expected = Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]);
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
	}
	
	// test cases for TSP.Integer nested class
	
	@Test
	public void testIntCostFromArrays() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		int[][] expected = {
			{ 0, 4, 7 },
			{ 4, 0, 6 },
			{ 7, 6, 0 }
		};
		TSP.Integer tsp = new TSP.Integer(x, y);
		assertEquals(0, tsp.minCost());
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		int expectedCost = 17;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(new double[1], new double[1])
		);
	}
	
	@Test
	public void testIntCostFromArraysWithDistance() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		int[][] expected = {
			{ 0, 4, 10 },
			{ 4, 0, 6 },
			{ 10, 6, 0 }
		};
		TSP.Integer tsp = new TSP.Integer(
			x, 
			y,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost());
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		int expectedCost = 20;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(new double[1], new double[1])
		);
	}
	
	@Test
	public void testIntCost() {
		int W = 5;
		int N = 10;
		TSP.Integer tsp = new TSP.Integer(N, W);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Integer(N, W);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k])));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j])));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(4, 6).cost(new Permutation(3))
		);
	}
	
	@Test
	public void testIntCostSeed() {
		int W = 5;
		int N = 10;
		TSP.Integer tsp = new TSP.Integer(N, W, 42);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Integer(N, W, 42);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k])));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j])));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.Integer(4, 6, 42).cost(new Permutation(5))
		);
	}
	
	@Test
	public void testIntCostWithDistance() {
		int W = 5;
		int N = 10;
		TSP.Integer tsp = new TSP.Integer(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Integer(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
	}
	
	@Test
	public void testIntCostSeedWithDistance() {
		int W = 5;
		int N = 10;
		TSP.Integer tsp = new TSP.Integer(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.Integer(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
	}
	
	// test cases for TSP.DoubleMatrix nested class
	
	@Test
	public void testDoubleCostMatrixFromArrays() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		double[][] expected = {
			{ 0.0, 4.0, Math.sqrt(52.0) },
			{ 4.0, 0.0, 6.0 },
			{ Math.sqrt(52.0), 6.0, 0.0 }
		};
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(x, y);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		double expectedCost = 10 + Math.sqrt(52.0);
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(new double[1], new double[1])
		);
	}
	
	@Test
	public void testDoubleCostMatrixFromArraysWithDistance() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		double[][] expected = {
			{ 0.0, 4.0, 10.0 },
			{ 4.0, 0.0, 6.0 },
			{ 10.0, 6.0, 0.0 }
		};
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(
			x, 
			y,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		double expectedCost = 20.0;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(new double[1], new double[1])
		);
	}
	
	@Test
	public void testDoubleCostMatrix() {
		int W = 5;
		int N = 10;
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(N, W);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.DoubleMatrix(N, W);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				double expected = Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(4, 6).cost(new Permutation(3))
		);
	}
	
	@Test
	public void testDoubleCostMatrixSeed() {
		int W = 5;
		int N = 10;
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(N, W, 42);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.DoubleMatrix(N, W, 42);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				double expected = Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.DoubleMatrix(4, 6, 42).cost(new Permutation(5))
		);
	}
	
	@Test
	public void testDoubleCostMatrixWithDistance() {
		int W = 5;
		int N = 10;
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.DoubleMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]);
			for (int j = 0; j < N; j++) {
				double expected = Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]);
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
	}
	
	@Test
	public void testDoubleCostMatrixSeedWithDistance() {
		int W = 5;
		int N = 10;
		TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(0, tsp.minCost(), 0.0);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.DoubleMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		double expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]);
			for (int j = 0; j < N; j++) {
				double expected = Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]);
				assertEquals(expected, tsp.d.distance(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]), 1E-10);
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm), 1E-10);
		assertEquals(expectedCost, tsp.value(perm), 1E-10);
	}
	
	// test cases for TSP.IntegerMatrix nested class
	
	@Test
	public void testIntCostMatrixFromArrays() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		int[][] expected = {
			{ 0, 4, 7 },
			{ 4, 0, 6 },
			{ 7, 6, 0 }
		};
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(x, y);
		assertEquals(0, tsp.minCost());
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		int expectedCost = 17;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(new double[1], new double[1])
		);
	}
	
	@Test
	public void testIntCostMatrixFromArraysWithDistance() {
		double[] x = { 2.0, 2.0, 8.0 };
		double[] y = { 5.0, 9.0, 9.0 };
		int[][] expected = {
			{ 0, 4, 10 },
			{ 4, 0, 6 },
			{ 10, 6, 0 }
		};
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(
			x, 
			y,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost());
		assertEquals(x.length, tsp.x.length);
		assertEquals(y.length, tsp.y.length);
		assertTrue(x != tsp.x);
		assertTrue(y != tsp.y);
		for (int i = 0; i < x.length; i++) {
			assertEquals(x[i], tsp.x[i], 0.0);
			assertEquals(y[i], tsp.y[i], 0.0);
			for (int j = 0; j < y.length; j++) {
				assertEquals(expected[i][j], tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected[i][j], tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		int[] permArray = { 1, 2, 0 };
		int expectedCost = 20;
		Permutation perm = new Permutation(permArray);
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(new double[2], new double[3])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(new double[1], new double[1])
		);
	}
	
	@Test
	public void testIntCostMatrix() {
		int W = 5;
		int N = 10;
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(N, W);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.IntegerMatrix(N, W);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k])));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j])));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(4, 6).cost(new Permutation(3))
		);
	}
	
	@Test
	public void testIntCostMatrixSeed() {
		int W = 5;
		int N = 10;
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(N, W, 42);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.IntegerMatrix(N, W, 42);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[k])*(tsp.x[i]-tsp.x[k]) + (tsp.y[i]-tsp.y[k])*(tsp.y[i]-tsp.y[k])));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.sqrt((tsp.x[i]-tsp.x[j])*(tsp.x[i]-tsp.x[j]) + (tsp.y[i]-tsp.y[j])*(tsp.y[i]-tsp.y[j])));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new TSP.IntegerMatrix(4, 6, 42).cost(new Permutation(5))
		);
	}
	
	@Test
	public void testIntCostMatrixWithDistance() {
		int W = 5;
		int N = 10;
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.IntegerMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2)
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
	}
	
	@Test
	public void testIntCostMatrixSeedWithDistance() {
		int W = 5;
		int N = 10;
		TSP.IntegerMatrix tsp = new TSP.IntegerMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(0, tsp.minCost());
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
		}
		W = 10;
		N = 3;
		tsp = new TSP.IntegerMatrix(N, W,
			(x1, y1, x2, y2) -> Math.abs(x1-x2) + Math.abs(y1-y2),
			42
		);
		assertEquals(N, tsp.x.length);
		assertEquals(N, tsp.y.length);
		int[] permArray = {1, 2, 0};
		Permutation perm = new Permutation(permArray);
		int expectedCost = 0;
		for (int i = 0; i < N; i++) {
			assertTrue(tsp.x[i] < W && tsp.x[i] >= 0.0);
			assertTrue(tsp.y[i] < W && tsp.y[i] >= 0.0);
			int k = (i+1)%N;
			expectedCost += (int)Math.round(Math.abs(tsp.x[i]-tsp.x[k]) + Math.abs(tsp.y[i]-tsp.y[k]));
			for (int j = 0; j < N; j++) {
				int expected = (int)Math.round(Math.abs(tsp.x[i]-tsp.x[j]) + Math.abs(tsp.y[i]-tsp.y[j]));
				assertEquals(expected, tsp.d.distanceAsInt(tsp.x[i], tsp.y[i], tsp.x[j], tsp.y[j]));
				assertEquals(expected, tsp.edgeCostForHeuristics(i,j), 1E-10);
			}
		}
		assertEquals(expectedCost, tsp.cost(perm));
		assertEquals(expectedCost, tsp.value(perm));
	}
}
