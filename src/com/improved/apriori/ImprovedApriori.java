package com.improved.apriori;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import com.improved.io.FileReader;
import com.improved.list.FirstCandidateList;
import com.improved.list.FrequentItemList;
import com.improved.list.GenerateFrequentItemSet;
import com.improved.list.NextCandidateList;
import com.improved.model.RuleModel;

/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class ImprovedApriori {

	// This will Store the details for each and every Candidate Item set
	public static Map<Integer, Map<String, Float>> candidateList = new HashMap<Integer, Map<String, Float>>();

	// This will Store the details for each and every Frequent Item set
	public static Map<Integer, Map<String, Float>> frequentItemSet = new HashMap<Integer, Map<String, Float>>();

	static String filename = "";
	static int k = 1;
	static float minSupportCount;
	static float minConfidence;

	static Scanner in = new Scanner(System.in);
	
	public ImprovedApriori(String filename,float minSupportCount,float minConfidence){
		ImprovedApriori.filename = filename;
		ImprovedApriori.minSupportCount = minSupportCount;
		ImprovedApriori.minConfidence = minConfidence;
	}

	/**
	 * @param ar
	 */
	public void initiateImprovedApriori() {

		try {

			// Read the file in appropriate format
			FileReader.readFile(filename);

			// Read Column vice data (Column)
			Map<String, List<String>> _Columnlist = FileReader.columnList;

			// Find 1st Candidate List
			Map<String, Float> _cList = FirstCandidateList.generateFirstCandidateList(_Columnlist);
			candidateList.put(k, _cList);

			// Generate Frequent Item Set
			Map<String, Float> _fList = FrequentItemList.generateFrequentItemList(minSupportCount,
					candidateList.get(k));
			frequentItemSet.put(k, _fList);

			/*
			 * GENERATE CANDIDATE ITEM SETS & FREQUENT ITEM SETS FROM K=2 TO N
			 */
			for (int K = 2; !frequentItemSet.get(K - 1).isEmpty(); K++) {

				// Generate Unique List
				GenerateFrequentItemSet.generateUniqueSets(frequentItemSet.get(K - 1));
				List<String> _data = GenerateFrequentItemSet.uniqueList;

				// Get Combination of itemset based on K = 2,3,...
				String[] uniquelist = _data.toArray(new String[_data.size()]);
				GenerateFrequentItemSet.getCombination(uniquelist, uniquelist.length, K);
				
				LinkedHashMap<Float,List<String>> _temp = new LinkedHashMap<Float,List<String>>();
				
				// Set Deletion mark to 0
				for (Map.Entry<Float, List<String>> transaction : FileReader.transaction.entrySet()) {
					
					// Exclude Header
					if(transaction.getKey().equals((float)0.1)){
						continue;
					}
					int containsCount = 0;
					for (Map.Entry<String, Float> frequentlist : frequentItemSet.get(K - 1).entrySet()) {
						// Take item set from Transaction and check in frequent item list	
						for(String _str:frequentlist.getKey().split(",")){
							if(transaction.getValue().contains(_str)){
								containsCount++;
								break;
							}	
						}
						if(containsCount > 0){
							break;
						}
					}
					if(containsCount == 0){
					  // This means the item set is infrequent
						_temp.put(transaction.getKey(), transaction.getValue());
					}
				}

				// This loop will actually delete the original details and replace with new once
				for(Map.Entry<Float,List<String>> _remap:_temp.entrySet()){
					FileReader.transaction.put((float)(_remap.getKey().intValue()),FileReader.transaction.remove(_remap.getKey()));
				}
				
				_temp.clear();
				// Add it to Candidate List
				Map<String, Float> _candidateList = NextCandidateList
						.getCandidateList(GenerateFrequentItemSet.combination, K);
				candidateList.put(++k, _candidateList);

				// Generate Frequent Item Set
				Map<String, Float> frequentItemList = FrequentItemList.generateFrequentItemList(minSupportCount,
						candidateList.get(k));
				frequentItemSet.put(k, frequentItemList);

				// Clear the list to store the details for next iteration
				GenerateFrequentItemSet.combination.clear();
				GenerateFrequentItemSet.uniqueList.clear();

			}

			/* GENERATE RULES BASED OF FREQUENT ITEM SETS */

			//Combines all the frequent item set in a Map
			GenerateRules.generateListToCalculateConfidence();

			// Generate Subset & Rules for each and every frequent item set
			// where supportCount >= minSupport && confidence >= minimum
			// confidence
			for (int i = 2; i < frequentItemSet.size(); i++) {
				// Get the frequent item set with 2,3,4,... subsets
				for (Map.Entry<String, Float> _freq : frequentItemSet.get(i).entrySet()) {
					// Generate Subsets

					for (int j = i - 1; j >= 1; j--) {
						GenerateRules.generateSubSets(_freq.getKey().split(","), _freq.getKey().split(",").length, j);
					}

					List<String> combination = GenerateRules.combination;

					// Generate the rules for each and every frequent item set
					// combination
					GenerateRules.generateRules(combination, _freq, minConfidence);

					// Clear the List
					GenerateRules.combination.clear();
				}
			}

			// Write Data to files
			writeDataToFile();

		} catch (Exception e) {
			if(e.getMessage().contains("No such file")){
				System.out.println("File not found. Retry Again !");
			}else{
				System.out.println("Invalid Error. Retry Again !");
			}
		}
	}

	/**
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	// Output the data to files
	static void writeDataToFile() throws FileNotFoundException, UnsupportedEncodingException {

		// Create the file to store the output (E.g. File name will be like
		// Rules_For_data1.txt)
		File file = new File("Rules_For_" + filename.split("\\.")[0] + ".txt");
		PrintWriter writer = new PrintWriter(file, "UTF-8");

		// Write data to file
		writer.println("Summary:");
		writer.println("Total rows in the original set:" + (FileReader.transaction.size() - 1));
		writer.println("Total rules discovered:" + GenerateRules._rules.size());
		writer.println("The selected measures: Support=" + minSupportCount + " Confidence=" + minConfidence);
		writer.println("-------------------------------------------------------\n");
		writer.println("Rules:\n");

		int ruleCount = 1;

		// Write data to file
		for (RuleModel _model : GenerateRules._rules) {

			writer.println("Rule#" + ruleCount++ + ": (Support=" + _model.getSupportCount() + ", Confidence="
					+ _model.getConfidence() + ")");
			writer.println(_model.getLhs());
			writer.println("------>" + _model.getRhs());
			writer.println("\n");
		}
		System.out.println("Rules are generated in file named:Rules_For_" + filename.split("\\.")[0] + ".txt");
		writer.close();
	}
}
