package pvt.disney.dti.gateway.util.quote;

import java.util.Collection;

/**
 * Draft Interface for the internal Quote Util interface.
 * @author lewit019
 *
 */
public interface QuoteUtil {

   Collection<ProductQuoteResTO> quoteProducts(Collection<ProductQuoteReqTO> productsToQuote);
   
}
