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


import org.cicirello.search.operators.MutationOperator;
import org.cicirello.permutations.Permutation;
import org.cicirello.math.rand.RandomIndexer;

/**
 * <p>This class implements a scramble mutation on permutations, where one mutation
 * consists in randomizing the order of a randomly selected subpermutation.
 * The pair of indexes that indicate the subpermutation to scramble is chosen
 * uniformly at random from among all n(n-1)/2 possible pairs of indexes, where n
 * is the length of the permutation.</p>
 * <p>The runtime (worst case and average case) of 
 * the {@link #mutate(Permutation) mutate} method is O(n),
 * where n is the length of the permutation.  
 * The worst case runtime occurs when the random indexes are
 * the two end points.  
 * On average, a scramble mutation moves approximately n/3 elements.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class ScrambleMutation implements MutationOperator<Permutation> {

	private final int[] indexes;
	
	/**
	 * Constructs a ScrambleMutation mutation operator.
	 */
	public ScrambleMutation() { 
		indexes = new int[2]; 
	}
	
	@Override
	public final void mutate(Permutation c) {
		if (c.length() >= 2) {
			generateIndexes(c.length(), indexes);
			c.scramble(indexes[0], indexes[1]);
		}
	}
	
	@Override
	public ScrambleMutation split() {
		return new ScrambleMutation();
	}
	
	/*
	 * This package access method allows the window limited version
	 * implemented as a subclass to change how indexes are generated
	 * without modifying the mutate method.
	 */
	void generateIndexes(int n, int[] indexes) {
		RandomIndexer.nextIntPair(n, indexes);
	}
	
}