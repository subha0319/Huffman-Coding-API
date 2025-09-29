package com.github.subha0319.huffman_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HuffmanServiceTest {

    private HuffmanService huffmanService;

    @BeforeEach
    void setUp() {
        huffmanService = new HuffmanService();
    }

    @Test
    void testCompressionAndDecompression_NormalText() {
        String text = "hello world";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        String decompressedText = huffmanService.decompress(result.encodedText(), result.codeTable());
        assertEquals(text, decompressedText);
    }

    @Test
    void testCompressionAndDecompression_EmptyText() {
        String text = "";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        String decompressedText = huffmanService.decompress(result.encodedText(), result.codeTable());
        assertEquals(text, decompressedText);
        assertTrue(result.encodedText().isEmpty());
        assertTrue(result.codeTable().isEmpty());
    }
    
    @Test
    void testCompressionAndDecompression_SingleCharacter() {
        String text = "aaaaa";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        String decompressedText = huffmanService.decompress(result.encodedText(), result.codeTable());
        assertEquals(text, decompressedText);
        assertEquals("00000", result.encodedText());
    }

    @Test
    void testCompressionAndDecompression_UniqueCharacters() {
        String text = "abcdefg";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        String decompressedText = huffmanService.decompress(result.encodedText(), result.codeTable());
        assertEquals(text, decompressedText);
    }
    
    @Test
    void testCompressionAndDecompression_WithSpecialCharactersAndSpaces() {
        String text = "This is a test with special characters!@#$%^&*() and spaces.";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        String decompressedText = huffmanService.decompress(result.encodedText(), result.codeTable());
        assertEquals(text, decompressedText);
    }

    @Test
    void testDecompression_WithInvalidCode() {
        String text = "hello";
        HuffmanService.CompressionResult result = huffmanService.compress(text);
        
        // Introduce an invalid bit sequence that doesn't map to any character
        String invalidEncodedText = result.encodedText() + "1111"; // Assuming '1111' is not a valid code
        
        String decompressedText = huffmanService.decompress(invalidEncodedText, result.codeTable());
        
        // The decompression should stop when it can't find a match.
        // It should decompress the valid part of the string.
        assertTrue(text.startsWith(decompressedText));
    }

    @Test
    void testDecompression_WithEmptyCodeTable() {
        String decompressedText = huffmanService.decompress("101010", Map.of());
        assertEquals("", decompressedText);
    }
}
```

/* How to Run the Tests:

1.  Make sure your backend server is **stopped** if it's currently running.
2.  In the terminal window that is in your `backend` directory, run the following command:
    * On macOS or Linux:
        ```bash
        ./mvnw test
        ```
    * On Windows:
        ```bash
        mvnw.cmd test 
*/      
