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
 
package org.cicirello.search.representations;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test cases for the classes that represent inputs to
 * functions (univariate vs multivariate, integer inputs vs floating-point inputs).
 */
public class NumericRepresentationsTests {
	
	private final double EPSILON = 1e-10;
	
	@Test
	public void testDefaultSetIntArray() {
		class Vector implements IntegerValued {
			
			private int[] a;
			
			Vector(int[] a) { this.a = a.clone(); }
			
			@Override public int length() { return a.length; }
			
			@Override public int get(int i) { return a[i]; }
			
			@Override public int[] toArray(int[] values) {
				//not correct in general (shouldn't expose internals, but this is for testing
				return a;
			}
			
			@Override public void set(int i, int value) { a[i] = value; }
		}
		Vector v = new Vector(new int[] {4, 5, 6});
		v.set(new int[] {7, 8, 9});
		assertArrayEquals(new int[] {7, 8, 9}, v.toArray(null));
	}
	
	@Test
	public void testDefaultSetDoubleArray() {
		class Vector implements RealValued {
			
			private double[] a;
			
			Vector(double[] a) { this.a = a.clone(); }
			
			@Override public int length() { return a.length; }
			
			@Override public double get(int i) { return a[i]; }
			
			@Override public double[] toArray(double[] values) {
				//not correct in general (shouldn't expose internals, but this is for testing
				return a;
			}
			
			@Override public void set(int i, double value) { a[i] = value; }
		}
		Vector v = new Vector(new double[] {4, 5, 6});
		v.set(new double[] {7, 8, 9});
		assertArrayEquals(new double[] {7, 8, 9}, v.toArray(null));
	}
	
	@Test
	public void testExchangeIntegerVector() {
		for (int n = 1; n <= 8; n*=2) {
			for (int first = 0; first < n; first++) {
				for (int last = first; last < n; last++) {
					int[] raw1 = new int[n];
					int[] raw2 = new int[n];
					for (int i = 0; i < n; i++) {
						raw1[i] = 100 + i;
						raw2[i] = 200 + i;
					}
					IntegerVector v1 = new IntegerVector(raw1);
					IntegerVector v2 = new IntegerVector(raw2);
					IntegerVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i));
						assertEquals(raw1[i], v2.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					
					// try first > last case
					if (first != last) {
						v1 = new IntegerVector(raw1);
						v2 = new IntegerVector(raw2);
						IntegerVector.exchange(v1, v2, last, first);
						for (int i = 0; i < first; i++) {
							assertEquals(raw1[i], v1.get(i));
							assertEquals(raw2[i], v2.get(i));
						}
						for (int i = first; i <= last; i++) {
							assertEquals(raw2[i], v1.get(i));
							assertEquals(raw1[i], v2.get(i));
						}
						for (int i = last+1; i < n; i++) {
							assertEquals(raw1[i], v1.get(i));
							assertEquals(raw2[i], v2.get(i));
						}
					}
				}
			}
		}
	}
	
	@Test
	public void testExchangeBoundedIntegerVector() {
		for (int n = 1; n <= 8; n*=2) {
			for (int first = 0; first < n; first++) {
				for (int last = first; last < n; last++) {
					int[] raw1 = new int[n];
					int[] raw2 = new int[n];
					for (int i = 0; i < n; i++) {
						raw1[i] = 100 + i;
						raw2[i] = 200 + i;
					}
					BoundedIntegerVector v1 = new BoundedIntegerVector(raw1, 0, 300);
					BoundedIntegerVector v2 = new BoundedIntegerVector(raw2, 0, 300);
					IntegerVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i));
						assertEquals(raw1[i], v2.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					
					// First not in bounds of second
					v1 = new BoundedIntegerVector(raw1, 100, 199);
					v2 = new BoundedIntegerVector(raw2, 0, 300);
					IntegerVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(199, v1.get(i));
						assertEquals(raw1[i], v2.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					
					// Second not in bounds of first
					v1 = new BoundedIntegerVector(raw1, 0, 300);
					v2 = new BoundedIntegerVector(raw2, 200, 300);
					IntegerVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i));
						assertEquals(200, v2.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					
					// second is not bounded
					v1 = new BoundedIntegerVector(raw1, 100, 199);
					IntegerVector v3 = new IntegerVector(raw2);
					IntegerVector.exchange(v1, v3, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v3.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(199, v1.get(i));
						assertEquals(raw1[i], v3.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i));
						assertEquals(raw2[i], v3.get(i));
					}
					
					// First is not bounded
					v3 = new IntegerVector(raw1);
					v2 = new BoundedIntegerVector(raw2, 200, 300);
					IntegerVector.exchange(v3, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v3.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v3.get(i));
						assertEquals(200, v2.get(i));
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v3.get(i));
						assertEquals(raw2[i], v2.get(i));
					}
				}
			}
		}
	}
	
	@Test
	public void testExchangeRealVector() {
		for (int n = 1; n <= 8; n*=2) {
			for (int first = 0; first < n; first++) {
				for (int last = first; last < n; last++) {
					double[] raw1 = new double[n];
					double[] raw2 = new double[n];
					for (int i = 0; i < n; i++) {
						raw1[i] = 100 + i;
						raw2[i] = 200 + i;
					}
					RealVector v1 = new RealVector(raw1);
					RealVector v2 = new RealVector(raw2);
					RealVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i), 0.0);
						assertEquals(raw1[i], v2.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					
					// try first > last case
					if (first != last) {
						v1 = new RealVector(raw1);
						v2 = new RealVector(raw2);
						RealVector.exchange(v1, v2, last, first);
						for (int i = 0; i < first; i++) {
							assertEquals(raw1[i], v1.get(i), 0.0);
							assertEquals(raw2[i], v2.get(i), 0.0);
						}
						for (int i = first; i <= last; i++) {
							assertEquals(raw2[i], v1.get(i), 0.0);
							assertEquals(raw1[i], v2.get(i), 0.0);
						}
						for (int i = last+1; i < n; i++) {
							assertEquals(raw1[i], v1.get(i), 0.0);
							assertEquals(raw2[i], v2.get(i), 0.0);
						}
					}
				}
			}
		}
	}
	
	@Test
	public void testExchangeBoundedRealVector() {
		for (int n = 1; n <= 8; n*=2) {
			for (int first = 0; first < n; first++) {
				for (int last = first; last < n; last++) {
					double[] raw1 = new double[n];
					double[] raw2 = new double[n];
					for (int i = 0; i < n; i++) {
						raw1[i] = 100 + i;
						raw2[i] = 200 + i;
					}
					BoundedRealVector v1 = new BoundedRealVector(raw1, 0, 300);
					BoundedRealVector v2 = new BoundedRealVector(raw2, 0, 300);
					RealVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i), 0.0);
						assertEquals(raw1[i], v2.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					
					// First not in bounds of second
					v1 = new BoundedRealVector(raw1, 100, 199);
					v2 = new BoundedRealVector(raw2, 0, 300);
					RealVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(199, v1.get(i), 0.0);
						assertEquals(raw1[i], v2.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					
					// Second not in bounds of first
					v1 = new BoundedRealVector(raw1, 0, 300);
					v2 = new BoundedRealVector(raw2, 200, 300);
					RealVector.exchange(v1, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v1.get(i), 0.0);
						assertEquals(200, v2.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					
					// second is not bounded
					v1 = new BoundedRealVector(raw1, 100, 199);
					RealVector v3 = new RealVector(raw2);
					RealVector.exchange(v1, v3, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v3.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(199, v1.get(i), 0.0);
						assertEquals(raw1[i], v3.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v1.get(i), 0.0);
						assertEquals(raw2[i], v3.get(i), 0.0);
					}
					
					// First is not bounded
					v3 = new RealVector(raw1);
					v2 = new BoundedRealVector(raw2, 200, 300);
					RealVector.exchange(v3, v2, first, last);
					for (int i = 0; i < first; i++) {
						assertEquals(raw1[i], v3.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
					for (int i = first; i <= last; i++) {
						assertEquals(raw2[i], v3.get(i), 0.0);
						assertEquals(200, v2.get(i), 0.0);
					}
					for (int i = last+1; i < n; i++) {
						assertEquals(raw1[i], v3.get(i), 0.0);
						assertEquals(raw2[i], v2.get(i), 0.0);
					}
				}
			}
		}
	}
	
	@Test
	public void testUnivariate() {
		SingleReal f0 = new SingleReal();
		assertEquals(0, f0.get(), EPSILON);
		SingleReal f5 = new SingleReal(5.0);
		assertEquals(5.0, f5.get(), EPSILON);
		assertEquals(0, f0.get(0), EPSILON);
		assertEquals(5.0, f5.get(0), EPSILON);
		double[] array1 = f0.toArray(null);
		assertEquals(0, array1[0], EPSILON);
		double[] array2 = f5.toArray(null);
		assertEquals(5, array2[0], EPSILON);
		double[] array3 = f5.toArray(array1);
		assertTrue(array1 == array3);
		assertEquals(5, array3[0], EPSILON);
		
		double[] wrongLength = new double[2];
		double[] array4 = f5.toArray(wrongLength);
		assertTrue(array4 != wrongLength);
		assertEquals(1, array4.length);
		assertEquals(5.0, array4[0], EPSILON);
		
		SingleReal copy = new SingleReal(f5);
		assertEquals(5.0, copy.get(), EPSILON);
		SingleReal copy2 = f5.copy();
		assertEquals(5.0, copy2.get(), EPSILON);
		assertTrue(copy2 != f5);
		assertEquals(f5.getClass(), copy2.getClass());
		f0.set(10);
		assertEquals(10.0, f0.get(), EPSILON);
		f0.set(0, 8);
		assertEquals(8.0, f0.get(0), EPSILON);
		f0.set(new double[] {42});
		assertEquals(42.0, f0.get());
		
		assertEquals(f5, copy);
		assertEquals(f5, copy2);
		assertEquals(f5.hashCode(), copy.hashCode());
		assertEquals(f5.hashCode(), copy2.hashCode());
		
		assertEquals(1, f0.length());
		assertEquals(1, f5.length());
		
		assertFalse(f5.equals(null));
		assertFalse(f5.equals("hello"));
	}
	
	@Test
	public void testIntegerUnivariate() {
		SingleInteger f0 = new SingleInteger();
		assertEquals(0, f0.get());
		SingleInteger f5 = new SingleInteger(5);
		assertEquals(5, f5.get());
		assertEquals(0, f0.get(0));
		assertEquals(5, f5.get(0));
		int[] array1 = f0.toArray(null);
		assertEquals(0, array1[0]);
		int[] array2 = f5.toArray(null);
		assertEquals(5, array2[0]);
		int[] array3 = f5.toArray(array1);
		assertTrue(array1 == array3);
		assertEquals(5, array3[0]);
		
		int[] wrongLength = new int[2];
		int[] array4 = f5.toArray(wrongLength);
		assertTrue(array4 != wrongLength);
		assertEquals(1, array4.length);
		assertEquals(5, array4[0]);

		SingleInteger copy = new SingleInteger(f5);
		assertEquals(5, copy.get());
		SingleInteger copy2 = f5.copy();
		assertEquals(5, copy2.get());
		assertTrue(copy2 != f5);
		assertEquals(f5.getClass(), copy2.getClass());
		f0.set(10);
		assertEquals(10, f0.get());
		f0.set(0, 8);
		assertEquals(8, f0.get(0));
		f0.set(new int[] {42});
		assertEquals(42, f0.get());
		
		assertEquals(f5, copy);
		assertEquals(f5, copy2);
		assertEquals(f5.hashCode(), copy.hashCode());
		assertEquals(f5.hashCode(), copy2.hashCode());
		
		assertEquals(1, f0.length());
		assertEquals(1, f5.length());
		
		assertFalse(f5.equals(null));
		assertFalse(f5.equals("hello"));
	}
	
	@Test
	public void testMultivariate() {
		for (int n = 0; n <= 10; n++) {
			RealVector f = new RealVector(n);
			assertEquals(n, f.length());
			double[] array = f.toArray(null);
			assertEquals(n, array.length);
			for (int i = 0; i < n; i++) {
				assertEquals(0.0, f.get(i), EPSILON);
				assertEquals(0.0, array[i], EPSILON);
			}
			double[] initial = new double[n];
			for (int i = 0; i < n; i++) {
				initial[i] = n - i;
			}
			RealVector f2 = new RealVector(initial);
			array = f2.toArray(null);
			double[] array2 = new double[n];
			double[] array3 = f2.toArray(array2);
			assertTrue(array2 == array3);
			RealVector f3 = new RealVector(initial);
			assertEquals(n, f2.length());
			assertEquals(n, array.length);
			assertEquals(f2, f3);
			assertEquals(f2.hashCode(), f3.hashCode());
			if (n > 1) assertNotEquals(f2, f);
			for (int i = 0; i < n; i++) {
				f.set(i, (double)(n-i));
				assertEquals((double)(n-i), f2.get(i), EPSILON);
				assertEquals((double)(n-i), array[i], EPSILON);
				assertEquals((double)(n-i), array3[i], EPSILON);
				assertEquals((double)(n-i), f.get(i), EPSILON);
				f3.set(i, 100.0);
				assertNotEquals(f2, f3);
			}
			double[] changed = new double[initial.length];
			for (int i = 0; i < changed.length; i++) {
				changed[i] = (i+1)*5;
			}
			f3.set(changed.clone());
			assertArrayEquals(changed, f3.toArray(null));
			assertEquals(f2, f);
			assertEquals(f2.hashCode(), f.hashCode());
			RealVector copy = new RealVector(f2);
			RealVector copy2 = f2.copy();
			assertEquals(f2.getClass(), copy2.getClass());
			assertEquals(f2, copy);
			assertEquals(f2, copy2);
			assertTrue(f2 != copy2);
			assertEquals(f2.hashCode(), copy.hashCode());
			assertEquals(f2.hashCode(), copy2.hashCode());
			
			assertFalse(f2.equals(null));
			assertFalse(f2.equals("hello"));
		}
	}
	
	@Test
	public void testIntegerMultivariate() {
		for (int n = 0; n <= 10; n++) {
			IntegerVector f = new IntegerVector(n);
			assertEquals(n, f.length());
			int[] array = f.toArray(null);
			assertEquals(n, array.length);
			for (int i = 0; i < n; i++) {
				assertEquals(0, f.get(i));
				assertEquals(0, array[i]);
			}
			int[] initial = new int[n];
			for (int i = 0; i < n; i++) {
				initial[i] = n - i;
			}
			IntegerVector f2 = new IntegerVector(initial);
			array = f2.toArray(null);
			int[] array2 = new int[n];
			int[] array3 = f2.toArray(array2);
			assertTrue(array2 == array3);
			IntegerVector f3 = new IntegerVector(initial);
			assertEquals(n, f2.length());
			assertEquals(n, array.length);
			assertEquals(f2, f3);
			assertEquals(f2.hashCode(), f3.hashCode());
			if (n > 1) assertNotEquals(f2, f);
			for (int i = 0; i < n; i++) {
				f.set(i, n-i);
				assertEquals(n-i, f2.get(i));
				assertEquals((n-i), array[i]);
				assertEquals((n-i), array3[i]);
				assertEquals(n-i, f.get(i));
				f3.set(i, 100);
				assertNotEquals(f2, f3);
			}
			int[] changed = new int[initial.length];
			for (int i = 0; i < changed.length; i++) {
				changed[i] = (i+1)*5;
			}
			f3.set(changed.clone());
			assertArrayEquals(changed, f3.toArray(null));
			assertEquals(f2, f);
			assertEquals(f2.hashCode(), f.hashCode());
			IntegerVector copy = new IntegerVector(f2);
			IntegerVector copy2 = f2.copy();
			assertEquals(f2.getClass(), copy2.getClass());
			assertEquals(f2, copy);
			assertEquals(f2, copy2);
			assertTrue(f2 != copy2);
			assertEquals(f2.hashCode(), copy.hashCode());
			assertEquals(f2.hashCode(), copy2.hashCode());
			
			assertFalse(f2.equals(null));
			assertFalse(f2.equals("hello"));
		}
	}
	
	@Test
	public void testBoundedIntegerVector() {
		for (int n = 0; n <= 10; n++) {
			int[] initial = new int[n];
			for (int i = 0; i < n; i++) {
				initial[i] = n - i;
			}
			BoundedIntegerVector f2 = new BoundedIntegerVector(initial, 1, (n >= 1? n : 1)+1);
			int[] array = f2.toArray(null);
			int[] array2 = new int[n];
			int[] array3 = f2.toArray(array2);
			assertTrue(array2 == array3);
			BoundedIntegerVector f3 = new BoundedIntegerVector(initial, 1, (n >= 1? n : 1)+1);
			assertEquals(n, f2.length());
			assertEquals(n, array.length);
			assertEquals(f2, f3);
			assertEquals(f2.hashCode(), f3.hashCode());
			for (int i = 0; i < n; i++) {
				assertEquals(n-i, f2.get(i));
				assertEquals((n-i), array[i]);
				assertEquals((n-i), array3[i]);
				f3.set(i, n+1);
				assertEquals(n+1, f3.get(i));
				assertNotEquals(f2, f3);
			}
			BoundedIntegerVector copy = new BoundedIntegerVector(f2);
			BoundedIntegerVector copy2 = f2.copy();
			assertEquals(f2.getClass(), copy2.getClass());
			assertEquals(f2, copy);
			assertEquals(f2, copy2);
			assertTrue(f2 != copy2);
			assertEquals(f2.hashCode(), copy.hashCode());
			assertEquals(f2.hashCode(), copy2.hashCode());
		}
		for (int n = 1; n <= 10; n++) {
			int[] initial = new int[n];
			for (int i = 0; i < n; i++) {
				initial[i] = i;
			}
			BoundedIntegerVector f = new BoundedIntegerVector(initial, 2, 5);
			for (int i = 0; i < n; i++) {
				if (i < 2) assertEquals(2, f.get(i));
				else if (i < 5) assertEquals(i, f.get(i));
				else assertEquals(5, f.get(i));
			}
			for (int i = 0; i < n; i++) {
				for (int j = 2; j <= 5; j++) {
					f.set(i, j);
					assertEquals(j, f.get(i));
				}
				f.set(i, 1);
				assertEquals(2, f.get(i));
				f.set(i, 6);
				assertEquals(5, f.get(i));
			}
		}
		int[] values = { 3, 4, 3, 4 };
		BoundedIntegerVector f1 = new BoundedIntegerVector(values, 1, 10);
		BoundedIntegerVector f2 = new BoundedIntegerVector(values, 2, 10);
		BoundedIntegerVector f3 = new BoundedIntegerVector(values, 1, 9);
		assertNotEquals(f1, f2);
		assertNotEquals(f1, f3);
		assertFalse(f1.sameBounds(f2));
		assertFalse(f1.sameBounds(f3));
		assertTrue(f1.sameBounds(new BoundedIntegerVector(values, 1, 10)));
		assertFalse(f1.equals(null));
		IntegerVector nonBounded = new IntegerVector(values);
		assertFalse(f1.equals(nonBounded));
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new BoundedIntegerVector(values, 2, 1)
		);
		
		int[] changed = {0, 4, 8, 12};
		f3.set(changed);
		int[] expected = {1, 4, 8, 9};
		assertArrayEquals(expected, f3.toArray(null));
	}
	
	@Test
	public void testBoundedRealVector() {
		for (int n = 0; n <= 10; n++) {
			double[] initial = new double[n];
			for (int i = 0; i < n; i++) {
				initial[i] = n - i;
			}
			BoundedRealVector f2 = new BoundedRealVector(initial, 1, (n >= 1? n : 1)+1);
			double[] array = f2.toArray(null);
			double[] array2 = new double[n];
			double[] array3 = f2.toArray(array2);
			assertTrue(array2 == array3);
			BoundedRealVector f3 = new BoundedRealVector(initial, 1, (n >= 1? n : 1)+1);
			assertEquals(n, f2.length());
			assertEquals(n, array.length);
			assertEquals(f2, f3);
			assertEquals(f2.hashCode(), f3.hashCode());
			for (int i = 0; i < n; i++) {
				assertEquals(n-i, f2.get(i), 0.0);
				assertEquals((n-i), array[i], 0.0);
				assertEquals((n-i), array3[i], 0.0);
				f3.set(i, n+1);
				assertEquals(n+1, f3.get(i), 0.0);
				assertNotEquals(f2, f3);
			}
			BoundedRealVector copy = new BoundedRealVector(f2);
			BoundedRealVector copy2 = f2.copy();
			assertEquals(f2.getClass(), copy2.getClass());
			assertEquals(f2, copy);
			assertEquals(f2, copy2);
			assertTrue(f2 != copy2);
			assertEquals(f2.hashCode(), copy.hashCode());
			assertEquals(f2.hashCode(), copy2.hashCode());
		}
		for (int n = 1; n <= 10; n++) {
			double[] initial = new double[n];
			for (int i = 0; i < n; i++) {
				initial[i] = i;
			}
			BoundedRealVector f = new BoundedRealVector(initial, 2, 5);
			for (int i = 0; i < n; i++) {
				if (i < 2) assertEquals(2, f.get(i), 0.0);
				else if (i < 5) assertEquals(i, f.get(i), 0.0);
				else assertEquals(5, f.get(i), 0.0);
			}
			for (int i = 0; i < n; i++) {
				for (int j = 2; j <= 5; j++) {
					f.set(i, j);
					assertEquals(j, f.get(i), 0.0);
				}
				f.set(i, 1);
				assertEquals(2, f.get(i), 0.0);
				f.set(i, 6);
				assertEquals(5, f.get(i), 0.0);
			}
		}
		final double[] values = { 3, 4, 3, 4 };
		BoundedRealVector f1 = new BoundedRealVector(values, 1, 10);
		BoundedRealVector f2 = new BoundedRealVector(values, 2, 10);
		BoundedRealVector f3 = new BoundedRealVector(values, 1, 9);
		assertFalse(f1.sameBounds(f2));
		assertFalse(f1.sameBounds(f3));
		assertTrue(f1.sameBounds(new BoundedRealVector(values, 1, 10)));
		assertNotEquals(f1, f2);
		assertNotEquals(f1, f3);
		assertFalse(f1.equals(null));
		RealVector nonBounded = new RealVector(values);
		assertFalse(f1.equals(nonBounded));
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new BoundedRealVector(values, 1.0001, 1)
		);
		
		double[] changed = {0, 4, 8, 12};
		f3.set(changed);
		double[] expected = {1, 4, 8, 9};
		assertArrayEquals(expected, f3.toArray(null));
	}	
}
