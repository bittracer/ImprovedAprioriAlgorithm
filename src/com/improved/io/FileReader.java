package com.improved.io;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class FileReader {

	public static final LinkedHashMap<Float, List<String>> transaction = new LinkedHashMap<Float, List<String>>();

	public static final LinkedHashMap<String, List<String>> columnList = new LinkedHashMap<String, List<String>>();

	public static final LinkedHashMap<String, String> uniqueConbination = new LinkedHashMap<String, String>();

	/**
	 * @param string
	 * @throws IOException
	 */
	public static void readFile(String string) throws IOException {

		FileInputStream fis = new FileInputStream(string);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		Float i = 0f;
		// This loop reads the data from the file & stores it in Map<Integer,
		// List<String>> transaction
		while ((line = br.readLine()) != null) {

			String[] split = line.split(",");

			List<String> _row = new ArrayList<String>();
			for (String _sp : split) {
				_row.add(_sp);
			}
			transaction.put((float) (i++ + 0.1), _row);
		}

		// This loop reads the data from Map<Integer, List<String>> transaction
		// and convert into column to store in Map<String, List<String>>
		// columnList
		for (int count = 0; count < transaction.get((float)0.1).size(); count++) {
			List<String> set = new ArrayList<String>();

			for (int column = 0; column < i; column++) {
				if (column != 0) {
					set.add(transaction.get((float)(column + 0.1)).get(count));
				}
			}
			columnList.put(transaction.get((float)0.1).get(count), set);
		}

		br.close();

		// This loop get the key value pairs of Column name and data where data
		// is the key & column name is value which will be used at the end to
		// map it
		for (Map.Entry<String, List<String>> _columnList : columnList.entrySet()) {
			for (String _list : _columnList.getValue()) {
				uniqueConbination.put(_list, _columnList.getKey());
			}
		}
	}
}
