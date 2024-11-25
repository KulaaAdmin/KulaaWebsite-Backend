package com.kula.kula_project_backend.security;

import org.junit.jupiter.api.Test;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TextReviewServiceTest {

    @Autowired
    private TextReviewService textReviewService; // Assuming your service is named this

    @Test
    public void testSanitizeDisallowedHtml() {
        // Test case with disallowed HTML elements
        String input = "<script>alert('XSS');</script><img src='test.jpg'>";
        String expected = "<img src=\"test.jpg\" />"; // Adjust to handle self-closing img tag
        String output = textReviewService.policy.sanitize(input);
        assertEquals(expected, output);
    }

    @Test
    public void testSanitizeEventHandlers() {
        // Test case with event handlers
        String input = "<div onclick=\"alert('XSS')\">Click me</div>";
        String expected = "Click me"; // Expect just the text since div is removed
        String output = textReviewService.policy.sanitize(input);
        assertEquals(expected, output);
    }

    @Test
    public void testSanitizeImgWithAllowedProtocol() {
        // Test case with allowed image protocol
        String input = "<img src=\"http://example.com/image.jpg\">";
        String expected = "<img src=\"http://example.com/image.jpg\" />"; // Adjust to handle self-closing img tag
        String output = textReviewService.policy.sanitize(input);
        assertEquals(expected, output);
    }


}
