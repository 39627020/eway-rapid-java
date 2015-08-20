package com.eway.payment.rapid.sdk.message.convert;

import org.apache.commons.lang3.StringUtils;

import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.ShippingDetails;
import com.eway.payment.rapid.sdk.beans.external.ShippingMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;

public class InternalTransToTransConverter implements BeanConverter<com.eway.payment.rapid.sdk.beans.internal.Transaction, Transaction> {

    public Transaction doConvert(com.eway.payment.rapid.sdk.beans.internal.Transaction iTransaction) throws RapidSdkException {
        Transaction transaction = new Transaction();
        transaction.setTokenCustomerID(iTransaction.getTokenCustomerID());

        Customer eWayCustomer = getEwayCustomer(iTransaction);

        if (eWayCustomer.getTokenCustomerID() == null && iTransaction.getTokenCustomerID() != null) {
            eWayCustomer.setTokenCustomerID(iTransaction.getTokenCustomerID());
        }

        transaction.setCustomer(eWayCustomer);

        transaction.setPaymentDetails(getPaymentDetails(iTransaction));
        transaction.setShippingDetails(getShippingDetails(iTransaction));
        return transaction;
    }

    private Customer getEwayCustomer(com.eway.payment.rapid.sdk.beans.internal.Transaction iTransaction) throws RapidSdkException {
        com.eway.payment.rapid.sdk.beans.internal.Customer iCustomer = iTransaction.getCustomer();
        InternalCustomerToCustomerConverter custConvert = new InternalCustomerToCustomerConverter();
        Customer customer = custConvert.doConvert(iCustomer);
        InternalTransactionToAddressConverter addressConvert = new InternalTransactionToAddressConverter();
        customer.setAddress(addressConvert.doConvert(iTransaction));
        return customer;
    }

    private PaymentDetails getPaymentDetails(com.eway.payment.rapid.sdk.beans.internal.Transaction iTransaction) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTotalAmount(iTransaction.getTotalAmount());
        paymentDetails.setInvoiceReference(iTransaction.getInvoiceReference());
        paymentDetails.setInvoiceNumber(iTransaction.getInvoiceNumber());
        return paymentDetails;
    }

    private ShippingDetails getShippingDetails(com.eway.payment.rapid.sdk.beans.internal.Transaction iTransaction) throws RapidSdkException {
        ShippingDetails shippingDetails = new ShippingDetails();
        if (iTransaction.getShippingAddress() != null) {
            InternalTransactionToAddressConverter addressConvert = new InternalTransactionToAddressConverter();
            shippingDetails.setShippingAddress(addressConvert.doConvert(iTransaction));
            String methodName = iTransaction.getShippingAddress().getShippingMethod();
            if (!StringUtils.isBlank(methodName)) {
                for (ShippingMethod m : ShippingMethod.values()) {
                    if (m.name().equalsIgnoreCase(methodName)) {
                        shippingDetails.setShippingMethod(m);
                        break;
                    }
                }
            }
        }
        if (iTransaction.getCustomer() != null) {
            shippingDetails.setEmail(iTransaction.getCustomer().getEmail());
            shippingDetails.setFax(iTransaction.getCustomer().getFax());
            shippingDetails.setLastName(iTransaction.getCustomer().getLastName());
            shippingDetails.setFirstName(iTransaction.getCustomer().getFirstName());
            shippingDetails.setPhone(iTransaction.getCustomer().getPhone());
        }
        return shippingDetails;
    }
}
