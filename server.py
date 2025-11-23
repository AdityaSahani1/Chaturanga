#!/usr/bin/env python3
import http.server
import socketserver
import os

PORT = 5000
DIRECTORY = "docs"

class MyHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def end_headers(self):
        self.send_header('Cache-Control', 'no-cache, no-store, must-revalidate')
        self.send_header('Pragma', 'no-cache')
        self.send_header('Expires', '0')
        super().end_headers()

Handler = MyHTTPRequestHandler

with socketserver.TCPServer(("0.0.0.0", PORT), Handler) as httpd:
    print(f"Serving Chaturanga documentation at http://0.0.0.0:{PORT}")
    print(f"Access the documentation to see project details and build instructions")
    httpd.serve_forever()