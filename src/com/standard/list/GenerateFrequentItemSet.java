package com.standard.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class GenerateFrequentItemSet {

	public static final List<String> uniqueList = new ArrayList<String>();
	public static List<String> combination = new ArrayList<String>();

	/**
	 * @param arr
	 * @param size
	 * @param itemSetCount
	 * @param index
	 * @param data
	 * @param i
	 */
	public static void getKCombination(String arr[], int size, int itemSetCount, int index, String data[], int i) {
		// Current combination is ready to be printed, print it
		if (index == itemSetCount) {

			String temp = "";
			for (int j = 0; j < itemSetCount; j++) {
				temp += data[j];
				if(j<itemSetCount-1){
					temp += ",";
				}
			}
			combination.add(temp);
			return;
		}

		// When no more elements are there to put in data[]
		if (i >= size)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		getKCombination(arr, size, itemSetCount, index + 1, data, i + 1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		getKCombination(arr, size, itemSetCount, index, data, i + 1);
	}

	/**
	 * @param arr
	 * @param n
	 * @param itemsets
	 */
	public static void getCombination(String _data[], int length, int itemsets) {
		// A temporary array to store all combination one by one
		String data[] = new String[itemsets];

		// Print all combination using temprary array 'data[]'
		getKCombination(_data, length, itemsets, 0, data, 0);
	}

	/**
	 * @param candidateList
	 */
	public static void generateUniqueSets(Map<String, Float> candidateList) {

		for (Map.Entry<String, Float> entry : candidateList.entrySet()) {
			String[] _split = entry.getKey().split(",");
			for (String _spl : _split) {
				if (!uniqueList.contains(_spl)) {
					uniqueList.add(_spl);
				}
			}
		}
	}

}