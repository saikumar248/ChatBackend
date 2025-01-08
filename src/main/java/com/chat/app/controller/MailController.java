package com.chat.app.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
public class MailController {

	
	public static String generateOTP(int length) {
        // Ensure OTP length is greater than 0
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        // Characters allowed in the OTP
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        // Generate random characters for the OTP
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(digits.length());
            otp.append(digits.charAt(index));
        }

        return otp.toString();
    }
	
	@PostMapping("/sendEmailOTPforSignup/{tomail}")
	public String sendEmail(@PathVariable String tomail) {
	    String postUrl = "https://api.zeptomail.in/v1.1/email";
	    StringBuilder responseBuilder = new StringBuilder();
	    
	    
	    
	    
	    
	    String otp = generateOTP(4);
	    try {
	        // Setup the connection
	    	
	    	
	        URL url = new URL(postUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestProperty("Authorization", "Zoho-enczapikey PHtE6r0FQLjs2WUooxZT4qfsRcP3Nt96+uxjKgcU5N5DAvZQTE1WqIoolDK+qx97A6JGFvGTwYls57jIs+KCdmnrY29JVGqyqK3sx/VYSPOZsbq6x00buVoSdkfbUYPmd9Ri3STVu97SNA==");

	        // Build the request body
	        JSONObject requestBody = new JSONObject();

	        JSONObject from = new JSONObject();
	        from.put("address", "support@qtnext.com");
	        from.put("name", "Ramanasoft");
	        requestBody.put("from", from);

	        JSONObject to = new JSONObject();
	        JSONObject emailAddress = new JSONObject();
	        emailAddress.put("address", tomail);
	        to.put("email_address", emailAddress);
	        requestBody.put("to", new JSONObject[]{to});
	        
	        requestBody.put("subject", "ChatApp - OTP for Password Reset");
	        requestBody.put("htmlbody", "<html>" +
	            "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
	            "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>" +
	            "<h2 style='text-align: center; color: #4CAF50;'>ChatApp - OTP Verification</h2>" +
	            "<p>Hello,</p>" +
	            "<p>We received a request to reset your password for your ChatApp account. Please use the OTP below to proceed:</p>" +
	            "<div style='text-align: center; font-size: 24px; font-weight: bold; color: #333; padding: 10px; background: #f9f9f9; border: 1px dashed #ddd; border-radius: 5px;'>" +
	            otp +
	            "</div>" +
	            "<p>If you did not request a password reset, please ignore this email or contact our support team if you have concerns.</p>" +
	            "<p>Thank you,<br>ChatApp Team</p>" +
	            "<hr style='border: none; border-top: 1px solid #ddd;'>" +
	            "<p style='font-size: 12px; text-align: center; color: #999;'>This is an automated message. Please do not reply to this email.</p>" +
	            "</div>" +
	            "</body>" +
	            "</html>");

//
//	        requestBody.put("subject", "RS TEAM");
//	        requestBody.put("htmlbody", "Your OTP is "+otp);

	        // Send the request
	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(requestBody.toString().getBytes());
	            os.flush();
	        }

	        // Handle the response
	        int responseCode = conn.getResponseCode();
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream()
	        ));

	        String output;
	        while ((output = br.readLine()) != null) {
	            responseBuilder.append(output);
	        }

	        br.close();
	        conn.disconnect();

	        return responseCode == HttpURLConnection.HTTP_OK ? "done" : "failed with status code: " + responseCode;
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	        return otp;
	    }
	}

}
