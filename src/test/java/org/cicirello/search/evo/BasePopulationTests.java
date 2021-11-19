/*
 * Chips-n-Salsa: A library of parallel self-adaptive local search algorithms.
 * Copyright (C) 2002-2021 Vincent A. Cicirello
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

import org.junit.*;
import static org.junit.Assert.*;
import org.cicirello.util.Copyable;
import org.cicirello.search.operators.Initializer;
import org.cicirello.search.SolutionCostPair;
import org.cicirello.search.ProgressTracker;
import org.cicirello.search.problems.Problem;
import org.cicirello.search.problems.OptimizationProblem;

/**
 * JUnit 4 test cases for BasePopulation.
 */
public class BasePopulationTests {
	
	private static final double EPSILON = 1e-10;
	
	
	@Test
	public void testBasePopulationDouble() {
		TestObject.reinit();
		ProgressTracker<TestObject> tracker = new ProgressTracker<TestObject>();
		TestSelectionOp selection = new TestSelectionOp();
		TestFitnessDouble f = new TestFitnessDouble();
		BasePopulation.Double<TestObject> pop = new BasePopulation.Double<TestObject>(
			10,
			new TestInitializer(),
			f,
			selection,
			tracker
		);
		assertTrue(tracker == pop.getProgressTracker());
		tracker = new ProgressTracker<TestObject>();
		pop.setProgressTracker(tracker);
		assertTrue(tracker == pop.getProgressTracker());
		
		assertEquals(10, pop.size());
		assertEquals(10, pop.mutableSize());
		pop.init();
		assertEquals(6.4, pop.getFitnessOfMostFit(), EPSILON);
		assertEquals(1.0/7.0, pop.getMostFit().getCostDouble(), EPSILON);
		assertEquals(6, pop.getMostFit().getSolution().id);
		assertFalse(selection.called);
		pop.select();
		assertTrue(selection.called);
		int[] expected = { 2, 3, 4, 5, 6, 5, 4, 3, 2, 1};
		for (int i = 0; i < 10; i++) {
			// fitnesses of original before selection.
			assertEquals(expected[9-i]+0.4, pop.getFitness(i), EPSILON);
			// subject to mutation to opposite order since we selected, which reversed.
			assertEquals(expected[i], pop.get(i).id);
			
		}
		assertEquals(6.4, pop.getFitnessOfMostFit(), EPSILON);
		assertEquals(1.0/7.0, pop.getMostFit().getCostDouble(), EPSILON);
		assertEquals(6, pop.getMostFit().getSolution().id);
		pop.replace();
		pop.select();
		for (int i = 0; i < 10; i++) {
			assertEquals(expected[9-i], pop.get(i).id);
			assertEquals(expected[i]+0.4, pop.getFitness(i), EPSILON);
		}
		assertEquals(6.4, pop.getFitnessOfMostFit(), EPSILON);
		assertEquals(1.0/7.0, pop.getMostFit().getCostDouble(), EPSILON);
		assertEquals(6, pop.getMostFit().getSolution().id);
		
		f.changeFitness(1);
		pop.updateFitness(0);
		f.changeFitness(10);
		pop.updateFitness(1);
		pop.replace();
		assertEquals(expected[9]+0.4+1, pop.getFitness(0), EPSILON);
		assertEquals(expected[8]+0.4+10, pop.getFitness(1), EPSILON);
		assertEquals(12.4, pop.getFitnessOfMostFit(), EPSILON);
		assertEquals(2, pop.getMostFit().getSolution().id);
		assertEquals(1.0/3.0, pop.getMostFit().getCostDouble(), EPSILON);
		
		f.changeFitness(12);
		BasePopulation.Double<TestObject> pop2 = pop.split();
		
		// orginal should be same
		assertEquals(expected[9]+0.4+1, pop.getFitness(0), EPSILON);
		assertEquals(expected[8]+0.4+10, pop.getFitness(1), EPSILON);
		assertEquals(12.4, pop.getFitnessOfMostFit(), EPSILON);
		assertEquals(2, pop.getMostFit().getSolution().id);
		assertEquals(1.0/3.0, pop.getMostFit().getCostDouble(), EPSILON);
		
		// trackers should be same
		assertTrue(pop.getProgressTracker() == pop2.getProgressTracker());
		
		assertEquals(10, pop2.size());
		assertEquals(10, pop2.mutableSize());
		pop2.init();
		for (int i = 0; i < 10; i++) {
			assertEquals(1-i+12+0.4, pop2.getFitness(i), EPSILON);
		}
	}
	
	
	
	private static class TestSelectionOp implements SelectionOperator {
		
		boolean called;
		public TestSelectionOp() {
			called = false;
		}
		public void select(PopulationFitnessVector.Integer fitnesses, int[] selected) {
			int next = selected.length - 1;
			for (int i = 0; i < selected.length; i++) {
				selected[i] = next;
				next--;
			}
			called = true;
		}
		public void select(PopulationFitnessVector.Double fitnesses, int[] selected) {
			int next = selected.length - 1;
			for (int i = 0; i < selected.length; i++) {
				selected[i] = next;
				next--;
			}
			called = true;
		}
	}
	
	private static class TestFitnessDouble implements FitnessFunction.Double<TestObject> {
		
		private TestProblemDouble problem;
		private int adjustment;
		
		public TestFitnessDouble() {
			problem = new TestProblemDouble();
		}
		
		public double fitness(TestObject c) {
			return c.id + 0.4 + adjustment;
		}
		
		public Problem<TestObject> getProblem() {
			return problem;
		}
		
		public void changeFitness(int adjustment) {
			this.adjustment = adjustment;
		}
	}
	
	private static class TestProblemDouble implements OptimizationProblem<TestObject> {
		public double cost(TestObject c) {
			return 1.0 / (1.0 + c.id);
		}
		public double value(TestObject c) {
			return cost(c);
		}
	}
	
	private static class TestInitializer implements Initializer<TestObject> {
		public TestObject createCandidateSolution() {
			return new TestObject();
		}
		public TestInitializer split() {
			return this;
		}
	}
	
	private static class TestObject implements Copyable<TestObject> {
		
		private static int IDENTIFIER = 0;
		private static boolean increase = true;
		public static void reinit() {
			increase = true;
			IDENTIFIER = 0;
		}
		
		private int id;
		
		public TestObject() {
			if (increase) IDENTIFIER++;
			else IDENTIFIER--;
			id = IDENTIFIER;
			if (IDENTIFIER == 6) increase = false;
		}
		
		private TestObject(int id) {
			this.id = id;
		}
		
		public TestObject copy() {
			return new TestObject(id);
		}
		
		public boolean equals(Object other) {
			return id == ((TestObject)other).id;
		}
	}
}