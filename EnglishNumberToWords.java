

import java.text.DecimalFormat;

public class EnglishNumberToWords {
	private static final String[] tensNames = {
		    "",
		    " ten",
		    " twenty",
		    " thirty",
		    " forty",
		    " fifty",
		    " sixty",
		    " seventy",
		    " eighty",
		    " ninety"
		  };

		  private static final String[] numNames = {
		    "",
		    " one",
		    " two",
		    " three",
		    " four",
		    " five",
		    " six",
		    " seven",
		    " eight",
		    " nine",
		    " ten",
		    " eleven",
		    " twelve",
		    " thirteen",
		    " fourteen",
		    " fifteen",
		    " sixteen",
		    " seventeen",
		    " eighteen",
		    " nineteen"
		  };

		  private EnglishNumberToWords() {}

		  private static String convertLessThan100(int number) {
			    String soFar;

			    if (number % 100 < 20){
			      soFar = numNames[number % 100];
			      number /= 100;
			    }
			    else {
			      soFar = numNames[number % 10];
			      number /= 10;

			      soFar = tensNames[number % 10] + soFar;
			      number /= 10;
			    }
			    if (number == 0) return soFar;
			    return numNames[number] + " hundred" + soFar;
			  }
		  
		  public static String convert(long number) {
			    // 0 to 99,99,99,999
			    if (number == 0) { return "zero"; }

			    String snumber = Long.toString(number);

			    // pad with "0"
			    String mask = "000000000";
			    DecimalFormat df = new DecimalFormat(mask);
			    snumber = df.format(number);

			    // XXnnnnnnn
			    int crores = Integer.parseInt(snumber.substring(0,2));
			    // nnXXnnnnn
			    int lacs  = Integer.parseInt(snumber.substring(2,4));
			    // nnnnXXnnn
			    int hundredThousands = Integer.parseInt(snumber.substring(4,6));
			    // nnnnnnXXX
			    int thousands = Integer.parseInt(snumber.substring(6,9));

			    String rsCrores;
			    switch (crores) {
			    case 0:
			    	rsCrores = "";
			      break;
			    case 1 :
			    	rsCrores = convertLessThan100(crores)
			      + " crore ";
			      break;
			    default :
			    	rsCrores = convertLessThan100(crores)
			      + " crore ";
			    }
			    String result =  rsCrores;

			    String rsLacs;
			    switch (lacs) {
			    case 0:
			    	rsLacs = "";
			      break;
			    case 1 :
			    	rsLacs = convertLessThan100(lacs)
			         + " lakh ";
			      break;
			    default :
			    	rsLacs = convertLessThan100(lacs)
			         + " lakh ";
			    }
			    result =  result + rsLacs;

			    String rsHundredThousands;
			    switch (hundredThousands) {
			    case 0:
			    	rsHundredThousands = "";
			      break;
			    case 1 :
			    	rsHundredThousands = "one thousand ";
			      break;
			    default :
			    	rsHundredThousands = convertLessThan100(hundredThousands)
			         + " thousand ";
			    }
			    result =  result + rsHundredThousands;

			    String tradThousand;
			    tradThousand = convertLessThan100(thousands);
			    result =  result + tradThousand;

			    // remove extra spaces!
			    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
			  }
}
