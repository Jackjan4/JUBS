package de.timetoerror.jabs.net;

import de.timetoerror.jabs.moduling.ModuleManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Jackjan
 */
public class MessageProcessor implements Runnable {

    private final SocketChannel chan;

    public MessageProcessor(SocketChannel chan) {
        
        this.chan = chan;
    }

    /**
     * Starts the message processing
     */
    @Override
    public void run() {

        // Getting the client request
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            while (chan.read(buf) != -1) {
            }
            
            chan.shutdownInput();

            ClientRequest req = new ClientRequest(buf.array(), chan.getRemoteAddress());

            // Processing on modules
            ModuleManager m = ModuleManager.getInstance();
            m.processInModules(req);
            
            // Write result to client
            writeChannel(chan,req.getAnswer().getBuffer());
            
            
        } catch (IOException ex) {
            System.out.println("Error:");
            ex.printStackTrace();
        }
        
        

        // Close connection to client - done :)
        closeChannel(chan);
    }
    
    
    /**
     * Closes the connection with the client.
     * @param chan 
     */
    private void closeChannel(SocketChannel chan) {
        try {
            chan.close();
        } catch (IOException ex) {
            System.out.println("Error: ");
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Writes the answer to the client
     * @param chan
     * @param buf 
     */
    private void writeChannel(SocketChannel chan, ByteBuffer buf)
    {
        try {
            buf.flip();
            chan.write(buf);
        } catch (IOException ex) {
            System.out.println("Error: ");
            ex.printStackTrace();
        }
    }

}
