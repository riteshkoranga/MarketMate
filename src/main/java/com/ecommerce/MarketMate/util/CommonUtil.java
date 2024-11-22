package com.ecommerce.MarketMate.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecommerce.MarketMate.model.productOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
    @Autowired
    private  JavaMailSender mailSender;


    public  Boolean sendMail(String url, String recipientEmail) throws UnsupportedEncodingException, MessagingException{
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom("riteshkoranga5@gmail.com","MarketMate");
        helper.setTo(recipientEmail);

        String content="<p>Dear Customer,</p>" + "<p>You have requested to reset your password.</p>"
        +"<p>Click the link below to change your password: </p>"+"<p><a href=\""+url+"\">Change Password</a></p>";
        
        helper.setSubject("Password Reset");
        helper.setText(content,true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteUrl= request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");
        
    }

    String msg=null;

    public Boolean SendMailForProductOrder(productOrder order,String status) throws UnsupportedEncodingException, MessagingException{
        msg="<p>Order <b>[[orderStatus]]</b></p>"+
    "<p>Order Details : </p>"
    +"<p><b>Name : [[userName]]</b></p>"
    +"<p>Product : [[productName]]</p>"
    +"<p>Category : [[category]]</p>"
    +"<p>Quantity : [[quantity]]</p>"
    +"<p>Price : [[price]]</p>"
    +"<p>Payemnt Type : [[paymentType]]</p>";
    
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom("riteshkoranga5@gmail.com","MarketMate");
        helper.setTo(order.getOrderAddress().getEmail());

        
        msg=msg.replace("[[orderStatus]]", status);
        msg=msg.replace("[[userName]]", order.getOrderAddress().getFirstName()+" "+order.getOrderAddress().getLastName());
        msg=msg.replace("[[productName]]", order.getProduct().getName());
        msg=msg.replace("[[category]]", order.getProduct().getCategory());
        msg=msg.replace("[[quantity]]", order.getQuantity().toString());
        msg=msg.replace("[[price]]", order.getPrice().toString());
        msg=msg.replace("[[paymentType]]", order.getPaymentType());

        
        
        helper.setSubject("Order Status");
        helper.setText(msg,true);
        mailSender.send(message);
        return true;
    }

}
