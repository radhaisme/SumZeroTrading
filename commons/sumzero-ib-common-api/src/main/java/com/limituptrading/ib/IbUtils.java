/**
 * MIT License

Copyright (c) 2015  Rob Terpilowski

Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
and associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.zerosumtrading.ib;

import com.zerosumtrading.broker.order.OrderStatus;
import com.zerosumtrading.broker.order.TradeDirection;
import com.zerosumtrading.broker.order.TradeOrder;
import com.zerosumtrading.data.FuturesTicker;
import com.zerosumtrading.data.InstrumentType;
import com.zerosumtrading.data.Ticker;
import com.zerosumtrading.marketdata.QuoteType;
import java.awt.Image;
import java.io.IOException;
import java.math.BigDecimal;
import javax.imageio.ImageIO;

/**
 *
 * @author Rob Terpilowski
 */
public class IbUtils {

    public static String translateToIbFuturesSymbol(String symbol) {
        switch (symbol) {
            case "6C":
                return "CAD";
            case "6E":
                return "EUR";
            case "6J":
                return "JPY";
            case "6S":
                return "CHF";
            case "6B":
                return "GBP";
            default:
                return symbol;
        }
    }

    public static String getOrderType(TradeOrder.Type orderType) {
        if (orderType == TradeOrder.Type.LIMIT) {
            return "LMT";
        } else if (orderType == TradeOrder.Type.MARKET) {
            return "MKT";
        } else if (orderType == TradeOrder.Type.STOP) {
            return "STP";
        } else {
            throw new IllegalStateException("Unknown order type: " + orderType);
        }
    }

    public static String getTif(TradeOrder.Duration duration) {
        if (duration == TradeOrder.Duration.GOOD_UNTIL_CANCELED) {
            return "GTC";
        } else if (duration == TradeOrder.Duration.GOOD_UTNIL_TIME) {
            return "GTD";
        } else if (duration == TradeOrder.Duration.FILL_OR_KILL) {
            return "IOC";
        } else {
            return "DAY";
        }
    }

    public static String getSecurityType(InstrumentType instrumentType) {
        if (instrumentType == InstrumentType.STOCK) {
            return "STK";
        } else if (instrumentType == InstrumentType.FOREX) {
            return "CASH";
        } else if (instrumentType == InstrumentType.FUTURES) {
            return "FUT";
        } else if (instrumentType == InstrumentType.OPTION) {
            return "OPT";
        } else if (instrumentType == InstrumentType.INDEX) {
            return "IND";
        } else {
            throw new IllegalStateException("Unknown instrument type: " + instrumentType);
        }
    }

    public static String getAction(TradeDirection direction) {
        if (direction == TradeDirection.BUY) {
            return "BUY";
        } else if (direction == TradeDirection.SELL) {
            return "SELL";
        } else if (direction == TradeDirection.SELL_SHORT) {
            return "SELL";
        } else if (direction == TradeDirection.BUY_TO_COVER) {
            return "BUY";
        } else {
            throw new IllegalStateException("Unknown trade direction: " + direction);
        }
    }

    public static QuoteType getQuoteType(int field) {
        switch (field) {
            case 0:
                return QuoteType.BID_SIZE;
            case 1:
                return QuoteType.BID;
            case 2:
                return QuoteType.ASK;
            case 3:
                return QuoteType.ASK_SIZE;
            case 4:
                return QuoteType.LAST;
            case 5:
                return QuoteType.LAST_SIZE;
            case 8:
                return QuoteType.VOLUME;
            case 9:
                return QuoteType.CLOSE;
            case 14:
                return QuoteType.OPEN;
            default:
                return QuoteType.UNKNOWN;
        }
    }

    public static OrderStatus.Status getOrderStatus(String status) {
        if ("PendingSubmit".equalsIgnoreCase(status)) {
            return OrderStatus.Status.NEW;
        } else if ("PendingCancel".equalsIgnoreCase(status)) {
            return OrderStatus.Status.PENDING_CANCEL;
        } else if ("PreSubmitted".equalsIgnoreCase(status)) {
            return OrderStatus.Status.NEW;
        } else if ("Submitted".equalsIgnoreCase(status)) {
            return OrderStatus.Status.NEW;
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            return OrderStatus.Status.CANCELED;
        } else if ("Filled".equalsIgnoreCase(status)) {
            return OrderStatus.Status.FILLED;
        } else if ("Inactive".equalsIgnoreCase(status)) {
            return OrderStatus.Status.CANCELED;
        } else {
            return OrderStatus.Status.UNKNOWN;
        }
    }

    public static String getExpiryString(int expiryMonth, int expiryYear) {
        StringBuilder sb = new StringBuilder();

        sb.append(expiryYear);

        if (expiryMonth < 10) {
            sb.append("0");
        }
        sb.append(expiryMonth);
        return sb.toString();
    }

    public static Image getIconImage() {
        try {
            return ImageIO.read(IbUtils.class.getResourceAsStream("/com/zerosumtrading/ib/ui/IB-Icon-sm.jpg"));
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static BigDecimal getIbMultiplier( Ticker ticker ) {
        if( ticker instanceof FuturesTicker ) {
            FuturesTicker futuresTicker = (FuturesTicker) ticker;
            switch( futuresTicker.getSymbol() ) {
                case "ZC":
                case "ZS":
                case "ZW":
                    return new BigDecimal( 5000 );
                default:
                    return ticker.getContractMultiplier();
                    
            }
        } 
        
        return ticker.getContractMultiplier();
    }
    
}