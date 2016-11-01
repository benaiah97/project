package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The order request found in an eGalaxy XML. This is use for the DTI DLR Reservation Request and maps to an eGalaxy Order message.
 * 
 * @author Shannon Moon
 * 
 */
public class GWOrdersRqstTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/**
	 * the collection of order elements than can be contained in an Orders element
	 */
	private ArrayList<GWOrderTO> orderList = new ArrayList<GWOrderTO>();

	/**
	 * @return
	 */
	public ArrayList<GWOrderTO> getOrderList() {
		return orderList;
	}

	/**
	 * @param orderList
	 */
	public void setOrderList(ArrayList<GWOrderTO> orderList) {
		this.orderList = orderList;
	}

	/**
	 * Helper method.
	 * 
	 * @param to
	 */
	public void addOrder(GWOrderTO to) {
		orderList.add(to);
	}

	/**
	 * 
	 */
	public String toString() {
		StringBuffer thisTo = new StringBuffer(
				"GWOrdersRqstTO.orderList.size=" + orderList.size());
		Iterator<GWOrderTO> iter = orderList.iterator();
		while (iter.hasNext()) {
			GWOrderTO orderTo = iter.next();

			String orderID = orderTo.getOrderID();
			String orderDate = orderTo.getOrderDate();
			String orderStatus = orderTo.getOrderStatus();
			// total determined dynamically
			String orderRef = orderTo.getOrderReference();

			thisTo.append(" [Order:");
			thisTo.append(" orderID:" + orderID);
			thisTo.append(" orderDate:" + orderDate);
			thisTo.append(" orderRef:" + orderRef);
			thisTo.append(" orderStat:" + orderStatus);

			GWOrderContactTO contactTo = orderTo.getOrderContact();
			String first = contactTo.getFirstName();
			String last = contactTo.getLastName();
			String street1 = contactTo.getStreet1();
			String street2 = contactTo.getStreet2();
			String city = contactTo.getCity();
			String state = contactTo.getState();
			String zip = contactTo.getZip();
			String country = contactTo.getCountry();
			String phone = contactTo.getPhone();
			String email = contactTo.getEmail();
			thisTo.append(" (OrderContact:");
			thisTo.append(" first:" + first);
			thisTo.append(" last:" + last);
			thisTo.append(" street1:" + street1);
			thisTo.append(" street2:" + street2);
			thisTo.append(" city:" + city);
			thisTo.append(" state:" + state);
			thisTo.append(" zip:" + zip);
			thisTo.append(" country:" + country);
			thisTo.append(" phone:" + phone);
			thisTo.append(" email:" + email);
			thisTo.append(" )");

			GWOrderContactTO shipToTo = orderTo.getShipToContact();
			thisTo.append(" (Ship Contact:");
			thisTo.append(shipToTo.getEmail());
			thisTo.append(" )");

			thisTo.append(" groupVisitDate:" + orderTo.getGroupVisitDate());

			// now for order lines: ick

			Iterator<GWOrderLineTO> lineIter = orderTo.getOrderLineList()
					.iterator();
			int count = 0;
			while (lineIter.hasNext()) {
				count++;
				thisTo.append("(OrderLine " + count + ":");
				GWOrderLineTO lineTo = lineIter.next();

				// products
				if (lineTo.getDetailType().equalsIgnoreCase("1")) {
					thisTo.append(" DetailType:" + "1");
					String plu = lineTo.getPlu();
					String desc = lineTo.getDescription();
					String taxcode = lineTo.getTaxCode();
					String qty = lineTo.getQty();
					String amt = lineTo.getAmount();
					String total = lineTo.getTotal();
					String tktDate = lineTo.getTicketDate();

					thisTo.append(" plu:" + plu);
					thisTo.append(" desc:" + desc);
					thisTo.append(" taxcode:" + taxcode);
					thisTo.append(" qty:" + qty);
					thisTo.append(" amt:" + amt);
					thisTo.append(" total:" + total);
					thisTo.append(" tktDate:" + tktDate);
				}
				// payments
				else if (lineTo.getDetailType().equalsIgnoreCase("2")) {
					thisTo.append(" DetailType:" + "2");
					String payCode = lineTo.getPaymentCode();
					String payDate = lineTo.getPaymentDate();
					String desc = lineTo.getDescription();
					String endorse = lineTo.getEndorsement();
					String billzip = lineTo.getBillingZip();
					String amt = lineTo.getAmount();
					String total = lineTo.getTotal();
					String expDate = lineTo.getExpDate();
					String cvn = lineTo.getCvn();

					thisTo.append(" payCode:" + payCode);
					thisTo.append(" payDate:" + payDate);
					thisTo.append(" desc:" + desc);
					thisTo.append(" endorse:" + endorse);
					thisTo.append(" billzip:" + billzip);
					thisTo.append(" amt:" + amt);
					thisTo.append(" total:" + total);
					thisTo.append(" expDate:" + expDate);
					thisTo.append(" cvn:" + cvn);
				}
				else {
					thisTo.append(" SOMETHING IS WRONG WITH ORDERLINE DETAIL TYPE");
				}
				thisTo.append(")");
			}

			thisTo.append("]");
		}

		return thisTo.toString();
	}
}
