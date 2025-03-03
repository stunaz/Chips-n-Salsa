/*
 * Chips-n-Salsa: A library of parallel self-adaptive local search algorithms.
 * Copyright (C) 2002-2021  Vincent A. Cicirello
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
 
package org.cicirello.search.operators;

import org.cicirello.search.concurrent.Splittable;

/**
 * <p>Implement the CrossoverOperator interface to implement a crossover operator
 * for use in evolutionary algorithms.</p>
 *
 * @param <T> The type of object used to represent candidate solutions to the problem.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a> 
 */
public interface CrossoverOperator<T> extends Splittable<CrossoverOperator<T>> {
	
	/**
	 * Performs a crossover for an evolutionary algorithm, such that crossover
	 * forms two children from two parents. Implementations of this method 
	 * modify the parameters, transforming the parents into the children.
	 *
	 * @param c1 A candidate solution subject to the crossover. This method
	 * changes the state of c1.
	 * @param c2 A candidate solution subject to the crossover. This method
	 * changes the state of c2.
	 */
	void cross(T c1, T c2);
}
