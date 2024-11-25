package com.kula.kula_project_backend.security;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class TextReviewService {
    public final PolicyFactory policy = new HtmlPolicyBuilder()
        // Allow basic text formatting elements like <b>, <i>, <u>, <strong>, and <em>
        .allowElements("b", "i", "u", "strong", "em")

        // Allow paragraph <p> and list elements <ul>, <ol>, <li> for better content readability
        .allowElements("p", "ul", "ol", "li")

        // Allow <a> elements (hyperlinks) and their href attribute
        // Ensure that only http and https protocols are allowed for URLs
        //.allowElements("a")
        //.allowAttributes("href").onElements("a")

        // Add rel="nofollow" to all links to prevent SEO manipulation or spamming
        .requireRelNofollowOnLinks()
        //.allowUrlProtocols("http", "https") // Only allow secure protocols (http, https) for href links

        // Allow <img> tags (images) with restrictions on src and alt attributes
        // Only allow http and https protocols in image src to prevent unsafe links
        .allowElements("img")
        .allowAttributes("src", "alt").onElements("img")
        .allowUrlProtocols("http", "https") // Only allow safe image URLs using http and https protocols

        // Disallow all on* event handler attributes (e.g., onclick, onload)
        // Prevent JavaScript event injections commonly used for XSS attacks
        .disallowAttributes("on*")

        // Finally, build the policy factory with these configurations
        .globally().toFactory();

}
