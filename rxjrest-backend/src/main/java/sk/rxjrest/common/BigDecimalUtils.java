package sk.rxjrest.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class BigDecimalUtils {


	/**
	 * Converting String into BigDecimal
	 * @param stringBigDecimal is any string
	 * @return BigDecimal or null if that string is not number
	 */
	public static BigDecimal saveConvertStringToBigDecimal(String stringBigDecimal){
		
		final DecimalFormat format = new DecimalFormat();
		
	    format.setParseBigDecimal(true);
	    
	    BigDecimal result = null;
	    
		try {
			
			final Number number = format.parse(stringBigDecimal);
			
			if(format.isParseBigDecimal()){
				result = new BigDecimal(number.toString());
			}
			
		} catch (ParseException ignore) {}
		
		return result;
		
	}
	
}
