package com.github.cao.awa.lilium.network.encode.compress;

public class RequestCompressor {
    private RequestCompressorType compressor = RequestCompressorType.DEFLATE;

    public void setCompressor(RequestCompressorType compressor) {
        this.compressor = compressor;
    }

    public int id() {
        return this.compressor.id();
    }

    public byte[] compress(byte[] data) {
        return this.compressor.compressor()
                              .compress(data);
    }

    public byte[] decompress(byte[] data) {
        return this.compressor.compressor()
                              .decompress(data);
    }
}
