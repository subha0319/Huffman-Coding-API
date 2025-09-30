import React, { useState } from 'react';
import './App.css';

function App() {
  // State for the compression section
  const [textToCompress, setTextToCompress] = useState('');

  // State for the decompression section
  const [textToDecompress, setTextToDecompress] = useState('');
  const [codeTableInput, setCodeTableInput] = useState('');

  // State for results and UI feedback
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // API endpoint base URL for local development
  //const API_URL = 'http://localhost:8080/api';

  // API endpoint base URL for deployed backend
  const API_URL = 'https://huffman-coding-api.onrender.com/api';

  /**
   * Handles the compression request.
   */
  const handleCompress = async () => {
    if (!textToCompress) {
      setError('Please enter text to compress.');
      return;
    }
    setLoading(true);
    setError('');
    setResult(null);

    try {
      const response = await fetch(`${API_URL}/compress`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ text: textToCompress }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      
      const originalSize = new Blob([textToCompress]).size;
      // Each character in the encoded string is a bit, so we divide by 8 for bytes
      const compressedSize = Math.ceil(data.encodedText.length / 8); 
      const compressionRatio = originalSize > 0 ? (compressedSize / originalSize) * 100 : 0;


      setResult({
        type: 'compress',
        originalText: textToCompress,
        encodedText: data.encodedText,
        codeTable: data.codeTable,
        originalSize: originalSize,
        compressedSize: compressedSize,
        compressionRatio: compressionRatio.toFixed(2),
      });

      // Populate decompression fields for convenience
      setTextToDecompress(data.encodedText);
      setCodeTableInput(JSON.stringify(data.codeTable, null, 2));

    } catch (e) {
      setError('Failed to compress text. Make sure the backend server is running.');
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handles the decompression request.
   */
  const handleDecompress = async () => {
    let parsedCodeTable;
    if (!textToDecompress || !codeTableInput) {
      setError('Please enter the encoded text and the code table to decompress.');
      return;
    }

    try {
      parsedCodeTable = JSON.parse(codeTableInput);
    } catch (e) {
      setError('Invalid JSON in the code table. Please check the format.');
      return;
    }
    
    setLoading(true);
    setError('');
    setResult(null);

    try {
      const response = await fetch(`${API_URL}/decompress`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          encodedText: textToDecompress,
          codeTable: parsedCodeTable,
        }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setResult({
        type: 'decompress',
        decodedText: data.text,
      });

    } catch (e) {
      setError('Failed to decompress text. Make sure the backend server is running and the inputs are correct.');
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Huffman Coding Visualizer</h1>
        <p>A tool to demonstrate lossless text compression.</p>
      </header>
      <main className="container">
        <div className="io-grid">
          {/* Compression Section */}
          <div className="card">
            <h2>Compress</h2>
            <p>Enter text below to see its compressed form.</p>
            <textarea
              className="textarea"
              value={textToCompress}
              onChange={(e) => setTextToCompress(e.target.value)}
              placeholder="Type your text here..."
              rows="8"
            />
            <button className="btn" onClick={handleCompress} disabled={loading}>
              {loading ? 'Compressing...' : 'Compress'}
            </button>
          </div>

          {/* Decompression Section */}
          <div className="card">
            <h2>Decompress</h2>
            <p>Enter the encoded binary string and code table.</p>
            <textarea
              className="textarea"
              value={textToDecompress}
              onChange={(e) => setTextToDecompress(e.target.value)}
              placeholder="Encoded binary string (e.g., 10110...)"
              rows="4"
            />
            <textarea
              className="textarea"
              value={codeTableInput}
              onChange={(e) => setCodeTableInput(e.target.value)}
              placeholder='Code table in JSON format (e.g., {"a": "01", "b": "10"})'
              rows="4"
            />
            <button className="btn" onClick={handleDecompress} disabled={loading}>
              {loading ? 'Decompressing...' : 'Decompress'}
            </button>
          </div>
        </div>

        {error && <div className="card error-card"><p>{error}</p></div>}
        
        {/* Results Section */}
        {result && (
          <div className="card results-card">
            <h2>Results</h2>
            {result.type === 'compress' && (
              <div className="result-grid">
                <div>
                    <h3>Stats</h3>
                    <p><strong>Original Size:</strong> {result.originalSize} bytes</p>
                    <p><strong>Compressed Size:</strong> {result.compressedSize} bytes</p>
                    <p><strong>Compression Ratio:</strong> {result.compressionRatio}%</p>
                </div>
                <div>
                    <h3>Code Table</h3>
                    <pre className="code-block">{JSON.stringify(result.codeTable, null, 2)}</pre>
                </div>
                <div className="full-width">
                    <h3>Original Text</h3>
                    <pre className="code-block scrollable">{result.originalText}</pre>
                </div>
                <div className="full-width">
                    <h3>Encoded Binary String</h3>
                    <pre className="code-block scrollable">{result.encodedText}</pre>
                </div>
              </div>
            )}
            {result.type === 'decompress' && (
              <div>
                <h3>Decoded Text</h3>
                <pre className="code-block scrollable">{result.decodedText}</pre>
              </div>
            )}
          </div>
        )}
      </main>
      <footer className="App-footer">
        <p>Built to showcase Data Structures and Algorithms.</p>
      </footer>
    </div>
  );
}

export default App;
