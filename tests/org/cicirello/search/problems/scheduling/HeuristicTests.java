/*
 * Chips-n-Salsa: A library of parallel self-adaptive local search algorithms.
 * Copyright (C) 2002-2020  Vincent A. Cicirello
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

package org.cicirello.search.problems.scheduling;

import org.junit.*;
import static org.junit.Assert.*;

import org.cicirello.permutations.Permutation;
import org.cicirello.search.ss.PartialPermutation;
import org.cicirello.search.ss.IncrementalEvaluation;

/**
 * JUnit tests for scheduling heuristics.
 */
public class HeuristicTests {
	
	@Test
	public void testEDD() {
		int[] duedates = { 3, 0, 1, 7 };
		double[] expected = {0.25, 1.0, 0.5, 0.125};
		FakeProblemDuedates problem = new FakeProblemDuedates(duedates);
		EarliestDueDate h = new EarliestDueDate(problem);
		for (int j = 0; j < duedates.length; j++) {
			assertEquals(expected[j], h.h(null, j, null), 1E-10);
		}
	}
	
	@Test
	public void testMST() {
		int[] p        = { 2, 4, 3, 5 };
		int[] duedates = { 3, 8, 5, 2 };
		double[] expected = { 7, 4, 6, 11};
		FakeProblemDuedates problem = new FakeProblemDuedates(duedates, p);
		MinimumSlackTime h = new MinimumSlackTime(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		for (int j = 0; j < duedates.length; j++) {
			assertEquals(expected[j], h.h(null, j, inc), 1E-10);
		}
		
		PartialPermutation partial = new PartialPermutation(expected.length);
		problem = new FakeProblemDuedates(duedates, p, 3);
		h = new MinimumSlackTime(problem);
		inc = h.createIncrementalEvaluation();
		for (int j = 0; j < duedates.length; j++) {
			assertEquals(expected[j]+3, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testWSPT() {
		double e = WeightedShortestProcessingTime.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p);
		WeightedShortestProcessingTime h = new WeightedShortestProcessingTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(null, j, null), 1E-10);
		}
	}
	
	@Test
	public void testSmallestSetup() {
		double e = SmallestSetup.MIN_H;
		int highS = (int)Math.ceil(1 / e)*2;
		int[][] s = {
			{0, highS, highS, highS, highS},
			{1, 1, 7, 3, 1},
			{1, 1, 3, 1, 1},
			{1, 1, 1, 7, 1},
			{1, 1, 1, 1, 15}
		};
		int[] w =    { 7, 8, 2, 10, 4};
		int[] p =    { 2, 5, 9, 2, 10};
		double[] expected = { 1, 0.5, 0.25, 0.125, 0.0625 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, s);
		SmallestSetup h = new SmallestSetup(problem);
		PartialPermutation partial = new PartialPermutation(expected.length);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(partial, j, null), 1E-10);
		}
		partial.extend(0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals(e, h.h(partial, j, null), 1E-10);
		}
		partial.extend(1);
		double[] expected2 = { 999, 999, 0.125, 0.25, 0.5 };
		for (int j = 2; j < expected.length; j++) {
			assertEquals(expected2[j], h.h(partial, j, null), 1E-10);
		}
	}
	
	@Test
	public void testSmallestNormalizedSetup() {
		double e = SmallestNormalizedSetup.MIN_H;
		int highS = (int)Math.ceil(1 / e)*2;
		int[][] s = {
			{0, 3, 2, 4, highS},
			{1, 3, 2, 4, 1},
			{1, 3, 9, 4, 1},
			{1, 3, 1, 999, 1},
			{1, 3, 1, 4, 999} // don't test last 2 jobs
		};
		int[] w =    { 7, 8, 2, 10, 4};
		int[] p =    { 2, 5, 9, 2, 10};
		double[] expected = { 1, 0.5, 0.25 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, s);
		SmallestNormalizedSetup h = new SmallestNormalizedSetup(problem);
		PartialPermutation partial = new PartialPermutation(p.length);
		for (int j = 0; j < expected.length; j++) {
			assertEquals("j:"+j, expected[j], h.h(partial, j, null), 1E-10);
		}
	}
	
	@Test
	public void testSmallestTwoJobSetup() {
		double e = SmallestTwoJobSetup.MIN_H;
		int highS = (int)Math.ceil(1 / e)*2;
		int[][] s = {
			{0, highS, highS, 0, highS},
			{1, 0, 7, 3, 1},
			{9, 9, 3, 0, 9},
			{7, 1, 7, 6, 7},
			{5, 5, 2, 5, 13}
		};
		int[] w =    { 7, 8, 2, 10, 4};
		int[] p =    { 2, 5, 9, 2, 10};
		double[] expected = { 1, 0.5, 0.25, 0.125, 0.0625 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, s);
		SmallestTwoJobSetup h = new SmallestTwoJobSetup(problem);
		PartialPermutation partial = new PartialPermutation(expected.length);
		for (int j = 0; j < expected.length; j++) {
			assertEquals("scheduled first", expected[j], h.h(partial, j, null), 1E-10);
		}
		partial.extend(0);
		for (int j = 1; j < expected.length; j++) {
			if (s[0][j]==highS)
				assertEquals(e, h.h(partial, j, null), 1E-10);
			else 
				assertEquals(0.5, h.h(partial, j, null), 1E-10);
		}
	}
	
	@Test
	public void testWSPTSetupAdjusted() {
		double e = WeightedShortestProcessingPlusSetupTime.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		PartialPermutation partial = new PartialPermutation(expected.length);
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p);
		WeightedShortestProcessingPlusSetupTime h = new WeightedShortestProcessingPlusSetupTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(partial, j, null), 1E-10);
		}
		
		int[] ps =    { 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP - 1};
		problem = new FakeProblemWeightsPTime(w, ps, 0, 1);
		h = new WeightedShortestProcessingPlusSetupTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(partial, j, null), 1E-10);
		}
	}
	
	@Test
	public void testSPT() {
		double e = ShortestProcessingTime.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 1, 0.5, 0.25, 0.125, 1, 0.5, 0.25, 0.125, 1, 0.5, 0.25, 0.125, e};
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p);
		ShortestProcessingTime h = new ShortestProcessingTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(null, j, null), 1E-10);
		}
	}
	
	@Test
	public void testSPTSetupAdjusted() {
		double e = ShortestProcessingPlusSetupTime.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 1, 0.5, 0.25, 0.125, 1, 0.5, 0.25, 0.125, 1, 0.5, 0.25, 0.125, e};
		PartialPermutation partial = new PartialPermutation(expected.length);
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p);
		ShortestProcessingPlusSetupTime h = new ShortestProcessingPlusSetupTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(partial, j, null), 1E-10);
		}
		
		int[] ps =    { 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP - 1};
		problem = new FakeProblemWeightsPTime(w, ps, 0, 1);
		h = new ShortestProcessingPlusSetupTime(problem);
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], h.h(partial, j, null), 1E-10);
		}
	}
	
	@Test
	public void testWSPT2() {
		double e = WeightedShortestProcessingTimeLateOnly.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		PartialPermutation partial = new PartialPermutation(expected.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals("j:"+j, expected[j], h.h(partial, j, inc), 1E-10);
		}
		// All on time tests
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedShortestProcessingTimeLateOnly(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals(e, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testWSPT2SetupAdjusted() {
		double e = WeightedShortestProcessingPlusSetupTimeLateOnly.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		PartialPermutation partial = new PartialPermutation(expected.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedShortestProcessingPlusSetupTimeLateOnly h = new WeightedShortestProcessingPlusSetupTimeLateOnly(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals("j:"+j, expected[j], h.h(partial, j, inc), 1E-10);
		}
		// All on time tests
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedShortestProcessingPlusSetupTimeLateOnly(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals(e, h.h(partial, j, inc), 1E-10);
		}
		// Repeat with setups
		int[] ps =    { 0, 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP-1};
		problem = new FakeProblemWeightsPTime(w, ps, 0, 1);
		h = new WeightedShortestProcessingPlusSetupTimeLateOnly(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals("j:"+j, expected[j], h.h(partial, j, inc), 1E-10);
		}
		problem = new FakeProblemWeightsPTime(w, ps, 20, 1);
		h = new WeightedShortestProcessingPlusSetupTimeLateOnly(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected.length; j++) {
			assertEquals(e, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testWeightedCriticalRatio() {
		double e = WeightedCriticalRatio.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		for (int i = 1; i < p.length; i++) slack[i] = 20-p[i]-p[0];
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedCriticalRatio h = new WeightedCriticalRatio(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// All on time tests
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCriticalRatio(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double expected = expected0[j]/(1.0+slack[j]/p[j]);
			if (slack[j] <= 0) expected = e;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testWeightedCriticalRatioSetupAdjusted() {
		double e = WeightedCriticalRatioSetupAdjusted.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		for (int i = 1; i < p.length; i++) slack[i] = 20-p[i]-p[0];
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedCriticalRatioSetupAdjusted h = new WeightedCriticalRatioSetupAdjusted(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// All on time tests
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCriticalRatioSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double expected = expected0[j]/(1.0+slack[j]/p[j]);
			if (slack[j] <= 0) expected = e;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// Repeat with setups
		int[] ps =    { 0, 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP-1};
		problem = new FakeProblemWeightsPTime(w, ps, 0, 1);
		h = new WeightedCriticalRatioSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		problem = new FakeProblemWeightsPTime(w, ps, 20, 1);
		h = new WeightedCriticalRatioSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double expected = expected0[j]/(1.0+slack[j]/p[j]);
			if (slack[j] <= 0) expected = e;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testMontagne() {
		double e = Montagne.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		int pSum = 0;
		for (int i = 1; i < p.length; i++) pSum += p[i];
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All d=0
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		Montagne h = new Montagne(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("d=0, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// All d = pSum
		problem = new FakeProblemWeightsPTime(w, p, pSum);
		h = new Montagne(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("d=pSum, j:"+j, e, h.h(partial, j, inc), 1E-10);
		}
		// All d = pSum / 2
		problem = new FakeProblemWeightsPTime(w, p, pSum/2);
		h = new Montagne(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - pSum/2/(1.0*pSum);
			double expected = expected0[j]*correction;
			assertEquals("d=pSum, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testCOVERT() {
		double e = WeightedCostOverTime.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
		}
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedCostOverTime h = new WeightedCostOverTime(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCostOverTime(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.5 * slack[j] / p[j];
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCostOverTime(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.25 * slack[j] / p[j];
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testCOVERTSetupAdjustedS0() {
		double e = WeightedCostOverTimeSetupAdjusted.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
		}
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedCostOverTimeSetupAdjusted h = new WeightedCostOverTimeSetupAdjusted(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCostOverTimeSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.5 * slack[j] / p[j];
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new WeightedCostOverTimeSetupAdjusted(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.25 * slack[j] / p[j];
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testCOVERTSetupAdjustedS1() {
		double e = WeightedCostOverTimeSetupAdjusted.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 0, 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP-1};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0]-2;
			if (slack[i] < 0) slack[i] = 0;
		}
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 1);
		WeightedCostOverTimeSetupAdjusted h = new WeightedCostOverTimeSetupAdjusted(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20, 1);
		h = new WeightedCostOverTimeSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.5 * slack[j] / (1+p[j]);
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20, 1);
		h = new WeightedCostOverTimeSetupAdjusted(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = 1.0 - 0.25 * slack[j] / (1+p[j]);
			if (correction <= 0) correction = 0;
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testATC() {
		double e = ApparentTardinessCost.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		ApparentTardinessCost h = new ApparentTardinessCost(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ApparentTardinessCost(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ApparentTardinessCost(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testATCSetupAdjustedS0() {
		double e = ApparentTardinessCostSetupAdjusted.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		ApparentTardinessCostSetupAdjusted h = new ApparentTardinessCostSetupAdjusted(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ApparentTardinessCostSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ApparentTardinessCostSetupAdjusted(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testATCSetupAdjustedS1() {
		double e = ApparentTardinessCostSetupAdjusted.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 0, 0, 1, 3, 7, 0, 1, 3, 7, 0, 1, 3, 7, highP-1};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0]-2;
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 1);
		ApparentTardinessCostSetupAdjusted h = new ApparentTardinessCostSetupAdjusted(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k default of 2
		problem = new FakeProblemWeightsPTime(w, p, 20, 1);
		h = new ApparentTardinessCostSetupAdjusted(problem);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4
		problem = new FakeProblemWeightsPTime(w, p, 20, 1);
		h = new ApparentTardinessCostSetupAdjusted(problem, 4);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testATCS0() {
		double e = ATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests, k1=2, k2=7?shouldn't matter
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		ATCS h = new ATCS(problem, 2, 7);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k1=2, k2=7?shouldn't matter
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ATCS(problem, 2, 7);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4, k2=7?shouldn't matter
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new ATCS(problem, 4, 7);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testATCS1() {
		double e = ATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		// All late tests, k1=2, k2=1
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 4);
		ATCS h = new ATCS(problem, 2, 1);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		double sAve = ((ATCS.IncrementalStatsCalculator)inc).averageSetupTime();
		for (int j = 1; j < expected0.length; j++) {
			double correction = expected0[j]*Math.exp(-4.0/sAve);
			assertEquals("negativeSlack, j:"+j, correction < e ? e : correction, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k1=2, k2=2
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new ATCS(problem, 2, 2);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve)*Math.exp(-2.0/sAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-4);
		}
		// d=20, k=4, k2=3
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new ATCS(problem, 4, 3);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve)*Math.exp(-4.0/3.0/sAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-4);
		}
	}
	
	@Test
	public void testATCSdefault() {
		double e = ATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		// All late tests, 
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 4);
		ATCS h = new ATCS(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		double sAve = ((ATCS.IncrementalStatsCalculator)inc).averageSetupTime();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("negativeSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
		// d=20, 
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new ATCS(problem);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("positiveSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
		// d=20,
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new ATCS(problem);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("positiveSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
	}
	
	@Test
	public void testDynamicATCS0() {
		double e = DynamicATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i]-p[0];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		//Doesn't really matter: partial.extend(0);
		// All late tests, k1=2, k2=7?shouldn't matter
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		DynamicATCS h = new DynamicATCS(problem, 2, 7);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			assertEquals("negativeSlack, j:"+j, expected0[j], h.h(partial, j, inc), 1E-10);
		}
		// d=20, k1=2, k2=7?shouldn't matter
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new DynamicATCS(problem, 2, 7);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k=4, k2=7?shouldn't matter
		problem = new FakeProblemWeightsPTime(w, p, 20);
		h = new DynamicATCS(problem, 4, 7);
		inc = h.createIncrementalEvaluation();
		inc.extend(partial, 0);
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-10);
		}
	}
	
	@Test
	public void testDynamicATCS1() {
		double e = DynamicATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		// All late tests, k1=2, k2=1
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 4);
		DynamicATCS h = new DynamicATCS(problem, 2, 1);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		double sAve = ((DynamicATCS.IncrementalStatsCalculator)inc).averageSetupTime();
		for (int j = 1; j < expected0.length; j++) {
			double correction = expected0[j]*Math.exp(-4.0/sAve);
			assertEquals("negativeSlack, j:"+j, correction < e ? e : correction, h.h(partial, j, inc), 1E-10);
		}
		// d=20, k1=2, k2=2
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new DynamicATCS(problem, 2, 2);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.5*slack[j]/pAve)*Math.exp(-2.0/sAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-4);
		}
		// d=20, k=4, k2=3
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new DynamicATCS(problem, 4, 3);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			double correction = Math.exp(-0.25*slack[j]/pAve)*Math.exp(-4.0/3.0/sAve);
			double expected = expected0[j] * correction;
			assertEquals("positiveSlack, j:"+j, expected < e ? e : expected, h.h(partial, j, inc), 1E-4);
		}
	}
	
	@Test
	public void testDynamicATCSdefault() {
		double e = DynamicATCS.MIN_H;
		int highP = (int)Math.ceil(1 / e)*2;
		int[] w =    { 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 1};
		int[] p =    { 1, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, highP};
		double[] expected0 = { 999, 1, 0.5, 0.25, 0.125, e, e, e, e, 2, 1, 0.5, 0.25, e};
		double[] slack = new double[p.length];
		double pAve = 0;
		for (int i = 1; i < p.length; i++) {
			slack[i] = 20-p[i];
			if (slack[i] < 0) slack[i] = 0;
			pAve += p[i];
		}
		pAve /= p.length - 1;
		PartialPermutation partial = new PartialPermutation(expected0.length);
		// All late tests, 
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 4);
		DynamicATCS h = new DynamicATCS(problem);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		double sAve = ((DynamicATCS.IncrementalStatsCalculator)inc).averageSetupTime();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("negativeSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
		// d=20, 
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new DynamicATCS(problem);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("positiveSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
		// d=20,
		problem = new FakeProblemWeightsPTime(w, p, 20, 4);
		h = new DynamicATCS(problem);
		inc = h.createIncrementalEvaluation();
		for (int j = 1; j < expected0.length; j++) {
			assertTrue("positiveSlack, j:"+j, expected0[j] >= h.h(partial, j, inc));
		}
	}
	
	@Test
	public void testSchedulingHeuristicIncEvalExtend() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 3, 5, 6, 10, 15 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		assertEquals(0, inc.currentTime());
		for (int i = 0; i < p.length; i++) {
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
			assertEquals(e[i], inc.currentTime());
		}
	}
	
	@Test
	public void testSchedulingHeuristicIncEvalExtendWithSetups() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 10, 13, 18, 29, 44 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 0, 7);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		assertEquals(0, inc.currentTime());
		for (int i = 0; i < p.length; i++) {
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
			assertEquals(e[i], inc.currentTime());
		}
	}
	
	@Test
	public void testSchedulingHeuristicSlack() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 7, 5, 4, 0, -5 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 10);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		for (int i = 0; i < p.length; i++) {
			assertEquals(e[i], inc.slack(i, partial));
			assertEquals(e[i], inc.slack(i));
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
		}
	}
	
	@Test
	public void testSchedulingHeuristicSlackWithSetups() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 19, 16, 11, 0, -15 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 29, 7);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		for (int i = 0; i < p.length; i++) {
			assertEquals(e[i], inc.slack(i, partial));
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
		}
	}
	
	@Test
	public void testSchedulingHeuristicSlackPlus() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 7, 5, 4, 0, 0 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 10);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		for (int i = 0; i < p.length; i++) {
			assertEquals(e[i], inc.slackPlus(i, partial));
			assertEquals(e[i], inc.slackPlus(i));
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
		}
	}
	
	@Test
	public void testSchedulingHeuristicSlackPlusWithSetups() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] e = { 19, 16, 11, 0, 0 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 29, 7);
		WeightedShortestProcessingTimeLateOnly h = new WeightedShortestProcessingTimeLateOnly(problem);
		PartialPermutation partial = new PartialPermutation(e.length);
		SchedulingHeuristic.IncrementalTimeCalculator inc = (SchedulingHeuristic.IncrementalTimeCalculator)h.createIncrementalEvaluation();
		for (int i = 0; i < p.length; i++) {
			assertEquals(e[i], inc.slackPlus(i, partial));
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
		}
	}
	
	@Test
	public void testSchedulingHeuristicTotalAveragePTime() {
		int[] w = { 1, 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4, 5 };
		int[] expectedTotal = { 15, 12, 10, 9, 5 };
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, 10);
		Montagne h = new Montagne(problem);
		PartialPermutation partial = new PartialPermutation(expectedTotal.length);
		SchedulingHeuristic.IncrementalAverageProcessingCalculator inc = (SchedulingHeuristic.IncrementalAverageProcessingCalculator)h.createIncrementalEvaluation();
		for (int i = 0; i < p.length; i++) {
			assertEquals(expectedTotal[i], inc.totalProcessingTime());
			assertEquals(expectedTotal[i]/(p.length-i-0.0), inc.averageProcessingTime(), 1E-10);
			inc.extend(partial, i);
			if (i < partial.numExtensions()) partial.extend(i);
			else partial.extend(partial.numExtensions()-1);
		}
		assertEquals(0, inc.totalProcessingTime());
	}
	
	@Test
	public void testAverageSetupCalculation() {
		int[] w = { 1, 1, 1, 1 };
		int[] p = { 3, 2, 1, 4 };
		int d = 5;
		int[][] s = {
			{1, 2, 3, 4}, 
			{3, 4, 1, 2}, 
			{6, 6, 6, 6}, 
			{8, 1, 2, 3}  
		};
		FakeProblemWeightsPTime problem = new FakeProblemWeightsPTime(w, p, d, s);
		DynamicATCS h = new DynamicATCS(problem, 2, 1);
		IncrementalEvaluation inc = h.createIncrementalEvaluation();
		DynamicATCS.IncrementalStatsCalculator incATCS = (DynamicATCS.IncrementalStatsCalculator)inc;
		assertEquals(58.0/16, incATCS.averageSetupTime(), 1E-10);
		PartialPermutation partial = new PartialPermutation(4);
		inc.extend(partial, 3);
		partial.extend(3);
		assertEquals(32.0/9.0, incATCS.averageSetupTime(), 1E-10);
		inc.extend(partial, 2);
		partial.extend(2);
		assertEquals(17.0/4.0, incATCS.averageSetupTime(), 1E-10);
		inc.extend(partial, 1);
		partial.extend(1);
		assertEquals(3.0, incATCS.averageSetupTime(), 1E-10);
		inc.extend(partial, 0);
		partial.extend(0);
		assertEquals(0.0, incATCS.averageSetupTime(), 1E-10);
	}
	
	private static class FakeProblemDuedates implements SingleMachineSchedulingProblem {
		
		private FakeProblemData data;
		
		public FakeProblemDuedates(int[] d) {
			data = new FakeProblemData(d);
		}
		
		public FakeProblemDuedates(int[] d, int[] p) {
			data = new FakeProblemData(d, p, true);
		}
		
		public FakeProblemDuedates(int[] d, int[] p, int s) {
			data = new FakeProblemData(d, p, true);
			data.s = s;
		}
		
		@Override
		public SingleMachineSchedulingProblemData getInstanceData() {
			return data;
		}
		
		@Override public int cost(Permutation p) { return 10; }
		@Override public int value(Permutation p) { return 10; }
	}
	
	private static class FakeProblemWeightsPTime implements SingleMachineSchedulingProblem {
		
		private FakeProblemData data;
		
		public FakeProblemWeightsPTime(int[] w, int[] p) {
			data = new FakeProblemData(w, p);
		}
		
		public FakeProblemWeightsPTime(int[] w, int[] p, int d) {
			data = new FakeProblemData(w, p, d);
		}
		
		public FakeProblemWeightsPTime(int[] w, int[] p, int d, int s) {
			data = new FakeProblemData(w, p, d, s);
		}
		
		public FakeProblemWeightsPTime(int[] w, int[] p, int d, int[][] s) {
			data = new FakeProblemDataSetups(w, p, d, s);
		}
		
		@Override
		public SingleMachineSchedulingProblemData getInstanceData() {
			return data;
		}
		
		@Override public int cost(Permutation p) { return 10; }
		@Override public int value(Permutation p) { return 10; }
	}
	
	private static class FakeProblemDataSetups extends FakeProblemData {
		private int[][] s;
		public FakeProblemDataSetups(int[] w, int[] p, int d, int[][] s) {
			super(w, p, d);
			this.s = new int[s.length][];
			for (int i = 0; i < s.length; i++) {
				this.s[i] = s[i].clone();
			}
		}
		@Override public int getSetupTime(int j) { return s[j][j]; }
		@Override public int getSetupTime(int i, int j) { return s[i][j]; }
		@Override public boolean hasSetupTimes() { return true; }
	}
	
	private static class FakeProblemData implements SingleMachineSchedulingProblemData {

		private int[] d;
		private int[] w;
		private int[] p;
		private int s;
		private int n;
		
		public FakeProblemData(int[] d) {
			this.d = d.clone();
			s = -1;
			n = d.length;
		}
		
		public FakeProblemData(int[] d, int[] p, boolean duedates) {
			if (duedates) {
				this.d = d.clone();
				this.p = p.clone();
			} else {
				throw new IllegalArgumentException();
			}
			s = -1;
			n = d.length;
		}
		
		public FakeProblemData(int[] w, int[] p) {
			this.w = w.clone();
			this.p = p.clone();
			s = -1;
			n = p.length;
		}
		
		public FakeProblemData(int[] w, int[] p, int d) {
			this.w = w.clone();
			this.p = p.clone();
			this.d = new int[w.length];
			for (int i = 0; i < this.d.length; i++) this.d[i] = d;
			s = -1;
			n = p.length;
		}
		
		public FakeProblemData(int[] w, int[] p, int d, int s) {
			this(w, p, d);
			this.s = s;
			n = p.length;
		}
		
		@Override public int numberOfJobs() { return n; }
		@Override public int getProcessingTime(int j) { return p==null? 0: p[j]; }
		@Override public int[] getCompletionTimes(Permutation schedule) { return null; }
		@Override public boolean hasDueDates() { return d != null; }
		@Override public int getDueDate(int j) { return d[j]; }
		@Override public boolean hasWeights() { return w != null; }
		@Override public int getWeight(int j) { return w[j]; }
		@Override public int getSetupTime(int j) { return s; }
		@Override public int getSetupTime(int i, int j) { return 2*i + j; }
		@Override public boolean hasSetupTimes() { return s > 0; }
	}	
}