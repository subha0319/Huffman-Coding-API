package com.github.subha0319.huffman_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// --- DTOs (Data Transfer Objects) for API requests and responses ---

/**
 * Represents the JSON request body for the /compress endpoint.
 * e.g., { "text": "hello world" }
 */
record CompressionRequest(String text) {}

/**
 * Represents the JSON request body for the /decompress endpoint.
 * e.g., { "encodedText": "101...", "codeTable": { "h": "101", ... } }
 */
record DecompressionRequest(String encodedText, Map<Character, String> codeTable) {}

/**
 * Represents the JSON response body for the /decompress endpoint.
 * e.g., { "text": "hello world" }
 */
record DecompressionResponse(String text) {}


// --- The Main Controller Class ---

/**
 * Controller to handle all API requests related to Huffman coding.
 * Listens for incoming HTTP requests and delegates the work to the HuffmanService.
 */
@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000") // Allow requests from our React app
@CrossOrigin(origins = "*")
public class HuffmanController {

    private final HuffmanService huffmanService;

    /**
     * Constructor-based dependency injection.
     * Spring will automatically provide an instance of HuffmanService here.
     * This is the recommended way to inject dependencies.
     *
     * @param huffmanService The service containing the compression/decompression logic.
     */
    @Autowired
    public HuffmanController(HuffmanService huffmanService) {
        this.huffmanService = huffmanService;
    }

    /**
     * Handles POST requests to /api/compress.
     * Takes a JSON object with text, compresses it, and returns the result.
     *
     * @param request The request body, automatically converted from JSON by Spring.
     * @return The result of the compression, which will be converted to JSON.
     */
    @PostMapping("/compress")
    public HuffmanService.CompressionResult compress(@RequestBody CompressionRequest request) {
        return huffmanService.compress(request.text());
    }

    /**
     * Handles POST requests to /api/decompress.
     * Takes a JSON object with the encoded data and code table, and returns the original text.
     *
     * @param request The request body, automatically converted from JSON.
     * @return A response object containing the original text.
     */
    @PostMapping("/decompress")
    public DecompressionResponse decompress(@RequestBody DecompressionRequest request) {
        String decompressedText = huffmanService.decompress(request.encodedText(), request.codeTable());
        return new DecompressionResponse(decompressedText);
    }
}
