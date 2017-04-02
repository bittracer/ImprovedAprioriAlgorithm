package com.improved.list;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrequentItemList {

	public static Map<String, Float> generateFrequentItemList(
			float minSupportCount, Map<String, Float> candidateList) {
		
		Map<String, Float> _candidateList = new HashMap<String, Float>();
		_candidateList.putAll(candidateList);

		for (Iterator<Map.Entry<String, Float>> it = _candidateList.entrySet()
				.iterator(); it.hasNext();) {

			if (it.next().getValue() < minSupportCount) {
				it.remove();
			}
		}

		return _candidateList;
	}
}
