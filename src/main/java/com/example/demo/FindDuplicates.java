package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.codec.language.Metaphone;

public class FindDuplicates {

  private List<String[]> users;
  private int[][] distances;
  private HashSet<String[]> nonDuplicates;
  private HashMap<String[], List<String[]>> duplicates;

  public FindDuplicates(String csv) {
    duplicates = new HashMap<>();
    nonDuplicates = new HashSet<>();
    users = new ArrayList<>();

    Scanner scanner;
    try {
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString() + "/src/main/java/com/example/demo/";
      scanner = new Scanner(new File(s + csv));
    } catch (
        FileNotFoundException e) {
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();
      throw new IllegalArgumentException(s + "could not find file.");
    }

    scanner.useDelimiter("\n");
    scanner.next();
    int count = 0;
    while (scanner.hasNext()) {
      count++;
      String[] values = scanner.next().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      users.add(values);
      nonDuplicates.add(values);
    }

    distances = new int[count][count];
    scanner.close();
  }

  // compare fields 1 by 1 with Levenshtein and Metaphone
  private int getDistance(String[] a, String[] b) {
    int sum = 0;
    Metaphone mp = new Metaphone();

    for (int i = 1; i < a.length; i++) {
      String a1 = a[i];
      String b1 = b[i];
      // if field is email, don't compare suffix
      if (i == 4) {
        a1 = a1.substring(0, a1.indexOf('@'));
        b1 = b1.substring(0, b1.indexOf('@'));
      }

      // if fields are/sound the same, don't add to sum;
      if (a1.equals(b1) || mp.isMetaphoneEqual(a1, b1)) {
        continue;
      }
      sum += Levenshtein.distance(a1, b1);
    }
    return sum;
  }

  // Finds duplicates in users
  public String findDups() {
    // This method is very slow, with more time a minhash may have been a better solution.
    for (int i = 0; i < distances.length - 1; i++) {
      for (int k = i + 1; k < distances.length - 1; k++) {
        distances[i][k] = getDistance(users.get(i), users.get(k));
        if (distances[i][k] < 40) {
          if (!duplicates.containsKey(users.get(i))) {
            duplicates.put(users.get(i), new ArrayList<>());
            nonDuplicates.remove(users.get(i));
          }
          duplicates.get(users.get(i)).add(users.get(k));
          nonDuplicates.remove(users.get(k));
        }
      }
    }
    return dupsToString();
  }

  // Returns string of results
  private String dupsToString() {
    StringBuilder output = new StringBuilder();
    int count = 1;
    for (String[] s : this.duplicates.keySet()) {
      output.append("Duplicate set ");
      output.append(count++);
      output.append('\n');
      output.append(Arrays.toString(s));
      output.append('\n');

      for (String[] dup : duplicates.get(s)) {
        output.append(Arrays.toString(dup));
        output.append('\n');
      }
      output.append('\n');
    }

    output.append("Non-duplicates\n");
    for (String[] s : nonDuplicates) {
      output.append(Arrays.toString(s));
      output.append('\n');

    }
    return output.toString();
  }

}
