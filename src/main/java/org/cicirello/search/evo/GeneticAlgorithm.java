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

package org.cicirello.search.evo;

import org.cicirello.search.representations.BitVector;
import org.cicirello.search.ProgressTracker;
import org.cicirello.search.operators.Initializer;
import org.cicirello.search.operators.CrossoverOperator;
import org.cicirello.search.operators.bits.BitVectorInitializer;
import org.cicirello.search.operators.bits.BitFlipMutation;

/**
 * <p>This class is an implementation of a genetic algorithm (GA) with the common
 * bit vector representation of solutions to optimization problems, and the
 * generational model where children replace their parents each generation.
 * It uses the usual bit flip mutation, where each bit of each member of the
 * population is mutated (flipped) with some probability, known as the mutation
 * rate, each generation. All other genetic operators, such as crossover and
 * selection are configurable. This GeneticAlgorithm class can also be configured
 * with or without the use of elitism. With elitism, a specified number of the
 * most fit members of the population survive into the next generation unaltered.</p>
 *
 * <p>The library also includes other classes for evolutionary algorithms that
 * may be more relevant depending upon your use-case. For example, see the
 * {@link SimpleGeneticAlgorithm} class for the form of GA known as the Simple GA,
 * the {@link MutationOnlyGeneticAlgorithm} class if all you want to use is mutation and no
 * crossover, and the {@link GenerationalEvolutionaryAlgorithm} class if you want
 * to optimize something other than BitVectors or if you want even greater flexibility
 * in configuring your evolutionary search.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class GeneticAlgorithm extends GenerationalEvolutionaryAlgorithm<BitVector> {
	
	// Constructors with Initializer.
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, initializer, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount, ProgressTracker<BitVector> tracker) {
		super(n, new BitFlipMutation(mutationRate), 1.0, crossover, crossoverRate, initializer, f, selection, eliteCount, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, initializer, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount, ProgressTracker<BitVector> tracker) {
		super(n, new BitFlipMutation(mutationRate), 1.0, crossover, crossoverRate, initializer, f, selection, eliteCount, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws NullPointerException if any of crossover, initializer, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, ProgressTracker<BitVector> tracker) {
		this(n, initializer, f, mutationRate, crossover, crossoverRate, selection, 0, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws NullPointerException if any of crossover, initializer, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, ProgressTracker<BitVector> tracker) {
		this(n, initializer, f, mutationRate, crossover, crossoverRate, selection, 0, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, initializer, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount) {
		super(n, new BitFlipMutation(mutationRate), 1.0, crossover, crossoverRate, initializer, f, selection, eliteCount);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, initializer, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount) {
		super(n, new BitFlipMutation(mutationRate), 1.0, crossover, crossoverRate, initializer, f, selection, eliteCount);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws NullPointerException if any of crossover, initializer, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection) {
		this(n, initializer, f, mutationRate, crossover, crossoverRate, selection, 0);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param initializer An initializer for generating random initial population members.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws NullPointerException if any of crossover, initializer, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, Initializer<BitVector> initializer, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection) {
		this(n, initializer, f, mutationRate, crossover, crossoverRate, selection, 0);
	}
	
	// Constructors specifying length of bit vectors instead of Initializer object.
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount, ProgressTracker<BitVector> tracker) {
		this(n, new BitVectorInitializer(bitLength), f, mutationRate, crossover, crossoverRate, selection, eliteCount, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount, ProgressTracker<BitVector> tracker) {
		this(n, new BitVectorInitializer(bitLength), f, mutationRate, crossover, crossoverRate, selection, eliteCount, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative
	 * @throws NullPointerException if any of crossover, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, ProgressTracker<BitVector> tracker) {
		this(n, bitLength, f, mutationRate, crossover, crossoverRate, selection, 0, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param tracker A ProgressTracker.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative.
	 * @throws NullPointerException if any of crossover, f, selection, or tracker are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, ProgressTracker<BitVector> tracker) {
		this(n, bitLength, f, mutationRate, crossover, crossoverRate, selection, 0, tracker);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount) {
		this(n, new BitVectorInitializer(bitLength), f, mutationRate, crossover, crossoverRate, selection, eliteCount);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 * @param eliteCount The number of elite population members. Pass 0 for no elitism. eliteCount must be less than n.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative.
	 * @throws IllegalArgumentException if eliteCount is greater than or equal to n.
	 * @throws NullPointerException if any of crossover, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection, int eliteCount) {
		this(n, new BitVectorInitializer(bitLength), f, mutationRate, crossover, crossoverRate, selection, eliteCount);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type double, the {@link FitnessFunction.Double} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative
	 * @throws NullPointerException if any of crossover, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Double<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection) {
		this(n, bitLength, f, mutationRate, crossover, crossoverRate, selection, 0);
	}
	
	/**
	 * <p>Initializes a genetic algorithm with a generational model where children replace the parents,
	 * using the standard bit flip mutation. All other characteristics, such as crossover operator and
	 * selection operator are configurable. This constructor supports fitness functions
	 * with fitnesses of type int, the {@link FitnessFunction.Integer} interface.</p>
	 *
	 * @param n The population size.
	 * @param bitLength The length of each bit vector.
	 * @param f The fitness function.
	 * @param mutationRate The per-bit probability of flipping a bit. Each bit of each member of the 
	 *    population is flipped with this probability, and the decisions to flip bits are independent.
	 * @param crossover The crossover operator.
	 * @param crossoverRate The probability that a pair of parents undergo crossover.
	 * @param selection The selection operator.
	 *
	 * @throws IllegalArgumentException if n is less than 1.
	 * @throws IllegalArgumentException if mutationRate &le; 0 or if mutationRate &ge; 1.
	 * @throws IllegalArgumentException if crossoverRate is less than 0.
	 * @throws IllegalArgumentException if bitLength is negative.
	 * @throws NullPointerException if any of crossover, f, or selection are null.
	 */
	public GeneticAlgorithm(int n, int bitLength, FitnessFunction.Integer<BitVector> f, double mutationRate, CrossoverOperator<BitVector> crossover, double crossoverRate, SelectionOperator selection) {
		this(n, bitLength, f, mutationRate, crossover, crossoverRate, selection, 0);
	}
	
	/*
	 * Internal constructor for use by split method
	 * package private so subclasses in same package can use it for initialization for their own split methods.
	 */
	GeneticAlgorithm(GeneticAlgorithm other) {
		super(other);
		// Just call super constructor to perform split() logic. This
		// subclass doesn't currently maintain any additional state.
		// Only reason for overriding split() method, and thus providing this
		// constructor is to ensure runtime type of split instance is same,
		// although strictly speaking it would still function correctly otherwise.
	}
	
	@Override
	public GeneticAlgorithm split() {
		return new GeneticAlgorithm(this);
	}
}
