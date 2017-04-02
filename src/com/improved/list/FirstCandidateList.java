package com.improved.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.improved.io.FileReader;


/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class FirstCandidateList {

	static final List<String> uniqueItemSet = new ArrayList<String>();

	/**
	 * @param _columnList
	 * @return
	 */
	// Generates the first candidate list
	public static Map<String, Float> generateFirstCandidateList(
			Map<String, List<String>> _columnList) {

		Float numberOfTrans = 0f;
		Float occurances = 0f;
		// For Storing CandidateItemSet
		Map<String, Float> candidateItemset = new HashMap<String, Float>();

		// Get List of Unique Item Set from original data
		List<String> uniqueItemSet = findUniqueItemSet(_columnList);

		// Calculate Count for each uniqueItemSet
		for (String uqItemSet : uniqueItemSet) {

			for (Map.Entry<Float, List<String>> entry : FileReader.transaction.entrySet()) {
				numberOfTrans++;
				if (entry.getKey() != 0) {// Exclude headers
					if (entry.getValue().contains(uqItemSet)) {
						for (String _list : entry.getValue()) {
							if (_list.equals(uqItemSet)) {
								occurances++;
							}
						}
					}
				}
			}
			
			candidateItemset.put(uqItemSet, (occurances / (numberOfTrans - 1)));
			occurances = 0f;
			numberOfTrans = 0f;
		}

		return candidateItemset;
	}

	/**
	 * @param _columnList
	 * @return
	 */
	//This function calculates the unique item sets from Map<String, List<String>> columnList & other frequent item sets
	public static List<String> findUniqueItemSet(
			Map<String, List<String>> _columnList) {

		for (Entry<String, List<String>> entry : _columnList.entrySet()) {

			List<String> tempis = entry.getValue();
			for (int count = 0; count < tempis.size(); count++) {
				if (!uniqueItemSet.contains(tempis.get(count))) {
					uniqueItemSet.add(tempis.get(count));
				}
			}
		}
		return uniqueItemSet;
	}

}