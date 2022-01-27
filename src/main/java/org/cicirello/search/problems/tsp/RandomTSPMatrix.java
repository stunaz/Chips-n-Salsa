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

import java.util.SplittableRandom;
import org.cicirello.search.problems.OptimizationProblem;
import org.cicirello.search.problems.IntegerCostOptimizationProblem;
import org.cicirello.permutations.Permutation;
import org.cicirello.math.rand.RandomIndexer;

/**
 * <p>This class and its nested classes implement the Traveling Salesperson Problem (TSP),
 * and its variant, the Asymmetric Traveling Salesperson Problem (ATSP),
 * by generating a random distance matrix.
 * The RandomTSPMatrix class provides two inner classes, one for edge 
 * costs that are floating-point valued (class {@link Double}), and
 * one for integer cost edges (class {@link Integer}). Both nested classes support both the
 * TSP and ATSP, and both also provide the option to control whether or not the distance
 * matrix satisfies the triangle inequality.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public abstract class RandomTSPMatrix extends BaseTSP {
	
	/*
	 * package private constructor for use by subclasses only
	 */
	RandomTSPMatrix() {}
	
	/**
	 * <p>This class implements the Traveling Salesperson Problem (TSP),
	 * and its variant, the Asymmetric Traveling Salesperson Problem (ATSP),
	 * by generating a random distance matrix, with integer cost edges.
	 * It supports both the
	 * TSP and ATSP, and also provides the option to control whether or not the distance
	 * matrix satisfies the triangle inequality.</p>
	 *
	 * <p>The random distance matrix is generated via an approach based on that
	 * of the paper: Cirasella J., Johnson D.S., McGeoch L.A., Zhang W. (2001) The Asymmetric 
	 * Traveling Salesman Problem: Algorithms, Instance Generators, and Tests. In <i>Algorithm Engineering and Experimentation (ALENEX 2001)</i>.
	 * There are some minor differences between the approach described in that paper and
	 * the approach of this class. This class generates random distances with a minimum of 1, whereas
	 * the approach described in that paper allows distances of 0.</p>
	 *
	 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
	 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
	 */
	public final static class Integer extends RandomTSPMatrix implements IntegerCostOptimizationProblem<Permutation> {
		
		private final int[][] d;
		
		/**
		 * Generates a random instance of either the TSP.
		 * The instances generated by this constructor may not satisfy the
		 * triangle inequality. If you desire an instance that satisfies the
		 * triangle inequality, see the other constructors. The distance
		 * matrix generated by this constructor is symmetric. See the other
		 * constructors for the ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [1, maxDistance].
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 1.
		 */
		public Integer(int n, int maxDistance) {
			this(n, maxDistance, true, false);
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 * The instances generated by this constructor may not satisfy the
		 * triangle inequality. If you desire an instance that satisfies the
		 * triangle inequality, see the other constructors.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [1, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 1.
		 */
		public Integer(int n, int maxDistance, boolean symmetric) {
			this(n, maxDistance, symmetric, false);
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [1, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 * @param triangleInequality Pass true if you want the generated distance matrix
		 *        to respect the triangle inequality, or false for purely random distances.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 1.
		 */
		public Integer(int n, int maxDistance, boolean symmetric, boolean triangleInequality) {
			this(n, maxDistance, symmetric, triangleInequality, new SplittableRandom());
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [1, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 * @param triangleInequality Pass true if you want the generated distance matrix
		 *        to respect the triangle inequality, or false for purely random distances.
		 * @param seed The seed for the random number generator to enable reproducing the
		 *        same instance for experiment reproducibility.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 1.
		 */
		public Integer(int n, int maxDistance, boolean symmetric, boolean triangleInequality, long seed) {
			this(n, maxDistance, symmetric, triangleInequality, new SplittableRandom(seed));
		}
		
		/**
		 * Although the focus of this class is generating random TSP and ATSP instances, this 
		 * constructor enables specifying the distance matrix directly.
		 *
		 * @param distance The distance matrix, such that distance[i][j] is the distance from
		 *    city i to city j. The distance matrix must be square, with same number of rows as columns,
		 *    and whose dimension determines number of cities. Dimensions must be at least 2 by 2.
		 *
		 * @throws IllegalArgumentException if distance is not at least a 2 by 2 array.
		 * @throws IllegalArgumentException if number of rows is not same as number of columns, or
		 *      if rows don't all have same length.
		 */
		public Integer(int[][] distance) {
			final int n = distance.length;
			if (n < 2) throw new IllegalArgumentException("distance must be at least 2 by 2");
			d = new int[n][n];
			for (int i = 0; i < n; i++) {
				if (distance[i].length != n) {
					throw new IllegalArgumentException("num rows and columns must be the same");
				}
				System.arraycopy(distance[i], 0, d[i], 0, n);
			}
		}
		
		/*
		 * internal private constructor
		 */
		private Integer(int n, int maxDistance, boolean symmetric, boolean triangleInequality, SplittableRandom gen) {
			if (n < 2) throw new IllegalArgumentException("n must be at least 2");
			if (maxDistance < 1) throw new IllegalArgumentException("maxDistance must be at least 1");
			d = new int[n][n];
			if (symmetric) {
				symmetricInitD(maxDistance, gen);
				if (triangleInequality) {
					symmetricCloseUnderShortestPaths();
				}
			} else {
				asymmetricInitD(maxDistance, gen);
				if (triangleInequality) {
					asymmetricCloseUnderShortestPaths();
				}
			}
		}
		
		/**
		 * Gets the distance (i.e., cost) of an edge.
		 *
		 * @param i The source city.
		 * @param j The destination city.
		 * @return The distance from i to j
		 */
		public final int getDistance(int i, int j) {
			return d[i][j];
		}
		
		/**
		 * Gets the number of cities in the TSP instance.
		 * @return number of cities
		 */
		@Override
		public final int length() {
			return d.length;
		}
		
		@Override
		public int cost(Permutation candidate) {
			int total = d[candidate.get(candidate.length()-1)][candidate.get(0)];
			for (int k = 1; k < candidate.length(); k++) {
				total = total + d[candidate.get(k-1)][candidate.get(k)];
			}
			return total;
		}
		
		@Override
		public int value(Permutation candidate) {
			return cost(candidate);
		}
		
		@Override
		public int minCost() {
			return 0;
		}
		
		/*
		 * package private to support implementing heuristics in same package.
		 */
		@Override
		final double edgeCostForHeuristics(int i, int j) {
			return d[i][j];
		}
		
		private void symmetricInitD(int maxDistance, SplittableRandom gen) {
			for (int i = 0; i < d.length; i++) {
				for (int j = i + 1; j < d.length; j++) {
					d[i][j] = d[j][i] = 1 + RandomIndexer.nextInt(maxDistance, gen);
				}
			}
		}
		
		private void asymmetricInitD(int maxDistance, SplittableRandom gen) {
			for (int i = 0; i < d.length; i++) {
				for (int j = 0; j < d.length; j++) {
					if (i != j) {
						d[i][j] = 1 + RandomIndexer.nextInt(maxDistance, gen);
					}
				}
			}
		}
		
		private void symmetricCloseUnderShortestPaths() {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int i = 0; i < d.length; i++) {
					for (int j = i + 1; j < d.length; j++) {
						for (int k = 0; k < d.length; k++) {
							if (k != i && k != j) {
								int sum = d[i][k] + d[k][j];
								if (d[i][j] > sum) {
									d[i][j] = d[j][i] = sum; 
									changed = true;
								}
							}
						}						
					}
				}
			}
		}
		
		private void asymmetricCloseUnderShortestPaths() {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int i = 0; i < d.length; i++) {
					for (int j = 0; j < d.length; j++) {
						if (i != j) {
							for (int k = 0; k < d.length; k++) {
								if (k != i && k != j) {
									int sum = d[i][k] + d[k][j];
									if (d[i][j] > sum) {
										d[i][j] = sum; 
										changed = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * <p>This class implements the Traveling Salesperson Problem (TSP),
	 * and its variant, the Asymmetric Traveling Salesperson Problem (ATSP),
	 * by generating a random distance matrix, with floating-point cost edges.
	 * It supports both the
	 * TSP and ATSP, and also provides the option to control whether or not the distance
	 * matrix satisfies the triangle inequality.</p>
	 *
	 * <p>The random distance matrix is generated via an approach based on that
	 * of the paper: Cirasella J., Johnson D.S., McGeoch L.A., Zhang W. (2001) The Asymmetric 
	 * Traveling Salesman Problem: Algorithms, Instance Generators, and Tests. In <i>Algorithm Engineering and Experimentation (ALENEX 2001)</i>.
	 * There are some minor differences between the approach described in that paper and
	 * the approach of this class. This class generates random floating-point distances, whereas
	 * the approach described in that paper uses integer valued distances.</p>
	 *
	 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
	 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
	 */
	public final static class Double extends RandomTSPMatrix implements OptimizationProblem<Permutation> {
		
		private final double[][] d;
		
		/**
		 * Generates a random instance of either the TSP.
		 * The instances generated by this constructor may not satisfy the
		 * triangle inequality. If you desire an instance that satisfies the
		 * triangle inequality, see the other constructors. The distance
		 * matrix generated by this constructor is symmetric. See the other
		 * constructors for the ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [0, maxDistance].
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 0.0.
		 */
		public Double(int n, double maxDistance) {
			this(n, maxDistance, true, false);
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 * The instances generated by this constructor may not satisfy the
		 * triangle inequality. If you desire an instance that satisfies the
		 * triangle inequality, see the other constructors.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [0, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 0.0.
		 */
		public Double(int n, double maxDistance, boolean symmetric) {
			this(n, maxDistance, symmetric, false);
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [0, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 * @param triangleInequality Pass true if you want the generated distance matrix
		 *        to respect the triangle inequality, or false for purely random distances.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 0.0.
		 */
		public Double(int n, double maxDistance, boolean symmetric, boolean triangleInequality) {
			this(n, maxDistance, symmetric, triangleInequality, new SplittableRandom());
		}
		
		/**
		 * Generates a random instance of either the TSP or ATSP.
		 *
		 * @param n The number of cities.
		 * @param maxDistance The maximum distance between cities. The edge costs
		 *        between pairs of cities are uniform in the interval [0, maxDistance].
		 * @param symmetric Pass true for the TSP, or false for the ATSP.
		 * @param triangleInequality Pass true if you want the generated distance matrix
		 *        to respect the triangle inequality, or false for purely random distances.
		 * @param seed The seed for the random number generator to enable reproducing the
		 *        same instance for experiment reproducibility.
		 *
		 * @throws IllegalArgumentException if n &lt; 2.
		 * @throws IllegalArgumentException if maxDistance &lt; 0.0.
		 */
		public Double(int n, double maxDistance, boolean symmetric, boolean triangleInequality, long seed) {
			this(n, maxDistance, symmetric, triangleInequality, new SplittableRandom(seed));
		}
		
		/**
		 * Although the focus of this class is generating random TSP and ATSP instances, this 
		 * constructor enables specifying the distance matrix directly.
		 *
		 * @param distance The distance matrix, such that distance[i][j] is the distance from
		 *    city i to city j. The distance matrix must be square, with same number of rows as columns,
		 *    and whose dimension determines number of cities. Dimensions must be at least 2 by 2.
		 *
		 * @throws IllegalArgumentException if distance is not at least a 2 by 2 array.
		 * @throws IllegalArgumentException if number of rows is not same as number of columns, or
		 *      if rows don't all have same length.
		 */
		public Double(double[][] distance) {
			final int n = distance.length;
			if (n < 2) throw new IllegalArgumentException("distance must be at least 2 by 2");
			d = new double[n][n];
			for (int i = 0; i < n; i++) {
				if (distance[i].length != n) {
					throw new IllegalArgumentException("num rows and columns must be the same");
				}
				System.arraycopy(distance[i], 0, d[i], 0, n);
			}
		}
		
		/*
		 * internal private constructor
		 */
		private Double(int n, double maxDistance, boolean symmetric, boolean triangleInequality, SplittableRandom gen) {
			if (n < 2) throw new IllegalArgumentException("n must be at least 2");
			if (maxDistance < 0) throw new IllegalArgumentException("maxDistance must be non-negative");
			d = new double[n][n];
			if (symmetric) {
				symmetricInitD(maxDistance + Math.ulp(maxDistance), gen);
				if (triangleInequality) {
					symmetricCloseUnderShortestPaths();
				}
			} else {
				asymmetricInitD(maxDistance + Math.ulp(maxDistance), gen);
				if (triangleInequality) {
					asymmetricCloseUnderShortestPaths();
				}
			}
		}
		
		/**
		 * Gets the distance (i.e., cost) of an edge.
		 *
		 * @param i The source city.
		 * @param j The destination city.
		 * @return The distance from i to j
		 */
		public final double getDistance(int i, int j) {
			return d[i][j];
		}
		
		/**
		 * Gets the number of cities in the TSP instance.
		 * @return number of cities
		 */
		@Override
		public final int length() {
			return d.length;
		}
		
		@Override
		public double cost(Permutation candidate) {
			double total = d[candidate.get(candidate.length()-1)][candidate.get(0)];
			for (int k = 1; k < candidate.length(); k++) {
				total = total + d[candidate.get(k-1)][candidate.get(k)];
			}
			return total;
		}
		
		@Override
		public double value(Permutation candidate) {
			return cost(candidate);
		}
		
		@Override
		public double minCost() {
			return 0;
		}
		
		/*
		 * package private to support implementing heuristics in same package.
		 */
		@Override
		final double edgeCostForHeuristics(int i, int j) {
			return d[i][j];
		}
		
		private void symmetricInitD(double maxDistance, SplittableRandom gen) {
			for (int i = 0; i < d.length; i++) {
				for (int j = i + 1; j < d.length; j++) {
					d[i][j] = d[j][i] = gen.nextDouble(maxDistance);
				}
			}
		}
		
		private void asymmetricInitD(double maxDistance, SplittableRandom gen) {
			for (int i = 0; i < d.length; i++) {
				for (int j = 0; j < d.length; j++) {
					if (i != j) {
						d[i][j] = gen.nextDouble(maxDistance);
					}
				}
			}
		}
		
		private void symmetricCloseUnderShortestPaths() {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int i = 0; i < d.length; i++) {
					for (int j = i + 1; j < d.length; j++) {
						for (int k = 0; k < d.length; k++) {
							if (k != i && k != j) {
								double sum = d[i][k] + d[k][j];
								if (d[i][j] > sum) {
									d[i][j] = d[j][i] = sum; 
									changed = true;
								}
							}
						}						
					}
				}
			}
		}
		
		private void asymmetricCloseUnderShortestPaths() {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int i = 0; i < d.length; i++) {
					for (int j = 0; j < d.length; j++) {
						if (i != j) {
							for (int k = 0; k < d.length; k++) {
								if (k != i && k != j) {
									double sum = d[i][k] + d[k][j];
									if (d[i][j] > sum) {
										d[i][j] = sum; 
										changed = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
