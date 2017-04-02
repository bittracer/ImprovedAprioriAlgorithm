package com.improved.list;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.improved.io.FileReader;

public class NextCandidateList {

	public static LinkedHashMap<String, Float> getCandidateList(List<String> _combination, int itemSetSize) {

		LinkedHashMap<String, Float> _candidateList = new LinkedHashMap<String, Float>();
		LinkedHashMap<Float, List<String>> transaction = FileReader.transaction;
		String[] _kCombination = new String[_combination.size()];
		_kCombination = _combination.toArray(_kCombination);
		
		
		float count = 0;
		float acutalCount = 0;
		for (int i = 0; i < _kCombination.length; i++) {
			for (float j = 0; j < transaction.size(); j++) {
				for (int k = 0; k < itemSetSize; k++) {
					if(transaction.get(j+(float)0.1)==null){
						break;
					}else if (transaction.get(j+(float)0.1).contains(_kCombination[i].split(",")[k])) {
						count++;
					}
				}
				if (count == itemSetSize) {
					acutalCount++;
				}
				count = 0;
			}
			_candidateList.put(_kCombination[i], (acutalCount/ transaction.size()));
			acutalCount = 0;
		}
		
		return _candidateList;
	}
}
