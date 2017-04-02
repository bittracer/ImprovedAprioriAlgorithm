package com.standard.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.standard.io.FileReader;
import com.standard.model.RuleModel;

/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class GenerateRules {

	public static List<String> combination = new ArrayList<String>();

	public static List<RuleModel> _rules = new ArrayList<RuleModel>();

	public static Map<String, Float> frequentItemSet = new HashMap<String, Float>();

	/**
	 * @param arr
	 * @param size
	 * @param itemSetCount
	 * @param index
	 * @param data
	 * @param i
	 */
	public static void splitRecursiveCombination(String arr[], int size, int itemSetCount, int index, String data[], int i) {
		
		// Save current combination
		if (index == itemSetCount) {

			String temp = "";
			for (int j = 0; j < itemSetCount; j++) {
				temp += data[j];
				if (j < itemSetCount - 1) {
					temp += ",";
				}
			}
			combination.add(temp);
			return;
		}

		//No elements then return
		if (i >= size)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		splitRecursiveCombination(arr, size, itemSetCount, index + 1, data, i + 1);

		// excluded current & replace it with next (Note: i+1 is passed, but index is not changed)
		splitRecursiveCombination(arr, size, itemSetCount, index, data, i + 1);
	}

	/**
	 * @param arr
	 * @param n
	 * @param itemsets
	 */
	static void generateSubSets(String arr[], int n, int itemsets) {
		// A temporary array to store all combination one by one
		String data[] = new String[itemsets];

		// Print all combination using temporary array
		splitRecursiveCombination(arr, n, itemsets, 0, data, 0);
	}

	/**
	 * @param combination
	 * @param _subSet
	 * @param minConfidence
	 */
	//This function generate the rules & store it 
	static void generateRules(List<String> combination, Map.Entry<String, Float> _subSet, float minConfidence) {

		float confidence = 0;
		for (String _comb : combination) {

			// use S -> (I-S) to get remaining items
			String remaining = getRemainingItemsFromItemSet(Arrays.asList(_subSet.getKey().toString().split(",")), _comb);

			// Create the model which will store the output data (i.e. Rules )
			RuleModel _model = new RuleModel();
			
			if (frequentItemSet.get(_comb) == null) {
				if (_comb.split(",").length == 3) {
					if (frequentItemSet.get(_comb.toString().split(",")[1] + "," + _comb.toString().split(",")[0] + ","
							+ _comb.toString().split(",")[2]) != null) {
						confidence=frequentItemSet.get(_subSet.getKey())
								/ frequentItemSet.get(_comb.toString().split(",")[1] + ","
										+ _comb.toString().split(",")[0] + "," + _comb.toString().split(",")[2]);

					} else if (frequentItemSet.get(_comb.toString().split(",")[2] + "," + _comb.toString().split(",")[0]
							+ "," + _comb.toString().split(",")[1]) != null) {
						confidence=frequentItemSet.get(_subSet.getKey())
								/ frequentItemSet.get(_comb.toString().split(",")[2] + ","
										+ _comb.toString().split(",")[0] + "," + _comb.toString().split(",")[1]);

					} else if (frequentItemSet.get(_comb.toString().split(",")[0] + "," + _comb.toString().split(",")[2]
							+ "," + _comb.toString().split(",")[1]) != null) {
						confidence=frequentItemSet.get(_subSet.getKey())
								/ frequentItemSet.get(_comb.toString().split(",")[0] + ","
										+ _comb.toString().split(",")[2] + "," + _comb.toString().split(",")[1]);

					} else if (frequentItemSet.get(_comb.toString().split(",")[2] + "," + _comb.toString().split(",")[1]
							+ "," + _comb.toString().split(",")[0]) != null) {
						confidence=frequentItemSet.get(_subSet.getKey())
								/ frequentItemSet.get(_comb.toString().split(",")[2] + ","
										+ _comb.toString().split(",")[1] + "," + _comb.toString().split(",")[0]);

					}
				} else if(_comb.split(",").length == 2){
					if (frequentItemSet.get(_comb) != null) {
						confidence=frequentItemSet.get(_subSet.getKey()) / frequentItemSet.get(_comb);
					} else {
						confidence = frequentItemSet.get(_subSet.getKey()) / frequentItemSet
								.get(_comb.toString().split(",")[1] + "," + _comb.toString().split(",")[0]);
					}
				}

			} else {
				confidence=frequentItemSet.get(_subSet.getKey()) / frequentItemSet.get(_comb);
			}
			
			// If confidence>=minConfidence then only we are supposed to select the rule else it is discarded
			if(confidence>=minConfidence){
				_model.setSupportCount(_subSet.getValue());
				_model.setLhs(getFormattedData(_comb));
				_model.setRhs(getFormattedData(remaining));
				_model.setConfidence(confidence);
				_rules.add(_model);
			}
			
		}
	}

	/**
	 * @param _firstSet
	 * @param _comb
	 * @return
	 */
	// It gives the remaining item set using the rule S -> (I-S), where S & I are non-empty subsets 
	static String getRemainingItemsFromItemSet(List<String> _firstSet, String _comb) {

		List<String> _combList = new LinkedList<String>(Arrays.asList(_comb.split(",")));
		List<String> _firstSet1 = new LinkedList<String>(_firstSet);
		String remaining="";

		for (String _outer : _firstSet) {
			for (String _inner : _combList) {
				if (_outer.equals(_inner)) {
					_firstSet1.remove(_outer);
					break;
				}
			}
		}
		
		for(int i=0;i<_firstSet1.size();i++){
			
			remaining += _firstSet1.get(i);
			if(i<_firstSet1.size()-1){
				remaining += ",";
			}
		}
		
		return remaining;
	}

	/**
	 * No Param
	 */
	// Combines all the frequent item set in a Map 
	static void generateListToCalculateConfidence() {

		for (Map.Entry<Integer, Map<String, Float>> _frequentSet : StandardApriori.frequentItemSet.entrySet()) {
			for (Map.Entry<String, Float> _set : _frequentSet.getValue().entrySet()) {
				frequentItemSet.put(_set.getKey(), _set.getValue());
			}
		}
	}
	
	/**
	 * @param combination
	 * @return
	 */
	// Format the data to display it in beautify & readable manner
	static String getFormattedData(String combination){
		
		String[] _split = combination.split(",");
		String data ="{";
		for(String _entity:_split){
			data += " "+FileReader.uniqueConbination.get(_entity) + " = " + _entity +" ";
		}
		
		data += "}";
		return data;
	}
}
