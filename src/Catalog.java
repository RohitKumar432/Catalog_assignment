import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;

public class Catalog {

    // Function to decode the Y values from different bases to decimal
    public static BigInteger decodeValue(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return new BigInteger(value, baseInt);
    }

    // Function to perform Lagrange interpolation and find the constant term 'c'
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;
        for (Map.Entry<Integer, BigInteger> iEntry : points.entrySet()) {
            int xi = iEntry.getKey();
            BigInteger yi = iEntry.getValue();
            BigInteger li = BigInteger.ONE;
            BigInteger xiProd = BigInteger.ONE;

            for (Map.Entry<Integer, BigInteger> jEntry : points.entrySet()) {
                if (iEntry.equals(jEntry)) continue;
                int xj = jEntry.getKey();
                // Calculate the Lagrange basis polynomial
                li = li.multiply(BigInteger.valueOf(-xj)).divide(BigInteger.valueOf(xi - xj));
                xiProd = xiProd.multiply(BigInteger.valueOf(xi - xj));
            }
            result = result.add(yi.multiply(li).mod(xiProd)).mod(xiProd);
        }
        return result;
    }

    // Function to read the JSON input and solve for the constant term 'c'
    public static BigInteger solve(JSONObject input) {
        JSONObject keys = input.getJSONObject("keys");
        int n = keys.getInt("n");  // Number of points
        int k = keys.getInt("k");  // Required number of points (k = m + 1)

        Map<Integer, BigInteger> points = new HashMap<>();

        // Parse and decode the points
        for (String key : input.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject point = input.getJSONObject(key);
            String base = point.getString("base");
            String value = point.getString("value");
            BigInteger yDecoded = decodeValue(base, value);
            points.put(Integer.parseInt(key), yDecoded);
        }

        // Use Lagrange interpolation to find the constant term 'c'
        return lagrangeInterpolation(points, k);
    }

    public static void main(String[] args) {
        // Test Case 1
        String testCase1 = """
        {
            "keys": {
                "n": 4,
                "k": 3
            },
            "1": {
                "base": "10",
                "value": "4"
            },
            "2": {
                "base": "2",
                "value": "111"
            },
            "3": {
                "base": "10",
                "value": "12"
            },
            "6": {
                "base": "4",
                "value": "213"
            }
        }
        """;

        // Test Case 2
        String testCase2 = """
        {
            "keys": {
                "n": 10,
                "k": 7
            },
            "1": {
                "base": "6",
                "value": "13444211440455345511"
            },
            "2": {
                "base": "15",
                "value": "aed7015a346d63"
            },
            "3": {
                "base": "15",
                "value": "6aeeb69631c227c"
            },
            "4": {
                "base": "16",
                "value": "e1b5e05623d881f"
            },
            "5": {
                "base": "8",
                "value": "316034514573652620673"
            },
            "6": {
                "base": "3",
                "value": "2122212201122002221120200210011020220200"
            },
            "7": {
                "base": "3",
                "value": "20120221122211000100210021102001201112121"
            },
            "8": {
                "base": "6",
                "value": "20220554335330240002224253"
            },
            "9": {
                "base": "12",
                "value": "45153788322a1255483"
            },
            "10": {
                "base": "7",
                "value": "1101613130313526312514143"
            }
        }
        """;

        // Parse the test cases
        JSONObject test1 = new JSONObject(testCase1);
        JSONObject test2 = new JSONObject(testCase2);

        // Solve both test cases
        BigInteger result1 = solve(test1);
        BigInteger result2 = solve(test2);

        // Print the results
        System.out.println("Constant term (c) for Test Case 1: " + result1);
        System.out.println("Constant term (c) for Test Case 2: " + result2);
    }
}
