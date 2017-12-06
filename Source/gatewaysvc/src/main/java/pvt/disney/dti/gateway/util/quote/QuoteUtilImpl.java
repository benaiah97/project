/**
 * 
 */
package pvt.disney.dti.gateway.util.quote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO:  Implement this for real.
 * Mock-up draft to access the quote service.
 * @author lewit019
 *
 */
public class QuoteUtilImpl implements QuoteUtil {

   /**
    * Mock-up to return the prices
    */
   public Collection<ProductQuoteResTO> quoteProducts(Collection<ProductQuoteReqTO> productsToQuote) {
      
      ArrayList<ProductQuoteResTO> productQuotes = new ArrayList<ProductQuoteResTO>();
      
      for (ProductQuoteReqTO aQuoteReq:productsToQuote) {
         
         ProductQuoteResTO aQuoteRes = new ProductQuoteResTO();
         aQuoteRes.setProdCode(aQuoteReq.getProdCode());
         aQuoteRes.setTktValidityValidStart(aQuoteReq.getTktValidityValidStart());
         aQuoteRes.setTktValidityValidEnd(aQuoteReq.getTktValidityValidEnd());
         aQuoteRes.setGatePrice(new BigDecimal("100.01"));
         aQuoteRes.setGateTax(new BigDecimal("10.01"));
         aQuoteRes.setNetPrice(new BigDecimal("90.09"));
         aQuoteRes.setNetTax(new BigDecimal("9.09"));
         aQuoteRes.setQuoteToken("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vehicula eu odio vitae pulvinar.");
         
         productQuotes.add(aQuoteRes);
         
      }
      
      return productQuotes;
   }
   
}
