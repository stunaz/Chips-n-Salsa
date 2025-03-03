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
 
package org.cicirello.search.operators.permutations;

import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.search.operators.IterableMutationOperator;
import org.cicirello.search.operators.MutationIterator;
import org.cicirello.permutations.Permutation;
import org.cicirello.math.rand.RandomSampler;

/**
 * <p>This class implements a block interchange mutation on permutations, where one mutation
 * consists in swapping two randomly chosen non-overlapping "blocks" (i.e., subsequences).  
 * The block interchange is chosen uniformly at random
 * from among all possible block interchanges.  The two random blocks
 * are not required to be of the same length, but are required to be non-overlapping
 * (i.e., cannot share elements).</p>
 *
 * <p>As an example, consider the permutation: p1 = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9].
 * If a block interchange swaps the blocks [1, 2] and [5, 6, 7, 8], the result
 * is: [0, 5, 6, 7, 8, 3, 4, 1, 2, 9].  This mutation operator is related to the 
 * {@link BlockMoveMutation}, which swaps a pair of randomly
 * selected adjacent blocks.</p>
 *
 * <p>The runtime (worst case and average case) of both
 * the {@link #mutate(Permutation) mutate} and {@link #undo(Permutation) undo} methods is O(n),
 * where n is the length of the permutation.  There are a variety of ways of 
 * demonstrating the worst case behavior, one of which is if the exchanged blocks are at 
 * opposite ends of the permutation and are of differing lengths, which would result in
 * movement of every permutation element.  On average, approximately 0.6 n elements
 * are moved by this mutation operator.</p>  
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class BlockInterchangeMutation implements UndoableMutationOperator<Permutation>, IterableMutationOperator<Permutation> {
	
	// needed to implement undo
	private final int[] indexes;
	
	/**
	 * Constructs a BlockInterchangeMutation mutation operator.
	 */
	public BlockInterchangeMutation() { 
		indexes = new int[4]; 
	}
	
	@Override
	public final void mutate(Permutation c) {
		if (c.length() >= 2) {
			generateIndexes(c.length(), indexes);
			c.swapBlocks(indexes[0], indexes[1], indexes[2], indexes[3]);
		}
	}
	
	@Override
	public final void undo(Permutation c) {
		if (c.length() >= 2) {
			c.swapBlocks(indexes[0], indexes[0]+indexes[3]-indexes[2], indexes[3]-indexes[1]+indexes[0], indexes[3]);
		}
	}
	
	@Override
	public BlockInterchangeMutation split() {
		return new BlockInterchangeMutation();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>The worst case runtime of the {@link MutationIterator#hasNext} and the 
	 * {@link MutationIterator#setSavepoint} methods of the {@link MutationIterator} 
	 * created by this method is O(1).  
	 * The worst case runtime of the {@link MutationIterator#nextMutant} and
	 * {@link MutationIterator#rollback} methods 
	 * is O(n), where n is the length of the Permutation.</p>
	 */
	@Override
	public MutationIterator iterator(Permutation p) {
		return new BlockInterchangeIterator(p);
	}
	
	/*
	 * This package access method allows the window limited version
	 * implemented as a subclass to change how indexes are generated
	 * without modifying the mutate method.
	 */
	void generateIndexes(int n, int[] indexes) {
		// The RandomSampler.sampleInsertion method puts result in sorted order
		// to begin with. 
		RandomSampler.sampleInsertion(n+2, 4, indexes);
		// All index values generated by above are unique.
		// However, block size 1 should be allowed, which would require
		// duplicated indexes.  We handle this by passing n+2 (above).  An index 
		// equal to n is treated as a duplicate of indexes[0] (i.e., a block of size 1).
		// An index equal to n+1 is likewise treated as a duplicate to define the right
		// block to be of size 1.
		if (indexes[3]==n) {
			indexes[3] = indexes[2];
			indexes[2] = indexes[1];
			indexes[1] = indexes[0];
		} else if (indexes[2]==n) {
			indexes[3] = indexes[2] = indexes[1];
			indexes[1] = indexes[0];
		} else if (indexes[3]==n+1) {
			indexes[3] = indexes[2];
		}
	}
	
}
