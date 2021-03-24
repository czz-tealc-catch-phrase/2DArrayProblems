package com.chirs.co.app;

import javafx.util.Pair;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

public class SolutionTest {
    /*Transpose*/
    @Test
    public void test_transpose() {
        List<int[][]> input = new ArrayList<int[][]>();
        input.add(new int[][]{
                {0,1,2},
                {3,4,5},
                {6,7,8}});

        List<int[][]> expected = new ArrayList<int[][]>();
        expected.add(new int[][]{
                {0,3,6},
                {1,4,7},
                {2,5,8}});

        Function<int[][], Object> f = (x) -> this.transposeAMatrix(x);

        BiFunction<int[][], int[][], Object> validate = (out, exp) -> {
            for(int j = 0; j < out.length; j++) {
                assertEquals(out.length, exp.length);
                assertArrayEquals(out[j], exp[j]);
            }
            return true;
        };

        generalTest(input, expected, f, validate);
    }
    private int[][] transposeAMatrix(int[][] input) {
        for(int i = 0; i < input.length; i++) {
            for(int j = i + 1; j < input[0].length; j++) {
                swap(input, i, j, j, i);
            }
        }
        return input;
    }

    /*Search Sorted Matrix*/
    private class SearchSortedMatrixStruct {
        int[][] matrix;
        int search;

        public SearchSortedMatrixStruct(int[][] matrix, int search) {
            this.matrix = matrix;
            this.search = search;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\nMatrix: \n{\n");
            for (int i = 0; i < matrix.length; i++) {
                sb.append("\t{");
                for (int j = 0; j < matrix[0].length; j++) {
                    sb.append(matrix[i][j]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("},\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            sb.append(String.format(
                    "}\nSearch: %d",
                    search));
            return sb.toString();
        }
    }
    @Test
    public void test_searchSortedMatrix() {
        List<SearchSortedMatrixStruct> input = new ArrayList<SearchSortedMatrixStruct>();
        List<Pair<Integer, Integer>> expected = new ArrayList<Pair<Integer, Integer>>();

        input.add(new SearchSortedMatrixStruct(
                    new int[][]{
                        {0,1,2},
                        {3,4,5},
                        {6,7,8}},
                    0));
        expected.add(new Pair(0,0));

        for (int i = 1; i < 9; i++) {
            input.add(new SearchSortedMatrixStruct(input.get(0).matrix, i));
        }
        expected.add(new Pair(0,1));
        expected.add(new Pair(0,2));
        expected.add(new Pair(1,0));
        expected.add(new Pair(1,1));
        expected.add(new Pair(1,2));
        expected.add(new Pair(2,0));
        expected.add(new Pair(2,1));
        expected.add(new Pair(2,2));

        input.add(new SearchSortedMatrixStruct(input.get(0).matrix, -1));
        expected.add(null);

        input.add(new SearchSortedMatrixStruct(
                new int[][]{
                        {0,0,0},
                        {0,0,0},
                        {0,0,0},
                        {0,0,0},
                        {0,0,0}},
                0));
        expected.add(new Pair(2,1));

        input.add(new SearchSortedMatrixStruct(
                new int[][]{
                        {0,0,0},
                        {0,0,0},
                        {0,0,0},
                        {0,0,0},
                        {0,0,0}},
                -1));
        expected.add(null);

        Function<SearchSortedMatrixStruct, Object> f = (x) -> searchSortedMatrix(x.matrix, x.search);
        BiFunction<Pair, Pair, Object> validate = (x,y) -> {
            if(x != null && y != null) {
                assertEquals(x.getKey(), y.getKey());
                assertEquals(x.getValue(), y.getValue());
            } else {
                assertNull(x);
                assertNull(y);
            }
            return true;
        };

        generalTest(input, expected, f, validate);
    }

    private Pair<Integer, Integer> searchSortedMatrix(int[][] input, int searchValue) {
        if(input == null || input[0] == null) {
            return null;
        }

        int start = 0;
        int end = input.length - 1;

        while(start <= end) {
            int mid = (start + end) / 2;

            int colIndex = helperSearch(input[mid], searchValue);

            if(colIndex > -1) {
                return new Pair<>(mid, colIndex);
            } else if(input[mid][0] > searchValue) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return null;
    }

    private int helperSearch(int[] row, int target) {
        int start = 0;
        int end = row.length - 1;

        while(start <= end) {
            int mid = (start + end) / 2;
            if(row[mid] == target) {
                return mid;
            } else if (row[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return -1;
    }

    @Test
    public void test_generalTest() {
        List<Integer> input = Arrays.asList(1,2,3,4);
        List<Integer> expected = Arrays.asList(1,2,3);
        Function<Integer, Integer> f = (x) -> x;
        BiFunction<Integer, Integer, Object> validation = (x, y) -> {
            assertEquals(x, y);
            return true;
        };
        try {
            generalTest(input, expected, f, validation);
            fail("Didn't throw expected assertion error");
        } catch(AssertionError ae) {
            assertEquals(
                ae.getMessage(),
                "Test authorship error: input [4] and expected [3] list sizes differ");
        }
    }

    /*Helper Functions*/
    private void generalTest(
            List input,
            List expected,
            Function f,
            BiFunction validate) {
        if(input.size() != expected.size()) {
            throw new AssertionError(String.format(
                    "Test authorship error: input [%d] and expected [%d] list sizes differ",
                    input.size(),
                    expected.size()));
        }

        List<AssertionErrorStruct> assertionErrors = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            Object in = input.get(i);
            Object exp = expected.get(i);
            Object out = null;
            try {
                out = f.apply(in);
                validate.apply(exp, out);
                System.out.println(String.format("Pass for input: %s, out %s\n\n", in, out));
            } catch(Throwable t) {
                AssertionErrorStruct error = new AssertionErrorStruct(t, in, exp, out);
                System.out.println(
                        String.format(
                                "Fail:\n%s\n\n",
                                error.print()));
                assertionErrors.add(error);
            }
            assertTrue(assertionErrors.isEmpty());
        }
    }

    private int[][] swap(int[][] in, int x1, int y1, int x2, int y2) {
        int value1 = in[x1][y1];
        in[x1][y1] = in[x2][y2];
        in[x2][y2] = value1;
        return in;
    }
}
