/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs.net;

import de.timetoerror.jputils.io.DynamicByteBuffer;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 *
 * @author Jackjan
 */
public class ClientRequest
{

    // Indicator if answer was writte at least once
    private boolean written;

    // The remote address of the clients
    private final InetSocketAddress client;

    // The raw message from the client
    private final byte[] message;

    // The processing pipeline
    private final DynamicByteBuffer processing;

    // The answer that the client should receive
    private final DynamicByteBuffer answer;

    public ClientRequest(byte[] msg, SocketAddress cl) {
        processing = DynamicByteBuffer.allocate();
        answer = DynamicByteBuffer.allocate();
        message = msg;
        processing.put(msg);
        client = (InetSocketAddress) cl;
        written = false;
    }

    /**
     * Gets the IP address of the client
     * @return 
     */
    public SocketAddress getIP() {
        return client;
    }

    /**
     * Adds a byte-Array to the client answer stream
     * @param buf 
     */
    public void writeAnswer(byte[] buf) {
        written = true;
        answer.put(buf);
    }

    /**
     * Writes information to the internal processing channel
     * @param buf 
     */
    public void writeProcessing(byte[] buf) {
        processing.put(buf);
    }

    /**
     * Gets the full client message with header and body
     * @return 
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * Gets the message header of the client
     * @return
     */
    public String getMessageHeader() {
        // Header ends with 59 & 61
        for (int i = 0; i < message.length; i++) {
            if (message[i] == 0x17 && message[i + 1] == 0x17) {
                return new String(Arrays.copyOfRange(message, 0, i));
            }
        }

        return null;
    }

    /**
     * Gets the message content from the client request
     * @return 
     */
    public String getMessageContent() {
        for (int i = 0; i < message.length; i++) {
            byte b = message[i];

            if (message[i] == 0x17 && message[i + 1] == 0x17) {
                return new String(Arrays.copyOfRange(message, i + 2, message.length));
            }
        }

        return null;
    }

    /**
     * Returns the processing stream
     * @return 
     */
    public DynamicByteBuffer getProcessing() {
        return processing;
    }

    /**
     * Returns the answer stream
     * @return 
     */
    public DynamicByteBuffer getAnswer() {
        return answer;
    }

}
