package com.rewetest.sharedlibrary.email.utility;


import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public final class EmailUtility {
    public static final String GMAIL_COM = "gmail.com";
    public static final String YAHOO_COM = "yahoo.com";
    public static final String HOTMAIL_COM = "hotmail.com";
    private static final Random RANDOM = new Random();

    private static final String[] SUBJECTS = {
            "Urgent: Update Your Account",
            "You’ve Won a Prize!",
            "Action Required: Confirm Your Subscription",
            "Thanks for Your Purchase",
            "Important Security Alert",
            "Invitation: Exclusive Event",
            "Reminder: Upcoming Appointment",
            "Survey: We Value Your Feedback",
            "Warning: Account Suspension",
            "Notification: Policy Changes",
            "News: Updates & Highlights",
            "Welcome: Getting Started",
            "Promotion: Limited Time Offer",
            "Job Alert: New Opportunities",
            "Announcement: New Features",
    };

    private static final String[] CONTENTS = {
            "Dear user, please update your account details...",
            "Congratulations! You’ve won a prize. Claim it now...",
            "Thank you for subscribing to our service...",
            "Thanks for your recent purchase with us. Your order details...",
            "We noticed unusual activity in your account. Please verify...",
            "You are invited to an exclusive event happening in your area...",
            "This is a friendly reminder about your upcoming appointment...",
            "Please take a few moments to complete our survey...",
            "Your account is at risk of being suspended due to irregular activity...",
            "We have updated our policies. Please review the new changes...",
            "Check out the latest news, updates, and highlights from...",
            "Welcome aboard! Here’s how to get started with our service...",
            "For a limited time only, we are offering an exclusive promotion...",
            "Explore new job opportunities that match your interests...",
            "We are excited to announce new features that have been added...",
    };

    public static final Map<String, Integer> DOMAIN_PARTITION_MAP = Map.of(
            GMAIL_COM, 0,
            YAHOO_COM, 1,
            HOTMAIL_COM, 2
    );


    public static String getRandomSubject() {
        int index = RANDOM.nextInt(SUBJECTS.length);
        return SUBJECTS[index];
    }

    public static String getRandomContent() {
        int index = RANDOM.nextInt(CONTENTS.length);
        return CONTENTS[index];
    }

    public static String getRandomDomain() {
        List<String> keys = new ArrayList<>(DOMAIN_PARTITION_MAP.keySet());
        int index = RANDOM.nextInt(keys.size());
        return keys.get(index);
    }

    public static List<String> generateRandomRecipients(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomEmail())
                .collect(Collectors.toList());
    }

    public static String generateRandomEmail() {
        String domain = getRandomDomain();
        String localPart = "user" + RANDOM.nextInt(1000) + "_" + RANDOM.nextInt(1000);
        return localPart + "@" + domain;
    }

}
